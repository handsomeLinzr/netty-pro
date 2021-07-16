package com.azhe.netty.rpc.registry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcProxyHandler extends ChannelInboundHandlerAdapter {

    private Object response;

    /**
     * 返回结果
     * @return
     */
    public Object getResponse() {
        return this.response;
    }

    /**
     * 保存结果
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = msg;
    }
}
