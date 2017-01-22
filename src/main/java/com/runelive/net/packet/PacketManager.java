package com.runelive.net.packet;

import com.neo.net.packet.Packet;
import com.neo.net.packet.PacketReader;

public final class PacketManager<E> {
	private final PacketHandlerListener<E> listener;
	@SuppressWarnings({ "unchecked" })
	private final PacketHandler<E>[] packetHandlers = new PacketHandler[256];

	public PacketManager(PacketHandlerListener<E> listener, PacketHandler<E> defaultHandler) {
		this.listener = listener;
		if (defaultHandler != null) {
			for (int i = 0; i < 256; i++) {
				packetHandlers[i] = defaultHandler;
			}
		}
	}

	public void bind(PacketHandler<E> handler, int opcode) {
		packetHandlers[opcode] = handler;
	}

	public void handle(E object, Packet packet) {
		if (packet.getOpcode() < 0 || packet.getOpcode() >= 256) {
			return;
		}
		if (packetHandlers[packet.getOpcode()] == null) {
			return;
		}
		try {
			packetHandlers[packet.getOpcode()].handle(object, new PacketReader(packet));
		} catch (PacketException ex) {
			//Server.getLogger().log(Level.INFO, "Error handling packet "+packet.getOpcode());
			listener.packetExceptionCaught(object, ex);
		} catch (Throwable t) {
			//Server.getLogger().log(Level.INFO, "Error handling packet "+packet.getOpcode());
			listener.errorCaught(object, t);
		}
	}
}
