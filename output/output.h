#pragma once
#include "java_lang.h"
#include <stdint.h>
#include <string>

using namespace java::lang;

namespace inputs {
namespace test050 {
struct __A;
struct __A_VT;
typedef __A* A;

struct __B;
struct __B_VT;
typedef __B* B;

struct __C;
struct __C_VT;
typedef __C* C;


struct __A {
__A_VT* __vptr;
static __A_VT __vtable;

__A();

static Class __class();
static void m(A);
static A m(A, A);
static void __init();
};

struct __A_VT {
Class __isa;
int32_t (*hashCode) (A);
bool (*equals) (A, Object);
Class (*getClass) (A);
String (*toString) (A);
A (*m) (A, A);

__A_VT():
__isa((Class)__A::__class()),
 hashCode((int32_t (*) (A))&__Object::hashCode),
 equals((bool (*) (A, Object))&__Object::equals),
 getClass((Class (*) (A))&__Object::getClass),
 toString((String (*) (A))&__Object::toString),
 m((A (*) (A, A))&__A::m)
{
}
};

struct __B {
__B_VT* __vptr;
static __B_VT __vtable;

__B();

static Class __class();
static void m(B);
static C m(B, B);
static A m(B, A);
static void __init();
};

struct __B_VT {
Class __isa;
int32_t (*hashCode) (B);
bool (*equals) (B, Object);
Class (*getClass) (B);
String (*toString) (B);
A (*m) (B, A);

__B_VT():
__isa((Class)__B::__class()),
 hashCode((int32_t (*) (B))&__Object::hashCode),
 equals((bool (*) (B, Object))&__Object::equals),
 getClass((Class (*) (B))&__Object::getClass),
 toString((String (*) (B))&__Object::toString),
 m((A (*) (B, A))&__B::m)
{
}
};

struct __C {
__C_VT* __vptr;
static __C_VT __vtable;

__C();

static Class __class();
static void m(C);
static void __init();
};

struct __C_VT {
Class __isa;
int32_t (*hashCode) (C);
bool (*equals) (C, Object);
Class (*getClass) (C);
String (*toString) (C);
void (*m) (C);

__C_VT():
__isa((Class)__C::__class()),
 hashCode((int32_t (*) (C))&__Object::hashCode),
 equals((bool (*) (C, Object))&__Object::equals),
 getClass((Class (*) (C))&__Object::getClass),
 toString((String (*) (C))&__Object::toString),
 m((void (*) (C))&__C::m)
{
}
};

}
}
