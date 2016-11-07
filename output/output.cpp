#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
using namespace java::lang;
namespace inputs { 
 namespace test001 { 
 __A::__A():__vptr(&__vtable){
A () 
 {} 
}

__A_VT __A::__vtable;

Class __A::__class() {
static Class k = 
new __Class(__rt::literal("class inputs.test001.A"), __Object::__class());
return k;}

__A_VT __A::__vtable;__A::toString(A__this){
 return __String "A" ; 
}; 
__Test001::__Test001():__vptr(&__vtable){
}

__Test001_VT __Test001::__vtable;

Class __Test001::__class() {
static Class k = 
new __Class(__rt::literal("class inputs.test001.Test001"), __Object::__class());
return k;}

__Test001_VT __Test001::__vtable;__Test001::main((__String[] args ) 
){
A a =  new  A (); 
std::cout << a->toString()<< endl ; 
}; 
}}