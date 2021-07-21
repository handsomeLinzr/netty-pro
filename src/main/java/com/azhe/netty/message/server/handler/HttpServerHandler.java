package com.azhe.netty.message.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.net.URL;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/21 1:52 下午
 * @since V2.0.0
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    // 根路径
    private URL baseURL = HttpServerHandler.class.getResource("");
    private final String webroot = "webroot";

    private String getResource(String page) throws Exception {
        String basePath = baseURL.toURI().toString();
        int start = basePath.indexOf("classes/");
        basePath = (basePath.substring(0, start) + "/" + "classes/").replace("/+", "/");
        String path = basePath + webroot + "/" + page;

        return null;
    }
    /**
     * http 请求
     * @param ctx 处理器
     * @param request 请求
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 获得请求uri
        String uri = request.getUri();
        RandomAccessFile file = null;

        try {
            String page = uri.equals("/")?"chat.html":uri;
            // 获得文件
            file = new RandomAccessFile(getResource(page), "r");
        } catch (Exception e) {
            // 向下传递
            ctx.fireChannelRead(request.retain());
            return;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        log.info("Client" + channel.remoteAddress() + "出异常！");
        cause.printStackTrace();
        channel.close();
    }
}
