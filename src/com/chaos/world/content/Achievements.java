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
		FLETCH_ARROW_SHAFT(Difficulty.EASY, "Fletch an arrow shaft", 37016, null),
		RUNECRAFT_RUNES(Difficulty.EASY, "Runecraft any elemental rune", 37017, null),
		CREATE_DUNG_PARTY(Difficulty.EASY, "Create a dungeoneering party", 37018, null), //TODO
		KILL_ROCKCRAB(Difficulty.EASY, "Kill a rock crab", 37019, null),
		KILL_SKELETON(Difficulty.EASY, "Kill a skeleton", 37020, null),
		KILL_YAK(Difficulty.EASY, "Kill a yak", 37021, null),
		INFUSE_WOLF_POUCH(Difficulty.EASY, "Infuse a wolf pouch", 37022, null),
		CREATE_CLAN_CHAT(Difficulty.EASY, "Create a clan chat", 37023, null), //TODO
		ADD_FRIEND(Difficulty.EASY, "Add a friend", 37025, null), //TODO
		GET_SLAYER_TASK(Difficulty.EASY, "Get a slayer task", 37025, null),
		SWITCH_SPELLBOOK(Difficulty.EASY, "Switch to another spellbook", 37026, null),
		SWITCH_PRAYBOOK(Difficulty.EASY, "Switch to the curse prayers", 37027, null),
		KILL_A_MONSTER_USING_MELEE(Difficulty.EASY, "Kill a Monster using Melee", 37028, null),
		KILL_A_MONSTER_USING_RANGED(Difficulty.EASY, "Kill a Monster using Ranged", 37029, null),
		KILL_A_MONSTER_USING_MAGIC(Difficulty.EASY, "Kill a Monster using Magic", 37030, null),
		DEAL_EASY_DAMAGE_USING_MELEE(Difficulty.EASY, "Deal 1000 Melee Damage", 37031, new int[] {0, 1000 }),
		DEAL_EASY_DAMAGE_USING_RANGED(Difficulty.EASY, "Deal 1000 Ranged Damage", 37032, new int[] {1, 1000 }),
		DEAL_EASY_DAMAGE_USING_MAGIC(Difficulty.EASY, "Deal 1000 Magic Damage", 37033, new int[] {2, 1000 }),
		PERFORM_A_SPECIAL_ATTACK(Difficulty.EASY, "Perform a Special Attack", 37034, null),

		ENTER_THE_LOTTERY_THREE_TIMES(Difficulty.MEDIUM, "Enter the Lottery 3 times", 37037, new int[] { 3, 1 }),
		FILL_WELL_OF_GOODWILL_50M(Difficulty.MEDIUM, "Pour 50M into the Well", 37038, new int[] { 4, 50000000 }),
		CUT_100_MAGIC_LOGS(Difficulty.MEDIUM, "Cut 100 Magic Logs", 37039, new int[] { 5, 100 }),
		BURN_100_MAGIC_LOGS(Difficulty.MEDIUM, "Burn 100 Magic Logs", 37040, new int[] { 6, 100 }),
		FISH_25_ROCKTAILS(Difficulty.MEDIUM, "Fish 25 Rocktails", 37041, new int[] { 7, 25 }),
		COOK_25_ROCKTAILS(Difficulty.MEDIUM, "Cook 25 Rocktails", 37042, new int[] { 8, 25 }),
		MINE_25_RUNITE_ORES(Difficulty.MEDIUM, "Mine 25 Runite Ores", 37043, new int[] { 9, 25 }),
		SMELT_25_RUNE_BARS(Difficulty.MEDIUM, "Smelt 25 Rune Bars", 37044, new int[] { 10, 25 }),
		HARVEST_10_TORSTOLS(Difficulty.MEDIUM, "Harvest 10 Torstols", 37045, new int[] { 11, 10 }),
		INFUSE_25_TITAN_POUCHES(Difficulty.MEDIUM, "Infuse 25 Steel Titans", 37046, new int[] { 12, 25 }),
		CATCH_5_KINGLY_IMPLINGS(Difficulty.MEDIUM, "Catch 5 Kingly Implings", 37047, new int[] {13, 5 }),
		COMPLETE_A_HARD_SLAYER_TASK(Difficulty.MEDIUM, "Complete a Hard Slayer Task", 37048, null),
		CRAFT_20_BLACK_DHIDE_BODIES(Difficulty.MEDIUM, "Craft 20 Black D'hide Bodies", 37049, new int[] {14, 20 }),
		FLETCH_450_RUNE_ARROWS(Difficulty.MEDIUM, "Fletch 450 Rune Arrows", 37050, new int[] {15, 450 }),
		STEAL_140_SCIMITARS(Difficulty.MEDIUM, "Pick-Pocket 150 times", 37051, new int[] {16, 150 }),
		MIX_AN_OVERLOAD_POTION(Difficulty.MEDIUM, "Mix an Overload Potion", 37052, null),
		ASSEMBLE_A_GODSWORD(Difficulty.MEDIUM, "Assemble a Godsword", 37053, null),
		CLIMB_50_AGILITY_OBSTACLES(Difficulty.MEDIUM, "Climb 50 Agility Obstacles", 37054, new int[] {17, 50 }),
		RUNECRAFT_500_BLOOD_RUNES(Difficulty.MEDIUM, "Runecraft 500 Blood Runes", 37055, new int[] {18, 500 }),
		BURY_25_FROST_DRAGON_BONES(Difficulty.MEDIUM, "Bury 25 Frost Dragon Bones", 37056, new int[] {19, 25 }),
		FIRE_500_CANNON_BALLS(Difficulty.MEDIUM, "Fire 500 Cannon Balls", 37057, new int[] {20, 500 }),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(Difficulty.MEDIUM, "Deal 100K Melee Damage", 37058, new int[] {21, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(Difficulty.MEDIUM, "Deal 100K Ranged Damage", 37059, new int[] {22, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(Difficulty.MEDIUM, "Deal 100K Magic Damage", 37060, new int[] {23, 100000 }),
		DEFEAT_THE_KING_BLACK_DRAGON(Difficulty.MEDIUM, "Defeat the King Black Dragon", 37061, null),
		DEFEAT_THE_CHAOS_ELEMENTAL(Difficulty.MEDIUM, "Defeat the Chaos Elemental", 37062, null),
		DEFEAT_A_TORMENTED_DEMON(Difficulty.MEDIUM, "Defeat a Tormented Demon", 37063, null),
		DEFEAT_THE_CULINAROMANCER(Difficulty.MEDIUM, "Defeat the Culinaromancer", 37064, null),
		DEFEAT_NOMAD(Difficulty.MEDIUM, "Defeat Nomad", 37065, null),
		DEFEAT_10_PLAYERS(Difficulty.MEDIUM, "Defeat 10 Players", 37066, new int[] {24, 10 }),
		REACH_A_KILLSTREAK_OF_3(Difficulty.MEDIUM, "Reach a Killstreak of 3", 37067, null),

		FILL_WELL_OF_GOODWILL_250M(Difficulty.HARD, "Pour 250M into the Well", 37070, new int[] { 25, 250000000 }),
		CUT_5000_MAGIC_LOGS(Difficulty.HARD, "Cut 2500 Magic Logs", 37071, new int[] { 26, 2500 }),
		BURN_2500_MAGIC_LOGS(Difficulty.HARD, "Burn 2500 Magic Logs", 37072, new int[] { 27, 2500 }),
		FISH_2000_ROCKTAILS(Difficulty.HARD, "Fish 1500 Rocktails", 37073, new int[] { 28, 1500 }),
		COOK_1000_ROCKTAILS(Difficulty.HARD, "Cook 1000 Rocktails", 37074, new int[] { 29, 1000 }),
		MINE_2000_RUNITE_ORES(Difficulty.HARD, "Mine 1000 Runite Ores", 37075, new int[] { 30, 1000 }),
		SMELT_1000_RUNE_BARS(Difficulty.HARD, "Smelt 1000 Rune Bars", 37076, new int[] { 31, 1000 }),
		HARVEST_1000_TORSTOLS(Difficulty.HARD, "Harvest 1000 Torstols", 37077, new int[] { 32, 1000 }),
		INFUSE_500_STEEL_TITAN_POUCHES(Difficulty.HARD, "Infuse 500 Steel Titans", 37078, new int[] { 33, 500 }),
		CRAFT_1000_DIAMOND_GEMS(Difficulty.HARD, "Craft 750 Diamond Gems", 37079, new int[] { 34, 750 }),
		CATCH_100_KINGLY_IMPLINGS(Difficulty.HARD, "Catch 100 Kingly Imps", 37080, new int[] {35, 100 }),
		FLETCH_5000_RUNE_ARROWS(Difficulty.HARD, "Fletch 5000 Rune Arrows", 37081, new int[] {36, 5000 }),
		STEAL_5000_SCIMITARS(Difficulty.HARD, "Steal 2,000 Times", 37082, new int[] {37, 2000 }),
		RUNECRAFT_8000_BLOOD_RUNES(Difficulty.HARD, "Runecraft 6000 Blood Runes", 37083, new int[] {38, 6000 }),
		BURY_500_FROST_DRAGON_BONES(Difficulty.HARD, "Bury 500 Frost Dragon Bones", 37084, new int[] {39, 500 }),
		FIRE_5000_CANNON_BALLS(Difficulty.HARD, "Fire 3000 Cannon Balls", 37085, new int[] {40, 3000 }),
		MIX_100_OVERLOAD_POTIONS(Difficulty.HARD, "Mix 100 Overload Potions", 37086, new int[] {41, 100 }),
		COMPLETE_AN_ELITE_SLAYER_TASK(Difficulty.HARD, "Complete an Elite Slayer Task", 37087, null),
		ASSEMBLE_5_GODSWORDS(Difficulty.HARD, "Assemble 5 Godswords", 37088, new int[] {42, 5 }),
		DEAL_HARD_DAMAGE_USING_MELEE(Difficulty.HARD, "Deal 5M Melee Damage", 37089, new int[] {43, 5000000 }),
		DEAL_HARD_DAMAGE_USING_RANGED(Difficulty.HARD, "Deal 5M Ranged Damage", 37090, new int[] {44, 5000000 }),
		DEAL_HARD_DAMAGE_USING_MAGIC(Difficulty.HARD, "Deal 5M Magic Damage", 37091, new int[] {45, 5000000 }),
		DEFEAT_JAD(Difficulty.HARD, "Defeat Jad", 37092, null),
		DEFEAT_BANDOS_AVATAR(Difficulty.HARD, "Defeat Bandos Avatar", 37093, null),
		DEFEAT_GENERAL_GRAARDOR(Difficulty.HARD, "Defeat General Graardor", 37094, null),
		DEFEAT_KREE_ARRA(Difficulty.HARD, "Defeat Kree'Arra", 37095, null),
		DEFEAT_COMMANDER_ZILYANA(Difficulty.HARD, "Defeat Commander Zilyana", 37096, null),
		DEFEAT_KRIL_TSUTSAROTH(Difficulty.HARD, "Defeat K'ril Tsutsaroth", 37097, null),
		DEFEAT_THE_CORPOREAL_BEAST(Difficulty.HARD, "Defeat The Corporeal Beast", 37098, null),
		DEFEAT_NEX(Difficulty.HARD, "Defeat Nex", 37099, null),
		DEFEAT_30_PLAYERS(Difficulty.HARD, "Defeat 30 Players", 37100, new int[] {46, 30 }),
		REACH_A_KILLSTREAK_OF_6(Difficulty.HARD, "Reach a Killstreak of 6", 37101, null),

		COMPLETE_ALL_HARD_TASKS(Difficulty.ELITE, "Complete all Hard Tasks", 37104, new int[] { 47, 32 }),
		CUT_AN_ONYX_STONE(Difficulty.ELITE, "Cut an Onyx Stone", 37105, null),
		REACH_MAX_EXP_IN_A_SKILL(Difficulty.ELITE, "Reach Max Exp in a Skill", 37106, null),
		HIT_700_WITH_SPECIAL_ATTACK(Difficulty.ELITE, "Hit 700 with Special Attack", 37107, new int[] { 48, 1 }),
		DEFEAT_10000_MONSTERS(Difficulty.ELITE, "Defeat 10,000 Monsters", 37108, new int[] { 49, 10000 }),
		DEFEAT_500_BOSSES(Difficulty.ELITE, "Defeat 500 Boss Monsters", 37109, new int[] { 50, 500 }),
		VOTE_100_TIMES(Difficulty.ELITE, "Vote 100 times", 37110, new int[] { 51, 100 }),
		// UNLOCK_ALL_LOYALTY_TITLES(Difficulty.ELITE, "Kill Zulrah 50 Times",
		// 37111, new int[]{52,
		// 50}),
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

		if (achievement.getDifficulty() == Difficulty.HARD) {
			doProgress(player, AchievementData.COMPLETE_ALL_HARD_TASKS);
		}
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
