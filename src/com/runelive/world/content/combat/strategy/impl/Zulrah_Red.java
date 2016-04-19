package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.Projectile;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;

public class Zulrah_Red implements CombatStrategy {

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
    NPC zulrah = (NPC) entity;
    if (victim.getConstitution() <= 0 || victim.getConstitution() <= 0) {
      return true;
    }
    zulrah.performAnimation(anim1);
    zulrah.performGraphic(gfx3);
    zulrah.setChargingAttack(true);
    TaskManager.submit(new Task(2, zulrah, false) {
      int tick = 0;

      @Override
      public void execute() {
        switch (tick) {
          case 1:
            new Projectile(zulrah, victim, gfx5.getId(), 44, 3, 43, 31, 0).sendProjectile();
            zulrah.setChargingAttack(false);
            stop();
            break;
        }
        tick++;
      }
    });
    return false;
  }


  @Override
  public int attackDelay(Character entity) {
    return entity.getAttackSpeed();
  }

  @Override
  public int attackDistance(Character entity) {
    return 10;
  }

  private static final Animation anim1 = new Animation(9254);
  private static final Animation anim2 = new Animation(9277);
  private static final Animation anim3 = new Animation(9300);
  private static final Animation anim4 = new Animation(9276);
  private static final Graphic gfx1 = new Graphic(444);
  private static final Graphic gfx2 = new Graphic(1625);
  private static final Graphic gfx3 = new Graphic(1626);
  private static final Graphic gfx4 = new Graphic(451);
  private static final Graphic gfx5 = new Graphic(1627);

  @Override
  public CombatType getCombatType() {
    return CombatType.MIXED;
  }
}
