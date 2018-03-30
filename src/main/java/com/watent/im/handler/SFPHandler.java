package com.watent.im.handler;

import com.watent.im.processor.MessageProcessor;
import com.watent.im.protocol.MessageObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 自定义协议处理器
 *
 * @author Dylan
 * @date 2018/3/29 15:51
 */
public class SFPHandler extends SimpleChannelInboundHandler<MessageObject> {

    private MessageProcessor processor = new MessageProcessor();

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageObject msg) throws Exception {
        processor.messageHandler(ctx.channel(), msg);
    }
}
