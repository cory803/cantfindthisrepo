package com.runelive.world.content.skill.impl.firemaking;

import com.runelive.world.entity.impl.player.Player;


public class Logdata {

  public static enum logData {

    LOG(1511, 1, 2000, 30),
    ACHEY(2862, 1, 2000, 30),
    OAK(1521, 15, 3000, 40),
    WILLOW(1519, 30, 4500, 45),
    TEAK(6333, 35, 5250, 45),
    ARCTIC_PINE(10810, 42, 6250, 45),
    MAPLE(1517, 45, 6750, 45),
    MAHOGANY(6332, 50, 7900, 45),
    EUCALYPTUS(12581, 58, 8500, 45),
    YEW(1515, 60, 10100, 50),
    MAGIC(1513, 75, 12150, 50);

    private int logId, level, burnTime;
    private int xp;

    private logData(int logId, int level, int xp, int burnTime) {
      this.logId = logId;
      this.level = level;
      this.xp = xp;
      this.burnTime = burnTime;
    }

    public int getLogId() {
      return logId;
    }

    public int getLevel() {
      return level;
    }

    public int getXp() {
      return xp;
    }

    public int getBurnTime() {
      return this.burnTime;
    }
  }

  public static logData getLogData(Player p, int log) {
    for (final Logdata.logData l : Logdata.logData.values()) {
      if (log == l.getLogId() || log == -1 && p.getInventory().contains(l.getLogId())) {
        return l;
      }
    }
    return null;
  }

}
