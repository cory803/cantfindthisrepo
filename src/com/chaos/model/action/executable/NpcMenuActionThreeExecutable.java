package com.chaos.model.action.executable;

import com.chaos.executable.Executable;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

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
