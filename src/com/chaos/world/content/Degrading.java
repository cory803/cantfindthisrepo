package com.chaos.world.content;

import com.chaos.model.Flag;
import com.chaos.model.Item;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @Author Jonny, High105
 * Handles item degrading for items in Chaos.
 */

public class Degrading {

    public enum DegradingType {
        DEFAULT(),
        BARROWS()
    }

    public enum DegradingItems {
        DH_HELM(4716, 4884, 60000, DegradingType.BARROWS),
        DH_AXE(4718, 4890, 100000, DegradingType.BARROWS),
        DH_BODY(4720, 4896, 90000, DegradingType.BARROWS),
        DH_LEGS(4722, 4902, 80000, DegradingType.BARROWS),
        VERAC_HELM(4753, 4980, 60000, DegradingType.BARROWS),
        VERAC_FLAIL(4755, 4986, 100000, DegradingType.BARROWS),
        VERAC_BODY(4757, 4992, 90000, DegradingType.BARROWS),
        VERAC_LEGS(4759, 4998, 80000, DegradingType.BARROWS),
        AHRIM_HOOD(4708, 4860, 60000, DegradingType.BARROWS),
        AHRIM_STAFF(4710, 4866, 100000, DegradingType.BARROWS),
        AHRIM_BODY(4712, 4872, 90000, DegradingType.BARROWS),
        AHRIM_LEGS(4714, 4878, 80000, DegradingType.BARROWS),
        GUTHAN_HELM(4724, 4908, 60000, DegradingType.BARROWS),
        GUTHAN_SPEAR(4726, 4914, 100000, DegradingType.BARROWS),
        GUTHAN_BODY(4728, 4920, 90000, DegradingType.BARROWS),
        GUTHAN_LEGS(4730, 4926, 80000, DegradingType.BARROWS),
        ABYSSAL_TENTACLE(12006, -1, -1, DegradingType.DEFAULT);

        DegradingItems(int regularID, int degradeID, int repairPrice, DegradingType degradingType) {
            this.regularID = regularID;
            this.degradeID = degradeID;
            this.repairPrice = repairPrice;
            this.degradingType = degradingType;
        }

        private int regularID;
        private int degradeID;
        private int repairPrice;
        private DegradingType degradingType;

        /**
         * Gets the regular id for degrading items
         * @return
         */
        public int getRegularID() {
            return regularID;
        }

        /**
         * Gets the degraded item ids
         * @return
         */
        public int getDegradeID() {
            return degradeID;
        }

        /**
         * Gets the repair price to repair degraded items
         * @return
         */
        public int getRepairPrice() {
            return repairPrice;
        }

        /**
         * Gets the type of item you are degrading
         * @return
         */
        public DegradingType getDegradingType() {
            return degradingType;
        }
    }

    /**
     * Processes the actual degrade of the item.
     * @param player
     * @param isAttacker
     */
    public void processDegrade(Player player, boolean isAttacker) {
        for (DegradingItems degradingItems: DegradingItems.values()) {
            for (Item equipmentItem: player.getEquipment().getItems()) {
                if(equipmentItem.getId() == degradingItems.getRegularID()) {
                    ItemDefinition equipmentDefinition = ItemDefinition.forId(degradingItems.getRegularID());
                    if(isAttacker) {
                        if(equipmentDefinition.getEquipmentSlot() != Equipment.WEAPON_SLOT) {
                            continue;
                        }
                    } else if(!isAttacker) {
                        if(equipmentDefinition.getEquipmentSlot() == Equipment.WEAPON_SLOT) {
                            continue;
                        }
                    }
                    equipmentItem.decrementCharges(1);
                    switch (degradingItems.getDegradingType()) {
                        case BARROWS:
                            //TODO: Add barrows degrading
                            break;
                        case DEFAULT:
                            if(equipmentItem.getCharges() == 0) {
                                player.getEquipment().setItem(equipmentDefinition.getEquipmentSlot(), new Item(-1));
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                player.getPacketSender().sendMessage("<col=ff0000>Your "+equipmentDefinition.getName()+" has degraded fully and disappeared!");
                            }
                            break;
                    }
                } else if(equipmentItem.getId() == degradingItems.getDegradeID()) {
                    //TODO: Add barrows degrading
                }
            }
        }
    }

    /**
     * Tells you how many charges you have left on your item.
     * @param player
     * @param slot
     * @return
     */
    public boolean isCheckCharges(Player player, int itemId, int slot) {
        if(player.getInventory().getItems()[slot] == null) {
            return false;
        }
        for (DegradingItems degradingItems: DegradingItems.values()) {
            if(player.getInventory().getItems()[slot].getId() == itemId) {
                Item inventoryItem = player.getInventory().getItems()[slot];
                if (inventoryItem.getId() == degradingItems.getRegularID()) {
                    ItemDefinition equipmentDefinition = ItemDefinition.forId(degradingItems.getRegularID());
                    int currentCharges = inventoryItem.getCharges();
                    double percentage = (double) currentCharges / equipmentDefinition.getCharges() * 100;
                    DecimalFormat format = new DecimalFormat("#.##");
                    percentage = Double.valueOf(format.format(percentage));
                    player.getPacketSender().sendMessage("You have " + Misc.formatAmount(currentCharges) + "/" + Misc.formatAmount(equipmentDefinition.getCharges()) + " charges (" + percentage + "%) left on your " + equipmentDefinition.getName() + ".");
                    return true;
                }
            }
        }
        return false;
    }
}
