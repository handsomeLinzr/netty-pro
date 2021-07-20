package com.azhe.netty.message.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义即时消息协议解码器
 */
public class IMDecoder extends ByteToMessageDecoder {

    /**
     * 解析
     * @param ctx 处理
     * @param in 字节缓冲区
     * @param out 解析到的数据集合
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
    }
}
