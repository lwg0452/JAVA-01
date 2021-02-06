package java0.homework;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo10 {

  //利用FutureTask
  public static void main(String[] args) {

    long start = System.currentTimeMillis();

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    FutureTask<Integer> futureTask = new FutureTask<>(
        new Callable<Integer>() {
          @Override
          public Integer call() throws Exception {
            return Fib.sum();
          }
        }
    );
    new Thread(futureTask).start();

    try {
      System.out.println("异步计算结果为：" + futureTask.get());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }
}
