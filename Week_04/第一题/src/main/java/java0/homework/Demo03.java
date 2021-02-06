package java0.homework;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Demo03 {

  //利用Future
  public static void main(String[] args) {

    long start = System.currentTimeMillis();

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Integer> future = executorService.submit(Fib::sum);

    int result;
    try {
      result = future.get();
      executorService.shutdown();
      System.out.println("异步计算结果为：" + result);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
