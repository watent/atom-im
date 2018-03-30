package com.watent.im.chat;

import com.watent.im.protocol.SFPDecoder;
import com.watent.im.protocol.SFPEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 *
 * @author Dylan
 * @date 2018/3/29 11:43
 */
public class ChatClient {

    public static void main(String[] args) throws Exception {
        int port = 8080; //服务端默认端口
        new ChatClient().connect(port, "localhost");
    }

    public void connect(int port, String host) throws Exception {
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
                        protected void initChannel(SocketChannel channel) throws Exception {

                            channel.pipeline().addLast(new SFPDecoder());
                            channel.pipeline().addLast(new SFPEncoder());
                            channel.pipeline().addLast(new ChatClientHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture cf = bs.connect(host, port).sync();
            //等待客户端链路关闭
            cf.channel().closeFuture().sync();
        } finally {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
