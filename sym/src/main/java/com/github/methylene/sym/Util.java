package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A collection of array related utility methods.
 */
public class Util {

  /**
   * Creates an array of [index, element] pairs from given array.
   * @param a an array
   * @return an array of the [index, element] pairs in {@code a}
   */
  public static int[][] withIndex(int[] a) {
    int[][] result = new int[a.length][];
    for (int i = 0; i < a.length; i += 1)
      result[i] = new int[]{i, a[i]};
    return result;
  }

  /**
   * Creates an array of the numbers {@code start} to {@code end} in sequence.
   * @param start a number
   * @param end a number
   * @param inclusive whether or not the greater of {@code start} and {@code end}
   *                  should be included in the result
   * @return the sequence from {@code start} to {@code end}
   */
  public static int[] sequence(int start, int end, boolean inclusive) {
    int direction = start < end ? 1 : -1;
    if (inclusive)
      end += direction;
    int[] result = new int[direction * (end - start)];
    for (int i = 0; i < result.length; i += 1)
      result[i] = start + direction * i;
    return result;
  }

  /**
   * Creates an array of the numbers {@code 0} (included) to {@code end} (excluded) in sequence.
   * If {@code end == 0} an empty array is returned.
   * @param end a non-negative number
   * @return an array of length {@code end}
   * @throws java.lang.IllegalArgumentException if {@code end} is negative
   */
  public static int[] sequence(int end) {
    if (end < 0)
      throw new IllegalArgumentException("illegal end: " + end);
    return sequence(0, end, false);
  }

  /**
   * Find element in array using exhaustive search. This is not very fast,
   * but may be sufficient for one-off searches.
   * @param ints an array
   * @param k a number
   * @return the least number {@code i >= 0} so that {@code ints[i] = k}, or {@code -1}
   */
  public static int indexOf(int[] ints, int k) {
    for (int i = 0; i < ints.length; i += 1)
      if (ints[i] == k)
        return i;
    return -1;
  }

  /**
   * Add a fixed number to each element of given array.
   * @param a an array of numbers
   * @param k a number
   * @return the array {@code b} defined as {@code b[i] = a[i] + k}
   */
  public static int[] add(int[] a, int k) {
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i] + k;
    return result;
  }

  /**
   * Add sequence numbers at the end of given array. If the input array is not shorter than the input,
   * it is returned. Otherwise a new array is created.
   * @param a an array
   * @param targetLength a non negative number
   * @return the array {@code b} defined as {@code b[i] = a[i]} if {@code i < a.length},
   * and {@code b[i] = i} if {@code a.length <= i < targetLength}
   */
  public static int[] padding(int[] a, int targetLength) {
    if (targetLength <= a.length)
      return a;
    int[] result = new int[targetLength];
    System.arraycopy(a, 0, result, 0, a.length);
    for (int i = a.length; i < targetLength; i += 1)
      result[i] = i;
    return result;
  }

  /**
   * Check that the input is a <a href="">ranking</a>. Each integer from {@code 0} to
   * {@code a.length - 1} must appear exactly once.
   * @param a an array
   * @throws java.lang.IllegalArgumentException if a is not valid
   */
  public static void validateRanking(int[] a) {
    boolean[] used = new boolean[a.length];
    for (int i : a) {
      if (i < 0 || i >= a.length)
        throw new IllegalArgumentException("invalid: " + Arrays.toString(a));
      if (used[i])
        throw new IllegalArgumentException("invalid: " + Arrays.toString(a) + ", duplicate: " + i);
      used[i] = true;
    }
  }

  /**
   * Produce an array of distinct random numbers.
   * @param size      length of array to generate
   * @param maxFactor controls the size of random numbers that are generated. All generated numbers will be
   *                  smaller than {@code size * maxFactor}.
   *                  This number must be not be less than 2.
   * @return a random array of {@code size} distinct nonnegative integers, all less than {@code size * maxFactor}
   * @throws java.lang.IllegalArgumentException if {@code factor} is less than 2
   */
  public static int[] distinctInts(int size, int maxFactor) {
    if (maxFactor < 2)
      throw new IllegalArgumentException("must not be less than 2: " + maxFactor);
    boolean[] test = new boolean[size * maxFactor];
    int[] result = new int[size];
    for (int i = 0; i < size; i += 1) {
      double random = Math.random();
      int candidate = (int) (size * maxFactor * random);
      int direction = candidate % 2 == 0 ? -1 : 1;
      while (test[candidate]) {
        candidate += direction;
        if (candidate == test.length)
          candidate = 0;
        else if (candidate == -1)
          candidate = test.length - 1;
      }
      test[candidate] = true;
      result[i] = candidate;
    }
    return result;
  }

  /**
   * Generates an array of distinct strings of the requested length. 
   * The array always starts with {@code "a"} and then all lower case one letter strings in lexicographic order.
   * Next come all lowercase two letter
   * strings starting with {@code "aa"}, in lexicographic order.
   * Note that the array returned by this method is sorted if and only if {@code n < 27},
   * because {@code "b".compareTo("aa") > 0}.
   * @param n length of array to generate
   * @return a list of distinct strings of length n
   */
  public static String[] symbols(int n) {
    String[] r = new String[n];
    String s = "a";
    for (int i = 0; i < n; i += 1) {
      r[i] = s;
      s = nextString(s);
    }
    return r;
  }

  /**
   * Utility method used by symbols method.
   * @param s a string
   * @return a string that's different from {@code s}
   */
  private static String nextString(String s) {
    char last = s.charAt(s.length() - 1);
    if (last == 'z') {
      int nflip = 1;
      while (s.length() > nflip && s.charAt(s.length() - 1 - nflip) == 'z')
        nflip += 1;
      if (nflip == s.length()) {
        StringBuilder news = new StringBuilder();
        for (int i = 0; i < nflip; i += 1)
          news.append('a');
        return news.append('a').toString();
      } else {
        StringBuilder news = new StringBuilder(s.substring(0, s.length() - nflip - 1));
        news.append((char) (s.charAt(s.length() - 1 - nflip) + 1));
        for (int i = 0; i < nflip; i += 1)
          news.append('a');
        return news.toString();
      }
    } else {
      return s.substring(0, s.length() - 1) + ((char) (last + 1));
    }
  }

  /**
   * Produce {@code length} random numbers between {@code 0} and {@code maxNumber}
   * @param maxNumber upper bound of random numbers
   * @param length result length
   * @return an array of random numbers
   */
  public static int[] randomNumbers(int maxNumber, int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = (int) (maxNumber * Math.random());
    return result;
  }

  public static Integer[] box(int[] numbers) {
    Integer[] result = new Integer[numbers.length];
    for (int i = 0; i < numbers.length; i += 1)
      result[i] = numbers[i];
    return result;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static char[] sortedCopy(char[] input) {
    char[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static short[] sortedCopy(short[] input) {
    short[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static double[] sortedCopy(double[] input) {
    double[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static float[] sortedCopy(float[] input) {
    float[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static long[] sortedCopy(long[] input) {
    long[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static byte[] sortedCopy(byte[] input) {
    byte[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static Comparable[] sortedCopy(Comparable[] input) {
    Comparable[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @param comp a comparator
   * @return a sorted copy of the input
   */
  public static Object[] sortedCopy(Object[] input, Comparator comp) {
    Object[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted, comp);
    return sorted;
  }

  /**
   * Returns a sorted copy of the input.
   * @param input an array
   * @return a sorted copy of the input
   */
  public static int[] sortedCopy(int[] input) {
    int[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

}
