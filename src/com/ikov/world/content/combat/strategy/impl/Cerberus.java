package com.ikov.world.content.combat.strategy.impl;

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.*;
import com.ikov.util.Misc;
import com.ikov.world.World;
import com.ikov.world.content.combat.CombatContainer;
import com.ikov.world.content.combat.CombatType;
import com.ikov.world.content.combat.strategy.CombatStrategy;
import com.ikov.world.entity.impl.Character;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class Cerberus implements CombatStrategy {
    Player player;

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
        NPC cerberus = (NPC) entity;;
        if (victim.getConstitution() <= 0 || victim.getConstitution() <= 0) {
            return true;
        }
        if (cerberus.getConstitution() <= 3000 && !cerberus.hasHealed()) {
            cerberus.performAnimation(anim1);
            cerberus.performGraphic(gfx1);
            cerberus.setConstitution(cerberus.getConstitution() + Misc.exclusiveRandom(1000, 3000));
            cerberus.setHealed(true);
        }
        if (cerberus.isChargingAttack()) {
            return true;
        }
        int random = Misc.getRandom(10);
        if (random <= 1 && Locations.goodDistance(cerberus.getPosition().getX(), cerberus.getPosition().getY(),
                victim.getPosition().getX(), victim.getPosition().getY(), 3)) {
            cerberus.performAnimation(anim2);
            cerberus.getCombatBuilder()
                    .setContainer(new CombatContainer(cerberus, victim, 1, 2, CombatType.MELEE, true));
        } else if (random >= 3 || !Locations.goodDistance(cerberus.getPosition().getX(),
                cerberus.getPosition().getY(), victim.getPosition().getX(), victim.getPosition().getY(), 14)) {
            cerberus.getCombatBuilder().setContainer(new CombatContainer(cerberus, victim, 1, 6, CombatType.MAGIC, true));
            cerberus.performAnimation(anim3);
            cerberus.performGraphic(gfx3);
            cerberus.setChargingAttack(true);
            TaskManager.submit(new Task(2, cerberus, false) {
                int tick = 0;

                @Override
                public void execute() {
                    switch (tick) {
                        case 1:
                            new Projectile(cerberus, victim, gfx5.getId(), 44, 3, 43, 31, 0).sendProjectile();
                            cerberus.setChargingAttack(false);
                            stop();
                            break;
                    }
                    tick++;
                }
            });
        } else {
            cerberus.getCombatBuilder()
                    .setContainer(new CombatContainer(cerberus, victim, 1, 5, CombatType.RANGED, true));
            cerberus.performAnimation(anim4);
            cerberus.performGraphic(gfx2);
            cerberus.setChargingAttack(true);
            TaskManager.submit(new Task(2, cerberus, false) {
                @Override
                public void execute() {
                    victim.performGraphic(gfx4);
                    cerberus.setChargingAttack(false);
                    stop();
                }
            });
        }
        return true;
    }


    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 10;
    }

    private static final Animation anim1 = new Animation(4494);
    private static final Animation anim2 = new Animation(4494);
    private static final Animation anim3 = new Animation(4489);
    private static final Animation anim4 = new Animation(4490);
    private static final Graphic gfx1 = new Graphic(444);
    private static final Graphic gfx2 = new Graphic(1246);
    private static final Graphic gfx3 = new Graphic(1246);
    private static final Graphic gfx4 = new Graphic(1247);
    private static final Graphic gfx5 = new Graphic(1247);

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
