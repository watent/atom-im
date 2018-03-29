package com.watent.im.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Http 处理类
 * 请求 封装 响应
 *
 * @author Dylan
 * @date 2018/3/27 15:10
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        System.out.println(msg);
        //处理客户端 http 请求
        String uri = msg.getUri();
        String source = uri.equals("/") ? "chat.html" : uri;
        //NIO文件传输 拿到资源文件
        RandomAccessFile file;
        try {
            file = new RandomAccessFile(getSource(source), "r");
        } catch (Exception e) {
            //继续下一次请求
            ctx.fireChannelRead(msg.retain());
            return;
        }

        //将资源响应给客户端
        HttpResponse response = new DefaultHttpResponse(msg.getProtocolVersion(), HttpResponseStatus.OK);

        String contentType = "text/html";
        if (uri.endsWith(".js")) {
            contentType = "text/javascript";
        } else if (uri.endsWith(".css")) {
            contentType = "text/css";
        } else if (uri.toLowerCase().matches("(jpg|png|gif|ico)$")) {
            String type = uri.substring(uri.lastIndexOf("."));
            contentType = "image/" + type;
        }

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, contentType + ";charset=utf-8");
        boolean keepAlive = HttpHeaders.isKeepAlive(msg);
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        //向客户端响应消息头
        ctx.write(response);
        //向客户端响应消息体
        ctx.write(new ChunkedNioFile(file.getChannel()));
        //响应结束 向客户端响应Http结束标记
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        //收尾
        file.close();
    }

    private String getSource(String source) throws URISyntaxException {
        //class 文件地址
        URL location = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
        String webRoot = "template";
        String path = location.toURI() + webRoot + "/" + source;
        path = path.replace("file:", "");
        System.out.println("path:" + path);
        return path;
    }


}
