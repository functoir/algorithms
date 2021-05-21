
def search(nums: list, num: int):

    # initialize low, high
    low: int = 0
    high: int = 1

    try:
        while nums[high] < num:
            low = high + 1
            high = high * 2

    except IndexError:
        pass

    count = 0
    while low <= high:
        count += 1
        print(f"{count} search: low = {low}, high = {high}")
        mid: int = (low + high) // 2
        try:
            if nums[mid] == num:
                return mid
            elif nums[mid] > num:
                high = mid - 1
            else:
                low = mid + 1

        except IndexError:
            high = mid - 1

    return "not found"


if __name__ == "__main__":
    test = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
    print(f"Test11: {search(test, 0)}\n\n")

    print(f"Test11: {search(test, 11)}\n\n")

    print(f"Test12: {search(test, 12)}\n\n")

    print(f"Test13: {search(test, 13)}\n\n")

    print(f"Test14: {search(test, 14)}\n\n")

    print(f"Test15: {search(test, 15)}\n\n")

    print(f"Test16: {search(test, 16)}\n\n")

    print(f"Test17: {search(test, 17)}\n\n")

    print(f"Test18: {search(test, 18)}\n\n")

    print(f"Test19: {search(test, 19)}\n\n")

    print(f"Test22: {search(test, 22)}\n\n")
