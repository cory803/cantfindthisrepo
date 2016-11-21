package com.chaos.model.container.impl;

import com.chaos.engine.task.TaskManager;
import com.chaos.engine.task.impl.ShopRestockTask;
import com.chaos.model.Item;
import com.chaos.model.Skill;
import com.chaos.model.container.ItemContainer;
import com.chaos.model.container.StackType;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.model.input.impl.EnterAmountToBuyFromShop;
import com.chaos.model.input.impl.EnterAmountToSellToShop;
import com.chaos.util.JsonLoader;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.content.PlayerLogs;
import com.chaos.world.content.minigames.impl.RecipeForDisaster;
import com.chaos.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Messy but perfect Shop System
 * 
 * @author Gabriel Hannason
 */

public class POSContainer extends ItemContainer {

	/*
	 * The shop constructor
	 */
	public POSContainer(Player player, int id, String owner, String name, Item[] stockItems) {
		super(player);
		if (stockItems.length > 42)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 40 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name;
		for (int i = 0; i < stockItems.length; i++) {
			if(stockItems[i] == null) {
				continue;
			}
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
		}
	}

	public final int id;

	private String name;

	private String owner;

	public String getOwner() {
		return this.owner;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public POSContainer setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Opens a shop for a player
	 * 
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public POSContainer open(Player player, boolean showReturn) {
		setPlayer(player);

		if(showReturn) {
			player.getPacketSender().sendString(41900, "Return to Search");
		}

		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();

		getPlayer().setPosContainer(POSManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShoppingPOS(true);

		refreshItems();

		return this;
	}

	/**
	 * Get a shop id for a players name
	 * @param owner
	 * @return
	 */
	public int getIdForName(String owner) {
		for(int i = 0; i <= POSManager.getShops().size(); i++) {
			if(POSManager.getShops().get(i).getOwner().equalsIgnoreCase(owner)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		POSContainer publicShop = POSManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getPOSContainer() != null && player.getPOSContainer().id == id && player.isShoppingPOS())
				player.getPOSContainer().setItems(publicShop.getItems());
		}
	}

	/**
	 * Checks a value of an item in a shop
	 * 
	 * @param player
	 *            The player who's checking the item's value
	 * @param slot
	 *            The shop item's slot (in the shop!)
	 * @param sellingItem
	 *            Is the player selling the item?
	 */
	public void checkValue(Player player, int slot, boolean sellingItem) {
		this.setPlayer(player);
		Item shopItem = new Item(getItems()[slot].getId());
		if (!player.isShopping()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 995)
			return;

		int finalValue;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";

		finalValue = ItemDefinition.forId(item.getId()).getValue();
		String s = "coins";

		if (sellingItem) {
			if (finalValue != 1) {
				finalValue = (int) (finalValue * 0.85);
			}
		}

		finalString += "" + finalValue + " " + s + "" + shopPriceEx(finalValue) + ".";

		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	/**
	 * Sell a item to your POS
	 * @param player
	 * @param slot
	 * @param amountToSell
	 */
	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShoppingPOS() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (itemToSell.getId() == 12183 || itemToSell.getId() == 12530 || itemToSell.getId() == 18016) {
			player.getPacketSender()
					.sendMessage("You can't sell shards in your player owned store.");
			return;
		}
		if (!itemToSell.sellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}

		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;

		if (this.full(itemToSell.getId()))
			return;

		if (player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());

		if (amountToSell == 0)
			return;

		int itemId = itemToSell.getId();

		boolean customShop = false;
		boolean inventorySpace = customShop ? true : false;


		if (!itemToSell.getDefinition().isStackable()) {
			if (!player.getInventory().contains(995))
				inventorySpace = true;
		}
		if (player.getInventory().getFreeSlots() <= 0
				&& player.getInventory().getAmount(995) > 0)
			inventorySpace = true;
		if (player.getInventory().getFreeSlots() > 0
				|| player.getInventory().getAmount(995) > 0)
			inventorySpace = true;

		int itemValue = 0; //TODO: Add the price of the item

		if (itemValue <= 0)
			return;

		/**
		 * Sell the item to the shop
		 */
		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShoppingPOS())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);

					//TODO: Write to the POS they did sell the item!

				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);

					//TODO: Write to the POS they did sell the item!

					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
		}
		player.getInventory().refreshItems();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public POSContainer switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShoppingPOS() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (!shopSellsItem(item))
			return this;
		if (getItems()[slot].getAmount() <= 1) {
			player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
			return this;
		}
		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if (amountBuying == 0)
			return this;
		if (amountBuying > 5000) {
			if (ItemDefinition.forId(item.getId()).getName().endsWith("s")) {
				player.getPacketSender().sendMessage(
						"You can only buy 5000 " + ItemDefinition.forId(item.getId()).getName() + " at a time.");
			} else {
				player.getPacketSender().sendMessage(
						"You can only buy 5000 " + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
			}
			return this;
		}
		int playerCurrencyAmount = 0;
		boolean usePouch = false;
		String currencyName = "";

		int value = 0; //TODO: Add the prite

		playerCurrencyAmount = player.getInventory().getAmount(995);

		currencyName = "coins";


		if (player.getMoneyInPouch() >= value) {
			playerCurrencyAmount = player.getMoneyInPouchAsInt();
			if (!(player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(995) == value)) {
				usePouch = true;
			}
		}

		if (value <= 0) {
			return this;
		}

		if (!hasInventorySpace(player, item, 995, value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}

		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender()
					.sendMessage("You do not have enough "
							+ ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s")))
							+ " to purchase this item.");
			return this;
		}

		/**
		 * Purchasing items
		 */
		for (int i = amountBuying; i > 0; i--) {
			if (!shopSellsItem(item)) {
				break;
			}
			if (getItems()[slot].getAmount() <= 1) {
				player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
				break;
			}
			if (!item.getDefinition().isStackable()) {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, 995, value)) {

					if (usePouch) {
						player.setMoneyInPouch((player.getMoneyInPouch() - value));
					} else {
						player.getInventory().delete(995, value, false);
					}

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

					playerCurrencyAmount -= value;

				} else {
					break;
				}
			} else {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, 995, value)) {

					int canBeBought = playerCurrencyAmount / (value);
					if (canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if (canBeBought == 0)
						break;

					if (usePouch) {
						player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
					} else {
						player.getInventory().delete(995, value * canBeBought, false);
					}

					if (getItems()[slot].getAmount() - canBeBought <= 0) {
						canBeBought -= 1;
						player.getInventory().add(item.getId(), 1);
						player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
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

		if (usePouch) {
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		}

		player.getInventory().refreshItems();
		refreshItems();
		publicRefresh();
		return this;
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 * 
	 * @param item
	 *            The item which the player is buying
	 * @return true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if (player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if (item.getDefinition().isStackable()) {
			if (player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if (currency != -1) {
			if (player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public POSContainer add(Item item, boolean refresh) {
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
	public POSContainer refreshItems() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShoppingPOS() || player.getPOSContainer() == null || player.getPOSContainer().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(POSManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			//if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
				//	|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public POSContainer full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	public String shopPriceEx(int shopPrice) {
		String ShopAdd = "";
		if (shopPrice >= 1000 && shopPrice < 1000000) {
			ShopAdd = " (" + (shopPrice / 1000) + "K)";
		} else if (shopPrice >= 1000000) {
			ShopAdd = " (" + (shopPrice / 1000000) + " million)";
		}
		return ShopAdd;
	}

	/**
	 * Check if the item is still in the shop container.
	 * This goes first before shopBuysItem() for lag purposes.
	 * @param item
	 * @return
	 */
	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public static class POSManager {

		private static Map<Integer, POSContainer> shops = new HashMap<Integer, POSContainer>();

		public static Map<Integer, POSContainer> getShops() {
			return shops;
		}

		/**
		 * Create a player owned shop
		 * @param id
		 * @param owner
		 * @param name
		 * @param stockItems
		 */
		public static void addShop(int id, String owner, String name, Item[] stockItems) {
			shops.put(id, new POSContainer(null, id, owner, name, stockItems));
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
	 * The inventory interface id, used to set the items right click values to
	 * 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

}
