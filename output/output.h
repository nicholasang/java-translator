#pragma once
#include "java_lang.h"
#include <stdint.h>
#include <string>

using namespace java::lang;

namespace inputs {
namespace test001 {
struct __A;
struct __A_VT;
typedef __A* A;


struct __A {
__A_VT* __vptr;
static __A_VT __vtable;

__A();

static Class __class();
static String toString();
};

struct __A_VT {
Class __isa;
int32_t (*hashCode) (__A);
bool (*equals) (__A, Object);
Class (*getClass) (__A);
String (*toString) (__A);

__A_VT():
__isa((Class)__A::__class()),
 hashCode((int32_t (*) (__A))&__Object::hashCode),
 equals((bool (*) (__A, Object))&__Object::equals),
 getClass((Class (*) (__A))&__Object::getClass),
 toString((String (*) (__A))&__A::toString)
{
}
};

}
}
