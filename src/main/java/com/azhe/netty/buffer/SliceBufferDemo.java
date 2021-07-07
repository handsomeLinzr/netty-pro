package com.azhe.netty.buffer;

import java.nio.ByteBuffer;

/**
 * Description: 分片缓冲区
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/7 12:31 下午
 * @since V2.0.0
 */
public class SliceBufferDemo {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            // 存放数据
            byteBuffer.put((byte) i);
        }

        // 截取分片缓存区
        byteBuffer.position(4);
        byteBuffer.limit(7);
        ByteBuffer slice = byteBuffer.slice();
        for (int i = 0; i < slice.capacity(); i++) {
            // 遍历分片数据，都 乘10
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }

        // 还原，准备读
        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }
    }

}
