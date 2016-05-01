package com.runelive.world.content.skill.impl.prayer;

public enum BonesData {
  BONES(526, 1444), BAT_BONES(530, 1444), WOLF_BONES(2859, 1444), BIG_BONES(532, 1140),
  BABYDRAGON_BONES(534, 2250), JOGRE_BONE(3125, 1140), ZOGRE_BONES(4812, 1672),
  LONG_BONES(10976, 3540), CURVED_BONE(10977, 3540), SHAIKAHAN_BONES(3123, 1900),
  DRAGON_BONES(536, 5472), FAYRG_BONES(4830, 6384), RAURG_BONES(4832, 6296),
  DAGANNOTH_BONES(6729, 7250), OURG_BONES(14793, 6500) ,WYVERN_BONES(6812, 7250), FROSTDRAGON_BONES(18830, 8000);

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
