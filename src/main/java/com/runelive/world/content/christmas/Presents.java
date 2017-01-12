package com.runelive.world.content.christmas;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.GameObject;
import com.runelive.model.Graphic;
import com.runelive.model.Item;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Handles presents
 * @Author Jonny
 */
public class Presents {

	/**
	 * Pickup your present!
	 * @param player
	 */
	public static void pickUpPresent(Player player, GameObject gameObject) {
		if(player == null) {
			return;
		}
		if (!player.getClickDelay().elapsed(3000))
			return;
		if(player.presentsPickedUp >= 2) {
			player.getPacketSender().sendMessage("You have already picked up 2 presents!");
			return;
		}
		if(!player.getInventory().hasRoomFor(1, 1)) {
			player.getPacketSender().sendMessage("You need atleast 1 inventory space to pickup a present.");
			return;
		}
		if(Misc.getHoursPlayedNumeric(player.getTotalPlayTime()) < 5 || player.getSkillManager().getTotalLevel() < 500) {
			player.getPacketSender().sendMessage("You must have played for atleast 5 hours, and have 500+ total level to get a present.");
			return;
		}
		player.getClickDelay().reset();
		player.presentsPickedUp += 1;
		TaskManager.submit(new Task(1, player, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
					case 0:
						player.setPositionToFace(gameObject.getPosition());
						player.performAnimation(new Animation(7276));
						break;
					case 2:
						int itemId = 6542;
						if(Misc.inclusiveRandom(1, 2) == 1) {
							itemId = 15420;
						}
						player.getInventory().add(new Item(itemId, 1));
						for(NPC npc: World.getNpcs()) {
							if(npc == null) {
								continue;
							}
							if(npc.getId() == 8540) {
								npc.forceChat("HO HO HO, Merry Christmas!");
							}
						}
						stop();
						break;
				}
				tick++;
			}
		});

		player.getPacketSender().sendMessage("You pickup a present from under the tree.");
	}

	/**
	 * Open your present and get rewards!
	 * @param player
	 * @param itemId
	 */
	public static void openPresent(Player player, int itemId) {
		if(player == null) {
			return;
		}
		if(!player.getInventory().hasRoomFor(1, 5)) {
			player.getPacketSender().sendMessage("You need atleast 5 inventory spaces to open a present.");
			return;
		}
		if(Misc.getHoursPlayedNumeric(player.getTotalPlayTime()) < 5 || player.getSkillManager().getTotalLevel() < 500) {
			player.getPacketSender().sendMessage("You must have played for atleast 5 hours, and have 500+ total level to open a present.");
			return;
		}
		if(player.getInventory().contains(itemId)) {
			player.getInventory().delete(itemId, 1);
			if(Misc.inclusiveRandom(1, 50) == 10) {
				Item[] loot = rareRewards[Misc.getRandom(rareRewards.length - 1)];
				for (Item item : loot) {
					player.getInventory().add(item);
					World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
							+ item.getDefinition().getName() + " from the a Present!");
				}
			} else {
				Item[] loot = commonRewards[Misc.getRandom(commonRewards.length - 1)];
				for (Item item : loot) {
					player.getInventory().add(item);
				}
			}
			player.performGraphic(new Graphic(1664));
		}
		player.getPacketSender().sendMessage("<col=ff0000><shad=f><icon=3>Merry christmas!");
	}

	private static final Item[][] commonRewards = {

			{new Item(14595, 1), new Item(14603, 1), new Item(14602, 1), new Item(14605, 1)},
			{new Item(15422, 1), new Item(15423, 1), new Item(15425, 1)},
			{new Item(20077, 1)},

	};

	private static final Item[][] rareRewards = {
			{new Item(1050, 1)},
			{new Item(21035, 1)},
			{new Item(21036, 1)},
			{new Item(21037, 1)},
			{new Item(21038, 1)},
			{new Item(21039, 1)},
			{new Item(21040, 1)},
			{new Item(21041, 1)},
			{new Item(21048, 1)},
			{new Item(4084, 1)},
			{new Item(5607, 1)},
	};

}
