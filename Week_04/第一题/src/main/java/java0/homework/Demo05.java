package java0.homework;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//使用阻塞队列
public class Demo05 {

  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(1);

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    new Thread(new Runnable() {
      @Override
      public void run() {
        blockingQueue.offer(Fib.sum());
      }
    }).start();

    try {
      System.out.println("异步计算结果为：" + blockingQueue.take());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
