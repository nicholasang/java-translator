#pragma once
#include "java_lang.h"
#include <stdint.h>
#include <string>

using namespace java::lang;

namespace inputs {
namespace testABCD {
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


struct __D {
__D_VT* __vptr;
__D_VT __vtable;

__D();

Class __class();
void allByMyself();
};

struct __D_VT {
Class __isa;
int32_t (*) (__D) hashCode;
bool (*) (__D, Object) equals;
Class (*) (__D) getClass;
String (*) (__D) toString;
void (*) (__D) allByMyself;

__D(__isa(null __isa),
hashCode(null hashCode),
equals(null equals),
getClass(null getClass),
toString(null toString),
allByMyself(null allByMyself),
);

};

struct __A {
__A_VT* __vptr;
__A_VT __vtable;
String publicDataA;
String protectedDataA;
String privateDataA;
String staticDataA;

__A();


__A(String item );

Class __class();
String getArbitraryData();
String Love();
};

struct __A_VT {
Class __isa;
int32_t (*) (__A) hashCode;
bool (*) (__A, Object) equals;
Class (*) (__A) getClass;
String (*) (__A) toString;
String (*) (__A) getArbitraryData;

__A(__isa(null __isa),
hashCode(null hashCode),
equals(null equals),
getClass(null getClass),
toString(null toString),
getArbitraryData(null getArbitraryData),
);

};

struct __C {
__C_VT* __vptr;
__C_VT __vtable;
String publicDataA;
String protectedDataA;
String privateDataA;

__C();

Class __class();
int32_t add(int32_t int32_t );
};

struct __C_VT {
Class __isa;
int32_t (*) (__C) hashCode;
bool (*) (__C, Object) equals;
Class (*) (__C) getClass;
String (*) (__C) toString;
String (*) (__C) getArbitraryData;
int32_t (*) (__C, int32_t, int32_t) add;

__C(__isa(null __isa),
hashCode(null hashCode),
equals(null equals),
getClass(null getClass),
toString(null toString),
getArbitraryData(null getArbitraryData),
add(null add),
);

};

struct __B {
__B_VT* __vptr;
__B_VT __vtable;
String privateDataB;
String publicDataA;
String protectedDataA;
String privateDataA;

__B();

Class __class();
String getArbitraryData();
};

struct __B_VT {
Class __isa;
int32_t (*) (__B) hashCode;
bool (*) (__B, Object) equals;
Class (*) (__B) getClass;
String (*) (__B) toString;
String (*) (__B) getArbitraryData;

__B(__isa(null __isa),
hashCode(null hashCode),
equals(null equals),
getClass(null getClass),
toString(null toString),
getArbitraryData(null getArbitraryData),
);

};
