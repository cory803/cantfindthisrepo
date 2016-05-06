package com.runelive.model.input.impl;

import com.runelive.model.Item;
import com.runelive.model.input.Input;
import com.runelive.world.content.dialogue.DialogueManager;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.content.MemberScrolls;

public class EnterForumAccountTokens extends Input {

  @Override
  public void handleSyntax(Player player, String syntax) {
	if(player.currentScroll == -1) {
		return;
	}
	MemberScrolls.handleScroll(player, syntax);
  }
}
