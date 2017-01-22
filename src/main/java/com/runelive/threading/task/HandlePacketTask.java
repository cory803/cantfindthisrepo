package com.runelive.threading.task;

import com.neo.net.packet.Packet;
import com.runelive.net.packet.PacketManager;
import com.runelive.threading.GameEngine;

public final class HandlePacketTask<E> extends Task {
	private final PacketManager<E> manager;
	private final E e;
	private final Packet packet;

	public HandlePacketTask(PacketManager<E> manager, E e, Packet packet) {
		this.manager = manager;
		this.e = e;
		this.packet = packet;
	}

	@Override
	public void execute(GameEngine context) {
		manager.handle(e, packet);
	}
}
