package java0.homework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo02 {

  //等待线程池被销毁
  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    AtomicInteger result = new AtomicInteger(); //这是得到的返回值

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(() -> {
      result.set(Fib.sum());
      executorService.shutdown();
    });

    while (!executorService.isTerminated()) {
    }

    System.out.println("异步计算结果为：" + result);
    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
