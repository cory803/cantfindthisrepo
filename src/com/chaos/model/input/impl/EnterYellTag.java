package com.chaos.model.input.impl;

import com.chaos.model.Item;
import com.chaos.model.input.Input;
import com.chaos.world.entity.impl.player.Player;

public class EnterYellTag extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		String yell_tag = syntax;
		boolean dont_send = false;
		int value = 500000000;
		switch(player.getDonatorRights()) {
			case PREMIUM:
				value = 10000000;
				break;
			case EXTREME:
				value = 8000000;
				break;
			case LEGENDARY:
				value = 6000000;
				break;
			case UBER:
				value = 4000000;
				break;
			case PLATINUM:
				value = 2000000;
				break;
		}
		String[] not_allowed = { "owner", "moderator", "admin", "fuck", "bitch", "shit", "nigger", "cancer", ".com",
				".org", ".net", "asshole", "faggot", "porn", "penis", "vagina", "ballsack", "nutsack", "<", ">", "img=",
				"col=", "shad=", "hair pube", "@" };
		for (int i = 0; i < not_allowed.length; i++) {
			if (yell_tag.toLowerCase().contains(not_allowed[i])) {

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

		player.setYellTag(yell_tag);
	}
}
