package com.runelive.net.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public abstract class StatefulFrameDecoder<T extends Enum<T>> extends ByteToMessageDecoder {
	private T state;

	public StatefulFrameDecoder(T state) {
		setState(state);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		this.decode(ctx, in, out, state);
	}

	@Override
	protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		this.decodeLast(ctx, in, out, state);
	}

	public final void setState(T state) {
		if (state == null) {
			throw new NullPointerException("state");
		}
		this.state = state;
	}

	protected abstract void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, T state) throws Exception;

	protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, T state) throws Exception {

	}
}

