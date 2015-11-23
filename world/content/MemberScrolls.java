package com.strattus.world.content;

import com.strattus.model.PlayerRights;
import com.strattus.util.Misc;
import com.strattus.world.content.dialogue.Dialogue;
import com.strattus.world.content.dialogue.DialogueExpression;
import com.strattus.world.content.dialogue.DialogueManager;
import com.strattus.world.content.dialogue.DialogueType;
import com.strattus.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getRights().isStaff()) {
			return;
		}
		PlayerRights rights = null;
		if(player.getAmountDonated() >= 10)
			rights = PlayerRights.DONATOR;
		if(player.getAmountDonated() >= 50)
			rights = PlayerRights.SUPER_DONATOR;
		if(player.getAmountDonated() >= 100)
			rights = PlayerRights.EXTREME_DONATOR;
		if(player.getAmountDonated() >= 250)
			rights = PlayerRights.DIAMOND_DONATOR;
		if(player.getAmountDonated() >= 1000)
			rights = PlayerRights.ONYX_DONATOR;
		if(rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage("You've become a "+Misc.formatText(rights.toString().toLowerCase())+"! Congratulations!");
			player.setRights(rights);
			player.getPacketSender().sendRights();
		}
	}

	public static boolean handleScroll(Player player, int item) {
		switch(item) {
		case 10942:
		case 10934:
		case 10935:
		case 10943:
			int funds = item == 10942 ? 10 : item == 10934 ? 20 : item == 10935 ? 50 : item == 10943 ? 100 : -1;
			player.getInventory().delete(item, 1);
			player.incrementAmountDonated(funds);
			player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
			checkForRankUpdate(player);
			PlayerPanel.refreshPanel(player);
			break;
		}
		return false;
	}
	
	public static Dialogue getTotalFunds(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public int npcId() {
				return 4657;
			}

			@Override
			public String[] dialogue() {
				return player.getAmountDonated() > 0 ? new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total.", "Thank you for supporting us!"} : new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total."};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(5);
			}
		};
	}
}
