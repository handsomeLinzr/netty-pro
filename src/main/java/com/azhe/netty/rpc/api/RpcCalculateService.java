package com.azhe.netty.rpc.api;

/**
 * Description: 远程调用接口
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 5:11 下午
 * @since V2.0.0
 */
public interface RpcCalculateService {

    int add(int a, int b);

    int sub(int a, int b);

    int mul(int a, int b);

    int div(int a, int b);

}
