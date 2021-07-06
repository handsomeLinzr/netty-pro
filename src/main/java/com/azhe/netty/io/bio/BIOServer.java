package com.azhe.netty.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/6 3:49 下午
 * @since V2.0.0
 */
public class BIOServer {

    private ServerSocket serverSocket;

    public BIOServer(int port) throws IOException {
       serverSocket = new ServerSocket(port);
    }

    public void listen() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            // 创建缓冲区
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            if (len > 0) {
                System.out.println("收到client端的数据：" + new String(buffer, 0, len));
            }
            inputStream.close();
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        new BIOServer(8081).listen();
    }

}
