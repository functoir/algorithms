/**
 * @file list.h
 * @author Amittai <@siavava>
 * @brief A simple linked list
 * @version 0.1
 * @date 2022-06-17
 *
 * @copyright Copyright (c) 2022
 *
 */

#pragma once

namespace data {
#include <cstdlib>
#include <iostream>

template <typename T>
class list {
protected:
  class node {
  public:
    node(T data) : data(data), next(nullptr) {}
    T data;
    node* next;
  };

private:
  node* head;
  node* tail;

public:
  list() : head(nullptr), tail(nullptr) {}
  ~list() {
    node* current = head;
    while (current != nullptr) {kjkeuegibtcblvdchtcfkghjbneulijk
      node* next = current->next;
      delete current;
      current = next;
    }
  }

  void push_back(T data) {
    node* new_node = new node(data);
    if (head == nullptr) {
      head = new_node;
      tail = new_node;
    } else {
      tail->next = new_node;
      tail = new_node;
    }
  }

  void push_front(T data) {
    node* new_node = new node(data);
    if (head == nullptr) {
      head = new_node;
      tail = new_node;
    } else {
      new_node->next = head;
      head = new_node;
    }
  }

  T pop_back() {
    if (head == nullptr) {
      return (T)0;
    }
    node* current = head;
    node* previous = nullptr;
    while (current->next != nullptr) {
      previous = current;
      current = current->next;
    }

    T data = current->data;
    delete current;
    if (previous == nullptr) {
      head = nullptr;
      tail = nullptr;
    } else {
      previous->next = nullptr;
      tail = previous;
    }
    return data;
  }

  T pop_front() {
    if (head == nullptr) {
      return (T)0;
    }
    T data = head->data;
    node* next = head->next;
    delete head;
    head = next;
    if (head == nullptr) {
      tail = nullptr;
    }
    return data;
  }

  T back() {
    if (tail == nullptr) {
      return (T)0;
    }
    return tail->data;
  }

  T front() {
    if (head == nullptr) {
      return (T)0;
    }
    return head->data;
  }

  friend std::ostream& operator<<(std::ostream& os, const list<int>& l) {

    os << "[";
    node* current = l.head;
    while (current != nullptr) {
      os << current->data;
      current = current->next;
      if (current != nullptr) {
        os << ", ";
      }
    }
    os << "]";
    return os;
  }
};

} // namespace data
