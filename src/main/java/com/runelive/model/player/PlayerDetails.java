package com.runelive.model.player;

import com.runelive.net.PlayerSession;
import com.runelive.world.entity.impl.player.Player;
import org.jboss.netty.channel.Channel;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 1/14/2017.
 *
 * @author Seba
 */
public class PlayerDetails {

    private final Channel channel;
    private final String name;
    private final long encodedName;
    private final String password;
    private final String ipAddress;
    private final String macAddress;
    private final String clientSerial;
    private final long serial;
    private final String clientVersion;
    private final int uid;
    private PlayerSession session;

    public PlayerDetails(Channel channel, String name, long encodedName, String password, String ipAddress, String macAddress, String clientSerial, long serial, String clientVersion, int uid) {
        this.channel = channel;
        this.name = name;
        this.encodedName = encodedName;
        this.password = password;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.clientSerial = clientSerial;
        this.serial = serial;
        this.clientVersion = clientVersion;
        this.uid = uid;
        this.session = new PlayerSession(channel);

        this.session.setPlayer(createPlayer());
    }

    public Channel getChannel() {
        return channel;
    }

    public String getName() {
        return name;
    }

    public long getEncodedName() {
        return encodedName;
    }

    public String getPassword() {
        return password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getClientSerial() {
        return clientSerial;
    }

    public long getSerial() {
        return serial;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public int getUid() {
        return uid;
    }

    public PlayerSession getSession() {
        return session;
    }

    public Player getPlayer() {
        return session.getPlayer();
    }

    private Player createPlayer() {
        Player player = new Player(session);
        player.setUsername(name);
        player.setLongUsername(encodedName);
        player.setPassword(password);
        player.setHostAddress(ipAddress);
        player.setMacAddress(macAddress);
        player.setComputerAddress(clientSerial);
        player.setSerialNumber(serial);

        return player;
    }
}
