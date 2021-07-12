package com.azhe.netty.rpc.consumer;

import com.azhe.netty.rpc.api.RpcCalculateService;
import com.azhe.netty.rpc.api.RpcHelloService;
import com.azhe.netty.rpc.provider.RpcCalculateServiceImpl;
import com.azhe.netty.rpc.provider.RpcHelloServiceImpl;

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
        RpcHelloService helloService = new RpcHelloServiceImpl();
        System.out.println(helloService.hello("呵呵哒"));

        RpcCalculateService calculateService = new RpcCalculateServiceImpl();
        System.out.println(calculateService.add(1,2));
        System.out.println(calculateService.sub(3,4));
        System.out.println(calculateService.mul(5,6));
        System.out.println(calculateService.div(20,2));
    }

}
