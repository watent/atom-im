package com.watent.im.protocol;

/**
 * 消息状态常量
 *
 * @author Dylan
 * @date 2018/3/29 10:19
 */
public class MessageStatus {

    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String CHAT = "CHAT";
    public static final String SYSTEM = "SYSTEM";

    public static boolean isSFP(String msg) {
        return msg.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT)\\]");
    }
}
