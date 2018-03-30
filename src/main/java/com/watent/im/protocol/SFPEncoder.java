package com.watent.im.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 自定义编码
 *
 * @author Dylan
 * @date 2018/3/29 11:52
 */
public class SFPEncoder extends MessageToByteEncoder<MessageObject> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageObject msg, ByteBuf out) throws Exception {
        // MessagePack 序列化工具
        out.writeBytes(new MessagePack().write(msg));
    }
}
