package com.watent.im.processor;

import com.watent.im.protocol.MessageCodec;
import com.watent.im.protocol.MessageObject;
import com.watent.im.protocol.MessageStatus;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 消息处理
 *
 * @author Dylan
 * @date 2018/3/27 18:04
 */
public class MessageProcessor {

    //用于记录/管理所有客户端的Channel
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private MessageCodec codec = new MessageCodec();

    //设置一些Channel的属性
    private AttributeKey<String> nickname = AttributeKey.valueOf("nickname");

    public void messageHandler(Channel client, String message) {
        System.out.println("客户端发送的指令:" + message);

        if (null == message || "".equals(message)) {
            return;
        }
        MessageObject msgObj = codec.decoder(message);
        if (msgObj.getCmd().equals(MessageStatus.LOGIN)) {
            //为Channel绑定昵称属性
            client.attr(nickname).set(msgObj.getNickname());

            //将用户的channel添加到 channelGroup 中
            users.add(client);
            //将用户登陆消息发送给所有用户
            for (Channel channel : users) {
                //封装一个System的消息对象
                if (channel == client) {
                    msgObj = new MessageObject(MessageStatus.SYSTEM, System.currentTimeMillis(), msgObj.getNickname(), "已经与服务器建立连接", users.size());
                } else {
                    msgObj = new MessageObject(MessageStatus.SYSTEM, System.currentTimeMillis(), msgObj.getNickname(), msgObj.getNickname() + "加入了聊天室", users.size());
                }
                //将消息发送给所有客户端
                channel.writeAndFlush(new TextWebSocketFrame(codec.encoder(msgObj)));
            }
        } else if (msgObj.getCmd().equals(MessageStatus.CHAT)) {
            for (Channel channel : users) {
                if (channel == client) {
                    //发送给自己
                    msgObj.setNickname("SELF");
                } else {
                    msgObj.setNickname(client.attr(nickname).get());
                }
                //重新编码
                String content = codec.encoder(msgObj);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }

        users.add(client);
        System.out.println("当前在线人数:" + users.size());
    }

    public void logout(Channel client) {

        users.remove(client);
        //获得客户的绑定的昵称
        String userName = client.attr(nickname).get();
        if (userName != null && !userName.equals("")) {
            MessageObject messageObject = new MessageObject(MessageStatus.SYSTEM, System.currentTimeMillis(), null, userName + "退出了聊天室", users.size());
            String content = codec.encoder(messageObject);
            for (Channel channel : users) {
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
    }
}
