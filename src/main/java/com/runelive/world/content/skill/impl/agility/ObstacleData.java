package com.runelive.world.content.skill.impl.agility;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.*;
import com.runelive.model.container.impl.Equipment;
import com.runelive.model.definitions.WeaponAnimations;
import com.runelive.util.Misc;
import com.runelive.world.content.Achievements;
import com.runelive.world.entity.impl.player.Player;

/**
 * Messy as fuck, what ever
 * 
 * @author Gabriel Hannason
 */
public enum ObstacleData {

	/* GNOME COURSE */
	LOG(2295, true) {
		@Override
		public void cross(final Player player) {
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.moveTo(new Position(2474, 3436));
			player.getPacketSender().sendMessage("You attempt to walk across the log..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 7;

				@Override
				public void execute() {
					tick--;
					player.getWalkingQueue().walkStep(0, -1);
					if (tick <= 0)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 7.5);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to safely make your way across the log.");
				}
			});
		}
	},
	UNDERWALL_TUNNEL(9311, true) {
		@Override
		public void cross(final Player player) {
			player.setPositionToFace(new Position(3139, 3516));
			player.performAnimation(new Animation(2589));
			player.getCharacterAnimations().setStandingAnimation(2590);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You climb under the wall...");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 6;

				@Override
				public void execute() {
					if(tick == 2) {
						player.moveTo(new Position(3144, 3514, 0));
					} else if(tick == 1) {
						player.performAnimation(new Animation(2591));
						WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
						player.getUpdateFlag().flag(Flag.APPEARANCE);
					}
					if (tick <= 0)
						stop();

					tick--;
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 4);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to climb under the wall.");
				}
			});
		}
	},
	UNDERWALL_TUNNEL2(9312, true) {
		@Override
		public void cross(final Player player) {
			player.setPositionToFace(new Position(3143, 3514));
			player.performAnimation(new Animation(2589));
			player.getCharacterAnimations().setStandingAnimation(2590);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You climb under the wall...");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 6;

				@Override
				public void execute() {
					if(tick == 2) {
						player.moveTo(new Position(3138, 3516, 0));
					} else if(tick == 1) {
						player.performAnimation(new Animation(2591));
						WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
						player.getUpdateFlag().flag(Flag.APPEARANCE);
					}
					if (tick <= 0)
						stop();

					tick--;
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 4);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to climb under the wall.");
				}
			});
		}
	},
	NET(2285, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the net..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 2) {
						player.moveTo(new Position(2473, 3423, 1));
						Agility.addExperience(player, 7.5);
					} else if (tick == 3) {
						player.setCrossedObstacle(1, true).setCrossingObstacle(false);
						stop();
					}
					tick++;
				}
			});
		}
	},
	BRANCH(2313, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the branch..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2473, 3420, 2));
					Agility.addExperience(player, 5);
					player.setCrossedObstacle(2, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	ROPE(2312, true) {
		@Override
		public void cross(final Player player) {
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You attempt to walk across the rope..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(1, 0);
					if (tick >= 6)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(3, true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 7.5);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to safely walk across the rope.");
				}
			});
		}
	},
	BRANCH_2(2314, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the branch..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
					Agility.addExperience(player, 5);
					player.setCrossedObstacle(4, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	NETS_2(2286, false) {
		@Override
		public void cross(final Player player) {
			if (player.getPosition().getY() != 3425) {
				player.setCrossingObstacle(false);
				return;
			}
			player.getPacketSender().sendMessage("You climb the net..");
			player.performAnimation(new Animation(828));
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() + 2, 0));
					Agility.addExperience(player, 7.5);
					player.setCrossedObstacle(5, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	PIT(6632, true) {
		@Override
		public void cross(final Player player) {
			final boolean north = player.getPosition().getY() >= 9197;
			if(north) {
				player.moveTo(new Position(player.getPosition().getX(), 9197, player.getDungeoneering().getZ()));
			} else {
				player.moveTo(new Position(player.getPosition().getX(), 9193, player.getDungeoneering().getZ()));
			}
			player.getPacketSender().sendMessage("You jump across the pit...");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 3 || tick >= 4) {
						if (player.getSkillAnimation() != 6132) {
							player.setSkillAnimation(6132);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					} else {
						if (player.getSkillAnimation() != -1) {
							player.setSkillAnimation(-1);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					}

					tick++;
					if(north) {
						player.getWalkingQueue().walkStep(0, -1);
					} else {
						player.getWalkingQueue().walkStep(0, 1);
					}
					if (tick >= 4)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					if(!north) {
						player.moveTo(new Position(player.getPosition().getX(), 9197, player.getDungeoneering().getZ()));
					} else {
						player.moveTo(new Position(player.getPosition().getX(), 9193, player.getDungeoneering().getZ()));
					}
					player.setCrossedObstacle(6, true).setCrossingObstacle(false).setSkillAnimation(-1);
					player.getClickDelay().reset();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Agility.addExperience(player, 7.5);
					Agility.resetProgress(player);
				}
			});
		}
	},
	PIPE_1(4058, true) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(2487, 3430));
			player.getPacketSender().sendMessage("You attempt to go through the pipe..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 3 || tick >= 4) {
						if (player.getSkillAnimation() != 844) {
							player.setSkillAnimation(844);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					} else {
						if (player.getSkillAnimation() != -1) {
							player.setSkillAnimation(-1);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					}

					tick++;
					player.getWalkingQueue().walkStep(0, 1);
					if (tick >= 4)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.moveTo(new Position(2487, 3437));
					player.setCrossedObstacle(6, true).setCrossingObstacle(false).setSkillAnimation(-1);
					player.getClickDelay().reset();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					if (Agility.passedAllObstacles(player)) {
						int marksOfGrace = 1;
						if(player.getDonatorRights().isDonator()) {
							marksOfGrace *= 2;
						}
						player.getInventory().add(11849, marksOfGrace);

						Agility.addExperience(player, 39);
						Achievements.finishAchievement(player, Achievements.AchievementData.GNOME_COURSE);
					} else {
						Agility.addExperience(player, 7.5);
					}
					Agility.resetProgress(player);
					player.getPacketSender().sendMessage("You manage to make your way through the pipe.");
				}
			});
		}
	},
	PIPE_2(154, true) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(2484, 3430));
			player.getPacketSender().sendMessage("You attempt to go through the pipe..");
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 3 || tick >= 4) {
						if (player.getSkillAnimation() != 844) {
							player.setSkillAnimation(844);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					} else {
						if (player.getSkillAnimation() != -1) {
							player.setSkillAnimation(-1);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
						}
					}

					tick++;
					player.getWalkingQueue().walkStep(0, 1);
					if (tick >= 4)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.moveTo(new Position(2483, 3437));
					player.setCrossedObstacle(6, true).setCrossingObstacle(false).setSkillAnimation(-1);
					player.getClickDelay().reset();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					if (Agility.passedAllObstacles(player)) {
						int marksOfGrace = 1;
						if(player.getDonatorRights().isDonator()) {
							marksOfGrace *= 2;
						}
						player.getInventory().add(11849, marksOfGrace);
						Agility.addExperience(player, 39);
						Achievements.finishAchievement(player, Achievements.AchievementData.GNOME_COURSE);
					} else {
						Agility.addExperience(player, 7.5);
					}
					player.getPacketSender().sendMessage("You manage to make your way through the pipe.");
					Agility.resetProgress(player);
				}
			});
		}

	},

	/* BARBARIAN OUTPOST COURSE */
	ROPE_SWING(2282, true) {
		@Override
		public void cross(final Player player) {
			Agility.resetProgress(player);
			player.getPacketSender().sendMessage("You attempt to swing on the ropeswing..");
			player.moveTo(new Position(2551, 3554));
			player.performAnimation(new Animation(751));
			final boolean success = Agility.isSucessive(player);
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					if (tick == 1)
						player.moveTo(new Position(player.getPosition().getX(), 3553, 0));
					if (!success) {
						player.moveTo(new Position(2550, 9950, 0));
						player.dealDamage(null, new Hit(Misc.getRandom(50), Hitmask.RED, CombatIcon.NONE));
						player.getPacketSender().sendMessage("You failed to swing your way across.");
						stop();
						return;
					}
					if (tick >= 3) {
						player.moveTo(new Position(player.getPosition().getX(), 3549, 0));
						Agility.addExperience(player, 22 );
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, success ? true : false).setCrossingObstacle(false);
					player.getPacketSender().sendMessage("You manage to swing yourself across.");
				}
			});
		}
	},
	LOG_2(2294, true) {
		@Override
		public void cross(final Player player) {
			final boolean fail = !Agility.isSucessive(player);
			player.getPacketSender().sendMessage("You attempt to walk-over the log..");
			player.setSkillAnimation(762);
			player.moveTo(new Position(2550, 3546, 0));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(-1, 0);
					if (tick >= 9 || player == null)
						stop();
					if (tick == 5 && fail) {
						stop();
						tick = 0;
						player.getWalkingQueue().clear();
						player.performAnimation(new Animation(764));
						TaskManager.submit(new Task(1, player, true) {
							int tick2 = 0;

							@Override
							public void execute() {
								if (tick2 == 0) {
									player.moveTo(new Position(2546, 3547));
									player.dealDamage(null, new Hit(Misc.getRandom(50), Hitmask.RED, CombatIcon.NONE));
								}
								tick2++;
								player.setSkillAnimation(772);
								player.getUpdateFlag().flag(Flag.APPEARANCE);
								player.getWalkingQueue().walkStep(0, 1);
								if (tick2 >= 4) {
									player.getPacketSender()
											.sendMessage("You are unable to make your way across the log.");
									player.setCrossedObstacle(1, false).setCrossingObstacle(false)
											.setSkillAnimation(-1);
									Agility.addExperience(player, 13.7);
									player.getUpdateFlag().flag(Flag.APPEARANCE);
									stop();
									return;
								}
							}
						});
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					if (!fail) {
						player.setCrossedObstacle(1, true).setCrossingObstacle(false).setSkillAnimation(-1);
						Agility.addExperience(player, fail ? 0  : 5 );
						player.getUpdateFlag().flag(Flag.APPEARANCE);
						player.moveTo(new Position(2541, 3546));
						player.getPacketSender().sendMessage("You safely make your way across the log.");
					}
				}
			});
		}
	},
	NET_3(2284, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You climb the net..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2537 + Misc.getRandom(1), 3546 + Misc.getRandom(1), 1));
					Agility.addExperience(player, 8.2 );
					player.setCrossedObstacle(2, true).setSkillAnimation(-1).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	BALANCE_LEDGE(2302, true) {
		@Override
		public void cross(final Player player) {
			if (player.getPosition().getX() != 2536) {
				player.setCrossingObstacle(false);
				return;
			}
			player.getPacketSender().sendMessage("You attempt to make your way across the ledge..");

			final boolean fallDown = !Agility.isSucessive(player);
			player.setCrossingObstacle(true);
			player.moveTo(new Position(2536, 3547, 1));
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.setSkillAnimation(756);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getWalkingQueue().walkStep(-1, 0);
					if (tick == 3 && fallDown) {
						player.performAnimation(new Animation(761));
						stop();
						TaskManager.submit(new Task(1) {
							@Override
							public void execute() {
								player.moveTo(new Position(2535, 3546, 0));
								player.dealDamage(null, new Hit(Misc.getRandom(50), Hitmask.RED, CombatIcon.NONE));
								player.getWalkingQueue().walkStep(0, -1);
								player.setCrossedObstacle(3, false).setSkillAnimation(-1);
								player.getUpdateFlag().flag(Flag.APPEARANCE);
								player.getPacketSender().sendMessage("You accidently slip and fall down!");
								TaskManager.submit(new Task(1) {
									@Override
									public void execute() {
										player.setCrossingObstacle(false);
										stop();
									}
								});
								stop();
							}
						});
					} else if (tick == 4) {
						player.setCrossedObstacle(3, true).setSkillAnimation(-1).setCrossingObstacle(false);
						player.getUpdateFlag().flag(Flag.APPEARANCE);
						Agility.addExperience(player, 22 );
						player.getPacketSender().sendMessage("You safely move across the ledge.");
						stop();
					}
				}
			});
		}
	},
	LADDER(3205, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(827));
			player.getPacketSender().sendMessage("You climb down the ladder...");
			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {
					player.moveTo(new Position(2532, 3546, 0));
					player.setCrossedObstacle(4, true).setCrossingObstacle(false);
					stop();
				}
			});
		}
	},
	RAMP(1948, false) {
		@Override
		public void cross(final Player player) {
			if (player.getPosition().getX() != 2535 && player.getPosition().getX() != 2538
					&& player.getPosition().getX() != 2542 && player.getPosition().getX() != 2541) {
				player.getPacketSender().sendMessage("You cannot jump over the wall from this side!");
				player.setCrossingObstacle(false);
				return;
			}
			final boolean first = player.getPosition().getX() == 2535;
			final boolean oneStep = player.getPosition().getX() == 2537 || player.getPosition().getX() == 2542;
			player.setPositionToFace(player.getInteractingObject().getPosition().copy());
			player.getPacketSender().sendMessage("You attempt to jump over the wall...");
			player.performAnimation(new Animation(1115));
			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {
					player.getPacketSender().sendClientRightClickRemoval();
					player.moveTo(new Position(player.getPosition().getX() + (oneStep ? 1 : 2), 3553));
					player.setCrossingObstacle(false).setCrossedObstacle(first ? 5 : 6, true);
					if (player.getPosition().getX() == 2543 && player.getPosition().getY() == 3553) {
						if (Agility.passedAllObstacles(player)) {
							int marksOfGrace = 2;
							if(player.getDonatorRights().isDonator()) {
								marksOfGrace *= 2;
							}
							player.getInventory().add(11849, marksOfGrace);
							Achievements.finishAchievement(player, Achievements.AchievementData.BARB_AGILITY);
							Agility.addExperience(player, 46.2);
							Agility.resetProgress(player);
						} else {
							Agility.addExperience(player, 13.7);
						}
						player.getPacketSender().sendMessage("You manage to jump over the wall.");
					}
					stop();
				}
			});
		}
	},
	ROPESWING_LADDER(1759, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(827));
			player.getPacketSender().sendMessage("You climb the ladder..");
			TaskManager.submit(new Task(1) {
				@Override
				public void execute() {
					player.setCrossingObstacle(false);
					player.moveTo(new Position(player.getPosition().getX() > 2610 ? 2209 : 2546,
							player.getPosition().getY() < 3550 ? 5348 : 9951, 0));
					stop();
				}
			});
		}
	},

	/* WILD COURSE */

	GATE_1(2309, true) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(2998, 3917));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You enter the gate and begin walking across the narrow path..");
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(0, 1);
					if (player.getPosition().getY() == 3930 || tick >= 15) {
						player.moveTo(new Position(2998, 3931, 0));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 15);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Agility.resetProgress(player);
					player.getPacketSender().sendMessage("You manage to make your way to the other side.");
				}
			});
		}
	},
	GATE_2(2308, true) {
		@Override
		public void cross(final Player player) {
			player.getPacketSender().sendMessage("You enter the gate and begin walking across the narrow path..");
			player.moveTo(new Position(2998, 3930));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(0, -1);
					if (player.getPosition().getY() == 3917 || tick >= 15) {
						player.moveTo(new Position(2998, 3916));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 15);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Agility.resetProgress(player);
					player.getPacketSender().sendMessage("You manage to make your way to the other side.");
				}
			});
		}
	},
	GATE_3(2308, true) {
		@Override
		public void cross(final Player player) {
			player.getPacketSender().sendMessage("You enter the gate and begin walking across the narrow path..");
			player.moveTo(new Position(2998, 3930));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(0, -1);
					if (player.getPosition().getY() == 3917 || tick >= 15) {
						player.moveTo(new Position(2998, 3916));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 15);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Agility.resetProgress(player);
					player.getPacketSender().sendMessage("You manage to make your way to the other side.");
				}
			});
		}
	},
	PIPE_3(2288, true) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(3004, 3937));
			player.setSkillAnimation(844);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You attempt to squeeze through the pipe..");
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(0, 1);
					if (tick == 4)
						player.moveTo(new Position(3004, 3947));
					else if (tick == 7)
						stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(0, true).setCrossedObstacle(1, true).setCrossedObstacle(2, true)
							.setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, 12.5);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to squeeze through the pipe.");
				}
			});
		}
	},
	ROPE_SWING_2(2283, true) {
		@Override
		public void cross(final Player player) {
			if (player.getPosition().getY() > 3953) {
				player.getPacketSender().sendMessage("You must be positioned infront of the Ropeswing to do that.");
				player.setCrossingObstacle(false);
				return;
			}
			player.getPacketSender().sendMessage("You attempt to swing on the ropeswing..");
			player.moveTo(new Position(3005, 3953));
			player.performAnimation(new Animation(751));
			player.setPositionToFace(new Position(3005, 3960, 0));
			final boolean fail = !Agility.isSucessive(player);
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					if (tick == 2) {
						if (fail) {
							player.moveTo(new Position(3004, 10356));
							player.dealDamage(null, new Hit(Misc.getRandom(60), Hitmask.RED, CombatIcon.NONE));
							player.getPacketSender().sendMessage("You failed to swing your way across.");
							stop();
							return;
						} else {
							Agility.addExperience(player, 20);
							player.setPositionToFace(new Position(3005, 3960, 0));
							player.moveTo(new Position(player.getPosition().getX(), 3954, 0));
						}
					}
					if (tick >= 3) {
						player.moveTo(new Position(player.getPosition().getX(), 3958, 0));
						player.setPositionToFace(new Position(3005, 3960, 0));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.getPacketSender().sendMessage("You manage to swing yourself across.");
					player.setCrossedObstacle(3, fail ? false : true).setCrossingObstacle(false);
				}
			});
		}
	},
	STEPPING_STONES(9326, true) {
		@Override
		public void cross(final Player player) {
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You attempt to pass the stones..");

			TaskManager.submit(new Task(1, player, true) {
				int tick = 1;

				@Override
				public void execute() {
					tick++;
					player.performAnimation(new Animation(769));
					if (tick == 4 || tick == 7 || tick == 10 || tick == 13 || tick == 16) {
						player.moveTo(new Position(player.getPosition().getX() - 1, player.getPosition().getY()));
					} else if (tick >= 17) {
						player.moveTo(new Position(2996, 3960, 0));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(4, true).setCrossingObstacle(false);
					Agility.addExperience(player, 20);
					player.getPacketSender().sendMessage("You manage to pass the stones.");
				}
			});
		}
	},
	BALANCE_LEDGE_2(2297, true) {
		@Override
		public void cross(final Player player) {
			player.moveTo(new Position(3001, 3945, 0));
			player.setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			final boolean fail = !Agility.isSucessive(player);
			player.getPacketSender().sendMessage("You attempt to make your way over the log..");
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					player.getWalkingQueue().walkStep(-1, 0);
					if (tick >= 7)
						stop();
					else if (fail && tick >= 3) {
						player.moveTo(new Position(3000, 10346));
						player.dealDamage(null, new Hit(Misc.getRandom(60), Hitmask.RED, CombatIcon.NONE));
						stop();
					}
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(5, fail ? false : true).setCrossingObstacle(false).setSkillAnimation(-1);
					Agility.addExperience(player, fail ? 0 : 20);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.getPacketSender().sendMessage("You manage to safely make your way over the log.");
				}
			});
		}
	},
	CLIMB_WALL(2994, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(828));
			player.getPacketSender().sendMessage("You attempt to climb up the wall..");
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.getPacketSender().sendClientRightClickRemoval();
					player.moveTo(new Position(2996, 3933, 0));
					stop();
				}

				@Override
				public void stop() {
					setEventRunning(false);
					player.setCrossedObstacle(6, true).setCrossingObstacle(false);
					if (Agility.passedAllObstacles(player)) {
						int marksOfGrace = 3;
						if(player.getDonatorRights().isDonator()) {
							marksOfGrace *= 2;
						}
						player.getInventory().add(11849, marksOfGrace);
						Agility.addExperience(player, 490);
					} else {
						Agility.addExperience(player, 13.7);
					}
					player.getPacketSender().sendMessage("You manage to climb up the wall.");
					Agility.resetProgress(player);
				}
			});
		}
	},
	LADDER_2(14758, false) {
		@Override
		public void cross(final Player player) {
			player.performAnimation(new Animation(827));
			player.setEntityInteraction(null);
			player.setCrossingObstacle(false);
			TaskManager.submit(new Task(1) {
				@Override
				public void execute() {
					player.moveTo(new Position(3005, 10362, 0));
					stop();
				}
			});
		}
	},

	/** MISC **/
	RED_DRAGON_LOG_BALANCE(5088, false) {
		@Override
		public void cross(final Player player) {
			player.setCrossingObstacle(true).setSkillAnimation(762);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			final int moveX = player.getPosition().getX() > 2683 ? 2686 : 2683;
			player.moveTo(new Position(moveX, 9506));
			TaskManager.submit(new Task(1, player, true) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick < 4)
						player.getWalkingQueue().walkStep(moveX == 2683 ? +1 : -1, 0);
					else if (tick == 4) {
						player.setSkillAnimation(-1).setCrossingObstacle(false);
						player.getUpdateFlag().flag(Flag.APPEARANCE);
						Agility.addExperience(player, 4);
						stop();
					}
					tick++;
				}
			});
		}
	},;

	ObstacleData(int object, boolean mustWalk) {
		this.object = object;
		this.mustWalk = mustWalk;
	}

	private int object;
	private boolean mustWalk;

	public int getObject() {
		return object;
	}

	public boolean mustWalk() {
		return mustWalk;
	}

	public void cross(final Player player) {

	}

	public static ObstacleData forId(int object) {
		if (object == 2993 || object == 2328 || object == 2994)
			return CLIMB_WALL;
		else if (object == 2307)
			return GATE_2;
		else if (object == 5088 || object == 5090)
			return RED_DRAGON_LOG_BALANCE;
		for (ObstacleData obstacleData : ObstacleData.values()) {
			if (obstacleData.getObject() == object)
				return obstacleData;
		}
		return null;
	}
}
