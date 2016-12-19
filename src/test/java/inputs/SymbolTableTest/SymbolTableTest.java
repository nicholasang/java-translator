package inputs.SymbolTableTest;

class A {
  int i;

  public A(int i) {
    this.i = i;
  }

  public int foo() {
    short y = 5;
    return y;
  }

  public static double bar(int x) {
    float i = (float) 5.0;
    return i;
  }

}

class B extends A {
  int x;

  public B(int i) {
    super(i);
    x = i;
  }

  private int fool() {
    return x;
  }
}

public class SymbolTableTest {
  public static void main(String[] args) {
    A[] array = new A[10];

    A x;
    for(int i = 0; i < array.length; i++) {
      x = array[i];
    }

    int k = 0;
    while(k < 10) {
      k = k + 1;
    }

    System.out.println(A.bar(5));
  }
}
