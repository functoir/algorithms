from bubble_sort import *
from exchange_sort import *
from heap_sort import *
from insertion_sort import *
from merge_sort import *
from radix_sort import *


if __name__ == "__main__":
    # Add code to do the rest of this problem
    sizes: list = []
    bubble_runtimes: list = []
    exchange_runtimes: list = []
    insertion_runtimes: list = []
    heap_runtimes: list = []
    merge_runtimes: list = []
    radix_runtimes: list = []
    # gradients: list = []

    maxkey = (2 ^ 32) - 1
    ns = [2 ** t for t in range(1, 10)]

    for n in ns:
        print(f"Generating and sorting data with n = {n}")
        data = randint(maxkey, size=n).tolist()
        # print(data)
        sizes.append(n)
        bubble_runtimes.append(timeit("bubble_sort(data)",
                                     number=10, globals=globals()))
        exchange_runtimes.append(timeit("exchange_sort(data)",
                                     number=10, globals=globals()))
        insertion_runtimes.append(timeit("insertion_sort(data)",
                                     number=10, globals=globals()))
        heap_runtimes.append(timeit("heap_sort(data)",
                                     number=10, globals=globals()))
        merge_runtimes.append(timeit("merge_sort(data)",
                                     number=10, globals=globals()))
        radix_runtimes.append(timeit("radix_sort(data)",
                                     number=10, globals=globals()))

    # Generate plots
    titles = {'family': 'serif', 'color': 'blue', 'size': 20}
    axes = {'family': 'serif', 'color': 'darkred', 'size': 15}
    plt.figure(1)
    plt.grid(True, which="both")
    plt.title("COMPARISON", fontdict=titles)
    plt.xlabel("Size of array", fontdict=axes)
    plt.ylabel("Runtime", fontdict=axes)
    plt.plot(sizes, bubble_runtimes, "--ro")
    plt.plot(sizes, exchange_runtimes, "--go")
    plt.plot(sizes, insertion_runtimes, "--bo")
    plt.plot(sizes, heap_runtimes, "--co")
    plt.plot(sizes, merge_runtimes, "--ko")
    plt.plot(sizes, radix_runtimes, "--mo")

    plt.show();

    print("\nFINISHED.\n")
