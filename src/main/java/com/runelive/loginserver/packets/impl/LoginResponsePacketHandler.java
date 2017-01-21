package com.runelive.loginserver.packets.impl;

import com.neo.net.packet.PacketReader;
import com.runelive.loginserver.LoginProcessor;
import com.runelive.loginserver.LoginServer;
import com.runelive.loginserver.packets.LoginServerPacketHandler;

import java.io.ByteArrayOutputStream;

public final class LoginResponsePacketHandler implements LoginServerPacketHandler {
    @Override
    public void handle(LoginServer loginServer, PacketReader packet) {
        long hash = packet.getLong();
        int response = packet.getByte();
        int staffRights = 0;
        int donorRights = 0;
        String gameSave = null;
        if (packet.getSize() > 9) {
            staffRights = packet.getByte();
            donorRights = packet.getByte();

            ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
            byte b;
            while ((b = (byte) packet.getByte()) != 10) {
                lineBuffer.write(b);
            }
            gameSave = new String(lineBuffer.toByteArray());
        }
        LoginProcessor.handleLoginResponse(hash, response, staffRights, donorRights, gameSave);
    }
}