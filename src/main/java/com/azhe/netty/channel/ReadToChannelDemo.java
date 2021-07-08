package com.azhe.netty.channel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读到缓存
 */
public class ReadToChannelDemo {

    public static void main(String[] args) throws IOException {
        FileInputStream fin = new FileInputStream("D://123.txt");
        // 获得文件通道
        FileChannel channel = fin.getChannel();

        // 缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = channel.read(byteBuffer);
        if (len > 0) {
            // 读之前翻转
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.print((char)byteBuffer.get());
            }
            System.out.println();
        }
        channel.close();
        fin.close();
    }

}
