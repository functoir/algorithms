from timeit import timeit
import matplotlib.pyplot as plt
from numpy.random import randint


def heapify(array: list, n: int, index: int) -> None:

    largest = index
    left = (2 * index) + 1
    right = (2 * index) + 2

    if left < n and array[left] > array[largest]:
        largest = left

    if right < n and array[right] > array[largest]:
        largest = right

    if largest != index:
        array[index], array[largest] = array[largest], array[index]
        heapify(array, n, largest)


def heap_sort(array: list) -> list:
    n = len(array)
    mid = n // 2

    for i in range(mid, -1, -1):
        heapify(array, n, i)

    for i in range(n-1, 0, -1):
        array[i], array[0] = array[0], array[i]

        heapify(array, i, 0)
    return array



if __name__ == '__main__':
    arr = [2, 57, 68, 21, 55, 99, 1, 25, 28, 5, 9, 100000, 503]
    print(f"original: {arr}")
    print(f"sorted: {heap_sort(arr)}")
    maxkey = (2 ^ 32) - 1

    # Add code to do the rest of this problem
    sizes: list = []
    runtimes: list = []
    gradients: list = []

    ns = [2 ** t for t in range(3, 20)]

    for n in ns:
        print(f"Generating and sorting data with n = {n}")
        data = randint(maxkey, size=n).tolist()
        # print(data)
        sizes.append(n)
        runtimes.append(timeit("heap_sort(data)",
                               number=10, globals=globals()))
        gradients.append(runtimes[-1] / sizes[-1])

    plt.subplot(121)
    plt.grid(True, which="both")
    plt.title("HEAP SORT")
    plt.xlabel("Size of array")
    plt.ylabel("Runtime")
    plt.plot(sizes, runtimes, "--ro")

    plt.subplot(122)
    plt.grid(True, which="both")
    plt.xlabel("Size of array")
    plt.ylabel("Gradient")
    plt.plot(sizes, gradients, "--bo")
    plt.savefig('./output/heap-sort.png', dpi=300, transparent=False)
    plt.show()
    print("\nFINISHED.\n")
