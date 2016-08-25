package com.chaos.world.content;

import com.chaos.model.Animation;
import com.chaos.model.GameObject;
import com.chaos.model.Graphic;
import com.chaos.model.Locations.Location;
import com.chaos.model.movement.WalkingQueue;
import com.chaos.util.Misc;
import com.chaos.world.content.clan.ClanChatManager;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class Gambling {

	public static void rollDice(Player player) {
		int amount = Misc.getRandom(100);
		if (player.getLocation() != Location.GAMBLE) {
			player.getPacketSender().sendMessage("").sendMessage("This dice can only be used in the gambling area!")
					.sendMessage("To get there, talk to the gambler.");
			return;
		}
		if (player.getCurrentClanChat() == null) {

			return;
		}
		if (player.getClanChatName() == null) {
			player.getPacketSender().sendMessage("You need to be in a clanchat channel to roll a dice.");
			return;
		} else if (player.getClanChatName().equalsIgnoreCase("chaos")) {
			player.getPacketSender().sendMessage("You can't roll a dice in this clanchat channel!");
			return;
		}
		if (!player.getClickDelay().elapsed(5000)) {
			player.getPacketSender().sendMessage("You must wait 5 seconds between each dice cast.");
			return;
		}
		if (player.dice_other) {
			amount = player.dice_other_amount;
		}
		player.getWalkingQueue().clear();
		player.performAnimation(new Animation(11900));
		player.performGraphic(new Graphic(2075));
		ClanChatManager.sendMessage(player.getCurrentClanChat(), "@bla@[ClanChat] @whi@" + player.getUsername()
				+ " just rolled @bla@" + amount + "@whi@ on the percentile dice.");
		player.forceChat("[HOST] " + player.getUsername() + " just ROLLED " + amount + " on the percentile dice.");
		player.getClickDelay().reset();
		player.dice_other = false;
		PlayerLogs.dicing(player, amount);
	}

	public static void plantSeed(Player player) {
		if (player.getLocation() == Location.VARROCK || player.getLocation() == Location.EDGEVILLE || player.getLocation() == Location.BARROWS) {
			player.getPacketSender().sendMessage("").sendMessage("You can't plant here! Try going to ::gamble.");
			return;
		}
		if (player.getLocation() == Location.DUEL_ARENA) {
			player.forceChat("Hey everyone! I just tried to do something silly!");
			return;
		}
		if (!player.getClickDelay().elapsed(1400))
			return;
		for (NPC npc : player.getLocalNpcs()) {
			if (npc != null && npc.getPosition().equals(player.getPosition())) {
				player.getPacketSender().sendMessage("You cannot plant a seed right here.");
				return;
			}
		}
		if (CustomObjects.objectExists(player.getPosition().copy())) {
			player.getPacketSender().sendMessage("You cannot plant a seed right here.");
			return;
		}
		FlowersData flowers = FlowersData.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getWalkingQueue().clear();
		player.getInventory().delete(299, 1);
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getWalkingQueue().clear();
		player.setInteractingObject(flower);
		WalkingQueue.stepAway(player);
		CustomObjects.globalObjectRemovalTask(flower, 90);
		player.setPositionToFace(flower.getPosition());
		player.getClickDelay().reset();
		PlayerLogs.plant(player, flowers.toString());
	}

	public enum FlowersData {

		PASTEL_FLOWERS(2980, 2460),
		RED_FLOWERS(2981, 2462),
		BLUE_FLOWERS(2982, 2464),
		YELLOW_FLOWERS(2983, 2466),
		PURPLE_FLOWERS(2984, 2468),
		ORANGE_FLOWERS(2985, 2470),
		RAINBOW_FLOWERS(2986, 2472),
		WHITE_FLOWERS(2987, 2474),
		BLACK_FLOWERS(2988, 2476);

		FlowersData(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		public int objectId;
		public int itemId;

		public static FlowersData forObject(int object) {
			for (FlowersData data : FlowersData.values()) {
				if (data.objectId == object)
					return data;
			}
			return null;
		}

		public static FlowersData generate() {
			double RANDOM = (java.lang.Math.random() * 100);
			if (RANDOM >= 1) {
				return values()[Misc.getRandom(6)];
			} else {
				return Misc.getRandom(3) == 1 ? WHITE_FLOWERS : BLACK_FLOWERS;
			}
		}
	}
}
