package com.azhe.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/6 4:43 下午
 * @since V2.0.0
 */
public class NIOServer {

    // 选择器
    private Selector selector;
    // 缓冲区
    private ByteBuffer byteBuffer;

    public NIOServer(int port) throws IOException {
        selector = Selector.open();
        byteBuffer = ByteBuffer.allocate(1024);
        // 通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 注册到选择器中，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("nio服务端启动，监听：" + port);
    }

    /**
     * 循环获得key做业务
     * @throws IOException
     */
    private void listen() throws IOException {
        while (true) {
            // 选择器调用获取返回的就绪连接
            selector.select();
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                // 获得key
                SelectionKey key = iterator.next();
                // 移除key，避免重复消费
                iterator.remove();
                process(key);
            }
        }
    }

    /**
     * 处理业务
     * @param key
     */
    private void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            // 有连接
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            serverSocketChannel.configureBlocking(false);
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            // 有读
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 有数据
            int len = socketChannel.read(byteBuffer);
            if (len > 0) {
                // 翻转
                byteBuffer.flip();
                String content = new String(byteBuffer.array(), 0, len);
                byteBuffer.clear();
                socketChannel.register(selector, SelectionKey.OP_WRITE);
                key.attach(content);
                System.out.println("收到客户端数据：" + content);
            }
        } else if (key.isWritable()) {
            // 有写
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 获得得到的数据
            String content = (String) key.attachment();
            socketChannel.write(ByteBuffer.wrap(content.getBytes()));
            socketChannel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(8082).listen();
    }

}
