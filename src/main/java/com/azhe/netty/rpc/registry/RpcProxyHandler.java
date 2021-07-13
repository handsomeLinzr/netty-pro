package com.azhe.netty.rpc.registry;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class RpcProxyHandler extends ChannelHandlerAdapter {

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = msg;
    }
}
