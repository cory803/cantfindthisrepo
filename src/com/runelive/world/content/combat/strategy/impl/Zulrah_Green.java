package com.runelive.world.content.combat.strategy.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.Position;
import com.runelive.model.Projectile;
import com.runelive.util.Misc;
import com.runelive.world.World;
import com.runelive.world.content.combat.CombatContainer;
import com.runelive.world.content.combat.CombatType;
import com.runelive.world.content.combat.strategy.CombatStrategy;
import com.runelive.world.content.minigames.impl.Zulrah;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class Zulrah_Green implements CombatStrategy {

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
    killer = (Player) victim;
    if (killer.getZulrahRotatingProcess()) {
      return true;
    }
    if (killer.getZulrahRotating()) {
      int hp = zulrah.getConstitution();
      killer.getCombatBuilder().reset(true);
      killer.setZulrahRotatingProcess(true);
      TaskManager.submit(new Task(1, killer, true) {
        int tick = 0;

        @Override
        public void execute() {
          tick++;
          if (tick == 1) {
            zulrah.performAnimation(go_down);
          } else if (tick == 6) {
            killer.getRegionInstance().getNpcsList().remove(zulrah);
            World.deregister(zulrah);
          } else if (tick == 7) {
            NPC new_zulrah_form = new NPC(Zulrah.ZULRAH_RED_NPC_ID,
                new Position(2269, 3075, killer.getPosition().getZ())).setSpawnedFor(killer);
            World.register(new_zulrah_form);
            killer.getRegionInstance().getNpcsList().add(new_zulrah_form);
            new_zulrah_form.setConstitution(hp);
            killer.setZulrahRotating(false);
            killer.setZulrahRotatingProcess(false);

          }
        }
      });
      TaskManager.submit(new Task(2, false) {
        int tick = 0;

        @Override
        public void execute() {
          switch (tick) {
            case 1:
              stop();
              break;
          }
          tick++;
        }
      });
    } else {
      zulrah.performAnimation(shoot);
      zulrah.getCombatBuilder()
          .setContainer(new CombatContainer(zulrah, victim, 1, 5, CombatType.RANGED, true));
      TaskManager.submit(new Task(2, zulrah, false) {
        int tick = 0;

        @Override
        public void execute() {
          switch (tick) {
            case 1:
              new Projectile(zulrah, victim, 1044, 44, 3, 43, 31, 0).sendProjectile();
              killer.setZulrahRotating(Misc.getRandom(5) == 3);
              stop();
              break;
          }
          tick++;
        }
      });
    }
    return false;
  }

  @Override
  public int attackDelay(Character entity) {
    return entity.getAttackSpeed();
  }

  @Override
  public int attackDistance(Character entity) {
    return 20;
  }

  private static Player killer = null;
  private static final Animation shoot = new Animation(5069);
  private static final Animation go_down = new Animation(5072);
  private static final Animation go_up = new Animation(5073);
  private static final Graphic range_graphic = new Graphic(1044);

  @Override
  public CombatType getCombatType() {
    return CombatType.RANGED;
  }
}
