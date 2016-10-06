package org.scripts.kotlin.content.dialog.Tutorial;

import com.chaos.model.Direction;
import com.chaos.model.Flag;
import com.chaos.model.Item;
import com.chaos.model.Position;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.definitions.WeaponAnimations;
import com.chaos.model.definitions.WeaponInterfaces;
import com.chaos.model.options.fouroption.FourOption;
import com.chaos.model.options.twooption.TwoOption;
import com.chaos.model.player.GameMode;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.model.player.dialog.DialogMessage;
import com.chaos.net.packet.impl.EquipPacketListener;
import com.chaos.world.content.PlayerPanel;
import com.chaos.world.entity.impl.player.Player;
import com.chaos.world.entity.updating.PlayerUpdating;
import org.scripts.kotlin.content.dialog.BankPin.BankPinDial;
import org.scripts.kotlin.content.dialog.BankPin.BankPinTut;

public class StartTutorial extends Dialog {

    public Dialog dialog = this;
    Player p;
    public StartTutorial(Player player) {
        super(player);
        setEndState(7);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello, Welcome to Chaos!\\nWhat Game Mode would you like?");
            case 1:
                return Dialog.createOption(new FourOption(
                        "Play in @red@Knight@bla@ mode (@blu@1000XP@bla@ Per Hit)",
                        "Play in @red@Realism@bla@ mode (@blu@10XP@bla@ Per Hit)",
                        "Play in @red@Ironman@bla@ mode (@blu@No trading & 100XP Per Hit@bla@)",
                        "Learn about these modes") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                player.getGameModeAssistant().setGameMode(GameMode.KNIGHT);
                                PlayerPanel.refreshPanel(player);
                                //Equipment
                                player.getEquipment().set(Equipment.HEAD_SLOT, new Item(1153, 1));
                                player.getEquipment().set(Equipment.BODY_SLOT, new Item(1115, 1));
                                player.getEquipment().set(Equipment.LEG_SLOT, new Item(1067, 1));
                                player.getEquipment().set(Equipment.FEET_SLOT, new Item(3105, 1));
                                player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(1191, 1));
                                player.getEquipment().set(Equipment.HANDS_SLOT, new Item(11118, 1));
                                player.getEquipment().set(Equipment.RING_SLOT, new Item(2552, 1));
                                player.getEquipment().set(Equipment.CAPE_SLOT, new Item(1052, 1));
                                player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(1323, 1));
                                player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1712, 1));
                                player.getEquipment().refreshItems();
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                EquipPacketListener.resetWeapon(player);

                                //Inventory
                                player.getInventory().add(new Item(7947, 50), true);
                                player.getInventory().add(new Item(386, 25), true);
                                player.getInventory().add(new Item(2434, 2), true);
                                player.getInventory().add(new Item(841, 1), true);
                                player.getInventory().add(new Item(843, 1), true);
                                player.getInventory().add(new Item(884, 250), true);
                                player.getInventory().add(new Item(886, 100), true);
                                player.getInventory().add(new Item(1169, 1), true);
                                player.getInventory().add(new Item(1129, 1), true);
                                player.getInventory().add(new Item(1095, 1), true);
                                player.getInventory().add(new Item(1381, 1), true);
                                player.getInventory().add(new Item(577, 1), true);
                                player.getInventory().add(new Item(1011, 1), true);
                                player.getInventory().add(new Item(579, 1), true);
                                player.getInventory().add(new Item(558, 200), true);
                                player.getInventory().add(new Item(554, 200), true);
                                player.getInventory().add(new Item(555, 200), true);
                                player.getInventory().add(new Item(557, 200), true);
                                player.getInventory().add(new Item(562, 75), true);
                                player.getInventory().add(new Item(1333, 1), true);
                                player.getInventory().add(new Item(995, 300000), true);
                                player.getInventory().add(new Item(10586, 1), true);
                                player.getInventory().refreshItems();

                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_4:
                                player.getGameModeAssistant().setGameMode(GameMode.REALISM);
                                PlayerPanel.refreshPanel(player);
                                //Equipment
                                player.getEquipment().set(Equipment.HEAD_SLOT, new Item(1153, 1));
                                player.getEquipment().set(Equipment.BODY_SLOT, new Item(1115, 1));
                                player.getEquipment().set(Equipment.LEG_SLOT, new Item(1067, 1));
                                player.getEquipment().set(Equipment.FEET_SLOT, new Item(3105, 1));
                                player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(1191, 1));
                                player.getEquipment().set(Equipment.HANDS_SLOT, new Item(11118, 1));
                                player.getEquipment().set(Equipment.RING_SLOT, new Item(2552, 1));
                                player.getEquipment().set(Equipment.CAPE_SLOT, new Item(1052, 1));
                                player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(1323, 1));
                                player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1712, 1));
                                player.getEquipment().refreshItems();
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                EquipPacketListener.resetWeapon(player);

                                //Inventory
                                player.getInventory().add(new Item(7947, 50), true);
                                player.getInventory().add(new Item(386, 25), true);
                                player.getInventory().add(new Item(2434, 2), true);
                                player.getInventory().add(new Item(841, 1), true);
                                player.getInventory().add(new Item(843, 1), true);
                                player.getInventory().add(new Item(884, 250), true);
                                player.getInventory().add(new Item(886, 100), true);
                                player.getInventory().add(new Item(1169, 1), true);
                                player.getInventory().add(new Item(1129, 1), true);
                                player.getInventory().add(new Item(1095, 1), true);
                                player.getInventory().add(new Item(1381, 1), true);
                                player.getInventory().add(new Item(577, 1), true);
                                player.getInventory().add(new Item(1011, 1), true);
                                player.getInventory().add(new Item(579, 1), true);
                                player.getInventory().add(new Item(558, 200), true);
                                player.getInventory().add(new Item(554, 200), true);
                                player.getInventory().add(new Item(555, 200), true);
                                player.getInventory().add(new Item(557, 200), true);
                                player.getInventory().add(new Item(562, 75), true);
                                player.getInventory().add(new Item(1333, 1), true);
                                player.getInventory().add(new Item(995, 300000), true);
                                player.getInventory().add(new Item(16389, 1), true);
                                player.getInventory().refreshItems();

                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_4:
                                player.getGameModeAssistant().setGameMode(GameMode.IRONMAN);
                                PlayerPanel.refreshPanel(player);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_4:
                                //TODO: Open the url for game modes information
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createPlayer(DialogHandler.HAPPY, "I would like to play as a "+getPlayer().getGameModeAssistant().getGameMode().getModeName()+".");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM_CONTINUED, "A very interesting choice! Would you like to go through the Chaos tutorial to help you learn how to make money and other sorts of stuff?");
            case 4:
                return Dialog.createOption(new TwoOption(
                        "Yes, take the tutorial.",
                        "No, skip the tutorial.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                setState(5);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_2:
                                player.setNewPlayer(false);
                                player.setPlayerLocked(false);
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getDialog().sendDialog(new BankPinTut(player));
                                break;
                        }
                    }
                });
            case 5:
//                p.getPacketSender().sendCameraAngle(200,120, 2, 1, 103);
                p.moveTo(new Position(3182, 3676, 3));
                return Dialog.createNpc(DialogHandler.HAPPY, "We have an active Bounty Hunter with Revenants and Scorpia. There are also some resourace areas for skilling located inside the crator.");
            case 6:
                return Dialog.createNpc(DialogHandler.HAPPY, "There are specific areas on the teleports that designate areas just for skilling. It makes it easier to interact with other skillers!");
            case 7:
                p.setNewPlayer(false);
                p.setPlayerLocked(false);
                p.moveTo(new Position(3333, 3333, 0));
                p.setDirection(Direction.NORTH);
                return Dialog.createNpc(DialogHandler.HAPPY, "You will be placed in the global 'Chaos' clan chat and near a bank in the tutorial.");
        }
        return null;
    }
}