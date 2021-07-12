package com.azhe.netty.rpc.provider;

import com.azhe.netty.rpc.api.RpcHelloService;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 5:18 下午
 * @since V2.0.0
 */
public class RpcHelloServiceImpl implements RpcHelloService {
    public String hello(String name) {
        return "hello " + name;
    }
}
