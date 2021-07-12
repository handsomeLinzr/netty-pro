package com.azhe.netty.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 5:26 下午
 * @since V2.0.0
 */
public class RpcRegistry {

    // 端口
    private final int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    /**
     * 启动注册中心
     */
    private void start() {

    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }

}
