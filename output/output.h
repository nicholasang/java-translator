#pragma once
#include "java_lang.h"
#include <stdint.h>
#include <string>

using namespace java::lang;

namespace inputs {
namespace test005 {
struct __A;
struct __A_VT;
typedef __A* A;

struct __B;
struct __B_VT;
typedef __B* B;


struct __A {
__A_VT* __vptr;
static __A_VT __vtable;

__A();

static Class __class();
static String toString(A);
static void __init();
};

struct __A_VT {
Class __isa;
int32_t (*hashCode) (A);
bool (*equals) (A, Object);
Class (*getClass) (A);
String (*toString) (A);

__A_VT():
__isa((Class)__A::__class()),
 hashCode((int32_t (*) (A))&__Object::hashCode),
 equals((bool (*) (A, Object))&__Object::equals),
 getClass((Class (*) (A))&__Object::getClass),
 toString((String (*) (A))&__A::toString)
{
}
};

struct __B {
__B_VT* __vptr;
static __B_VT __vtable;

__B();

static Class __class();
static String toString(B);
static void __init();
};

struct __B_VT {
Class __isa;
int32_t (*hashCode) (B);
bool (*equals) (B, Object);
Class (*getClass) (B);
String (*toString) (B);

__B_VT():
__isa((Class)__B::__class()),
 hashCode((int32_t (*) (B))&__Object::hashCode),
 equals((bool (*) (B, Object))&__Object::equals),
 getClass((Class (*) (B))&__Object::getClass),
 toString((String (*) (B))&__B::toString)
{
}
};

}
}
