package com.azhe.netty.tomcat.bio.http;

import java.io.IOException;
import java.io.OutputStream;

public class AZResponse {

    private OutputStream os;

    public AZResponse(OutputStream os) {
        this.os = os;
    }

    /**
     * 输出
     * @param content
     */
    public void write(String content) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(content);
        System.out.println(stringBuilder.toString());
        os.write(stringBuilder.toString().getBytes());
    }

}
