from timeit import timeit
import matplotlib.pyplot as plt
from numpy.random import randint

import math


def merge_sort(array: list):
    if len(array) > 1:
        mid = math.ceil(len(array) / 2)
        l = array[:mid]
        r = array[mid:]

        merge_sort(l)
        merge_sort(r)

        i, j, k = 0, 0, 0
        while i < len(l) and j < len(r):
            if l[i] < r[j]:
                array[k] = l[i]
                i += 1
            else:
                array[k] = r[j]
                j += 1
            k += 1
        while i < len(l):
            array[k] = l[i]
            i += 1
            k += 1

        while j < len(r):
            array[k] = r[j]
            j += 1
            k += 1
    return array


if __name__ == '__main__':
    arr = [2, 57, 68, 21, 55, 99, 1, 25, 28, 5, 9, 100000, 503]
    print(f"original: {arr}")
    print(f"sorted: {merge_sort(arr)}")

    maxkey = (2 ^ 32) - 1

    # Add code to do the rest of this problem
    sizes: list = []
    runtimes: list = []
    gradients: list = []

    ns = [2 ** t for t in range(3, 15)]

    for n in ns:
        print(f"Generating and sorting data with n = {n}")
        data = randint(maxkey, size=n).tolist()
        # print(data)
        sizes.append(n)
        runtimes.append(timeit("merge_sort(data)",
                               number=10, globals=globals()))
        gradients.append(runtimes[-1] / sizes[-1])

    plt.subplot(121)
    plt.grid(True, which="both")
    plt.title("MERGE SORT")
    plt.xlabel("Size of array")
    plt.ylabel("Runtime")
    plt.plot(sizes, runtimes, "--ro")

    plt.subplot(122)
    plt.grid(True, which="both")
    plt.xlabel("Size of array")
    plt.ylabel("Gradient")
    plt.plot(sizes, gradients, "--b+")
    plt.show()
    print("\nFINISHED.\n")
