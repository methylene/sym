package com.github.methylene.sym;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import static com.github.methylene.sym.ArrayUtil.checkLength;
import static com.github.methylene.sym.ArrayUtil.negativeFailure;

/**
 * A permutation operation that can be used to rearrange arrays and lists.
 * <p/>
 * Instances of this class are immutable, and none of the apply methods modify the input.
 * The toCycles method can be used to obtain the destructive version of an instance.
 *
 * @see #toCycles
 */
public final class Permutation implements Comparable<Permutation>, Serializable {

  private static final long serialVersionUID = 1L;

  /*
   *  An array of N integers where each of the integers between 0 and N-1 appear exactly once.
   *  This array is never modified, and no code outside of this class can have a reference to it.
   *  Because of this, Permutation instances are effectively immutable.
   */
  private final int[] ranking;

  private static final Permutation IDENTITY = new Permutation(new int[0], false);

  private Permutation(int[] ranking, boolean validate) {
    ranking = Rankings.trim(ranking);
    this.ranking = validate ? Rankings.checkRanking(ranking) : ranking;
  }

  public static Permutation define() {
    return IDENTITY;
  }

  public static Permutation define(int a0) {
    if (a0 == 0) {
      return IDENTITY;
    } else {
      throw new IllegalArgumentException("not a ranking");
    }
  }

  public static Permutation define(int a0, int a1) {
    return define(new int[]{a0, a1}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2) {
    return define(new int[]{a0, a1, a2}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3) {
    return define(new int[]{a0, a1, a2, a3}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4) {
    return define(new int[]{a0, a1, a2, a3, a4}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5) {
    return define(new int[]{a0, a1, a2, a3, a4, a5}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5, int a6) {
    return define(new int[]{a0, a1, a2, a3, a4, a5, a6}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7) {
    return define(new int[]{a0, a1, a2, a3, a4, a5, a6, a7}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8) {
    return define(new int[]{a0, a1, a2, a3, a4, a5, a6, a7, a8}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9) {
    return define(new int[]{a0, a1, a2, a3, a4, a5, a6, a7, a8, a9}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10) {
    return define(new int[]{a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10}, true, false);
  }

  public static Permutation define(int a0, int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10, int... a11) {
    int[] a00 = {a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
    int[] a = new int[a00.length + a11.length];
    System.arraycopy(a00, 0, a, 0, a00.length);
    System.arraycopy(a11, 0, a, a00.length, a11.length);
    return define(a, true, false);
  }

  public static Permutation define(int[] ranking) {
    return define(ranking, true);
  }

  private static Permutation define(int[] ranking, boolean dirty) {
    return define(ranking, dirty, dirty);
  }

  private static Permutation define(int[] ranking, boolean validate, boolean copy) {
    int[] trimmed = Rankings.trim(ranking);
    if (trimmed.length == 0)
      return IDENTITY;
    if (copy && ranking == trimmed) {
      trimmed = Arrays.copyOf(trimmed, trimmed.length);
    }
    return new Permutation(trimmed, validate);
  }

  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycle</a>.
   * A single number {@code n} creates the identity of length {@code n + 1}.
   * An emtpy input produces the permutation of length {@code 0}.
   *
   * @param cycle a list of distinct, non-negative numbers
   * @return the cyclic permutation defined by {@code cycle}
   * @throws java.lang.IllegalArgumentException if {@code cycle} contains negative numbers or duplicates
   */
  public static Permutation defineCycle(int... cycle) {
    return define(CycleUtil.cyclic(cycle), false);
  }


  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycle</a>.
   *
   * @param cycle1based a list of numbers that defines a permutation in 1-based cycle notation
   * @return the cyclic permutation defined by {@code cycle1based}
   * @see com.github.methylene.sym.Permutation#defineCycle
   */
  public static Permutation cycle1(int... cycle1based) {
    return defineCycle(ArrayUtil.add(cycle1based, -1));
  }

  /**
   * Creates a random permutation of given length.
   *
   * @param length the length of arrays that the result can be applied to
   * @return a random permutation that can be applied to an array of length {@code length}
   */
  public static Permutation random(int length) {
    return define(Rankings.random(length), false);
  }

  /**
   * Return the identity permutation. It is the only permutation that can be applied to arrays of any length.
   *
   * @return the identity permutation that can be applied to an array of length {@code length}
   * @see Permutation#isIdentity
   */
  public static Permutation identity() {
    return IDENTITY;
  }

  /**
   * <p>Permutation composition. The following is true for all non-negative numbers
   * {@code i}:</p>
   * <pre><code>
   *   this.apply(other.apply(i)) == this.compose(other).apply(i)
   * </code></pre>
   *
   * @param other a permutation
   * @return the product of this instance and {@code other}
   * @see #product
   */
  public Permutation compose(Permutation other) {
    if (this.isIdentity())
      return other;
    if (other.ranking.length == 0)
      return this;
    return define(Rankings.comp(this.ranking, other.ranking), false);
  }

  /**
   * Take the product of the given permutations.
   *
   * @param permutations an array of permutations
   * @return the product of the input
   * @see #compose
   */
  public static Permutation product(Permutation... permutations) {
    Permutation result = identity();
    for (Permutation permutation : permutations)
      result = result.compose(permutation);
    return result;
  }

  /**
   * Take the product of the given permutations. If the input is empty, a permutation of length {@code 0} is returned.
   *
   * @param permutations an iterable of permutations
   * @return the product of the input
   * @see #compose
   */
  public static Permutation product(Iterable<Permutation> permutations) {
    Permutation result = identity();
    for (Permutation permutation : permutations)
      result = result.compose(permutation);
    return result;
  }


  /**
   * Raise this permutation to the {@code n}th power.
   * If {@code n} is positive, the product
   * <pre><code>
   *   this.compose(this)[...]compose(this)
   * </code></pre>
   * ({@code n} times) is returned.
   * If {@code n} is negative, the product
   * <pre><code>
   *   this.invert().compose(this.invert()) [...] compose(this.invert());
   * </code></pre>
   * ({@code -n} times) is returned.
   * If {@code n} is zero, the identity permutation of length {@code this.length} is returned.
   *
   * @param n any integer
   * @return the {@code n}th power of this permutation
   */
  public Permutation pow(int n) {
    if (n == 0)
      return identity();
    if (this.ranking.length == 0)
      return this;
    Permutation seed = n < 0 ? invert() : this;
    Permutation result = seed;
    for (int i = 1; i < Math.abs(n); i += 1)
      result = result.compose(seed);
    return result;
  }

  /**
   * <p>Inverts this permutation. The following always returns true:</p>
   * <pre><code>
   *   this.compose(this.inverse).isIdentity();
   * </code></pre>
   *
   * @return the inverse of this permutation
   * @see #compose
   * @see #isIdentity
   */
  public Permutation invert() {
    if (this.ranking.length == 0)
      return this;
    return define(Rankings.invert(ranking), false);
  }

  /**
   * Creates a cycle that acts as a delete followed by an insert. Examples:
   * <pre><code>
   *   Permutation.move(0, 2).apply("12345");
   *   => 23145
   *   </code></pre>
   * <pre><code>
   *   Permutation.move(3, 1).apply("12345");
   *   => 14235
   * </code></pre>
   * If {@code delete == insert}, the identity of length {@code delete + 1} is returned.
   *
   * @param delete a non-negative integer
   * @param insert a non-negative integer
   * @return a permutation of length {@code Math.max(delete, insert) + 1}
   * @see #defineCycle
   */
  public static Permutation move(int delete, int insert) {
    return defineCycle(ArrayUtil.range(insert, delete, true));
  }

  /**
   * <p>Calculate the orbit of given index.
   * The orbit is an array of distinct integers that contains all indexes</p>
   * <pre><code>
   *   i, apply(i), apply(apply(i)), ...
   * </code></pre>
   * <p>The orbit always contains at least one element, that is the start index {@code i} itself.</p>
   *
   * @param i a non negative number which is less than {@code this.length()}
   * @return the orbit of {@code i}
   * @throws java.lang.IllegalArgumentException if {@code i < 0} or {@code i >= this.length}.
   */
  public int[] orbit(int i) {
    return CycleUtil.orbit(ranking, i);
  }

  /**
   * <p>Calculate the order of this permutation. The order is the smallest positive number {@code n}
   * such that</p>
   * <pre><code>
   *   this.pow(n).isIdentity();
   * </code></pre>
   *
   * @return the order of this permutation
   * @throws java.lang.IllegalArgumentException if {@code pos < 0} or {@code pos >= this.length}
   * @see #isIdentity
   * @see #pow
   */
  public int order() {
    int i = 1;
    Permutation p = this;
    while (p.ranking.length != 0) {
      i += 1;
      p = p.compose(this);
    }
    return i;
  }

  /**
   * <p>Determine whether this permutation has at most one than one nontrivial orbit.</p>
   *
   * @return true if this permutation is a cycle
   * @see #defineCycle
   * @see #orbit
   */
  public boolean isCycle() {
    return CycleUtil.isCyclicRanking(ranking);
  }

  /**
   * Get a cycle based version of this operation, which can be used to change arrays in place.
   *
   * @return a cycle based version of this operation
   */
  public Cycles toCycles() {
    if (this.ranking.length == 0)
      return Cycles.identity();
    return Cycles.create(new Orbits(CycleUtil.toOrbits(ranking)));
  }


  /**
   * <p>Returns a permutation that reverses its input. Example:</p>
   * <pre><code>
   *   Permutation.reverse(5).apply("12345");
   *   => 54321
   * </code></pre>
   *
   * @param length a non negative number
   * @return a permutation that reverses an array of length {@code length}
   */
  public static Permutation reverse(int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = length - i - 1;
    }
    return define(result, false);
  }

  /**
   * Check if this permutation reverses its input.
   *
   * @param n a nonnegative number
   * @return true if this permutation reverses or "flips" an input of length {@code n}
   * @throws java.lang.IllegalArgumentException if {@code n} is negative
   * @see #reverse
   */
  public boolean reverses(int n) {
    if (ranking.length < n)
      return false;
    if (n < 0)
      negativeFailure();
    for (int i = 0; i < n; i += 1)
      if (ranking[i] != ranking.length - i - 1)
        return false;
    return true;
  }

  /**
   * Returns a shifted permutation. The following is true for the shifted permutation:
   * <pre><code>
   *   p.shift(n).apply(j) = j, j < n
   *   p.shift(n).apply(n + i) = n + p.apply(i)
   * </code></pre>
   *
   * @param n a non negative number
   * @return the shifted permutation
   * @throws java.lang.IllegalArgumentException if n is negative
   */
  public Permutation shift(int n) {
    if (ranking.length == 0 && n == 0)
      return this;
    return define(Rankings.shift(n, ranking), false);
  }

  /**
   * Find a cycle in this permutation or return {@code null} if this is the identity.
   *
   * @return a cycle in this permutation or {@code null} if there are no cycles because this is the identity
   */
  public int[] findCycle() {
    if (ranking.length == 0)
      return null;
    for (int i = 0; i < ranking.length; i++)
      if (ranking[i] != i)
        return orbit(i);
    throw new IllegalStateException(); // we'll never get here
  }

  /**
   * <p>Determine whether this permutation moves any index.</p>
   *
   * @return true if this is the identity
   */
  public boolean isIdentity() {
    return ranking.length == 0;
  }

  /**
   * Return the minimum number of elements that an array or list must have, in order for this operation to
   * be applicable.
   *
   * @return the length of this operation
   */

  public int length() {
    return ranking.length;
  }

  /**
   * Convert this permutation to a human readable string. This representation may change in the future.
   *
   * @return a String representation of this permutation.
   */
  @Override
  public String toString() {
    return Arrays.toString(ranking);
  }

  /**
   * Equality test. In order for permutations to be equal, they must have the same length, and their effects
   * on indexes and arrays must be identical.
   *
   * @param other another object
   * @return true if the other object is an equivalent permutation
   */
  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;
    return Arrays.equals(ranking, ((Permutation) other).ranking);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(ranking);
  }

  /**
   * A compare method compatible with {@code equals}: permutations compare to {@code 0}
   * if and only they are equal.
   *
   * @param other a permutation, not necessarily of the same length
   * @return the result of lexicographic comparison of {@code this.ranking} and {@code other.ranking}
   * @see #equals
   */
  @Override
  public int compareTo(Permutation other) {
    if (this == other)
      return 0;
    for (int i = 0; i < Math.min(this.ranking.length, other.ranking.length); i += 1)
      if (this.ranking[i] != other.ranking[i])
        return this.ranking[i] - other.ranking[i];
    return other.ranking.length - this.ranking.length;
  }

  /**
   * Get a copy of the ranking that represents of this permutation.
   *
   * @return a copy of the ranking
   */
  public int[] getRanking() {
    return Arrays.copyOf(ranking, ranking.length);
  }

  /**
   * Move an index. The following is true for arrays {@code a} of any type and of length
   * {@code a.length >= this.length}, and all indexes {@code 0 <= i < a.length}:
   * <code><pre>
   *   apply(a)[apply(i)] == a[i];
   * </pre></code>
   * If the input is greater than or equal to {@code this.length()}, then the same number is returned.
   *
   * @param i a non negative number
   * @return the moved index
   * @throws java.lang.IllegalArgumentException if the input is negative
   */
  public int apply(int i) {
    if (i < 0)
      negativeFailure();
    if (i >= ranking.length)
      return i;
    return ranking[i];
  }

  /* ============== apply to arrays ============== */

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see #apply(int)
   * @see #toCycles()
   * @see Cycles#apply(Object[])
   */
  public <T> T[] apply(T[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see Cycles#clobber(byte[])
   * @see #apply(int)
   */
  public byte[] apply(byte[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see Cycles#clobber(short[])
   * @see #apply(int)
   */
  public short[] apply(short[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see Cycles#clobber(int[])
   * @see #apply(int)
   */
  public int[] apply(int[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see Cycles#clobber(long[])
   * @see #apply(int)
   */
  public long[] apply(long[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see Cycles#clobber(float[])
   * @see #apply(int)
   */
  public float[] apply(float[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see #apply(int)
   * @see Cycles#clobber(double[])
   */
  public double[] apply(double[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see #apply(int)
   */
  public boolean[] apply(boolean[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array. This method does not modify its input array.
   *
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see Cycles#clobber(char[])
   * @see #apply(int)
   */
  public char[] apply(char[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange the characters in the string.
   *
   * @param s a string of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code s}
   * @throws java.lang.IllegalArgumentException if {@code s.length() < this.length()}
   * @see #apply(int)
   */
  public String apply(String s) {
    if (this.ranking.length == 0)
      return s;
    char[] dst = new char[s.length()];
    s.getChars(0, s.length(), dst, 0);
    return new String(apply(dst));
  }

  /**
   * Rearrange a list. This method does not modify the input list.
   *
   * @param input a list that must have at least {@code this.length()} elements
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input} has less than {@code this.length()} elements
   * @see Cycles#clobber(List)
   * @see #apply(int)
   */
  public <E> List<E> apply(List<E> input) {
    if (ranking.length == 0)
      return input;
    int length = input.size();
    checkLength(ranking.length, length);
    return Rankings.apply(ranking, input);
  }

  public static Permutation sorting(byte[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static Permutation sorting(short[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static Permutation sorting(long[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static Permutation sorting(float[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static Permutation sorting(double[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static final class SortingBuilder<E> {
    private final E[] a;

    public SortingBuilder(E[] a) {
      this.a = a;
    }

    public Permutation using(Comparator<E> comparator) {
      return define(Rankings.sorting(a, comparator), false);
    }
  }

  public static <E extends Comparable> Permutation sorting(E[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static Permutation sorting(char[] input) {
    return define(Rankings.sorting(input), false);
  }


  public static <E> SortingBuilder<E> sorting(E[] input) {
    return new SortingBuilder<>(input);
  }

  public static Permutation sorting(int[] input) {
    return define(Rankings.sorting(input), false);
  }

  public static Permutation sorting(String s) {
    char[] chars = new char[s.length()];
    s.getChars(0, chars.length, chars, 0);
    return sorting(chars);
  }

  public static final class TakingBuilder<E extends Comparable> {
    private final E[] from;

    private TakingBuilder(E[] from) {
      this.from = from;
    }

    public Permutation to(E[] to) {
      return define(Rankings.from(from, to), false, false);
    }
  }

  public static final class TakingBuilderInt {
    private final int[] from;

    private TakingBuilderInt(int[] from) {
      this.from = from;
    }

    public Permutation to(int[] to) {
      return define(Rankings.from(from, to), false, false);
    }
  }

  public static final class TakingBuilderLong {
    private final long[] from;

    private TakingBuilderLong(long[] from) {
      this.from = from;
    }

    public Permutation to(long[] to) {
      return define(Rankings.from(from, to), false, false);
    }
  }

  public static final class TakingBuilderDouble {
    private final double[] from;

    private TakingBuilderDouble(double[] from) {
      this.from = from;
    }

    public Permutation to(double[] to) {
      return define(Rankings.from(from, to), false, false);
    }
  }

  public static final class TakingBuilderComp<E> {

    private final E[] from;
    private final E[] to;

    private TakingBuilderComp(E[] from, E[] to) {
      this.from = from;
      this.to = to;
    }

    public Permutation using(Comparator<E> comp) {
      return define(Rankings.from(from, to, comp), false);
    }
  }

  public static final class TakingBuilderObj<E> {

    private final E[] from;

    private TakingBuilderObj(E[] from) {
      this.from = from;
    }

    public TakingBuilderComp<E> to(E[] to) {
      return new TakingBuilderComp<>(from, to);
    }

  }


  public static TakingBuilderInt taking(int[] a) {
    return new TakingBuilderInt(a);
  }

  public static <E extends Comparable> TakingBuilder<E> taking(E[] a) {
    return new TakingBuilder<>(a);
  }

  public static TakingBuilderLong taking(long[] a) {
    return new TakingBuilderLong(a);
  }

  public static TakingBuilderDouble taking(double[] a) {
    return new TakingBuilderDouble(a);
  }

  public static <E> TakingBuilderObj<E> taking(E[] a) {
    return new TakingBuilderObj<>(a);
  }

  public boolean sorts(int[] a) {
    return Rankings.sorts(ranking, a);
  }

  public boolean sorts(byte[] a) {
    return Rankings.sorts(ranking, a);
  }

  public boolean sorts(short[] a) {
    return Rankings.sorts(ranking, a);
  }

  public boolean sorts(char[] a) {
    return Rankings.sorts(ranking, a);
  }

  public boolean sorts(long[] a) {
    return Rankings.sorts(ranking, a);
  }

  public boolean sorts(float[] a) {
    return Rankings.sorts(ranking, a);
  }

  public boolean sorts(double[] a) {
    return Rankings.sorts(ranking, a);
  }

  public <E extends Comparable<E>> boolean sorts(E[] a) {
    return Rankings.sorts(ranking, a);
  }

  public <E extends Comparable<E>> boolean sorts(List<E> a) {
    return Rankings.sorts(ranking, a);
  }

  public static final class SortsBuilder<E> {
    private final E[] a;
    private final int[] ranking;

    public SortsBuilder(E[] a, int[] ranking) {
      this.a = a;
      this.ranking = ranking;
    }

    public boolean using(Comparator<E> comparator) {
      return Rankings.sorts(ranking, a, comparator);
    }
  }

  public <E> SortsBuilder<E> sorts(E[] a) {
    return new SortsBuilder<>(a, ranking);
  }

  /* Transport an array safely to Cycles constructor */
  static final class Orbits {
    static Orbits EMPTY = new Orbits(new int[0][]);
    final int[][] orbits;

    private Orbits(int[][] orbits) {this.orbits = orbits;}
  }

  public static Stream<Permutation> symmetricGroup(int n) {
    return Rankings.symmetricGroup(n).map(a -> define(a, false));
  }

}
