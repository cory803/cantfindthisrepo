package org.scripts.kotlin.content.commands.writenpc;

import com.chaos.model.StaffRights;
import com.chaos.model.player.command.Command;
import com.chaos.world.World;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

import java.io.File;
import java.util.HashMap;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/30/2016.
 *
 * @author Seba
 */
public class CopyWriteNpc extends Command {

    File file = new File("./data/def/npcSpawns.dat");

    public CopyWriteNpc(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (player.idNpcSpawn == 0) {
            player.getPacketSender().sendMessage("Error with command.");
        } else {
            if (SpawnList.spawnList == null) {
                SpawnList.spawnList = new HashMap<>();
                SpawnList.deSerialize(file); //TODO: Add file.
            }

            int npcId;
            int radius;
            boolean canWalk;
            npcId = player.idNpcSpawn;
            radius = player.radiusNpcSpawn;
            canWalk = player.canWalkNpcSpawn;

            SpawnList spawnList;

            if (SpawnList.spawnList.containsKey(npcId)) {
                spawnList = SpawnList.spawnList.get(npcId);
            } else {
                spawnList = new SpawnList();
                spawnList.id = npcId;
                SpawnList.spawnList.put(spawnList.id, spawnList);
            }

            Spawn spawn = new Spawn();
            spawn.setX(player.getPosition().getX());
            spawn.setY(player.getPosition().getY());
            spawn.setZ(player.getPosition().getZ());
            spawn.setDirection(Spawn.DIRECTION.NONE);
            spawn.setWalking(canWalk ? Spawn.STATE.TRUE : Spawn.STATE.FALSE);
            spawn.setRadius(radius);
            spawn.setWorld(Spawn.WORLD.ALL);

            spawnList.spawns.add(spawn);

            SpawnList.serialize(file);

            NPC npc = new NPC(npcId, player.getPosition());
            npc.setWalking(canWalk);
            npc.walkingDistance = radius;

            World.register(npc);

            player.getPacketSender().sendMessage("We have successfully written the npc.");
        }
    }
}
