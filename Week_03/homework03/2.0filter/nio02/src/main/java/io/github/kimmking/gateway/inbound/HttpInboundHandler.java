package io.github.kimmking.gateway.inbound;

import homework03.InboundFilter;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

  private final String proxyServer;
  private OkhttpOutboundHandler handler;
  HttpRequestFilter filter;

  public HttpInboundHandler(String proxyServer) {
    this.proxyServer = proxyServer;
    handler = new OkhttpOutboundHandler(this.proxyServer);
    filter = new InboundFilter();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    System.out.println("==== channelReadComplete(ChannelHandlerContext ctx)");
    ctx.flush();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("==== channelRead(ChannelHandlerContext ctx, Object msg) ");

    if (false == (msg instanceof FullHttpRequest)) {
      return;
    }
    FullHttpRequest fullRequest = (FullHttpRequest) msg;
    try {
      filter.filter(fullRequest, ctx);
      handler.handle(fullRequest, ctx);

    } catch (Exception e) {
      System.err.println(e.getMessage());
      if (fullRequest != null) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);

        if (!HttpUtil.isKeepAlive(fullRequest)) {
          ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
          ctx.write(response);
        }
      }
      ctx.flush();
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

}

