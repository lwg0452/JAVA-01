package java0.homework;

import java.util.concurrent.Semaphore;

public class Demo06 {

  //使用信号量同步
  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    Semaphore semaphore = new Semaphore(0);
    final int[] result = new int[1];

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    new Thread(new Runnable() {
      @Override
      public void run() {
        result[0] = Fib.sum();
        semaphore.release();
      }
    }).start();

    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("异步计算结果为：" + result[0]);
    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
