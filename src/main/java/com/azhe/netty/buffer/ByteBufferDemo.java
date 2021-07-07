package com.azhe.netty.buffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description: buffer的三个重要属性
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/7 11:55 上午
 * @since V2.0.0
 */
public class ByteBufferDemo {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/lzr/123.txt");
        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        output("初始化", byteBuffer);

        channel.read(byteBuffer);
        output("read操作", byteBuffer);

        byteBuffer.flip();
        output("flip操作", byteBuffer);

        System.out.println("----------------------------------------------");
        while (byteBuffer.hasRemaining()) {
            System.out.print((char)byteBuffer.get());
        }
        System.out.println();
        System.out.println("----------------------------------------------");
        output("get操作", byteBuffer);

        byteBuffer.clear();
        output("clear操作", byteBuffer);

        fileInputStream.close();
        channel.close();
    }

    public static void output(String step, Buffer buffer) {
        System.out.println(step + " : ");
        // 缓存容量大小
        System.out.println("capacity : " + buffer.capacity());
        // 当前游标位置
        System.out.println("position : " + buffer.position());
        // 当前允许读取、写入的最大位置
        System.out.println("limit : " + buffer.limit());
        System.out.println();
    }

}
