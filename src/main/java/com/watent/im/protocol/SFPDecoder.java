package com.watent.im.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.List;

/**
 * 自定义解码
 *
 * @author Dylan
 * @date 2018/3/29 11:53
 */
public class SFPDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            //解码反序列化
            int length = in.readableBytes();
            byte[] arr = new byte[length];
            // TODO ??
//            MessageObject test = new MessagePack().read(in.array(), MessageObject.class);
//            System.out.println(test);
//            String content = new String(in.array(), in.readerIndex(), length);
//            String content = new String(arr, in.readerIndex(), length);

//            if (!"".equals(content.trim())) {
//                if (!MessageStatus.isSFP(content)) {
//                    ctx.channel().pipeline().remove(this);
//                    return;
//                }
//            }
            in.getBytes(in.readerIndex(), arr, 0, length);
            MessageObject messageObject = new MessagePack().read(arr, MessageObject.class);

            if (!MessageStatus.isSFP("[" + messageObject.getCmd() + "]")) {
                ctx.channel().pipeline().remove(this);
                return;
            }

            out.add(messageObject);
            in.clear();
        } catch (IOException e) {
            e.printStackTrace();
            ctx.channel().pipeline().remove(this);
        }
    }

}
