package com.github.methylene.sym;

import static com.github.methylene.sym.Util.*;
import static java.lang.System.arraycopy;
import static java.util.Arrays.binarySearch;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A collection of methods that return rankings, or operate on rankings.
 */
public final class Rankings {

  private Rankings() {}

  private static final Comparator<int[]> COMPARE_FIRST = new Comparator<int[]>() {
    public int compare(int[] a, int[] b) {
      return a[0] - b[0];
    }
  };

  private static final int[] IDENTITY_0 = new int[0];

  /**
   * Check that the input ranking is valid. In order to be valid, each non-negative integer less than
   * {@code a.length} must appear exactly once.
   * @param a an array
   * @return true if a is a ranking
   */
  public static boolean isValid(int[] a) {
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i++) {
      if (a[i] < 0 || a[i] >= a.length)
        return false;
      if (used[a[i]])
        return false;
      used[a[i]] = true;
    }
    return true;
  }

  /**
   * Return the identity ranking
   * @param length the length of the ranking
   * @return the identity ranking of the given length
   */
  public static int[] identity(int length) {
    if (length == 0)
      return IDENTITY_0;
    return Util.sequence(length);
  }

  /**
   * Find the length that this ranking can be safely trimmed to.
   * @param ranking a ranking
   * @return the length that this ranking can be safely trimmed to
   */
  public static int trimmedLength(int[] ranking) {
    for (int i = ranking.length - 1; i >= 0; i--)
      if (ranking[i] != i)
        return i + 1;
    return 0;
  }

  /**
   * If possible, trim ranking to something shorter but equivalent.
   * @param ranking a ranking
   * @return the trimmed ranking
   */
  public static int[] trim(int[] ranking) {
    int length = trimmedLength(ranking);
    if (length == 0)
      return INT_0;
    if (length < ranking.length)
      return Arrays.copyOf(ranking, length);
    return ranking;
  }

  /**
   * Return a ranking that acts similar to the input but operates on higher indexes,
   * leaving the lower {@code n} indexes unmoved.
   * @param n a non-negative number
   * @param ranking a ranking
   * @return the shifted ranking
   * @throws java.lang.IllegalArgumentException if {@code n} is negative
   */
  public static int[] shift(int n, int[] ranking)  {
    if (n < 0)
      negativeFailure();
    if (n == 0)
      return ranking;
    int[] shifted = new int[n + ranking.length];
    for (int i = 1; i < n; i++)
      shifted[i] = i;
    for (int i = 0; i < ranking.length; i++)
      shifted[i + n] = ranking[i] + n;
    return shifted;
  }

  /**
   * Ensure that the input is a ranking.
   * @param a an array
   * @return the input ranking
   * @throws java.lang.IllegalArgumentException if {@code a} is not a valid ranking
   * @see #isValid
   */
  public static int[] checkRanking(int[] a) {
    if (!isValid(a))
      throw new IllegalArgumentException("argument is not a ranking");
    return a;
  }

  public static int[] invert(int[] ranking) {
    int[][] rankingWithIndex = withIndex(ranking);
    Arrays.sort(rankingWithIndex, COMPARE_FIRST);
    int[] inverted = new int[ranking.length];
    for (int i = 0; i < ranking.length; i += 1)
      inverted[i] = rankingWithIndex[i][1];
    return inverted;
  }


  public static int[] random(int length) {
    return sort(distinctInts(length, 4));
  }

  /**
   * Multiply two rankings.
   * @param lhs a ranking
   * @param rhs another ranking
   * @return the product of the input rankings
   */
  public static int[] comp(int[] lhs, int[] rhs) {
    if (lhs.length >= rhs.length) {
      if (rhs.length == 0)
          return lhs;
      int[] result = new int[lhs.length];
      for (int i = 0; i < rhs.length; i++)
        result[i] = lhs[rhs[i]];
      if (lhs.length > rhs.length)
        arraycopy(lhs, rhs.length, result, rhs.length, lhs.length - rhs.length);
      return result;
    }
    if (lhs.length == 0)
      return rhs;
    int[] result = new int[rhs.length];
    for (int i = 0; i < rhs.length; i++) {
      int n = rhs[i];
      result[i] = n >= lhs.length ? n : lhs[n];
    }
    return result;
  }

  /* ================= nextOffset ================= */

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final int[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final byte[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final short[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final long[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final float[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final double[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final char[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || sorted[next] != sorted[idx])
        if (idx == 0 || sorted[idx - 1] != sorted[idx])
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || sorted[next] != sorted[idx])
        return 0;
    }
    return offset;
  }

  /**
   * Find the next position, after {@code idx + offset}, of {@code sorted[idx]} in a sorted array.
   * For a given element {@code el}, iterating this method over the {@code offset} element, starting with
   * {@code offset = 0}, will enumerate all positions of {@code sorted[idx]} in the sorted array.
   * @param idx the start index
   * @param offset the current offset from the start index
   * @param sorted a sorted array
   * @return the next offset or {@code 0} if there is no next offset
   */
  public static int nextOffset(final int idx, int offset, final Object[] sorted) {
    if (offset >= 0) {
      int next = idx + ++offset;
      if (next >= sorted.length || !sorted[next].equals(sorted[idx]))
        if (idx == 0 || !sorted[idx - 1].equals(sorted[idx]))
          return 0;
        else
          return -1;
    } else {
      int next = idx + --offset;
      if (next < 0 || !sorted[next].equals(sorted[idx]))
        return 0;
    }
    return offset;
  }

  /* ================= shift ================= */

  /**
   * Encode an int as a non-zero int
   * @param i an int
   * @return {@code i + 1} if {@code i} is non-negative, otherwise {@code i}
   * @see #unshift
   */
  public static int shift(int i) {
    return i >= 0 ? i + 1 : i;
  }

  /**
   * Undo the shift
   * @param i a non-zero int
   * @return {@code i - 1} if {@code i} is positive, otherwise {@code i}
   * @see #shift
   */
  public static int unshift(int i) {
    return i > 0 ? i - 1 : i;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final int[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final byte[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final short[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final long[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final char[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final float[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final double[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  static int nextOffsetShifting(final int idx, int shiftedOffset, final Object[] sorted) {
    if (shiftedOffset == 0)
      return 0;
    int offset = nextOffset(idx, unshift(shiftedOffset), sorted);
    if (offset == 0)
      slotFailure();
    return offset;
  }

  /* ================= sort ================= */

  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], int[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(int[] a) {
    int[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }

  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], byte[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(byte[] a) {
    byte[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], short[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(short[] a) {
    short[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], long[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(long[] a) {
    long[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], float[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(float[] a) {
    float[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }

  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], double[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(double[] a) {
    double[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], char[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(char[] a) {
    char[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }

  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see #apply(int[], Comparable[])
   * @see com.github.methylene.sym.Util#indexOf
   * @throws java.lang.NullPointerException if {@code a} is {@code null} or contains a {@code null} element
   */
  public static <E extends Comparable> int[] sort(E[] a) {
    Comparable[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      int idx = binarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that sorts the input when applied to it.
   * For each index {@code i < a.length}, the return value
   * satisfies the following property.
   * Let
   * <pre><code>
   *   int[] sort = sort(a);
   *   int[] sorted = apply(sort, a);
   *   int[] unsort = invert(sort);
   *   int idx = Arrays.binarySearch(sorted, el);
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el, 0) == unsort[idx]
   * </code></pre>
   * @param a an array
   * @param comp a comparator
   * @return a ranking that sorts the input
   * @see #apply(int[], Object[])
   * @see com.github.methylene.sym.Util#indexOf
   * @throws java.lang.NullPointerException if {@code a} is {@code null} or contains a {@code null} element
   */
  public static <E> int[] sort(Object[] a, Comparator<E> comp) {
    Object[] sorted = sortedCopy(a, comp);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i++) {
      @SuppressWarnings("unchecked")
      int idx = binarySearch(sorted, a[i], (Comparator) comp);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = idx + offset;
      offsets[idx] = shift(offset);
    }
    return ranking;
  }



  /* ================= from ================= */

  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @throws java.lang.NullPointerException if any argument is {@code null}
   * @see #apply(int[], int[])
   */
  public static int[] from(int[] a, int[] b) {
    checkEqualLength(a, b);
    int[] sort = sort(b);
    int[] unsort = invert(sort);
    int[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (a[i] != b[ranking[i]])
        slotFailure();
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @throws java.lang.NullPointerException if any argument is {@code null}
   * @see #apply(int[], java.lang.Comparable[])
   */
  public static <E extends Comparable> int[] from(E[] a, E[] b) {
    checkEqualLength(a, b);
    int[] sort = sort(b);
    int[] unsort = invert(sort);
    Comparable[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (!a[i].equals(b[ranking[i]]))
        slotFailure();
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.NullPointerException if any argument is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @see #apply(int[], byte[])
   */
  public static int[] from(byte[] a, byte[] b) {
    checkEqualLength(a, b);
    int[] sort = sort(b);
    int[] unsort = invert(sort);
    byte[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (a[i] != b[ranking[i]])
        slotFailure();
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.NullPointerException if any argument is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @see #apply(int[], long[])
   */
  public static int[] from(long[] a, long[] b) {
    checkEqualLength(a, b);
    int[] sort = sort(b);
    int[] unsort = invert(sort);
    long[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (a[i] != b[ranking[i]])
        slotFailure();
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.NullPointerException if any argument is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @see #apply(int[], float[])
   */
  public static int[] from(float[] a, float[] b) {
    checkEqualLength(a, b);
    int[] sort = sort(b);
    int[] unsort = invert(sort);
    float[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (a[i] != b[ranking[i]])
        slotFailure();
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.NullPointerException if any argument is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @see #apply(int[], double[])
   */
  public static int[] from(double[] a, double[] b) {
    checkEqualLength(a, b);
    int[] sort = sort(b);
    int[] unsort = invert(sort);
    double[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i]);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (a[i] != b[ranking[i]])
        slotFailure();
    }
    return ranking;
  }


  /**
   * Produce a particular ranking that produces {@code b} when applied to {@code a}.
   * @param a an array
   * @param b an array
   * @return a ranking that produces {@code b} when applied to {@code a}
   * @throws java.lang.NullPointerException if any argument is null, or if {@code a} or {@code b}
   * contain a {@code null} element
   * @throws java.lang.IllegalArgumentException if {@code b} can not be obtained by rearranging {@code a}
   * @see #apply(int[], java.lang.Object[])
   */
  public static <E> int[] from(E[] a, E[] b, Comparator<E> comp) {
    checkEqualLength(a, b);
    int[] sort = sort(b, comp);
    int[] unsort = invert(sort);
    Object[] sorted = apply(sort, b);
    int[] ranking = new int[a.length];
    int[] offsets = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = exceptionalBinarySearch(sorted, a[i], comp);
      int offset = nextOffsetShifting(idx, offsets[idx], sorted);
      ranking[i] = unsort[idx + offset];
      offsets[idx] = shift(offset);
      if (!a[i].equals(b[ranking[i]]))
        slotFailure();
    }
    return ranking;
  }



  /* ================= apply ================= */

  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not check that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   */
  public static Object[] apply(int[] ranking, Object[] input) {
    checkLength(ranking.length, input.length);
    Object[] result = new Object[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static Comparable[] apply(int[] ranking, Comparable[] input) {
    checkLength(ranking.length, input.length);
    Comparable[] result = new Comparable[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static String[] apply(int[] ranking, String[] input) {
    checkLength(ranking.length, input.length);
    String[] result = new String[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static byte[] apply(int[] ranking, byte[] input) {
    checkLength(ranking.length, input.length);
    byte[] result = new byte[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static short[] apply(int[] ranking, short[] input) {
    checkLength(ranking.length, input.length);
    short[] result = new short[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }

  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static int[] apply(int[] ranking, int[] input) {
    checkLength(ranking.length, input.length);
    int[] result = new int[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static long[] apply(int[] ranking, long[] input) {
    checkLength(ranking.length, input.length);
    long[] result = new long[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static float[] apply(int[] ranking, float[] input) {
    checkLength(ranking.length, input.length);
    float[] result = new float[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static double[] apply(int[] ranking, double[] input) {
    checkLength(ranking.length, input.length);
    double[] result = new double[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static boolean[] apply(int[] ranking, boolean[] input) {
    checkLength(ranking.length, input.length);
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  /**
   * Apply the ranking to the input array. An element at {@code i} is moved to {@code ranking[i]}.
   * Indexes that are greater or equal to the length of the ranking are not moved.
   * This method does not validate that the first argument is indeed a ranking.
   * @param ranking a ranking
   * @param input an input array
   * @return the result of applying the ranking to the input
   * @throws java.lang.IllegalArgumentException if the length of {@code input} is less than the length of {@code ranking}
   * @throws java.lang.ArrayIndexOutOfBoundsException can be thrown if the {@code ranking} argument is not a ranking
   */
  public static char[] apply(int[] ranking, char[] input) {
    checkLength(ranking.length, input.length);
    char[] result = new char[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }

}
