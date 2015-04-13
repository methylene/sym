package com.github.methylene.sym;

import static java.lang.System.arraycopy;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import java.util.*;

/**
 * A collection of array related utilities
 */
public final class Util {

  /** An empty array of Bytes */
  static final Byte[] BOX_BYTE_0 = new Byte[0];
  /** An empty array of Shorts */
  static final Short[] BOX_SHORT_0 = new Short[0];
  /** An empty array of Characters */
  static final Character[] BOX_CHAR_0 = new Character[0];
  /** An empty array of Longs */
  static final Long[] BOX_LONG_0 = new Long[0];
  /** An empty array of Floats */
  static final Float[] BOX_FLOAT_0 = new Float[0];
  /** An empty array of Doubles */
  static final Double[] BOX_DOUBLE_0 = new Double[0];
  /** An empty array of Integers */
  static final Integer[] BOX_INT_0 = new Integer[0];
  /** An empty array of ints */
  public static final int[] INT_0 = new int[]{};

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
   * @param skip number of matches to skip; if {@code skip = 0} the index of the first match, if any, will be returned
   * @return the least non-negative number {@code i} so that {@code a[i] = el}, or {@code -1} if {@code el} is not
   * found in {@code a}, or if all occurences are skipped
   * @throws java.lang.IllegalArgumentException if {@code skip < 0}
   */
  public static int indexOf(int[] a, int el, final int skip) {
    if (skip < 0)
      negativeFailure();
    int cnt = 0;
    for (int i = 0; i < a.length; i += 1)
      if (a[i] == el)
        if (cnt++ >= skip)
          return i;
    return -1;
  }

  static int indexOf(int[] a, int el) {
    return indexOf(a, el, 0);
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
   * Shuffle the input array in place.
   * @param a an array
   * @return the shuffled array
   */
  public static int[] shuffle(int[] a) {
    Random r = new Random();
    for (int i = a.length - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      if (j != i) {
        int tmp = a[j];
        a[j] = a[i];
        a[i] = tmp;
      }
    }
    return a;
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

  /* ================= box ================= */

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Integer[] box(int[] a) {
    if (a.length == 0)
      return BOX_INT_0;
    Integer[] result = new Integer[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
    return result;
  }

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Byte[] box(byte[] a) {
    if (a.length == 0)
      return BOX_BYTE_0;
    Byte[] result = new Byte[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
    return result;
  }

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Short[] box(short[] a) {
    if (a.length == 0)
      return BOX_SHORT_0;
    Short[] result = new Short[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
    return result;
  }

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Long[] box(long[] a) {
    if (a.length == 0)
      return BOX_LONG_0;
    Long[] result = new Long[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
    return result;
  }

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Float[] box(float[] a) {
    if (a.length == 0)
      return BOX_FLOAT_0;
    Float[] result = new Float[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
    return result;
  }

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Double[] box(double[] a) {
    if (a.length == 0)
      return BOX_DOUBLE_0;
    Double[] result = new Double[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
    return result;
  }

  /**
   * Box every value in the input. Return an array of boxed values.
   * @param a an array of primitives
   * @return an array of boxed primitives
   */
  public static Character[] box(char[] a) {
    if (a.length == 0)
      return BOX_CHAR_0;
    Character[] result = new Character[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i];
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

  static void checkLength(int rankingLength, int inputLength) {
    if (inputLength < rankingLength)
      throw new IllegalArgumentException("not enough input: minimum input length is " + rankingLength
          + ", but input length is " + inputLength);
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
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(int[] input) {
    if (input.length < 2) {return true;}
    int test = input[0];
    for (int i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(byte[] input) {
    if (input.length < 2) {return true;}
    byte test = input[0];
    for (byte i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(short[] input) {
    if (input.length < 2) {return true;}
    short test = input[0];
    for (short i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(char[] input) {
    if (input.length < 2) {return true;}
    int test = input[0];
    for (int i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(float[] input) {
    if (input.length < 2) {return true;}
    float test = input[0];
    for (float i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(double[] input) {
    if (input.length < 2) {return true;}
    double test = input[0];
    for (double i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   */
  public static boolean isSorted(long[] input) {
    if (input.length < 2) {return true;}
    long test = input[0];
    for (long i : input) {
      if (i < test) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @return true if the {@code input} is sorted
   * @throws java.lang.NullPointerException if the input contains null
   */
  public static <E extends Comparable<E>> boolean isSorted(E[] input) {
    if (input.length == 0) {return true;}
    E test = input[0];
    if (test == null) {throw new NullPointerException("null is not allowed");}
    for (E i : input) {
      if (i.compareTo(test) < 0) {return false;}
      test = i;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an iterable
   * @return true if the {@code input} is sorted
   * @throws java.lang.NullPointerException if the input contains null
   */
  public static <E extends Comparable<E>> boolean isSorted(Iterable<E> input) {
    Iterator<E> iterator = input.iterator();
    if (!iterator.hasNext()) {return true;}
    E test = iterator.next();
    if (test == null) {throw new NullPointerException("null is not allowed");}
    while (iterator.hasNext()) {
      E next = iterator.next();
      if (next.compareTo(test) < 0) {return false;}
      test = next;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an iterable
   * @return true if the {@code input} is sorted
   * @throws java.lang.NullPointerException if the input contains null
   */
  public static <E> boolean isSorted(Comparator<E> comparator, Iterable<E> input) {
    Iterator<E> iterator = input.iterator();
    if (!iterator.hasNext()) {return true;}
    E test = iterator.next();
    if (test == null)
      throw new NullPointerException("null is not allowed");
    while (iterator.hasNext()) {
      E next = iterator.next();
      if (next == null)
        throw new NullPointerException("null is not allowed");
      if (comparator.compare(next, test) < 0) {return false;}
      test = next;
    }
    return true;
  }

  /**
   * Test if input is sorted
   * @param input an array
   * @param comparator a comparator
   * @return true if the {@code input} is sorted
   * @throws java.lang.NullPointerException if the input contains null
   */
  @SuppressWarnings("unchecked")
  public static <E> boolean isSorted(Comparator<E> comparator, Object[] input) {
    if (input.length == 0) {return true;}
    Object test = input[0];
    if (test == null) {throw new NullPointerException("null is not allowed");}
    for (Object i : input) {
      if (comparator.compare((E) i, (E) test) < 0) {return false;}
      test = i;
    }
    return true;
  }

  static int exceptionalBinarySearch(int[] sorted, int el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(byte[] sorted, byte el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(short[] sorted, short el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(long[] sorted, long el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(float[] sorted, float el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(double[] sorted, double el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(char[] sorted, char el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static int exceptionalBinarySearch(Comparable[] sorted, Comparable el) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static <E> int exceptionalBinarySearch(Object[] sorted, E el, Comparator<E> comparator) {
    @SuppressWarnings("unchecked")
    final int idx = binarySearch(sorted, el, (Comparator) comparator);
    if (idx < 0)
      slotFailure();
    return idx;
  }

  static void checkEqualLength(int[] a, int[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(byte[] a, byte[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(short[] a, short[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(float[] a, float[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(double[] a, double[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(long[] a, long[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(char[] a, char[] b) {
    if (a.length != b.length)
      lengthFailure();
  }
  static void checkEqualLength(Object[] a, Object[] b) {
    if (a.length != b.length)
      lengthFailure();
  }

  /* ================= unique ================= */

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static int[] unique(int[] sorted) {
    int[] result = new int[sorted.length];
    int idx = 0;
    int previous = sorted[0];
    for (int i : sorted) {
      if (i != previous) {
        result[idx++] = previous;
        previous = i;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static byte[] unique(byte[] sorted) {
    byte[] result = new byte[sorted.length];
    int idx = 0;
    byte previous = sorted[0];
    for (byte b : sorted) {
      if (b != previous) {
        result[idx++] = previous;
        previous = b;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static short[] unique(short[] sorted) {
    short[] result = new short[sorted.length];
    int idx = 0;
    short previous = sorted[0];
    for (short n : sorted) {
      if (n != previous) {
        result[idx++] = previous;
        previous = n;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static long[] unique(long[] sorted) {
    long[] result = new long[sorted.length];
    int idx = 0;
    long previous = sorted[0];
    for (long n : sorted) {
      if (n != previous) {
        result[idx++] = previous;
        previous = n;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static float[] unique(float[] sorted) {
    float[] result = new float[sorted.length];
    int idx = 0;
    float previous = sorted[0];
    for (float f : sorted) {
      if (f != previous) {
        result[idx++] = previous;
        previous = f;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static double[] unique(double[] sorted) {
    double[] result = new double[sorted.length];
    int idx = 0;
    double previous = sorted[0];
    for (double d : sorted) {
      if (d != previous) {
        result[idx++] = previous;
        previous = d;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /**
   * Returns a copy of the input array with duplicates removed.
   * This will only produce the expected result if the input is sorted.
   * @param sorted a sorted array
   * @return the unique sorted array
   */
  public static char[] unique(char[] sorted) {
    char[] result = new char[sorted.length];
    int idx = 0;
    char previous = sorted[0];
    for (char c : sorted) {
      if (c != previous) {
        result[idx++] = previous;
        previous = c;
      }
    }
    result[idx++] = previous;
    return idx == sorted.length ? result : copyOf(result, idx);
  }

  /* ================= isUnique ================= */

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(int[] sorted) {
    if (sorted.length < 2)
      return true;
    int previous = sorted[0];
    for (int i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(byte[] sorted) {
    if (sorted.length < 2)
      return true;
    byte previous = sorted[0];
    for (byte i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(short[] sorted) {
    if (sorted.length < 2)
      return true;
    short previous = sorted[0];
    for (short i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(char[] sorted) {
    if (sorted.length < 2)
      return true;
    char previous = sorted[0];
    for (char i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(long[] sorted) {
    if (sorted.length < 2)
      return true;
    long previous = sorted[0];
    for (long i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(double[] sorted) {
    if (sorted.length < 2)
      return true;
    double previous = sorted[0];
    for (double i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   */
  public static boolean isUnique(float[] sorted) {
    if (sorted.length < 2)
      return true;
    float previous = sorted[0];
    for (float i : sorted)
      if (i == previous)
        return false;
    return true;
  }

  /**
   * Test if the input contains duplicates. This may give an incorrect answer if the input is not sorted.
   * @param sorted a sorted array
   * @return true if the input contains no duplicate element
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E> boolean isUnique(E[] sorted) {
    if (sorted.length < 2)
      return true;
    E previous = sorted[0];
    if (previous == null)
      nullFailure();
    for (E el : sorted)
      if (el.equals(previous))
        return false;
    return true;
  }

}
