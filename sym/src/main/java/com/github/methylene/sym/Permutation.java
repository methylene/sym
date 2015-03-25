package com.github.methylene.sym;

import static com.github.methylene.sym.Util.checkLength;
import static com.github.methylene.sym.Util.negativeFailure;
import com.github.methylene.lists.LookupList;

import java.util.*;

/**
 * <p>A permutation that can be used to rearrange arrays.</p>
 */
public final class Permutation implements Comparable<Permutation> {

  public static final Transposition[] DESTRUCTIVE_0 = new Transposition[0];
  public static final Transposition.Factory NON_CACHING_FACTORY = new Transposition.Factory(0);

  /*
   *  An array of N integers where each of the integers between 0 and N-1 appears exactly once.
   *  This array is never modified.
   */
  private final int[] ranking;

  private static final Permutation IDENTITY = new Permutation(new int[0], false);

  private Permutation(int[] ranking, boolean validate) {
    ranking = Rankings.trim(ranking);
    this.ranking = validate ? Rankings.checkRanking(ranking) : ranking;
  }

  /**
   * Return the permutation defined by the given array.
   * @param ranking a list of numbers that specifies the permutation in zero-based
   *               <a href="http://en.wikipedia.org/wiki/Permutation#Definition_and_usage">one-line notation</a>.
   *               For example, {@code perm(1, 2, 0)} creates the permutation
   *               that maps {@code "abc"} to {@code "cab"}.
   * @throws java.lang.IllegalArgumentException if the input is not a ranking
   */
  public static Permutation perm(int... ranking) {
    return perm(ranking, true);
  }

  private static Permutation perm(int[] ranking, boolean validate) {
    int[] trimmed = Rankings.trim(ranking);
    if (trimmed.length == 0)
      return IDENTITY;
    return new Permutation(ranking == trimmed ? Arrays.copyOf(ranking, ranking.length) : trimmed, validate);
  }

  /**
   * Creates a new permutation from the given array, using 1-based notation.
   * @param ranking1based a permutation definition in 1-based one-line notation.
   * @return The permutation defined by {@code ranking1based}.
   */
  public static Permutation perm1(int... ranking1based) {
    return perm(Util.add(ranking1based, -1));
  }

  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycle</a>.
   * A single number {@code n} creates the identity of length {@code n + 1}.
   * An emtpy input produces the permutation of length {@code 0}.
   * @param cycle a list of distinct, non-negative numbers
   * @return the cyclic permutation defined by {@code cycle}
   * @throws java.lang.IllegalArgumentException if {@code cycle} contains negative numbers or duplicates
   */
  public static Permutation cycle(int... cycle) {
    return perm(Cycles.asRanking(cycle), false);
  }


  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycle</a>.
   * @param cycle1based a list of numbers that defines a permutation in 1-based cycle notation
   * @return the cyclic permutation defined by {@code cycle1based}
   * @see com.github.methylene.sym.Permutation#cycle
   */
  public static Permutation cycle1(int... cycle1based) {
    return cycle(Util.add(cycle1based, -1));
  }

  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">transposition</a>.
   * @param i a positive integer
   * @param j a positive integer
   * @return the permutation of length {@code Math.max(i, j)} that swaps the elements at indexes {@code i}
   * and {@code j}
   * @see com.github.methylene.sym.Permutation#cycle
   */
  public static Permutation swap(int i, int j) {
    return cycle(i, j);
  }

  /**
   * Creates a random permutation of given length.
   * @param length the length of arrays that the result can be applied to
   * @return a random permutation that can be applied to an array of length {@code length}
   */
  public static Permutation random(int length) {
    return perm(Rankings.random(length), false);
  }

  /**
   * Return the identity permutation. It is the only permutation that can be applied to arrays of any length.
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
   *   this.apply(other.apply(i)) == this.comp(other).apply(i)
   * </code></pre>
   * @param other a permutation
   * @return the product of this instance and {@code other}
   * @see com.github.methylene.sym.Permutation#prod
   */
  public Permutation comp(Permutation other) {
    if (this.isIdentity())
      return other;
    if (other.ranking.length == 0)
      return this;
    return perm(Rankings.comp(this.ranking, other.ranking), false);
  }

  /**
   * Take the product of the given permutations. If the input is empty, a permutation of length {@code 0} is returned.
   * @param permutations an array of permutations
   * @return the product of the input
   * @see com.github.methylene.sym.Permutation#comp
   */
  public static Permutation prod(Permutation... permutations) {
    Permutation result = identity();
    for (Permutation permutation : permutations)
      result = result.comp(permutation);
    return result;
  }

  /**
   * Take the product of the given permutations. If the input is empty, a permutation of length {@code 0} is returned.
   * @param permutations an iterable of permutations
   * @return the product of the input
   * @see com.github.methylene.sym.Permutation#comp
   */
  public static Permutation prod(Iterable<Permutation> permutations) {
    Permutation result = identity();
    for (Permutation permutation : permutations)
      result = result.comp(permutation);
    return result;
  }


  /**
   * Raise this permutation to the {@code n}th power.
   * If {@code n} is positive, the product
   * <pre><code>
   *   this.comp(this)[...]comp(this)
   * </code></pre>
   * ({@code n} times) is returned.
   * If {@code n} is negative, the product
   * <pre><code>
   *   this.invert().comp(this.invert()) [...] comp(this.invert());
   * </code></pre>
   * ({@code -n} times) is returned.
   * If {@code n} is zero, the identity permutation of length {@code this.length} is returned.
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
      result = result.comp(seed);
    return result;
  }

  /**
   * <p>Inverts this permutation. The following always returns true:</p>
   * <pre><code>
   *   this.comp(this.inverse).isIdentity();
   * </code></pre>
   * @return the inverse of this permutation
   * @see com.github.methylene.sym.Permutation#comp
   * @see com.github.methylene.sym.Permutation#isIdentity
   */
  public Permutation invert() {
    if (this.ranking.length == 0)
      return this;
    return perm(Rankings.invert(ranking), false);
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
   * @param delete a non-negative integer
   * @param insert a non-negative integer
   * @return a permutation of length {@code Math.max(delete, insert) + 1}
   * @see com.github.methylene.sym.Permutation#cycle
   */
  public static Permutation move(int delete, int insert) {
    return cycle(Util.sequence(insert, delete, true));
  }

  /**
   * <p>Calculate the orbit of given index.
   * The orbit is an array of distinct integers that contains all indexes</p>
   * <pre><code>
   *   i, apply(i), apply(apply(i)), ...
   * </code></pre>
   * <p>The orbit always contains at least one element, that is the start index {@code i} itself.</p>
   * @param i a non negative number which is less than {@code this.length()}
   * @return the orbit of {@code i}
   * @throws java.lang.IllegalArgumentException if {@code i < 0} or {@code i >= this.length}.
   */
  public int[] orbit(int i) {
    return Cycles.orbit(ranking, i);
  }

  /**
   * <p>Calculate the order of this permutation. The order is the smallest positive number {@code n}
   * such that</p>
   * <pre><code>
   *   this.pow(n).isIdentity();
   * </code></pre>
   * @return the order of this permutation
   * @throws java.lang.IllegalArgumentException if {@code pos < 0} or {@code pos >= this.length}
   * @see com.github.methylene.sym.Permutation#isIdentity
   * @see com.github.methylene.sym.Permutation#pow
   */
  public int order() {
    int i = 1;
    Permutation p = this;
    while (p.ranking.length != 0) {
      i += 1;
      p = p.comp(this);
    }
    return i;
  }

  /**
   * <p>Determine whether this permutation has at most one than one nontrivial orbit.</p>
   * @return true if this permutation is a cycle
   * @see com.github.methylene.sym.Permutation#toCycles
   * @see com.github.methylene.sym.Permutation#cycle
   * @see com.github.methylene.sym.Permutation#orbit
   */
  public boolean isCycle() {
    return Cycles.isCyclicRanking(ranking);
  }

  /**
   * <p>Write this permutation as a product of cycles.</p>
   * <p>For every permutation {@code p} in the returned list, the following are true:</p>
   * <pre><code>
   *   p.isCycle() == true;
   *   p.isIdentity() == false;
   * </code></pre>
   * @return a cycle decomposition of this permutation
   * @see com.github.methylene.sym.Permutation#cycle
   * @see com.github.methylene.sym.Permutation#isCycle
   */
  public List<Permutation> toCycles() {
    if (this.ranking.length == 0)
      return Collections.emptyList();
    List<int[]> orbits = Cycles.toOrbits(ranking);
    ArrayList<Permutation> result = new ArrayList<Permutation>(orbits.size());
    for (int[] orbit : orbits)
      result.add(cycle(orbit));
    return result;
  }
  /**
   * Write this permutation as a product of transpositions.
   * @return a decomposition of this permutation into transpositions
   * @param factory transposition factory
   * @see Permutation#prod
   * @see Permutation#swap
   */
  public List<Transposition> toTranspositions(Transposition.Factory factory) {
    if (this.ranking.length == 0)
      return Collections.emptyList();
    return Cycles.toTranspositions(ranking, factory);
  }

  /**
   * Write this permutation as a product of transpositions.
   * @return a decomposition of this permutation into transpositions
   * @see Permutation#prod
   * @see Permutation#swap
   */
  public List<Transposition> toTranspositions() {
    return toTranspositions(NON_CACHING_FACTORY);
  }

  /**
   * Get a compiled version of this operation, which may use less memory and can be used to change arrays in place.
   * @param factory transposition factory
   * @return a destructive version of this instance
   */
  public CompiledPermutation compile(Transposition.Factory factory) {
    if (this.ranking.length == 0)
      return CompiledPermutation.IDENTITY;
    return CompiledPermutation.create(toTranspositions(factory));
  }

  /**
   * Get a compiled version of this operation, which may use less memory and can be used to change arrays in place.
   * @return a destructive version of this instance
   */
  public CompiledPermutation compile() {
    return compile(NON_CACHING_FACTORY);
  }

  /**
   * Calculate the <a href="http://en.wikipedia.org/wiki/Parity_of_a_permutation">signature</a> of this permutation.
   * @return {@code 1} if this permutation can be written as an even number of transpositions, {@code -1} otherwise
   * @see com.github.methylene.sym.Permutation#swap
   * @see com.github.methylene.sym.Permutation#toTranspositions
   */
  public int signature() {
    return signature(NON_CACHING_FACTORY);
  }

  public int signature(Transposition.Factory factory) {
    return toTranspositions(factory).size() % 2 == 0 ? 1 : -1;
  }

  /**
   * <p>Returns a permutation that reverses its input. Example:</p>
   * <pre><code>
   *   Permutation.reverse(5).apply("12345");
   *   => 54321
   * </code></pre>
   * @param length a non negative number
   * @return a permutation that reverses an array of length {@code length}
   */
  public static Permutation reverse(int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = length - i - 1;
    }
    return perm(result, false);
  }

  /**
   * Check if this permutation reverses its input.
   * @return true if this permutation reverses or "flips" an input of length {@code n}
   * @param n a nonnegative number
   * @see #reverse
   * @throws java.lang.IllegalArgumentException if {@code n} is negative
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
   * @param n a non negative number
   * @return the shifted permutation
   * @throws java.lang.IllegalArgumentException if n is negative
   */
  public Permutation shift(int n) {
    if (ranking.length == 0 && n == 0)
      return this;
    return perm(Rankings.shift(n, ranking), false);
  }

  /**
   * Find a cycle in this permutation or return {@code null} if this is the identity.
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
   * @return true if this is the identity
   */
  public boolean isIdentity() {
    return ranking.length == 0;
  }

  /**
   * Return the minimum number of elements that an array or list must have, in order for this operation to
   * be applicable.
   * @return the length of this operation
   */

  public int length() {
    return ranking.length;
  }

  /**
   * Convert this permutation to a human readable string. This representation may change in the future.
   * @return a String representation of this permutation.
   */
  @Override public String toString() {
    return Arrays.toString(ranking);
  }

  /**
   * Equality test. In order for permutations to be equal, they must have the same length, and their effects
   * on indexes and arrays must be identical.
   * @param other another object
   * @return true if the other object is an equivalent permutation
   */
  @Override public boolean equals(Object other) {
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
   * @param other a permutation, not necessarily of the same length
   * @return the result of lexicographic comparison of {@code this.ranking} and {@code other.ranking}
   * @see com.github.methylene.sym.Permutation#equals
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
   * Rearrange an array. Each element of the return value of this method can be safely cast to the element type
   * of the input.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public Object[] apply(Object[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }


  /**
   * Rearrange an array. Each element of the return value of this method can be safely cast to the element type
   * of the input.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public Comparable[] apply(Comparable[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public String[] apply(String[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public byte[] apply(byte[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }


  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public short[] apply(short[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public int[] apply(int[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }


  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public long[] apply(long[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public float[] apply(float[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }


  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public double[] apply(double[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public boolean[] apply(boolean[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public char[] apply(char[] input) {
    if (this.ranking.length == 0)
      return input;
    return Rankings.apply(ranking, input);
  }

  /**
   * Rearrange the return value of {@link String#getChars}.
   * @param s a string of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code s}
   * @throws java.lang.IllegalArgumentException if {@code s.length() < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public String apply(String s) {
    if (this.ranking.length == 0)
      return s;
    char[] dst = new char[s.length()];
    s.getChars(0, s.length(), dst, 0);
    return new String(apply(dst));
  }

  /**
   * Rearrange a list.
   * @param input a list that must have at least {@code this.length()} elements
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input} has less than {@code this.length()} elements
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public <E> List<E> apply(List<E> input) {
    if (ranking.length == 0)
      return input;
    int length = input.size();
    checkLength(ranking.length, length);
    if (input instanceof LookupList)
      return ((LookupList<E>) input).shuffle(this);
    return Rankings.apply(ranking, input);
  }

  /**
   * Returns a permutation that sorts the input.
   * @param input an array
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(byte[] input) {
    return perm(Rankings.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @param input an array
   * @see com.github.methylene.sym.Permutation#sort(char[]) */
  public static Permutation sort(short[] input) {
    return perm(Rankings.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(long[] input) {
    return perm(Rankings.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(float[] input) {
    return perm(Rankings.sort(input), false);
  }


  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(double[] input) {
    return perm(Rankings.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static <E extends Comparable> Permutation sort(E[] input) {
    return perm(Rankings.sort(input), false);
  }


  /**
   * Returns a certain permutation that sorts the input array.
   * TODO describe more precisely which permutation is chosen if there is a choice
   * @param input an array, not necessarily distinct
   * @return a permutation that sorts the input
   * @throws java.lang.IllegalArgumentException if {@code strictness} is true and {@code input} contains duplicates
   */
  public static Permutation sort(char[] input) {
    return perm(Rankings.sort(input), false);
  }

  /** @see com.github.methylene.sym.Permutation#sort(char[]) */
  public static <E> Permutation sort(Object[] input, Comparator<E> comp) {
    return perm(Rankings.sort(input, comp), false);
  }


  /** @see com.github.methylene.sym.Permutation#sort(char[]) */
  public static Permutation sort(int[] input) {
    return perm(Rankings.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input string.
   * @param s a string
   * @return a permutation that sorts {@code s}
   */
  public static Permutation sort(String s) {
    char[] chars = new char[s.length()];
    s.getChars(0, chars.length, chars, 0);
    return sort(chars);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @param a an array
   * @param b an array that can be obtained by changing the order of the elements of {@code a}
   * @return a permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true
   * @throws java.lang.IllegalArgumentException if {@code b} is not a rearrangement of {@code a}.
   */
  public static Permutation from(int[] a, int[] b) {
    return perm(Rankings.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @throws java.lang.NullPointerException if {@code a} or {@code b} contain null
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static <E extends Comparable> Permutation from(E[] a, E[] b) {
    return perm(Rankings.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(byte[] a, byte[] b) {
    return perm(Rankings.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(long[] a, long[] b) {
    return perm(Rankings.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(float[] a, float[] b) {
    return perm(Rankings.from(a, b), false);
  }


  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(double[] a, double[] b) {
    return perm(Rankings.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static <E> Permutation from(E[] a, E[] b, Comparator<E> comp) {
    return perm(Rankings.from(a, b, comp), false);
  }


  /* ================= sorts ================= */

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(int[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(byte[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(short[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(char[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(long[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(float[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public boolean sorts(double[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @return true if {@code this.apply(a)} is sorted
   */
  public <E extends Comparable<E>> boolean sorts(E[] a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a a list
   * @return true if {@code this.apply(a)} is sorted
   */
  public <E extends Comparable<E>> boolean sorts(List<E> a) {
    return Rankings.sorts(ranking, a);
  }

  /**
   * Check if this permutation will sort the input when applied to it.
   * @param a an array
   * @param comparator a Comparator
   * @return true if {@code this.apply(a)} is sorted
   */
  @SuppressWarnings("unchecked")
  public <E> boolean sorts(Comparator<E> comparator, Object[] a) {
    return Rankings.sorts(ranking, a, (Comparator) comparator);
  }

  /**
   * A list of transpositions
   */
  public static final class CompiledPermutation {

    static final CompiledPermutation IDENTITY = new CompiledPermutation(new Transposition[0]);

    private final Transposition[] transpositions;
    private final int length;

    private CompiledPermutation(Transposition[] transpositions) {
      int length = 0;
      for (Transposition t : transpositions)
        length = Math.max(length, t.length());
      this.transpositions = transpositions;
      this.length = length;
    }

    public static CompiledPermutation create(List<Transposition> transpositions) {
      if (transpositions.isEmpty())
        return IDENTITY;
      return new CompiledPermutation(transpositions.toArray(new Transposition[transpositions.size()]));
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(int[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(byte[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(char[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(short[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(float[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(double[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(long[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input array.
     * @param array an array
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     */
    public void clobber(Object[] array) {
      checkLength(length, array.length);
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(array);
    }

    /**
     * Apply this operation by modifying the input list.
     * The input list must support {@link java.util.List#set(int, Object)}.
     * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
     * @param list an array
     * @throws java.lang.UnsupportedOperationException if the input list is not mutable
     * @throws java.lang.IllegalArgumentException if {@code list.size() < this.length()}
     */
    public void clobber(List<?> list) {
      checkLength(length, list.size());
      for (int i = transpositions.length - 1; i != -1; i--)
        transpositions[i].clobber(list);
    }

    public int[] apply(int[] a) {
      int[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public byte[] apply(byte[] a) {
      byte[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public char[] apply(char[] a) {
      char[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public short[] apply(short[] a) {
      short[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public float[] apply(float[] a) {
      float[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public double[] apply(double[] a) {
      double[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public long[] apply(long[] a) {
      long[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public <E> E[] apply(E[] a) {
      E[] copy = Arrays.copyOf(a, a.length);
      clobber(copy);
      return copy;
    }

    public <E> List<E> apply(List<E> a) {
      ArrayList<E> copy = new ArrayList<E>(a.size());
      for (int i = 0; i < a.size(); i++)
        copy.set(i, a.get(apply(i)));
      return copy;
    }

    public int apply(int j) {
      for (int i = transpositions.length - 1; i != -1; i--)
        j = transpositions[i].apply(j);
      return j;
    }

    /**
     * Get the transposition that define this operation.
     * @return a copy of the array of transpositions
     */
    public Transposition[] getTranspositions() {
      return Arrays.copyOf(transpositions, transpositions.length);
    }

    /**
     * Uncompile this operation.
     * @return an uncompiled version of this operation
     */
    public Permutation toPermutation() {
      Permutation p = Permutation.identity();
      for (Transposition t : transpositions)
        p = p.comp(Permutation.swap(t.getFirst(), t.getSecond()));
      return p;
    }

    public String toString() {
      return Arrays.toString(transpositions);
    }

    /**
     * Return the minimum number of elements that an array or list must have, in order for this operation to
     * be applicable.
     * @return the length of this operation
     */
    public int length() {
      return length;
    }

  }

}
