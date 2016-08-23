package com.chaos.world.content.skill.impl.firemaking;

import com.chaos.world.entity.impl.player.Player;

public class Logdata {

	public static enum logData {

		LOG(1511, 1, 40, 30), ACHEY(2862, 1, 40, 30), OAK(1521, 15, 60, 40), WILLOW(1519, 30, 90, 45), TEAK(
				6333, 35, 105, 45), ARCTIC_PINE(10810, 42, 125, 45), MAPLE(1517, 45, 135, 45), MAHOGANY(6332, 50,
						158,
						45), EUCALYPTUS(12581, 58, 194, 45), YEW(1515, 60, 203, 50), MAGIC(1513, 75, 304, 50);

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
