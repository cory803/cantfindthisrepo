package com.ikov.engine.task.impl;

import com.ikov.engine.task.Task;
import com.ikov.model.Locations;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.minigames.impl.PestControl;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;

	@Override
	protected void execute() {
		World.updateServerTime();
		
		if(tick >= 6 && (Locations.PLAYERS_IN_WILD >= 3 || Locations.PLAYERS_IN_DUEL_ARENA >= 3 || PestControl.TOTAL_PLAYERS >= 3)) {
			if(Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.TOTAL_PLAYERS || Misc.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessage("<img=10> @blu@There are currently "+Locations.PLAYERS_IN_WILD+" players roaming the Wilderness!");
			}
			tick = 0;
		}

		tick++;
	}
}