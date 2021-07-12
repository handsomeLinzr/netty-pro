package com.azhe.netty.rpc.provider;

import com.azhe.netty.rpc.api.RpcCalculateService;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 5:19 下午
 * @since V2.0.0
 */
public class RpcCalculateServiceImpl implements RpcCalculateService {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int mul(int a, int b) {
        return a * b;
    }

    public int div(int a, int b) {
        return a / b;
    }
}
