package com.chaos.world.content.skill.impl.thieving;

public enum ThievingStall {

    GENERAL_STALL(4876, 1, 4, StallRewards.GENERAL_STALL, 7, false),
    CRAFTING_STALL(4874, 5, 9, StallRewards.CRAFTING_STALL, 7, false),
    FOOD_STALL(4875, 15, 12, StallRewards.FOOD_STALL, 7, false),
    SILK_STALL(2560, 20, 14, StallRewards.SILK_STALL, 5, false),
    FUR_STALL(2563, 35, 22, StallRewards.FUR_STALL, 10, false),
    VEGETABLE_STALL(4706, 45, 30, StallRewards.VEGETABLE_STALL, 180, true),
    DONATOR_FISH_STALL(4705, 50, 32, StallRewards.DONATOR_FISH_STALL, 180, true),
    SILVER_STALL(2565, 50, 36, StallRewards.SILVER_STALL, 30, false),
    BAKERS_STALL(2561, 55, 39, StallRewards.BAKERS_STALL, 2, false),
    SPICE_STALL(2564, 65, 42, StallRewards.SPICE_STALL, 80, false),
    MAGIC_STALL(4877, 65, 42, StallRewards.MAGIC_STALL, 80, false),
    GEM_STALL(2562, 75, 52, StallRewards.GEM_STALL, 80, false),
    SCIMITAR_STALL(4878, 95, 65, StallRewards.SCIMITAR_STALL, 180, false);

    private int objectId, requiredLevel, experience, respawnDelay;
    private StallRewards stallRewards;
    private boolean donator;

    ThievingStall(int objectId, int requiredLevel, int experience, StallRewards stallRewards,
                  int respawnDelay, boolean donator) {
        this.objectId = objectId;
        this.requiredLevel = requiredLevel;
        this.experience = experience;
        this.stallRewards = stallRewards;
        this.respawnDelay = respawnDelay;
        this.donator = donator;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public int getRequiredLevel() {
        return this.requiredLevel;
    }

    public int getExperience() {
        return this.experience;
    }

    public StallRewards getStallRewards() {
        return this.stallRewards;
    }

    public int getRespawnDelay() {
        return this.respawnDelay;
    }

    public boolean isDonator() { return this.donator; }

    public static ThievingStall forId(int objectId) {
        for (ThievingStall stall : ThievingStall.values()) {
            if (stall.getObjectId() == objectId) {
                return stall;
            }
        }

        return null;
    }

    public static ThievingStall forOrdinal(int ordinalId) {
        for (ThievingStall stall : ThievingStall.values()) {
            if (stall.ordinal() == ordinalId) {
                return stall;
            }
        }

        return null;
    }

}
