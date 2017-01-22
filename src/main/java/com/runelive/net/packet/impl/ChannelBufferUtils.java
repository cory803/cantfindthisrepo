package com.runelive.net.packet.impl;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public final class ChannelBufferUtils {

    public static String readLine(ByteBuf channelBuffer) {
        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
        byte b;
        while ((b = channelBuffer.readByte()) != 10) {
            lineBuffer.write(b);
        }
        return new String(lineBuffer.toByteArray());
    }

    public static void putLine(ByteBuf channelBuffer, String line) {
        channelBuffer.writeBytes(line.getBytes());
        channelBuffer.writeByte(10);
    }

    public static ByteBuf decode(ByteBuf channelBuffer, Random random) {
        int length = channelBuffer.writerIndex() - channelBuffer.readerIndex();
        byte[] decrypted = new byte[length];
        channelBuffer.readBytes(decrypted);
        for (int i = 0; i < length; i++) {
            decrypted[i] ^= random.nextInt();
        }
        ByteBuf decoded = channelBuffer.alloc().buffer(decrypted.length);
        decoded.writeBytes(decrypted);
        return decoded;
    }
}
