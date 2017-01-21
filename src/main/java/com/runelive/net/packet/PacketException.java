package com.runelive.net.packet;

public final class PacketException extends RuntimeException {
	public PacketException() {
	}

	public PacketException(String message) {
		super(message);
	}
}
