#include <iostream>
#include "output.h"
#include "java_lang.h"
using namespace std;
using namespace java::lang;
using namespace inputs ::testABCD ;
int main(int argc, char* argv[])
{
    std::cout << (new __String("Hello, world!"))->data<< endl ;
    return 0;
}