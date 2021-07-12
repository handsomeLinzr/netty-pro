package com.azhe.netty.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Description: 聊天服务端
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 2:39 下午
 * @since V2.0.0
 */
public class ChatNioServer {

    // 端口
    private final int port;
    // 多路复用器
    private final Selector selector;
    // 编解码器
    private final Charset charset = StandardCharsets.UTF_8;
    // 读缓存
    private final ByteBuffer byteBufferRead = ByteBuffer.allocate(1024);
    // 昵称集合
    private final Set<String> nameSet = new HashSet<>(16);

    // 短语
    private static final String HELLO = "欢迎进入聊天系统，请输入您的昵称：";
    private static final String NAME_EXISTS = "昵称已存在，请重新设置：";
    // 分隔符
    private static final String SPLIT_FLAG = "#@#";

    public ChatNioServer(int port) throws IOException {
        this.port = port;
        this.selector = Selector.open();
        // 服务端
        ServerSocketChannel server = ServerSocketChannel.open();
        // 非阻塞
        server.configureBlocking(false);
        // 注册多路复用
        server.register(this.selector, SelectionKey.OP_ACCEPT);
        // 绑定端口
        server.bind(new InetSocketAddress(this.port));
        System.out.println("the chat system has bean start and listen for : " + this.port);
    }

    private void listen() throws IOException {
        // 循环
        while (!Thread.interrupted()) {
            int keyNum = this.selector.select();
            if (keyNum == 0) continue;
            // 处理问题
            Set<SelectionKey> keySet = this.selector.selectedKeys();
            Iterator<SelectionKey> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                // 处理key
                process(key);
            }
        }
    }

    /**
     * 处理key的事件
     * @param key
     * @throws IOException
     */
    private void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            // 连接
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            // 客户端通道非阻塞
            client.configureBlocking(false);
            // 注册select
            client.register(this.selector, SelectionKey.OP_READ);

            // 继续监听其他客户端连接事件
            key.interestOps(SelectionKey.OP_ACCEPT);

            // 发送欢迎语句
            client.write(charset.encode(HELLO));
        }

        if (key.isReadable()) {
            // 读取客户端信息
            SocketChannel client = (SocketChannel) key.channel();
            StringBuilder content = new StringBuilder();
            while (client.read(byteBufferRead) > 0) {
                byteBufferRead.flip();
                content.append(charset.decode(byteBufferRead));
                byteBufferRead.clear();
            }
            // 分析信息
            if (content.length() > 0) {
                String[] split = content.toString().split(SPLIT_FLAG);
                if (split.length == 1) {
                    // 设置昵称
                    if (nameSet.contains(split[0])) {
                        client.write(charset.encode(NAME_EXISTS + "'" + split[0] + "'"));
                    } else {
                        // 添加昵称
                        nameSet.add(split[0]);
                        int count = onlineCount();
                        broadCast(null, "欢迎 " + split[0] + " 进入聊天系统，当前在线人数：" + count);
                    }
                } else {
                    String nickName = split[0];
                    String message = split[1];
                    broadCast(client, nickName + " 说：" + message);
                }
            }
        }
    }

    /**
     * 获得在线人数
     * @return
     */
    private int onlineCount() {
        int i = 0;
        // 获得当前多路复用器上的所有key
        for (SelectionKey key : selector.keys()) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel) {
                i ++;
            }
        }
        return i;
    }

    /**
     * 广播消息
     * @param client
     * @param content
     */
    private void broadCast(SocketChannel client, String content) throws IOException {
        for (SelectionKey key : selector.keys()) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel && channel != client) {
                SocketChannel targetChannel = (SocketChannel) channel;
                targetChannel.write(charset.encode(content));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatNioServer(8080).listen();
    }

}
