package com.azhe.netty.tomcat.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class AZRequest {

    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public AZRequest(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    /**
     * 获得方法
     * @return
     */
    public String getMethod() {
        return request.getMethod().name();
    }

    /**
     * 获得url路径
     * @return
     */
    public String getUrl() {
        return request.getUri();
    }

    /**
     * 获得参数
     * @param name
     * @return
     */
    public String getParameter(String name) {
        Map<String, List<String>> parameters = getParameters();
        if (parameters.containsKey(name)) {
            return parameters.get(name).get(0);
        }
        return null;
    }

    private Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        return decoder.parameters();
    }

}
