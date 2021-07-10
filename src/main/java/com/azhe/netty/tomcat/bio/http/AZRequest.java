package com.azhe.netty.tomcat.bio.http;

import java.io.IOException;
import java.io.InputStream;

public class AZRequest {

    // 请求的地址
    private String url;
    // 请求的方法类型
    private String method;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    // 拿到对应的url 和 method
    public AZRequest(InputStream is) throws IOException {
        String requestContent = "";
        // 读取数据到缓存
        byte[] buffer = new byte[1024];
        int len = 0;
        if ((len = is.read(buffer)) > 0) {
            requestContent = new String(buffer, 0, len);
        }

        String line = requestContent.split("\n")[0];
        String[] arr = line.split("\\s");
        // 请求方法
        this.method = arr[0];
        // 请求路径，去掉参数
        this.url = arr[1].split("\\?")[0];
    }

}
