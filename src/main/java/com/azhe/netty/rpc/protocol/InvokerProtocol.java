package com.azhe.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: 自定义传输协议
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2021/7/12 5:14 下午
 * @since V2.0.0
 */
@Data
public class InvokerProtocol implements Serializable {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数数组
     */
    private Object[] parameters;

}
