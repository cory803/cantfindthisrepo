package com.runelive.net.packet;

import com.neo.net.packet.PacketReader;

public interface PacketHandler<E> {
	void handle(E object, PacketReader reader);
}
