package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>A permutation that can be used to rearrange arrays.</p>
 */
public final class Permutation implements Comparable<Permutation> {

  /*
   *  An array of N integers where each of the integers between 0 and N-1 appears exactly once.
   *  This array is never modified.
   */
  private final int[] ranking;

  private static final Permutation[] IDENTITIES = new Permutation[]{
      new Permutation(Util.sequence(0)),
      new Permutation(Util.sequence(1))
  };


  private Permutation(int[] ranking, boolean validate) {
    if (validate)
      if (!Util.isRanking(ranking))
        throw new IllegalArgumentException("input is not a ranking");
    this.ranking = ranking;
  }

  /**
   * Creates a new permutation from the given array.
   * @param ranking a list of numbers that specifies the permutation in zero-based
   *               <a href="http://en.wikipedia.org/wiki/Permutation#Definition_and_usage">one-line notation</a>.
   *               For example, {@code ranking = new int[]{1, 2, 0}} creates the permutation
   *               that sends {@code new char[]{'a', 'b', 'c'}} to {@code new char[]{'c', 'a', 'b'}}.
   * @throws java.lang.IllegalArgumentException if the input is not a ranking
   */
  public Permutation(int[] ranking) {
    this(ranking, true);
  }

  /**
   * Creates a new permutation from the given array, using 1-based indexes.
   * @param ranking1based a permutation definition in 1-based one-line notation.
   * @return The permutation defined by {@code ranking1based}.
   */
  static public Permutation perm1(int... ranking1based) {
    return new Permutation(Util.add(ranking1based, -1));
  }

  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycle</a>.
   * A single number {@code n} creates the identity of length {@code n + 1}.
   * An emtpy input produces the permutation of length {@code 0}.
   * @param cycle a list of distinct, non-negative numbers
   * @return the cyclic permutation defined by {@code cycle}
   * @throws java.lang.IllegalArgumentException if {@code cycle} contains negative numbers or duplicates
   */
  static public Permutation cycle(int... cycle) {
    int maxIndex = -1;
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
    return sort(distinctInts(length, 4));
  }

  /**
   * Creates the identity permutation of given length.
   * @param length the length of arrays that the resulting permutation can be applied to
   * @return the identity permutation that can be applied to an array of length {@code length}
   * @see Permutation#isIdentity
   */
  public static Permutation identity(int length) {
    if (length < IDENTITIES.length)
      return IDENTITIES[length];
    return new Permutation(Util.sequence(length));
  }

  /**
   * <p>Permutation composition. The following is true for all non-negative numbers
   * {@code i}:</p>
   * <pre><code>
   *   this.apply(other.apply(i)) == this.comp(other).apply(i)
   * </code></pre>
   * <p>If the other permutation is of a different length, padding will be applied to the shorter of the two
   * permutations, to make the composition meaningful.</p>
   * @param other a permutation
   * @return the product of this instance and {@code other}
   * @see com.github.methylene.sym.Permutation#padding
   * @see com.github.methylene.sym.Permutation#prod
   */
  public Permutation comp(Permutation other) {
    int length = Math.max(ranking.length, other.ranking.length);
    int[] lhs = Util.padding(ranking, length);
    int[] rhs = Util.padding(other.ranking, length);
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = lhs[rhs[i]];
    return new Permutation(result);
  }

  /**
   * Create a permutation of greater or equal {@code length}.
   * If {@code n} is a number such that {@code this.length <= n}, the following holds
   * for all indexes {@code 0 <= i < n}:
   * <pre><code>
   *   this.padding(n).apply(i) == this.apply(i) // if i < this.length()
   *   this.padding(n).apply(i) == i // if i >= this.length()
   * </code></pre>
   * @param targetLength a number that is not smaller than {@code this.length}
   * @return the padded permutation
   * @throws java.lang.IllegalArgumentException if {@code targetLength} is less than {@code this.length}
   */
  public Permutation padding(int targetLength) {
    if (targetLength < ranking.length)
      throw new IllegalArgumentException("targetLength can not be less than current length");
    if (targetLength == ranking.length)
      return this;
    return new Permutation(Util.padding(ranking, targetLength));
  }

  /**
   * Take the product of the given permutations. If the input is empty, a permutation of length {@code 0} is returned.
   * @param permutations an array of permutations
   * @return the product of the input
   * @see com.github.methylene.sym.Permutation#comp
   */
  public static Permutation prod(Permutation... permutations) {
    Permutation result = identity(0);
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
    Permutation result = identity(0);
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
      return identity(ranking.length);
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
    return new Permutation(PermutationFactory.invert(ranking));
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
   * Calculate the number of times this must be applied to given index until
   * it is in its original position again, also know as the orbit length of the index.
   * @param i a non negative number which is less than {@code this.length()}
   * @return the length of the orbit of {@code i}
   * @throws java.lang.IllegalArgumentException if {@code i < 0} or {@code i >= this.length}
   * @see com.github.methylene.sym.Permutation#orbit
   */
  public int orbitLength(int i) {
    if (i < 0 || i >= ranking.length)
      throw new IllegalArgumentException("bad index: " + i);
    int length = 1;
    int j = i;
    while ((j = apply(j)) != i)
      length += 1;
    return length;
  }

  private int[] orbit(int pos, int orbitLength) {
    if (pos < 0 || pos >= ranking.length)
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
    return orbit(i, orbitLength(i));
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
    while (!p.isIdentity()) {
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
    int[] candidate = null;
    for (int i = 0; i < ranking.length; i += 1) {
      int orbitLength = orbitLength(i);
      if (orbitLength > 1) {
        if (candidate == null) {
          candidate = Util.sortedCopy(orbit(i, orbitLength));
        } else {
          if (Arrays.binarySearch(candidate, i) < 0) {
            return false;
          }
        }
      }
    }
    return true;
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
    LinkedList<int[]> orbits = new LinkedList<int[]>();
    boolean[] done = new boolean[ranking.length];
    for (int i = 0; i < ranking.length; i += 1) {
      if (!done[i]) {
        int orbitLength = orbitLength(i);
        if (orbitLength > 1) {
          int[] candidate = orbit(i, orbitLength);
          for (int k : candidate)
            done[k] = true;
          boolean goodCandidate = true;
          for (int[] orbit : orbits) {
            if (orbit.length == candidate.length) {
              if (Util.indexOf(orbit, candidate[0]) != -1) {
                goodCandidate = false;
                break;
              }
            }
          }
          if (goodCandidate)
            orbits.push(candidate);
        }
      }
    }
    ArrayList<Permutation> result = new ArrayList<Permutation>(orbits.size());
    for (int[] orbit : orbits) {
      result.add(cycle(orbit).padding(ranking.length));
    }
    return result;
  }

  /**
   * Write this permutation as a product of transpositions.
   * @return a decomposition of this permutation into transpositions
   * @see Permutation#prod
   * @see Permutation#swap
   */
  public List<Permutation> toTranspositions() {
    List<Permutation> result = new ArrayList<Permutation>();
    for (Permutation cycle : toCycles()) {
      int[] orbit = null;
      for (int i = 0; i < cycle.ranking.length; i += 1) {
        int orbitLength = cycle.orbitLength(i);
        if (orbitLength > 1)
          orbit = cycle.orbit(cycle.ranking[i], orbitLength);
      }
      assert orbit != null;
      for (int i = 0; i < orbit.length - 1; i += 1)
        result.add(swap(orbit[i], orbit[i + 1]).padding(ranking.length));
    }
    return result;
  }

  /**
   * Calculate the <a href="http://en.wikipedia.org/wiki/Parity_of_a_permutation">signature</a> of this permutation.
   * @return {@code 1} if this permutation can be written as an even number of transpositions, {@code -1} otherwise
   * @see com.github.methylene.sym.Permutation#swap
   * @see com.github.methylene.sym.Permutation#toTranspositions
   */
  public int signature() {
    return toTranspositions().size() % 2 == 0 ? 1 : -1;
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
    return new Permutation(result);
  }

  /**
   * Determine whether this permutation reverses its input.
   * @return true if this permutation reverses or "flips" its input
   * @see com.github.methylene.sym.Permutation#reverse
   */
  public boolean isReverse() {
    for (int i = 0; i < ranking.length; i += 1)
      if (ranking[i] != ranking.length - i - 1)
        return false;
    return true;
  }

  /**
   * <p>Determine whether this permutation performs any rearrangement of its input.</p>
   * @return true if this is an identity permutation
   */
  public boolean isIdentity() {
    for (int i = 0; i < ranking.length; i += 1)
      if (ranking[i] != i)
        return false;
    return true;
  }

  /**
   * Get the length an array must have in order for this permutation to rearrange its elements.
   * @return the length of this permutation
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

  @Override public int hashCode() {
    return Arrays.hashCode(ranking);
  }

  /**
   * Standard java comparison, compatible with {@code equals} in the sense that permutations compare as {@code 0}
   * if and only they are equal.
   * @param other a permutation, not necessarily of the same length
   * @return the result of lexicographic comparison of {@code this.ranking} and {@code other.ranking}
   * @see com.github.methylene.sym.Permutation#equals
   */
  @Override public int compareTo(Permutation other) {
    if (this == other)
      return 0;
    for (int i = 0; i < Math.min(this.ranking.length, other.ranking.length); i += 1)
      if (this.ranking[i] != other.ranking[i])
        return this.ranking[i] - other.ranking[i];
    return other.ranking.length - this.ranking.length;
  }

  /**
   * Get a copy of the internal representation of this permutation.
   * @return A copy of the index map that represents this permutation.
   */
  public int[] getRanking() {
    return Arrays.copyOf(ranking, ranking.length);
  }


  /**
   * Move an index. The following is true for arrays {@code a} of any type and of length
   * {@code a.length == this.length}, and all indexes {@code 0 <= i < a.length}:
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
      throw new IllegalArgumentException("negative index: " + i);
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
    return PermutationFactory.apply(ranking, input);
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
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public String[] apply(String[] input) {
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public byte[] apply(byte[] input) {
    return PermutationFactory.apply(ranking, input);
  }


  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public short[] apply(short[] input) {
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public int[] apply(int[] input) {
    return PermutationFactory.apply(ranking, input);
  }


  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public long[] apply(long[] input) {
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public float[] apply(float[] input) {
    return PermutationFactory.apply(ranking, input);
  }


  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public double[] apply(double[] input) {
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public boolean[] apply(boolean[] input) {
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange an array.
   * @param input an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public char[] apply(char[] input) {
    return PermutationFactory.apply(ranking, input);
  }

  /**
   * Rearrange the return value of {@link String#getChars}.
   * @param s a string of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code s}
   * @throws java.lang.IllegalArgumentException if {@code s.length() < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public String apply(String s) {
    if (s.length() < ranking.length)
      throw new IllegalArgumentException("input too short: " + s.length() + ", minimum input length is " + ranking.length);
    char[] dst = new char[s.length()];
    s.getChars(0, s.length(), dst, 0);
    return new String(apply(dst));
  }

  /**
   * Rearrange a list.
   * @param input a list that has exactly {@code this.length()} elements
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input} does not have exactly {@code this.length()} elements
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public <E> List<E> apply(List<E> input) {
    int length = input.size();
    if (length < ranking.length)
      throw new IllegalArgumentException("input too short: " + length + ", minimum input size is " + ranking.length);
    ArrayList<E> result = new ArrayList<E>(length);
    for (int i = 0; i < length; i += 1)
      result.add(null);
    for (int i = 0; i < length; i += 1)
      result.set(apply(i), input.get(i));
    return result;
  }

  /**
   * Rearrange the array that is obtained by calling
   * <pre><code>
   *   Util.symbols(this.length());
   * </code></pre>
   * @return the result of applying this permutation to a certain standard array of length {@code this.length}
   * @see com.github.methylene.sym.Util#symbols
   * @see com.github.methylene.sym.Permutation#apply(String[])
   */
  public String[] apply() {
    return apply(Util.symbols(ranking.length));
  }

  /**
   * Returns a permutation that sorts the input.
   * @param input an array
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(byte[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @param input an array
   * @see com.github.methylene.sym.Permutation#sort(char[]) */
  public static Permutation sort(short[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(long[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(float[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }


  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static Permutation sort(double[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.Permutation#sort(char[])
   */
  public static <E extends Comparable> Permutation sort(E[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }


  /**
   * Returns a certain permutation that sorts the input array.
   * TODO describe more precisely which permutation is chosen if there is a choice
   * @param input an array, not necessarily distinct
   * @return a permutation that sorts the input
   * @throws java.lang.IllegalArgumentException if {@code strictness} is true and {@code input} contains duplicates
   */
  public static Permutation sort(char[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
  }

  /** @see com.github.methylene.sym.Permutation#sort(char[]) */
  public static <E> Permutation sort(E[] input, Comparator<E> comp) {
    return new Permutation(PermutationFactory.sort(input, comp), false);
  }


  /** @see com.github.methylene.sym.Permutation#sort(char[]) */
  public static Permutation sort(int[] input) {
    return new Permutation(PermutationFactory.sort(input), false);
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
    return new Permutation(PermutationFactory.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @throws java.lang.NullPointerException if {@code a} or {@code b} contain null
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static <E extends Comparable> Permutation from(E[] a, E[] b) {
    return new Permutation(PermutationFactory.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(byte[] a, byte[] b) {
    return new Permutation(PermutationFactory.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(long[] a, long[] b) {
    return new Permutation(PermutationFactory.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(float[] a, float[] b) {
    return new Permutation(PermutationFactory.from(a, b), false);
  }


  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static Permutation from(double[] a, double[] b) {
    return new Permutation(PermutationFactory.from(a, b), false);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @see com.github.methylene.sym.Permutation#from(int[], int[])
   */
  public static <E> Permutation from(E[] a, E[] b, Comparator<E> comp) {
    return new Permutation(PermutationFactory.from(a, b, comp), false);
  }

}
