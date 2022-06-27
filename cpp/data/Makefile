# Makefile for data

OBJECTS = list.o
CC = g++

list.o: list.hpp

list_test: list.hpp list.cpp
	$(CC) $(CFLAGS) $^ -DUNITTEST

test: list_test
	make $^
  # ./list_test
