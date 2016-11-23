#include <iostream>
#include "output.h"
#include "java_lang.h"
using namespace std;
using namespace java::lang;
using namespace inputs ::test005 ;
int main(int argc, char* argv[])
{
    B b =  (B) new  __B ();
    A a1 =  (A) new  __A ();
    A a2 =  (A) b ;
    std::cout << a1->__vptr->toString(a1)->data<< endl ;
    std::cout << a2->__vptr->toString(a2)->data<< endl ;
    return 0;
}