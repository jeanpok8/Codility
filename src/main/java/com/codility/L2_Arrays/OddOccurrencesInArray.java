package com.codility.L2_Arrays;

/*
* A non-empty array A consisting of N integers is given. The array contains an odd number of elements, and each element of the array can be paired with another element that has the same value, except for one element that is left unpaired.

For example, in array A such that:

  A[0] = 9  A[1] = 3  A[2] = 9
  A[3] = 3  A[4] = 9  A[5] = 7
  A[6] = 9
the elements at indexes 0 and 2 have value 9,
the elements at indexes 1 and 3 have value 3,
the elements at indexes 4 and 6 have value 9,
the element at index 5 has value 7 and is unpaired.
Write FrogJmp function:

class Solution { public int solution(int[] A); }

that, given an array A consisting of N integers fulfilling the above conditions, returns the value of the unpaired element.

For example, given array A such that:

  A[0] = 9  A[1] = 3  A[2] = 9
  A[3] = 3  A[4] = 9  A[5] = 7
  A[6] = 9
the function should return 7, as explained in the example above.

Assume that:

N is an odd integer within the range [1..1,000,000];
each element of array A is an integer within the range [1..1,000,000,000];
all but one of the values in A occur an even number of times.
Complexity:

expected worst-case time complexity is O(N);
expected worst-case space complexity is O(1) (not counting the storage required for input arguments).
* */


import java.util.*;

/**
 * Created by Chaklader on 6/23/18.
 */
public class OddOccurrencesInArray {


    /*
     * solution - a
     * */
    public int solution(int[] arr) {

        int result = arr[0];

        /*
         * the equal elements will cancel each other
         * */
        for (int i = 1; i < arr.length; i++) {
            result ^= arr[i];
        }

        return result;
    }


    /*
     * solution - b
     * */
    /*
     * it doesn't follow the expected worst-case space complexity is O(1)
     * */
    public int solution1(int[] arr) {

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {

            if (!list.contains(arr[i])) {
                list.add(arr[i]);
            } else {
                list.remove(arr[i]);
            }
        }

        return list.size() == 1 ? list.get(0) : -1;
    }


    /*
     * solution - c
     * */
    public int solution2(int[] A) {

        int N = A.length;

        Map<Integer, Integer> map = new LinkedHashMap<>();

        for (int i = 0; i < N; i++) {

            if (!map.containsKey(A[i])) {
                map.put(A[i], 1);
            } else {
                map.remove(A[i]);
            }
        }

        if (map.size() == 1) {

            Integer value = (new ArrayList<Integer>(map.values())).get(0);
            return value;
        }

        return -1;
    }


    public static void printMap(Map map) {

        System.out.println(Arrays.toString(map.entrySet().toArray()));
    }


    public static void main(String[] args) {

        System.out.println("Berlin");
    }
}
