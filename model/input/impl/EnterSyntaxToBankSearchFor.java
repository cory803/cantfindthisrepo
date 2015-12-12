package com.ikov.model.input.impl;

import com.ikov.model.container.impl.Bank.BankSearchAttributes;
import com.ikov.model.input.Input;
import com.ikov.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		if(searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
