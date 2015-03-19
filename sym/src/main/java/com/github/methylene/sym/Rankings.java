package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;
import static com.github.methylene.sym.Util.lengthFailure;
import static com.github.methylene.sym.Util.nullFailure;
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
   * @see com.github.methylene.sym.Rankings#isRanking
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

  /* ================= map ================= */

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(int el, int idx, int[] sorted, boolean[] used) {
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }


  @Override public int hashCode() {
    return super.hashCode();
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(byte el, int idx, byte[] sorted, boolean[] used) {
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(short el, int idx, short[] sorted, boolean[] used) {
    if (idx < 0)
      slotFailure();
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(long el, int idx, long[] sorted, boolean[] used) {
    if (idx < 0)
      slotFailure();
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(char el, int idx, char[] sorted, boolean[] used) {
    if (idx < 0)
      slotFailure();
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(float el, int idx, float[] sorted, boolean[] used) {
    if (idx < 0)
      slotFailure();
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(double el, int idx, double[] sorted, boolean[] used) {
    if (idx < 0)
      slotFailure();
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || sorted[next] != el)
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || sorted[next] != el)
          slotFailure();
        offset--;
      }
    return current;
  }

  /**
   * Find the next free position in {@code sorted} where the value is {@code el}.
   * The first position to be tried is {@code idx}. If that is used, {@code j = idx + 1} is tried.
   * If that is also used, {@code j = idx + 2} and so on until an unused position is found or {@code sorted[j] != a[i]}.
   * After that, {@code j = idx - 1} is tried, then {@code idx - 2} and so on. If no free position can be found,
   * an Exception is thrown.
   * @param el an element of sorted
   * @param idx a position to start looking at
   * @param sorted a sorted array
   * @param used an array that tells us which positions are already taken
   * @return an index {@code j} in {@code sorted} so that {@code a[i] = sorted[j]} and {@code !used[j]}
   * @throws java.lang.IllegalArgumentException if no such index can be found
   */
  public static int findSlot(Object el, int idx, Object[] sorted, boolean[] used) {
    if (idx < 0)
      slotFailure();
    if (el == null)
      nullFailure();
    int current;
    int offset = 0;
    while (used[current = idx + offset])
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length || !sorted[next].equals(el))
          if (idx > 0)
            offset = -1;
          else
            slotFailure();
        else
          offset++;
      } else {
        int next = current - 1;
        if (next < 0 || !sorted[next].equals(el))
          slotFailure();
        offset--;
      }
    return current;
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
   * </code></pre>
   * then for each index {@code i < a.length}, the following is true:
   * <pre><code>
   *   Util.indexOf(a, el) == unsort[Arrays.binarySearch(sorted, el)]
   * </code></pre>
   * @param a an array
   * @return a ranking that sorts the input
   * @see com.github.methylene.sym.Rankings#apply(int[], int[])
   * @see com.github.methylene.sym.Util#indexOf
   */
  public static int[] sort(int[] a) {
    int[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }

  public static int[] sort(byte[] a) {
    byte[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] sort(short[] a) {
    short[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] sort(long[] a) {
    long[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] sort(float[] a) {
    float[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }

  public static int[] sort(double[] a) {
    double[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] sort(char[] a) {
    char[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }

  public static <E extends Comparable> int[] sort(E[] a) {
    Comparable[] sorted = sortedCopy(a);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      final int slot = findSlot(a[i], binarySearch(sorted, a[i]), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }


  public static <E> int[] sort(E[] a, Comparator<E> comp) {
    Object[] sorted = sortedCopy(a, comp);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[a.length];
    for (int i = 0; i < a.length; i += 1) {
      @SuppressWarnings("unchecked")
      final int slot = findSlot(a[i], binarySearch(sorted, a[i], (Comparator) comp), sorted, used);
      ranking[i] = slot;
      used[slot] = true;
    }
    return ranking;
  }


  /* ================= from ================= */

  public static int[] from(int[] a, int[] b) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b);
    final int[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      final int idx = binarySearch(sorted, a[i]);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (a[i] != b[ranking[i]])
        slotFailure();
      used[slot] = true;
    }
    return ranking;
  }


  public static <E extends Comparable> int[] from(E[] a, E[] b) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b);
    final Comparable[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      final int idx = binarySearch(sorted, a[i]);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (!a[i].equals(b[ranking[i]]))
        slotFailure();
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] from(byte[] a, byte[] b) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b);
    final byte[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      final int idx = binarySearch(sorted, a[i]);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (a[i] != b[ranking[i]])
        slotFailure();
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] from(long[] a, long[] b) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b);
    final long[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      final int idx = binarySearch(sorted, a[i]);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (a[i] != b[ranking[i]])
        slotFailure();
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] from(float[] a, float[] b) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b);
    final float[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      final int idx = binarySearch(sorted, a[i]);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (a[i] != b[ranking[i]])
        slotFailure();
      used[slot] = true;
    }
    return ranking;
  }


  public static int[] from(double[] a, double[] b) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b);
    final double[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      final int idx = binarySearch(sorted, a[i]);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (a[i] != b[ranking[i]])
        slotFailure();
      used[slot] = true;
    }
    return ranking;
  }


  public static <E> int[] from(E[] a, E[] b, Comparator<E> comp) {
    if (a.length != b.length)
      lengthFailure();
    final int[] sort = sort(b, comp);
    final Object[] sorted = apply(sort, b);
    final int[] unsort = invert(sort);
    final int[] ranking = new int[a.length];
    final boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      @SuppressWarnings("unchecked")
      final int idx = binarySearch(sorted, a[i], (Comparator) comp);
      if (idx < 0)
        slotFailure();
      final int slot = findSlot(a[i], idx, sorted, used);
      ranking[i] = unsort[slot];
      if (!a[i].equals(b[ranking[i]]))
        slotFailure();
      used[slot] = true;
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
