package com.azhe.netty.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Description: 聊天客户端
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 2:39 下午
 * @since V2.0.0
 */
public class ChatNioClient {

    // 端口
    private final int port;
    // 多路复用器
    private final Selector selector;
    // 客户端
    private final SocketChannel client;
    // 读取缓存
    private final ByteBuffer byteBufferRead = ByteBuffer.allocate(1024);
    // 编解码器
    private final Charset charset = StandardCharsets.UTF_8;
    // 昵称
    private String nickName = "";
    // 分隔符
    private static final String SPLIT_FLAG = "#@#";
    private static final String NAME_EXISTS = "昵称已存在，请重新设置：";

    public ChatNioClient(int port) throws IOException {
        this.port = port;
        this.selector = Selector.open();
        // 连接
        this.client = SocketChannel.open(new InetSocketAddress("localhost", this.port));
        // 非阻塞
        this.client.configureBlocking(false);
        // 注册读事件
        this.client.register(this.selector, SelectionKey.OP_READ);
    }

    /**
     * 分出两条线程分别读入和写出事件
     */
    private void session() {
        new Thread(new Reader()).start();
        new Thread(new Writer()).start();
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            // 循环
            while (!Thread.interrupted()) {
                try {
                    // 这里会阻塞，当有事件进来时（注册的其中一个事件）继续执行
                    int keyNum = selector.select();
                    if (keyNum == 0) continue;
                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        process(key);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 处理key
         * @param key
         */
        private void process(SelectionKey key) throws IOException {
            if (key.isReadable()) {
                // 只处理读事件
                SocketChannel client = (SocketChannel) key.channel();
                StringBuilder content = new StringBuilder();
                while (client.read(byteBufferRead) > 0) {
                    byteBufferRead.flip();
                    content.append(charset.decode(byteBufferRead));
                    byteBufferRead.clear();
                }
                System.out.println(content.toString());
                if ((NAME_EXISTS + "'" + nickName + "'").equals(content.toString())) {
                    nickName = "";
                }
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private class Writer implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                while (scanner.hasNext()) {
                    // 循环获取控制台输入
                    String line = scanner.nextLine();
                    // 空字符则继续循环
                    if ("".equals(line)) continue;
                    if ("".equals(nickName)) {
                        // 第一次设置昵称
                        nickName = line;
                        client.write(charset.encode(line));
                    } else {
                        // 聊天
                        client.write(charset.encode(nickName + SPLIT_FLAG + line));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                scanner.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatNioClient(8080).session();
    }

}
