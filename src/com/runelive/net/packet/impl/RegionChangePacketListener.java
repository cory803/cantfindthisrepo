package com.runelive.net.packet.impl;

import com.runelive.engine.task.Task;
import com.runelive.engine.task.TaskManager;
import com.runelive.model.RegionInstance.RegionInstanceType;
import com.runelive.net.packet.Packet;
import com.runelive.net.packet.PacketListener;
import com.runelive.world.content.CustomObjects;
import com.runelive.world.content.Sounds;
import com.runelive.world.entity.impl.GroundItemManager;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.updating.NPCUpdating;


public class RegionChangePacketListener implements PacketListener {

  @Override
  public void handleMessage(Player player, Packet packet) {
    if (player.isAllowRegionChangePacket()) {
      CustomObjects.handleRegionChange(player);
      GroundItemManager.handleRegionChange(player);
      Sounds.handleRegionChange(player);
      player.getTolerance().reset();
      // Hunter.handleRegionChange(player);
      if (player.getRegionInstance() != null && player.getPosition().getX() != 1
          && player.getPosition().getY() != 1) {
        if (player.getRegionInstance().equals(RegionInstanceType.BARROWS)
            || player.getRegionInstance().equals(RegionInstanceType.WARRIORS_GUILD))
          player.getRegionInstance().destruct();
      }

      /** NPC FACING **/
      TaskManager.submit(new Task(1, player, false) {
        @Override
        protected void execute() {
          for (NPC npc : player.getLocalNpcs()) {
            if (npc == null || npc.getMovementCoordinator().getCoordinator().isCoordinate())
              continue;
            NPCUpdating.updateFacing(player, npc);
          }
          stop();
        }
      });

      player.setRegionChange(false).setAllowRegionChangePacket(false);
    }
  }
}
