package com.chaos.world.content;

import com.chaos.model.DonatorRights;
import com.chaos.world.entity.impl.player.Player;

public class Bonds {

	public static final int BOND = 13190;

	public static void checkForRankUpdate(Player player) {
		int donated = player.getAmountDonated();
		if(donated >= 1000) {
			if(player.getDonatorRights() != DonatorRights.PLATINUM) {
				player.setDonatorRights(DonatorRights.PLATINUM);
				player.getPacketSender().sendMessage(DonatorRights.PLATINUM.getColor() + DonatorRights.PLATINUM.getTitle() + "Congratulations! You are now a Platinum Donator.");
			}
		} else if(donated >= 500) {
			if(player.getDonatorRights() != DonatorRights.UBER) {
				player.setDonatorRights(DonatorRights.UBER);
				player.getPacketSender().sendMessage(DonatorRights.UBER.getColor() + DonatorRights.UBER.getTitle() + "Congratulations! You are now a Uber Donator.");
			}
		} else if(donated >= 150) {
			if(player.getDonatorRights() != DonatorRights.LEGENDARY) {
				player.setDonatorRights(DonatorRights.LEGENDARY);
				player.getPacketSender().sendMessage(DonatorRights.LEGENDARY.getColor() + DonatorRights.LEGENDARY.getTitle() + "Congratulations! You are now a Legendary Donator.");
			}
		} else if(donated >= 50) {
			if(player.getDonatorRights() != DonatorRights.EXTREME) {
				player.setDonatorRights(DonatorRights.EXTREME);
				player.getPacketSender().sendMessage(DonatorRights.EXTREME.getColor() + DonatorRights.EXTREME.getTitle() + "Congratulations! You are now a Extreme Donator.");
			}
		} else if(donated >= 20) {
			if(player.getDonatorRights() != DonatorRights.PREMIUM) {
				player.setDonatorRights(DonatorRights.PREMIUM);
				player.getPacketSender().sendMessage(DonatorRights.PREMIUM.getColor() + DonatorRights.PREMIUM.getTitle() + "Congratulations! You are now a Premium Donator.");
			}
		}
	}

	public static void handleBond(Player player) {
		if (player.getInventory().contains(BOND)) {
			if (player.getInventory().hasRoomFor(20435, 5)) {
				player.getInventory().delete(BOND, 1);
				player.getInventory().add(20435, 5);
				player.getPacketSender().sendMessage("You claimed one bond and received 5 donator tokens.");
			} else {
				player.getPacketSender().sendMessage("You need more room in your inventory before opening this bond.");
			}
		} else {
			player.getPacketSender().sendMessage("You do not have any bonds to open.");
		}
	}
}
