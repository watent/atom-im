package com.watent.im.server;

import com.watent.im.handler.HttpHandler;
import com.watent.im.handler.SFPHandler;
import com.watent.im.handler.WebSocketHandler;
import com.watent.im.protocol.SFPDecoder;
import com.watent.im.protocol.SFPEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 服务
 *
 * @author Dylan
 * @date 2018/3/27 14:30
 */
public class ChatServer {

    public static void main(String[] args) {

        Integer port = 8080;
        new ChatServer().bind(port);
    }

    public void bind(Integer port) {

        //1.服务端接收客户端连接
        EventLoopGroup acceptorGroup = new NioEventLoopGroup();
        //2.用于进行SocketChannel的网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //Netty用于启动NIO服务器的辅助启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //将两个NIO线程组传入辅助启动类中
        bootstrap.group(acceptorGroup, workerGroup)
                //设置创建的Channel为NioServerSocketChannel类型
                .channel(NioServerSocketChannel.class)
                //配置NioServerSocketChannel的TCP参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置绑定IO事件的处理类
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast(new SFPDecoder());
                        pipeline.addLast(new SFPEncoder());
                        pipeline.addLast(new SFPHandler());

                        //支持Http协议
                        //Http请求处理的编解码器
                        pipeline.addLast(new HttpServerCodec());
                         //用于将HTTP请求进行封装为FullHttpRequest对象
                        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                        //处理文件流
                        pipeline.addLast(new ChunkedWriteHandler());
                        //Http请求的具体处理对象
                        pipeline.addLast(new HttpHandler());
                        //支持WebSocket协议
                        pipeline.addLast(new WebSocketServerProtocolHandler("im"));
                        pipeline.addLast(new WebSocketHandler());

                    }
                });
        try {
            //绑定端口，同步等待成功（sync()：同步阻塞方法，等待bind操作完成才继续）
            //ChannelFuture主要用于异步操作的通知回调
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务端启动在8080端口");
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程池资源
            acceptorGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
