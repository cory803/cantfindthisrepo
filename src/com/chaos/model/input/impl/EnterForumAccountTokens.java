package com.chaos.model.input.impl;

import com.chaos.model.input.Input;
import com.chaos.world.content.MemberScrolls;
import com.chaos.world.entity.impl.player.Player;

public class EnterForumAccountTokens extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (player.currentScroll == -1) {
			return;
		}
		MemberScrolls.handleScroll(player, syntax);
	}
}
