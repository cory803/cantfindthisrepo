package com.runelive.model.input.impl;

import com.runelive.model.container.impl.Bank.BankSearchAttributes;
import com.runelive.model.input.Input;
import com.runelive.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttributes().isSearchingBank();
		if (searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
