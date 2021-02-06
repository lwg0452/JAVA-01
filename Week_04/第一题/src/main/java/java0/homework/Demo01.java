package java0.homework;

public class Demo01 {

  //在主线程中调用thread.join()同步
  public static void main(String[] args) {

    long start = System.currentTimeMillis();
    final int[] result = new int[1]; //存放返回值

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        result[0] = Fib.sum();
      }
    });

    thread.start();

    try {
      thread.join(); //等待thread运行结束
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("异步计算结果为：" + result[0]);
    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
  }

}
