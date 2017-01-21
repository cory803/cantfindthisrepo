package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.*;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.HitQueue;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

import java.util.List;

public class Phoenix implements CombatStrategy {

    public static NPC PHOENIX;

    public static void spawn(int id, Position pos) {
        PHOENIX = new NPC(id, pos);
        World.register(PHOENIX);
        PHOENIX.forceChat(RESPAWN_MESSAGES[(int) (Math.random() * RESPAWN_MESSAGES.length)]);
    }

    public static final String[] RESPAWN_MESSAGES = {
            "I am near immortal !",
            "Time to die",
            "Mwuahahaha!!!",
            "I was resurrected"
    };

    public static void resetOrIncrement() {
        if (World.PHOENIX_RESPAWN >= 5) {
            World.PHOENIX_RESPAWN = 0;
        } else {
            World.PHOENIX_RESPAWN++;
        }
    }

    public boolean summonMinions(List<Player> list) {
        boolean doubleSpawn = Misc.random(10) == 0;
        for (int i = 0; i < list.size(); i++) {
            if (doubleSpawn) {
//                System.out.println("Double Spawning minion");
                NPC npc_ = new NPC(6216, list.get(i).getPosition());
                npc_.getCombatBuilder().setAttackTimer(3);
                npc_.getCombatBuilder().attack(list.get(i).getCharacter());
                World.register(npc_);
            }
//            System.out.println("Spawning minion");
            NPC npc_ = new NPC(6216, list.get(i).getPosition());
            npc_.getCombatBuilder().setAttackTimer(3);
            npc_.getCombatBuilder().attack(list.get(i).getCharacter());
            World.register(npc_);
        }

        return false;
    }

    public static void death(final int id, final Position pos) {
        TaskManager.submit(new Task(40, World.PHOENIX_RESPAWN < 5 ? true : false) {
            @Override
            protected void execute() {
                resetOrIncrement();
                spawn(id, pos);
                stop();
            }
        });
    }

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC phoenix = (NPC) entity;
        if (victim.getConstitution() <= 0) {
            return true;
        }
        if (phoenix.isChargingAttack()) {
            return true;
        }

        final CombatType style = Misc.getRandom(3) == 0 ? CombatType.MAGIC : CombatType.RANGED;
        boolean spawnMinions = Misc.getRandom(10) == 0 ? true : false;
        phoenix.performAnimation(new Animation(phoenix.getDefinition().getAttackAnimation()));
        phoenix.setChargingAttack(true);
        Player target = (Player) victim;
        final List<Player> list = Misc.getCombinedPlayerList(target);
        TaskManager.submit(new Task(1, phoenix, false) {
            int tick = 0;

            @Override
            public void execute() {
                if (style == CombatType.MAGIC) {
                    if (tick == 1) {
                        for (Player near : list) {
                            if (near == null)
                                continue;
                            new Projectile(phoenix, near, 393, 44, 3, 43, 43, 0)
                                    .sendProjectile();
                            near.performGraphic(new Graphic(129));
                            if (spawnMinions) {
                                summonMinions(list);
                            }
                        }
                    } else if (tick == 2) {
                        for (Player near : list) {
                            if (near == null)
                                continue;
                            //System.out.println("Near player: " + near.getUsername());

                            new HitQueue.CombatHit(phoenix.getCombatBuilder(),
                                    new CombatContainer(phoenix, near, 1, style, true)).handleAttack();
                        }
                        phoenix.setChargingAttack(false);
                        stop();
                    }
                } else if (style == CombatType.RANGED) {
                    if (tick == 1) {
                            new Projectile(phoenix, victim, 1099, 44, 5, 33, 33, 0).sendProjectile();
                        if (spawnMinions) {
                            summonMinions(list);
                        }
                    } else if (tick == 2) {
                            new HitQueue.CombatHit(phoenix.getCombatBuilder(),
                                    new CombatContainer(phoenix, victim, 1, style, true)).handleAttack();
                        phoenix.setChargingAttack(false);
                        stop();
                    }
                }
                tick++;
            }
        });
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 4;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }
}
