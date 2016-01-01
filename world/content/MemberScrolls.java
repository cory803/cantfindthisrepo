package com.ikov.world.content;

import com.ikov.model.PlayerRights;
import com.ikov.util.Misc;
import com.ikov.world.content.dialogue.Dialogue;
import com.ikov.world.content.dialogue.DialogueExpression;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.dialogue.DialogueType;
import com.ikov.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getRights().isStaff()) {
			return;
		}
		if(player.getAmountDonated() >= 10) {
			player.setDonorRights(1);
			player.getPacketSender().sendMessage("You've become a Regular Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 50) {
			player.setDonorRights(2);
			player.getPacketSender().sendMessage("You've become a Super Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 100) {
			player.setDonorRights(3);
			player.getPacketSender().sendMessage("You've become a Extreme Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 500) {
			player.setDonorRights(4);
			player.getPacketSender().sendMessage("You've become a Legendary Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 5000) {
			player.setDonorRights(5);
			player.getPacketSender().sendMessage("You've become a Uber Donator! Congratulations!");
		}
	}

	public static boolean handleScroll(Player player, int item) {
		int funds = 0;
		switch(item) {
		case 10935:
			player.setDialogueActionId(131);
			DialogueManager.start(player, 131);
			break;
		case 10943:
			player.setDialogueActionId(129);
			DialogueManager.start(player, 129);
			break;
		case 10934:
			player.setDialogueActionId(130);
			DialogueManager.start(player, 130);
			break;
		case 7629:
			player.setDialogueActionId(132);
			DialogueManager.start(player, 132);
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
