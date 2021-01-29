package homework03;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InboundFilter implements HttpRequestFilter {

  public static InboundFilter newInstance() {
    return new InboundFilter();
  }

  // 只转发 GET 方法,url 为 http://127.0.0.1/test,有请求参数 net_gate 的请求
  //转发时删除请求的参数,并且修改请求头
  @Override
  public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
    //过滤非 GET 方法的请求
    String method = fullRequest.method().name();
    if (!method.equals("GET")) {
      throw new RuntimeException("非法请求方法:" + method);
    }

    //过滤 URL 不是 http://127.0.0.1/test 的请求
    String uri = fullRequest.uri();
    System.out.println(uri);
    String pattern = "^/test?.*";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(uri);
    if (!m.find()) {
      throw new RuntimeException("非法请求:" + uri);
    }

    Map<String, String> paramMap = new ConcurrentHashMap<>();
    //获取请求参数,存入paramMap
    QueryStringDecoder decoder = new QueryStringDecoder(fullRequest.uri());
    decoder.parameters().entrySet().forEach(entry -> {
      paramMap.put(entry.getKey(), entry.getValue().get(0));
    });

    //过滤缺少 net_gate 参数的请求
    if (!paramMap.containsKey("net_gate")) {
      throw new RuntimeException("缺少参数 net_gate");
    }

    //修改url
    fullRequest.setUri("/test");

    //修改头部
    fullRequest.headers().set("net_gate", paramMap.get("net_gate"));
  }

}
