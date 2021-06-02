# Brief:
***
```python
"""
...This module implements methods for finding fibonacci numbers, namely:

    1. fib_recursive() ->
        A function that, given an integer n, finds the n-th fibonacci number
        using the recursion relationship F(n) = F(n-1) + F(n-2).
        As is to be expected, this method has O(2^n) time complexity
        and is a very inefficient method of finding the solution.

    2. fib_memoized() ->
        Acknowledging that the recursive function recomputes many values,
        this method attempts to improve runtime by saving values once seen
        reusing them in later calls to the same i-th fibonacci number.
        This is expected to have O(n) time complexity, but is it optimal?

    2. fib_iterative() ->
        Using Dynamic Programming and our knowledge of the fibonacci sequence,
        this function, given an integer n, builds the n-th fibonacci sequence
        from the ground up, iterating n times and swapping values between two variables.
        This has O(n) time complexity and much better space complexity than the memoized version.

    3. fib_matrix() ->
        A better way to compute the fibonacci sequence, this function acknowledges that
        the n-1 exponential of the matrix [[1, 1], [1, 0]] contains the n-th fibonacci
        number for any n.
        Thus, we compute the n-th number by matrix exponential, which takes us
        O(log n) time.

    (c) Amittai J. Wekesa (github: @siavava), May 2021.
...
"""
```

***
# Comparison
