package com.runelive.loginserver.login;

import com.neo.cipher.Cipher;
import com.neo.net.packet.PacketBuffer;
import com.neo.net.packet.PacketDecoder;
import com.runelive.GameSettings;
import com.runelive.loginserver.DecoderConstants;
import com.runelive.loginserver.LoginServerPacketEventListener;
import com.runelive.net.login.StatefulFrameDecoder;
import com.runelive.util.FilterExecutable;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public final class LoginServerLoginDecoder extends StatefulFrameDecoder<LoginServerLoginDecoder.State> {
    public LoginServerLoginDecoder() {
        super(State.LOGIN);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, State state) throws Exception {
        Response response = this.decode(ctx, in, state);
        if (response != null) {
            System.err.println("Login server could not connect: " + response);
        }
    }

    public Response decode(ChannelHandlerContext ctx, ByteBuf buffer, State state) throws Exception {
        switch (state) {
            case LOGIN: {
                if (!buffer.isReadable(9)) {
                    return null;
                }
                buffer.readLong();
                Response response = Response.values()[buffer.readUnsignedByte()];
                if (response != Response.INITALIZE) {
                    return response;
                }
                ByteBuf channelBuffer = Unpooled.buffer(8 + 1);
                channelBuffer.writeLong(1348905539048L);
                channelBuffer.writeByte((byte) GameSettings.WORLD_ID);
                ctx.writeAndFlush(channelBuffer);
                this.setState(State.SYNC);
                return null;
            }
            case SYNC: {
                if (!buffer.isReadable(1)) {
                    return null;
                }
                Response response = Response.values()[buffer.readUnsignedByte()];
                if (response != Response.SUCCESS) {
                    return response;
                }
                final PacketBuffer packet = new PacketBuffer(4, World.getPlayers().size() * 8);
                World.executeAll(new FilterExecutable<Player>() {
                    @Override
                    public void execute(Player player) {
                        packet.putLong(player.getLongUsername());
                    }
                });
                ctx.writeAndFlush(packet.toPacket());
                this.setState(State.FINISH);
                ctx.pipeline().replace("decoder", "decoder", new PacketDecoder(DecoderConstants.PACKET_SIZES, Cipher.BLANK_CIPHER, new LoginServerPacketEventListener()));
                return null;
            }
        }
        return null;
    }

    enum State {
        LOGIN,
        SYNC,
        FINISH
    }
}
