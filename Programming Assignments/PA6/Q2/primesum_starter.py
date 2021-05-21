#!/usr/bin/env python3
"""
Starter (demo) python program for a programming assignment in
Dartmouth course CS 31, Spring 2021. Students enrolled in this
edition of the course may use and modify this code as they see fit.

Copyright © Amit Chakrabarti, 2021.
"""

import numpy as np
from sympy import sieve


def num_prime_sums(n):
    """Return the number of ways to write the positive integer t as a
    sum of (not necessarily distinct) prime numbers.

    REPLACE the rest of this docstring with a good high-level 
    description of your idea. Additionally, add comments to your code if 
    some step is not obvious.
    """
    # You'll want to use either sympy.isprime() or sympy.primerange()
    # somewhere in your code.
    l = sieve.primerange(1, n+1)
    l = list(l)
    num = len(l)
    a = np.zeros((num + 1, n+1))
    for i in range(num+1):
        a[i, 0] = 1
    for i in range(1, num+1):
        for u in range(1, n+1):
            num_pi = u//l[i-1]
            for j in range(num_pi+1):
                a[i, u] += a[i-1, u - j * l[i-1]]
    return int(a[-1, -1])


def smallest_with_many_sums(t):
    """Return the smallest positive integer that can be written as a sum
    of primes in at least t different ways. Assumes t > 0.
    """
    i = 1
    while num_prime_sums(i) < t:
        i += 1
    return i
    

if __name__ == "__main__":
    # Test the implementations
    assert num_prime_sums(10) == 5
    assert smallest_with_many_sums(5) == 10

    # Add code to do what's asked in the problem statement
    answer = smallest_with_many_sums(10 ** 9)
    print(answer)
