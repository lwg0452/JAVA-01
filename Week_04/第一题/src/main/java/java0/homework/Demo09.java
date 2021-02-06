package java0.homework;

public class Demo09 {

  //主线程sleep足够长的时间
  public static void main(String[] args) {

    long start=System.currentTimeMillis();
    int[] result = new int[1];

    // 在这里创建一个线程或线程池，异步执行 Fib.sum() 方法
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        result[0] = Fib.sum();
      }
    });

    thread.start();

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("异步计算结果为："+result[0]);
    System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
  }
}
