package com.runelive.world.content.skill.impl.slayer;

import com.runelive.model.Position;
import com.runelive.util.Misc;

/**
 * @author Gabriel Hannason
 */

public enum SlayerTasks {

	NO_TASK(null, 0, null, 0, null),

	/**
	 * Easy tasks
	 */
	ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), AQUA_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), RED_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), PINK_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), WHITE_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), LIME_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), PURPLE_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), ORGANGE_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), YELLOW_ROCK_CRAB(SlayerMaster.VANNAKA, 1265, "Rock Crabs can be found in Relleka.", 2100, new Position(2681, 3715)), EXPERIMENT(SlayerMaster.VANNAKA, 1677, "Experiments can be found in the Training Teleport.", 2150, new Position(3564, 9954, 0)), GIANT_BAT(SlayerMaster.VANNAKA, 78, "Giant Bats can be found in Taverly Dungeon.", 2000, new Position(2907, 9833)), CHAOS_DRUID(SlayerMaster.VANNAKA, 181, "Chaos Druids can be found in Edgeville Dungeon.", 2120, new Position(3109, 9931, 0)), ZOMBIE(SlayerMaster.VANNAKA, 76, "Zombies can be found in Edgeville Dungeon.", 2000, new Position(3144, 9903, 0)), HOBGOBLIN(SlayerMaster.VANNAKA, 2686, "Hobgoblins can be found in Edgeville Dungeon.", 4500, new Position(3123, 9876, 0)), HILL_GIANT(SlayerMaster.VANNAKA, 117, "Hill Giants can be found in Edgeville Dungeon.", 4700, new Position(3120, 9844, 0)), DEADLY_RED_SPIDER(SlayerMaster.VANNAKA, 63, "Deadly Red Spiders can be found in Edgeville Dungeon.", 4500, new Position(3083, 9940, 0)), BABY_BLUE_DRAGON(SlayerMaster.VANNAKA, 52, "Baby Blue Dragons can be found in Taverly Dungeon.", 5000, new Position(2891, 9772, 0)), SKELETON(SlayerMaster.VANNAKA, 90, "Skeletons can be found in Edgeville Dungeon.", 2200, new Position(3094, 9896)), EARTH_WARRIOR(SlayerMaster.VANNAKA, 124, "Earth Warriors can be found in Edgeville Dungeon.", 540, new Position(3124, 9986)), YAK(SlayerMaster.VANNAKA, 5529, "Yaks can be found in the Training Teleport.", 2500, new Position(3203, 3267)), GHOUL(SlayerMaster.VANNAKA, 1218, "Ghouls can be found in the Training Teleport.", 4800, new Position(3418, 3508)),

	/**
	 * Medium tasks
	 */
	CHAOS_DWOGRE1(SlayerMaster.DURADEL, 8771, "Chaos Dwogre's can be found somewhere in the Chaos Tunnels.", 13877, new Position(3248, 5489)), CHAOS_DWARF_HAND_CANNONEER1(SlayerMaster.DURADEL, 8776, "Chaos Dwarf hand cannoneer's can be found somewhere in the Chaos Tunnels.", 13877, new Position(3248, 5489)), BANDIT(SlayerMaster.DURADEL, 1880, "Bandits can be found in the Training teleport.", 6500, new Position(3172, 2976)), WILD_DOG(SlayerMaster.DURADEL, 1593, "Wild Dogs can be found in Brimhaven Dungeon.", 6700, new Position(2680, 9557)), MOSS_GIANT(SlayerMaster.DURADEL, 112, "Moss Giants can be found in Brimhaven Dungeon.", 6600, new Position(2676, 9549)), FIRE_GIANT(SlayerMaster.DURADEL, 110, "Fire Giants can be found in Brimhaven Dungeon.", 6900, new Position(2664, 9480)), GREEN_DRAGON(SlayerMaster.DURADEL, 941, "Green Dragons can be found in western Wilderness.", 7500, new Position(2977, 3615)), BLUE_DRAGON(SlayerMaster.DURADEL, 55, "Blue Dragons can be found in Taverly Dungeon.", 8000, new Position(2892, 9799)), HELLHOUND(SlayerMaster.DURADEL, 49, "Hellhounds can be found in Taverly Dungeon.", 8000, new Position(2870, 9848)), BLACK_DEMON(SlayerMaster.DURADEL, 84, "Black Demons can be found in Edgeville Dungeon.", 8270, new Position(3089, 9967)), BLOODVELD(SlayerMaster.DURADEL, 1618, "Bloodvelds can be found in Slayer Tower.", 7200, new Position(3418, 3570, 1)), INFERNAL_MAGE(SlayerMaster.DURADEL, 1643, "Infernal Mages can be found in Slayer Tower.", 7000, new Position(3445, 3579, 1)), ABERRANT_SPECTRE(SlayerMaster.DURADEL, 1604, "Aberrant Spectres can be found in Slayer Tower.", 7300, new Position(3432, 3553, 1)), GARGOYLE(SlayerMaster.DURADEL, 1610, "Gargoyles can be found in Slayer Tower.", 8100, new Position(3438, 3534, 2)), TZHAAR_XIL(SlayerMaster.DURADEL, 2605, "TzHaar-Xils can be found in Tzhaar City.", 9000, new Position(2445, 5147)), TZHAAR_HUR(SlayerMaster.DURADEL, 2600, "TzHaar-Hurs can be found in Tzhaar City.", 7900, new Position(2456, 5135)), ORK(SlayerMaster.DURADEL, 6273, "Orks can be found in the Godwars Dungeon.", 7600, new Position(2867, 5322)), ARMOURED_ZOMBIE(SlayerMaster.DURADEL, 8162, "Armoured Zombies can be found in the Training Teleport", 8000, new Position(3085, 9672)), DUST_DEVIL(SlayerMaster.DURADEL, 1624, "Dust Devils can be found in the Training Teleport", 9500, new Position(3279, 2964)), JUNGLE_STRYKEWYRM(SlayerMaster.DURADEL, 9467, "Strykewyrms can be found in the Strykewyrm Cavern", 10874, new Position(2731, 5095)), DESERT_STRYKEWYRM(SlayerMaster.DURADEL, 9465, "Strykewyrms can be found in the Strykewyrm Cavern.", 11120, new Position(2731, 5095)), ABYSSAL_DEMON(SlayerMaster.DURADEL, 1615, "Abyssal Demons can be found in Slayer Tower.", 15600, new Position(3418, 3564, 2)), DARK_BEAST(SlayerMaster.DURADEL, 2783, "Dark Beasts can be found in Slayer Tower.", 16000, new Position(3423, 3550, 2)), MONKEY_GUARD(SlayerMaster.KURADEL, 1459, "Monkey Guards can be found in the Training Teleport", 14000, new Position(2795, 2775)), FROST_DRAGON(SlayerMaster.KURADEL, 51, "Frost Dragons can be found in the deepest of Wilderness.", 22570, new Position(2968, 3902)), SKELETAL_WYVERN1(SlayerMaster.DURADEL, 3068, "Wyvern's can be found somewhere in the exntrance of the Chaos Tunnels.", 13877, new Position(3182, 5471)),

	/**
	 * Hard tasks
	 */
	CHAOS_DWOGRE(SlayerMaster.KURADEL, 8771, "Chaos Dwogre's can be found somewhere in the Chaos Tunnels.", 13877, new Position(3248, 5489)), SKELETAL_WYVERN(SlayerMaster.KURADEL, 3068, "Wyvern's can be found somewhere in the exntrance of the Chaos Tunnels.", 13877, new Position(3182, 5471)), CHAOS_DWARF_HAND_CANNONEER(SlayerMaster.KURADEL, 8776, "Chaos Dwarf hand cannoneer's can be found somewhere in the Chaos Tunnels.", 13877, new Position(3248, 5489)), WATERFIEND(SlayerMaster.KURADEL, 5361, "Waterfiends can be found in the Ancient Cavern.", 13400, new Position(1737, 5353)), ICE_STRYKEWYRM(SlayerMaster.KURADEL, 9463, "Strykewyrms can be found in the Strykewyrm Cavern.", 13877, new Position(2731, 5095)), STEEL_DRAGON(SlayerMaster.KURADEL, 1592, "Steel dragons can be found in Brimhaven Dungeon.", 15600, new Position(2710, 9441)), MITHRIL_DRAGON(SlayerMaster.KURADEL, 5363, "Mithril Dragons can be found in the Ancient Cavern.", 16000, new Position(1761, 5329, 1)), GREEN_BRUTAL_DRAGON(SlayerMaster.KURADEL, 5362, "Green Brutal Dragons can be found in the Ancient Cavern.", 15590, new Position(1767, 5340)), SKELETON_WARLORD(SlayerMaster.KURADEL, 6105, "Skeleton Warlords can be found in the Ancient Cavern.", 14400, new Position(1763, 5358)), SKELETON_BRUTE(SlayerMaster.KURADEL, 6104, "Skeleton Brutes can be found in the Ancient Cavern.", 14400, new Position(1788, 5335)), AVIANSIE(SlayerMaster.KURADEL, 6246, "Aviansies can be found in the Godwars Dungeon.", 14600, new Position(2868, 5268, 2)), NECHRYAEL(SlayerMaster.KURADEL, 1613, "Nechryaels can be found in Slayer Tower.", 14600, new Position(3448, 3564, 2)), ABYSSAL_DEMON2(SlayerMaster.KURADEL, 1615, "Abyssal Demons can be found in Slayer Tower.", 15600, new Position(3418, 3564, 2)), DARK_BEAST2(SlayerMaster.KURADEL, 2783, "Dark Beasts can be found in Slayer Tower.", 16000, new Position(3423, 3550, 2)), GARGOYLE2(SlayerMaster.KURADEL, 1610, "Gargoyles can be found in Slayer Tower.", 10400, new Position(3438, 3534, 2)), ABERRANT_SPECTRE2(SlayerMaster.KURADEL, 1604, "Aberrant Spectres can be found in Slayer Tower.", 10400, new Position(3432, 3553, 1)), HELLHOUND2(SlayerMaster.KURADEL, 49, "Hellhounds can be found in Taverly Dungeon.", 9400, new Position(2870, 9848)),
	/**
	 * Elite
	 */
	CERBERUS(SlayerMaster.SUMONA, 5866, "Ceberus can be found in the sewers of Varrock.", 100000, new Position(1240, 1244)), NEX(SlayerMaster.SUMONA, 13447, "Nex can be found in the Godwars Dungeon.", 100000, new Position(2903, 5203)), GENERAL_GRAARDOR(SlayerMaster.SUMONA, 6260, "General Graardor can be found in the Godwars Dungeon.", 68000, new Position(2863, 5354, 2)), KRIL_TSUTSAROTH(SlayerMaster.SUMONA, 6203, "K'ril Tsutsaroth can be found in the Godwars Dungeon.", 68000, new Position(2925, 5332, 2)), COMMANDER_ZILYANA(SlayerMaster.SUMONA, 6247, "Commander Zilyana can be found in the Godwars Dungeon.", 68000, new Position(2908, 5265, 0)), KREE_ARRA(SlayerMaster.SUMONA, 6222, "Kree'Arra can be found in the Godwars Dungeon.", 68000, new Position(2839, 5295, 2)), TORMENTED_DEMON(SlayerMaster.SUMONA, 8349, "Tormented Demons can be found using the Boss teleport.", 40000, new Position(2718, 9820)), KING_BLACK_DRAGON(SlayerMaster.SUMONA, 50, "The King Black Dragon can be found using the Boss teleport.", 26000, new Position(2273, 4680, 1)), DAGANNOTH_SUPREME(SlayerMaster.SUMONA, 2881, "The Dagannoth Kings can be found using the Boss teleport.", 26000, new Position(1908, 4367)), DAGANNOTH_REX(SlayerMaster.SUMONA, 2883, "The Dagannoth Kings can be found using the Boss teleport.", 26000, new Position(1908, 4367)), DAGANNOTH_PRIME(SlayerMaster.SUMONA, 2882, "The Dagannoth Kings can be found using the Boss teleport.", 26000, new Position(1908, 4367)), CHAOS_ELEMENTAL(SlayerMaster.SUMONA, 3200, "The Chaos Elemental can be found using the Boss teleport.", 58000, new Position(3285, 3921)), SLASH_BASH(SlayerMaster.SUMONA, 2060, "Slash Bash can be found using the Boss teleport.", 28000, new Position(2547, 9448)), KALPHITE_QUEEN(SlayerMaster.SUMONA, 1160, "The Kalphite Queen can be found using the Boss teleport.", 31000, new Position(3476, 9502)), PHOENIX(SlayerMaster.SUMONA, 8549, "The Phoenix can be found using the Boss teleport.", 21000, new Position(2839, 9557)), CORPOREAL_BEAST(SlayerMaster.SUMONA, 8133, "The Corporeal Beast can be found using the Boss teleport.", 80000, new Position(2885, 4375)), BANDOS_AVATAR(SlayerMaster.SUMONA, 4540, "The Bandos Avatar can be found using the Boss teleport.", 34000, new Position(2891, 4767)), GLACORS(SlayerMaster.SUMONA, 1382, "Glacor's can be found using the Boss teleport.", 34000, new Position(3052, 9582)), GANODERMIC_BEAST(SlayerMaster.SUMONA, 130, "Ganodermic Beast's can be found using the Boss teleport.", 34000, new Position(2254, 3190)), VENENATIS(SlayerMaster.SUMONA, 2000, "Venenatis can be found in the wilderness.", 100000, new Position(3364, 3001)), SCORPIA(SlayerMaster.SUMONA, 2001, "Scorpia can be found in the wilderness.", 100000, new Position(3364, 3001)),

	WILDERNESS_NECHRYAEL(SlayerMaster.NASTROTH, 5532,
			"Wilderness Nechryaels can be found in the Wilderness Slayer area.", 29200,
			new Position(3448, 3564, 2)), WILDERNESS_DARK_BEAST(SlayerMaster.NASTROTH, 5518,
					"Wilderness Dark Beasts can be found in the Wilderness Slayer area.", 32000,
					new Position(3448, 3564, 2)), WILDERNESS_ABYSSAL_DEMONS(SlayerMaster.NASTROTH, 5533,
							"Wilderness Abyssal Demons can be found in the Wilderness Slayer Area.", 31200,
							new Position(3448, 3564, 2));

	/*
	 * @param taskMaster
	 * 
	 * @param npcId
	 * 
	 * @param npcLocation
	 * 
	 * @param XP
	 * 
	 * @param taskPosition
	 */

	private SlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
		this.taskMaster = taskMaster;
		this.npcId = npcId;
		this.npcLocation = npcLocation;
		this.XP = XP;
		this.taskPosition = taskPosition;
	}

	private SlayerMaster taskMaster;
	private int npcId;
	private String npcLocation;
	private int XP;
	private Position taskPosition;

	public SlayerMaster getTaskMaster() {
		return this.taskMaster;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String getNpcLocation() {
		return this.npcLocation;
	}

	public int getXP() {
		return this.XP;
	}

	public Position getTaskPosition() {
		return this.taskPosition;
	}

	public static SlayerTasks forId(int id) {
		for (SlayerTasks tasks : SlayerTasks.values()) {
			if (tasks.ordinal() == id) {
				return tasks;
			}
		}
		return null;
	}

	public static int[] getNewTaskData(SlayerMaster master) {
		int slayerTaskId = 1, slayerTaskAmount = 20;
		int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0, wildernessTasks = 0;

		/*
		 * Calculating amount of tasks
		 */
		for (SlayerTasks task : SlayerTasks.values()) {
			if (task.getTaskMaster() == SlayerMaster.VANNAKA)
				easyTasks++;
			else if (task.getTaskMaster() == SlayerMaster.DURADEL)
				mediumTasks++;
			else if (task.getTaskMaster() == SlayerMaster.KURADEL)
				hardTasks++;
			else if (task.getTaskMaster() == SlayerMaster.SUMONA)
				eliteTasks++;
			else if (task.getTaskMaster() == SlayerMaster.NASTROTH)
				wildernessTasks++;
		}

		/*
		 * Getting a task
		 */
		if (master == SlayerMaster.VANNAKA) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = Misc.exclusiveRandom(10, 30);
		} else if (master == SlayerMaster.DURADEL) {
			slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
			slayerTaskAmount = Misc.exclusiveRandom(50, 90);
		} else if (master == SlayerMaster.KURADEL) {
			slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
			slayerTaskAmount = Misc.exclusiveRandom(90, 150);
		} else if (master == SlayerMaster.SUMONA) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
			slayerTaskAmount = Misc.exclusiveRandom(5, 25);
		} else if (master == SlayerMaster.NASTROTH) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(wildernessTasks - 1);
			slayerTaskAmount = Misc.exclusiveRandom(45, 75);
		}
		return new int[] { slayerTaskId, slayerTaskAmount };
	}
}
