package com.runelive.world.content.combat.strategy.impl;

import com.runelive.model.Animation;
import com.runelive.model.Locations.Location;
import com.runelive.model.Projectile;
import com.runelive.util.Misc;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;

public class ZamorakianMage implements CombatStrategy {

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
        NPC zmage = (NPC) entity;
        if (zmage.getConstitution() <= 0 || victim.getConstitution() <= 0) {
            return true;
        }
        zmage.performAnimation(new Animation(zmage.getDefinition().getAttackAnimation()));
        boolean mage = Misc.getRandom(10) <= 7;
        new Projectile(zmage, victim, 2729, 44, 3, 43, 43, 2737).sendProjectile();
        zmage.getCombatBuilder().setContainer(new CombatContainer(zmage, victim, 1, mage ? 3 : 2,
                mage ? CombatType.MAGIC : CombatType.MAGIC, true));
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return entity.getLocation() == Location.DUNGEONEERING ? 6 : 50;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
