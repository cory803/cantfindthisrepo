package com.chaos.world.content;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.GameObject;
import com.chaos.model.Item;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

public class CrystalChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if (!p.getClickDelay().elapsed(3000))
			return;
		if (!p.getInventory().contains(989)) {
			p.getPacketSender().sendMessage("This chest can only be opened with a Crystal key.");
			return;
		}
		p.performAnimation(new Animation(827));
		p.getInventory().delete(989, 1);
		p.getPacketSender().sendMessage("You open the chest..");
		TaskManager.submit(new Task(1, p, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 2:
					Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
					for (Item item : loot) {
						p.getInventory().add(item);
					}
					p.getPacketSender().sendMessage("..and find some items!");
					CustomObjects.objectRespawnTask(p, new GameObject(173, chest.getPosition().copy(), 10, 0), chest,
							3);
					stop();
					break;
				}
				tick++;
			}
		});
		p.getClickDelay().reset();
	}

	private static final Item[][] itemRewards = {
			{ new Item(1631, 1), new Item(995, 2000) },
			{ new Item(1631, 1) },
			{ new Item(1631, 1), new Item(995, 1000), new Item(372, 5) },
			{ new Item(1631, 1), new Item(556, 50), new Item(559, 50), new Item(557, 50), new Item(554, 50), new Item(558, 50),
					new Item(555, 50), new Item(564, 10), new Item(560, 10), new Item(561, 10), new Item(563, 10) },
			{ new Item(1631, 1), new Item(454, 100) },
			{ new Item(1631, 1), new Item(1604, 2), new Item(1602, 2) },
			{ new Item(1631, 1), new Item(2364, 3) },
			{ new Item(1631, 1), new Item(441, 150) },
			{ new Item(1631, 1), new Item(995, 2000) },
			{ new Item(1631, 1), new Item(995, 750), new Item(985, 1) },
			{ new Item(1631, 1), new Item(995, 750), new Item(987, 1) },
			{ new Item(1631, 1), new Item(1079, 1) }
	};

}
