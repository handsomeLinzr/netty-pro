package com.azhe.netty.tomcat.netty.http;

import java.io.IOException;

public abstract class AZServlet {

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
