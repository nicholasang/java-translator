#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
using namespace java::lang;
namespace inputs
{
namespace testABCD
{


__D_VT __D::__vtable;

Class __D::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.testABCD.D"), __Object::__class());
    return k;
}

void __D::allByMyself(D __this)
{
    std::cout << (new __String("-don't wanna be all by myself anymore."))->data<< endl ;

}
__D::__D() :__vptr(&__vtable)
{
}

namespace __rt
{
template <>
java::lang::Class Array<inputs::javalang::D>::__class()
{
    static java::lang::Class k =
        new java::lang::__Class(literal("[Linputs.javalang.D;"),
                                java::lang::__Object::class(),
                                inputs::javalang::__D::__class());
    return k;
}
}

__A_VT __A::__vtable;

Class __A::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.testABCD.A"), __Object::__class());
    return k;
}

__A::__A( ):__vptr(&__vtable)
{

}
__A::__A(String item  ):__vptr(&__vtable)
{

}
String publicDataA =  (String) (new __String("publicDataA")) ;
String protectedDataA =  (String) (new __String("protectedDataA")) ;
String privateDataA =  (String) (new __String("privateDataA")) ;
String staticDataA =  (String) (new __String("staticDataA")) ;

String __A::getArbitraryData(A __this)
{
    return __this->publicDataA ;

}

String __A::Love(A __this)
{
    return (new __String("is all you need")) ;

}


namespace __rt
{
template <>
java::lang::Class Array<inputs::javalang::A>::__class()
{
    static java::lang::Class k =
        new java::lang::__Class(literal("[Linputs.javalang.A;"),
                                java::lang::__Object::class(),
                                inputs::javalang::__A::__class());
    return k;
}
} namespace inputs
{
namespace testABCD
{


__C_VT __C::__vtable;

Class __C::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.testABCD.inputs.testABCD.C"), __Object::__class());
    return k;
}


int32_t __C::add(C __this, int32_t x int32_t y  )
{
    return x + y ;

}
__C::__C() :__vptr(&__vtable)
{
}

namespace __rt
{
template <>
java::lang::Class Array<inputs::javalang::C>::__class()
{
    static java::lang::Class k =
        new java::lang::__Class(literal("[Linputs.javalang.C;"),
                                java::lang::__Object::class(),
                                inputs::javalang::__C::__class()),
    inputs::javalang::__A::__class());
    return k;
}
} namespace inputs
{
namespace testABCD
{


__B_VT __B::__vtable;

Class __B::__class()
{
    static Class k =
        new __Class(__rt::literal("class inputs.testABCD.inputs.testABCD.inputs.testABCD.B"), __Object::__class());
    return k;
}

String privateDataB =  (String) (new __String("dataB")) ;

String __B::getArbitraryData(B __this)
{
    return __this->privateDataB ;

}
__B::__B() :__vptr(&__vtable)
{
}

namespace __rt
{
template <>
java::lang::Class Array<inputs::javalang::B>::__class()
{
    static java::lang::Class k =
        new java::lang::__Class(literal("[Linputs.javalang.B;"),
                                java::lang::__Object::class(),
                                inputs::javalang::__B::__class()),
    inputs::javalang::__A::__class());
    return k;
}
}
}
}
}
}
}
}