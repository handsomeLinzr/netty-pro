package com.azhe.netty.tomcat.bio.http;

import java.io.IOException;

/**
 * 自定义Servlet
 */
public abstract class AZServlet {

    /**
     * 处理请求
     * @param request
     * @param response
     */
    public void service(AZRequest request, AZResponse response) throws IOException {
        if ("get".equalsIgnoreCase(request.getMethod())) {
            get(request, response);
        } else {
            post(request, response);
        }
    }

    public abstract void get(AZRequest request, AZResponse response) throws IOException;

    public abstract void post(AZRequest request, AZResponse response) throws IOException;

}
