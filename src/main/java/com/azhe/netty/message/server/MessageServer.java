package com.azhe.netty.message.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息推送服务端
 */
@Slf4j
public class MessageServer {

    public void start(int port) {

        // 线程池组
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 添加处理器
                            ChannelPipeline pipeline = channel.pipeline();
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port);
            log.info("消息推送系统启动，端口：{}", port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public void start() {
        start(8080);
    }

    public static void main(String[] args) {

        /*
          指定端口启动服务端，默认8080
         */
        MessageServer server = new MessageServer();
        if (args.length > 0) {
            server.start(Integer.parseInt(args[0]));
        } else {
            server.start();
        }
    }

}
