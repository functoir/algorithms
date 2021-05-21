#!/usr/bin/env python3
"""
Starter (demo) python program for a programming assignment in
Dartmouth course CS 31, Spring 2021. Students enrolled in this
edition of the course may use and modify this code as they see fit.

Copyright © Amit Chakrabarti, 2021.
Modified by Amittai Wekesa & Zitong Wu, May 2021.
"""

import matplotlib.pyplot as plt
import numpy as np


def lcs(xs: list, ys: list):
    """Given lists xs and ys (of ints, chars, or whatever), return a
    longest common subsequence of the two lists.
    """
    m: int = len(xs)                                    # size of xs + 1
    n: int = len(ys)                                    # size of ys + 1
    OPT = np.zeros((m+1, n+1), dtype=int)               # OPT values

    # Calculate OPT values
    for i in range(1, m+1):
        for j in range(1, n+1):
            OPT[i, j] = max(OPT[i - 1, j], OPT[i, j - 1])
            if xs[i-1] == ys[j-1]:
                OPT[i, j] = max(OPT[i, j], OPT[i - 1, j - 1] + 1)

    # backtrack and recover sequence used.
    sequence: list = []
    i, j = m, n
    while i > 0 and j > 0:

        # If common character, prepend to sequence and move diagonally.
        if xs[i-1] == ys[j-1]:
            sequence.insert(0, xs[i-1])
            i -= 1
            j -= 1

        # Else move up or left to the largest neighbor
        else:
            if OPT[i-1, j] >= OPT[i, j-1]:
                i -= 1
            else:
                j -= 1

    # Return the subsequence of characters/values
    return sequence


def lcs_len(xs: list, ys: list):
    """Given lists xs and ys (of ints, chars, or whatever), return the
    length of a longest common subsequence of the two lists.
    """
    m: int = len(xs)                                    # size of xs
    n: int = len(ys)                                    # size of ys
    opt = np.zeros(n+1)                                 # 1D array to swap values
    last_value: int = 0                                 # variable to track last value dumped from array

    # Calculate OPT values
    for i in range(1, m+1):
        for j in range(1, n+1):
            current_value = max(opt[j], opt[j-1])       # opt[j] -> previous iteration, opt[j-1] -> current iteration

            if xs[i-1] == ys[j-1]:
                current_value = max(current_value, last_value + 1)

            # if num is not last index in list, save it before update.
            # CAVEAT: we don't need to save the last index because
            # we don't consider it when updating the first index.
            if j < n:
                last_value = opt[j]
            else:
                last_value = 0

            # finally, save the new value to list.
            opt[j] = current_value

    # return the computed LCS length of the full size arrays.
    return opt[n]


def get_digits(fname):
    with open(fname, "r") as f:
        s = f.read()
    return s.strip()


if __name__ == "__main__":
    # Test the implementation of lcs() on two short strings.
    # assert "".join(lcs("DEDICATION", "REDACTING")) == "EDCTIN"

    print(lcs("DEDICATION", "REDACTING"))

    print(lcs("PROTOPLASM", "CHROMATOPHORES"))

    print(lcs("WHAT", "WATER"))

    print(lcs("randomstring", "anotherstring."))

    # Read in digits of pi and e (as chars) from provided text files.
    # These files will need to be in the current directory so that the
    # program can find them. The files contain waaaay more digits than
    # we need, but we'll slice the lists. This step doesn't hurt.
    pi_digits = get_digits("pi_digits.txt")
    e_digits = get_digits("e_digits.txt")

    print(f"LCS for first ten digits = {lcs(pi_digits[:10], e_digits[:10])}")

    print(f"LCS for first 100 digits (joined into str) = {''.join(lcs(pi_digits[:100], e_digits[:100]))}")

    # Determine LCS between first 10 digits (each) of pi and e. The
    # answer is [1,1,2] and turning it into a string gives "112".
    assert "".join(lcs(pi_digits[:10], e_digits[:10])) == "112"

    # Add code to do the rest of this problem
    sizes = []
    LCS = []
    gradients = []
    for r in range(100, 2001, 100):
        print(f"Computing with n = {r}...")
        sizes.append(r)
        LCS.append(lcs_len(pi_digits[:r], e_digits[:r]))
        gradients.append(LCS[-1]/sizes[-1])

    for index in range(len(sizes)):
        print(f"Size: {sizes[index]}; LCS: {LCS[index]}")

    plt.subplot(121)
    plt.grid(True, which="both")
    plt.xlabel("Length of pi and e");
    plt.ylabel("Length of Subsequence")
    plt.plot(sizes, LCS, "--ro")

    plt.subplot(122)
    plt.grid(True, which="both")
    plt.xlabel("Length of pi and e")
    plt.ylabel("Gradient")
    plt.plot(sizes, gradients, "--bo")
    plt.show()
