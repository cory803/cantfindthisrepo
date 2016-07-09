package com.runelive.world.content.tasks;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.runelive.model.Skill;
import com.runelive.util.Misc;
import com.runelive.world.entity.impl.player.Player;

/**
 * Created by Vados on 27/05/2016.
 */

public class DailyTaskManager {

	public static void resetTask(Player p) {
		p.completedDailyTask = true;
		// p.dailyTaskProgress = 0;
		p.dailyTask = 0;
	}

	private static int getTodayDate() {
		Calendar cal = new GregorianCalendar();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		return (month * 100 + day);
	}

	private static String dailyTask(int i) {
		switch (i) {
		/**
		 * Skilling tasks
		 */
		case 0:
			return "Catch 100 Fish from any fishing spot.";
		case 1:
			return "Chop down 100 Logs from any tree.";
		case 2:
			return "Steal 100 times from any thieving stall.";
		case 3:
			return "Fletch 100 Short or Long bows.";
		case 4:
			return "Fletch 1000 Bolts or Arrows.";
		case 5:
			return "Add 1000 gem tips to Bolts.";
		case 6:
			return "Successfully cook 100 Fish.";
		case 7:
			return "Smith 100 bars (any).";
		case 8:
			return "Make 100 Summoning Scrolls.";
		case 9:
			return "Exchange stardust.";
		case 10:
			return "Brew 100 Potions.";
		/**
		 * Combat related tasks (easy)
		 */
		case 11:
			return "Cast Low/High Alchemy 200 times.";
		case 12:
			return "Complete 3 Slayer Tasks.";
		case 13:
			return "Kill 5 General Graardor's";
		case 14:
			return "Kill 5 Commander Zilyana's";
		case 15:
			return "Kill 5 K'ril Tsutsaroth";
		case 16:
			return "Kill 5 K'ree Arra's";
		case 22:
			return "Kill 5 Tormented Demons";
		case 23:
			return "Kill 5 Kalphite Queen";
		/**
		 * Combat related tasks (medium)
		 */
		case 17:
			return "Complete 5 Slayer Tasks.";
		case 18:
			return "Kill 10 General Graardor's";
		case 19:
			return "Kill 10 Commander Zilyana's";
		case 20:
			return "Kill 10 K'ril Tsutsaroth";
		case 21:
			return "Kill 10 K'ree Arra's";
		case 24:
			return "Kill 10 Tormented Demons";
		case 25:
			return "Kill 10 Kalphite Queen";
		/**
		 * Combat related tasks (Hard)
		 */
		case 26:
			return "Kill 5 of Nex.";
		case 27:
			return "Complete 10 Slayer Tasks.";
		case 28:
			return "Kill 15 General Graardor's";
		case 29:
			return "Kill 15 Commander Zilyana's";
		case 30:
			return "Kill 15 K'ril Tsutsaroth";
		case 31:
			return "Kill 15 K'ree Arra's";
		case 32:
			return "Kill 15 Tormented Demons";
		case 33:
			return "Kill 15 Corporeal Beast.";
		}
		return "Error code ##INVALID TASK SET## out of bounds exception caught " + i + ".";
	}

	public static void giveCombatReward(int task, Player p) {
		int ACHIEVEMENT_BOX = 21117;
		switch (task) {
		case 0: // Easy
			if (p.dailyTaskProgress >= 5 && !p.completedDailyTask) {
				resetTask(p);
				p.getBank(p.getCurrentBankTab()).add(18778, 1);
				p.getPacketSender().sendMessage("@dre@You have received @blu@1x Starved Effigy. ");
				addCoinsToPouch(p);
			}
			break;
		case 1: // Medium
			if (p.dailyTaskProgress >= 5 && !p.completedDailyTask) {
				resetTask(p);
				p.getBank(p.getCurrentBankTab()).add(18778, 1);
				p.getPacketSender()
						.sendMessage("@dre@You have received @blu@1xStarved Effigy. ");
				addCoinsToPouch(p);
			}
			break;
		case 2:
			if (p.dailyTaskProgress >= 10 && !p.completedDailyTask) {
				resetTask(p);
				p.getBank(p.getCurrentBankTab()).add(18778, 1);
				p.getBank(p.getCurrentBankTab()).add(ACHIEVEMENT_BOX, 1);
				p.getPacketSender().sendMessage(
						"@dre@You have received 1xStarved Effigy @dre@& @blu@ 1xAchievement Box.");
				addCoinsToPouch(p);
			}
			break;
		case 3:
			if (p.dailyTaskProgress >= 15 && !p.completedDailyTask) {
				resetTask(p);
				p.getBank(p.getCurrentBankTab()).add(18778, 2);
				p.getBank(p.getCurrentBankTab()).add(ACHIEVEMENT_BOX, 2);
				p.getPacketSender().sendMessage(
						"@dre@You have received @blu@2xStarved Effigy @dre@& @blu@ 2xAchievement Box.");
				addCoinsToPouch(p);
			}
			break;
		}
	}

	public static void addCoinsToPouch(Player player) {
		Locale locale = new Locale("en", "US");
		NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
		long rewardCoins = Misc.inclusiveRandom(1_000_000, 3_000_000);
		player.setMoneyInPouch((player.getMoneyInPouch() + (rewardCoins)));
		player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		player.getPacketSender().sendMessage("<col=ff0000>" + currencyFormatter.format(rewardCoins)
				+ "</col> coins have been added to your money pouch.");
	}

	public static int SKILL_EXP = 500_000;

	public static void handleTaskReward(Player p) {
		if (p.dailyTask == 0 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.FISHING, SKILL_EXP);
		}
		if (p.dailyTask == 1 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.WOODCUTTING, SKILL_EXP);
		}
		if (p.dailyTask == 2 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.THIEVING, SKILL_EXP);
		}
		if (p.dailyTask == 3 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.FLETCHING, SKILL_EXP / 3);
			p.getSkillManager().addExperience(Skill.WOODCUTTING, SKILL_EXP / 3);
			p.getSkillManager().addExperience(Skill.CRAFTING, SKILL_EXP / 3);
		}
		if (p.dailyTask == 4 && p.dailyTaskProgress >= 1000 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.FLETCHING, SKILL_EXP / 2);
			p.getSkillManager().addExperience(Skill.SMITHING, SKILL_EXP / 2);
		}
		if (p.dailyTask == 5 && p.dailyTaskProgress >= 1000 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.FLETCHING, SKILL_EXP / 2);
			p.getSkillManager().addExperience(Skill.CRAFTING, SKILL_EXP / 2);
		}
		if (p.dailyTask == 6 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.COOKING, SKILL_EXP);
		}
		if (p.dailyTask == 7 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.SUMMONING, SKILL_EXP);
		}
		if (p.dailyTask == 8 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.SUMMONING, SKILL_EXP / 2);
		}
		if (p.dailyTask == 9 && p.dailyTaskProgress >= 1 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.MINING, SKILL_EXP);
		}
		if (p.dailyTask == 10 && p.dailyTaskProgress >= 100 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.HERBLORE, SKILL_EXP);
		}
		if (p.dailyTask == 11 && p.dailyTaskProgress >= 200 && !p.completedDailyTask) {
			resetTask(p);
			addCoinsToPouch(p);
			p.getSkillManager().addExperience(Skill.MAGIC, SKILL_EXP);
		}
		if (p.dailyTask == 12 && p.dailyTaskProgress >= 3 && !p.completedDailyTask) {
			resetTask(p);
			p.getSlayer().giveReward(p, 50);
			p.getSkillManager().addExperience(Skill.SLAYER, SKILL_EXP / 2);
		}
		if (p.dailyTask == 17 && p.dailyTaskProgress >= 5 && !p.completedDailyTask) {
			resetTask(p);
			p.getSlayer().giveReward(p, 100);
			p.getSkillManager().addExperience(Skill.SLAYER, SKILL_EXP);
		}
		if (p.dailyTask == 27 && p.dailyTaskProgress >= 10 && !p.completedDailyTask) {
			resetTask(p);
			p.getSlayer().giveReward(p, 150);
			p.getSkillManager().addExperience(Skill.SLAYER, SKILL_EXP * 2);
		}
		if (p.dailyTask == 13 || p.dailyTask == 14 || p.dailyTask == 15 || p.dailyTask == 16 || p.dailyTask == 22
				|| p.dailyTask == 23 && p.dailyTaskProgress >= 5 && !p.completedDailyTask) {
			giveCombatReward(0, p);
		}
		if (p.dailyTask == 26 && p.dailyTaskProgress >= 5 && !p.completedDailyTask) {
			giveCombatReward(1, p);
		}
		if (p.dailyTask == 18 || p.dailyTask == 19 || p.dailyTask == 20 || p.dailyTask == 21 || p.dailyTask == 24
				|| p.dailyTask == 25 && p.dailyTaskProgress >= 10 && !p.completedDailyTask) {
			giveCombatReward(2, p);
		}
		if (p.dailyTask == 28 || p.dailyTask == 29 || p.dailyTask == 30 || p.dailyTask == 31 || p.dailyTask == 32
				|| p.dailyTask == 33 && p.dailyTaskProgress >= 15 && !p.completedDailyTask) {
			giveCombatReward(3, p);
		}
		p.getPacketSender().sendMessage("@blu@Congratulations! You have completed your Daily Task for the day.");
	}

	public static void checkTaskProgress(Player p) {
		if (p.dailyTask == 0 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 0 && p.dailyTaskProgress <= 99) {
			p.getPacketSender().sendMessage("@dre@You have caught @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@Fish out of your Daily Task.");
		}
		if (p.dailyTask == 1 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 1 && p.dailyTaskProgress <= 99) {
			p.getPacketSender().sendMessage("@dre@You have cut @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@Logs out of your Daily Task.");
		}
		if (p.dailyTask == 2 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 2 && p.dailyTaskProgress <= 99) {
			p.getPacketSender().sendMessage("@dre@You have stolen from @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@Stalls out of your Daily Task.");
		}
		if (p.dailyTask == 3 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 3 && p.dailyTaskProgress <= 99) {
			p.getPacketSender().sendMessage("@dre@You have fletched @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@Long/Short bows out of your Daily Task.");
		}
		if (p.dailyTask == 4 || p.dailyTask == 5 && p.dailyTaskProgress >= 1000) {
			handleTaskReward(p);
		} else if (p.dailyTask == 4 || p.dailyTask == 5 && p.dailyTaskProgress <= 999) {
			p.getPacketSender().sendMessage("@dre@You have made @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@1,000 @dre@Ammo out of your Daily Task.");
		}
		if (p.dailyTask == 6 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 6 && p.dailyTaskProgress <= 100) {
			p.getPacketSender().sendMessage(
					"@dre@You have cooked @blu@" + p.dailyTaskProgress + "@dre@/@blu@100 @dre@out of your Daily Task.");
		}
		if (p.dailyTask == 7 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 7 && p.dailyTaskProgress <= 100) {
			p.getPacketSender().sendMessage("@dre@You have infused @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@pouches out of your Daily Task.");
		}
		if (p.dailyTask == 8 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 8 && p.dailyTaskProgress <= 100) {
			p.getPacketSender().sendMessage("@dre@You have smelted @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@bars out of your Daily Task.");
		}
		if (p.dailyTask == 9 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 9 && p.dailyTaskProgress <= 100) {
			p.getPacketSender().sendMessage("@dre@You have mined @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@rocks out of your Daily Task.");
		}
		if (p.dailyTask == 10 && p.dailyTaskProgress >= 100) {
			handleTaskReward(p);
		} else if (p.dailyTask == 10 && p.dailyTaskProgress <= 100) {
			p.getPacketSender().sendMessage("@dre@You have mixed @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@100 @dre@potions out of your Daily Task.");
		}
		if (p.dailyTask == 11 && p.dailyTaskProgress >= 200) {
			handleTaskReward(p);
		} else if (p.dailyTask == 11 && p.dailyTaskProgress <= 200) {
			p.getPacketSender().sendMessage("@dre@You have casted alchemyt @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@200 @dre@times out of your Daily Task.");
		}
		if (p.dailyTask == 12 && p.dailyTaskProgress >= 3) {
			handleTaskReward(p);
		} else if (p.dailyTask == 12 && p.dailyTaskProgress <= 3) {
			p.getPacketSender().sendMessage("@dre@You have completed @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@3 @dre@Slayer Tasks out of your Daily Task.");
		}
		if (p.dailyTask == 13 || p.dailyTask == 14 || p.dailyTask == 15 || p.dailyTask == 16 || p.dailyTask == 22
				|| p.dailyTask == 23 && p.dailyTaskProgress >= 5) {
			handleTaskReward(p);
		} else if (p.dailyTask == 13 || p.dailyTask == 14 || p.dailyTask == 15 || p.dailyTask == 16 || p.dailyTask == 22
				|| p.dailyTask == 23 && p.dailyTaskProgress <= 5) {
			p.getPacketSender().sendMessage(
					"@dre@You have killed @blu@" + p.dailyTaskProgress + "@dre@/@blu@5 @dre@of your Daily Boss Task.");
		}
		if (p.dailyTask == 17 && p.dailyTaskProgress >= 5) {
			handleTaskReward(p);
		} else if (p.dailyTask == 17 && p.dailyTaskProgress <= 5) {
			p.getPacketSender().sendMessage("@dre@You have completed @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@5 @dre@Slayer Tasks out of your Daily Task.");
		}
		if (p.dailyTask == 18 || p.dailyTask == 19 || p.dailyTask == 21 || p.dailyTask == 24
				|| p.dailyTask == 25 && p.dailyTaskProgress >= 10) {
			handleTaskReward(p);
		} else if (p.dailyTask == 18 || p.dailyTask == 19 || p.dailyTask == 20 || p.dailyTask == 21 || p.dailyTask == 24
				|| p.dailyTask == 25 && p.dailyTaskProgress <= 10) {
			p.getPacketSender().sendMessage(
					"@dre@You have killed @blu@" + p.dailyTaskProgress + "@dre@/@blu@10 @dre@of your Daily Boss Task.");
		}
		if (p.dailyTask == 26 && p.dailyTaskProgress >= 5) {
			handleTaskReward(p);
		} else if (p.dailyTask == 26 && p.dailyTaskProgress <= 5) {
			p.getPacketSender().sendMessage("@dre@You have killed @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@5 @dre@Nex's of your Daily Boss Task.");
		}
		if (p.dailyTask == 27 && p.dailyTaskProgress >= 10) {
			handleTaskReward(p);
		} else if (p.dailyTask == 27 && p.dailyTaskProgress <= 10) {
			p.getPacketSender().sendMessage("@dre@You have completed @blu@" + p.dailyTaskProgress
					+ "@dre@/@blu@10 @dre@ Slayer Tasks of your Daily Task.");
		}
		if (p.dailyTask == 28 || p.dailyTask == 29 || p.dailyTask == 30 || p.dailyTask == 31 || p.dailyTask == 32
				|| p.dailyTask == 33 && p.dailyTaskProgress >= 15) {
			handleTaskReward(p);
		} else if (p.dailyTask == 28 || p.dailyTask == 29 || p.dailyTask == 30 || p.dailyTask == 31 || p.dailyTask == 32
				|| p.dailyTask == 33 && p.dailyTaskProgress <= 15) {
			p.getPacketSender().sendMessage(
					"@dre@You have killed @blu@" + p.dailyTaskProgress + "@dre@/@blu@15 @dre@of your Daily Boss Task.");
		}
	}

	public static void doTaskProgress(Player p) {
		p.dailyTaskProgress++;
		checkTaskProgress(p);
	}

	public static void giveNewTask(Player p) {
		if (p.dailyTaskDate == getTodayDate()) {
			if (!p.completedDailyTask) {
				p.getPacketSender().sendMessage("Your daily task is: " + dailyTask(p.dailyTask));
			} else {
				p.getPacketSender()
						.sendMessage("You already finished your daily task, Come back tomorrow for a new one.");
			}
		} else {
			p.dailyTaskDate = getTodayDate();
			p.dailyTask = Misc.inclusiveRandom(0, 33);
			p.getPacketSender().sendMessage("Your task for today is: " + dailyTask(p.dailyTask));
		}
	}
}
