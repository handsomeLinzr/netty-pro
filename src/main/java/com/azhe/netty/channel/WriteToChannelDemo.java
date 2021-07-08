package com.azhe.netty.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 写到缓存
 */
public class WriteToChannelDemo {

    static final byte[] MESSAGE = {83, 111, 109, 101, 32};

    public static void main(String[] args) throws IOException {
        // 获得文件输出通道
        FileOutputStream fileOutputStream = new FileOutputStream("D://456.txt");
        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (byte b : MESSAGE) {
            byteBuffer.put(b);
        }

        // 翻转
        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }

}
