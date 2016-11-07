cmake_minimum_required(VERSION 3.2.3)
project(java_lang_3)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp java_lang.cpp)
add_executable(java_lang_3 $ {SOURCE_FILES})