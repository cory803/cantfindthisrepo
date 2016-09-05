package com.chaos.world.content;

import com.chaos.util.Misc;
import com.chaos.world.entity.impl.player.Player;

public class Achievements {

	public enum AchievementData {

		TELEPORT_HOME(Difficulty.EASY, "Teleport home", 37005, null),
		BURY_BIG_BONE(Difficulty.EASY, "Bury a big bone", 37006, null),
		CATCH_LOBSTER(Difficulty.EASY, "Catch a lobster while fishing", 37007, null),
		COOK_LOBSTER(Difficulty.EASY, "Cook a raw lobster succesfully", 37008, null),
		EAT_LOBSTER(Difficulty.EASY, "Eat a cooked lobster", 37009, null),
		MINE_IRON(Difficulty.EASY, "Mine iron ore", 37010, null),
		SMELT_IRON(Difficulty.EASY, "Smelt an iron bar", 37011, null),
		SMITH_IRON_DAGGER(Difficulty.EASY, "Smith an iron dagger", 37012, null),
		CHOP_WILLOW(Difficulty.EASY, "Chop a willow tree", 37013, null),
		BURN_WILLOW(Difficulty.EASY, "Burn a willow log", 37014, null),
		MAKE_POTION(Difficulty.EASY, "Make any potion", 37015, null),
		GNOME_COURSE(Difficulty.EASY, "Complete the gnome agility course", 37016, null),
		FLETCH_ARROW_SHAFT(Difficulty.EASY, "Fletch an arrow shaft", 37017, null),
		RUNECRAFT_RUNES(Difficulty.EASY, "Runecraft any elemental rune", 37018, null),
		CREATE_DUNG_PARTY(Difficulty.EASY, "Create a dungeoneering party", 37019, null), //TODO
		KILL_ROCKCRAB(Difficulty.EASY, "Kill a rock crab", 37020, null),
		KILL_SKELETON(Difficulty.EASY, "Kill a skeleton", 37021, null),
		KILL_YAK(Difficulty.EASY, "Kill a yak", 37022, null),
		INFUSE_WOLF_POUCH(Difficulty.EASY, "Infuse a wolf pouch", 37023, null),
		CREATE_CLAN_CHAT(Difficulty.EASY, "Create a clan chat", 37024, null), //TODO
		ADD_FRIEND(Difficulty.EASY, "Add a friend", 37025, null), //TODO
		GET_SLAYER_TASK(Difficulty.EASY, "Get a slayer task", 37026, null),
		SWITCH_SPELLBOOK(Difficulty.EASY, "Switch to another spellbook", 37027, null), //TODO
		SWITCH_PRAYBOOK(Difficulty.EASY, "Switch to the curse prayers", 37028, null), //TODO
		KILL_A_MONSTER_USING_MELEE(Difficulty.EASY, "Kill a Monster using Melee", 37029, null),
		KILL_A_MONSTER_USING_RANGED(Difficulty.EASY, "Kill a Monster using Ranged", 37030, null),
		KILL_A_MONSTER_USING_MAGIC(Difficulty.EASY, "Kill a Monster using Magic", 37031, null),
		DEAL_EASY_DAMAGE_USING_MELEE(Difficulty.EASY, "Deal 1000 Melee Damage", 37032, new int[] {0, 1000 }),
		DEAL_EASY_DAMAGE_USING_RANGED(Difficulty.EASY, "Deal 1000 Ranged Damage", 37033, new int[] {1, 1000 }),
		DEAL_EASY_DAMAGE_USING_MAGIC(Difficulty.EASY, "Deal 1000 Magic Damage", 37034, new int[] {2, 1000 }),
		PERFORM_A_SPECIAL_ATTACK(Difficulty.EASY, "Perform a Special Attack", 37035, null),
		BLOW_KISS(Difficulty.EASY, "Blow a Kiss", 37036, null),

        BURY_50_DRAGON_BONES(Difficulty.EASY, "Bury 50 dragon bones", 37039, new int[] { 3, 50 }),
        MIX_AN_OVERLOAD_POTION(Difficulty.EASY, "Make an Overload Potion", 37040, null),
		CHOP_250_MAPLE_LOGS(Difficulty.MEDIUM, "Cut 250 Maple Logs", 37041, new int[] { 4, 250 }),
		BURN_200_MAPLE_LOGS(Difficulty.MEDIUM, "Burn 200 Maple Logs", 37042, new int[] { 5, 200 }),
		FISH_100_SHARKS(Difficulty.MEDIUM, "Fish 100 sharks", 37043, new int[] { 6, 100 }),
		COOK_100_SHARKS(Difficulty.MEDIUM, "Cook 100 sharks", 37044, new int[] { 7, 100 }),
		MINE_200_COAL(Difficulty.MEDIUM, "Mine 200 Coal ores", 37045, new int[] { 8, 200 }),
		SMELT_50_MITH_BARS(Difficulty.MEDIUM, "Smelt 50 Mithril Bars", 37046, new int[] { 9, 50 }),
		HARVEST_10_TORSTOLS(Difficulty.MEDIUM, "Harvest 10 Torstols", 37047, new int[] { 10, 10 }),
		INFUSE_25_TITAN_POUCHES(Difficulty.MEDIUM, "Infuse 25 Steel Titans", 37048, new int[] { 11, 25 }),
		CATCH_5_KINGLY_IMPLINGS(Difficulty.MEDIUM, "Catch 5 Kingly Implings", 37049, new int[] {12, 5 }),
		COMPLETE_A_HARD_SLAYER_TASK(Difficulty.MEDIUM, "Complete a Hard Slayer Task", 37050, null),
		CRAFT_20_BLACK_DHIDE_BODIES(Difficulty.MEDIUM, "Craft 20 Black D'hide Bodies", 37051, new int[] {13, 20 }),
		FLETCH_450_RUNE_ARROWS(Difficulty.MEDIUM, "Fletch 450 Rune Arrows", 37052, new int[] {14, 450 }),
		STEAL_140_SCIMITARS(Difficulty.MEDIUM, "Pick-Pocket 150 times", 37053, new int[] {15, 150 }),
		BARB_AGILITY(Difficulty.MEDIUM, "Complete Barb Agility Course", 37054, null),
		CLIMB_50_AGILITY_OBSTACLES(Difficulty.MEDIUM, "Climb 50 Agility Obstacles", 37055, new int[] {16, 50 }),
		RUNECRAFT_500_NATS(Difficulty.MEDIUM, "Runecraft 500 Nature Runes", 37056, new int[] {17, 500 }),
		BURY_25_FROST_DRAGON_BONES(Difficulty.MEDIUM, "Bury 25 Frost Dragon Bones", 37057, new int[] {18, 25 }),
		FIRE_500_CANNON_BALLS(Difficulty.MEDIUM, "Fire 500 Cannon Balls", 37058, new int[] {19, 500 }),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(Difficulty.MEDIUM, "Deal 100K Melee Damage", 37059, new int[] {20, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(Difficulty.MEDIUM, "Deal 100K Ranged Damage", 37060, new int[] {21, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(Difficulty.MEDIUM, "Deal 100K Magic Damage", 37061, new int[] {22, 100000 }),
		DEFEAT_THE_KING_BLACK_DRAGON(Difficulty.MEDIUM, "Defeat the King Black Dragon", 37062, null),
		DEFEAT_THE_CHAOS_ELEMENTAL(Difficulty.MEDIUM, "Defeat the Chaos Elemental", 37063, null),
		DEFEAT_A_TORMENTED_DEMON(Difficulty.MEDIUM, "Defeat a Tormented Demon", 37064, null),
		DEFEAT_THE_CULINAROMANCER(Difficulty.MEDIUM, "Defeat the Culinaromancer", 37065, null),
		DEFEAT_NOMAD(Difficulty.MEDIUM, "Defeat Nomad", 37066, null),
		DEFEAT_10_PLAYERS(Difficulty.MEDIUM, "Defeat 10 Players", 37067, new int[] {23, 10 }),
		LOW_ALCH_ITEMS(Difficulty.MEDIUM, "Low Alch 300 Items", 37068, new int[] { 24, 300 }),

		BURY_500_FROST_DRAGON_BONES(Difficulty.HARD, "Bury 500 Frost Dragon Bones", 37071, new int[] {25, 500 }),
		STEAL_5000_SCIMITARS(Difficulty.HARD, "Steal 2,000 Times", 37072, new int[] {26, 2000 }),
		FIRE_5000_CANNON_BALLS(Difficulty.HARD, "Fire 3000 Cannon Balls", 37073, new int[] {27, 3000 }),
		MIX_100_OVERLOAD_POTIONS(Difficulty.HARD, "Mix 100 Overload Potions", 37074, new int[] {28, 100 }),
		COMPLETE_AN_ELITE_SLAYER_TASK(Difficulty.HARD, "Complete an Elite Slayer Task", 37075, null),
		DEAL_HARD_DAMAGE_USING_MELEE(Difficulty.HARD, "Deal 5M Melee Damage", 37076, new int[] {29, 5000000 }),
		DEAL_HARD_DAMAGE_USING_RANGED(Difficulty.HARD, "Deal 5M Ranged Damage", 37077, new int[] {30, 5000000 }),
		DEAL_HARD_DAMAGE_USING_MAGIC(Difficulty.HARD, "Deal 5M Magic Damage", 37078, new int[] {31, 5000000 }),
		DEFEAT_JAD(Difficulty.HARD, "Defeat Jad", 37079, null),
		DEFEAT_BANDOS_AVATAR(Difficulty.HARD, "Defeat Bandos Avatar", 37080, null),
		DEFEAT_GENERAL_GRAARDOR(Difficulty.HARD, "Defeat General Graardor", 37081, null),
		DEFEAT_KREE_ARRA(Difficulty.HARD, "Defeat Kree'Arra", 37082, null),
		DEFEAT_COMMANDER_ZILYANA(Difficulty.HARD, "Defeat Commander Zilyana", 37083, null),
		DEFEAT_KRIL_TSUTSAROTH(Difficulty.HARD, "Defeat K'ril Tsutsaroth", 37084, null),
		DEFEAT_THE_CORPOREAL_BEAST(Difficulty.HARD, "Defeat The Corporeal Beast", 37085, null),
		DEFEAT_NEX(Difficulty.HARD, "Defeat Nex", 37086, null),
		DEFEAT_30_PLAYERS(Difficulty.HARD, "Defeat 30 Players", 37087, new int[] {32, 30 }),

		RUNECRAFT_8000_BLOOD_RUNES(Difficulty.ELITE, "Runecraft 6000 Blood Runes", 37090, new int[] {33, 6000 }),
		CUT_5000_MAGIC_LOGS(Difficulty.ELITE, "Cut 2500 Magic Logs", 37091, new int[] { 34, 2500 }),
		BURN_2500_MAGIC_LOGS(Difficulty.ELITE, "Burn 2500 Magic Logs", 37092, new int[] { 35, 2500 }),
		FISH_2000_ROCKTAILS(Difficulty.ELITE, "Fish 1500 Rocktails", 37093, new int[] { 36, 1500 }),
		COOK_1000_ROCKTAILS(Difficulty.ELITE, "Cook 1000 Rocktails", 37094, new int[] { 37, 1000 }),
		MINE_2000_RUNITE_ORES(Difficulty.ELITE, "Mine 1000 Runite Ores", 37095, new int[] { 38, 1000 }),
		SMELT_1000_RUNE_BARS(Difficulty.ELITE, "Smelt 1000 Rune Bars", 37096, new int[] { 39, 1000 }),
		HARVEST_1000_TORSTOLS(Difficulty.ELITE, "Harvest 1000 Torstols", 37097, new int[] { 40, 1000 }),
		INFUSE_500_STEEL_TITAN_POUCHES(Difficulty.ELITE, "Infuse 500 Steel Titans", 37098, new int[] { 41, 500 }),
		CRAFT_1000_DIAMOND_GEMS(Difficulty.ELITE, "Craft 750 Diamond Gems", 37099, new int[] { 42, 750 }),
		CATCH_100_KINGLY_IMPLINGS(Difficulty.ELITE, "Catch 100 Kingly Imps", 37100, new int[] {43, 100 }),
		FLETCH_5000_RUNE_ARROWS(Difficulty.ELITE, "Fletch 5000 Rune Arrows", 37101, new int[] {44, 5000 }),
		CUT_AN_ONYX_STONE(Difficulty.ELITE, "Cut an Onyx Stone", 37102, null),
		REACH_MAX_EXP_IN_A_SKILL(Difficulty.ELITE, "Reach Max Exp in a Skill", 37103, null),
		HIT_700_WITH_SPECIAL_ATTACK(Difficulty.ELITE, "Hit 700 with Special Attack", 37104, new int[] { 45, 1 }),
		DEFEAT_10000_MONSTERS(Difficulty.ELITE, "Defeat 10,000 Monsters", 37105, new int[] { 45, 10000 }),
		DEFEAT_500_BOSSES(Difficulty.ELITE, "Defeat 500 Boss Monsters", 37106, new int[] { 47, 500 }),
		;
		public static int SIZE = AchievementData.values().length;

		AchievementData(Difficulty difficulty, String interfaceLine, int interfaceFrame, int[] progressData) {
			this.difficulty = difficulty;
			this.interfaceLine = interfaceLine;
			this.interfaceFrame = interfaceFrame;
			this.progressData = progressData;
		}

		private Difficulty difficulty;
		private String interfaceLine;
		private int interfaceFrame;
		private int[] progressData;

		public Difficulty getDifficulty() {
			return difficulty;
		}
	}

	public enum Difficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE;
	}

	public static boolean handleButton(Player player, int button) {
		if (!(button >= -28531 && button <= -28425)) {
			return false;
		}
		int index = -1;
		if (button >= -28531 && button <= -28502) {
			index = 28531 + button;
		} else if (button >= -28499 && button <= -28469) {
			index = 30 + 28499 + button;
		} else if (button >= -28466 && button <= -28435) {
			index = 61 + 28466 + button;
		} else if (button >= -28432 && button <= -28425) {
			index = 93 + 28432 + button;
		}
		if (index >= 0 && index < AchievementData.values().length) {
			AchievementData achievement = AchievementData.values()[index];
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				player.getPacketSender().sendMessage(
						"<img=4> <col=339900>You have completed the achievement: " + achievement.interfaceLine + ".");
			} else if (achievement.progressData == null) {
				player.getPacketSender().sendMessage(
						"<img=4> <col=660000>You have not started the achievement: " + achievement.interfaceLine + ".");
			} else {
				int progress = player.getAchievementAttributes().getProgress()[achievement.progressData[0]];
				int requiredProgress = achievement.progressData[1];
				if (progress == 0) {
					player.getPacketSender().sendMessage("<img=4> <col=660000>You have not started the achievement: "
							+ achievement.interfaceLine + ".");
				} else if (progress != requiredProgress) {
					player.getPacketSender()
							.sendMessage("<img=4> <col=FFFF00>Your progress for this achievement is currently at: "
									+ Misc.insertCommasToNumber("" + progress) + "/"
									+ Misc.insertCommasToNumber("" + requiredProgress) + ".");
				}
			}
		}
		return true;
	}

	public static void updateInterface(Player player) {
		for (AchievementData achievement : AchievementData.values()) {
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			player.getPacketSender().sendString(achievement.interfaceFrame,
					(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
		}
		player.getPacketSender().sendString(37001, "Achievements: " + player.getPointsHandler().getAchievementPoints()
				+ "/" + AchievementData.values().length);
	}

	public static void setPoints(Player player) {
		int points = 0;
		for (AchievementData achievement : AchievementData.values()) {
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				points++;
			}
		}
		player.getPointsHandler().setAchievementPoints(points, false);
	}

	public static void doProgress(Player player, AchievementData achievement) {
		doProgress(player, achievement, 1);
	}

	public static void doProgress(Player player, AchievementData achievement, int amt) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		if (achievement.progressData != null) {
			int progressIndex = achievement.progressData[0];
			int amountNeeded = achievement.progressData[1];
			int previousDone = player.getAchievementAttributes().getProgress()[progressIndex];
			if ((previousDone + amt) < amountNeeded) {
				player.getAchievementAttributes().getProgress()[progressIndex] = previousDone + amt;
				if (previousDone == 0)
					player.getPacketSender().sendString(achievement.interfaceFrame,
							"@yel@" + achievement.interfaceLine);
			} else {
				finishAchievement(player, achievement);
			}
		}
	}

	public static void finishAchievement(Player player, AchievementData achievement) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender().sendString(achievement.interfaceFrame, ("@gre@") + achievement.interfaceLine)
				.sendMessage("<img=4> <col=339900>You have completed the achievement "
						+ Misc.formatText(achievement.toString().toLowerCase() + "."))
				.sendString(37001, "Achievements: " + player.getPointsHandler().getAchievementPoints() + "/"
						+ AchievementData.values().length);
		player.getPacketSender().sendString(1, "[ACHIEVEMENT]-"+ Misc.formatText(achievement.toString().toLowerCase() +"-"+achievement.getDifficulty().ordinal()));
		player.getPointsHandler().setAchievementPoints(1, true);
	}

	public static class AchievementAttributes {

		public AchievementAttributes() {
		}

		/** ACHIEVEMENTS **/
		private boolean[] completed = new boolean[AchievementData.values().length];
		private int[] progress = new int[53];

		public boolean[] getCompletion() {
			return completed;
		}

		public void setCompletion(int index, boolean value) {
			this.completed[index] = value;
		}

		public void setCompletion(boolean[] completed) {
			this.completed = completed;
		}

		public int[] getProgress() {
			return progress;
		}

		public void setProgress(int index, int value) {
			this.progress[index] = value;
		}

		public void setProgress(int[] progress) {
			this.progress = progress;
		}

		/** MISC **/
		private int coinsGambled;
		private double totalLoyaltyPointsEarned;
		private boolean[] godsKilled = new boolean[5];

		public int getCoinsGambled() {
			return coinsGambled;
		}

		public void setCoinsGambled(int coinsGambled) {
			this.coinsGambled = coinsGambled;
		}

		public double getTotalLoyaltyPointsEarned() {
			return totalLoyaltyPointsEarned;
		}

		public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
			this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
		}

		public boolean[] getGodsKilled() {
			return godsKilled;
		}

		public void setGodKilled(int index, boolean godKilled) {
			this.godsKilled[index] = godKilled;
		}

		public void setGodsKilled(boolean[] b) {
			this.godsKilled = b;
		}
	}
}
