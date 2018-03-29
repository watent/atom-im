package com.watent.im.handler;

import com.watent.im.processor.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * WebSocket 处理类
 *
 * @author Dylan
 * @date 2018/3/27 17:02
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    MessageProcessor messageProcessor = new MessageProcessor();

    /**
     * 接收消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //服务端与客户端 websocket 交互
//        System.out.println("\n-----WebSocketHandler------ :" + msg.text());
//        ctx.writeAndFlush(new TextWebSocketFrame("对接成功"));

        messageProcessor.messageHandler(ctx.channel(), msg.text());
    }

    /**
     * 客户端链接断开事件
     *
     * @param ctx ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        messageProcessor.logout(ctx.channel());
    }
}
