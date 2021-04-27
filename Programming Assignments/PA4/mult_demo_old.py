#!/usr/bin/env python3
"""
Starter (demo) python program for a programming assignment in
Dartmouth course CS 31, Winter 2021. Students enrolled in this
edition of the course may use and modify this code as they see fit.

Copyright © Amit Chakrabarti, 2021.
Modified by: Amittai J. Wekesa (@siavava)
"""

import random
import matplotlib
import matplotlib.pyplot as plt
from timeit import timeit
import math


def make_poly(degree, seed=2021):
    random.seed(seed)
    return random.choices(range(-99, 100), k=degree)


def naive_mult(ps: list, qs: list):
    rs = []
    for k in range(2 * len(ps) - 1):            # Loop to 2n remembering that Python indexing starts at 0
        rk = 0                                  # Initialize rk
        for i in range(k+1):                    # loop to k
            try:
                rk += ps[i] * qs[k-i]           # get elements and multiply, add to rk
            except IndexError:
                continue                        # if index out of bounds, ignore and continue
        rs.append(rk)
    return rs


def k_mult(ps, qs):
    if len(ps) == 0:
        return []
    elif len(ps) == 1 or len(qs) == 1:
        return [ps[0] * qs[0]]
    else:
        # Initialize variables
        results = []
        t = math.ceil(len(ps)/2)
        a, b = ps[:t], ps[t:]
        c, d = qs[:t], qs[t:]

        # Compute a * c, b * d
        one, two = k_mult(a, c), k_mult(b, d)

        # Compute a - b
        a_b, d_c = [], []
        # for i in range(len(a)):
        #     a_b.append(a[i])
        # for i in range(len(b)):
        #     try:
        #         a_b[i] -= b[i]
        #     except IndexError:
        #         a_b.append(-b[i])

        for i in range(min(len(a), len(b))):
            a_b.append(a[i] - b[i])

            if i == min(len(a), len(b)) - 1:
                if len(a) > len(b):
                    for j in range(i+1, len(a)):
                        a_b.append(a[j])
                elif len(b) > len(a):
                    for j in range(i+1, len(b)):
                        a_b.append(-b[j])

        # Compute d - c
        # for i in range(len(d)):
        #     d_c.append(d[i])
        # for i in range(len(c)):
        #     try:
        #         d_c[i] -= c[i]
        #     except IndexError:
        #         d_c.append(-c[i])

        for i in range(min(len(c), len(d))):
            d_c.append(d[i] - c[i])

            if i == min(len(d), len(c)) - 1:
                if len(d) > len(c):
                    for j in range(i+1, len(d)):
                        d_c.append(d[i])
                elif len(c) > len(d):
                    for j in range(i+1, len(c)):
                        d_c.append(-c[j])



        # Compute (a-b)*(d-c)
        three = k_mult(a_b, d_c)

        # Merge results of a*c into results
        for i in range(len(one)):
            results.append(one[i])

        # Merge (a-b)*(d-c) into results,shifted by t
        for i in range(len(three)):
            try:
                results[t+i] += three[i]
            except IndexError:
                results.append(three[i])
            if i < len(one):
                results[t+i] += one[i]

            if i < len(two):
                results[t+i] += two[i]

        # Merge b*d into results, shifted by 2t
        for i in range(len(two)):
            try:
                results[i+2*t] += two[i]
            except IndexError:
                results.append(two[i])

        # return array of results
        return results


if __name__ == "__main__":
    r1 = timeit(stmt='naive_mult([31, -4, 15], [27, 18, -28])', globals=globals())
    print(naive_mult([31, -4, 15], [27, 18, -28]))
    print(r1)
    r2 = timeit(stmt='k_mult([31, -4, 15], [27, 18, -28])', globals=globals())
    print(r2)
    # Both r1 and r2 should be [837, 450, -535, 382, -420].

    # for n in range(50,10001,50):
    poly1 = make_poly(4000)
    poly2 = make_poly(4000)
    ns = (list(range(50, 300)) + list(range(300, 600, 5)) + list(range(600, 1000, 20))
          + list(range(1000, 2000, 50)) + list(range(2000, 4000, 100)))
    for n in ns:
        first = poly1[:10]
        second = poly2[:10]

        print(f"naive: {timeit('naive_mult(first, second)', globals=globals())}")
        print(f"karatsuba: {timeit('k_mult(first, second)', globals=globals())}")

        print(k_mult(first, second))
        # Generate two random polynomials. Feel free to change the
        # seed, which I have set to a default of 2021.
        # p = make_poly(n)
        # q = make_poly(n)
        # # Multiply them two ways. Results should be identical.
        # r1 = naive_mult(p, q)
        # print(r1)
        # r2 = k_mult(p, q)
        # print(r2)

    #     # Add code involving timeit() and code to generate plots as
    #     # asked for in the assignment. Refer to code from the previous
    #     # programming assignment if you forgot how to do these things.
    #     pass
