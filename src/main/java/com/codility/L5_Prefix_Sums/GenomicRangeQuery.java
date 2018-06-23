package com.codility.L5_Prefix_Sums;

/*
* A DNA sequence can be represented as a string consisting of the letters A, C, G and T, which correspond to the types of successive nucleotides in the sequence. Each nucleotide has an impact getImpactFactor, which is an integer. Nucleotides of types A, C, G and T have impact factors of 1, 2, 3 and 4, respectively. You are going to answer several queries of the form: What is the minimal impact getImpactFactor of nucleotides contained in a particular part of the given DNA sequence?

The DNA sequence is given as a non-empty string S = S[0]S[1]...S[N-1] consisting of N characters. There are M queries, which are given in non-empty arrays P and Q, each consisting of M integers. The K-th query (0 ≤ K < M) requires you to find the minimal impact getImpactFactor of nucleotides contained in the DNA sequence between positions P[K] and Q[K] (inclusive).

For example, consider string S = CAGCCTA and arrays P, Q such that:

    P[0] = 2    Q[0] = 4
    P[1] = 5    Q[1] = 5
    P[2] = 0    Q[2] = 6
The answers to these M = 3 queries are as follows:

The part of the DNA between positions 2 and 4 contains nucleotides G and C (twice), whose impact factors are 3 and 2 respectively, so the answer is 2.
The part between positions 5 and 5 contains a single nucleotide T, whose impact getImpactFactor is 4, so the answer is 4.
The part between positions 0 and 6 (the whole string) contains all nucleotides, in particular nucleotide A whose impact getImpactFactor is 1, so the answer is 1.
Write a function:

class Solution { public int[] solution(String S, int[] P, int[] Q); }

that, given a non-empty string S consisting of N characters and two non-empty arrays P and Q consisting of M integers, returns an array consisting of M integers specifying the consecutive answers to all queries.

The sequence should be returned as:

a Results structure (in C), or
a vector of integers (in C++), or
a Results record (in Pascal), or
an array of integers (in any other programming language).
For example, given the string S = CAGCCTA and arrays P, Q such that:

    P[0] = 2    Q[0] = 4
    P[1] = 5    Q[1] = 5
    P[2] = 0    Q[2] = 6
the function should return the values [2, 4, 1], as explained above.

Assume that:

N is an integer within the range [1..100,000];
M is an integer within the range [1..50,000];
each element of arrays P, Q is an integer within the range [0..N − 1];
P[K] ≤ Q[K], where 0 ≤ K < M;
string S consists only of upper-case English letters A, C, G, T.
Complexity:

expected worst-case time complexity is O(N+M);
expected worst-case space complexity is O(N) (not counting the storage required for input arguments).
* */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chaklader on 6/23/18.
 */
public class GenomicRangeQuery {


    /*
     * solution - a
     * */
    public int[] solution(String S, int[] P, int[] Q) {

        int[] impactFactors = new int[P.length];

        for (int i = 0; i < P.length; i++) {
            impactFactors[i] = getImpactFactor(S, P[i], Q[i]);
        }

        return impactFactors;
    }

    public int getImpactFactor(String S, int i, int j) {

        if (S.substring(i, j + 1).contains("A")) {
            return 1;
        } else if (S.substring(i, j + 1).contains("C")) {
            return 2;
        } else if (S.substring(i, j + 1).contains("G")) {
            return 3;
        }

        return 4;
    }


    /*
     * solution - b
     * */
    public static int[] solution1(String S, int[] P, int[] Q) {

        Map<Integer, ArrayList<Integer>> prefSums = getPrefSum(S);
        int[] res = new int[P.length];

        for (int i = 0; i < Q.length; i++) {

            for (int j = 1; j <= 4; j++) {

                int high = prefSums.get(j).get(Q[i]);
                int low = P[i] == 0 ? 0 : prefSums.get(j).get(P[i] - 1);

                if (high - low > 0) {
                    res[i] = j;
                    break;
                }
            }
        }
        return res;
    }


    public static Map<Integer, ArrayList<Integer>> getPrefSum(String s) {

        Map<Integer, ArrayList<Integer>> prefSums = new HashMap<Integer, ArrayList<Integer>>();

        for (int j = 0; j < 4; j++) {
            prefSums.put(j + 1, new ArrayList<Integer>());
        }

        int[] counters = new int[4];

        for (int i = 0; i < s.length(); i++) {

            switch (s.charAt(i)) {

                case 'A':
                    counters[0]++;
                    break;

                case 'C':
                    counters[1]++;
                    break;

                case 'G':
                    counters[2]++;
                    break;

                case 'T':
                    counters[3]++;
                    break;

                default:
                    break;
            }

            for (int j = 0; j < 4; j++) {
                prefSums.get(j + 1).add(counters[j]);
            }
        }

        return prefSums;
    }


    /*
     * solution - c
     * */
    /*
     * This solution uses prefix. Time complexity is O(N + M)
     * */
    public int[] solution2(String S, int[] P, int[] Q) {

        /*
         * used jagged array to hold the prefix sums of each A, C
         * and G genoms we don't need to get prefix sums of T
         * */

        int[][] genoms = new int[3][S.length() + 1];

        /*
         * If the char is found in the index i, then we set it
         * to be 1 else they are 0 3 short values are needed
         * for this reason*/
        short a, c, g;

        for (int i = 0; i < S.length(); i++) {

            a = 0;
            c = 0;
            g = 0;

            if ('A' == (S.charAt(i))) {
                a = 1;
            }

            if ('C' == (S.charAt(i))) {
                c = 1;
            }

            if ('G' == (S.charAt(i))) {
                g = 1;
            }

            /*
             * calculate prefix sums.
             * */
            genoms[0][i + 1] = genoms[0][i] + a;
            genoms[1][i + 1] = genoms[1][i] + c;
            genoms[2][i + 1] = genoms[2][i] + g;
        }

        int[] result = new int[P.length];


        /*
         * Go through the provided P[] and Q[] arrays as intervals
         * */
        for (int i = 0; i < P.length; i++) {

            int fromIndex = P[i] + 1;
            int toIndex = Q[i] + 1;


            /*
             * If the substring contains a, then genoms[0][toIndex] - genoms[0][fromIndex-1] > 0
             * */
            if (genoms[0][toIndex] - genoms[0][fromIndex - 1] > 0) {
                result[i] = 1;
            } else if (genoms[1][toIndex] - genoms[1][fromIndex - 1] > 0) {
                result[i] = 2;
            } else if (genoms[2][toIndex] - genoms[2][fromIndex - 1] > 0) {
                result[i] = 3;
            } else {
                result[i] = 4;
            }
        }

        return result;
    }


    /*
     * solution - d
     * */
    public int[] solution3(String s, int[] p, int[] q) {

        final int[] cnt = new int[(s.length() + 1) * 4];

        for (int i = 0; i < s.length(); i++) {

            final char c = s.charAt(i);
            final int k = (i + 1) * 4;

            cnt[k] = cnt[k - 4];
            cnt[k + 1] = cnt[k - 3];
            cnt[k + 2] = cnt[k - 2];
            cnt[k + 3] = cnt[k - 1];

            switch (c) {
                case 'A':
                    cnt[k]++;
                    break;
                case 'C':
                    cnt[k + 1]++;
                    break;
                case 'G':
                    cnt[k + 2]++;
                    break;
                case 'T':
                    cnt[k + 3]++;
                    break;
            }
        }

        for (int i = 0; i < p.length; i++) {

            final int from = p[i];
            final int to = q[i];

            int m = 4 * from;
            int n = 4 * (to + 1);

            if (cnt[n] - cnt[m] > 0) {
                p[i] = 1;
                continue;
            }

            if (cnt[n + 1] - cnt[m + 1] > 0) {
                p[i] = 2;
                continue;
            }

            if (cnt[n + 2] - cnt[m + 2] > 0) {
                p[i] = 3;
                continue;
            }

            if (cnt[n + 3] - cnt[m + 3] > 0) {
                p[i] = 4;
            }
        }
        return p;
    }
}
