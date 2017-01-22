package com.runelive.loginserver;


import com.runelive.net.packet.PacketException;
import com.runelive.net.packet.PacketHandlerListener;

public final class LoginServerPacketHandlerListener implements PacketHandlerListener<LoginServer> {
	@Override
	public void packetExceptionCaught(LoginServer server, PacketException cause) {
		cause.printStackTrace();
		server.close();
	}

	@Override
	public void errorCaught(LoginServer server, Throwable cause) {
		cause.printStackTrace();
		server.close();
	}
}
