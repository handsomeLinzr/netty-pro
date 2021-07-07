package com.azhe.netty.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接缓存
 */
public class DirectBufferDemo {

    public static void main(String[] args) throws IOException {
        // 直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);

        // 源文件
        FileInputStream fin = new FileInputStream("D://123.txt");
        FileChannel finChannel = fin.getChannel();
        // 复制文件
        FileOutputStream fou = new FileOutputStream("D://123copy.txt");
        FileChannel fouChannel = fou.getChannel();
        // 读取文件的长度
        int len;
        while (true) {
            // 清除缓存
            byteBuffer.clear();
            // 将内容读到缓存中
            len = finChannel.read(byteBuffer);
            if (len == -1) {
                // 没数据，退出
                break;
            }
            byteBuffer.flip();
            fouChannel.write(byteBuffer);
        }
    }

}
