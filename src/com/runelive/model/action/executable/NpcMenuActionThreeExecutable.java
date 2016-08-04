package com.runelive.model.action.executable;

import com.runelive.executable.Executable;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public final class NpcMenuActionThreeExecutable implements Executable {
	private final Player player;
	private final NPC npc;

	public NpcMenuActionThreeExecutable(Player player, NPC npc) {
		this.player = player;
		this.npc = npc;
	}

	@Override
	public int execute() {
		player.getActions().thirdClickNpc(npc);
		return STOP;
	}
}
