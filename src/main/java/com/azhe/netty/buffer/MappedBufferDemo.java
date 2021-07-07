package com.azhe.netty.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射缓存
 */
public class MappedBufferDemo {

    private static final int start = 0;
    private static final int size = 1024;

    public static void main(String[] args) throws IOException {
        // 文件的操作
        RandomAccessFile raf = new RandomAccessFile("D://123.txt", "rw");
        FileChannel fileChannel = raf.getChannel();

        // 将channel 和 内存映射缓存关联
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, start, size);
        mappedByteBuffer.put(0, (byte)97);
        mappedByteBuffer.put(1023, (byte)122);
        raf.close();

    }

}
