package com.azhe.netty.tomcat.bio;

import com.azhe.netty.tomcat.bio.http.AZRequest;
import com.azhe.netty.tomcat.bio.http.AZResponse;
import com.azhe.netty.tomcat.bio.http.AZServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * BIO 编写的简单 tomcat
 */
public class AZTomcat {

    // 端口
    private int port;
    // 服务socket
    private ServerSocket serverSocket;
    // servlet 对应的mapper
    private Map<String, AZServlet> servletMap = new HashMap<>(16);

    /**
     * 启动方法：
     * 1.初始化
     * 2.监听
     */
    private void start() {
        init();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("正在监听端口：" + port);

            while (true) {
                Socket client = serverSocket.accept();
                process(client);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            Properties webXml = new Properties();
            // 根路径
            String rootPath = this.getClass().getResource("/").getPath();
            FileInputStream fileInputStream = new FileInputStream(rootPath + "bio-web.properties");
            // 加载配置
            webXml.load(fileInputStream);

            Set<Object> keySet = webXml.keySet();
            for (Object k : keySet) {
                String key = k.toString();
                if ("server.port".equals(key)) {
                    // 端口
                    this.port = Integer.parseInt(webXml.getProperty(key));
                } else if (key.endsWith(".url")) {
                    // servlet服务
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName + ".className");
                    AZServlet servlet = (AZServlet) Class.forName(className).newInstance();
                    servletMap.put(url, servlet);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理请求
     * @param socket
     * @throws IOException
     */
    private void process(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        // 封装 request 和 response
        AZRequest request = new AZRequest(is);
        AZResponse response = new AZResponse(os);

        String url = request.getUrl();
        if (servletMap.containsKey(url)) {
            // 调用服务处理
            servletMap.get(url).service(request, response);
        } else {
            // 返回404
            response.write("404-NOT FOUND");
        }

        os.flush();
        os.close();
        is.close();
        socket.close();
    }

    public static void main(String[] args) {
        new AZTomcat().start();
    }

}
