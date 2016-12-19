#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
using namespace java::lang;
namespace inputs { 
 namespace test005 { 
 

__A_VT __A::__vtable;

Class __A::__class() {
static Class k = 
new __Class(__rt::literal("class inputs.test005.A"), __Object::__class());
return k;}


String __A::toString(A __this)
 {
 return (new __String("A")) ;

}
 __A::__A() :__vptr(&__vtable) {
}}
}

namespace __rt
{template<>
java::lang::Class Array<inputs::test005::A>::__class(){
static java::lang::Class k =
new java::lang::__Class(literal("[Linputs.test005.A;"),
java::lang::__Object::__class(),
inputs::test005::__A::__class());
return k;
}
}

namespace inputs {
namespace test005{

__B_VT __B::__vtable;

Class __B::__class() {
static Class k = 
new __Class(__rt::literal("class inputs.test005.B"), __Object::__class());
return k;}


String __B::toString(B __this)
 {
 return (new __String("B")) ;

}
 __B::__B() :__vptr(&__vtable) {
}}
}

namespace __rt
{template<>
java::lang::Class Array<inputs::test005::B>::__class(){
static java::lang::Class k =
new java::lang::__Class(literal("[Linputs.test005.B;"),
inputs::test005::__A::__class(),
inputs::test005::__B::__class());
return k;}}