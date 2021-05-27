from timeit import timeit
import matplotlib.pyplot as plt
from numpy.random import randint


def radix_sort(array: list) -> list:
    # Find the maximum element in the array.
    highest: int = max(array)

    # count the number of digits in the highest element.
    digits: int = 1
    while highest > 1:
        highest /= 10
        digits += 1

    # track the current place value
    place_value = 1

    # check each number's place value and sort
    while digits > 0:
        array = count_sort(array, place_value)
        place_value *= 10
        digits -= 1

    return array


def count_sort(array: list, place_value) -> list:
    # init counters
    counters = [0] * 10
    length: int = len(array)

    # count occurrences at each index
    for i in range(length):
        position: int = (array[i] // place_value) % 10
        counters[position] += 1

    # find cumulative indexes
    for i in range(1, 10):
        counters[i] += counters[i - 1]

    # reconstruct the index
    reconstructed: list = [0] * length
    i = length - 1
    while i >= 0:
        element = array[i]
        position = (element // place_value) % 10
        new_position = counters[position] - 1
        reconstructed[new_position] = element
        counters[position] -= 1
        i -= 1
    return reconstructed


if __name__ == '__main__':
    arr = [2, 57, 68, 21, 55, 99, 1, 25, 28, 5, 9, 100000, 503]
    print(f"original: {arr}")
    print(f"sorted: {radix_sort(arr)}")
    maxkey = (2 ^ 32) - 1

    # Add code to do the rest of this problem
    sizes: list = []
    radix_runtimes: list = []
    gradients: list = []

    ns = [2 ** t for t in range(3, 20)]

    for n in ns:
        print(f"Generating and sorting data with n = {n}")
        data = randint(maxkey, size=n).tolist()
        # print(data)
        sizes.append(n)
        radix_runtimes.append(timeit("radix_sort(data)",
                                     number=10, globals=globals()))
        gradients.append(radix_runtimes[-1] / sizes[-1])

    plt.subplot(121)
    plt.grid(True, which="both")
    plt.title("COMBINED SORT")
    plt.xlabel("Size of array")
    plt.ylabel("Runtime")
    plt.plot(sizes, radix_runtimes, "--ro")

    plt.subplot(122)
    plt.grid(True, which="both")
    plt.xlabel("Size of array")
    plt.ylabel("Gradient")
    plt.plot(sizes, gradients, "--b+")
    plt.show()
    print("\nFINISHED.\n")
