package com.azhe.netty.message.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

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
     *
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        try {
            // 可读取的长度
            int length = in.readableBytes();
            // 创建用于保存缓存的字节数组
            byte[] array = new byte[length];

            // 读取数据到字节数组
            in.getBytes(in.readerIndex(), array, 0, length);
            // 序列化数据后添加到out中，传递给下一个解码器处理
            out.add(new MessagePack().read(array, IMMessage.class));
            in.clear();
        } catch (MessageTypeException e) {
            // 序列化失败，传进来的数据不是 IMMessage 形式
            // 则说明当前通道不是命令行格式，移除当前解码器
            ctx.channel().pipeline().remove(this);
        }

    }
}
