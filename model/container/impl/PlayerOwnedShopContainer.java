package com.ikov.model.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.ikov.engine.task.TaskManager;
import com.ikov.engine.task.impl.ShopRestockTask;
import com.ikov.model.GameMode;
import com.ikov.model.Item;
import com.ikov.model.Skill;
import com.ikov.model.container.ItemContainer;
import com.ikov.model.container.StackType;
import com.ikov.model.definitions.ItemDefinition;
import com.ikov.model.input.impl.EnterAmountToBuyFromShop;
import com.ikov.model.input.impl.EnterAmountToSellToShop;
import com.ikov.model.input.impl.EnterAmountOfPosOfferPrice;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.PlayerLogs;
import com.ikov.world.entity.impl.player.Player;
import com.ikov.world.content.pos.PosOffers;
import com.ikov.world.content.pos.PlayerOwnedShops;

/**
 * Handles the Player Owned Shop Containers
 * @author Jonathan Sirens
 */

public class PlayerOwnedShopContainer extends ItemContainer {

	public PlayerOwnedShopContainer(Player player, String name, Item[] stockItems) {
		super(player);
		this.name = name + "'s store";
		this.index = getIndex(name);
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	private int index;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getIndex() {
		return this.index;
	}

	public String getName() {
		return name;
	}
	
	public static int getIndex(String name) {
		return PlayerOwnedShops.getIndex(name);
	}

	public PlayerOwnedShopContainer setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Opens a shop for a player
	 * @param player	The player to open the shop for
	 * @return			The shop instance
	 */
	public PlayerOwnedShopContainer open(Player player, String owner) {
		setPlayer(player);
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setPlayerOwnedShop(PlayerOwnedShopManager.getShops().get(getIndex(owner))).setInterfaceId(INTERFACE_ID).setPlayerOwnedShopping(true);
		refreshItems();
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		PlayerOwnedShopContainer publicShop = PlayerOwnedShopManager.getShops().get(index);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if (player.getPlayerOwnedShop() != null && player.getPlayerOwnedShop().index == index && player.isPlayerOwnedShopping())
				player.getPlayerOwnedShop().setItems(publicShop.getItems());
		}
	}
	
	public void sellItem(Player player, int slot, int amountToSell, long price) {
		this.setPlayer(player);
		if(!player.isPlayerOwnedShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if(!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if(this.full(itemToSell.getId()))
			return;
		if(player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());
		if(amountToSell == 0)
			return;
		int itemId = itemToSell.getId();
		int count = 0;
		boolean inventorySpace = false;
		if(!itemToSell.getDefinition().isStackable()) {
			if(!player.getInventory().contains(995))
				inventorySpace = true;
		}
		if(player.getInventory().getFreeSlots() <= 0 && player.getInventory().getAmount(995) > 0)
			inventorySpace = true;
		if(player.getInventory().getFreeSlots() > 0 || player.getInventory().getAmount(995) > 0)
			inventorySpace = true;
		PosOffers o = PlayerOwnedShops.SHOPS[index];
		if (o == null)
			return;
		
		if (!player.getUsername().equalsIgnoreCase(o.getOwner())) {
			player.getPacketSender().sendMessage("You can't sell items to this shop.");
			return;
		}
		
		for (int q = 0; q < o.getOffers().size(); q++) {
			if (o.getOffers().get(q).getItemId() == itemId) {
				price = o.getOffers().get(q).getPrice();
			}
		}
		
		if (price < 0)
			return;
		if (price == 0) {
			player.setHasNext(true);
			player.setInputHandling(new EnterAmountOfPosOfferPrice(slot, amountToSell)); //Enter the price of the item
			player.getPacketSender().sendEnterAmountPrompt("Enter the price of the item:");
			return;
		}
			
		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if(this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId()) || !player.isPlayerOwnedShopping())
				break;
			if(!itemToSell.getDefinition().isStackable()) {
				if(inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					//player.getInventory().add(new Item(995, itemValue), false);
					PlayerOwnedShops.soldItem(player, index, itemToSell.getId(), amountToSell, price);
					player.save();
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if(inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					//player.getInventory().add(new Item(995, itemValue * amountToSell), false);
					PlayerOwnedShops.soldItem(player, index, itemToSell.getId(), amountToSell, price);
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
			count++;
		}
		player.getInventory().refreshItems();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public PlayerOwnedShopContainer switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if(player == null)
			return this;
		if(!player.isPlayerOwnedShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if(amountBuying == 0)
			return this;
		boolean usePouch = false;
		int playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		String currencyName = "";
		playerCurrencyAmount = player.getInventory().getAmount(995);
		PlayerLogs.log(player.getUsername(), "Player has bought the store item: "+item.getDefinition().getName()+" ("+item.getId()+"), amount: " + amountBuying);
		currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
		if(player.getMoneyInPouch() >= value) {
			playerCurrencyAmount = player.getMoneyInPouchAsInt();
			if(!(player.getInventory().getFreeSlots() == 0 && player.getInventory().getAmount(995) == value)) {
				usePouch = true;
			}
		}
		if(value <= 0) {
			return this;
		}
		if(!hasInventorySpace(player, item, 995, value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}
		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender().sendMessage("You do not have enough " + ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s"))) + " to purchase this item.");
			return this;
		}

		for (int i = amountBuying; i > 0; i--) {
			if(!item.getDefinition().isStackable()) {
				if(playerCurrencyAmount >= value && hasInventorySpace(player, item, 995, value)) {
					if(usePouch) {
						player.setMoneyInPouch((player.getMoneyInPouch() - value));
					} else {
						player.getInventory().delete(currency.getId(), value, false);
					}
					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);
					playerCurrencyAmount -= value;
				} else {
					break;
				}
			} else {
				if(playerCurrencyAmount >= value && hasInventorySpace(player, item, 995, value)) {
					int canBeBought = playerCurrencyAmount / (value);
					if(canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if(canBeBought == 0)
						break;
					
						if(usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(995, value * canBeBought, false);
						}

					super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
					playerCurrencyAmount -= value;
					break;
				} else {
					break;
				}
			}
			amountBuying--;
		}
		if(usePouch) {
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch()); //Update the money pouch
		}
		player.getInventory().refreshItems();
		refreshItems();
		publicRefresh();
		return this;
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 * @param item	The item which the player is buying
	 * @return		true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if(player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if(item.getDefinition().isStackable()) {
			if(player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if(currency != -1) {
			if(player.getInventory().getFreeSlots() == 0 && player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PlayerOwnedShopContainer add(Item item, boolean refresh) {
		super.add(item, false);
		publicRefresh();
		return this;
	}

	@Override
	public int capacity() {
		return 42;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public PlayerOwnedShopContainer refreshItems() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isPlayerOwnedShopping() || player.getPlayerOwnedShop() == null || player.getPlayerOwnedShop().index != index)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(PlayerOwnedShopManager.getShops().get(index), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if(player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public PlayerOwnedShopContainer full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public static boolean shopBuysItem(int index_id, Item item) {
		PlayerOwnedShopContainer shop = PlayerOwnedShopManager.getShops().get(index_id);
		if(shop != null && shop.getOriginalStock() != null) {
			for(Item it : shop.getOriginalStock()) {
				if(it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static class PlayerOwnedShopManager {

		private static Map<Integer, PlayerOwnedShopContainer> pos_shops = new HashMap<Integer, PlayerOwnedShopContainer>();

		public static Map<Integer, PlayerOwnedShopContainer> getShops() {
			return pos_shops;
		}

		public static void load() {
			for(PosOffers o : PlayerOwnedShops.SHOPS) {
				if(o == null) {
					continue;
				}
				Item[] items = new Item[o.getOffers().size()];
				for(int i = 0; i < o.getOffers().size(); i++) {
					items[i] = new Item(o.getOffers().get(i).getItemId(), o.getOffers().get(i).getAmount());
				}
				System.out.println("Added " + o.getOwner() + " ; " + getIndex(o.getOwner()));
				pos_shops.put(getIndex(o.getOwner()), new PlayerOwnedShopContainer(null, o.getOwner(), items));
			}
		}
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values
	 * to 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;
}
