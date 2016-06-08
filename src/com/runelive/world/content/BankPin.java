package com.runelive.world.content;

import com.runelive.GameSettings;
import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.GameMode;
import com.runelive.net.security.ConnectionHandler;
import com.runelive.util.Misc;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.content.dialogue.impl.Tutorial;
import com.runelive.world.entity.impl.player.Player;

/**
 * account-pin
 * 
 * @author Gabriel Hannason NOTE: This was taken & redone from my PI base
 */
public class BankPin {

  public static void deletePin(Player player) {
    player.getBankPinAttributes().setHasBankPin(false).setHasEnteredBankPin(false)
        .setInvalidAttempts(0).setLastAttempt(System.currentTimeMillis());
    PlayerLogs.pins(player, "Delete");
    for (int i = 0; i < player.getBankPinAttributes().getBankPin().length; i++) {
      player.getBankPinAttributes().getBankPin()[i] = -1;
      player.getBankPinAttributes().getEnteredBankPin()[i] = -1;
    }
    player.getPacketSender().sendMessage("Your account-pin was deleted.").sendInterfaceRemoval();
  }

  public static void init(Player player, boolean openBankAfter) {
	if(!player.getBankPinAttributes().hasBankPin()) {
		player.getPacketSender().sendMessage("<col=ff0000>Enter a pin to set for your account.");
	}
    if (player.getBankPinAttributes().getInvalidAttempts() == 3) {
      if (System.currentTimeMillis() - player.getBankPinAttributes().getLastAttempt() < 400000) {
        player.getPacketSender()
            .sendMessage(
                "You must wait "
                    + (int) ((400 - (System.currentTimeMillis()
                        - player.getBankPinAttributes().getLastAttempt()) * 0.001))
                    + " seconds before attempting to enter your account-pin again.");
        return;
      } else
        player.getBankPinAttributes().setInvalidAttempts(0);
      player.getPacketSender().sendInterfaceRemoval();
    }
    player.setOpenBank(openBankAfter);
    randomizeNumbers(player);
    player.getPacketSender().sendString(15313, "First click the FIRST digit");
    player.getPacketSender().sendString(14923, "");
    player.getPacketSender().sendString(14913, "?");
    player.getPacketSender().sendString(14914, "?");
    player.getPacketSender().sendString(14915, "?");
    player.getPacketSender().sendString(14916, "?");
    sendPins(player);
    player.getPacketSender().sendInterface(7424);
    for (int i = 0; i < player.getBankPinAttributes().getEnteredBankPin().length; i++)
      player.getBankPinAttributes().getEnteredBankPin()[i] = -1;
  }

  public static void clickedButton(Player player, int button) {
    sendPins(player);
    if (player.getBankPinAttributes().getEnteredBankPin()[0] == -1) {
      player.getPacketSender().sendString(15313, "Now click the SECOND digit");
      player.getPacketSender().sendString(14913, "*");
      for (int i = 0; i < actionButtons.length; i++)
        if (actionButtons[i] == button)
          player.getBankPinAttributes().getEnteredBankPin()[0] =
              player.getBankPinAttributes().getBankPins()[i];
    } else if (player.getBankPinAttributes().getEnteredBankPin()[1] == -1) {
      player.getPacketSender().sendString(15313, "Now click the THIRD digit");
      player.getPacketSender().sendString(14914, "*");
      for (int i = 0; i < actionButtons.length; i++)
        if (actionButtons[i] == button)
          player.getBankPinAttributes().getEnteredBankPin()[1] =
              player.getBankPinAttributes().getBankPins()[i];
    } else if (player.getBankPinAttributes().getEnteredBankPin()[2] == -1) {
      player.getPacketSender().sendString(15313, "Now click the FINAL digit");
      player.getPacketSender().sendString(14915, "*");
      for (int i = 0; i < actionButtons.length; i++)
        if (actionButtons[i] == button)
          player.getBankPinAttributes().getEnteredBankPin()[2] =
              player.getBankPinAttributes().getBankPins()[i];
    } else if (player.getBankPinAttributes().getEnteredBankPin()[3] == -1) {
      player.getPacketSender().sendString(14916, "*");
      for (int i = 0; i < actionButtons.length; i++)
        if (actionButtons[i] == button)
          player.getBankPinAttributes().getEnteredBankPin()[3] =
              player.getBankPinAttributes().getBankPins()[i];
      if (!player.getBankPinAttributes().hasBankPin()) {
        player.getBankPinAttributes().setHasBankPin(true).setHasEnteredBankPin(true)
            .setBankPin(player.getBankPinAttributes().getEnteredBankPin());
        player.getPacketSender()
            .sendMessage("You've created a account-pin. Your digit is "
                + player.getBankPinAttributes().getEnteredBankPin()[0] + "-"
                + player.getBankPinAttributes().getEnteredBankPin()[1] + "-"
                + player.getBankPinAttributes().getEnteredBankPin()[2] + "-"
                + player.getBankPinAttributes().getEnteredBankPin()[3] + ". Please write it down.");
        PlayerLogs.pins(player, "Created");
        player.getPacketSender().sendInterfaceRemoval();
        player.setLastBankSerial(""+player.getSerialNumber());
        player.setLastBankIp(player.getHostAddress());
        if(player.continueTutorial()) {
        	  DialogueManager.start(player, Tutorial.get(player, 16));
        } else if(player.continueLoginAccountPin()) {
        		player.setPlayerLocked(false);
        } else if(player.continueSkipTutorial()) {
        	player.setNewPlayer(false);
			player.getPacketSender().sendInterfaceRemoval();
			if(ConnectionHandler.getStarters(player.getHostAddress()) <= GameSettings.MAX_STARTERS_PER_IP) {
				if(player.getGameMode() != GameMode.NORMAL) {
					player.getInventory().add(995, 10000).add(1153, 1).add(1115, 1).add(1067, 1).add(1323, 1).add(1191, 1).add(841, 1).add(882, 50).add(1167, 1).add(1129, 1).add(1095, 1).add(1063, 1).add(579, 1).add(577, 1).add(1011, 1).add(1379, 1).add(556, 50).add(558, 50).add(557, 50).add(555, 50).add(1351, 1).add(1265, 1).add(1712, 1).add(11118, 1).add(1007, 1).add(1061, 1).add(330, 100).add(16127, 1);
				} else {
					player.getInventory().add(995, 5000000).add(1153, 1).add(1115, 1).add(1067, 1).add(1323, 1).add(1191, 1).add(841, 1).add(882, 1000).add(1167, 1).add(1129, 1).add(1095, 1).add(1063, 1).add(579, 1).add(577, 1).add(1011, 1).add(1379, 1).add(556, 1000).add(558, 1000).add(557, 1000).add(555, 1000).add(1351, 1).add(1265, 1).add(1712, 1).add(11118, 1).add(1007, 1).add(1061, 1).add(386, 100).add(16127, 1);
				}
				player.getPacketSender().sendMessage("<col=FF0066>You've been given a Novite 2h! It is untradeable and you will keep it on death.");
				ConnectionHandler.addStarter(player.getHostAddress(), true);
				player.setReceivedStarter(true);
			} else {
				player.getPacketSender().sendMessage("Your connection has received enough starting items.");
			}
			player.getPacketSender().sendInterface(3559);
			player.getAppearance().setCanChangeAppearance(true);
			player.setPlayerLocked(false);
			TaskManager.submit(new Task(20, player, false) {
				@Override
				protected void execute() {
					if(player != null && player.isRegistered()) {
						player.getPacketSender().sendMessage("<img=4> @blu@Want to go player killing? Mandrith now sells premade PvP sets.");
						player.getPacketSender().sendMessage("<img=4> @blu@Join 'RuneLive' clan chat for help!");
					}
					stop();
				}
			});
			player.save();
        }
        return;
      }
      for (int i = 0; i < player.getBankPinAttributes().getEnteredBankPin().length; i++) {
        if (player.getBankPinAttributes().getEnteredBankPin()[i] != player.getBankPinAttributes()
            .getBankPin()[i]) {
          player.getPacketSender().sendInterfaceRemoval();
          int invalidAttempts = player.getBankPinAttributes().getInvalidAttempts() + 1;
          if (invalidAttempts >= 3)
            player.getBankPinAttributes().setLastAttempt(System.currentTimeMillis());
          player.getBankPinAttributes().setInvalidAttempts(invalidAttempts);
          PlayerLogs.pins(player, "Invalid entered");
          player.getPacketSender().sendMessage("Invalid account-pin entered entered.");
          return;
        }
      }
      PlayerLogs.pins(player, "Entered");
      player.getBankPinAttributes().setInvalidAttempts(0).setHasEnteredBankPin(true);
      if (player.openBank()) {
        player.getBank(0).open();
      } else if(player.continueTutorial()) {
    	  DialogueManager.start(player, Tutorial.get(player, 16));
      } else {
        player.getPacketSender().sendInterfaceRemoval();
      }
      player.setLastBankSerial(""+player.getSerialNumber());
      player.setLastBankIp(player.getHostAddress());
    }
    randomizeNumbers(player);
  }

  private static void sendPins(Player player) {
    for (int i = 0; i < player.getBankPinAttributes().getBankPins().length; i++)
      player.getPacketSender().sendString(stringIds[i],
          "" + player.getBankPinAttributes().getBankPins()[i]);
  }

  public static void randomizeNumbers(Player player) {
    int i = Misc.getRandom(5);
    switch (i) {
      case 0:
        player.getBankPinAttributes().getBankPins()[0] = 1;
        player.getBankPinAttributes().getBankPins()[1] = 7;
        player.getBankPinAttributes().getBankPins()[2] = 0;
        player.getBankPinAttributes().getBankPins()[3] = 8;
        player.getBankPinAttributes().getBankPins()[4] = 4;
        player.getBankPinAttributes().getBankPins()[5] = 6;
        player.getBankPinAttributes().getBankPins()[6] = 5;
        player.getBankPinAttributes().getBankPins()[7] = 9;
        player.getBankPinAttributes().getBankPins()[8] = 3;
        player.getBankPinAttributes().getBankPins()[9] = 2;
        break;

      case 1:
        player.getBankPinAttributes().getBankPins()[0] = 5;
        player.getBankPinAttributes().getBankPins()[1] = 4;
        player.getBankPinAttributes().getBankPins()[2] = 3;
        player.getBankPinAttributes().getBankPins()[3] = 7;
        player.getBankPinAttributes().getBankPins()[4] = 8;
        player.getBankPinAttributes().getBankPins()[5] = 6;
        player.getBankPinAttributes().getBankPins()[6] = 9;
        player.getBankPinAttributes().getBankPins()[7] = 2;
        player.getBankPinAttributes().getBankPins()[8] = 1;
        player.getBankPinAttributes().getBankPins()[9] = 0;
        break;

      case 2:
        player.getBankPinAttributes().getBankPins()[0] = 4;
        player.getBankPinAttributes().getBankPins()[1] = 7;
        player.getBankPinAttributes().getBankPins()[2] = 6;
        player.getBankPinAttributes().getBankPins()[3] = 5;
        player.getBankPinAttributes().getBankPins()[4] = 2;
        player.getBankPinAttributes().getBankPins()[5] = 3;
        player.getBankPinAttributes().getBankPins()[6] = 1;
        player.getBankPinAttributes().getBankPins()[7] = 8;
        player.getBankPinAttributes().getBankPins()[8] = 9;
        player.getBankPinAttributes().getBankPins()[9] = 0;
        break;

      case 3:
        player.getBankPinAttributes().getBankPins()[0] = 9;
        player.getBankPinAttributes().getBankPins()[1] = 4;
        player.getBankPinAttributes().getBankPins()[2] = 2;
        player.getBankPinAttributes().getBankPins()[3] = 7;
        player.getBankPinAttributes().getBankPins()[4] = 8;
        player.getBankPinAttributes().getBankPins()[5] = 6;
        player.getBankPinAttributes().getBankPins()[6] = 0;
        player.getBankPinAttributes().getBankPins()[7] = 3;
        player.getBankPinAttributes().getBankPins()[8] = 1;
        player.getBankPinAttributes().getBankPins()[9] = 5;
        break;

      case 4:
        player.getBankPinAttributes().getBankPins()[0] = 8;
        player.getBankPinAttributes().getBankPins()[1] = 7;
        player.getBankPinAttributes().getBankPins()[2] = 6;
        player.getBankPinAttributes().getBankPins()[3] = 2;
        player.getBankPinAttributes().getBankPins()[4] = 5;
        player.getBankPinAttributes().getBankPins()[5] = 4;
        player.getBankPinAttributes().getBankPins()[6] = 1;
        player.getBankPinAttributes().getBankPins()[7] = 0;
        player.getBankPinAttributes().getBankPins()[8] = 3;
        player.getBankPinAttributes().getBankPins()[9] = 9;
        break;
    }
    sendPins(player);
  }

  private static final int stringIds[] =
      {14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892};

  private static final int actionButtons[] =
      {14873, 14874, 14875, 14876, 14877, 14878, 14879, 14880, 14881, 14882};

  public static class BankPinAttributes {
    public BankPinAttributes() {}

    private boolean hasBankPin;
    private boolean hasEnteredBankPin;
    private int[] bankPin = new int[4];
    private int[] enteredBankPin = new int[4];
    private int bankPins[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private int invalidAttempts;
    private long lastAttempt;

    public boolean hasBankPin() {
      return hasBankPin;
    }

    public BankPinAttributes setHasBankPin(boolean hasBankPin) {
      this.hasBankPin = hasBankPin;
      return this;
    }

    public boolean hasEnteredBankPin() {
      return hasEnteredBankPin;
    }

    public BankPinAttributes setHasEnteredBankPin(boolean hasEnteredBankPin) {
      this.hasEnteredBankPin = hasEnteredBankPin;
      return this;
    }

    public boolean onDifferent(Player player) {
      String last_ip = player.getLastBankIp();
      String current_ip = player.getHostAddress();
      String last_serial = player.getLastBankSerial();
      String current_serial = ""+player.getSerialNumber();
      boolean on_different = false;
      if (!last_ip.equals(current_ip) && !last_serial.equals(current_serial)) {
        on_different = true;
      }
      return on_different;
    }

    public int[] getBankPin() {
      return bankPin;
    }

    public BankPinAttributes setBankPin(int[] bankPin) {
      this.bankPin = bankPin;
      return this;
    }
	
    public BankPinAttributes setBankPin(int index, int bankPin) {
      this.bankPin[index] = bankPin;
      return this;
    }

    public int[] getEnteredBankPin() {
      return enteredBankPin;
    }

    public int[] getBankPins() {
      return bankPins;
    }

    public int getInvalidAttempts() {
      return invalidAttempts;
    }

    public BankPinAttributes setInvalidAttempts(int invalidAttempts) {
      this.invalidAttempts = invalidAttempts;
      return this;
    }

    public long getLastAttempt() {
      return lastAttempt;
    }

    public BankPinAttributes setLastAttempt(long lastAttempt) {
      this.lastAttempt = lastAttempt;
      return this;
    }
  }
}
