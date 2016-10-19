package edu.nyu.oop;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;

/**
 * findMethodsTests
 *
 * @author Karl Toby Rosenberg
 */
public class findMethodsExperiments {
    public static class PreA {
        PreA() {}

        public void test0() {
            System.out.println("PreA_0");
        }
    }
    public static class A extends PreA {
        A() {}

        public static void test1() {
            System.out.println("A_1");
        }
        public void test2() {
            System.out.println("A_2");
        }
        void test3() {
            System.out.println("A_3");
        }
        protected void test4() {
            System.out.println("A_4");
        }

        private void test5(int x) {
            System.out.println("A_5");
        }
    }

    public static class B extends A {
        B() {}

        public void test6() {
            System.out.println("B_6");
        }
    }

    public static void main(String[] args) {

        System.out.println("TESTING A");
        testPrint1(new A().getClass());
        System.out.println("TESTING B");
        testPrint1(new B().getClass());

        B b = new B();

        b.test0();

        System.out.println("\n---------------------------------------\n");
        System.out.println("print B's inherited methods from A and PreA");
        LinkedHashSet<Method> methods = recursiveInheritedMethodFinder(B.class);

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
