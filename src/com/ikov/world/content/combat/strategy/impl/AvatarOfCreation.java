package com.ikov.world.content.combat.strategy.impl;

/**
 * Created by Ikov on 05/04/2016.
 */

import com.ikov.engine.task.Task;
import com.ikov.engine.task.TaskManager;
import com.ikov.model.*;
import com.ikov.util.Misc;
import com.ikov.world.content.combat.CombatContainer;
import com.ikov.world.content.combat.CombatType;
import com.ikov.world.content.combat.HitQueue;
import com.ikov.world.content.combat.strategy.CombatStrategy;
import com.ikov.world.entity.impl.Character;
import com.ikov.world.entity.impl.npc.NPC;
import com.ikov.world.entity.impl.player.Player;

public class AvatarOfCreation implements CombatStrategy {

    private static final Animation attack_anim = new Animation(11202);
    private static final Graphic graphic1 = new Graphic(433, GraphicHeight.MIDDLE);

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
        NPC creation = (NPC)entity;
        if(creation.isChargingAttack() || creation.getConstitution() <= 0) {
            return true;
        }
        CombatType style = Misc.getRandom(4) <= 1 && Locations.goodDistance(creation.getPosition(), victim.getPosition(), 1) ? CombatType.MELEE : CombatType.RANGED;
        if(style == CombatType.MELEE) {
            creation.performAnimation(new Animation(creation.getDefinition().getAttackAnimation()));
            creation.getCombatBuilder().setContainer(new CombatContainer(creation, victim, 1, 1, CombatType.MELEE, true));
        } else {
            creation.performAnimation(attack_anim);
            creation.setChargingAttack(true);
            Player target = (Player)victim;
            for (Player t : Misc.getCombinedPlayerList(target)) {
                if(t == null || t.isTeleporting())
                    continue;
                if(t.getPosition().distanceToPoint(creation.getPosition().getX(), creation.getPosition().getY()) > 20)
                    continue;
                new Projectile(creation, target, graphic1.getId(), 44, 3, 43, 43, 0).sendProjectile();
            }
            TaskManager.submit(new Task(2, target, false) {
                @Override
                public void execute() {
                    for (Player t : Misc.getCombinedPlayerList(target)) {
                        if(t == null)
                            continue;
                        creation.getCombatBuilder().setVictim(t);
                        new HitQueue.CombatHit(creation.getCombatBuilder(), new CombatContainer(creation, t, 1, CombatType.RANGED, true)).handleAttack();
                    }
                    creation.setChargingAttack(false);
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
        return 3;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
