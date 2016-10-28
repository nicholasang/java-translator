package inputs.testABCD;


public class TestABCD {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }
}

class D {

    void allByMyself() {
        System.out.println("-don't wanna be all by myself anymore.");
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

    public static String[] Love() {
        return new String[] {"is", "all", "you", "need"};
    }

}
