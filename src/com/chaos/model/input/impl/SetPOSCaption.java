package com.chaos.model.input.impl;

import com.chaos.model.Item;
import com.chaos.model.input.Input;
import com.chaos.world.content.pos.PlayerOwnedShops;
import com.chaos.world.content.pos.PosOffers;
import com.chaos.world.entity.impl.player.Player;
import org.scripts.kotlin.content.dialog.npcs.Ellis;

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
		PlayerOwnedShops.SHOPS_ARRAYLIST.get(PlayerOwnedShops.getIndex(player.getUsername())).setCaption(syntax);
		player.getPacketSender().sendMessage("<col=CA024B>You have set your player owned stores caption to '"+syntax+"'.");
	}
}
