package com.chaos.world.content.skill.impl.farming;

public enum Plants {

	GUAM(5291, 199, 4, 173, 170, 9, 5, SeedType.HERB, 11, 13, 5),
	MARENTILL(5292, 201, 11, 173, 170, 14, 5, SeedType.HERB, 14, 18, 5),
	TARROMIN(5293, 203, 18, 173, 170, 19, 5, SeedType.HERB, 16, 18, 5),
	HARRALANDER(5294, 205, 25, 173, 170, 26, 5, SeedType.HERB, 22, 24, 5),
	RANARR(5295, 207, 32, 173, 170, 32, 5, SeedType.HERB, 27, 31, 5),
	TOADFLAX(5296, 3050, 39, 173, 170, 36, 5, SeedType.HERB, 34, 39, 5),
	IRIT(5297, 209, 46, 173, 170, 44, 5, SeedType.HERB, 43, 49, 5),
	AVANTOE(5298, 211, 53, 173, 170, 50, 5, SeedType.HERB, 54, 62, 5),
	WERGALI(14870, 14836, 60, 173, 170, 46, 5, SeedType.HERB, 51, 53, 5),
	KWUARM(5299, 213, 68, 173, 170, 56, 5, SeedType.HERB, 69, 78, 5),
	SNAPDRAGON(5300, 3052, 75, 173, 170, 62, 5, SeedType.HERB, 88, 99, 5),
	CADANTINE(5301, 215, 82, 173, 170, 67, 5, SeedType.HERB, 106, 120, 5),
	LANTADYME(5302, 2486, 89, 173, 170, 73, 5, SeedType.HERB, 135, 152, 5),
	DWARF_WEED(5303, 217, 96, 173, 170, 79, 5, SeedType.HERB, 171, 192, 5),
	TORSTOL(5304, 219, 103, 173, 170, 85, 5, SeedType.HERB, 200, 226, 5),

	POTATO(5318, 1942, 6, 0, 0, 1, 7, SeedType.ALLOTMENT, 8, 10, 4),
	ONION(5319, 1957, 13, 0, 0, 5, 7, SeedType.ALLOTMENT, 10, 12, 4),
	CABBAGE(5324, 1967, 20, 0, 0, 7, 7, SeedType.ALLOTMENT, 10, 12, 4),
	TOMATO(5322, 1982, 27, 0, 0, 12, 7, SeedType.ALLOTMENT, 12, 14, 4),
	SWEETCORN(5320, 7088, 34, 0, 0, 20, 7, SeedType.ALLOTMENT, 17, 19, 5),
	STRAWBERRY(5323, 5504, 43, 0, 0, 31, 6, SeedType.ALLOTMENT, 26, 29, 6),
	WATERMELON(5321, 5982, 52, 0, 0, 47, 4, SeedType.ALLOTMENT, 48, 54, 8),

	MARIGOLD(5096, 6010, 8, 0, 0, 2, 7, SeedType.FLOWER, 8, 5, 4),
	ROSEMARY(5097, 6014, 13, 0, 0, 11, 7, SeedType.FLOWER, 12, 67, 4),
	NASTURTIUM(5098, 6012, 18, 0, 0, 24, 7, SeedType.FLOWER, 20, 111,4),
	WOAD(5099, 5738, 23, 0, 0, 25, 7, SeedType.FLOWER, 21, 116, 4),
	LIMPWURT(5100, 225, 28, 0, 0, 26, 7, SeedType.FLOWER, 21, 120, 5),
	WHITE_LILY(14589, 14583, 37, 0, 0, 52, 7, SeedType.FLOWER, 50, 250, 4);

	public final int seed;
	public final int harvest;
	public final int healthy;
	public final int diseased;
	public final int dead;
	public final int level;
	public final int minutes;
	public final byte stages;
	public final double plantExperience;
	public final double harvestExperience;
	public final SeedType type;

	public static boolean isSeed(int id) {
		for (Plants i : values()) {
			if (i.seed == id) {
				return true;
			}
		}

		return false;
	}

	Plants(int seed, int harvest, int config, int diseased, int dead, int level, int minutes, SeedType type,
			double plantExperience, double harvestExperience, int stages) {
		this.seed = seed;
		this.harvest = harvest;
		healthy = config;
		this.level = level;
		this.diseased = diseased;
		this.dead = dead;
		this.minutes = minutes;
		this.type = type;
		this.plantExperience = plantExperience;
		this.harvestExperience = harvestExperience;
		this.stages = ((byte) stages);
	}
}
