package jvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

  public static void main(String[] args) throws IOException {
    try {
      Object obj = new HelloClassLoader().findClass("Hello").newInstance();
      Method method = obj.getClass().getDeclaredMethod("hello");
      method.invoke(obj);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    File file = new File("Hello.xlass");
    int len = (int)file.length();
    byte[] bytes = new byte[len];
    try (InputStream in = new FileInputStream(file)) {
      int offset = 0;
      int n = 0;
      while ((n = in.read(bytes, offset, len - offset)) > 0) {
        offset += n;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (255 - bytes[i]);
    }
    return defineClass(name, bytes, 0, bytes.length);
  }
}
