#include <iostream>
#include "output.h"
#include "java_lang.h"
using namespace std;
using namespace java::lang; using namespace inputs ::test002 ; int main(int argc, char* argv[])
{ A a =  (A) new  __A (); 
Object o =  (Object) a ; 
std::cout << o->__vptr->toString(o)->data<< endl ; 
return 0; }