package com.azhe.netty.rpc.registry;

import com.azhe.netty.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryHandler extends ChannelInboundHandlerAdapter {

    private List<String> classList = new ArrayList<>(10);
    private Map<String, Object> serverMap = new ConcurrentHashMap<>(16);

    public RegistryHandler() {
        scannerClass("com.azhe.netty.rpc.provider");
        doRegistry();
    }

    /**
     * 扫描服务类，实际中是在配置文件中获取ip和端口
     * @param path
     */
    private void scannerClass(String path) {
        // 路径,经包路径形式改为文件路径形式
        URL url = this.getClass().getClassLoader().getResource(path.replaceAll("\\.", "/"));
        // 获得复文件
        File dir = new File(url.getPath());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // 文件夹，递归调用扫描
                scannerClass(path + "." + file.getName());
            } else {
                // 文件
                classList.add(path + "." + file.getName().replaceAll("\\.class", "").trim());
            }
        }
    }

    /**
     * 注册服务
     */
    private void doRegistry() {
        try {
            for (String className : classList) {
                Class<?> serverClass = Class.forName(className);
                Class<?> serverInterface = serverClass.getInterfaces()[0];
                serverMap.put(serverInterface.getName(), serverClass.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理读方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获得自定义协议
        InvokerProtocol invokerProtocol = (InvokerProtocol) msg;
        // 结果
        Object result;
        // 处理
        String className = invokerProtocol.getClassName();
        if (serverMap.containsKey(className)) {
            Object server = serverMap.get(className);
            Method method = server.getClass().getMethod(invokerProtocol.getMethodName(), invokerProtocol.getParameterTypes());
            result = method.invoke(server, invokerProtocol.getParameters());
        } else {
            result ="404 - NOT FOUND";
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
}
