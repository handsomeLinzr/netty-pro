package com.azhe.netty.tomcat.netty.servlet;

import com.azhe.netty.tomcat.netty.http.AZRequest;
import com.azhe.netty.tomcat.netty.http.AZResponse;
import com.azhe.netty.tomcat.netty.http.AZServlet;

import java.io.IOException;

public class FirstServlet extends AZServlet {
    @Override
    public void get(AZRequest request, AZResponse response) throws IOException {
        post(request, response);
    }

    @Override
    public void post(AZRequest request, AZResponse response) throws IOException {
        try {
            response.write("the first servlet from netty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
