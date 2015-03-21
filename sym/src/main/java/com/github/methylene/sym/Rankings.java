package com.github.methylene.sym;

import static com.github.methylene.sym.Util.checkEqualLength;
import static com.github.methylene.sym.Util.distinctInts;
import static com.github.methylene.sym.Util.exceptionalBinarySearch;
import static com.github.methylene.sym.Util.lengthFailure;
import static com.github.methylene.sym.Util.padding;
import static com.github.methylene.sym.Util.slotFailure;
import static com.github.methylene.sym.Util.sortedCopy;
import static com.github.methylene.sym.Util.withIndex;
import static java.lang.Math.max;
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


  /**
   * Check that the input is a <i>ranking</i>. Each non-negative integer less than
   * {@code a.length} must appear exactly once.
   * @param a an array
   * @return true if a is a ranking
   */
  public static boolean isRanking(int[] a) {
    boolean[] used = new boolean[a.length];
    for (int i : a) {
      if (i < 0 || i >= a.length)
        return false;
      if (used[i])
        return false;
      used[i] = true;
    }
    return true;
  }

  /**
   * Ensure that the input is a ranking.
   * @param a an array
   * @return the input array
   * @throws java.lang.IllegalArgumentException if {@code a} is not a ranking
   * @see #isRanking
   */
  public static int[] checkRanking(int[] a) {
    if (!isRanking(a))
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

  public static int[] comp(int[] ranking, int[] other) {
    int length = max(ranking.length, other.length);
    int[] lhs = padding(ranking, length);
    int[] rhs = padding(other, length);
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = lhs[rhs[i]];
    return result;
  }

  /* ================= nextOffset ================= */

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
   * Produce a ranking that sorts the input when applied to it. For each index {@code i < a.length}, the return value
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
   * @see com.github.methylene.sym.Rankings#apply(int[], int[])
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


  public static <E> int[] sort(E[] a, Comparator<E> comp) {
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


  public static Object[] apply(int[] ranking, Object[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    Object[] result = new Object[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static Comparable[] apply(int[] ranking, Comparable[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    Comparable[] result = new Comparable[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static String[] apply(int[] ranking, String[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    String[] result = new String[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static byte[] apply(int[] ranking, byte[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    byte[] result = new byte[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static short[] apply(int[] ranking, short[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    short[] result = new short[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }

  public static int[] apply(int[] ranking, int[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    int[] result = new int[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static long[] apply(int[] ranking, long[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    long[] result = new long[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static float[] apply(int[] ranking, float[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    float[] result = new float[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static double[] apply(int[] ranking, double[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    double[] result = new double[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static boolean[] apply(int[] ranking, boolean[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static char[] apply(int[] ranking, char[] input) {
    if (input.length < ranking.length)
      lengthFailure();
    char[] result = new char[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }

}
