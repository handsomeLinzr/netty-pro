package com.azhe.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8082));
        socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
        while (!socketChannel.finishConnect()) {

        }
        System.out.println("连接完成");
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                socketChannel = (SocketChannel) key.channel();
                if (key.isReadable()) {
                    int len = socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    String content = new String(byteBuffer.array(), 0, len);
                    System.out.println("从服务端收到消息：" + content);
                    byteBuffer.clear();
                } else if (key.isWritable()) {
                    byteBuffer.put("你好".getBytes());
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                    byteBuffer.clear();
                }
            }
        }
    }

}
