from timeit import timeit
import matplotlib.pyplot as plt
from numpy.random import randint


def exchange_sort(array: list):
    n: int = len(array)
    for i in range(n - 1):
        for j in range(i, n):
            if array[i] > array[j]:
                array[i], array[j] = array[j], array[i]
    return array


if __name__ == "__main__":
    arr = [2, 57, 68, 21, 55, 99, 1, 25, 28, 5, 9, 100000, 503]
    print(f"original: {arr}")
    print(f"sorted: {exchange_sort(arr)}")
    maxkey = (2 ^ 32) - 1

    # Add code to do the rest of this problem
    sizes: list = []
    runtimes: list = []
    gradients: list = []

    ns = [2 ** t for t in range(3, 13)]

    for n in ns:
        print(f"Generating and sorting data with n = {n}")
        data = randint(maxkey, size=n).tolist()
        # print(data)
        sizes.append(n)
        runtimes.append(timeit("exchange_sort(data)",
                               number=10, globals=globals()))
        gradients.append(runtimes[-1] / sizes[-1])

    plt.subplot(121)
    plt.grid(True, which="both")
    plt.title("EXCHANGE SORT")
    plt.xlabel("Size of array")
    plt.ylabel("Runtime")
    plt.plot(sizes, runtimes, "--ro")

    plt.subplot(122)
    plt.grid(True, which="both")
    plt.xlabel("Size of array")
    plt.ylabel("Gradient")
    plt.plot(sizes, gradients, "--b+")
    plt.savefig('./output/exchange-sort.png', dpi=300, transparent=False)
    plt.show()
    print("\nFINISHED.\n")
