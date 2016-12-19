cmake_minimum_required(VERSION 3.3)
project(java_lang_4)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    java_lang.cpp
    java_lang.h
    ptr.h
    main.cpp)

add_executable(java_lang_4 $ {SOURCE_FILES})