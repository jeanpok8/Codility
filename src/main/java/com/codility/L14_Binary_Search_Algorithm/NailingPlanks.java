package com.codility.L14_Binary_Search_Algorithm;

/*
* You are given two non-empty arrays A and B consisting of N integers. These arrays represent N planks. More precisely, A[K] is the start and B[K] the end of the K−th plank.

Next, you are given a non-empty array C consisting of M integers. This array represents M nails. More precisely, C[I] is the position where you can hammer in the I−th nail.

We say that a plank (A[K], B[K]) is nailed if there exists a nail C[I] such that A[K] ≤ C[I] ≤ B[K].

The goal is to find the minimum number of nails that must be used until all the planks are nailed. In other words, you should find a value J such that all planks will be nailed after using only the first J nails. More precisely, for every plank (A[K], B[K]) such that 0 ≤ K < N, there should exist a nail C[I] such that I < J and A[K] ≤ C[I] ≤ B[K].

For example, given arrays A, B such that:

    A[0] = 1    B[0] = 4
    A[1] = 4    B[1] = 5
    A[2] = 5    B[2] = 9
    A[3] = 8    B[3] = 10
four planks are represented: [1, 4], [4, 5], [5, 9] and [8, 10].

Given array C such that:

    C[0] = 4
    C[1] = 6
    C[2] = 7
    C[3] = 10
    C[4] = 2
if we use the following nails:

0, then planks [1, 4] and [4, 5] will both be nailed.
0, 1, then planks [1, 4], [4, 5] and [5, 9] will be nailed.
0, 1, 2, then planks [1, 4], [4, 5] and [5, 9] will be nailed.
0, 1, 2, 3, then all the planks will be nailed.
Thus, four is the minimum number of nails that, used sequentially, allow all the planks to be nailed.

Write a function:

class Solution { public int solution(int[] A, int[] B, int[] C); }

that, given two non-empty arrays A and B consisting of N integers and a non-empty array C consisting of M integers, returns the minimum number of nails that, used sequentially, allow all the planks to be nailed.

If it is not possible to nail all the planks, the function should return −1.

For example, given arrays A, B, C such that:

    A[0] = 1    B[0] = 4
    A[1] = 4    B[1] = 5
    A[2] = 5    B[2] = 9
    A[3] = 8    B[3] = 10

    C[0] = 4
    C[1] = 6
    C[2] = 7
    C[3] = 10
    C[4] = 2
the function should return 4, as explained above.

Assume that:

N and M are integers within the range [1..30,000];
each element of arrays A, B, C is an integer within the range [1..2*M];
A[K] ≤ B[K].
Complexity:

expected worst-case time complexity is O((N+M)*log(M));
expected worst-case space complexity is O(M) (not counting the storage required for input arguments).
* */


import java.util.Arrays;

/**
 * Created by Chaklader on 6/28/18.
 */
public class NailingPlanks {


    /*
     * Count the minimum number of nails that allow a series of planks
     * to be nailed. We will get the number till the last usable nails
     * for the purpose
     *
     * A plank (A[K], B[K]) is nailed if there exists a nail C[I] such that A[K] ≤ C[I] ≤ B[K].
     * The goal is to find the minimum number of nails that must be used until all the planks
     * are nailed.
     * */

    /*
     * solution -a
     */
    public static int solution(int[] A, int[] B, int[] C) {

        /*
         * A and B are also in the ascending sorted
         * order. Otherwise, do the soring please
         * */
        int planksSize = A.length;
        int nailsSize = C.length;

        int[][] sortedNails = new int[nailsSize][2];

		/*
            |------------------|
            |    Initial       |
            |------------------|
            |    4 0           |
            |    6 1           |
            |    7 2           |
            |    10 3          |
            |    2 4           |
            |                  |
            |------------------|
            |Sorted On Nail Len|
            |------------------|
            |    2 4           |
            |    4 0           |
            |    6 1           |
            |    7 2           |
            |    10 3          |
            --------------------
        */
        for (int i = 0; i < nailsSize; i++) {

            sortedNails[i][0] = C[i];
            sortedNails[i][1] = i;
        }

        /*
         * sort based on the size of the nail in ascending order
         * */
        Arrays.sort(sortedNails, (int[] x, int[] y) -> (Integer.compare(x[0], y[0])));

        int resultIndex = 0;

        for (int i = 0; i < planksSize; i++) {

            resultIndex = minIndex(A[i], B[i], sortedNails, resultIndex);

            if (resultIndex == -1) {
                return -1;
            }
        }

        int minNumberOfNails = resultIndex + 1;

        return minNumberOfNails;
    }


    /*
     * get the minimum index for the nail
     */
    public static int minIndex(int plankStart, int plankEnd, int[][] sortedNails, int oldIndex) {


        int startIndex = 0;
        int endIndex = sortedNails.length - 1;

        int N = sortedNails.length;

        /*
         * the index is in absolute value and
         * smallest possible fit for the given
         * planks
         * */
        int resultIndex = -1;

        /*
         * find the minimum index of the
         * nail for provided plank using
         * binary search
         * */
        while (startIndex <= endIndex) {

            int middle = (startIndex + endIndex) / 2;

            if (sortedNails[middle][0] < plankStart) {
                startIndex = middle + 1;
            } else if (sortedNails[middle][0] > plankEnd) {
                endIndex = middle - 1;
            } else {

                /*
                 * we try to find the minimum length for the nail
                 * */
                endIndex = middle - 1;
                resultIndex = middle;
            }
        }

        if (resultIndex == -1 || sortedNails[resultIndex][0] > plankEnd) {
            return -1;
        }

        /*
         * get the real index for the nail in the sortedNails matrix
         * */
        int minIndex = sortedNails[resultIndex][1];

        /*
         *
         * PURPOSE
         * -------
         * find the nail with lower index than the one found with binary search
         *
         *
         * ALGORITHMS
         * ----------
         *
         * i.    ResultIndex is less than N and nails length should be less
         *       than the planks end
         * ii.   Compare with the current index and find the minimum of them
         * iii.  If the found nail has index less then or equal to the old
         *       Index, return the old index
         * */
        while (resultIndex < N && sortedNails[resultIndex][0] <= plankEnd) {

            minIndex = Math.min(minIndex, sortedNails[resultIndex][1]);

            /*
             * the old result fits with the other planks of
             * higher length and the lower min index wont
             * sufis that
             * */
            if (minIndex <= oldIndex) {
                return oldIndex;
            }

            resultIndex++;
        }

        return minIndex;
    }


    /*
     * solution - b
     */
    public int solution1(int[] A, int[] B, int[] C) {


        int N = A.length;
        int M = C.length;

        int[][] sortedNail = new int[M][2];

        for (int i = 0; i < M; i++) {
            sortedNail[i][0] = C[i];
            sortedNail[i][1] = i;
        }

        /*
         * sort based on the size of the nail in ascending order
         * */
        // Arrays.sort(sortedNails, (int[] x, int[] y) -> (Integer.compare(x[0], y[0])));
        Arrays.sort(sortedNail, (int x[], int y[]) -> x[0] - y[0]);

        int result = 0;

        /*
         * find the earlist position that can nail each
         * plank, and the max value for all planks is the
         * result
         * */
        for (int i = 0; i < N; i++) {

            result = getMinIndex(A[i], B[i], sortedNail, result);

            if (result == -1) {
                return -1;
            }
        }

        return result + 1;
    }


    /*
     * for each plank , we can use binary search to get the minimal index of the
     * sorted array of nails, and we should check the candidate nails to get the
     * minimal index of the original array of nails.
     * */
    public int getMinIndex(int startPlank, int endPlank, int[][] nail, int preIndex) {

        int min = 0;
        int max = nail.length - 1;
        int minIndex = -1;

        int M = nail.length;

        while (min <= max) {

            int mid = (min + max) / 2;

            if (nail[mid][0] < startPlank) {
                min = mid + 1;
            } else if (nail[mid][0] > endPlank) {
                max = mid - 1;
            } else {
                max = mid - 1;
                minIndex = mid;
            }
        }

        if (minIndex == -1) {
            return -1;
        }

        int minIndexOrigin = nail[minIndex][1];

        /*
         * find the smallest original position
         * of nail that can nail the plank
         * */
        for (int i = minIndex; i < M; i++) {

            if (nail[i][0] > endPlank) {
                break;
            }

            minIndexOrigin = Math.min(minIndexOrigin, nail[i][1]);

            /*
             * we need the maximal index of nails to nail
             * every plank, so the smaller index can be
             * omitted
             * */
            if (minIndexOrigin <= preIndex) {
                return preIndex;
            }
        }

        return minIndexOrigin;
    }


    /*
     * solution - C
     */
    public int solution2(int[] A, int[] B, int[] C) {

        int[] nails = new int[2 * C.length + 1];
        int begin = 0;

        int end = C.length;
        int result = -1;


        /*
         * binary search. O(log(M)) times
         * */
        while (begin <= end) {

            int middle = (begin + end) / 2;

            // prepare helper array. O(M)
            // for (int i = 0; i < nails.length; i++) {
            //     nails[i] = 0;
            // }

            /*
             * set existing nails to 1. O(M)
             * */
            for (int i = 0; i < middle; i++) {
                nails[C[i]] = 1;
            }

            /*
             * count nails from 0 to i. O(M)
             * */
            int counter = 0;

            for (int i = 0; i < nails.length; i++) {
                if (nails[i] == 1) {
                    counter++;
                }
                nails[i] = counter;
            }

            /*
             * find nails number between A[i] and B[i]
             * */
            boolean isFound = true;

            for (int i = 0; i < A.length; i++) {

                if (nails[B[i]] - nails[A[i] - 1] == 0) {
                    isFound = false;
                    break;
                }
            }

            if (isFound) {
                end = middle - 1;
                result = middle;
            } else {
                begin = middle + 1;
            }
        }

        return result;
    }


    /*
     * Displays a 2d array in the console, one line per row
     * */
    static void printMatrix(int[][] grid) {

        for (int r = 0; r < grid.length; r++) {

            for (int c = 0; c < grid[r].length; c++) {

                System.out.print(grid[r][c] + " ");
            }

            System.out.println();
        }
    }
}
