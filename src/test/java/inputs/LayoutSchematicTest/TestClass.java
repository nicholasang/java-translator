package inputs.LayoutSchematicTest;


public class TestClass {

    // test access modifier and type conversion
    public int publicField;

    // test default/no access modifier (also type conversion)
    byte defaultField;

    // test static field
    static boolean staticField;

    // test multiple fields declared at once
    public int multiple = 5, declared;

    // test array type
    public int[] arrayField;


    // test access modifier and parameters
    private void privateMethod(String string, int integer) {}

    // test array type (return and parameter)
    public boolean[] mainish(String[] args) {
        return new boolean[1];
    }

    // test overriding superclass method
    public String toString() {
        return "TestClass";
    }

    // test static method and void return type
    static void staticMethod() {}


    public TestClass(int num) {
        publicField = num;
    }

    private TestClass(byte num) {
        defaultField = num;
    }

}