package com.strattus.model.input.impl;

import com.strattus.model.container.impl.Bank.BankSearchAttributes;
import com.strattus.model.input.Input;
import com.strattus.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		if(searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
