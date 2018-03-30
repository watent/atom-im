package com.watent.im.chat;

import com.watent.im.protocol.MessageObject;
import com.watent.im.protocol.MessageStatus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Scanner;

/**
 * 聊天客户端处理器
 *
 * @author Dylan
 * @date 2018/3/29 11:44
 */
public class ChatClientHandler extends ChannelHandlerAdapter {

    private Channel client;

    public ChatClientHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        client = ctx.channel();
        //登陆
        MessageObject message = new MessageObject(MessageStatus.LOGIN, System.currentTimeMillis(), "Derry");
        sendMsg(message);
        System.out.println("成功连接至服务器，已执行登录动作");
        session();
    }

    private void session() {
        new Thread() {

            @Override
            public void run() {
                System.out.println("Derry,你好，请在控制台输入消息内容");
                Scanner sc = new Scanner(System.in);
                MessageObject message;
                do {
                    String content = sc.nextLine();
                    if ("exit".equals(content)) {
                        //logout
                        message = new MessageObject(MessageStatus.LOGOUT, System.currentTimeMillis(), "Derry");
                    } else {
                        message = new MessageObject(MessageStatus.CHAT, System.currentTimeMillis(), "Derry", content);
                    }
                } while (sendMsg(message));
                sc.close();
            }

        }.start();
    }


    private boolean sendMsg(MessageObject message) {

        client.writeAndFlush(message);
        System.out.println("消息发送成功，请继续输入：");
        return true;
    }
}
