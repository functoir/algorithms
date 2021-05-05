#!/usr/bin/env python3
"""
Starter (demo) python program for a programming assignment in
Dartmouth course CS 31, Winter 2021. Students enrolled in this
edition of the course may use and modify this code as they see fit.

Copyright © Amit Chakrabarti, 2021.

Modified by:
Amittai J. Wekesa (@siavava)

Credit Statement:
I worked on this problem alone (see write-up document for explanation).
I did not refer to any outside/internet sources
"""

import math
import random
from timeit import timeit

import matplotlib.pyplot as plt

k_mult_ops = 0
naive_mult_ops = 0


def make_poly(degree, seed=2021):
    random.seed(seed)
    return random.choices(range(-99, 100), k=degree)


def naive_mult(ps: list, qs: list):
    global naive_mult_ops
    rs = []
    for k in range(2 * len(ps) - 1):            # Loop to 2n remembering that Python indexing starts at 0
        rk = 0                                  # Initialize rk
        for i in range(k+1):                    # loop to k
            try:
                rk += ps[i] * qs[k-i]           # get elements and multiply, add to rk
                naive_mult_ops += 1
            except IndexError:
                continue                        # if index out of bounds, ignore and continue
        rs.append(rk)
    return rs


def k_mult(ps, qs, start=0, end=None):
    global k_mult_ops
    if end is None:                                     # if end undefined, set to length of list
        end = len(ps)
    if start >= end:                                    # if list length is empty, return empty list
        return []
    elif start == end-1:                                # if single element, return their product
        k_mult_ops += 1
        return [ps[start] * qs[start]]
    else:
        # Initialize variables
        results, a_minus_b, d_minus_c = [], [], []
        for index in range(2*end - 1):
            results.append(0)
        mid = math.ceil((start + end) / 2)              # Get mid-point

        k_mult_ops += 1

        ac = k_mult(ps, qs, start, mid)                 # recursive call to k_mult
        bd = k_mult(ps, qs, mid, end)                   # recursive call to k_mult

        # Compute (a-b), (d-c)
        for i in range(start, mid):
            if mid+i < end:
                a_minus_b.append(ps[i] - ps[mid+i])
                d_minus_c.append(qs[mid+i] - qs[i])
                k_mult_ops += 2
            else:                                       # if first half has more elements than second half
                a_minus_b.append(ps[i])
                d_minus_c.append(-qs[i])
                k_mult_ops += 2

            if i == mid-1 and mid < (end-mid):                         # if second half has more elements than first half
                for j in range(mid+i, end):
                    a_minus_b.append(-ps[j])
                    d_minus_c.append(qs[j])
                    k_mult_ops += 2
        # Compute (a-b)*(d-c)
        three = k_mult(a_minus_b, d_minus_c)   # recursive call to k_mult
        # Merge results of a*c into results
        for i in range(len(ac)):
            results[i] += ac[i]
            # results.append(ac[i])

        # Merge (a-b)*(d-c) + ac + bd into results, shifted by t
        for i in range(len(three)):
            results[mid+i] += three[i]
            # try:                                        # if elements exists, add
            #     k_mult_ops += 1
            # except IndexError:                           # if not, append
            #     results.append(three[i])

            # if i < len(ac):

        for i in range(len(ac)):
            print(f"01 mid = {mid}; mid + i = {mid + i}; len(results) = {len(results)}")
            results[mid+i] += ac[i]
            # try:
            #     k_mult_ops += 1
            # except IndexError:
            #     results.append(ac[i])

        # if i < len(bd):
        for i in range(len(bd)):
            print(f"02 mid = {mid}; mid + i = {mid + i}; len(results) = {len(results)}")
            try:
                results[mid+i] += bd[i]
                k_mult_ops += 1
            except IndexError:
                results.append(bd[i])

        # Merge b*d into results, shifted by 2t
        for i in range(len(bd)):
            try:
                results[i + 2*mid] += bd[i]
                k_mult_ops += 1
            except IndexError:
                results.append(bd[i])
        return results


if __name__ == "__main__":
    print(f"naive: {naive_mult([31, -4, 15], [27, 18, -28])}")
    print(f"karatsuba: {k_mult([31, -4, 15], [27, 18, -28])}")

    # for n in range(50,10001,50):
    poly1 = make_poly(4000)
    poly2 = make_poly(4000)

    # Verification test:
    for index in range(10):
        print(f"naive: {naive_mult(poly1[:index], poly2[:index])}")
        print(f"karatsuba: {k_mult(poly1[:index], poly2[:index])}")

    ns = (list(range(50, 300))
          + list(range(300, 600, 5)) + list(range(600, 1000, 20))
          + list(range(1000, 2000, 50)) + list(range(2000, 4000, 100)))

    naive_runtime = []
    k_runtime = []
    ratios = []
    logs = []
    naive_ops = []
    k_ops = []

    # for n in ns:
    #
    #     first = poly1[:n]
    #     second = poly2[:n]
    #
    #     print(f"Generating data with n = {n}")
    #
    #     naive_runtime.append(timeit("naive_mult(first, second)",
    #                                 number=10, globals=globals()))
    #     k_runtime.append(timeit("k_mult(first, second)",
    #                             number=10, globals=globals()))
    #     ratios.append(k_runtime[-1] / naive_runtime[-1])
    #
    #     logs.append(n ** math.log(3, 2))
    #
    #     naive_ops.append(naive_mult_ops)
    #     k_ops.append(k_mult_ops)
    #     naive_mult_ops, k_mult_ops = 0, 0

    # ratios.append(better_times[-1] / worse_times[-1])
    # plt.subplot(121);
    # plt.grid(True, which="both")
    # plt.xlabel("List length");
    # plt.ylabel("Runtime")
    # # plt.plot(ns, ratios, "--ro")
    # plt.plot(ns, naive_runtime, "--bo")
    # plt.plot(ns, k_runtime, "--ro")

    # Plot time taken vs list length on a log scale
    # plt.subplot(122);
    # plt.grid(True, which="both")
    # plt.xlabel("List length (log scale)");
    # plt.ylabel("Ratio of karatsuba/naive")
    # plt.plot(ns, ratios, "--r+")
    # plt.show()

    # Num of operations:
    # plt.subplot(121);
    # plt.grid(True, which="both")
    # plt.xlabel("List length")
    # plt.ylabel("No. of Operations")
    # # plt.plot(ns, ratios, "--ro")
    # plt.plot(ns, naive_ops, "--bo")
    # plt.plot(ns, k_ops, "--ro")
    # # plt.show()
    #
    # # Hidden constant:
    # plt.subplot(122);
    # plt.grid(True, which="both")
    # plt.xlabel("n^log_{2}(3)")
    # plt.ylabel("Runtime")
    # # plt.plot(ns, ratios, "--ro")
    # # plt.plot(logs, naive, "--bo")
    # plt.plot(logs, k_ops, "--ro")
    # plt.show()
