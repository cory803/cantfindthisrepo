package com.chaos.model.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

/**
 * Messy but perfect Shop System
 * 
 * @author Gabriel Hannason
 */

public class Shop extends ItemContainer {

	/*
	 * The shop constructor
	 */
	public Shop(Player player, int id, String name, Item currency, Item[] stockItems) {
		super(player);
		if (stockItems.length > 42)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 40 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name.length() > 0 ? name : "General Store";
		this.currency = currency;
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	public final int id;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Shop setName(String name) {
		this.name = name;
		return this;
	}

	public Item getCurrency() {
		return currency;
	}

	public Shop setCurrency(Item currency) {
		this.currency = currency;
		return this;
	}

	private boolean restockingItems;

	public boolean isRestockingItems() {
		return restockingItems;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	/**
	 * Opens a shop for a player
	 * 
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public Shop open(Player player) {
		if (player.getGameModeAssistant().isIronMan()) {
			if (id != IRON_SLAYER_STORE && id != IRON_VOTING_REWARDS_STORE
					&& id != SKILLCAPE_STORE_1 && id != SKILLCAPE_STORE_2 && id != SKILLCAPE_STORE_3
					&& id != 18 && id != 30 && id != 38 && id != 39 && id != 40) {
				player.getPacketSender().sendMessage("You're unable to access this shop because you're an iron man.");
				return this;
			}
		}
		setPlayer(player);
		if (id == 44) {
			player.getPacketSender().sendString(41900, "Buy experience");
		} else {
			player.getPacketSender().sendString(41900, "");
		}
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShopping(true);
		refreshItems();
		if (Misc.getMinutesPlayed(getPlayer()) <= 190)
			getPlayer().getPacketSender()
					.sendMessage("Note: When selling an item to a store, it loses 15% of its original value.");
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getShop() != null && player.getShop().id == id && player.isShopping())
				player.getShop().setItems(publicShop.getItems());
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
		if (sellingItem) {
			if (!shopBuysItem(id, item)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}
		}
		int finalValue = 0;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
		if (getCurrency().getId() != -1) {
			finalValue = ItemDefinition.forId(item.getId()).getValue();
			String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
					? currency.getDefinition().getName().toLowerCase()
					: currency.getDefinition().getName().toLowerCase() + "s";
			/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
			if (id == TOKKUL_EXCHANGE_STORE || id == STARDUST_EXCHANGE_STORE || id == AGILITY_TICKET_STORE) {
				Object[] obj = ShopManager.getCustomShopData(id, item.getId());
				if (obj == null)
					return;
				finalValue = (int) obj[0];
				s = (String) obj[1];
			}
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + s + "" + shopPriceEx(finalValue) + ".";
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return;
			finalValue = (int) obj[0];
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + (String) obj[1] + ".";
		}
		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (id == CREDIT_STORE_1 || id == CREDIT_STORE_2 || id == CREDIT_STORE_3
//				id == DONATOR_STORE || id == DONATOR_STORE_MISC
				) {
			player.getPacketSender().sendMessage("You cannot sell items to this store.");
			return;
		}
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (itemToSell.getId() == 12183 || itemToSell.getId() == 12530 || itemToSell.getId() == 18016) {
			player.getPacketSender()
					.sendMessage("You can't sell this item to this shop, please exchange shards with pikkupstix.");
			return;
		}
		boolean creditShop = id == CREDIT_STORE_1 || id == CREDIT_STORE_2 || id == CREDIT_STORE_3;
		if (!itemToSell.sellable() && !creditShop) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}
		if (!shopBuysItem(id, itemToSell)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
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
		if (itemToSell.getId() == 868) {
			System.out.println(player.getUsername() + " is trying to sell " + amountToSell + " RuneKnives.");
		}
		/*
		 * if(amountToSell > 300) { String s =
		 * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
		 * ItemDefinition.forId(itemToSell.getId()).getName() :
		 * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
		 * player.getPacketSender().sendMessage("You can only sell 300 "+s+
		 * " at a time."); return; }
		 */
		int itemId = itemToSell.getId();
		int count = 0;
		boolean customShop = this.getCurrency().getId() == -1;
		boolean inventorySpace = customShop ? true : false;
		if (!customShop) {
			if (!itemToSell.getDefinition().isStackable()) {
				if (!player.getInventory().contains(this.getCurrency().getId()))
					inventorySpace = true;
			}
			if (player.getInventory().getFreeSlots() <= 0
					&& player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
			if (player.getInventory().getFreeSlots() > 0
					|| player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
		}
		int itemValue = 0;
		if (getCurrency().getId() > 0) {
			itemValue = ItemDefinition.forId(itemToSell.getId()).getValue();
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, itemToSell.getId());
			if (obj == null)
				return;
			itemValue = (int) obj[0];
		}
		if (itemValue <= 0)
			return;
		itemValue = (int) (itemValue * 0.85);
		if (itemValue <= 0) {
			itemValue = 1;
		}
		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShopping())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue), false);
						player.save();
					} else {
						// Return points here
						if (creditShop) {
							player.setCredits(itemValue, true);
							player.getPacketSender().sendMessage("You have sold the item: "
									+ itemToSell.getDefinition().getName() + " for " + itemValue + " credits.");
							player.getPacketSender().sendMessage("You now have " + player.getCredits() + " credits.");
						}
					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell), false);
					} else {
						if (creditShop) {
							player.setCredits(itemValue * amountToSell, true);
							player.getPacketSender()
									.sendMessage("You have sold the item: " + itemToSell.getDefinition().getName()
											+ " for " + itemValue * amountToSell + " credits.");
							player.getPacketSender().sendMessage("You now have " + player.getCredits() + " credits.");
						}
						// Return points here
					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
			count++;
		}
		if (customShop) {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (this.id == GENERAL_STORE) {
			if (player.getGameModeAssistant().isIronMan()) {
				player.getPacketSender().sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
		}
		if (!shopSellsItem(item))
			return this;
		if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {
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
		if (item.getId() == 868) {
			System.out.println(player.getUsername() + " is trying to buy " + amountBuying + " RuneKnives.");
		}
		boolean customShop = getCurrency().getId() == -1;
		boolean usePouch = false;
		int playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		String currencyName = "";
		if (getCurrency().getId() != -1) {
			playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
			PlayerLogs.other(player, "Player has bought the store item: " + item.getDefinition().getName() + " ("
					+ item.getId() + "), amount: " + amountBuying);
			currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
			if (currency.getId() == 995) {
				if (player.getMoneyInPouch() >= value) {
					playerCurrencyAmount = player.getMoneyInPouchAsInt();
					if (!(player.getInventory().getFreeSlots() == 0
							&& player.getInventory().getAmount(currency.getId()) == value)) {
						usePouch = true;
					}
				}
			} else {
				/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
				if (id == TOKKUL_EXCHANGE_STORE || id == STARDUST_EXCHANGE_STORE
						|| id == AGILITY_TICKET_STORE || id == CREDIT_STORE_1
						|| id == CREDIT_STORE_2 || id == CREDIT_STORE_3) {
					value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
				}
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return this;
			value = (int) obj[0];
			currencyName = (String) obj[1];
			if (id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2) {
				playerCurrencyAmount = player.getPointsHandler().getPkPoints();
			} else if (id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2 || id == IRON_VOTING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getVotingPoints();
//			} else if (id == DUNGEONEERING_STORE) {
//				playerCurrencyAmount = player.getPointsHandler().getDungeoneeringTokens();
			} else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			} else if (id == CREDIT_STORE_1) {
				playerCurrencyAmount = player.getCredits();
			} else if (id == CREDIT_STORE_2) {
				playerCurrencyAmount = player.getCredits();
			} else if (id == CREDIT_STORE_3) {
				playerCurrencyAmount = player.getCredits();
			}
		}
		if (value <= 0) {
			return this;
		}
		if (!hasInventorySpace(player, item, getCurrency().getId(), value)) {
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
		if (id == SKILLCAPE_STORE_1 || id == SKILLCAPE_STORE_2 || id == SKILLCAPE_STORE_3) {
			for (int i = 0; i < item.getDefinition().getRequirement().length; i++) {
				int req = item.getDefinition().getRequirement()[i];
				if ((i == 3 || i == 5) && req == 99)
					req *= 10;
				if (req > player.getSkillManager().getMaxLevel(i)) {
					player.getPacketSender().sendMessage("You need to have at least level 99 in "
							+ Misc.formatText(Skill.forId(i).toString().toLowerCase()) + " to buy this item.");
					return this;
				}
			}
		}

		for (int i = amountBuying; i > 0; i--) {
			if (!shopSellsItem(item)) {
				break;
			}
			if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {
				player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
				break;
			}
			if (!item.getDefinition().isStackable()) {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - value));
						} else {
							player.getInventory().delete(currency.getId(), value, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2) {
							player.getPointsHandler().setPkPoints(-value, true);
						} else if (id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2 || id == IRON_VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value, true);
//						} else if (id == DUNGEONEERING_STORE) {
//							player.getPointsHandler().setDungeoneeringTokens(-value, true);
						} else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						} else if (id == CREDIT_STORE_1) {
							player.setCredits(-value, true);
						} else if (id == CREDIT_STORE_2) {
							player.setCredits(-value, true);
						} else if (id == CREDIT_STORE_3) {
							player.setCredits(-value, true);
						}
					}

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

					playerCurrencyAmount -= value;
				} else {
					break;
				}
			} else {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					int canBeBought = playerCurrencyAmount / (value);
					if (canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if (canBeBought == 0)
						break;

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(currency.getId(), value * canBeBought, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2) {
							player.getPointsHandler().setPkPoints(-value * canBeBought, true);
						} else if (id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2 || id == IRON_VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value * canBeBought, true);
//						} else if (id == DUNGEONEERING_STORE) {
//							player.getPointsHandler().setDungeoneeringTokens(-value * canBeBought, true);
						} else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						} else if (id == CREDIT_STORE_1) {
							player.setCredits(-value * canBeBought, true);
						} else if (id == CREDIT_STORE_2) {
							player.setCredits(-value * canBeBought, true);
						} else if (id == CREDIT_STORE_3) {
							player.setCredits(-value * canBeBought, true);
						}
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
		if (!customShop) {
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
																							// the
																							// money
																							// pouch
			}
		} else {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
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
	public Shop add(Item item, boolean refresh) {
		super.add(item, false);
		if (id != RECIPE_FOR_DISASTER_STORE)
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
	public Shop refreshItems() {
		if (id == RECIPE_FOR_DISASTER_STORE) {
			RecipeForDisaster.openRFDShop(getPlayer());
			return this;
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
					|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public Shop full() {
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

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public void fireRestockTask() {
		if (isRestockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	public boolean fullyRestocked() {
		if (id == GENERAL_STORE) {
			return getValidItems().size() == 0;
		} else if (id == RECIPE_FOR_DISASTER_STORE) {
			return true;
		}
		if (getOriginalStock() != null) {
			for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
				if (getItems()[shopItemIndex].getAmount() != getOriginalStock()[shopItemIndex].getAmount())
					return false;
			}
		}
		return true;
	}

	public static boolean shopBuysItem(int shopId, Item item) {
		if (shopId == GENERAL_STORE)
			return true;
		if (shopId == STARDUST_EXCHANGE_STORE|| shopId == RECIPE_FOR_DISASTER_STORE
				|| shopId == IRON_VOTING_REWARDS_STORE || shopId == VOTING_REWARDS_STORE || shopId == VOTING_REWARDS_STORE2
				|| shopId == AGILITY_TICKET_STORE || shopId == TOKKUL_EXCHANGE_STORE
				|| shopId == SLAYER_STORE || shopId == IRON_SLAYER_STORE)
			return false;
		Shop shop = ShopManager.getShops().get(shopId);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static class ShopManager {

		private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

		public static Map<Integer, Shop> getShops() {
			return shops;
		}

		public static JsonLoader parseShops() {
			return new JsonLoader() {
				@Override
				public void load(JsonObject reader, Gson builder) {
					int id = reader.get("id").getAsInt();
					String name = reader.get("name").getAsString();
					Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
					Item currency = new Item(reader.get("currency").getAsInt());
					shops.put(id, new Shop(null, id, name, currency, items));
				}

				@Override
				public String filePath() {
					return "./data/def/json/world_shops.json";
				}
			};
		}

		public static Object[] getCustomShopData(int shop, int item) {
			if (shop == VOTING_REWARDS_STORE || shop == VOTING_REWARDS_STORE2) {
				switch (item) {
					case 14484: // id
						return new Object[] { 5, "Vote points" }; //5 = how many vote points
				}
			} else if (shop == PKING_REWARDS_STORE || shop == PKING_REWARDS_STORE2) {
				switch (item) {

					case 14484:
						return new Object[] { 5750, "Pk points" };

					case 11694:
						return new Object[] { 6500, "Pk points" };

					case 11700:
					case 11698:
					case 11696:
						return new Object[] {3000, "Pk points" };

					case 19780:
						return new Object[] {6000, "Pk points" };

					case 13920:
						return new Object[] {1000, "Pk points" };

					case 13908:
						return new Object[] {1500, "Pk points" };

					case 13914:
						return new Object[] {1500, "Pk points" };

					case 13926:
						return new Object[] {2000, "Pk points" };

					case 13911:
						return new Object[] {2500, "Pk points" };

					case 13917:
						return new Object[] {2500, "Pk points" };

					case 13923:
						return new Object[] {3000, "Pk points" };

					case 13929:
						return new Object[] {2250, "Pk points" };

					case 13950:
						return new Object[] {500, "Pk points" };

					case 13944:
						return new Object[] {1000, "Pk points" };

					case 13947:
						return new Object[] {1000, "Pk points" };

					case 13938:
						return new Object[] {500, "Pk points" };

					case 13932:
						return new Object[] {1000, "Pk points" };

					case 13935:
						return new Object[] {1000, "Pk points" };

					case 11517:
						return new Object[] {10, "Pk points" };

					case 11525:
						return new Object[] {10, "Pk points" };

					case 6570:
						return new Object[] {500, "Pk points" };

					case 19111:
						return new Object[] {2000, "Pk points" };

					case 8850:
						return new Object[] {500, "Pk points" };

					case 20072:
					case 12954:
						return new Object[] {1000, "Pk points" };

					case 10551:
						return new Object[] {1000, "Pk points" };

					case 4151:
						return new Object[] {500, "Pk points" };

					case 11283:
						return new Object[] {750, "Pk points" };

					case 6585:
						return new Object[] {500, "Pk points" };

					case 21140:
						return new Object[] {5000, "Pk points" };

					case 15126:
						return new Object[] {500, "Pk points" };

					case 13576:
						return new Object[] {3000, "Pk points" };

					case 20171:
						return new Object[] {3250, "Pk points" };

					case 12926:
						return new Object[] {7500, "Pk points" };

					case 21144:
						return new Object[] {12000, "Pk points" };

					case 21146:
						return new Object[] {15, "Pk points" };

					case 21107:
						return new Object[] {7500, "Pk points" };

					case 19672:
						return new Object[] {9000, "Pk points" };

					case 19673:
						return new Object[] {9000, "Pk points" };

					case 19674:
						return new Object[] {9000, "Pk points" };

					case 7968:
					case 11718:
						return new Object[] {1000, "Pk points" };
					
					case 11720:
					case 11724:
					case 11726:
						return new Object[] {2500, "Pk points" };
					case 11722:
						return new Object[] {3000, "Pk points" };
					case 11601:
						return new Object[] {35000, "Pk points" };
					case 6731:
					case 6733:
					case 6735:
					case 6737:
						return new Object[] {500, "Pk points" };

				}
			} else if (shop == TOKKUL_EXCHANGE_STORE) {
				switch (item) {
					case 13225:
						return new Object[]{300000, "tokkul"};
					case 438:
					case 436:
						return new Object[]{10, "tokkul"};
					case 440:
						return new Object[]{25, "tokkul"};
					case 453:
						return new Object[]{30, "tokkul"};
					case 442:
						return new Object[]{30, "tokkul"};
					case 444:
						return new Object[]{40, "tokkul"};
					case 447:
						return new Object[]{70, "tokkul"};
					case 449:
						return new Object[]{120, "tokkul"};
					case 451:
						return new Object[]{250, "tokkul"};
					case 1623:
						return new Object[]{20, "tokkul"};
					case 1621:
						return new Object[]{40, "tokkul"};
					case 1619:
						return new Object[]{70, "tokkul"};
					case 1617:
						return new Object[]{150, "tokkul"};
					case 1631:
						return new Object[]{1600, "tokkul"};
					case 6571:
						return new Object[]{50000, "tokkul"};
					case 11128:
						return new Object[]{22000, "tokkul"};
					case 6522:
						return new Object[]{20, "tokkul"};
					case 6524:
					case 6523:
					case 6526:
						return new Object[]{5000, "tokkul"};
					case 6528:
					case 6568:
						return new Object[]{800, "tokkul"};
				}
			} else if (shop == AGILITY_TICKET_STORE) {
				switch (item) {
					case 14936:
					case 14938:
						return new Object[] { 60, "agility tickets" };
					case 10941:
					case 10939:
					case 10940:
					case 10933:
						return new Object[] { 20, "agility tickets" };
					case 20786:
						return new Object[] { 100, "agility tickets" };
				}
			} else if (shop == SLAYER_STORE) {
				switch (item) {
					case 13263:
						return new Object[] { 250, "slayer points" };
					case 13281:
						return new Object[] { 5, "slayer points" };
					case 15403:
					case 11730:
					case 10887:
					case 15241:
						return new Object[] { 300, "slayer points" };
					case 15490:
					case 15488:
					case 21369:
						return new Object[] { 125, "slayer points" };
					case 11235:
					case 4151:
					case 15486:
						return new Object[] { 250, "slayer points" };
					case 15243:
						return new Object[] { 3, "slayer points" };
					case 10551:
						return new Object[] { 200, "slayer points" };
					case 2572:
						return new Object[] { 500, "slayer points" };
					case 21110:
						return new Object[] { 1250, "slayer points" };
				}
			} else if (shop == IRON_SLAYER_STORE) {
				switch (item) {
					case 5574:
					case 5575:
					case 5576:
						return new Object[] { 50, "slayer points" };
					case 544:
					case 542:
						return new Object[] { 10, "slayer points" };
				}
			}
			return null;
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

	/*
	 * Declared shops
	 */
	public static final int GENERAL_STORE = 0;
	public static final int RECIPE_FOR_DISASTER_STORE = 36;

	private static final int SKILLCAPE_STORE_1 = 8;
	private static final int SKILLCAPE_STORE_2 = 9;
	private static final int SKILLCAPE_STORE_3 = 10;

	private static final int CREDIT_STORE_1 = 12;
	private static final int CREDIT_STORE_2 = 13;
	private static final int CREDIT_STORE_3 = 14;

	private static final int SLAYER_STORE = 15;
	private static final int IRON_SLAYER_STORE = 33;

	private static final int TOKKUL_EXCHANGE_STORE = 17;

	private static final int STARDUST_EXCHANGE_STORE = 18;

	private static final int PKING_REWARDS_STORE = 21;
	private static final int PKING_REWARDS_STORE2 = 22;

	private static final int VOTING_REWARDS_STORE = 23;
	private static final int VOTING_REWARDS_STORE2 = 24;
	private static final int IRON_VOTING_REWARDS_STORE = 25;

	private static final int AGILITY_TICKET_STORE = 28;


}
