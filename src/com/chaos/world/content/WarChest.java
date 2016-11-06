package com.chaos.world.content;

import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.model.Animation;
import com.chaos.model.GameObject;
import com.chaos.model.Item;
import com.chaos.model.player.GameMode;
import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

public class WarChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if (!p.getClickDelay().elapsed(3000))
			return;
		if (!p.getInventory().contains(18236)) {
			p.getPacketSender().sendMessage("This chest can only be opened with a War key.");
			return;
		}
		if(p.getInventory().getFreeSlots() < 1) {
			p.getPacketSender().sendMessage("You need atleast 1 free inventory slots to open this chest.");
			return;
		}
		p.performAnimation(new Animation(827));
		p.getInventory().delete(18236, 1);
		p.getPacketSender().sendMessage("You open the chest..");
		p.setUsingChest(true);
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
					p.setUsingChest(false);
					stop();
					break;
				}
				tick++;
			}
		});
		p.getClickDelay().reset();
	}

	private static final Item[][] itemRewards = {
			//Promethium
			{new Item(15771, 1)},
			{new Item(16291, 1)},
			{new Item(16313, 1)},
			{new Item(16357, 1)},
			{new Item(16379, 1)},
			{new Item(16401, 1)},
			{new Item(16423, 1)},
			{new Item(16665, 1)},
			{new Item(16687, 1)},
			{new Item(16709, 1)},
			{new Item(16731, 1)},
			{new Item(16829, 1)},
			{new Item(16907, 1)},
			{new Item(16953, 1)},
			{new Item(17037, 1)},
			{new Item(17135, 1)},
			{new Item(17257, 1)},
			{new Item(17359, 1)},
	};

}
