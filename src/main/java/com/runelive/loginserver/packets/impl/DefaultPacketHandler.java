package com.runelive.loginserver.packets.impl;

import com.neo.net.packet.PacketReader;
import com.runelive.loginserver.LoginServer;
import com.runelive.loginserver.packets.LoginServerPacketHandler;
import com.runelive.net.packet.PacketException;

public final class DefaultPacketHandler implements LoginServerPacketHandler {
	@Override
	public void handle(LoginServer loginServer, PacketReader packet) {
		throw new PacketException("Packet : [opcode=" + packet.getOpcode() + " length=" + packet.getSize() + ']');
	}
}
