package com.example.http_server.homework02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpUtils {

  public static OkHttpClient client;

  // GET 调用
  public static String getAsString(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url)
        .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }


  public static void main(String[] args) throws Exception {

    // 缓存客户端实例
    client = new OkHttpClient();
    String url = "http://localhost:8801";
    String text = OkHttpUtils.getAsString(url);
    System.out.println("url: " + url + " ; response: \n" + text);

    // 清理资源
    client = null;
  }
}
