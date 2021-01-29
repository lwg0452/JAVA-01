package io.github.kimmking.gateway.outbound.okhttp;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import homework03.InboundFilter;
import homework03.OkHttpUtils;
import homework03.OutboundFilter;
import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;

public class OkhttpOutboundHandler {

  private String backendUrl;
  private HttpResponseFilter filter;

  public OkhttpOutboundHandler(String backendUrl) {
    this.backendUrl =
        backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
    filter = new OutboundFilter();
  }

  public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
    String url = this.backendUrl + fullRequest.uri();
    FullHttpResponse response = null;
    try {
      //
      HttpHeaders headers = fullRequest.headers();
      //
      String body = OkHttpUtils.getResponseAsString(url);
      byte[] bytesArray = body.getBytes("UTF-8");
      response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytesArray));
      filter.filter(response);
      response.headers().set("Content-Type", "application/json");
      response.headers().set("X-proxy-tag", this.getClass().getSimpleName());
      response.headers().setInt("Content-Length", bytesArray.length);
    } catch (Throwable e) {
      e.printStackTrace();
      response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
      exceptionCaught(ctx, e);
    } finally {
      if (fullRequest != null) {
        if (null == response) {
          response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        }
        if (!HttpUtil.isKeepAlive(fullRequest)) {
          ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
          //response.headers().set(CONNECTION, KEEP_ALIVE);
          ctx.write(response);
        }
      }
      ctx.flush();
      //ctx.close();
    }
  }


  //当出现异常时打印调用栈,关闭当前连接
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

}
