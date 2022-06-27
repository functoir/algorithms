/**
 * @file list.cpp
 * @author Amittai <@siavava>
 * @brief Unit tests for the list class
 * @version 0.1
 * @date 2022-06-17
 * 
 * @copyright Copyright (c) 2022
 * 
 */

#ifdef UNITTEST

#include "list.hpp"
#include <iostream>

using namespace data;

int main() {


  list<int> l;
  for (int i = 0; i < 10; i++) {
    l.push_back(i);
  }

  std::cout << "l: " << l << std::endl;

  return 0;
}

#endif