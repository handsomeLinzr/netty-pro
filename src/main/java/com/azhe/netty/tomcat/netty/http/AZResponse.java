package com.azhe.netty.tomcat.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public class AZResponse {

    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public AZResponse(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    /**
     * 输出数据
     * @param content
     */
    public void write(String content) throws Exception {
        if (content == null || "".equals(content)) {
            return;
        }
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));
        ctx.write(fullHttpResponse);

        ctx.flush();
        ctx.close();
    }

}
