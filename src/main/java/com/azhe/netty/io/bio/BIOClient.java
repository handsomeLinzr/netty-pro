package com.azhe.netty.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/6 3:50 下午
 * @since V2.0.0
 */
public class BIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8082);
        OutputStream outputStream = socket.getOutputStream();
        String name = UUID.randomUUID().toString();
        System.out.println("bioclinet要发送数据：" + name);
        outputStream.write(name.getBytes());
        outputStream.close();
        socket.close();
    }

}
