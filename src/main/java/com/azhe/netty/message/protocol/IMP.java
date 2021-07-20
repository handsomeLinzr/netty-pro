package com.azhe.netty.message.protocol;

import lombok.Getter;

/**
 * 自定义的IM通信协议  Instant Messaging Protocol 即时通信协议
 *
 * eg:：[SYSTEM][124343423123][Tom老师] – Student加入聊天室
 *      [CHAT][124343423123][Tom老师][ALL] - 大家好，我是Tom老师！
 */
@Getter
public enum IMP {

    /**
     * 系统消息
     */
    SYSTEM("SYSTEM"),

    /**
     * 登录消息
     */
    LOGIN("LOGIN"),

    /**
     * 登出消息
     */
    LOGOUT("LOGOUT"),

    /**
     * 聊天消息
     */
    CHAT("CHAT"),

    /**
     * 花
     */
    FLOWER("FLOWER");

    /**
     * 协议名称
     */
    private final String name;

    IMP(String name) {
        this.name = name;
    }
}
