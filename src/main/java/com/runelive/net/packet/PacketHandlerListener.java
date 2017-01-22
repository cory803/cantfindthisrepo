package com.runelive.net.packet;

public interface PacketHandlerListener<E> {
	void packetExceptionCaught(E object, PacketException cause);
	void errorCaught(E object, Throwable cause);
}
