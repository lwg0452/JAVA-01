package homework03;

import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.netty.handler.codec.http.FullHttpResponse;

// 在响应的 Header 中添加 filter:OutboundFilter
public class OutboundFilter implements HttpResponseFilter {

  @Override
  public void filter(FullHttpResponse response) {
    response.headers().set("filter", "OutboundFilter");
  }
}
