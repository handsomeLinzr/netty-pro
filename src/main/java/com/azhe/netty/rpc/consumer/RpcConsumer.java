package com.azhe.netty.rpc.consumer;

import com.azhe.netty.rpc.api.RpcCalculateService;
import com.azhe.netty.rpc.api.RpcHelloService;
import com.azhe.netty.rpc.registry.RpcProxy;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 5:21 下午
 * @since V2.0.0
 */
public class RpcConsumer {

    public static void main(String[] args) {
        RpcHelloService helloService = RpcProxy.getProxy(RpcHelloService.class, 8080);
        System.out.println(helloService.hello("阿哲"));

        RpcCalculateService calculateService = RpcProxy.getProxy(RpcCalculateService.class, 8080);
        System.out.println(calculateService.add(1,2));
        System.out.println(calculateService.sub(3,4));
        System.out.println(calculateService.mul(5,6));
        System.out.println(calculateService.div(20,10));
    }

}
