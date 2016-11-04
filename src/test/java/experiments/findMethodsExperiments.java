package experiments;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;

/**
 * findMethodsTests
 *
 * @author Karl Toby Rosenberg
 */
public class findMethodsExperiments {

    public static void printSuperClassVirtualMethods(Class c) {
        System.out.println("\n---------------------------------------\n");

        LinkedHashSet<Method> methods = recursiveInheritedMethodFinder(c);

        for(Method m : methods) {
            methodDisplay(m);
        }
    }

    public static void testPrint1(Class c) {
        Method[] methods = c.getDeclaredMethods();
        for(Method m : methods) {
            methodDisplay(m);
        }
    }

    /////////////////////////////IMPORTANT
    public static LinkedHashSet<Method> recursiveInheritedMethodFinder(Class c) {
        if(c == null || c.getName().equals(Object.class.getName())) {
            return null;
        }

        LinkedHashSet<Method> set = new LinkedHashSet<Method>();
        recursiveInheritedMethodFinder(c.getSuperclass(), set);
        return set;
    }
    /////////////////////////////IMPORTANT
    private static void recursiveInheritedMethodFinder(Class c, LinkedHashSet<Method> set) {
        if(c == null || c.getName().equals(Object.class.getName())) {
            return;
        }

        recursiveInheritedMethodFinder(c.getSuperclass(), set);

        Method[] methods = c.getDeclaredMethods();
        for(Method m : methods) {
            if(Modifier.isPrivate(m.getModifiers()) || Modifier.isStatic(m.getModifiers())) {
                continue;
            }

            set.add(m);
        }
    }

    public static void methodDisplay(Method m) {
        System.out.print(Modifier.toString(m.getModifiers()) + " " +  m.getReturnType() + " " + m.getName() + " (");
        Class[] paramList = m.getParameterTypes();
        for(Class t : paramList) {
            System.out.printf("%s", t.getName());
        }
        System.out.println(")");
    }
}
