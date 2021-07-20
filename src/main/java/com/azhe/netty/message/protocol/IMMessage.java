package com.azhe.netty.message.protocol;

import lombok.Data;
import org.msgpack.annotation.Message;

/**
 * 自定义 IMP 对应的java实体类
 */
@Message
@Data
public class IMMessage {

    /**
     * ip 和 端口
     */
    private String addr;

    /**
     * 命令类型
     */
    private String cmd;

    /**
     * 发送时间
     */
    private long time;

    /**
     * 当前在线人数
     */
    private int online;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 终端
     */
    private String terminal;



    @Override
    public String toString() {
        return "IMMessage{" +
                "addr='" + addr + '\'' +
                ", cmd='" + cmd + '\'' +
                ", time=" + time +
                ", online=" + online +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                ", terminal='" + terminal + '\'' +
                '}';
    }
}
