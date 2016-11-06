#include "output.h"
#include "java_lang.h"
using namespace std;
#include <iostream>
namespace inputs { 
 namespace test001 { 
 class A{ 
 public :  string toString () //__String not recognized
 { return "A" ; 
} 
}; 
class Test001{ 
 int main () //main needs to be empty args and int
 {A* a =  new  A (); //need *
std::cout << a->toString()<< endl ; //use ->
delete a; //need to add this for A*
} 
}; 
}}