package com.runelive.loginserver;

import com.neo.net.packet.Packet;
import com.neo.net.packet.event.PacketErrorEvent;
import com.neo.net.packet.event.PacketEventListener;
import com.runelive.GameServer;

public final class LoginServerPacketEventListener implements PacketEventListener {
	@Override
	public void packetError(PacketErrorEvent packetErrorEvent) {
		packetErrorEvent.getChannel().close();
	}

	@Override
	public void packetReceived(Packet packet) {
		GameServer.getLoginServer().sendIncomingPacket(packet);
	}
}
