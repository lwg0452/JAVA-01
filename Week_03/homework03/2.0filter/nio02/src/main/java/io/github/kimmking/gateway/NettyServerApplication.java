package io.github.kimmking.gateway;


import io.github.kimmking.gateway.inbound.HttpInboundServer;

public class NettyServerApplication {

  public final static String GATEWAY_NAME = "NIOGateway";
  public final static String GATEWAY_VERSION = "2.0.0";

  public static void main(String[] args) {

    String proxyPort = System.getProperty("proxyPort", "8888");

    // 这是之前的单个后端url的例子
    String proxyServer = System.getProperty("proxyServer", "http://localhost:8803");
    //  "http://localhost:8888/test?net_gate=11111"  ==> gateway API 成功转发
    //  http://localhost:8888/test 转发失败
    //  http://localhost:8888/aaa/bbb 转发失败
    //  http://localhost:8803  ==> backend service
    int port = Integer.parseInt(proxyPort);
    System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " starting...");
    HttpInboundServer server = new HttpInboundServer(port, proxyServer);
    System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " started at http://localhost:" + port
        + " for server:" + server.toString());
    try {
      server.run();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}