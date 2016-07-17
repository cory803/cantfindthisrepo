package com.runelive.world.content.skill.thieving;

import java.util.Arrays;

public enum StallRewards {

    BAKERS_STALL(
            new WeightedItem(1891, 1, 1),
            new WeightedItem(1898, 1, 1),
            new WeightedItem(2309, 1, 1)),
    CRAFTING_STALL(
            new WeightedItem(1755, 1, 1024),
            new WeightedItem(1592, 1, 1024),
            new WeightedItem(1595, 1, 1024),
            new WeightedItem(1597, 1, 1024)),
    FOOD_STALL(
            new WeightedItem(5974, 1, 100)),
    GENERAL_STALL(
            new WeightedItem(590, 1, 100)),
    SILK_STALL(
            new WeightedItem(950, 1, 100)),
    FUR_STALL(
            new WeightedItem(958, 1, 100)),
    SILVER_STALL(new WeightedItem(442, 1, 100)),
    SPICE_STALL(new WeightedItem(2007, 1, 100)),
    MAGIC_STALL(
            new WeightedItem(1379, 1, 124),
            new WeightedItem(1381, 1, 1),
            new WeightedItem(1385, 1, 1),
            new WeightedItem(1387, 1, 1),
            new WeightedItem(1383, 1, 1)),
    GEM_STALL(
            new WeightedItem(1624, 1, 5),
            new WeightedItem(1621, 1, 5),
            new WeightedItem(1619, 1, 5),
            new WeightedItem(1617, 1, 5),
            new WeightedItem(1631, 1, 30)),
    SCIMITAR_STALL(
            new WeightedItem(1321, 1, 5),
            new WeightedItem(1324, 1, 5),
            new WeightedItem(1325, 1, 5),
            new WeightedItem(1327, 1, 5),
            new WeightedItem(1329, 1, 5),
            new WeightedItem(1333, 1, 5),
            new WeightedItem(4587, 1, 30));

    public final WeightedItem[] rewards;
    public final int weightSum;

    StallRewards(WeightedItem... rewards) {
        this.rewards = rewards;
        weightSum = Arrays.asList(rewards).stream().mapToInt(WeightedItem::getWeight).sum();
    }

    public WeightedItem[] getRewards() {
        return this.rewards;
    }

    public int getWeightSum() {
        return weightSum;
    }

}


class WeightedItem {

    private final int id;
    private final int amount;
    private final int weight;

    public WeightedItem(int id, int amount, int weight) {
        this.id = id;
        this.amount = amount;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getWeight() {
        return weight;
    }

}
