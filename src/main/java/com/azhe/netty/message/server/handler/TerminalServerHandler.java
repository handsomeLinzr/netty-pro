package com.azhe.netty.message.server.handler;

import com.azhe.netty.message.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/21 1:02 下午
 * @since V2.0.0
 */
@Slf4j
public class TerminalServerHandler extends SimpleChannelInboundHandler<IMMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {

    }


}
