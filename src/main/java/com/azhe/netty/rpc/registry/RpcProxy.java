package com.azhe.netty.rpc.registry;

import com.azhe.netty.rpc.protocol.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * 远程代理
 */
public class RpcProxy {

    public static <T> T getProxy(Class<T> clazz, int port) {
        MethodProxy methodProxy = new MethodProxy(clazz, port);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);
    }

    private static class MethodProxy implements InvocationHandler {

        private Class<?> clazz;
        private int port;
        public MethodProxy(Class<?> clazz, int port) {
            this.clazz = clazz;
            this.port = port;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result;
            if (Object.class.equals(method.getDeclaringClass())) {
                // 实现类，直接调用
                result = method.invoke(proxy, args);
            } else {
                // 接口类，远程调用
                result = rpcInvoke(proxy, method, args);
            }
            return result;
        }

        /**
         * 远程调用服务
         * @param proxy
         * @param method
         * @param args
         * @return
         */
        private Object rpcInvoke(Object proxy, Method method, Object[] args) {
            // 创建协议
            InvokerProtocol invokerProtocol = new InvokerProtocol();
            invokerProtocol.setClassName(clazz.getName());
            invokerProtocol.setMethodName(method.getName());
            invokerProtocol.setParameterTypes(method.getParameterTypes());
            invokerProtocol.setParameters(args);

            RpcProxyHandler consumerHandler = new RpcProxyHandler();

            // 线程组
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                // 解码：inboud
                                // 编码：outboud
                                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0 ,4));
                                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                pipeline.addLast("encoder", new ObjectEncoder());
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                pipeline.addLast("handler", consumerHandler);
                            }
                        });

                ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", this.port)).sync();
                // 将结果返回
                channelFuture.channel().writeAndFlush(invokerProtocol).sync();
                // 关闭连接
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
            return consumerHandler.getResponse();
        }
    }


}
