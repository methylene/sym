package com.github.methylene.sym;

import static java.lang.Math.max;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A collection of array related utility methods.
 */
public final class Util {

  private Util() {}

  /**
   * Creates an array of [element, index] pairs from given array.
   * @param a an array
   * @return an array of the [element, index] pairs in {@code a}
   */
  public static int[][] withIndex(int[] a) {
    int[][] result = new int[a.length][];
    for (int i = 0; i < a.length; i += 1)
      result[i] = new int[]{a[i], i};
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
   * Find element in array by comparing each element in sequence, starting at index {@code 0}.
   * @param a an array
   * @param el a number
   * @return the least non-negative number {@code i} so that {@code a[i] = el}, or {@code -1} if {@code el} is not
   * found in {@code a}.
   */
  public static int indexOf(int[] a, int el) {
    for (int i = 0; i < a.length; i += 1)
      if (a[i] == el)
        return i;
    return -1;
  }

  /**
   * Calculates the factorial.
   * @param n a nonnegative number
   * @return the factorial of {@code n}
   * @throws java.lang.IllegalArgumentException if n is negative
   */
  public static long factorial(int n) {
    if (n < 0) {throw new IllegalArgumentException("negative number is not allowed");}
    if (n > 20) {throw new IllegalArgumentException("preventing long overflow");}
    long seed = 1;
    for (int i = 1; i <= n; i += 1)
      seed = seed * i;
    return seed;
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
   * Calculate the maximum of the numbers in the input.
   * @param a an array
   * @return the maximum of all numbers in {@code a}
   */
  public static int max(int[] a) {
    if (a.length == 0)
      throw new IllegalArgumentException("argument must not be empty");
    int maxIndex = a[0];
    for (int index : a)
      maxIndex = Math.max(maxIndex, index);
    return maxIndex;
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
   * Find a pair of duplicate indexes.
   * @param input
   * @param start the index where to start looking in the array
   * @return A pair {@code i, j} of indexes so that {@code input[i] == input[j]}
   * @throws java.lang.IllegalArgumentException if no duplicates were found
   */
  public static int[] duplicateIndexes(int[] input, int start) {
    int max = 0;
    for (int j : input)
      max = Math.max(max, j);
    int[] test = new int[max + 1];
    Arrays.fill(test, -1);
    for (int _: input) {
      if (test[input[start]] == -1)
        test[input[start]] = start;
      else
        return new int[]{test[input[start]], start};
      start = (start + 1) % input.length;
    }
    throw new IllegalArgumentException("no duplicates found");
  }

  public static int[] duplicateIndexes(int[] input) {
    return duplicateIndexes(input, (int) (Math.random() * input.length));
  }

  public static int[] duplicateIndexes(Object[] input, Comparator comp) {
    @SuppressWarnings("unchecked")
    Map<Object, Integer> test = new TreeMap<Object, Integer>(comp);
    int start = (int) (Math.random() * input.length);
    for (Object _: input) {
      if (!test.containsKey(input[start])) {
        test.put(input[start], start);
      } else {
        return new int[]{test.get(input[start]), start};
      }
      start = (start + 1) % input.length;
    }
    throw new IllegalArgumentException("no duplicates found");
  }

  public static int[] duplicateIndexes(long[] input) {
    int max = 0;
    for (long i : input) {
      if (i > Integer.MAX_VALUE)
        throw new IllegalArgumentException("too large: " + i);
      max = (int) Math.max(max, i);
    }
    int[] test = new int[max + 1];
    Arrays.fill(test, -1);
    int start = (int) (Math.random() * input.length);
    for (long _: input) {
      if (test[(int) input[start]] == -1) {
        test[(int) input[start]] = start;
      } else {
        return new int[]{test[(int) input[start]], start};
      }
      start = (start + 1) % input.length;
    }
    return new int[0];
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
  @SuppressWarnings("unchecked")
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

  static void lengthFailure() {
    throw new IllegalArgumentException("length mismatch");
  }

  static void indexFailure() {
    throw new IllegalArgumentException("index out of bounds");
  }

  static void slotFailure() {
    throw new IllegalArgumentException("could not find a free spot");
  }

  static void negativeFailure() {
    throw new IllegalArgumentException("negative number not allowed");
  }

  static void nullFailure() {
    throw new IllegalArgumentException("null values are not allowed");
  }

  static void duplicateFailure() {
    throw new IllegalArgumentException("repeated values are not allowed");
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code array} is sorted
   */
  public static boolean isSorted(int[] input) {
    if (input.length == 0) {return true;}
    int test = input[0];
    for (int i: input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code array} is sorted
   */
  public static boolean isSorted(char[] input) {
    if (input.length == 0) {return true;}
    int test = input[0];
    for (int i: input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code array} is sorted
   */
  public static boolean isSorted(float[] input) {
    if (input.length == 0) {return true;}
    float test = input[0];
    for (float i: input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code array} is sorted
   */
  public static boolean isSorted(double[] input) {
    if (input.length == 0) {return true;}
    double test = input[0];
    for (double i: input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code array} is sorted
   */
  public static boolean isSorted(long[] input) {
    if (input.length == 0) {return true;}
    long test = input[0];
    for (long i: input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code array} is sorted
   * @throws java.lang.NullPointerException if the input contains null
   */
  public static <E extends Comparable<E>> boolean isSorted(E[] input) {
    if (input.length == 0) {return true;}
    E test = input[0];
    if (test == null) {throw new NullPointerException("null is not allowed");}
    for (E i: input) {
      if (i.compareTo(test) < 0) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @param comparator a comparator
   * @return true if the {@code array} is sorted
   * @throws java.lang.NullPointerException if the input contains null
   */
  public static <E> boolean isSorted(Comparator<E> comparator, E[] input) {
    if (input.length == 0) {return true;}
    E test = input[0];
    if (test == null) {throw new NullPointerException("null is not allowed");}
    for (E i: input) {
      if (comparator.compare(i, test) < 0) {return false;}
      test = i;
    }
    return true;
  }

}
