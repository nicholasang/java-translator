#pragma once

#include <iostream>
#include <string>

#define TRACE true

namespace __rt {

  template<typename T>
  class Ptr {
    T* addr;

    void trace(std::string s) const {
      if(TRACE)
        std::cout << __FUNCTION__ << ":" << __LINE__ << ": " << s << std::endl;
    }

  public:
    Ptr(T* addr) : addr(addr) {
      trace("constructor");
    }

    Ptr(const Ptr& other) : addr(other.addr) {
      trace("copy constructor");
    }

    ~Ptr() {
      trace("destructor");
    }

    Ptr& operator=(const Ptr& right) {
      trace("assignment operator");
      if (addr != right.addr) {
        addr = right.addr;
      }
      return *this;
    }

    T& operator*() const {
      trace("dereference operator");
      return *addr;
    }

    T* operator->() const {
      trace("arrow operator");
      return addr;
    }

    operator T*() const {
      trace("cast operator");
      return addr;
    }

    Ptr<T> id(Ptr<T> p) const {
      trace("return");
      return p;
    }

  };

}