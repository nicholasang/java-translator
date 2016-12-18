#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
using namespace java::lang;
namespace inputs
{
namespace test002
{


__A_VT __A::__vtable;

Class __A::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.test002.A"), __Object::__class());
    return k;
}


String __A::toString(A __this)
{
    return (new __String("A")) ;

}
__A::__A() :__vptr(&__vtable)
{
}
}
}