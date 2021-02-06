package java0.homework;

public class Fib {

  public static int sum() {
    return fib(36);
  }

  private static int fib(int n) {
    if (n < 2) {
      return 1;
    }
    int curr = 2, pre = 1;
    for (int i = 2; i < n; i++) {
      int temp = curr;
      curr += pre;
      pre = temp;
    }
    return curr;
  }
}