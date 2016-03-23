package com.ikov.world.content;

import com.ikov.world.content.dialogue.Dialogue;
import com.ikov.world.content.dialogue.DialogueExpression;
import com.ikov.world.content.dialogue.DialogueManager;
import com.ikov.world.content.dialogue.DialogueType;
import com.ikov.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getDonorRights() == 1) {
			if(player.getAmountDonated() < 10) {
				player.setAmountDonated(10);
			}
		}
		if(player.getDonorRights() == 2) {
			if(player.getAmountDonated() < 25) {
				player.setAmountDonated(25);
			}
		}
		if(player.getDonorRights() == 3) {
			if(player.getAmountDonated() < 50) {
				player.setAmountDonated(50);
			}
		}
		if(player.getDonorRights() == 4) {
			if(player.getAmountDonated() < 150) {
				player.setAmountDonated(150);
			}
		}
		if(player.getDonorRights() == 5) {
			if(player.getAmountDonated() < 500) {
				player.setAmountDonated(500);
			}
		}
		if(player.getAmountDonated() >= 10 && player.getAmountDonated() < 25 && player.getDonorRights() != 1) {
			player.setDonorRights(1);
			player.getPacketSender().sendMessage("You've become a Regular Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 25 && player.getAmountDonated() < 50 && player.getDonorRights() != 2) {
			player.setDonorRights(2);
			player.getPacketSender().sendMessage("You've become a Super Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 50 && player.getAmountDonated() < 150 && player.getDonorRights() != 3) {
			player.setDonorRights(3);
			player.getPacketSender().sendMessage("You've become a Extreme Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 150 && player.getAmountDonated() < 500 && player.getDonorRights() != 4) {
			player.setDonorRights(4);
			player.getPacketSender().sendMessage("You've become a Legendary Donator! Congratulations!");
		} else if(player.getAmountDonated() >= 500 && player.getDonorRights() != 5) {
			player.setDonorRights(5);
			player.getPacketSender().sendMessage("You've become a Uber Donator! Congratulations!");
		}
	}

	public static boolean handleScroll(Player player, int item) {
		int funds = 0;
		int credits = 0;
		switch(item) {
			case 10943:
				funds = 10;
				player.getInventory().delete(10943, 1);
				player.incrementAmountDonated(funds);
				player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
				MemberScrolls.checkForRankUpdate(player);
				credits = 10000;
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 10,000 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 10934:
				funds = 25;
				player.getInventory().delete(10934, 1);
				player.incrementAmountDonated(funds);
				player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
				MemberScrolls.checkForRankUpdate(player);
				credits = 27875;
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 27,875 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 10935:
				funds = 50;
				player.getInventory().delete(10935, 1);
				player.incrementAmountDonated(funds);
				player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
				MemberScrolls.checkForRankUpdate(player);
				credits = 55750;
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 55,750 credits. Your total is now at "+player.getCredits()+".");
				PlayerPanel.refreshPanel(player);
			break;
			case 7629:
				funds = 125;
				player.getInventory().delete(7629, 1);
				player.incrementAmountDonated(funds);
				player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
				MemberScrolls.checkForRankUpdate(player);
				credits = 111500;
				player.addCredits(credits);
				player.getPacketSender().sendMessage("Your account has gained 137,500 credits. Your total is now at "+player.getCredits()+".");
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
