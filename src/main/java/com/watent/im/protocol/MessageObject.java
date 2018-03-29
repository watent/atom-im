package com.watent.im.protocol;

/**
 * 消息对象封装
 *
 * @author Dylan
 * @date 2018/3/28 09:26
 */
public class MessageObject {

    /**
     * 消息指令
     * LOGIN\LOGOUT\CHAT\SYSTEM
     */
    private String cmd;
    /**
     * 时间戳
     */
    private Long time;
    /**
     * 发送人
     */
    private String nickname;
    /**
     * 消息体
     */
    private String content;
    /**
     * 在线人数
     */
    private int online;


    public MessageObject(String cmd, long time, String nickName) {
        super();
        this.cmd = cmd;
        this.time = time;
        this.nickname = nickName;
    }

    public MessageObject(String cmd, long time, String nickName, String content) {
        super();
        this.cmd = cmd;
        this.time = time;
        this.nickname = nickName;
        this.content = content;
    }

    public MessageObject(String cmd, Long time, String nickname, String content, int online) {
        this.cmd = cmd;
        this.time = time;
        this.nickname = nickname;
        this.content = content;
        this.online = online;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
