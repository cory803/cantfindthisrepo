package com.chaos.world.content;

import com.chaos.model.Item;
import com.chaos.world.entity.impl.player.Player;

import java.util.ArrayList;

/**
 * Created by tanne on 8/29/2016.
 */
public class Degrading {

    ArrayList<Item> barrowsItems = null;
    public ArrayList<Item> getBarrowsItems() {
        return barrowsItems;
    }

    public void deleteBarrowsAmour(Player player) {
        for (barrowsArmour arm: barrowsArmour.values()) {
            if (player.getInventory().contains(arm.getBarrowsID())) {
                player.getInventory().delete(arm.getBarrowsID(), 1);
                player.getInventory().add(arm.getDegradeID(), 1);
                barrowsItems.add(new Item(arm.getDegradeID()));
                System.out.println("Removed Item from inv: " + new Item(arm.getBarrowsID()).getDefinition().getName()
                        + " -- Added Degraded Item: " + new Item(arm.getDegradeID()).getDefinition().getName());
            }
            if (player.getEquipment().contains(arm.getBarrowsID())) {
                player.getEquipment().delete(arm.getBarrowsID(), 1);
                barrowsItems.add(new Item(arm.getDegradeID()));
                System.out.println("Removed Item from equip: " + new Item(arm.getBarrowsID()).getDefinition().getName()
                        + " -- Added Degraded Item: " + new Item(arm.getDegradeID()).getDefinition().getName());
            }
        }
    }

    public enum barrowsArmour {
        DH_HELM(4716, 4884, 60000),
        DH_AXE(4718, 4890, 100000),
        DH_BODY(4720, 4896, 90000),
        DH_LEGS(4722, 4902, 80000),
        VERAC_HELM(4753, 4980, 60000),
        VERAC_FLAIL(4755, 4986, 100000),
        VERAC_BODY(4757, 4992, 90000),
        VERAC_LEGS(4759, 4998, 80000),
        AHRIM_HOOD(4708, 4860, 60000),
        AHRIM_STAFF(4710, 4866, 100000),
        AHRIM_BODY(4712, 4872, 90000),
        AHRIM_LEGS(4714, 4878, 80000),
        GUTHAN_HELM(4724, 4908, 60000),
        GUTHAN_SPEAR(4726, 4914, 100000),
        GUTHAN_BODY(4728, 4920, 90000),
        GUTHAN_LEGS(4730, 4926, 80000);

        barrowsArmour(int barrowsID, int degradeID, int repairPrice) {
            this.barrowsID = barrowsID;
            this.degradeID = degradeID;
            this.repairPrice = repairPrice;
        }

        private int barrowsID;
        private int degradeID;
        private int repairPrice;

        public int getBarrowsID() {
            return barrowsID;
        }

        public int getDegradeID() {
            return degradeID;
        }

        public int getRepairPrice() {
            return repairPrice;
        }
    }
}
