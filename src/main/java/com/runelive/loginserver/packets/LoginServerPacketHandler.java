package com.runelive.loginserver.packets;

import com.neo.net.packet.PacketReader;
import com.runelive.loginserver.LoginServer;
import com.runelive.net.packet.PacketHandler;

public interface LoginServerPacketHandler extends PacketHandler<LoginServer> {
	void handle(LoginServer loginServer, PacketReader packet);
}
