package com.azhe.netty.tomcat.netty;

import com.azhe.netty.tomcat.netty.http.AZRequest;
import com.azhe.netty.tomcat.netty.http.AZResponse;
import com.azhe.netty.tomcat.netty.http.AZServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * netty 版 tomcat
 */
public class AZTomcat {

    // 端口
    private int port;
    // 服务
    private Map<String, AZServlet> servletMap = new HashMap<>(16);

    /**
     * 启动服务
     */
    public void start() {
        init();
        // boss 线程池组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // worker 线程池组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // netty 的 server服务 ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)    // boss线程池和worker线程池
                    .channel(NioServerSocketChannel.class)    // 主线程处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {      // 子线程处理类
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());  // 对返回编码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());   // 对请求解码
                            socketChannel.pipeline().addLast(new AZHandler());  // 自定义处理器
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)    // 主线程配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true);    // 子线程配置

            // 启动服务
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("服务启动，监听：" + port);

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public class AZHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 处理请求
            if (msg instanceof HttpRequest) {
                // 获得请求
                HttpRequest httpRequest = (HttpRequest) msg;
                AZRequest request = new AZRequest(ctx, httpRequest);
                AZResponse response = new AZResponse(ctx, httpRequest);
                // 根据url进行转发
                String url = request.getUrl();
                if (servletMap.containsKey(url)) {
                    AZServlet servlet = servletMap.get(url);
                    servlet.service(request, response);
                } else {
                    response.write("404 - NOT FOUND");
                }
            }
        }
    }

    /**
     * 初始化
     */
    public void init() {
        try {
            // 获取配置
            Properties properties = new Properties();
            String rootPath = this.getClass().getResource("/").getPath();
            FileInputStream fileInputStream = new FileInputStream(rootPath + "netty-web.properties");
            properties.load(fileInputStream);

            for (Object k : properties.keySet()) {
                String key = k.toString();
                if ("server.port".equals(key)) {
                    // 端口
                    this.port = Integer.parseInt(properties.getProperty(key));
                } else if (key.endsWith(".url")) {
                    // 配置服务和url的映射
                    String serverName = key.replaceAll("\\.url$", "");
                    String url = properties.getProperty(key);
                    String className = properties.getProperty(serverName + ".className");
                    AZServlet servlet = (AZServlet) Class.forName(className).newInstance();
                    servletMap.put(url, servlet);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new AZTomcat().start();
    }

}
