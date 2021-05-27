from timeit import timeit
import matplotlib.pyplot as plt


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

    matrix = [[1, 1], [1, 0]]
    final = mat_power(matrix, n - 1)
    return final[0][0]


def mat_power(matrix: list, power: int) -> list:
    if power == 1:
        return matrix

    half_power = mat_power(matrix, power // 2)

    if (power % 2) == 0:
        return matrix_mult(half_power, half_power)
    else:
        return matrix_mult(matrix, matrix_mult(half_power, half_power))


def matrix_mult(mat_a: list, mat_b: list) -> list:
    n: int = len(mat_a)
    if n != len(mat_b):
        print("ERROR: Cannot multiply non-square matrices using Strassen. Stop.")

    if n > 2:
        mode = "matrix"
    else:
        mode = "nums"

    # sub-nums
    if mode == "nums":
        # Step 1: compute sums
        s1 = mat_b[0][1] - mat_b[1][1]
        s2 = mat_a[0][0] + mat_a[0][1]
        s3 = mat_a[1][0] + mat_a[1][1]
        s4 = mat_b[1][0] - mat_b[0][0]
        s5 = mat_a[0][0] + mat_a[1][1]
        s6 = mat_b[0][0] + mat_b[1][1]
        s7 = mat_a[0][1] - mat_a[1][1]
        s8 = mat_b[1][0] + mat_b[1][1]
        s9 = mat_a[0][0] - mat_a[1][0]
        s10 = mat_b[0][0] + mat_b[0][1]

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
        results[1][0] = p3 + p4
        results[0][1] = p1 + p2
        results[1][1] = p5 + p1 - p3 - p7

        return results

    # sub-matrices
    elif mode == "matrix":
        s1 = matrix_difference(matrix_get(mat_b, 2), matrix_get(mat_b, 3))
        s2 = matrix_sum(matrix_get(mat_a, 0), matrix_get(mat_a, 2))
        s3 = matrix_sum(matrix_get(mat_a, 1), matrix_get(mat_a, 3))
        s4 = matrix_difference(matrix_get(mat_b, 1), matrix_get(mat_b, 0))
        s5 = matrix_sum(matrix_get(mat_a, 0), matrix_get(mat_a, 3))
        s6 = matrix_sum(matrix_get(mat_b, 0), matrix_get(mat_b, 3))
        s7 = matrix_difference(matrix_get(mat_a, 2), matrix_get(mat_a, 3))
        s8 = matrix_sum(matrix_get(mat_b, 1), matrix_get(mat_b, 3))
        s9 = matrix_difference(matrix_get(mat_a, 0), matrix_get(mat_a, 1))
        s10 = matrix_sum(matrix_get(mat_b, 0), matrix_get(mat_b, 2))

        # Step 2: compute products
        p1 = matrix_mult(matrix_get(mat_a, 0), s1)
        print(f"{matrix_get(mat_a, 0)} x {s1} = {p1}")
        p2 = matrix_mult(matrix_get(mat_b, 3), s2)
        p3 = matrix_mult(matrix_get(mat_b, 0), s3)
        p4 = matrix_mult(matrix_get(mat_a, 3), s4)
        p5 = matrix_mult(s5, s6)
        p6 = matrix_mult(s7, s8)
        p7 = matrix_mult(s9, s10)

        # Step 3: compile results
        results = [[0] * n] * n
        print(f"results: {results}")
        results = matrix_compile(results, matrix_sum(matrix_sum(p5, p1), matrix_difference(p6, p2)), 0)
        print(f"results: {results}")
        results = matrix_compile(results, matrix_sum(p3, p4), 1)
        print(f"results: {results}")
        results = matrix_compile(results, matrix_sum(p1, p2), 2)
        print(f"results: {results}")
        results = matrix_compile(results, matrix_difference(matrix_sum(p5, p1), matrix_sum(p3, p7)), 3)
        print(f"results: {results}")

        # return compiled results
        return results


"""
---
Checked and certified
---
"""


def matrix_sum(mat_a: list, mat_b: list):
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


"""
---
Checked and certified
---
"""


def matrix_compile(main: list, sub: list, pos: int):
    n = len(sub)
    total = len(main)
    print(f"main before compile: {main}")
    print(f"sub: {sub}")
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

    elif pos == 1:
        for col in range(total):
            column = []
            for row in range(total):
                if col in range(n, total) and row in range(n):
                    column.append(sub[col - n][row])
                else:
                    column.append(main[col][row])
            results.append(column)
    elif pos == 2:
        for col in range(total):
            column = []
            for row in range(total):
                if col in range(n) and row in range(n, total):
                    column.append(sub[col][row - n])
                else:
                    column.append(main[col][row])
            results.append(column)

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


"""
---
Checked and certified
---
"""


def matrix_get(main: list, pos: int) -> list:
    print(f"matrix_get {pos}")
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
                # sub[col][row] = main[mid+col][row]
            sub.append(column)
        return sub

    elif pos == 2:
        for col in range(mid):
            column = []
            for row in range(mid):
                column.append(main[col][mid + row])
                # sub[col][row] = main[col][mid+row]
            sub.append(column)
        return sub

    elif pos == 3:
        for col in range(mid):
            column = []
            for row in range(mid):
                column.append(main[mid + col][mid + row])
                # sub[col][row] = main[mid + col][mid + row]
            sub.append(column)
        return sub


if __name__ == '__main__':
    print(f"The 21st fibonacci number is {fib_recursive(21)}")
    print(f"The 21st fibonacci number is {fib_iterative(21)}")
    print(f"The 21st fibonacci number is {fib_memoized(21)}")
    print(f"The 21st fibonacci number is {fib_matrix(21)}")

    a = [[1, 1], [1, 0]]
    b = [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13, 14, 15, 16]]
    d = [[1, 2, 0, 0], [3, 4, 0, 0], [0, 0, 5, 6], [0, 0, 7, 8]]
    c = [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]

    h = [[1, 2], [3, 4]]
    print(b[0])
    print(b[0][3])
    print(matrix_mult(h, h))
    print(mat_power(h, 5))
    # print(matrix_mult(c, d))

    # for i in range(1, 100):
    #     print(i)
    #     print(f"{i}: {mat_power(b, i)}")
