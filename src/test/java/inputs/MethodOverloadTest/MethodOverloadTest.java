package inputs.MethodOverloadTest;

class A {
  public Object m(Object o1, Object o2) { return o1; }
  public A m(A a1, Object o2) { return a1; }
}

class B extends A {
  public Object m(Object o1, Object o2) { return o1; }
  public B m(B b1, Object o2) { return b1; }
  public Object m(Object o1, B a2) { return o1; }
}

// class C {
//   private long m(long i) { System.out.println("C.m(long)"); return i; }
//   int m(int i) { System.out.println("C.m(int)"); return i; }
//   void m(byte b) { System.out.println("C.m(byte)"); }
//   static void m(A o) { System.out.println("C.m(A)"); }
// }

// class D extends C {
//   void m(double d) { System.out.println("D.m(double)"); }
//   static void m(float d) { System.out.println("D.m(float)"); }
//   static void m(Object o) { System.out.println("D.m(Object)"); }
// }

class C {
  private long m(long i) { return i; }
  int m(int i) { return i; }
  void m(byte b) { }
  static void m(A o) { }
}

class D extends C {
  void m(double d) { }
  static void m(float d) { }
  static void m(Object o) { }
}

public class MethodOverloadTest {
  public static void main(String[] args) {
    A a = new A();
    Object o = new Object();
    B b = new B();
    C c = new C();
    D d = new D();
    long l = 1;
    byte y = 1;

    b.m(new A(), (Object) b);   // A.m(A, Object)
    b.m(a, o);                  // A.m(A, Object)

    b.m(b, new Object());       // B.m(B, Object)
    b.m((Object) b, b);         // B.m(Object, B)


    b.m(b, o).m(o, o);           // ((B) B.m(B, Object)).m(Object, Object)
    b.m(b.m(o, o), b.m(b, a));   // B.m((Object) B.m(Object, Object), (B) B.m(B, Object))

    d.m(l);                      // D.m(double) (can't inherit private (C.m(long) or use static D.m(float))
    c.m(y + y);                  // C.m(int) (adding -> int)
    D.m(a);                      // C.m(A)
    }
}
