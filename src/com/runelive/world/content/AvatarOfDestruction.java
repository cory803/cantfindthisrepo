package com.runelive.world.content;


import com.runelive.model.Position;
import com.runelive.util.Misc;
import com.runelive.util.Stopwatch;
import com.runelive.world.World;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;

public class AvatarOfDestruction {

    private static final int TIME = 200;
    private static Stopwatch timer = new Stopwatch().reset();
    public static FallenBoss FALLEN_BOSS = null;
    private static LocationData LAST_LOCATION = null;


    public static class FallenBoss {

        public FallenBoss(NPC npc, LocationData starLocation) {
            this.avatarOfDestruction = avatarOfDestruction;
            this.dropLocation = dropLocation;
        }

        private NPC avatarOfDestruction;
        private LocationData dropLocation;

        public NPC getavatarOfDestruction() {
            return avatarOfDestruction;
        }

        public LocationData getStarLocation() {
            return dropLocation;
        }
    }

    public static enum LocationData {


        LOCATION_1(new Position(3107, 3506), "Right of the Edgeville bank", "Edgeville"),
        LOCATION_2(new Position(3087, 3541), "Wilderness ditch, level 3", "Wilderness"),
        LOCATION_3(new Position(2470, 5166), "Enterence of TzHaar dungeon", "TzHaar"),
        LOCATION_4(new Position(3362, 3285), "Behind the Duel Arena", "Duel Arena"),
        LOCATION_5(new Position(2928, 3453), "Enterence of Taverley", "Taverley"),;

        private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
            this.spawnPos = spawnPos;
            this.clue = clue;
            this.playerPanelFrame = playerPanelFrame;
        }

        private Position spawnPos;
        private String clue;
        public String playerPanelFrame;
    }

    public static LocationData getRandom() {
        LocationData star = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
        return star;
    }

    public static void sequence() {
        if (FALLEN_BOSS == null) {
            if (timer.elapsed(TIME)) {
                LocationData locationData = getRandom();
                if (LAST_LOCATION != null) {
                    if (locationData == LAST_LOCATION) {
                        locationData = getRandom();
                    }
                }
                LAST_LOCATION = locationData;
                FALLEN_BOSS = new FallenBoss(new NPC(934,locationData.spawnPos), locationData);
                World.sendMessage("<icon=2><shad=FF8C38> The Avatar of Destruction has spawned at " + locationData.clue + "!");
                //World.getPlayers().forEach(p -> p.getPacketSender().sendString(39162,"@or2@Crashed star: @yel@" + ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame + ""));
                timer.reset();
            }
            //} else {
            //if (FALLEN_BOSS.starObject.getPickAmount() >= MAXIMUM_MINING_AMOUNT) {
            //	despawn(false);
            //	timer.reset();
            //}
        }
    }

    public static void despawn(boolean respawn) {
        if (respawn) {
            timer.reset(0);
        } else {
            timer.reset();
        }
        if (FALLEN_BOSS != null) {
            for (Player p : World.getPlayers()) {
                if (p == null) {
                    continue;
                }
                //p.getPacketSender().sendString(39162,"@or2@Crashed star: @or2@[ @yel@N/A@or2@ ]");
				/*if (p.getInteractingObject() != null && p.getInteractingObject().getId() == FALLEN_BOSS.starObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("The Boss has been defeated.");
				}*/
            }
            FALLEN_BOSS = null;
        }
    }
}
