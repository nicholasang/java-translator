cmake_minimum_required(VERSION 3.3)
project(java_lang_4)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")

set(SOURCE_FILES
    java_lang.cc
    java_lang.h
    main.cc
    ptr.h)

add_executable(java_lang_4 $ {SOURCE_FILES})