from timeit import timeit
import matplotlib.pyplot as plt
from matrix import *


def fib_recursive(n: int) -> int:
    if n == 1 or n == 2:
        return 1
    return fib_recursive(n - 1) + fib_recursive(n - 2)


def fib_iterative(n: int) -> int:
    curr: int = 1
    prev: int = 1

    for i in range(2, n):
        prev, curr = curr, curr + prev

    return curr


def fib_memoized(n: int) -> int:
    mem = [1, 1]

    for i in range(2, n):
        mem.append(mem[i - 1] + mem[i - 2])

    return mem[n - 1]


def fib_matrix(n):
    if n == 1 or n == 2:
        return 1
    final = matrix_power([[1, 1], [1, 0]], n - 1)
    return final[0][0]


if __name__ == '__main__':
    print(f"The 21st fibonacci number using recursion is {fib_recursive(21)}")
    print(f"The 21st fibonacci number using iteration is {fib_iterative(21)}")
    print(f"The 21st fibonacci number using memoization is {fib_memoized(21)}")
    print(f"The 21st fibonacci number using matrix multiplication is {fib_matrix(21)}")
