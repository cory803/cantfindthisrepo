package com.chaos.model.input.impl;

import com.chaos.model.container.impl.Bank.BankSearchAttributes;
import com.chaos.model.input.Input;
import com.chaos.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttributes().isSearchingBank();
		if (searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
