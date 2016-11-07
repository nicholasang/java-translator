#include <iostream>
#include "output.h"
#include "java_lang.h"
using namespace std;
using namespace java::lang;
using namespace inputs ::test001 ;
int main(int argc, char* argv[])
{
    A a =  new  __A ();
    std::cout << a->__vptr->toString(a)->data<< endl ;
    return 0;
}