package com.azhe.netty.buffer;

import java.nio.ByteBuffer;

/**
 * Description: 只读缓冲区
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/7 7:54 下午
 * @since V2.0.0
 */
public class ReadOnlyBufferDemo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        // 设置数据
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte)i);
        }
        // 创建只读缓冲区
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        // 每个数据 乘10
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byte b = byteBuffer.get(i);
            b *= 10;
            byteBuffer.put(i, b);
        }

        readOnlyBuffer.position(0);
        readOnlyBuffer.limit(readOnlyBuffer.capacity());
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
    }
}
