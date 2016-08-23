package com.chaos.model.input.impl;

import com.chaos.model.Item;
import com.chaos.model.container.impl.PlayerOwnedShopContainer;
import com.chaos.model.input.EnterAmount;
import com.chaos.world.entity.impl.player.Player;

public class EnterAmountToBuyFromPos extends EnterAmount {

	public EnterAmountToBuyFromPos(int id, int slot) {
		super(id, slot);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (player.isPlayerOwnedShopping() && getItem() > 0 && getSlot() >= 0) {
			PlayerOwnedShopContainer shop = player.getPlayerOwnedShop();
			if (shop != null) {
				if (getSlot() >= shop.getItems().length || shop.getItems()[getSlot()].getId() != getItem())
					return;
				player.getPlayerOwnedShop().setPlayer(player).forSlot(getSlot()).copy().setAmount(amount).copy();
				shop.switchItem(player.getInventory(), new Item(getItem(), amount), getSlot(), false, true);
			} else
				player.getPacketSender().sendInterfaceRemoval();
		} else
			player.getPacketSender().sendInterfaceRemoval();

	}

}
