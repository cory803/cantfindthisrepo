package com.runelive.loginserver;

import com.neo.cipher.Cipher;
import com.neo.net.packet.Packet;
import com.neo.net.packet.PacketEncoder;
import com.runelive.loginserver.login.LoginServerLoginDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ClientConnection {
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private Channel channel;
    private final AtomicBoolean connected = new AtomicBoolean();

    public boolean isConnected() {
        return connected.get();
    }

    public boolean close() {
        if (!this.isConnected()) {
            return false;
        }
        channel.close();
        return true;
    }

    public boolean connect(String host, int port) {
        if (connected.get()) {
            throw new IllegalStateException("Login Server is already connected.");
        }
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("encoder", new PacketEncoder(EncoderConstants.PACKET_SIZS, Cipher.BLANK_CIPHER));
                pipeline.addLast("decoder", new LoginServerLoginDecoder());
            }
        });
        bootstrap.channel(NioSocketChannel.class);
        channel = bootstrap.connect(new InetSocketAddress(host, port)).syncUninterruptibly().channel();
        connected.set(true);
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                connected.set(false);
            }
        });
        ByteBuf buf = Unpooled.buffer(8);
        buf.writeLong(75018594939L); //85525764514L
        channel.writeAndFlush(buf);
        return true;
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }
}
