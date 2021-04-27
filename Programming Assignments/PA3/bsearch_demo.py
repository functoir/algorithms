#!/usr/bin/env python3
"""
Starter (demo) python program for a programming assignment in
Dartmouth course CS 31, Winter 2021. Students enrolled in this
edition of the course may use and modify this code as they see fit.

Copyright © Amit Chakrabarti, 2021.
"""

import sys
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
from timeit import timeit


def binsrch_bad(lst, key):
    """Binary search for key in lst, which is assumed to be sorted.
    Return index in lst where key is found, or raise ValueError.
    This is a bad way to do binary search in a list (why?); to test 
    it, call this on a python list and **not, e.g., a numpy.array.**
    """
    if len(lst) == 0:
        raise ValueError("Not found")
    mid = len(lst) // 2
    if key == lst[mid]:
        return mid
    try:
        if key > lst[mid]:
            idx = binsrch_bad(lst[mid + 1:], key)
            return mid + 1 + idx
        else:
            return binsrch_bad(lst[:mid], key)
    except ValueError:
        raise


def binsrch_good(lst, key, low=0, high=-100):
    if high == -100:
        high = len(lst) - 1

    if low > high:
        raise ValueError("Not found")

    mid = (low + high) // 2
    if key == lst[mid]:
        return mid
    try:
        if key > lst[mid]:
            return binsrch_good(lst, key, mid + 1, high)
        else:
            return binsrch_good(lst, key, low, mid - 1)
    except ValueError:
        raise


def test_search(f, lst, nkeys=10, spread=5, verbose=False):
    """Test out binary search function f on some randomly chosen keys.
    Pick several random positions in lst. For each position i, pick a 
    random key in the integer interval [lst[i]-spread, lst[i]+spread],
    so that most searches are unsuccessful but a few are successful.
    """

    poss = np.random.randint(len(lst), size=nkeys)
    keys = [lst[i] + np.random.randint(-spread, spread + 1) for i in poss]
    for k in keys:
        try:
            idx = f(lst, k)
            if verbose:
                print(f"Using {f.__name__}, found {k} at {idx}")
        except ValueError:
            if verbose:
                print(f"Using {f.__name__}, did not find {k}")


if __name__ == "__main__":
    # Generate lists with random lengths chosen at a few different scales,
    # roughly doubling the length at each step. Populate the lists with
    # random non-negative integers that each fit in a machine word.
    ns = [2 ** t + np.random.randint(2 ** (t - 2)) for t in range(10, 24)]

    # Changed value of maximum permissible number
    # because Python kept complaining that the number is out of range
    # for the `int32` data-type
    # Our understanding was that the 64-bit compiler was creating a 64-bit number,
    # which would then fail to fit in a 32-bit variable.

    # maxkey = sys.maxsize
    maxkey = (2 ^ 32) - 1

    times = []

    worse_times = []
    better_times = []
    ratios = []

    for n in ns:
        # Use numpy to generate the lists, so that sorting is quicker
        # than basic python lists. Convert the result to a basic list
        # for testing/timing the binary search implementation.

        print(f"Generating and sorting data with n = {n}")
        data = np.sort(np.random.randint(maxkey, size=n)).tolist()

        better_times.append(timeit("test_search(binsrch_good, data)",
                                   number=10, globals=globals()))

        worse_times.append(timeit("test_search(binsrch_bad, data)",
                                  number=10, globals=globals()))

        ratios.append(better_times[-1] / worse_times[-1])

    # Plot the ratio of the time taken by the bad binary search algorithm
    # and the good binary search algorithm on each list.
    plt.subplot(121);
    plt.grid(True, which="both")
    plt.xlabel("List length");
    plt.ylabel("Ratio of good vs bad binsrch runtimes")
    # plt.plot(ns, ratios, "--ro")
    plt.plot(ns, ratios, "--ro")
    # Plot time taken vs list length on a log scale
    plt.subplot(122);
    plt.semilogx();
    plt.grid(True, which="both")
    plt.xlabel("List length (log scale)");
    plt.ylabel("Ratio of good vs bad binsrch runtimes")
    plt.plot(ns, ratios, "--ro")
    plt.show()
