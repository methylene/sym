package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;

import java.util.Arrays;
import java.util.Comparator;

/**
 * An instance of this class represents a permutation of at most {@link Integer#MAX_VALUE} values.
 */
public final class Permutation implements Comparable<Permutation> {

  private final int[] posmap;

  /**
   * @param posmap A list of numbers that specifies the permutation in zero-based
   *               <a href="http://en.wikipedia.org/wiki/Permutation#Definition_and_usage">one-line notation</a>.
   *               For example, {@code posmap = new int[]{1, 2, 0}} creates the permutation
   *               that sends {@code new char[]{'a', 'b', 'c'}} to {@code new char[]{'c', 'a', 'b'}}.
   */
  public Permutation(int[] posmap) {
    Util.validate(posmap);
    this.posmap = posmap;
  }

  /**
   * @param posmap1based Permutation definition in 1-based one-line notation.
   * @return The permutation defined by {@code posmap1based}.
   */
  static public Permutation perm1(int... posmap1based) {
    return new Permutation(Util.add(posmap1based, -1));
  }

  /**
   * @param cycle A list of numbers that defines a permutation in 0-based
   *              <a href="http://en.wikipedia.org/wiki/Permutation#Cycle_notation">cycle notation</a>
   * @return The permutation defined by {@code cycle}.
   */
  static public Permutation cycle(int... cycle) {
    int maxIndex = 0;
    for (int index: cycle) {
      if (index < 0)
        throw new IllegalArgumentException("negative index: " + index);
      maxIndex = Math.max(maxIndex, index);
    }
    int length = maxIndex + 1;
    boolean[] cycleIndexes = new boolean[length];
    for (int i : cycle) {
      if (cycleIndexes[i])
        throw new IllegalArgumentException("duplicate index: " + i);
      cycleIndexes[i] = true;
    }
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = !cycleIndexes[i] ? i : cycle[(Util.indexOf(cycle, i) + 1) % cycle.length];
    return new Permutation(result);
  }

  static public Permutation cycle1(int... cycle0based) {
    return cycle(Util.add(cycle0based, -1));
  }

  /**
   * @param i a positive integer
   * @param j a positive integer
   * @return The permutation of length {@code Math.max(i, j)} that swaps the elements at indexes {@code i}
   * and {@code j} (0-based).
   */
  static public Permutation swap(int i, int j) {
    return cycle(i, j);
  }

  /**
   * @param a An array of distinct objects. {@code null} is allowed to appear once.
   * @param b An array that contains, for each element {@code a[i]}, exactly one element {@code b[j]} such that
   *          {@code Objects.equals(a[i], b[j])} is true.
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(Object[] a, Object[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = Util.indexOf(b, a[i]);
    return new Permutation(result);
  }

  /**
   * @param a An array of distinct objects. {@code null} is not allowed.
   * @param b An array of distinct objects that contains, for each element {@code a[i]},
   *          exactly one element {@code b[j]} such that {@code comparator.compare(a[i], b[j]) == 0}.
   * @param comparator A comparator that compares objects in {@code a} and {@code b} so that they are distinct.
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(Object[] a, Object[] b, Comparator comparator) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = Permutation.sort(b, comparator);
    Object[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int k = Arrays.binarySearch(sortedB, a[i], comparator);
      result[i] = unsortB.apply(k);
    }
    return new Permutation(result);
  }

  /**
   * @param a An array of distinct objects. {@code null} is not allowed.
   * @param b An array that contains, for each element {@code a[i]}, exactly one element {@code b[j]} such that
   *          {@code a[i].compareTo(b[j]) == 0}.
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(Comparable[] a, Comparable[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = Permutation.sort(b);
    Comparable[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int k = Arrays.binarySearch(sortedB, a[i]);
      result[i] = unsortB.apply(k);
    }
    return new Permutation(result);
  }

  /**
   * @param a An array of distinct numbers.
   * @param b An array that contains, for each number {@code a[i]}, exactly one number {@code b[j]} such that
   *          {@code a[i] == b[j]}
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(int[] a, int[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = Permutation.sort(b);
    int[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i += 1) {
      int k = Arrays.binarySearch(sortedB, a[i]);
      result[i] = unsortB.apply(k);
    }
    return new Permutation(result);
  }

  /**
   * @param length The length of arrays that the result can be applied to.
   * @return A random permutation that can be applied to an array of length {@code length}.
   */
  static public Permutation random(int length) {
    return sort(distinctInts(length, 4));
  }

  /**
   * @param length The length of arrays that the resulting permutation can be applied to.
   * @return The identity permutation that can be applied to an array of length {@code length}.
   * @see Permutation#isIdentity
   */
  static public Permutation identity(int length) {
    int[] posmap = new int[length];
    for (int i = 0; i < length; i += 1)
      posmap[i] = i;
    return new Permutation(posmap);
  }

  /**
   * @param other A permutation of the same length as {@code this}.
   * @return The composition of {@code this} and {@code other}. If
   * {@code i} is an int such that {@code 0 < i < this.length()},
   * the following identity holds:
   * {@code this.apply(other.apply(i)) == this.comp(other).apply(i)}
   * @throws java.lang.IllegalArgumentException if {@code this.length() != other.length()}
   */
  public Permutation comp(Permutation other) {
    if (this.length() != other.length())
      throw new IllegalArgumentException("can only compose permutations of the same length");
    int[] lhs = posmap;
    int[] rhs = other.posmap;
    int[] result = new int[lhs.length];
    for (int i = 0; i < lhs.length; i += 1)
      result[i] = lhs[rhs[i]];
    return new Permutation(result);
  }

  /**
   * @param targetLength A number that is not smaller than {@code this.length}.
   * @return A permutation such that, for a non-negative number {@code m < targetLength}
   * <pre><code>
   *   this.pad(n).apply(m) = this.apply(m) // if m is smaller than this.length
   *   this.pad(n).apply(m) // otherwise
   * </code></pre>
   */
  public Permutation pad(int targetLength) {
    if (targetLength < length())
      throw new IllegalArgumentException("targetLength can not be shorter than current length");
    if (targetLength == length())
      return this;
    return new Permutation(Util.pad(posmap, targetLength));
  }

  /**
   * @param permutations A list of permutations, all of which have the same length.
   * @return The product (composition) of {@code permutations}.
   */
  public static Permutation prod(Permutation... permutations) {
    if (permutations.length == 0)
      return identity(0);
    Permutation result = permutations[0];
    for (int i = 1; i < permutations.length; i += 1)
      result = result.comp(permutations[i]);
    return result;
  }

  /**
   * @param n any integer
   * @return If {@code n} is positive, the product {@code this.comp(this)[...]comp(this)} ({@code n} times).
   * If {@code n} is negative, the product {@code this.comp(this.invert)[...]comp(this.invert)} ({@code -n} times).
   * Otherwise, the identity permutation of length {@code this.length}.
   */
  public Permutation pow(int n) {
    if (n == 0)
      return identity(length());
    Permutation seed = n < 0 ? invert() : this;
    Permutation result = seed;
    for (int i = 1; i < Math.abs(n); i += 1)
      result = result.comp(seed);
    return result;
  }

  /**
   * @return A permutation that satisfies {@code this.invert().apply(this.apply(i)) == i} for any int {@code i}
   * such that {@code 0 < i < this.length()}.
   */
  public Permutation invert() {
    int[][] posmapWithIndex = Util.withIndex(posmap);
    Arrays.sort(posmapWithIndex, Util.COMPARE_2ND);
    int[] result = new int[posmap.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[i] = posmapWithIndex[i][0];
    return new Permutation(result);
  }

  /**
   * @return true if {@code Arrays.equals(a, a)} is true for all arrays {@code a} of length {@code this.length}.
   */
  public boolean isIdentity() {
    for (int i = 0; i < posmap.length; i += 1)
      if (posmap[i] != i)
        return false;
    return true;
  }

  /**
   * @return The length that an array must have so that this permutation can be applied to it.
   */
  public int length() {
    return posmap.length;
  }

  @Override public String toString() {
    return Arrays.toString(Util.add(posmap, 1));
  }

  @Override public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;
    return Arrays.equals(posmap, ((Permutation) other).posmap);
  }

  @Override public int hashCode() {
    return Arrays.hashCode(posmap);
  }

  @Override public int compareTo(Permutation other) {
    if (this == other)
      return 0;
    for (int i = 0; i < Math.min(this.posmap.length, other.posmap.length); i += 1)
      if (this.posmap[i] != other.posmap[i])
        return this.posmap[i] - other.posmap[i];
    return other.posmap.length - this.posmap.length;
  }

  /* overloaded versions of apply */

  /**
   * @param pos A non negative number which is smaller than {@code this.length()}
   * @return The index that an element {@code a[pos]} an array {@code a} will have,
   * after this permutation has been applied to {@code a}.
   * @throws java.lang.ArrayIndexOutOfBoundsException If {@code pos < 0} or {@code pos >= this.length}.
   */
  public int apply(int pos) {
    return posmap[pos];
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public Object[] apply(Object[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    Object[] result = new Object[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public Comparable[] apply(Comparable[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    Comparable[] result = new Comparable[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public String[] apply(String[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    String[] result = new String[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public byte[] apply(byte[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    byte[] result = new byte[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public short[] apply(short[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    short[] result = new short[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public int[] apply(int[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    int[] result = new int[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public long[] apply(long[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    long[] result = new long[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public float[] apply(float[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    float[] result = new float[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public double[] apply(double[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    double[] result = new double[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public boolean[] apply(boolean[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public char[] apply(char[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    char[] result = new char[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * @return The result of applying this permutation to the incrementing array of distinct strings
   * of length {@code this.length()} that starts with {@code "a", "b", "c"...} and so on.
   * @see com.github.methylene.sym.Permutation#apply(String[])
   */
  public String[] apply() {
    return apply(Util.symbols(posmap.length));
  }

  /* overloaded versions of sort */

  /**
   * @param distinct An array of distinct bytes
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(byte[] distinct) {
    byte[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct shorts
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(short[] distinct) {
    short[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct integers.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(int[] distinct) {
    int[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct longs
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(long[] distinct) {
    long[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct floats.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(float[] distinct) {
    float[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct doubles
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(double[] distinct) {
    double[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct characters.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(char[] distinct) {
    char[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of distinct comparables. Null is not allowed.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(Comparable[] distinct) {
    Comparable[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  /**
   * @param distinct An array of objects that are distinct according to the {@code comparator}. Null is not allowed.
   * @param comparator A comparator which satisfies {@code comparator.compare(distinct[i], distinct[j]) != 0} for all
   * non-negative numbers i, j such that {@code i != j}, {@code i < distinct.length} and {@code j < distinct.length}.
   * @return The permutation that sorts {@code distinct}.
   * If {@code sorted} is the result of sorting {@code distinct}, the following holds for all non-negative integers
   * {@code i < distinct.length}:
   * <pre><code>
   *   distinct[i] = sorted[Permutation.sort(distinct).apply(i)]
   * </code></pre>
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   */
  static public Permutation sort(Object[] distinct, Comparator comparator) {
    Object[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted, comparator);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i], comparator);
    }
    return new Permutation(result);
  }

}
