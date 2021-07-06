package com.azhe.netty.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIOServer {

    private int port;

    public static void main(String[] args) {
        new AIOServer(8083);
    }

    /**
     * 构造函数
     * @param port
     */
    public AIOServer(int port) {
        this.port = port;
        listen();
    }

    private void listen() {
        try {
            // 线程池
            ExecutorService executorService = Executors.newCachedThreadPool();
            AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            // 工作线程，用来监听回调
            final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(threadGroup);
            // 监听端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("服务启动，监听：" + port);

            // 准备接收数据
            serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                // 实现completed方法来回调
                // 由系统自动触发
                // 回调有两个状态，成功
                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    System.out.println("IO操作成功，开始获取数据");
                    try {
                        byteBuffer.clear();
                        result.read(byteBuffer).get();
                        byteBuffer.flip();
                        result.write(byteBuffer);
                        byteBuffer.flip();
                    } catch (Exception e) {
                        System.out.println(e);
                    } finally {
                        try {
                            result.close();
                            serverSocketChannel.accept(null, this);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    System.out.println("操作完成");
                }

                // 失败
                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("操作失败" + exc);
                }
            });

            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
