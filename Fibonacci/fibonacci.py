"""
...This module implements methods for finding fibonacci numbers, namely:

    1. fib_recursive() ->
        A function that, given an integer n, finds the n-th fibonacci number
        using the recursion relationship F(n) = F(n-1) + F(n-2).
        As is to be expected, this method has O(2^n) time complexity
        and is a very inefficient method of finding the solution.

    2. fib_memoized() ->
        Acknowledging that the recursive function recomputes many values,
        this method attempts to improve runtime by saving values once seen
        reusing them in later calls to the same i-th fibonacci number.
        This is expected to have O(n) time complexity, but is it optimal?

    2. fib_iterative() ->
        Using Dynamic Programming and our knowledge of the fibonacci sequence,
        this function, given an integer n, builds the n-th fibonacci sequence
        from the ground up, iterating n times and swapping values between two variables.
        This has O(n) time complexity and much better space complexity than the memoized version.

    3. fib_matrix() ->
        A better way to compute the fibonacci sequence, this function acknowledges that
        the n-1 exponential of the matrix [[1, 1], [1, 0]] contains the n-th fibonacci
        number for any n.
        Thus, we compute the n-th number by matrix exponential, which takes us
        O(log n) time.

    (c) Amittai J. Wekesa (github: @siavava), May 2021.
...
"""

from timeit import timeit
import matplotlib.pyplot as plt
from Matrix.matrix import *


def fib_recursive(n: int) -> int:
    """ Find the n-th fibonacci number by naive recursion """
    if n <= 2:
        return n
    return fib_recursive(n - 1) + fib_recursive(n - 2)


def fib_iterative(n: int) -> int:
    """ Find the n-th fibonacci number by iteration using Dynamic Programming"""
    curr: int = 1
    prev: int = 1

    for i in range(2, n):
        prev, curr = curr, curr + prev

    return curr


def fib_memoized(n: int) -> int:
    """ Find the n-th fibonacci number with memoization"""
    memory: list = [0, 1]
    for i in range(2, n+1):
        memory.append(memory[-1] + memory[-2])
    return memory[n]


def fib_matrix(n):
    """
    Compute the n-th fibonacci number using matrix multiplication.
    """
    if n < 2:
        return n
    elif n == 2:
        return 1
    final = matrix_power([[1, 1], [1, 0]], n - 1)
    return final[0][0]


if __name__ == '__main__':
    print(f"The 21st fibonacci number using recursion is {fib_recursive(21)}")
    print(f"The 21st fibonacci number using iteration is {fib_iterative(21)}")
    print(f"The 21st fibonacci number using memoization is {fib_memoized(21)}")
    print(f"The 21st fibonacci number using matrix multiplication is {fib_matrix(21)}")

    # Add code to do the rest of this problem
    sizes: list = []
    # recursive_runtimes: list = []
    iterative_runtimes: list = []
    memoization_runtimes: list = []
    matrix_runtimes: list = []

    ns = [2 ** t for t in range(1, 19)]

    for n in ns:
        print(f"Computing with n = {n}")
        # print(data)
        sizes.append(n)
        # recursive_runtimes.append(timeit("fib_recursive(n)",
        #                                  number=10, globals=globals()))
        iterative_runtimes.append(timeit("fib_iterative(n)",
                                         number=10, globals=globals()))
        memoization_runtimes.append(timeit("fib_memoized(n)",
                                           number=10, globals=globals()))
        matrix_runtimes.append(timeit("fib_matrix(n)",
                                      number=10, globals=globals()))

    # Generate plots
    titles = {'family': 'serif', 'color': 'blue', 'size': 20}
    axes = {'family': 'serif', 'color': 'darkred', 'size': 15}
    plt.figure(1)
    plt.grid(True, which="both")
    plt.title("FIBONACCI", fontdict=titles)
    plt.xlabel("n", fontdict=axes)
    plt.ylabel("Runtime", fontdict=axes)
    # plt.plot(sizes, recursive_runtimes, "--ko")             # black
    plt.plot(sizes, iterative_runtimes, "--go")             # green
    plt.plot(sizes, memoization_runtimes, "--bo")           # blue
    plt.plot(sizes, matrix_runtimes, "--ro")                # red

    plt.show()

    # print(f"The {sizes[-1]}th fibonacci number using recursion is: {fib_recursive(sizes[-1])}.")
    print(f"The {sizes[-1]}th fibonacci number using iteration is: {fib_iterative(sizes[-1])}.")
    print(f"The {sizes[-1]}th fibonacci number using memoization is: {fib_memoized(sizes[-1])}.")
    print(f"The {sizes[-1]}th fibonacci number using matrix multiplication is: {fib_matrix(sizes[-1])}.")

    print("\nFINISHED.\n")
