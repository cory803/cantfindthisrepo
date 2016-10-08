package com.chaos.world.content.skill.impl.prayer;

public enum BonesData {
	BONES(526, 1), BAT_BONES(530, 1), WOLF_BONES(2859, 2), BIG_BONES(532, 4), BABYDRAGON_BONES(534, 12),
	JOGRE_BONE(3125, 15), ZOGRE_BONES(4812, 16), LONG_BONES(10976, 18), CURVED_BONE(10977, 22),
	SHAIKAHAN_BONES(3123, 25), DRAGON_BONES(536, 35), FAYRG_BONES(4830, 32),
	RAURG_BONES(4832, 30), DAGANNOTH_BONES(6729, 42),
	OURG_BONES(14793, 43), WYVERN_BONES(6812, 50), FROSTDRAGON_BONES(18830, 75),
	IMPIOUS_ASHES(20264, 4), ACCURSED_ASHES(20266, 13), INFERNAL_ASHES(20268, 34), LAVA_DRAGON_BONES(11943, 75);

	BonesData(int boneId, int buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	private int boneId;
	private int buryXP;

	public int getBoneID() {
		return this.boneId;
	}

	public int getBuryingXP() {
		return this.buryXP;
	}

	public static BonesData forId(int bone) {
		for (BonesData prayerData : BonesData.values()) {
			if (prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}

}
