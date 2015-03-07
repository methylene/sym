package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * An instance of this class represents a permutation of at most {@link Integer#MAX_VALUE} values.
 */
public final class Permutation implements Comparable<Permutation> {

  /* never modified */
  private final int[] posmap;

  Permutation(int[] posmap, boolean validate) {
    if (validate)
      Util.validate(posmap);
    this.posmap = posmap;
  }

  /**
   * @param posmap A list of numbers that specifies the permutation in zero-based
   *               <a href="http://en.wikipedia.org/wiki/Permutation#Definition_and_usage">one-line notation</a>.
   *               For example, {@code posmap = new int[]{1, 2, 0}} creates the permutation
   *               that sends {@code new char[]{'a', 'b', 'c'}} to {@code new char[]{'c', 'a', 'b'}}.
   */
  public Permutation(int[] posmap) {
    this(posmap, true);
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
    for (int index : cycle) {
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

  static public Permutation cycle1(int... cycle1based) {
    return cycle(Util.add(cycle1based, -1));
  }

  /**
   * @param i a positive integer
   * @param j a positive integer
   * @return The permutation of length {@code Math.max(i, j)} that swaps the elements at indexes {@code i}
   * and {@code j} (0-based). This permutation is called the <i>transposition</i> of {@code i} and {@code j}.
   */
  static public Permutation swap(int i, int j) {
    return cycle(i, j);
  }

  /**
   * @deprecated use {@link com.github.methylene.sym.PermutationFactory#from} instead
   * @param a          An array of distinct objects. {@code null} is not allowed.
   * @param b          An array of distinct objects that contains, for each element {@code a[i]},
   *                   exactly one element {@code b[j]} such that {@code comparator.compare(a[i], b[j]) == 0}.
   * @param comparator A comparator that compares objects in {@code a} and {@code b} so that they are distinct.
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(Object[] a, Object[] b, Comparator comparator) {
    return PermutationFactory.from(a, b, comparator);
  }

  /**
   * @deprecated use {@link com.github.methylene.sym.PermutationFactory#from} instead
   * @param a An array of distinct objects. {@code null} is not allowed.
   * @param b An array that contains, for each element {@code a[i]}, exactly one element {@code b[j]} such that
   *          {@code a[i].compareTo(b[j]) == 0}.
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(Comparable[] a, Comparable[] b) {
    return PermutationFactory.from(a, b);
  }

  /**
   * @deprecated use {@link com.github.methylene.sym.PermutationFactory#from} instead
   * @param a An array of distinct numbers.
   * @param b An array that contains, for each number {@code a[i]}, exactly one number {@code b[j]} such that
   *          {@code a[i] == b[j]}
   * @return A permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true.
   */
  static public Permutation from(int[] a, int[] b) {
    return PermutationFactory.from(a, b);
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
   * @param permutations An array of permutations, all of which have the same length.
   * @return The product (composition) of {@code permutations}, or a permutation of length 0 if {@code permutations}
   * is empty.
   * @throws java.lang.IllegalArgumentException If not all permutations have the same length.
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
   * @param permutations A list of permutations, all of which must have the same length.
   * @return The product (composition) of {@code permutations}, or a permutation of length 0 if {@code permutations}
   * is empty.
   * @throws java.lang.IllegalArgumentException If not all permutations have the same length.
   */
  public static Permutation prod(Iterable<Permutation> permutations) {
    Permutation result = null;
    for (Permutation permutation : permutations) {
      result = result == null ? permutation : result.comp(permutation);
    }
    return result == null ? identity(0) : result;
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
   * @param i A non-negative integer
   * @param j A non-negative integer
   * @return A permutation of length {@code Math.max(i, j)} that moves the element at index {@code i} to index {@code j}
   * and shifts the elements at indexes between {@code i} and {@code j} as necessary. Examples:
   * <pre><code>
   *   Permutation.insert(0, 2).pad(5).apply("12345");
   *   => 23145
   *   </code></pre>
   * <pre><code>
   *   Permutation.insert(3, 1).pad(5).apply("12345");
   *   => 14235
   * </code></pre>
   */
  public static Permutation insert(int i, int j) {
    if (i == j) {
      return identity(i);
    } else {
      int shift = i < j ? -1 : 1;
      int[] c = new int[Math.abs(i - j) + 1];
      c[0] = i;
      for (int k = 1; k < c.length; k += 1) {
        c[k] = j + shift * (k - 1);
      }
      return cycle(c);
    }
  }

  /**
   * @param pos A non negative number which is smaller than {@code this.length()}
   * @return The minimum number of times that {@code apply} must be written in an expression of the form
   * {@code apply(...(apply(pos))...)} so that it returns {@code pos}.
   * @throws java.lang.IllegalArgumentException If {@code pos < 0} or {@code pos >= this.length}.
   */
  public int orbitLength(int pos) {
    if (pos < 0 || pos >= posmap.length)
      throw new IllegalArgumentException("wrong pos: " + pos);
    int length = 1;
    int j = pos;
    while ((j = apply(j)) != pos) {
      length += 1;
    }
    return length;
  }

  private int[] orbit(int pos, int orbitLength) {
    if (pos < 0 || pos >= posmap.length)
      throw new IllegalArgumentException("wrong pos: " + pos);
    int[] result = new int[orbitLength];
    result[0] = pos;
    int j = pos;
    for (int k = 1; k < result.length; k += 1) {
      j = apply(j);
      result[k] = j;
    }
    return result;
  }

  /**
   * @param pos A non negative number which is smaller than {@code this.length()}
   * @return The minimum number of times that {@code apply} must be written in an expression of the form
   * {@code apply(...(apply(pos))...)} so that it returns {@code pos}.
   * @throws java.lang.IllegalArgumentException If {@code pos < 0} or {@code pos >= this.length}.
   */
  public int[] orbit(int pos) {
    return orbit(pos, orbitLength(pos));
  }

  /**
   * @return The minimum number of times that {@code comp} must be written in an expression of the form
   * {@code this.comp(this)...comp(this)} so that it returns the identity.
   * @throws java.lang.IllegalArgumentException If {@code pos < 0} or {@code pos >= this.length}.
   * @see Permutation#isIdentity
   */
  public int order() {
    int i = 1;
    Permutation p = this;
    while (!p.isIdentity()) {
      i += 1;
      p = p.comp(this);
    }
    return i;
  }

  /**
   * @return true if there are numbers {@code n_0 ... n_k} so that {@code this.equals(cycle(n_0, ..., n_k))}.
   */
  public boolean isCycle() {
    int[] candidate = null;
    for (int i = 0; i < posmap.length; i += 1) {
      int orbitLength = orbitLength(i);
      if (orbitLength > 1) {
        if (candidate == null) {
          candidate = orbit(i, orbitLength);
        } else {
          if (orbitLength != candidate.length) {
            return false;
          } else {
            if (Util.disjoint(posmap.length, candidate, orbit(i, orbitLength))) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * @return A list of Permutations so that {@code p.isCycle()} is true for each element {@code p} in that list
   * and {@code this.equals(prod(this.toCycles()))}. An identity permutation will not be contained in the return value.
   * @see Permutation#isIdentity
   */
  public List<Permutation> toCycles() {
    LinkedList<int[]> orbits = new LinkedList<int[]>();
    boolean[] done = new boolean[posmap.length];
    for (int i = 0; i < posmap.length; i += 1) {
      if (!done[i]) {
        int orbitLength = orbitLength(i);
        if (orbitLength > 1) {
          int[] candidate = orbit(i, orbitLength);
          for (int k : candidate)
            done[k] = true;
          boolean newOrbit = true;
          for (int[] orbit : orbits) {
            if (!Util.disjoint(posmap.length, orbit, candidate)) {
              newOrbit = false;
              break;
            }
          }
          if (newOrbit)
            orbits.push(candidate);
        }
      }
    }
    ArrayList<Permutation> result = new ArrayList<Permutation>(orbits.size());
    for (int[] orbit : orbits) {
      result.add(cycle(orbit).pad(posmap.length));
    }
    return result;
  }

  /**
   * @return A list of permutations, each of which is if the form {@code swap(i, j)} for some {@code i != j},
   * so that {@code this.equals(prod(this.toTranspositions()))}.
   * @see Permutation#prod
   * @see Permutation#swap
   */
  public List<Permutation> toTranspositions() {
    List<Permutation> result = new ArrayList<Permutation>();
    for (Permutation cycle : toCycles()) {
      int[] orbit = null;
      for (int i = 0; i < cycle.posmap.length; i += 1) {
        int orbitLength = cycle.orbitLength(i);
        if (orbitLength > 1)
          orbit = cycle.orbit(cycle.posmap[i], orbitLength);
      }
      assert orbit != null;
      for (int i = 0; i < orbit.length - 1; i += 1)
        result.add(swap(orbit[i], orbit[i + 1]).pad(posmap.length));
    }
    return result;
  }

  /**
   * @return {@code 1} if this permutation can be written as an even number of transpositions, {@code -1} otherwise.
   * @see Permutation#swap
   */
  public int signum() {
    return toTranspositions().size() % 2 == 0 ? 1 : -1;
  }

  /**
   * @param length A non negative number
   * @return A permutation of length {@code length} that reverses its input. Example:
   * <pre><code>
   *   Permutation.reverse(5).apply("12345");
   *   => 54321
   * </code></pre>
   */
  public static Permutation reverse(int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = length - i - 1;
    }
    return new Permutation(result);
  }

  /**
   * @return true if this permutation reverses or "flips" its input.
   * @see Permutation#reverse
   */
  public boolean isReverse() {
    for (int i = 0; i < posmap.length; i += 1)
      if (posmap[i] != posmap.length - i - 1)
        return false;
    return true;
  }

  /**
   * @return true if {@code this.apply(pos) == pos} for all integers {@code pos >= 0, pos < this.length()}.
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

  /**
   * @return a String representation of this permutation. This representation may change in the future.
   */
  @Override public String toString() {
    return Arrays.toString(posmap);
  }

  /**
   * @param other another object
   * @return true if the other object is a Permutation and has the exact same posmap.
   * In particular this returns false if {@code this.length() != other.length()}.
   */
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

  /**
   * @param other A permutation, not necessarily of the same length
   * @return The result of lexicographic comparison of {@code this.posmap} and {@code other.posmap}.
   * This is compatible with equals
   */
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
   * @throws java.lang.IllegalArgumentException If {@code pos < 0} or {@code pos >= this.length}.
   */
  public int apply(int pos) {
    if (pos < 0 || pos >= posmap.length)
      throw new IllegalArgumentException("wrong pos: " + pos);
    return posmap[pos];
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public Object[] apply(Object[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    Object[] result = new Object[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public Comparable[] apply(Comparable[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    Comparable[] result = new Comparable[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public String[] apply(String[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    String[] result = new String[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public byte[] apply(byte[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    byte[] result = new byte[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public short[] apply(short[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    short[] result = new short[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public int[] apply(int[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    int[] result = new int[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public long[] apply(long[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    long[] result = new long[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public float[] apply(float[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    float[] result = new float[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public double[] apply(double[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    double[] result = new double[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public boolean[] apply(boolean[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input An array of length {@code this.length()}.
   * @return The result of applying this permutation to {@code input}.
   * @throws java.lang.IllegalArgumentException If {@code input.length != this.length()}.
   */
  public char[] apply(char[] input) {
    if (input.length != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length);
    char[] result = new char[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    return result;
  }

  /**
   * @param input A String of length {@code this.length()}
   * @return The result of applying this permutation to the characters of {@code input}.
   * @throws java.lang.IllegalArgumentException if {@code input} has the wrong length
   */
  public String apply(String input) {
    if (input.length() != posmap.length)
      throw new IllegalArgumentException("wrong length: " + input.length());
    char[] dst = new char[input.length()];
    input.getChars(0, input.length(), dst, 0);
    return new String(apply(dst));
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
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct an array of distinct bytes
   * @return the permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException if {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(byte[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct An array of distinct integers.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(int[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct An array of distinct longs
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(long[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct An array of distinct floats.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(float[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct An array of distinct doubles
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(double[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct An array of distinct characters.
   * @return The permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(char[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct an array of distinct comparables. Null is not allowed.
   * @return the permutation that sorts {@code distinct}
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(Comparable[] distinct) {
    return PermutationFactory.sort(distinct, true);
  }

  /**
   * @deprecated use {@link PermutationFactory#sort} instead
   * @param distinct   an array of objects that are distinct according to the {@code comparator}. Null is not allowed.
   * @param comparator a comparator which satisfies {@code comparator.compare(distinct[i], distinct[j]) != 0} for all
   *                   non-negative numbers i, j such that {@code i != j}, {@code i < distinct.length} and {@code j < distinct.length}.
   * @return the permutation that sorts {@code distinct}.
   * If {@code sorted} is the result of sorting {@code distinct}, the following holds for all non-negative integers
   * {@code i < distinct.length}:
   * <pre><code>
   *   distinct[i] = sorted[Permutation.sort(distinct).apply(i)]
   * </code></pre>
   * @throws java.lang.IllegalArgumentException If {@code distinct} contains duplicate values.
   */
  static public Permutation sort(Object[] distinct, Comparator comparator) {
    return PermutationFactory.sort(distinct, comparator, true);
  }

  /**
   * @return A copy of the index map that represents this permutation.
   */
  public int[] getPosmap() {
    return Arrays.copyOf(posmap, posmap.length);
  }

  /**
   * QA method for internal use
   * @return this
   */
  Permutation validate() {
    Util.validate(posmap);
    return this;
  }

}
