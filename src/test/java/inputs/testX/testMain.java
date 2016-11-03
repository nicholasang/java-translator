package inputs.testX;

/**
 * Created by stud on 10/12/16.
 */
public class testMain {
    public static void main(String args[]) {

        testDependency X = new testDependency();
        int x = 7;
        int y = x + 1;

        int[] testArray = {x, y};

        String s = "hi";
        Object o = null;
        System.out.println(x + y);
        System.out.println("hi" + "gh");
    }
}
