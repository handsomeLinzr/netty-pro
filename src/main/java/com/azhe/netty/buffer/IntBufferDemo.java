package com.azhe.netty.buffer;

import java.nio.IntBuffer;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/7 11:10 上午
 * @since V2.0.0
 */
public class IntBufferDemo {

    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(8);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(2 * (i+1));
        }
        // 反转，限制的最高位置=当前位置，当前位置=0
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            // 还有数据，即当前位置 < 限制最高位置
            System.out.print(intBuffer.get() + " ");
        }
    }

}
