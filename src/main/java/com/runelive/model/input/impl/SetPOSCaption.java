package com.runelive.model.input.impl;

import com.runelive.model.input.Input;
import com.runelive.world.content.pos.PlayerOwnedShops;
import com.runelive.world.entity.impl.player.Player;

public class SetPOSCaption extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() >= 40) {
			player.getPacketSender().sendMessage("Your store caption can only have 40 characters!");
			return;
		}
		if(syntax.length() < 3) {
			player.getPacketSender().sendMessage("Your store caption must have atleast 3 characters!");
			return;
		}
		int index = PlayerOwnedShops.getIndex(player.getUsername());
		PlayerOwnedShops.SHOPS_ARRAYLIST.get(index).setCaption(syntax);
		player.getPacketSender().sendMessage("<col=CA024B>You set your caption to '"+syntax+"'.");
		PlayerOwnedShops.saveShop(PlayerOwnedShops.SHOPS_ARRAYLIST.get(index));
	}
}
