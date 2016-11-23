#pragma once
#include "java_lang.h"
#include <stdint.h>
#include <string>

using namespace java::lang;

namespace inputs
{
namespace testABCD
{
struct __D;
struct __D_VT;
typedef __D* D;

struct __A;
struct __A_VT;
typedef __A* A;

struct __C;
struct __C_VT;
typedef __C* C;

struct __B;
struct __B_VT;
typedef __B* B;


struct __D
{
    __D_VT* __vptr;
    static __D_VT __vtable;

    __D();

    static Class __class();
    static void allByMyself(D);
    static void __init();
};

struct __D_VT
{
    Class __isa;
    int32_t (*hashCode) (D);
    bool (*equals) (D, Object);
    Class (*getClass) (D);
    String (*toString) (D);
    void (*allByMyself) (D);

    __D_VT():
        __isa((Class)__D::__class()),
        hashCode((int32_t (*) (D))&__Object::hashCode),
        equals((bool (*) (D, Object))&__Object::equals),
        getClass((Class (*) (D))&__Object::getClass),
        toString((String (*) (D))&__Object::toString),
        allByMyself((void (*) (D))&__D::allByMyself)
    {
    }
};

struct __A
{
    __A_VT* __vptr;
    static __A_VT __vtable;
    String publicDataA;
protected:
    String protectedDataA;
private:
    String privateDataA;
public:
    String staticDataA;

    __A();

    static Class __class();
    static String getArbitraryData(A);
    static String Love(A);
    static void __init();
    static void __init(String);
};

struct __A_VT
{
    Class __isa;
    int32_t (*hashCode) (A);
    bool (*equals) (A, Object);
    Class (*getClass) (A);
    String (*toString) (A);
    String (*getArbitraryData) (A);

    __A_VT():
        __isa((Class)__A::__class()),
        hashCode((int32_t (*) (A))&__Object::hashCode),
        equals((bool (*) (A, Object))&__Object::equals),
        getClass((Class (*) (A))&__Object::getClass),
        toString((String (*) (A))&__Object::toString),
        getArbitraryData((String (*) (A))&__A::getArbitraryData)
    {
    }
};

struct __C
{
    __C_VT* __vptr;
    static __C_VT __vtable;
    String publicDataA;
protected:
    String protectedDataA;
private:
    String privateDataA;

public:
    __C();

    static Class __class();
    static int32_t add(C, int32_t, int32_t);
    static void __init();
};

struct __C_VT
{
    Class __isa;
    int32_t (*hashCode) (C);
    bool (*equals) (C, Object);
    Class (*getClass) (C);
    String (*toString) (C);
    String (*getArbitraryData) (C);
    int32_t (*add) (C, int32_t, int32_t);

    __C_VT():
        __isa((Class)__C::__class()),
        hashCode((int32_t (*) (C))&__Object::hashCode),
        equals((bool (*) (C, Object))&__Object::equals),
        getClass((Class (*) (C))&__Object::getClass),
        toString((String (*) (C))&__Object::toString),
        getArbitraryData((String (*) (C))&__A::getArbitraryData),
        add((int32_t (*) (C, int32_t, int32_t))&__C::add)
    {
    }
};

struct __B
{
    __B_VT* __vptr;
    static __B_VT __vtable;
private:
    String privateDataB;
public:
    String publicDataA;
protected:
    String protectedDataA;
private:
    String privateDataA;

public:
    __B();

    static Class __class();
    static String getArbitraryData(B);
    static void __init();
};

struct __B_VT
{
    Class __isa;
    int32_t (*hashCode) (B);
    bool (*equals) (B, Object);
    Class (*getClass) (B);
    String (*toString) (B);
    String (*getArbitraryData) (B);

    __B_VT():
        __isa((Class)__B::__class()),
        hashCode((int32_t (*) (B))&__Object::hashCode),
        equals((bool (*) (B, Object))&__Object::equals),
        getClass((Class (*) (B))&__Object::getClass),
        toString((String (*) (B))&__Object::toString),
        getArbitraryData((String (*) (B))&__B::getArbitraryData)
    {
    }
};

}
}
