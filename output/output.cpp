#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
using namespace java::lang;
namespace inputs
{
namespace test005
{


__A_VT __A::__vtable;

Class __A::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.test005.A"), __Object::__class());
    return k;
}


String __A::toString(A __this)
{
    return (new __String("A")) ;

}
__A::__A() :__vptr(&__vtable)
{
}

__B_VT __B::__vtable;

Class __B::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.test005.B"), __Object::__class());
    return k;
}


String __B::toString(B __this)
{
    return (new __String("B")) ;

}
__B::__B() :__vptr(&__vtable)
{
}
}
}