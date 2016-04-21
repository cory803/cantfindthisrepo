package com.runelive.model.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.runelive.engine.task.TaskManager;
import com.runelive.engine.task.impl.ShopRestockTask;
import com.runelive.model.GameMode;
import com.runelive.model.Item;
import com.runelive.model.Skill;
import com.runelive.model.container.ItemContainer;
import com.runelive.model.container.StackType;
import com.runelive.model.definitions.ItemDefinition;
import com.runelive.model.input.impl.EnterAmountToBuyFromShop;
import com.runelive.model.input.impl.EnterAmountToSellToShop;
import com.runelive.util.JsonLoader;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.PlayerLogs;
import com.runelive.world.content.minigames.impl.RecipeForDisaster;
import com.runelive.world.entity.impl.player.Player;

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
      throw new ArrayIndexOutOfBoundsException("Stock cannot have more than 40 items; check shop["
          + id + "]: stockLength: " + stockItems.length);
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

  private final int id;

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
   * @param player The player to open the shop for
   * @return The shop instance
   */
  public Shop open(Player player) {
    if ((id == 0 || id == 1 || id == 2 || id == 3 || id == 4 || id == 5 || id == 6 || id == 7
        || id == 13 || id == 14 || id == 15 || id == 16 || id == 19 || id == 26 || id == 27
        || id == 34 || id == 35 || id == 41 || id == 59)
        && (player.getGameMode() == GameMode.IRONMAN
            || player.getGameMode() == GameMode.HARDCORE_IRONMAN)) {
      player.getPacketSender().sendMessage(
          "You're unable to access this shop because you're an " + player.getGameMode() + ".");
      return this;
    }
    setPlayer(player);
    getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
    getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID)
        .setShopping(true);
    refreshItems();
    if (Misc.getMinutesPlayed(getPlayer()) <= 190)
      getPlayer().getPacketSender().sendMessage(
          "Note: When selling an item to a store, it loses 15% of its original value.");
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
   * @param player The player who's checking the item's value
   * @param slot The shop item's slot (in the shop!)
   * @param sellingItem Is the player selling the item?
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
    String finalString =
        sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
            : "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
    if (getCurrency().getId() != -1) {
      finalValue = ItemDefinition.forId(item.getId()).getValue();
      String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
          ? currency.getDefinition().getName().toLowerCase()
          : currency.getDefinition().getName().toLowerCase() + "s";
      /** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
      if (id == TOKKUL_EXCHANGE_STORE || id == STARDUST_EXCHANGE_STORE
          || id == ENERGY_FRAGMENT_STORE || id == AGILITY_TICKET_STORE || id == GRAVEYARD_STORE) {
        Object[] obj = ShopManager.getCustomShopData(id, item.getId());
        if (obj == null)
          return;
        finalValue = (int) obj[0];
        s = (String) obj[1];
      }
      if (sellingItem) {
        if (finalValue != 1) {
          finalValue = (int) (finalValue * 0.75);
        }
      }
      finalString += "" + (int) finalValue + " " + s + "" + shopPriceEx((int) finalValue) + ".";
    } else {
      Object[] obj = ShopManager.getCustomShopData(id, item.getId());
      if (obj == null)
        return;
      finalValue = (int) obj[0];
      if (sellingItem) {
        if (finalValue != 1) {
          finalValue = (int) (finalValue * 0.75);
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
    if (id == DONATOR_STORE || id == DONATOR_STORE_MISC || id == CREDIT_STORE_1
        || id == CREDIT_STORE_2 || id == CREDIT_STORE_3) {
      player.getPacketSender().sendMessage("You cannot sell items to this store.");
      return;
    }
    if (!player.isShopping() || player.isBanking()) {
      player.getPacketSender().sendInterfaceRemoval();
      return;
    }
    Item itemToSell = player.getInventory().getItems()[slot];
    if (itemToSell.getId() == 12183 || itemToSell.getId() == 12530 || itemToSell.getId() == 18016) {
      player.getPacketSender().sendMessage(
          "You can't sell this item to this shop, please exchange shards with pikkupstix.");
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
    /*
     * if(amountToSell > 300) { String s =
     * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
     * ItemDefinition.forId(itemToSell.getId()).getName() :
     * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
     * player.getPacketSender().sendMessage("You can only sell 300 "+s+" at a time."); return; }
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
              player.getPacketSender()
                  .sendMessage("You now have " + player.getCredits() + " credits.");
            }
          }
        } else {
          player.getPacketSender()
              .sendMessage("Please free some inventory space before doing that.");
          break;
        }
      } else {
        if (inventorySpace) {
          super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
          if (!customShop) {
            player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell),
                false);
          } else {
            if (creditShop) {
              player.setCredits(itemValue * amountToSell, true);
              player.getPacketSender()
                  .sendMessage("You have sold the item: " + itemToSell.getDefinition().getName()
                      + " for " + itemValue * amountToSell + " credits.");
              player.getPacketSender()
                  .sendMessage("You now have " + player.getCredits() + " credits.");
            }
            // Return points here
          }
          break;
        } else {
          player.getPacketSender()
              .sendMessage("Please free some inventory space before doing that.");
          break;
        }
      }
      amountToSell--;
      count++;
    }
    PlayerLogs.log(player.getUsername(),
        "Player has sold item: " + itemToSell.getDefinition().getName() + " (" + itemToSell.getId()
            + "), amount: " + count + " to the store.");
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
      if (player.getGameMode() == GameMode.IRONMAN) {
        player.getPacketSender()
            .sendMessage("Ironman-players are not allowed to buy items from the general-store.");
        return this;
      }
      if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
        player.getPacketSender().sendMessage(
            "Hardcore-ironman-players are not allowed to buy items from the general-store.");
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
        player.getPacketSender().sendMessage("You can only buy 5000 "
            + ItemDefinition.forId(item.getId()).getName() + " at a time.");
      } else {
        player.getPacketSender().sendMessage("You can only buy 5000 "
            + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
      }
      return this;
    }
    boolean customShop = getCurrency().getId() == -1;
    boolean usePouch = false;
    int playerCurrencyAmount = 0;
    int value = ItemDefinition.forId(item.getId()).getValue();
    String currencyName = "";
    if (getCurrency().getId() != -1) {
      playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
      PlayerLogs.log(player.getUsername(), "Player has bought the store item: "
          + item.getDefinition().getName() + " (" + item.getId() + "), amount: " + amountBuying);
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
            || id == ENERGY_FRAGMENT_STORE || id == AGILITY_TICKET_STORE || id == GRAVEYARD_STORE
            || id == CREDIT_STORE_1 || id == CREDIT_STORE_2 || id == CREDIT_STORE_3) {
          value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
        }
      }
    } else {
      Object[] obj = ShopManager.getCustomShopData(id, item.getId());
      if (obj == null)
        return this;
      value = (int) obj[0];
      currencyName = (String) obj[1];
      if (id == PKING_REWARDS_STORE) {
        playerCurrencyAmount = player.getPointsHandler().getPkPoints();
      } else if (id == VOTING_REWARDS_STORE || id == IRON_VOTING_REWARDS_STORE) {
        playerCurrencyAmount = player.getPointsHandler().getVotingPoints();
      } else if (id == DUNGEONEERING_STORE) {
        playerCurrencyAmount = player.getPointsHandler().getDungeoneeringTokens();
      } else if (id == PRESTIGE_STORE) {
        playerCurrencyAmount = player.getPointsHandler().getPrestigePoints();
      } else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
        playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
      } else if (id == CREDIT_STORE_1) {
        playerCurrencyAmount = player.getCredits();
      } else if (id == CREDIT_STORE_2) {
        playerCurrencyAmount = player.getCredits();
      } else if (id == CREDIT_STORE_3) {
        playerCurrencyAmount = player.getCredits();
      } else if (id == BOSS_POINT_STORE) {
        playerCurrencyAmount = player.getBossPoints();
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
    } else if (id == GAMBLING_STORE) {
      if (item.getId() == 15084 || item.getId() == 299) {
        if (player.getDonorRights() == 0) {
          player.getPacketSender().sendMessage("You need to be a member to use these items.");
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
        if (playerCurrencyAmount >= value
            && hasInventorySpace(player, item, getCurrency().getId(), value)) {

          if (!customShop) {
            if (usePouch) {
              player.setMoneyInPouch((player.getMoneyInPouch() - value));
            } else {
              player.getInventory().delete(currency.getId(), value, false);
            }
          } else {
            if (id == PKING_REWARDS_STORE) {
              player.getPointsHandler().setPkPoints(-value, true);
            } else if (id == VOTING_REWARDS_STORE || id == IRON_VOTING_REWARDS_STORE) {
              player.getPointsHandler().setVotingPoints(-value, true);
            } else if (id == DUNGEONEERING_STORE) {
              player.getPointsHandler().setDungeoneeringTokens(-value, true);
            } else if (id == PRESTIGE_STORE) {
              player.getPointsHandler().setPrestigePoints(-value, true);
            } else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
              player.getPointsHandler().setSlayerPoints(-value, true);
            } else if (id == CREDIT_STORE_1) {
              player.setCredits(-value, true);
            } else if (id == CREDIT_STORE_2) {
              player.setCredits(-value, true);
            } else if (id == CREDIT_STORE_3) {
              player.setCredits(-value, true);
            } else if (id == BOSS_POINT_STORE) {
              player.addBossPoints(-value);
            }
          }

          super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

          playerCurrencyAmount -= value;
        } else {
          break;
        }
      } else {
        if (playerCurrencyAmount >= value
            && hasInventorySpace(player, item, getCurrency().getId(), value)) {

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
            if (id == PKING_REWARDS_STORE) {
              player.getPointsHandler().setPkPoints(-value * canBeBought, true);
            } else if (id == VOTING_REWARDS_STORE || id == IRON_VOTING_REWARDS_STORE) {
              player.getPointsHandler().setVotingPoints(-value * canBeBought, true);
            } else if (id == DUNGEONEERING_STORE) {
              player.getPointsHandler().setDungeoneeringTokens(-value * canBeBought, true);
            } else if (id == PRESTIGE_STORE) {
              player.getPointsHandler().setPrestigePoints(-value * canBeBought, true);
            } else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
              player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
            } else if (id == CREDIT_STORE_1) {
              player.setCredits(-value * canBeBought, true);
            } else if (id == CREDIT_STORE_2) {
              player.setCredits(-value * canBeBought, true);
            } else if (id == CREDIT_STORE_3) {
              player.setCredits(-value * canBeBought, true);
            } else if (id == BOSS_POINT_STORE) {
              player.addBossPoints(-value * canBeBought);
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
        player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update the
                                                                                  // money pouch
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
   * @param item The item which the player is buying
   * @return true or false if the player has enough space to buy the item
   */
  public static boolean hasInventorySpace(Player player, Item item, int currency,
      int pricePerItem) {
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
      if (player == null || !player.isShopping() || player.getShop() == null
          || player.getShop().id != id)
        continue;
      player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
      player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
      player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
      if (player.getInputHandling() == null
          || !(player.getInputHandling() instanceof EnterAmountToSellToShop
              || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
        player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
    }
    return this;
  }

  @Override
  public Shop full() {
    getPlayer().getPacketSender()
        .sendMessage("The shop is currently full. Please come back later.");
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
    if (shopId == GENERAL_STORE || shopId == CREDIT_STORE_1 || shopId == CREDIT_STORE_2
        || shopId == CREDIT_STORE_3)
      return true;
    if (shopId == DUNGEONEERING_STORE || shopId == PKING_REWARDS_STORE || shopId == BOSS_POINT_STORE
        || shopId == STARDUST_EXCHANGE_STORE || shopId == VOTING_REWARDS_STORE
        || shopId == IRON_VOTING_REWARDS_STORE || shopId == RECIPE_FOR_DISASTER_STORE
        || shopId == ENERGY_FRAGMENT_STORE || shopId == AGILITY_TICKET_STORE
        || shopId == GRAVEYARD_STORE || shopId == TOKKUL_EXCHANGE_STORE || shopId == PRESTIGE_STORE
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
      if (shop == VOTING_REWARDS_STORE || shop == IRON_VOTING_REWARDS_STORE) {
        switch (item) {
          // iron man shop
          case 12:
          case 10:
          case 8:
          case 6:
            return new Object[] {1, "Voting points"};
          case 1275:
          case 1359:
            return new Object[] {5, "Voting points"};
          case 4587:
          case 1215:
          case 1540:
            return new Object[] {10, "Voting points"};
          case 10499:
            return new Object[] {3, "Voting points"};
          // iron man shop end
          case 21047: // kraken whip
            return new Object[] {100, "Voting points"};
          case 18744: // Guthix Halo
          case 18745: // Saradomin Halo
          case 18746: // Zamorak Halo
            return new Object[] {125, "Voting points"};
          case 19336:
          case 19337:
          case 19338:
          case 19339:
          case 19340:
            return new Object[] {15, "Voting points"};
          case 9813:
            return new Object[] {50, "Voting points"};
          case 20084:
          case 11850:
          case 11856:
          case 11854:
          case 11852:
          case 11846:
          case 11848:
          case 19335:
            return new Object[] {25, "Voting points"};
          case 6199:
            return new Object[] {4, "Voting points"};
          case 15501:
          case 15018:
          case 15019:
          case 15020:
            return new Object[] {10, "Voting points"};
          case 15220:
            return new Object[] {30, "Voting points"};
          case 13262:
            return new Object[] {6, "Voting points"};
          case 6585:
            return new Object[] {8, "Voting points"};
          case 6570:
            return new Object[] {5, "Voting points"};
          case 19111:
          case 14004:
          case 14005:
          case 14006:
          case 14007:
          case 2577:
            return new Object[] {15, "Voting points"};
          case 15332:
            return new Object[] {2, "Voting points"};
          case 14000:
          case 14001:
          case 14002:
          case 14003:
            return new Object[] {30, "Voting points"};
          case 13663:
            return new Object[] {1, "Voting points"};
        }
      } else if (shop == DONATOR_STORE) {
        switch (item) {
          case 1038:
          case 1040:
          case 1042:
          case 1044:
          case 1046:
          case 1048:
            return new Object[] {150, "Donator points"};
          case 6199:
            return new Object[] {20, "Donator points"};
          case 1050:
          case 11694:
            return new Object[] {100, "Donator points"};
          case 1053:
          case 1055:
          case 1057:
            return new Object[] {70, "Donator points"};
          case 4084:
          case 1419:
          case 1037:
          case 1947:
            return new Object[] {60, "Donator points"};
          case 14008:
          case 14009:
          case 14010:
          case 14011:
          case 14012:
          case 14013:
          case 14014:
          case 14015:
          case 14016:
            return new Object[] {100, "Donator points"};
          case 13887:
          case 13893:
            return new Object[] {70, "Donator points"};
          case 13896:
          case 13884:
          case 13890:
          case 11724:
          case 11726:
            return new Object[] {50, "Donator points"};
          case 14484:
          case 19780:
            return new Object[] {150, "Donator points"};
          case 20171:
            return new Object[] {15000, "Donator points"};
          case 13899:
          case 13902:
          case 11698:
          case 11700:
          case 11696:
            return new Object[] {75, "Donator points"};
        }
      } else if (shop == DONATOR_STORE_MISC) {
        switch (item) {
          case 6199:
          case 15220:
          case 15020:
          case 15019:
          case 15018:
            return new Object[] {20, "Donator points"};
          case 6585:
          case 19111:
          case 2577:
          case 2581:
          case 11235:
            return new Object[] {25, "Donator points"};
          case 4151:
          case 6570:
            return new Object[] {15, "Donator points"};
          case 10551:
          case 10548:
          case 10547:
          case 11730:
            return new Object[] {50, "Donator points"};
          case 2572:
          case 15241:
            return new Object[] {50, "Donator points"};
          case 5:
            return new Object[] {25, "Donator points"};
        }
      } else if (shop == PKING_REWARDS_STORE) {
        switch (item) {
          case 6918:
          case 6914:
          case 6889:
          case 2579:
            return new Object[] {25, "Pk points"};
          case 20171:
            return new Object[] {1500, "Pk points"};
          case 6916:
          case 6924:
            return new Object[] {30, "Pk points"};
          case 6920:
          case 6922:
            return new Object[] {20, "Pk points"};
          case 2581:
          case 11730:
            return new Object[] {100, "Pk points"};
          case 2577:
            return new Object[] {100, "Pk points"};
          case 15486:
          case 19111:
            return new Object[] {250, "Pk points"};
          case 13879:
          case 13883:
          case 15243:
          case 15332:
            return new Object[] {4, "Pk points"};
          case 15241:
          case 17273:
            return new Object[] {200, "Pk points"};
          case 10548:
          case 10547:
          case 10551:
            return new Object[] {150, "Pk points"};
          case 6570:
          case 20072:
          case 11235:
          case 4151:
          case 13262:
            return new Object[] {80, "Pk points"};
          case 11696:
          case 11698:
          case 11700:
            return new Object[] {500, "Pk points"};
          case 14484:
          case 19780:
            return new Object[] {750, "Pk points"};
          case 11728:
          case 15018:
          case 15019:
          case 15020:
          case 15220:
            return new Object[] {50, "Pk points"};
          case 11694:
            return new Object[] {600, "Pk points"};
        }
      } else if (shop == CREDIT_STORE_1) {
        switch (item) {
          case 21035:
          case 21036:
          case 21037:
          case 21038:
          case 21039:
          case 21040:
          case 21041:
          case 1038:
          case 1040:
          case 1042:
          case 1044:
          case 1046:
          case 1048:
          case 1050:
          case 21024:
          case 21025:
          case 21026:
            return new Object[] {50000, "Credits"};
          case 1037:
            return new Object[] {100000, "Credits"};
          case 4084:
            return new Object[] {125000, "Credits"};
          case 5607:
            return new Object[] {100000, "Credits"};
          case 21048:
          case 21049:
          case 1419:
            return new Object[] {150000, "Credits"};
          case 21034:
            return new Object[] {100000, "Credits"};
          case 6082:
          case 9920:
          case 5608:
            return new Object[] {125000, "Credits"};
          case 9921:
          case 9922:
          case 9923:
          case 9924:
          case 9925:
          case 15352:
            return new Object[] {35000, "Credits"};
          case 15449:
          case 15464:
          case 15454:
          case 15459:
            return new Object[] {165000, "Credits"};
        }
      } else if (shop == CREDIT_STORE_2) {
        switch (item) {
          case 11694:
          case 19780:
            return new Object[] {10000, "Credits"};
          case 14484:
            return new Object[] {7500, "Credits"};

          case 14008:
          case 14011:
          case 14014:
          case 21016:
          case 21017:
          case 21018:
          case 21019:
          case 21020:
          case 21021:
          case 21022:
          case 21023:
          case 11724:
          case 11726:
            return new Object[] {5000, "Credits"};
          case 14012:
          case 14013:
          case 14015:
          case 14016:
          case 14009:
          case 14010:
          case 21000:
          case 21001:
          case 21002:
          case 21003:
          case 21004:
          case 21005:
          case 21006:
          case 21007:
            return new Object[] {10000, "Credits"};
        }
      } else if (shop == BOSS_POINT_STORE) {
        switch (item) {
          case 11710:
          case 11712:
          case 11714:
          case 6585:
          case 11235:
          case 11732:
          case 4151:
            return new Object[] {150, "Boss Points"};
          case 20171:
            return new Object[] {1500, "Boss Points"};
          case 11286:
          case 13902:
          case 13905:
            return new Object[] {550, "Boss Points"};
          case 13752:
            return new Object[] {1000, "Boss Points"};
          case 13746:
            return new Object[] {1250, "Boss Points"};
          case 13748:
            return new Object[] {1450, "Boss Points"};
          case 13750:
            return new Object[] {1375, "Boss Points"};
          case 11724:
          case 11726:
          case 13899:
          case 11718:
          case 11720:
          case 11722:
            return new Object[] {600, "Boss Points"};
          case 11702:
            return new Object[] {900, "Boss Points"};
          case 14484:
          case 11704:
          case 11706:
          case 11708:
          case 13754:
            return new Object[] {650, "Boss Points"};
          case 15017:
          case 15018:
          case 15019:
          case 15020:
          case 15220:
            return new Object[] {250, "Boss Points"};
          case 6731:
          case 6733:
          case 6735:
          case 6737:
            return new Object[] {100, "Boss Points"};
        }
      } else if (shop == CREDIT_STORE_3) {
        switch (item) {
          case 15272:
          case 11212:
            return new Object[] {5, "Credits"};
          case 15332:
            return new Object[] {30, "Credits"};
          case 12158:
            return new Object[] {10, "Credits"};
          case 4151:
            return new Object[] {750, "Credits"};
          case 11235:
            return new Object[] {500, "Credits"};
          case 11283:
            return new Object[] {1250, "Credits"};
          case 11732:
          case 6733:
          case 6735:
          case 6731:
          case 15486:
          case 6889:
            return new Object[] {500, "Credits"};
          case 20000:
          case 20001:
          case 20002:
            return new Object[] {3000, "Credits"};
          case 6580:
          case 15018:
          case 15019:
          case 15020:
          case 6585:
            return new Object[] {1000, "Credits"};
          case 15126:
            return new Object[] {1250, "Credits"};
          case 6737:
            return new Object[] {1500, "Credits"};
          case 15220:
            return new Object[] {3000, "Credits"};
        }
      } else if (shop == ENERGY_FRAGMENT_STORE) {
        switch (item) {
          case 5509:
            return new Object[] {400, "energy fragments"};
          case 5510:
            return new Object[] {750, "energy fragments"};
          case 5512:
            return new Object[] {1000, "energy fragments"};
          case 13613:
            return new Object[] {800, "energy fragments"};
          case 13619:
            return new Object[] {1000, "energy fragments"};
          case 13622:
            return new Object[] {1000, "energy fragments"};
          case 13623:
            return new Object[] {450, "energy fragments"};
        }
      } else if (shop == AGILITY_TICKET_STORE) {
        switch (item) {
          case 14936:
          case 14938:
            return new Object[] {60, "agility tickets"};
          case 10941:
          case 10939:
          case 10940:
          case 10933:
            return new Object[] {20, "agility tickets"};
          case 20786:
            return new Object[] {100, "agility tickets"};
        }
      } else if (shop == STARDUST_EXCHANGE_STORE) {
        switch (item) {
          case 6180:
          case 6181:
          case 6182:
            return new Object[] {2500, "stardust"};
          case 7409:
            return new Object[] {3500, "stardust"};
          case 20786:
            return new Object[] {5000, "stardust"};
          case 453:
            return new Object[] {5, "stardust"};
          case 9185:
            return new Object[] {250, "stardust"};
        }
      } else if (shop == GRAVEYARD_STORE) {
        switch (item) {
          case 18337:
            return new Object[] {350, "zombie fragments"};
          case 10551:
            return new Object[] {500, "zombie fragments"};
          case 10548:
          case 10549:
          case 10550:
            return new Object[] {200, "zombie fragments"};
          case 7592:
          case 7593:
          case 7594:
          case 7595:
          case 7596:
            return new Object[] {25, "zombie fragments"};
          case 15241:
            return new Object[] {500, "zombie fragments"};
          case 15243:
            return new Object[] {2, "zombie fragments"};
        }
      } else if (shop == TOKKUL_EXCHANGE_STORE) {
        switch (item) {
          case 11978:
            return new Object[] {300000, "tokkul"};
          case 438:
          case 436:
            return new Object[] {10, "tokkul"};
          case 440:
            return new Object[] {25, "tokkul"};
          case 453:
            return new Object[] {30, "tokkul"};
          case 442:
            return new Object[] {30, "tokkul"};
          case 444:
            return new Object[] {40, "tokkul"};
          case 447:
            return new Object[] {70, "tokkul"};
          case 449:
            return new Object[] {120, "tokkul"};
          case 451:
            return new Object[] {250, "tokkul"};
          case 1623:
            return new Object[] {20, "tokkul"};
          case 1621:
            return new Object[] {40, "tokkul"};
          case 1619:
            return new Object[] {70, "tokkul"};
          case 1617:
            return new Object[] {150, "tokkul"};
          case 1631:
            return new Object[] {1600, "tokkul"};
          case 6571:
            return new Object[] {50000, "tokkul"};
          case 11128:
            return new Object[] {22000, "tokkul"};
          case 6522:
            return new Object[] {20, "tokkul"};
          case 6524:
          case 6523:
          case 6526:
            return new Object[] {5000, "tokkul"};
          case 6528:
          case 6568:
            return new Object[] {800, "tokkul"};
        }
      } else if (shop == DUNGEONEERING_STORE) {
        switch (item) {
          case 18351:
          case 18349:
          case 18353:
          case 18357:
          case 18355:
          case 18359:
          case 18361:
          case 18363:
            return new Object[] {200000, "Dungeoneering tokens"};
          case 18344:
            return new Object[] {153000, "Dungeoneering tokens"};
          case 18839:
            return new Object[] {140000, "Dungeoneering tokens"};
          case 18346:
            return new Object[] {100000, "Dungeoneering tokens"};
          case 18335:
            return new Object[] {75000, "Dungeoneering tokens"};
          case 19669:
            return new Object[] {50000, "Dungeoneering tokens"};
          case 6500:
            return new Object[] {75000, "Dungeoneering tokens"};
        }
      } else if (shop == PRESTIGE_STORE) {
        switch (item) {
          case 14018:
            return new Object[] {90, "Prestige points"};
          case 19335:
            return new Object[] {30, "Prestige points"};
          case 15220:
          case 15020:
          case 15019:
          case 15018:
            return new Object[] {20, "Prestige points"};
          case 20000:
          case 20001:
          case 20002:
            return new Object[] {50, "Prestige points"};
          case 4084:
            return new Object[] {170, "Prestige points"};
          case 13855:
          case 13848:
          case 13856:
          case 13854:
          case 13853:
          case 138352:
          case 13851:
          case 13850:
          case 13849:
          case 13857:
            return new Object[] {5, "Prestige points"};
          case 10400:
          case 10402:
          case 10416:
          case 10418:
          case 10408:
          case 10410:
          case 10412:
          case 10414:
          case 10404:
          case 10406:
            return new Object[] {2, "Prestige points"};
          case 13852:
          case 14595:
          case 14603:
            return new Object[] {5, "Prestige points"};
          case 14602:
          case 14605:
            return new Object[] {3, "Prestige points"};
        }
      } else if (shop == SLAYER_STORE) {
        switch (item) {
          case 13263:
            return new Object[] {250, "Slayer points"};
          case 13281:
            return new Object[] {5, "Slayer points"};
          case 15403:
          case 11730:
          case 10887:
          case 15241:
            return new Object[] {300, "Slayer points"};
          case 11235:
          case 4151:
          case 15486:
            return new Object[] {250, "Slayer points"};
          case 15243:
            return new Object[] {3, "Slayer points"};
          case 10551:
            return new Object[] {200, "Slayer points"};
          case 2572:
            return new Object[] {500, "Slayer points"};
        }
      } else if (shop == IRON_SLAYER_STORE) {
        switch (item) {
          case 5574:
          case 5575:
          case 5576:
            return new Object[] {50, "Slayer points"};
          case 544:
          case 542:
            return new Object[] {10, "Slayer points"};
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
   * The inventory interface id, used to set the items right click values to 'sell'.
   */
  public static final int INVENTORY_INTERFACE_ID = 3823;

  /*
   * Declared shops
   */
  public static final int GENERAL_STORE = 12;
  public static final int RECIPE_FOR_DISASTER_STORE = 36;

  public static final int POS = 100;

  private static final int VOTING_REWARDS_STORE = 27;
  private static final int IRON_VOTING_REWARDS_STORE = 82;
  private static final int PKING_REWARDS_STORE = 26;
  private static final int CREDIT_STORE_1 = 79;
  private static final int CREDIT_STORE_2 = 80;
  private static final int CREDIT_STORE_3 = 81;
  private static final int BOSS_POINT_STORE = 84;
  private static final int DONATOR_STORE = 54;
  private static final int DONATOR_STORE_MISC = 55;
  private static final int ENERGY_FRAGMENT_STORE = 33;
  private static final int AGILITY_TICKET_STORE = 39;
  private static final int GRAVEYARD_STORE = 42;
  private static final int TOKKUL_EXCHANGE_STORE = 43;
  private static final int STARDUST_EXCHANGE_STORE = 78;
  private static final int SKILLCAPE_STORE_1 = 8;
  private static final int SKILLCAPE_STORE_2 = 9;
  private static final int SKILLCAPE_STORE_3 = 10;
  private static final int GAMBLING_STORE = 41;
  private static final int DUNGEONEERING_STORE = 44;
  private static final int PRESTIGE_STORE = 46;
  private static final int SLAYER_STORE = 47;
  private static final int IRON_SLAYER_STORE = 85;
}
