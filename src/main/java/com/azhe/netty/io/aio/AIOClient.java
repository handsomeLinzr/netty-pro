package com.azhe.netty.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOClient {

    private final AsynchronousSocketChannel clientChannel;

    public AIOClient() throws IOException {
        clientChannel = AsynchronousSocketChannel.open();
    }

    /**
     * 连接
     */
    public void connect(String host, int port) {
        clientChannel.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                try {
                    // 发送数据
                    clientChannel.write(ByteBuffer.wrap("异步IO数据".getBytes())).get();
                    System.out.println("已发送数据到服务端");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("失败了" + exc);
            }
        });

        // 缓冲区
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 读取缓冲
        clientChannel.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println("操作完成" + result);
                System.out.println("获得反馈结果：" + new String(byteBuffer.array()));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("操作失败：" + exc);
            }
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws IOException {
        new AIOClient().connect("localhost", 8083);
    }

}
