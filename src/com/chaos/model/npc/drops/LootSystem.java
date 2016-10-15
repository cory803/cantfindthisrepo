package com.chaos.model.npc.drops;

import com.chaos.GameServer;
import com.chaos.GameSettings;
import com.chaos.model.GroundItem;
import com.chaos.model.Item;
import com.chaos.model.Locations;
import com.chaos.model.Position;
import com.chaos.model.container.impl.Bank;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.definitions.NpcDefinition;
import com.chaos.util.Misc;
import com.chaos.world.World;
import com.chaos.world.content.DropLog;
import com.chaos.world.content.Kraken;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.content.skill.impl.prayer.Prayer;
import com.chaos.world.content.skill.impl.summoning.CharmingImp;
import com.chaos.world.entity.impl.GroundItemManager;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.KnightLamp;
import org.scripts.kotlin.content.dialog.KrakenLoot;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/13/2016.
 *
 * @author Seba
 */
public class LootSystem {

    /**
     * Holds all of the drop table information
     */
    public static Map<Integer, LootTable> tables = new HashMap<>();

    /**
     * This holds the rare drop table information.
     */
    public static final int[][][] RARE_DROP_TABLE = {

    };

    /**
     * We are going to use our own {@link Random} for our drop system.
     */
    private static Random dice = new Random();

    /**
     * Creates our instance to {@link LootAnnouncement}
     */
    private static final LootAnnouncement announcement = new LootAnnouncement();

    /**
     * Used to access the announcement class.
     * @return
     */
    public static LootAnnouncement getAnnouncment() {
        return announcement;
    }

    /**
     * Loads the drop tables for the NPC
     */
    public static void loadDropTables() {
        tables.clear();
        try (DataInputStream stream = new DataInputStream(new FileInputStream(new File("./data/def/npcDrops.dat")))) {
            while (stream.available() > 0) {
                LootTable table = new LootTable();
                int mainId = stream.readShort();
                table.setRareTable(stream.readBoolean());

                if (stream.readByte() == 1) {
                    int total = 0;
                    for (int i = 0; i < LootCharm.CHARM.values().length; i++) {
                        int amt = stream.readByte();
                        total += stream.readByte();
                        if (i >= 7) {
                            continue;
                        }
                        table.setCharms(new LootCharm(amt, total), i);
                    }
                }

                table.setClueChance(stream.readFloat());

                int initSize = stream.readByte();
                LootItem[][] loot = new LootItem[initSize][];
                for (int i = 0; i < initSize; i++) {
                    int subSize = stream.readShort();
                    loot[i] = new LootItem[subSize];
                    for (int t = 0; t < subSize; t++) {
                        loot[i][t] = new LootItem(stream.readShort(), stream.readInt(), stream.readInt(), stream.readByte());
                    }
                }
                table.setLoot(loot);

                initSize = stream.readByte();
                for (int k = 0; k < initSize; k++) {
                    tables.put((int) stream.readShort(), table);
                }
                tables.put(mainId, table);
            }
            GameServer.getLogger().log(Level.INFO, "Loaded..." + tables.size() + " npc drop tables.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes our drop method for the player given the killed npc.
     * @param p {@link Player} The player that killed the {@link NPC}
     * @param n {@link NPC} The npc that has passed. Poor guy.
     */
    public static void drop(Player p, NPC n) {
        if (p == null) {
            return;
        }

        LootTable table = tables.get(n.getId());

        if (table == null) {
            return;
        }

        final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;

        /**
         * Drop charms
         */
        if (table.getCharms() != null) {
            dropCharm(p, n.getPosition().copy(), table.getCharms());
        }

        /**
         * Handle random caskets, keys, and wilderness scrolls.
         */
        if (goGlobal) {
            if (p.getLocation() == Locations.Location.WILDERNESS) {
                wildKeys(p, n.getDefinition().getCombatLevel(), n.getPosition().copy());
            }
        }

        /**
         * Always dropped items
         */
        for (int i = 0; i < table.getSortedLoot()[0].length; i++) {
            if (table.getSortedLoot()[0][i] != null && (meetsCondition(p, table.getSortedLoot()[0][i].getCondition(), table.getSortedLoot()[0][i].getId(), n))) {
                int amt = table.getSortedLoot()[0][i].getRandomAmount();

                if (GameSettings.DOUBLE_DROPS) {
                    amt += table.getSortedLoot()[0][i].getRandomAmount();
                }

                if(p.getInventory().contains(18337)) {
                    if (Prayer.isBone(table.getSortedLoot()[0][i].getId())) {
                        Prayer.crushBone(p, table.getSortedLoot()[0][i].getId());
                    }
                } else {
                    Player clanLooter = ClanChatManager.lootshare(p, n.getPosition().copy(), table.getSortedLoot()[0][i].getId(), amt);
                    if(p.getLocation() == Locations.Location.KRAKEN && clanLooter != null) {
                        GroundItemManager.spawnGroundItem(clanLooter, new GroundItem(new Item(table.getSortedLoot()[0][i].getId(), amt), clanLooter.getPosition(), p.getUsername(), false, 150, goGlobal, 200));
                        clanLooter.getKraken().setKrakenStage(Kraken.KrakenStage.DEFEATED);
                        clanLooter.getKraken().stopTentacleAttack();
                        clanLooter.setNpcClickId(6656);
                        clanLooter.getDialog().sendDialog(new KrakenLoot(clanLooter));
                        clanLooter.getPacketSender().sendMessage("Your loot from defeating Kraken has been placed under you.");
                    } else {
                        GroundItemManager.spawnGroundItem(clanLooter, new GroundItem(new Item(table.getSortedLoot()[0][i].getId(), amt), n.getPosition(), p.getUsername(), false, 150, goGlobal, 200));
                    }
                }
            }
        }

        int dropCount = 1;

        /**
         * Loops how many times we drop a random item.
         */
        for (int t = 0; t < dropCount; t++) {
            Item roll = rollDrop(p, n, table.getSortedLoot(), n.getId());

            if (roll != null) {
                Player pl = ClanChatManager.lootshare(p, n.getPosition().copy(), roll.getId(), roll.getAmount());
                if(p.getLocation() == Locations.Location.KRAKEN) {
                    GroundItemManager.spawnGroundItem(pl, new GroundItem(roll, p.getPosition(), p.getUsername(), false, 150, goGlobal, 200));
                    p.getKraken().setKrakenStage(Kraken.KrakenStage.DEFEATED);
                    p.getKraken().stopTentacleAttack();
                    p.setNpcClickId(6656);
                    p.getDialog().sendDialog(new KrakenLoot(p));
                    p.getPacketSender().sendMessage("Your loot from defeating Kraken has been placed under you.");
                } else {
                    GroundItemManager.spawnGroundItem(pl, new GroundItem(roll, n.getPosition(), p.getUsername(), false, 150, goGlobal, 200));
                }
                DropLog.submit(pl, new DropLog.DropLogEntry(roll.getId(), roll.getAmount()));
                announcement.sendAnnouncment(pl.getUsername(), roll, n.getDefinition().getName());
            }
        }
    }

    /**
     * Checks to make sure if the player has met the specifications for the drop.
     * @param player {@link Player} The player we need to check if they met the requirements
     * @param c {@link com.chaos.model.npc.drops.LootItem.CONDITION} The condition we are checking
     * @param item The item id of the drop.
     * @param n {@link NPC} Used for checking if the player is on a slayer task.
     * @return
     */
    private static boolean meetsCondition(Player player, LootItem.CONDITION c, int item, NPC n) {
        switch (c) {

            case NONE:
                return true;

            case TASK:
                return player.getSlayer().onSlayerTask(n.getId());

            case ONE_ITEM: {
                for (Bank bank : player.getBanks()) {
                    if (bank.contains(item)) {
                        return false;
                    }
                }
                if (player.getInventory().contains(item)) {
                    return false;
                }
                if (player.getSummoning().getBeastOfBurden().contains(item)) {
                    return false;
                }
                return true;
            }

            case DONOR:
                return player.getDonatorRights().isDonator();

        }
        return false;
    }

    /**
     * Rolls the drop table
     * 1/4 chance to get a specific table.
     * 1/x chance to get a specific drop in that table.
     * Tables:
     * 1 = common
     * 2 = uncommon
     * 3 = rare
     * 4 = very rare
     * 5 = epic
     */
    public static Item rollDrop(Player player, NPC npc, LootItem[][] table, int npcId) {
        //Table chance (1/4)
        double extraChance = 0;
        if(player != null) {
            extraChance = player.getGameModeAssistant().getGameMode().getMonsterDropRate();
            if(player.getEquipment().get(Equipment.RING_SLOT).getId() == 2572) {
                extraChance += .5;
            } else if(player.getEquipment().get(Equipment.RING_SLOT).getId() == 21110) {
                extraChance += .10;
            }
        }
        Item item = null;
        int tableChance = Misc.inclusiveRandom(2, 5);
        for (int i = 2; i < table.length; i++) {
            if(table[i].length > 0) {
            } else {
                continue;
            }
            //Uncommon drops (1/10)
            if(tableChance == 2) {
                if(i == 2) {
                    int chance = Misc.inclusiveRandom(1, 5);
                    if (chance == 5) {
                        LootItem loot = table[i][Misc.inclusiveRandom(0, table[i].length - 1)];
                        item = new Item(loot.getId(), loot.getRandomAmount());
                    }
                }
            }
            //Rare drops (1/50)
            if(tableChance == 3) {
                if(i == 3) {
                    int rarity = NpcDefinition.forId(npcId).getRare();
                    double conversion = ((int) extraChance * rarity / 4);
                    int chance = Misc.inclusiveRandom(1, rarity / 4 - ((int) conversion));
                    if (chance == 5) {
                        LootItem loot = table[i]
                                [Misc.inclusiveRandom(0, table[i].length - 1)];
                        item = new Item(loot.getId(), loot.getRandomAmount());
                    }
                }
            }
            //Very rare drops (1/200)
            if(tableChance == 4) {
                if(i == 4) {
                    int rarity = NpcDefinition.forId(npcId).getVeryRare();
                    double conversion = ((int) extraChance * rarity / 4);
                    int chance = Misc.inclusiveRandom(1, rarity / 4 - ((int) conversion));
                    if (chance == 5) {
                        LootItem loot = table[i][Misc.inclusiveRandom(0, table[i].length - 1)];
                        item = new Item(loot.getId(), loot.getRandomAmount());
                    }
                }
            }
            //Epic drops (1/1000)
            if(tableChance == 5) {
                if(i == 5) {
                    int rarity = NpcDefinition.forId(npcId).getEpicRare();
                    double conversion = (extraChance * rarity / 4);
                    int chance = Misc.inclusiveRandom(1, rarity / 4 - ((int) conversion));
                    if (chance == 5) {
                        LootItem loot = table[i][Misc.inclusiveRandom(0, table[i].length - 1)];
                        item = new Item(loot.getId(), loot.getRandomAmount());
                    }
                }
            }
        }
        return item;
    }

    private static void dropCharm(Player player, Position p, LootCharm[] charms) {
        int rolledCharm = dice.nextInt(101);
        for (int i = 0; i < charms.length; i++) {
            LootCharm charm = charms[i];

            /**
             * Check our chances of dropping some charms.
             */
            if (rolledCharm <= charm.getRarity()) {

                int amt = charm.getAmount();

                /**
                 * Check for a double drop event
                 */
                if (GameSettings.DOUBLE_DROPS) {
                    amt *= 2;
                }

                /**
                 * Get a player to get the drop. Lootshare support
                 */
                Player pl = ClanChatManager.lootshare(player, p, LootCharm.CHARM.values()[i].getCharm(), amt);

                /**
                 * Checks if a player has a Charming Imp
                 */
                if (player.getInventory().contains(6500) && CharmingImp.handleCharmDrop(pl, LootCharm.CHARM.values()[i].getCharm(), amt)) {
                    return;
                }
                if(pl.getLocation() == Locations.Location.KRAKEN) {
                    GroundItemManager.spawnGroundItem(pl, new GroundItem(new Item(LootCharm.CHARM.values()[i].getCharm(), amt), pl.getPosition(), pl.getUsername(), false, 150, true, 200));
                } else {
                    GroundItemManager.spawnGroundItem(pl, new GroundItem(new Item(LootCharm.CHARM.values()[i].getCharm(), amt), p, pl.getUsername(), false, 150, true, 200));
                }
                return;
            }
        }
    }

    private static int selectKey(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private static void wildKeys(Player player, int combat, Position pos) {
        //TODO: Redo wilderness key systems
        if (Misc.getRandom(combat <= 50 ? 400 : 350) < 5) {
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(selectKey(player.allKeys)), pos,
                    player.getUsername(), false, 150, true, 200));
            String message = "<icon=1><col=FF8C38>" + player.getUsername()
                    + " has just received a wilderness key as a random drop!";
            World.sendMessage(message);
        }
    }
}
