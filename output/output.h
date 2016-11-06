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
    static void allByMyself();
};

struct __D_VT
{
    Class __isa;
    int32_t (*hashCode) (__D);
    bool (*equals) (__D, Object);
    Class (*getClass) (__D);
    String (*toString) (__D);
    void (*allByMyself) (__D);

    __D_VT():
        __isa((Class)__D::__class()),
        hashCode((int32_t (*) (__D))&__Object::hashCode),
        equals((bool (*) (__D, Object))&__Object::equals),
        getClass((Class (*) (__D))&__Object::getClass),
        toString((String (*) (__D))&__Object::toString),
        allByMyself((void (*) (__D))&__D::allByMyself)
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


    __A(String item);

    static Class __class();
    static String getArbitraryData();
    static String Love();
};

struct __A_VT
{
    Class __isa;
    int32_t (*hashCode) (__A);
    bool (*equals) (__A, Object);
    Class (*getClass) (__A);
    String (*toString) (__A);
    String (*getArbitraryData) (__A);

    __A_VT():
        __isa((Class)__A::__class()),
        hashCode((int32_t (*) (__A))&__Object::hashCode),
        equals((bool (*) (__A, Object))&__Object::equals),
        getClass((Class (*) (__A))&__Object::getClass),
        toString((String (*) (__A))&__Object::toString),
        getArbitraryData((String (*) (__A))&__A::getArbitraryData)
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
    static int32_t add(int32_t, int32_t);
};

struct __C_VT
{
    Class __isa;
    int32_t (*hashCode) (__C);
    bool (*equals) (__C, Object);
    Class (*getClass) (__C);
    String (*toString) (__C);
    String (*getArbitraryData) (__C);
    int32_t (*add) (__C, int32_t, int32_t);

    __C_VT():
        __isa((Class)__C::__class()),
        hashCode((int32_t (*) (__C))&__Object::hashCode),
        equals((bool (*) (__C, Object))&__Object::equals),
        getClass((Class (*) (__C))&__Object::getClass),
        toString((String (*) (__C))&__Object::toString),
        getArbitraryData((String (*) (__C))&__A::getArbitraryData),
        add((int32_t (*) (__C, int32_t, int32_t))&__C::add)
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
    static String getArbitraryData();
};

struct __B_VT
{
    Class __isa;
    int32_t (*hashCode) (__B);
    bool (*equals) (__B, Object);
    Class (*getClass) (__B);
    String (*toString) (__B);
    String (*getArbitraryData) (__B);

    __B_VT():
        __isa((Class)__B::__class()),
        hashCode((int32_t (*) (__B))&__Object::hashCode),
        equals((bool (*) (__B, Object))&__Object::equals),
        getClass((Class (*) (__B))&__Object::getClass),
        toString((String (*) (__B))&__Object::toString),
        getArbitraryData((String (*) (__B))&__B::getArbitraryData)
    {
    }
};

}
}
