# Java to C++ Translator
----------

## Features:
- translates java into C++ header files for all provided inputs
- translates java into output.cpp and main.cpp files for 6 inputs (test000 through test005)
- supports method overriding and overloading

Input files must be correct Java!

All commands given below are from within sbt.

## To execute translator:
```runxtc -translateJava file/path/to/test/file.java```

**NOTE: file path and file name must *exactly* match the actual path/name (i.e. correctly capitalized)**

Output will be placed in the output.h, output.c, and main.cpp files within the output directory.

## To run translated C++ files:
```cpp ```

## To format translated C++ files:
```formatc ```



Project Map
-----------
```
├── README.md
│
├── build.sbt (managed library dependencies and c++ compilation configuration)
│
├── .sbtrc (like bash aliases but for sbt)
│
├── .gitignore (prevent certain files from being commmited to the git repo)
│
├── lib (unmanaged library dependencies, like xtc and its source) 
│
├── logs (logger output)
│   └── xtc.log 
│
├── output (target c++ source & supporting java_lang library)
│   ├── java_lang.cpp
│   ├── java_lang.h
│   ├── main.cpp
│   ├── output.cpp
│   └── output.h
│
├── project (sbt configuration, shouldn't need to be touched)
│
├── schema (ast schema & examples)
│   ├── cpp.ast
│   └── inheritance.ast
│
└── src 
    ├── main
    │   ├── java
    │   │   └── edu (translator source code)
    │   └── resources
    │       └── xtc.properties (translator properties file)
    └── test
        └── java
            ├── edu (translator unit tests)
            └── inputs (translator test inputs)
```
