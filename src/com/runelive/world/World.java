package com.runelive.world;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import com.runelive.model.Locations.Location;

import com.runelive.net.login.LoginResponses;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.runelive.GameServer;
import com.runelive.GameSettings;
import com.runelive.model.PlayerRights;
import com.runelive.util.Misc;
import com.runelive.net.login.LoginDecoder;
import com.runelive.world.content.ShootingStar;
import com.runelive.world.content.minigames.impl.FightPit;
import com.runelive.world.content.minigames.impl.PestControl;
import com.runelive.world.entity.Entity;
import com.runelive.world.entity.EntityHandler;
import com.runelive.world.entity.impl.CharacterList;
import com.runelive.world.entity.impl.npc.NPC;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerHandler;
import com.runelive.world.entity.updating.NpcUpdateSequence;
import com.runelive.world.entity.updating.PlayerUpdateSequence;
import com.runelive.world.entity.updating.UpdateSequence;

/**
 * @author Gabriel Hannason Thanks to lare96 for help with parallel updating system
 */
public class World {

    public static long currentServerTime;

    /**
     * Is global yell enabled?
     */
    private static boolean globalYell = true;

    public static boolean isGlobalYell() {
        return globalYell;
    }

    public static void setGlobalYell(boolean globalYell) {
        World.globalYell = globalYell;
    }

    /**
     * All of the registered players.
     */
    private static CharacterList<Player> players = new CharacterList<>(1000);

    /**
     * All of the registered NPCs.
     */
    private static CharacterList<NPC> npcs = new CharacterList<>(2027);

    /**
     * Used to block the game thread until updating has completed.
     */
    private static Phaser synchronizer = new Phaser(1);

    /**
     * A thread pool that will update players in parallel.
     */
    private static ExecutorService updateExecutor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
                    .setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

    /**
     * The queue of {@link Player}s waiting to be logged in.
     **/
    //private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

    /**
     * The queue of {@link Player}s waiting to be logged out.
     **/
    private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

    /**
     * The queue of {@link Player}s waiting to be given their vote reward.
     **/
    private static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void register(Entity entity) {
        EntityHandler.register(entity);
    }

    public static void deregister(Entity entity) {
        EntityHandler.deregister(entity);
    }

    public static Player getPlayerByName(String username) {
        Optional<Player> op =
                players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
        return op.isPresent() ? op.get() : null;
    }

    public static Player getPlayerByLong(long encodedName) {
        Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
        return op.isPresent() ? op.get() : null;
    }

    public static void sendMessage(String message) {
        players.forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void sendWildernessMessage(String message) {
        for (Player player : players) {
            if (player != null && (player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.WILDKEY_ZONE)) {
                player.getPacketSender().sendMessage(message);
            }
        }
    }

    public static void sendYell(String message, Player player) {
        players.stream().filter(p -> p != null && (p.yellToggle()) && (!p.getRelations().getIgnoreList().contains(player.getLongUsername())))
                .forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void sendStaffMessage(String message) {
        players.stream()
                .filter(p -> p != null && (p.getRights() == PlayerRights.OWNER
                        || p.getRights() == PlayerRights.COMMUNITY_MANAGER
                        || p.getRights() == PlayerRights.ADMINISTRATOR
                        || p.getRights() == PlayerRights.MODERATOR || p.getRights() == PlayerRights.SUPPORT))
                .forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void updateServerTime() {
        players.forEach(p -> p.getPacketSender().sendString(39161,
                "@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]"));
    }

    public static void updatePlayersOnline() {
        players.forEach(p -> p.getPacketSender().sendString(55073,
                "@red@Players online:   @gre@(@gre@" + (int) (players.size()) + "@gre@)"));
        players.forEach(p -> p.getPacketSender().sendString(57003,
                "Players:  @gre@" + (int) (World.getPlayers().size()) + ""));
    }

    public static void savePlayers() {
        players.forEach(p -> p.save());
    }

    public static CharacterList<Player> getPlayers() {
        return players;
    }

    public static CharacterList<NPC> getNpcs() {
        return npcs;
    }

    /*
     * Gets the player according to said name in long format.
     * @param name The name of the player to search for in long format.
     * @return The player who has the same name as said param.
     */
    public static Player getPlayerForLongName(long longName) {
        for (Player player : players) {
            if (player != null && player.getLongUsername() == longName) {
                return player;
            }
        }
        return null;
    }

    public static void sequence() {
        long start = System.currentTimeMillis();

        // Handle queued logins.
        /*for (int amount = 0; amount < GameSettings.LOGIN_THRESHOLD; amount++) {
            Player player = logins.poll();
            if (player == null)
                break;
            if (player.getResponse() == 2) {
                if (logouts.contains(player)) {
                    player.setResponse(LoginResponses.LOGIN_ACCOUNT_ONLINE);
                } else {
                    PlayerHandler.handleLogin(player);
                }
            }
        }*/
        long logout_start = System.currentTimeMillis();
        long loginCycle = logout_start - start;

        // Handle queued logouts.
        int amount = 0;
        Iterator<Player> $it = logouts.iterator();
        while ($it.hasNext()) {
            Player player = $it.next();
            if (player == null || amount >= GameSettings.LOGOUT_THRESHOLD)
                break;
            if (PlayerHandler.handleLogout(player)) {
                $it.remove();
                amount++;
            }
        }
        long minigame_start = System.currentTimeMillis();
        long logoutCycle = minigame_start - logout_start;

        FightPit.sequence();
        PestControl.sequence();
        ShootingStar.sequence();
        // EvilTrees.sequence();
        long player_start = System.currentTimeMillis();
        long minigameCycle = player_start - minigame_start;

        // First we construct the update sequences.
        UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
        UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();
        // Then we execute pre-updating code.
        players.forEach(playerUpdate::executePreUpdate);
        npcs.forEach(npcUpdate::executePreUpdate);
        // Then we execute parallelized updating code.
        synchronizer.bulkRegister(players.size());
        players.forEach(playerUpdate::executeUpdate);
        synchronizer.arriveAndAwaitAdvance();
        // Then we execute post-updating code.
        players.forEach(playerUpdate::executePostUpdate);
        npcs.forEach(npcUpdate::executePostUpdate);
        long end = System.currentTimeMillis();
        long entityUpdateCycle = end - player_start;
        // long cycleTime = end - start;
        GameServer.getPanel().addWorldCycle(loginCycle, logoutCycle, minigameCycle, entityUpdateCycle);
    }

    public static void logError(String name, Exception ex) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(name, true));
            bw.write("Date: " + new Date().toString());
            bw.newLine();
            if (ex.getCause() != null) {
                bw.write("cause: " + ex.getCause().toString());
                bw.newLine();
            }
            if (ex.getClass() != null) {
                bw.write(ex.getClass().getSimpleName().toString());
            }
            if (ex.getMessage() != null) {
                bw.write(": " + ex.getMessage());
                bw.newLine();
            }
            if (ex.getStackTrace() == null)
                ex.fillInStackTrace();
            if (ex.getStackTrace() != null) {
                for (StackTraceElement s : ex.getStackTrace()) {
                    bw.write("  at " + s.getClassName() + "." + s.getMethodName());
                    if (s.getFileName() != null)
                        bw.write("(" + s.getFileName() + ":" + s.getLineNumber() + ")");
                    else
                        bw.write("(Unknown Source)");
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.write("================================");
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static Queue<Player> getLoginQueue() {
        return logins;
    }*/

    public static Queue<Player> getLogoutQueue() {
        return logouts;
    }

    public static Queue<Player> getVoteRewardingQueue() {
        return voteRewards;
    }
}
