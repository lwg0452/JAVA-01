package java0.nio01.netty;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

  public static OkHttpClient client = new OkHttpClient();

  // GET 调用
  public static String getResponseAsString(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url)
        .build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

}
