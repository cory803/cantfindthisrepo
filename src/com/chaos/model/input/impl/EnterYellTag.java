package com.chaos.model.input.impl;

import com.chaos.model.Item;
import com.chaos.model.input.Input;
import com.chaos.world.content.dialogue.DialogueManager;
import com.chaos.world.entity.impl.player.Player;

public class EnterYellTag extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		String yell_tag = syntax;
		boolean dont_send = false;
		int value = 500000000;
		if (player.getDonorRights() == 1) {
			value = 500000000;
		} else if (player.getDonorRights() == 2) {
			value = 400000000;
		} else if (player.getDonorRights() == 3) {
			value = 300000000;
		} else if (player.getDonorRights() == 4) {
			value = 200000000;
		} else if (player.getDonorRights() == 5) {
			value = 100000000;
		}
		String[] not_allowed = { "owner", "moderator", "admin", "fuck", "bitch", "shit", "nigger", "cancer", ".com",
				".org", ".net", "asshole", "faggot", "porn", "penis", "vagina", "ballsack", "nutsack", "<", ">", "img=",
				"col=", "shad=", "hair pube", "@" };
		for (int i = 0; i < not_allowed.length; i++) {
			if (yell_tag.toLowerCase().contains(not_allowed[i])) {
				DialogueManager.sendStatement(player, "You are not allowed to have that in your yell tag!");
				dont_send = true;
			}
		}
		if (dont_send) {
			return;
		}
		if (player.getInventory().getAmount(995) >= value) {
			player.getInventory().delete(new Item(995, value));
		} else if (player.getMoneyInPouch() >= value) {
			player.setMoneyInPouch((player.getMoneyInPouch() - value));
		}
		player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		DialogueManager.sendStatement(player, "Your yell tag has been set to " + yell_tag + "!");
		player.setYellTag(yell_tag);
	}
}
