package java0.homework;

public class Demo07 {

  //wait和notify
  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    final int[] result = new int[1];
    Object o = new Object();

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (o) {
          result[0] = Fib.sum();
          o.notify();
        }
      }
    });
    thread.start();

    synchronized (o) {
      try {
        o.wait();
        System.out.println("异步计算结果为：" + result[0]);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
