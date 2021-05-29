
// in-place swap
let swap i j (arr : 'a []) =
    let tmp = arr.[i]
    arr.[i] <- arr.[j]
    arr.[j] <- tmp

// http://en.wikipedia.org/wiki/Bubble_sort
let bubbleSort arr =
    let rec loop (arr : 'a []) =
        let mutable swaps = 0
        for i = 0 to arr.Length - 2 do
            if arr.[i] > arr.[i+1] then
                swap i (i+1) arr
                swaps <- swaps + 1

        if swaps > 0 then loop arr else arr

    loop arr

// http://en.wikipedia.org/wiki/Insertion_sort
let insertionSort (arr : 'a []) =
    for i = 1 to arr.Length - 1 do
        let mutable j = i
        while j >= 1 && arr.[j] < arr.[j-1] do
            swap j (j-1) arr
            j <- j - 1
    arr

// http://en.wikipedia.org/wiki/Selection_sort
let selectionSort (arr : 'a []) =
    let rec loop n (arr : 'a []) =
        if n >= arr.Length - 1 then arr
        else
            let mutable x, mini = arr.[n], n
            for i = n+1 to arr.Length - 1 do
                if arr.[i] < x then
                    x    <- arr.[i]
                    mini <- i
            if n <> mini then swap n (mini) arr

            loop (n+1) arr

    loop 0 arr

// http://en.wikipedia.org/wiki/Merge_sort
let rec mergeSort (arr : 'a []) =
    let split (arr : _ array) =
        let n = arr.Length
        arr.[0..n/2-1], arr.[n/2..n-1]
 
    let rec merge (l : 'a array) (r : 'a array) =
        let n = l.Length + r.Length
        let res = Array.zeroCreate<'a> n
        let mutable i, j = 0, 0
        for k = 0 to n-1 do
            if i >= l.Length   then res.[k] <- r.[j]; j <- j + 1
            elif j >= r.Length then res.[k] <- l.[i]; i <- i + 1
            elif l.[i] < r.[j] then res.[k] <- l.[i]; i <- i + 1
            else res.[k] <- r.[j]; j <- j + 1
 
        res

    match arr with 
    | [||] | [| _ |] -> arr
    | _ -> let (x, y) = split arr
           merge (mergeSort x) (mergeSort y)

// http://en.wikipedia.org/wiki/Quicksort
let rec quickSort (arr : 'a []) =
    match arr with
    | [||] | [| _ |] -> arr
    | _ -> let l, (r, pivots) = Array.partition (fun n -> n < arr.[0]) arr
                                |> (fun (l, r) -> l, r |> Array.partition (fun n -> n <> arr.[0]))
           Array.concat <| seq { yield (quickSort l); yield pivots; yield (quickSort r) }

/// left is the index of the leftmost element of the subarray
/// right is the inde of the rightmost element of the subarray
/// number of elements in subarray = right - left + 1
let inline partition(arr : 'a [], left, right, pivotIdx) = 
    let pivot = arr.[pivotIdx]    
    swap pivotIdx right arr // move pivot to the end

    let mutable storeIdx = left
    for i = left to right - 1 do // left <= i < right
        if arr.[i] <= pivot then 
            swap i storeIdx arr
            storeIdx <- storeIdx + 1
    swap storeIdx right arr // move pivot to its final place
    storeIdx

/// in-place version of quick sort
let inline quickSortInPlace (arr : 'a []) =
    let rec loop (arr : 'a [], left, right) =
        // if the array has 2 or more items
        if left < right then
            // use the middle element, and sort the elements according to the value of the pivot
            // and return the new idx of the pivot
            let pivotIdx = (left + right) / 2
            let pivotNewIdx = partition(arr, left, right, pivotIdx)
            
            // recursively sort elements either side of the new pivot
            loop(arr, left, pivotNewIdx - 1)
            loop(arr, pivotNewIdx + 1, right)

    loop(arr, 0, arr.Length - 1)
    arr