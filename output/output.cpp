#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
using namespace java::lang;
namespace inputs
{
namespace test001
{
__A::__A():__vptr(&__vtable)
{
}

__A_VT __A::__vtable;

Class __A::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.test001.A"), __Object::__class());
    return k;
}

__A_VT __A::__vtable;
__A::toString(A__this)
{
    return __String "A" ;
};
}
}