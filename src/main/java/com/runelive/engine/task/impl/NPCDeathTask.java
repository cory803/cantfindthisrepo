package com.runelive.engine.task.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.DamageDealer;
import com.runelive.model.Locations.Location;
import com.runelive.model.npc.drops.LootSystem;
import com.runelive.world.World;
import com.runelive.world.content.Achievements;
import com.runelive.world.content.Achievements.AchievementData;
import com.runelive.world.content.KillsTracker;
import com.runelive.world.content.KillsTracker.KillsEntry;
import com.runelive.world.content.PlayerPanel;
import com.runelive.world.content.combat.strategy.impl.KalphiteQueen;
import com.runelive.world.content.combat.strategy.impl.Nex;
import com.runelive.world.content.combat.strategy.impl.Phoenix;
import com.runelive.world.content.minigames.impl.WarriorsGuild;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 * 
 * @author relex lawl
 */

public class NPCDeathTask extends Task {

	/**
	 * The NPCDeathTask constructor.
	 * 
	 * @param npc
	 *            The npc being killed.
	 */
	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;
	private Player npc_killer = null;

	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute() {
		try {
			npc.setEntityInteraction(null);
			switch (ticks) {
			case 2:
				npc.getWalkingQueue().setLockMovement(true).clear();
				DamageDealer damageDealer = npc.getCombatBuilder().getTopDamageDealer(true, null);
				killer = damageDealer == null ? null : damageDealer.getPlayer();

				if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
					npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));

				/** CUSTOM NPC DEATHS **/
				if (npc.getId() == 13447) {
					Nex.handleDeath();
				}
				if (npc.getId() == 1172) {
					if (killer.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 8) {
						killer.getMinigameAttributes().getClawQuestAttributes().setQuestParts(9);
						killer.getPacketSender()
								.sendMessage("The Shaikahan dropped a certificate you need to show the king.");
					}
				}
				break;
			case 0:
				if (killer != null) {

					boolean boss = (npc.getDefaultConstitution() > 2500);
					if (!Nex.nexMinion(npc.getId()) && npc.getId() != 1158
							&& !(npc.getId() >= 3493 && npc.getId() <= 3497)) {
						KillsTracker.submit(killer, new KillsEntry(npc.getDefinition().getName(), 1, boss));
						if (boss) {
							if (!npc.getDefinition().getName().contains("Reve")
									&& !npc.getDefinition().getName().contains("Zulrah")) {
								killer.addBossPoints(1);
								PlayerPanel.refreshPanel(killer);
								//killer.getPacketSender().sendMessage("You have defeated @blu@" + npc.getDefinition().getName() + "@bla@. You now have@red@ " + killer.getBossPoints() + " @bla@boss points.");
							}
							Achievements.doProgress(killer, AchievementData.DEFEAT_500_BOSSES);
						}
					}
					Achievements.doProgress(killer, AchievementData.DEFEAT_10000_MONSTERS);
					if (npc.getId() == 50) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_KING_BLACK_DRAGON);
					} else if (npc.getId() == 2054) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CHAOS_ELEMENTAL);
					} else if (npc.getId() == 8349) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_A_TORMENTED_DEMON);
					} else if (npc.getId() == 3491) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CULINAROMANCER);
					} else if (npc.getId() == 2001) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_SCORPIA);
					} else if (npc.getId() == 2745) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_JAD);
					} else if (npc.getId() == 4540) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_BANDOS_AVATAR);
					} else if (npc.getId() == 6260) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_GENERAL_GRAARDOR);
						killer.getAchievementAttributes().setGodKilled(0, true);
					} else if (npc.getId() == 6222) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_KREE_ARRA);
						killer.getAchievementAttributes().setGodKilled(1, true);
					} else if(npc.getId() == 4278 || npc.getId() == 4279 || npc.getId() == 4280 || npc.getId() == 4281
							|| npc.getId() == 4282 || npc.getId() == 4283 || npc.getId() == 4284 || npc.getId() == 4291) {
						WarriorsGuild.handleDrop(killer, npc);
						killer.getMinigameAttributes().getWarriorsGuildAttributes().setSpawnedArmour(false);
					} else if (npc.getId() == 6247) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_COMMANDER_ZILYANA);
						killer.getAchievementAttributes().setGodKilled(2, true);
					} else if (npc.getId() == 6203) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_KRIL_TSUTSAROTH);
						killer.getAchievementAttributes().setGodKilled(3, true);
					} else if (npc.getId() == 8133) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CORPOREAL_BEAST);
					} else if (npc.getId() == 13447) {
						Achievements.finishAchievement(killer, AchievementData.DEFEAT_NEX);
						killer.getAchievementAttributes().setGodKilled(4, true);
					} else if (npc.getId() == 5529) {
						Achievements.finishAchievement(killer, AchievementData.KILL_YAK);
					} else if (npc.getId() == 1265) {
						Achievements.finishAchievement(killer, AchievementData.KILL_ROCKCRAB);
					} else if (npc.getId() == 90) {
						Achievements.finishAchievement(killer, AchievementData.KILL_SKELETON);
					}
					if(npc.getId() == 502) {
						killer.resetKraken();
					}
					/** ACHIEVEMENTS **/
					switch (killer.getLastCombatType()) {
					case MAGIC:
						Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MAGIC);
						break;
					case MELEE:
						Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MELEE);
						break;
					case RANGED:
						Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_RANGED);
						break;
					}

					/** LOCATION KILLS **/
					if (npc.getLocation().handleKilledNPC(killer, npc)) {
						stop();
						return;
					}

					/** PARSE DROPS **/
					LootSystem.drop(killer, npc);

					killer.getSlayer().killedNpc(npc.getId());
				}
				stop();
				break;
			}
			ticks--;
		} catch (Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	@Override
	public void stop() {
		setEventRunning(false);

		npc.setDying(false);

		// respawn
		if (npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.BOSS_SYSTEM
				&& npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.WILDKEY_ZONE
				&& npc.getLocation() != Location.SHREKS_HUT && npc.getLocation() != Location.PHOENIX
				&& !npc.isDungeoneeringNpc()) {
			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime()));
		}


		if (npc.getId() == 8549 && World.PHOENIX_RESPAWN <= 5) {
			Phoenix.death(npc.getId(), npc.getPosition());
//			System.out.println("Respawn # " + World.PHOENIX_RESPAWN);
		} else if (npc.getId() == 6217 && npc.getLocation() == Location.PHOENIX) {

		} else {
			TaskManager.submit(new NPCRespawnTask(npc, 25));
		}

		//Respawn monsters in Monster Dzone that have no respawn time
		if (npc.getDefinition().getRespawnTime() == -1 && npc.getLocation() == Location.MONSTER_DONOR) {
			TaskManager.submit(new NPCRespawnTask(npc, 20));
		}

		World.deregister(npc);

		if (npc.getId() == 1158 || npc.getId() == 1160) {
			KalphiteQueen.death(npc.getId(), npc.getPosition());
		}

		if (Nex.nexMob(npc.getId())) {
			Nex.death(npc.getId());
		}
	}

	public Player getNpc_killer() {
		return npc_killer;
	}

	public void setNpc_killer(Player npc_killer) {
		this.npc_killer = npc_killer;
	}
}
