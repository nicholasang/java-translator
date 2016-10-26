package inputs.testABCDSimple;


public class TestABCDSimple {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}

class A {
    public String publicDataA = "publicDataA";
    protected String protectedDataA = "protectedDataA";
    private String privateDataA = "privateDataA";
    static String staticDataA = "staticDataA";

    public String getArbitraryData() {
        return this.publicDataA;
    }

    public static String[] notSharingWithChildren() {
        return new String[] {"Ever", "ever", "EVER"};
    }

}

class B extends A {
    private String privateDataB = "dataB";

    @Override
    public String getArbitraryData() {
        return this.privateDataB;
    }

}

class C extends A {



}

class D {

    void allByMyself() {
        System.out.println("-don't wanna be all by myself anymore.");
    }
}
