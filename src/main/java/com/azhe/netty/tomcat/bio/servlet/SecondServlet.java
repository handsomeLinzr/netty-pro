package com.azhe.netty.tomcat.bio.servlet;

import com.azhe.netty.tomcat.bio.http.AZRequest;
import com.azhe.netty.tomcat.bio.http.AZResponse;
import com.azhe.netty.tomcat.bio.http.AZServlet;

import java.io.IOException;

public class SecondServlet extends AZServlet {
    @Override
    public void get(AZRequest request, AZResponse response) throws IOException {
        post(request, response);
    }

    @Override
    public void post(AZRequest request, AZResponse response) throws IOException {
        response.write("this is the second servlet");
    }
}
