package java0.homework;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo08 {

  //使用锁
  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    int[] result = new int[1];

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();
        result[0] = Fib.sum();
        condition.signal();
        lock.unlock();
      }
    });

    thread.start();

    lock.lock();
    try {
      condition.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("异步计算结果为：" + result[0]);
    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
