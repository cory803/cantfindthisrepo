package com.runelive.loginserver;

import com.neo.io.types.StringType;
import com.neo.net.packet.PacketBuffer;
import com.runelive.GameServer;
import com.runelive.model.Skill;
import com.runelive.model.StaffRights;
import com.runelive.model.player.PlayerDetails;
import com.runelive.util.FilterExecutable;
import com.runelive.util.NameUtils;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;
import com.runelive.world.entity.impl.player.PlayerSaving;

public final class PacketCreator {
	private final LoginServer loginServer;

	public PacketCreator(LoginServer loginServer) {
		this.loginServer = loginServer;
	}

	public void requestLogin(PlayerDetails details) {
		int size = 8 + //Long name
				(details.getName().length() + 2) + // Formatted name
				(details.getPassword().length() + 2); //Password
		PacketBuffer packet = new PacketBuffer(1, size);
		packet.putLong(details.getEncodedName());
		packet.putString(StringType.UTF8, details.getName());
		packet.putString(StringType.UTF8, details.getPassword());
		loginServer.write(packet.toPacket());
	}

	public void pushHiscores(Player player) {
		int size = (player.getUsername().length() + 2) + (player.getGameModeAssistant().toString().length() + 2) + 2 + 2 + 8 + 100;

		PacketBuffer buffer = new PacketBuffer(3, size);
		buffer.putString(StringType.UTF8, player.getUsername()); // length + 2
		buffer.putString(StringType.UTF8, player.getGameModeAssistant().getModeName()); // length + 2
		buffer.putShort(player.getSkillManager().getCombatLevel()); //2
		buffer.putShort(player.getSkillManager().getTotalLevel()); // 2
		buffer.putLong(player.getSkillManager().getTotalExp()); // 8

		for (Skill skill : Skill.values()) {
			buffer.putInt(player.getSkillManager().getExperience(skill)); //4 * 25
		}
		loginServer.write(buffer.toPacket());
	}

	/**
	 * Submits all of the pending server log queries to the login server.
	 *
	 * Packet 4
	 *
	 * @param queries {@link String} Queries to be executed.
	 */
	public void submitServerLogs(String queries) {
		int size = queries.length() + 1;

		PacketBuffer buffer = new PacketBuffer(4, size);
		buffer.putBytes(queries.getBytes());
		buffer.putByte(10);

		loginServer.write(buffer.toPacket());
	}

	public void sendSavePacket(Player player, boolean logout) {
		String playerJson = PlayerSaving.toJson(player);
		int size = 8 + (player.getPassword().length() + 2) + 1 + 1 + playerJson.length() + 1 + 1;

		PacketBuffer packet = new PacketBuffer(2, size);
		packet.putString(StringType.UTF8, player.getUsername());
		packet.putBytes(player.getPassword().getBytes());
		packet.putByte(10);
		packet.putByte(player.getStaffRights().ordinal());
		packet.putByte(player.getDonatorRights().ordinal());
		packet.putBytes(playerJson.getBytes());
		packet.putByte(10);
		packet.putBoolean(logout);
		loginServer.write(packet.toPacket());
		if (player.getStaffRights().ordinal() < StaffRights.ADMINISTRATOR.ordinal()) {
			GameServer.getLoginServer().getPacketCreator().pushHiscores(player);
		}
	}

	public void serverUpdate(int time) {
		PacketBuffer packet = new PacketBuffer(6, 2);
		packet.putShort(time);
		loginServer.write(packet.toPacket());
	}

	public void playerConnected(long name) {
		PacketBuffer packet = new PacketBuffer(11, 8);
		packet.putLong(name);
		loginServer.write(packet.toPacket());
	}

	public void checkOnlineResponse(long name, int response) {
		PacketBuffer packet = new PacketBuffer(28, 8 + 1);
		packet.putLong(name);
		packet.putByte(response);
		loginServer.write(packet.toPacket());
	}
}
