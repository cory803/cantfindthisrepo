package com.runelive.world.content;

import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

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
		GNOME_COURSE(Difficulty.EASY, "Complete gnome agility course", 37016, null),
		FLETCH_ARROW_SHAFT(Difficulty.EASY, "Fletch an arrow shaft", 37017, null),
		RUNECRAFT_RUNES(Difficulty.EASY, "Runecraft any elemental rune", 37018, null),
		COMPLETE_DUNG_FLOOR(Difficulty.EASY, "Complete a dungeoneering floor", 37019, null), //TODO
		KILL_ROCKCRAB(Difficulty.EASY, "Kill a rock crab", 37020, null),
		KILL_SKELETON(Difficulty.EASY, "Kill a skeleton", 37021, null),
		KILL_YAK(Difficulty.EASY, "Kill a yak", 37022, null),
		INFUSE_WOLF_POUCH(Difficulty.EASY, "Infuse a wolf pouch", 37023, null),
		CREATE_CLAN_CHAT(Difficulty.EASY, "Create a clan chat", 37024, null), //TODO
		ADD_FRIEND(Difficulty.EASY, "Add a friend", 37025, null),
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
		MINE_400_COAL(Difficulty.MEDIUM, "Mine 400 Coal ores", 37045, new int[] { 8, 400 }),
		SMELT_50_MITH_BARS(Difficulty.MEDIUM, "Smelt 50 Mithril Bars", 37046, new int[] { 9, 50 }),
		COMPLETE_5_DUNG_FLOORS(Difficulty.MEDIUM, "Complete 5 Dung Floors", 37047, new int[] { 10, 5 }),
		INFUSE_25_TITAN_POUCHES(Difficulty.MEDIUM, "Infuse 25 Steel Titans", 37048, new int[] { 11, 25 }),
		CATCH_5_KINGLY_IMPLINGS(Difficulty.MEDIUM, "Catch 5 Kingly Implings", 37049, new int[] {12, 5 }),
		COMPLETE_A_HARD_SLAYER_TASK(Difficulty.MEDIUM, "Complete a Hard Slayer Task", 37050, null),
		CRAFT_20_BLACK_DHIDE_BODIES(Difficulty.MEDIUM, "Craft 20 Black D'hide Bodies", 37051, new int[] {13, 20 }),
		FLETCH_450_RUNE_BOLTS(Difficulty.MEDIUM, "Smith 450 Rune Bolts", 37052, new int[] {14, 450 }),
		PICK_POCKET_150_TIMES(Difficulty.MEDIUM, "Pick-Pocket 150 times", 37053, new int[] {15, 150 }),
		BARB_AGILITY(Difficulty.MEDIUM, "Complete Barb Agility Course", 37054, null),
		CLIMB_50_AGILITY_OBSTACLES(Difficulty.MEDIUM, "Climb 50 Agility Obstacles", 37055, new int[] {16, 50 }),
		RUNECRAFT_500_NATS(Difficulty.MEDIUM, "Runecraft 500 Nature Runes", 37056, new int[] {17, 500 }),
		BURY_25_FROST_DRAGON_BONES(Difficulty.MEDIUM, "Bury 25 Frost Dragon Bones", 37057, new int[] {18, 25 }),
		MINE_200_GOLD(Difficulty.MEDIUM, "Mine 200 Gold ores", 37058, new int[] {19, 200 }),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(Difficulty.MEDIUM, "Deal 100K Melee Damage", 37059, new int[] {20, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(Difficulty.MEDIUM, "Deal 100K Ranged Damage", 37060, new int[] {21, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(Difficulty.MEDIUM, "Deal 100K Magic Damage", 37061, new int[] {22, 100000 }),
		DEFEAT_THE_KING_BLACK_DRAGON(Difficulty.MEDIUM, "Defeat the King Black Dragon", 37062, null),
		DEFEAT_THE_CHAOS_ELEMENTAL(Difficulty.MEDIUM, "Defeat the Chaos Elemental", 37063, null),
		DEFEAT_A_TORMENTED_DEMON(Difficulty.MEDIUM, "Defeat a Tormented Demon", 37064, null),
		DEFEAT_THE_CULINAROMANCER(Difficulty.MEDIUM, "Defeat the Culinaromancer", 37065, null),
		DEFEAT_SCORPIA(Difficulty.MEDIUM, "Defeat Scorpia", 37066, null),
		DEFEAT_10_PLAYERS(Difficulty.MEDIUM, "Defeat 10 Players", 37067, new int[] {23, 10 }),
		LOW_ALCH_ITEMS(Difficulty.MEDIUM, "Low Alch 300 Items", 37068, new int[] { 24, 300 }),

		BURY_500_FROST_DRAGON_BONES(Difficulty.HARD, "Bury 500 Frost Dragon Bones", 37072, new int[] {25, 500 }),
		CHOP_750_YEW_LOGS(Difficulty.HARD, "Cut 750 Yew Logs", 37073, new int[] { 26, 750 }),
		BURN_500_YEW_LOGS(Difficulty.HARD, "Burn 500 Yew Logs", 37074, new int[] { 27, 500 }),
		FISH_700_MANTA(Difficulty.HARD, "Fish 700 Manta Ray", 37075, new int[] { 28, 700 }),
		COOK_500_MANTA(Difficulty.HARD, "Cook 500 Manta Ray", 37076, new int[] { 29, 500 }),
		MINE_400_ADDY(Difficulty.HARD, "Mine 300 Adamant Ores", 37077, new int[] { 30, 300 }),
		CRAFT_1000_DIAMOND_GEMS(Difficulty.HARD, "Craft 750 Diamond Gems", 37078, new int[] { 31, 750 }),
		ENCHANT_1000_BOLTS(Difficulty.HARD, "Enchant 1000 Bolts", 37079, new int[] { 32, 1000 }),
		HIGH_ALCH_ITEMS(Difficulty.HARD, "High Alch 1000 Items", 37080, new int[] { 33, 1000 }),
		STEAL_2000_TIMES(Difficulty.HARD, "Steal 2,000 Times", 37081, new int[] { 34, 2000 }),
		SMELT_300_ADAMANT_BARS(Difficulty.HARD, "Smelt 300 Adamant Bars", 37082, new int[] { 35, 300 }),
		MIX_100_OVERLOAD_POTIONS(Difficulty.HARD, "Mix 100 Overload Potions", 37083, new int[] { 36, 100 }),
		COMPLETE_AN_ELITE_SLAYER_TASK(Difficulty.HARD, "Complete a task from Duradel", 37084, null),
		DEAL_HARD_DAMAGE_USING_MELEE(Difficulty.HARD, "Deal 500K Melee Damage", 37085, new int[] { 37, 500000 }),
		DEAL_HARD_DAMAGE_USING_RANGED(Difficulty.HARD, "Deal 500K Ranged Damage", 37086, new int[] { 38, 500000 }),
		DEAL_HARD_DAMAGE_USING_MAGIC(Difficulty.HARD, "Deal 500K Magic Damage", 37087, new int[] { 39, 500000 }),
		DEFEAT_JAD(Difficulty.HARD, "Defeat Jad", 37088, null),
		DEFEAT_BANDOS_AVATAR(Difficulty.HARD, "Defeat Bandos Avatar", 37089, null),
		DEFEAT_GENERAL_GRAARDOR(Difficulty.HARD, "Defeat General Graardor", 37090, null),
		DEFEAT_KREE_ARRA(Difficulty.HARD, "Defeat Kree'Arra", 37091, null),
		DEFEAT_COMMANDER_ZILYANA(Difficulty.HARD, "Defeat Commander Zilyana", 37092, null),
		DEFEAT_KRIL_TSUTSAROTH(Difficulty.HARD, "Defeat K'ril Tsutsaroth", 37093, null),
		DEFEAT_THE_CORPOREAL_BEAST(Difficulty.HARD, "Defeat The Corporeal Beast", 37094, null),
		DEFEAT_NEX(Difficulty.HARD, "Defeat Nex", 37095, null),
		DEFEAT_30_PLAYERS(Difficulty.HARD, "Defeat 15 Players", 37096, new int[] { 40, 15 }),

		RUNECRAFT_6000_BLOOD_RUNES(Difficulty.ELITE, "Runecraft 6000 Blood Runes", 37099, new int[] { 41, 6000 }),
		CHOP_2500_MAGIC_LOGS(Difficulty.ELITE, "Chop 2500 Magic Logs", 37100, new int[] { 42, 2500 }),
		BURN_2500_MAGIC_LOGS(Difficulty.ELITE, "Burn 2500 Magic Logs", 37101, new int[] { 43, 2500 }),
		FISH_1500_ROCKTAILS(Difficulty.ELITE, "Fish 1500 Rocktails", 37102, new int[] { 44, 1500 }),
		COOK_1000_ROCKTAILS(Difficulty.ELITE, "Cook 1000 Rocktails", 37103, new int[] { 45, 1000 }),
		MINE_2000_RUNITE_ORES(Difficulty.ELITE, "Mine 1000 Runite Ores", 37104, new int[] { 46, 1000 }),
		SMELT_1000_RUNE_BARS(Difficulty.ELITE, "Smelt 1000 Rune Bars", 37105, new int[] { 47, 1000 }),
		COMPLETE_50_DUNG_FLOORS(Difficulty.ELITE, "Complete 50 Dung Floors", 37106, new int[] { 48, 50 }),
		INFUSE_500_STEEL_TITAN_POUCHES(Difficulty.ELITE, "Infuse 500 Steel Titans", 37107, new int[] { 49, 500 }),
		CATCH_100_KINGLY_IMPLINGS(Difficulty.ELITE, "Catch 100 Kingly Imps", 37108, new int[] {50, 100 }),
		FLETCH_5000_RUNE_ARROWS(Difficulty.ELITE, "Fletch 5000 Rune Arrows", 37109, new int[] {51, 5000 }),
		CUT_AN_ONYX_STONE(Difficulty.ELITE, "Cut an Onyx Stone", 37110, null),
		REACH_MAX_EXP_IN_A_SKILL(Difficulty.ELITE, "Reach Max Exp in a Skill", 37111, null),
		HIT_700_WITH_SPECIAL_ATTACK(Difficulty.ELITE, "Hit 700 with Special Attack", 37112, new int[] { 52, 1 }),
		DEFEAT_10000_MONSTERS(Difficulty.ELITE, "Defeat 10,000 Monsters", 37113, new int[] { 53, 10000 }),
		DEFEAT_500_BOSSES(Difficulty.ELITE, "Defeat 500 Boss Monsters", 37114, new int[] { 54, 500 }),
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
		public int[] progressData;

		public Difficulty getDifficulty() {
			return difficulty;
		}

		/**
		 * Get the name of your achievement
		 * @return
		 */
		public String getName() {
			return this.interfaceLine;
		}
	}

	public enum Difficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE;
	}

	public static boolean handleButton(Player player, int button) {
		if (!(button >= -28531 && button <= -28422)) {
			return false;
		}
		int index = -1;
		if (button >= -28531 && button <= -28500) {
			index = 28531 + button;
		} else if(button >= -28497 && button <= -28468) {
			index = 28529 + button;
		} else if(button >= -28464 && button <= -28440) {
			index = 28526 + button;
		} else if(button >= -28437 && button <= -28422) {
			index = 28524 + button;
		}
		if (index >= 0 && index < AchievementData.values().length) {
			AchievementData achievement = AchievementData.values()[index];
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				player.getPacketSender().sendMessage(
						"<icon=2> <col=339900>You have completed the achievement: " + achievement.interfaceLine + ".");
			} else if (achievement.progressData == null) {
				player.getPacketSender().sendMessage(
						"<icon=2> <col=660000>You have not started the achievement: " + achievement.interfaceLine + ".");
			} else {
				int progress = player.getAchievementAttributes().getProgress()[achievement.progressData[0]];
				int requiredProgress = achievement.progressData[1];
				if (progress == 0) {
					player.getPacketSender().sendMessage("<icon=2> <col=660000>You have not started the achievement: "
							+ achievement.interfaceLine + ".");
				} else if (progress != requiredProgress) {
					player.getPacketSender()
							.sendMessage("<icon=2> <col=8A6D00>Your progress for this achievement is currently at: "
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
		player.getPointsHandler().setAchievementPoints(1, true);
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender().sendString(achievement.interfaceFrame, ("@gre@") + achievement.interfaceLine)
				.sendMessage("<icon=3> <col=339900>You have completed the achievement "
						+ Misc.formatText(achievement.getName() + "."))
				.sendString(37001, "Achievements: " + player.getPointsHandler().getAchievementPoints() + "/"
						+ AchievementData.values().length);
		player.getPacketSender().sendString(1, "[ACHIEVEMENT]-"+ achievement.interfaceLine +"-"+achievement.getDifficulty().ordinal());
	}

	public static class AchievementAttributes {

		public AchievementAttributes() {
		}

		/** ACHIEVEMENTS **/
		private boolean[] completed = new boolean[AchievementData.values().length];
		private int[] progress = new int[55];

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
