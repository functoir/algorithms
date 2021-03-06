"""
...This file implements methods for matrix manipulation, including:

    1. matrix_power() ->
        divide-and-conquer strategies for
        finding a matrix exponential.
        This function makes use of Strassen's algorithm to optimize its runtime.

    2. matrix_multiply() ->
        an implementation of Strassen's O(n^log_2(7))
        algorithm for matrix faster matrix multiplication by substituting
        brute-force multiplication with strategic multiplications and additions.

    3. matrix_sum() ->
        Given two matrices, find their sum using brute-force methods.

    4. matrix_difference() ->
        Given two matrices A, B in order, find A - B using brute_force methods.

    5. matrix_get() ->
        A helper function that, given an n x n matrix such that n % 2 = 0
        and a position 0 <= x <= 3, returns the subset of the matrix.

    6. matrix_compile() ->
        A helper function that, given a larger matrix A and a smaller matrix B
        such that dim(A) = 2 * dim(B), saves the values in B into a
        specified subset of A.

    7. matrix_identity() ->
        A function that, given a dimension n, returns the (n x n) identity matrix.

    8. matrix_transpose() ->
        Given an (m x n) matrix, returns the (n x m) transpose of the matrix.
        
    IMPORTANT NOTE:
        It is usual to represent matrices as an array of arrays, wherein each sub-array
        represents either a column or row of the matrix. 
        
        In this implementation, each sub-array is interpreted to be a column of the matrix.
        It's important that the caller create/implement matrices with similar
        semantics, or the calculations are not guaranteed to be correct.

    (c) Amittai J. Wekesa (github: @siavava), May 2021.
...
"""

from timeit import timeit
import numpy as np
from numpy.random import randint
from mpl_toolkits import mplot3d
from matplotlib import pyplot as plt
from math import log, log2, ceil


def matrix_power(matrix: list, exponential: int) -> list:
    """
    Compute the power of a matrix.
    """
    if exponential == 0:
        return matrix_identity(len(matrix))
    elif exponential == 1:
        return matrix
    else:
        half_power: list = matrix_power(matrix, exponential // 2)
        power: list = matrix_multiply(half_power, half_power)
        if (exponential % 2) == 0:
            return power
        else:
            return matrix_multiply(matrix, power)


def matrix_identity(n: int) -> list:
    """
    Generate the n-sized identity matrix.
    """
    if n == 0:
        return [0]
    identity: list = []
    for col in range(n):
        column: list = []
        for row in range(n):
            if col == row:
                column.append(1)
            else:
                column.append(0)
        identity.append(column)
    return identity


def matrix_generate(n: int, maximum=10**4) -> list:
    """
    Generate a random square matrix of given size
    Maximum size of element
    """
    if n == 0:
        return [0]
    m: list = []
    for col in range(n):
        column: list = []
        for row in range(n):
            column.append(randint(maximum))
        m.append(column)
    return m


def matrix_transpose(matrix: list) -> list:
    """
    Compute the transpose of a matrix
    """
    transpose: list = []
    for row in range(len(matrix[1])):
        column = []
        for col in range(len(matrix)):
            column.append(matrix[col][row])
        transpose.append(column)
    return transpose


def matrix_multiply(mat_a: list, mat_b: list):
    """
    Multiply rwo matrices.
    """
    n: int = len(mat_a)
    if n != len(mat_b):
        print("ERROR: Cannot multiply non-square matrices using Strassen. Stop.")
        return None
    elif n % 2 != 0:
        print("ERROR: Cannot work on matrices whose dimensions are not a factor of two.")
        print("An update will be coming soon!")
        return None

    if n > 2:
        mode = "matrix"
    else:
        mode = "nums"

    # sub-nums
    if mode == "nums":
        # Step 1: compute sums
        s1 = mat_b[1][0] - mat_b[1][1]
        s2 = mat_a[0][0] + mat_a[1][0]
        s3 = mat_a[0][1] + mat_a[1][1]
        s4 = mat_b[0][1] - mat_b[0][0]
        s5 = mat_a[0][0] + mat_a[1][1]
        s6 = mat_b[0][0] + mat_b[1][1]
        s7 = mat_a[1][0] - mat_a[1][1]
        s8 = mat_b[0][1] + mat_b[1][1]
        s9 = mat_a[0][0] - mat_a[0][1]
        s10 = mat_b[0][0] + mat_b[1][0]

        # Step 2: compute products
        p1 = mat_a[0][0] * s1
        p2 = mat_b[1][1] * s2
        p3 = mat_b[0][0] * s3
        p4 = mat_a[1][1] * s4
        p5 = s5 * s6
        p6 = s7 * s8
        p7 = s9 * s10

        # Step 3: compile results
        results = [[0, 0], [0, 0]]
        results[0][0] = p5 + p4 - p2 + p6
        results[1][0] = p1 + p2
        results[0][1] = p3 + p4
        results[1][1] = p5 + p1 - p3 - p7

        return results

    # sub-matrices
    elif mode == "matrix":
        # compute matrix sums
        s1 = matrix_difference(matrix_get(mat_b, 1), matrix_get(mat_b, 3))
        s2 = matrix_sum(matrix_get(mat_a, 0), matrix_get(mat_a, 1))
        s3 = matrix_sum(matrix_get(mat_a, 2), matrix_get(mat_a, 3))
        s4 = matrix_difference(matrix_get(mat_b, 2), matrix_get(mat_b, 0))
        s5 = matrix_sum(matrix_get(mat_a, 0), matrix_get(mat_a, 3))
        s6 = matrix_sum(matrix_get(mat_b, 0), matrix_get(mat_b, 3))
        s7 = matrix_difference(matrix_get(mat_a, 1), matrix_get(mat_a, 3))
        s8 = matrix_sum(matrix_get(mat_b, 2), matrix_get(mat_b, 3))
        s9 = matrix_difference(matrix_get(mat_a, 0), matrix_get(mat_a, 2))
        s10 = matrix_sum(matrix_get(mat_b, 0), matrix_get(mat_b, 1))

        # Step 2: compute matrix products
        p1 = matrix_multiply(matrix_get(mat_a, 0), s1)
        p2 = matrix_multiply(s2, matrix_get(mat_b, 3))
        p3 = matrix_multiply(s3, matrix_get(mat_b, 0))
        p4 = matrix_multiply(matrix_get(mat_a, 3), s4)
        p5 = matrix_multiply(s5, s6)
        p6 = matrix_multiply(s7, s8)
        p7 = matrix_multiply(s9, s10)

        # Step 3: compile results
        results = [[0] * n] * n
        results = matrix_compile(results, matrix_sum(matrix_sum(p5, p4), matrix_difference(p6, p2)), 0)
        results = matrix_compile(results, matrix_sum(p1, p2), 1)
        results = matrix_compile(results, matrix_sum(p3, p4), 2)
        results = matrix_compile(results, matrix_difference(matrix_sum(p5, p1), matrix_sum(p3, p7)), 3)

        # return compiled results
        return results


def matrix_sum(mat_a: list, mat_b: list):
    """
    Compute the sum of two compatible matrices.
    """
    n: int = len(mat_a)
    if n != len(mat_b):
        print("ERROR: cannot add incompatible matrices. Stop.")
        return None
    results = []
    for col in range(n):
        column = []
        for row in range(n):
            column.append(mat_a[col][row] + mat_b[col][row])
        results.append(column)
    return results


def matrix_difference(mat_a: list, mat_b: list):
    """
    Compute the difference of two matrices.
    """
    n: int = len(mat_a)
    if n != len(mat_b):
        print("ERROR: cannot subtract incompatible matrices. Stop.")
        return None
    results = []
    for col in range(n):
        column = []
        for row in range(n):
            column.append(mat_a[col][row] - mat_b[col][row])
        results.append(column)
    return results


def matrix_compile(main: list, sub: list, pos: int):
    """
    Compile sub-matrices into a main matrix.
    """
    n = len(sub)
    total = len(main)
    if len(main) != 2 * n:
        print("ERROR: Cannot merge matrices of incompatible dimension. Stop.")
        return None

    results = []

    if pos == 0:
        for col in range(total):
            column = []
            for row in range(total):
                if col in range(n) and row in range(n):
                    column.append(sub[col][row])
                else:
                    column.append(main[col][row])
            results.append(column)
        return results

    elif pos == 1:
        for col in range(total):
            column = []
            for row in range(total):
                if col in range(n, total) and row in range(n):
                    column.append(sub[col - n][row])
                else:
                    column.append(main[col][row])
            results.append(column)
        return results

    elif pos == 2:
        for col in range(total):
            column = []
            for row in range(total):
                if col in range(n) and row in range(n, total):
                    column.append(sub[col][row - n])
                else:
                    column.append(main[col][row])
            results.append(column)
        return results

    elif pos == 3:
        for col in range(total):
            column = []
            for row in range(total):
                if col in range(n, total) and row in range(n, total):
                    column.append(sub[col - n][row - n])
                else:
                    column.append(main[col][row])
            results.append(column)
        return results


def matrix_get(main: list, pos: int):
    """
    Get a subset of a matrix.
    """
    n = len(main)
    if n % 2 != 0:
        print("ERROR: Cannot split matrix with odd num of cols/rows. Stop.")
        return None

    mid = n // 2
    sub = []

    if pos == 0:
        for col in range(mid):
            column = []
            for row in range(mid):
                column.append(main[col][row])
            sub.append(column)
        return sub

    elif pos == 1:
        for col in range(mid):
            column = []
            for row in range(mid):
                column.append(main[mid + col][row])
            sub.append(column)
        return sub

    elif pos == 2:
        for col in range(mid):
            column = []
            for row in range(mid):
                column.append(main[col][mid + row])
            sub.append(column)
        return sub

    elif pos == 3:
        for col in range(mid):
            column = []
            for row in range(mid):
                column.append(main[mid + col][mid + row])
            sub.append(column)
        return sub


if __name__ == '__main__':
    a = [[1, 1], [1, 0]]
    b = [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13, 14, 15, 16]]
    d = [[1, 2, 0, 0], [3, 4, 0, 0], [0, 0, 5, 6], [0, 0, 7, 8]]
    c = [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]

    h = [[1, 2], [3, 4]]
    print(matrix_multiply(d, d))
    print(matrix_power(d, 5))
    print(matrix_multiply(c, d))
    print(matrix_identity(4))
    print(matrix_transpose(b))
    print(matrix_power(b, 113))
    print(f"random matrix: {matrix_generate(8)}")

    # Add code to do the rest of this problem
    sizes: list = []
    runtimes: list = []
    data = []
    ns = [2 ** t for t in range(1, 7)]

    titles = {'family': 'serif', 'color': 'blue', 'size': 20}
    axes = {'family': 'serif', 'color': 'darkred', 'size': 15}
    power = 4
    for n in ns:
        print(f"Computing with size = {n}, power = {power}")
        matrix = matrix_generate(n, 10)
        sizes.append(n)
        data.append(timeit("matrix_power(matrix, 4)",
                                       number=10, globals=globals()))

    # Generate plots
    plt.subplot(121)
    plt.grid(True, which="both")
    plt.title("exponential = 4", fontdict=titles)
    plt.xlabel("size", fontdict=axes)
    plt.ylabel("runtime", fontdict=axes)
    plt.plot(sizes, data, "--ro")

    plt.subplot(122)
    plt.title("size = 2", fontdict=titles)
    plt.xlabel("exponential", fontdict=axes)
    plt.ylabel("runtime", fontdict=axes)
    size = 2
    maxInt = 10
    matrix = matrix_generate(size, maxInt)
    powers = []
    static_runtimes = []
    for power in range(600):
        print(f"Computing size = 2, power = {power}.")
        powers.append(power)
        static_runtimes.append(timeit("matrix_power(matrix, power)",
                                       number=10, globals=globals()))
    plt.plot(powers, static_runtimes, "--b+")
    plt.savefig('./output/matrix-powers.png', dpi=300, transparent=False)
    plt.show()
    print("FINISHED!")
