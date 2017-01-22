package com.runelive.loginserver.packets.impl;

import com.neo.net.packet.PacketReader;
import com.runelive.GameServer;
import com.runelive.loginserver.LoginProcessor;
import com.runelive.loginserver.LoginServer;
import com.runelive.loginserver.packets.LoginServerPacketHandler;

import java.util.logging.Level;

public final class ConfirmLoginPacketHandler implements LoginServerPacketHandler {
	@Override
	public void handle(LoginServer loginServer, PacketReader packet) {
		loginServer.setConnectionConfirmed(true);
        LoginProcessor.emptyPendingLogins();
        GameServer.getLogger().log(Level.INFO, "Login server is connected.");
	}
}
