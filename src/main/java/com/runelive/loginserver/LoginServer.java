package com.runelive.loginserver;

import com.neo.net.packet.Packet;
import com.runelive.GameServer;
import com.runelive.loginserver.packets.impl.ConfirmLoginPacketHandler;
import com.runelive.loginserver.packets.impl.LoginResponsePacketHandler;
import com.runelive.net.packet.PacketManager;
import com.runelive.threading.GameEngine;
import com.runelive.threading.task.HandlePacketTask;
import com.runelive.threading.task.Task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class LoginServer implements Runnable {

	private final ClientConnection connection = new ClientConnection();
	private String[] hosts;
	private final int port;
	private final Thread thread;
	private boolean running;
	private final Queue<Packet> outgoingPackets = new ConcurrentLinkedQueue<>();
	private final Queue<Packet> incomingPackets = new ConcurrentLinkedQueue<>();
	private final PacketManager<LoginServer> packetManager;
	private final PacketCreator packetCreator;
	private boolean connectionConfirmed;

	public LoginServer(String[] hosts, int port) {
		this.hosts = hosts;
		this.port = port;
		this.thread = new Thread(this);
		this.packetManager = new PacketManager(new LoginServerPacketHandlerListener(), null);
		this.packetCreator = new PacketCreator(this);
		this.initPacketHandlers();
	}

    public boolean hasOutgoingPackets() {
        if(!outgoingPackets.isEmpty()) {
            int i = outgoingPackets.size();
            System.out.println("Packets remaining: " + i);
            return true;
        } else {
            return false;
        }
    }

    public int getOutgoingQueueSize() {
        return outgoingPackets.size();
    }

	private void initPacketHandlers() {
		packetManager.bind(new ConfirmLoginPacketHandler(), 0);
		packetManager.bind(new LoginResponsePacketHandler(), 1);
	}

	public void start() {
		if (running) {
			throw new IllegalStateException("The processor is already running.");
		}
		thread.start();
	}

	public void stop() {
		if (!running) {
			throw new IllegalStateException("The processor is already stopped.");
		}
		running = false;
	}

	private boolean reconnecting = false;

	public void reconnect() {
		if (GameServer.getLoginServer().isConnected() && GameServer.getLoginServer().isConnectionConfirmed()) {
			System.out.println("Login server is already connected.");
			reconnecting = false;
		} else {
			GameServer.submit(new Task() {
				@Override
				public void execute(GameEngine context) {
					reconnecting = true;
					try {
						GameServer.getLoginServer().stop();
						GameServer.getLoginServer().close();
					} catch (Exception e) {
						System.err.println("Exception closing corrupt login server connection.");
						System.err.println(e.getMessage());

					}
					System.err.println("Attempting to re-establish login server connection...");
					GameServer.setLoginServer(new LoginServer(new String[] {"gameserver.rune.live"}, 43590));
					GameServer.getLoginServer().start();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (GameServer.getLoginServer().isConnected() && GameServer.getLoginServer().isConnectionConfirmed()) {
						System.err.println("Login server reconnected.");
					} else {
						System.err.println("Please try login server again in 5 seconds.");
					}
					reconnecting = false;
				}
			});
		}
	}

	public boolean isConnectionConfirmed() {
		return this.connectionConfirmed;
	}

	public boolean isConnected() {
		if (connection.isConnected()) {
			return true;
		}
		synchronized (this) {
			this.notify();
		}
		return connectionConfirmed = false;
	}

	public void close() {
		connection.close();
	}

	@Override
	public void run() {
		running = true;
		int attempts = 0;
		while (running) {
			try {
				if (!this.isConnected()) {
					while (!connection.connect(hosts[attempts % hosts.length], port)) {
						System.err.println(hosts[attempts % hosts.length] + " is offline, finding working host");
						attempts += 1;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignored) {
					}
				}
				while (this.isConnected() && connectionConfirmed && !outgoingPackets.isEmpty()) {
					connection.sendPacket(outgoingPackets.poll());
				}
				while (!incomingPackets.isEmpty()) {
					GameServer.submit(new HandlePacketTask<>(packetManager, this, incomingPackets.poll()));
				}
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException ignored) {
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
				connection.close();
			}
		}
		connection.close();
	}

	public void waitForPendingPackets() {
		while (!outgoingPackets.isEmpty() || !incomingPackets.isEmpty()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public boolean write(Packet packet) {
		boolean result = outgoingPackets.offer(packet);
		synchronized (this) {
			this.notify();
		}
		return result;
	}

	public boolean sendIncomingPacket(Packet packet) {
		boolean result = incomingPackets.offer(packet);
		synchronized (this) {
			this.notify();
		}
		return result;
	}

	public void setConnectionConfirmed(boolean connectionConfirmed) {
		this.connectionConfirmed = connectionConfirmed;
	}

	public PacketCreator getPacketCreator() {
		return packetCreator;
	}

}
