package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

/**
 * <p>A permutation that can be used to rearrange arrays.</p>
 */
public final class Permutation implements Comparable<Permutation> {

  /* index - > index; this array is never modified */
  private final int[] posmap;

  private static final PermutationFactory FACTORY = PermutationFactory.builder().build();
  private static final PermutationFactory STRICT_FACTORY = PermutationFactory.builder().setStrict(true).build();

  private static final Permutation[] IDENTITIES = new Permutation[]{
      new Permutation(Util.sequence(0)),
      new Permutation(Util.sequence(1))
  };

  private static final Comparator<int[]> COMPARE_2ND = new Comparator<int[]>() {
    public int compare(int[] a, int[] b) {
      return a[1] - b[1];
    }
  };

  /**
   * This returns the default non-strict factory.
   * @return the default factory
   * @see com.github.methylene.sym.PermutationFactory#isStrict
   */
  public static PermutationFactory factory() { return FACTORY; }

  /**
   * Returns the strict factory.
   * @return the strict factory
   * @see com.github.methylene.sym.PermutationFactory#isStrict
   */
  public static PermutationFactory strictFactory() { return STRICT_FACTORY; }

  /**
   * This constructor is for expert use only.
   * @param posmap posmap
   * @param validate expert setting: if false, skip a certain constructor sanity check, to save time
   *                 if we're already sure that the input is valid
   */
  public Permutation(int[] posmap, boolean validate) {
    if (validate)
      Util.validate(posmap);
    this.posmap = posmap;
  }

  /**
   * Creates a new permutation from the given array.
   * @param posmap a list of numbers that specifies the permutation in zero-based
   *               <a href="http://en.wikipedia.org/wiki/Permutation#Definition_and_usage">one-line notation</a>.
   *               For example, {@code posmap = new int[]{1, 2, 0}} creates the permutation
   *               that sends {@code new char[]{'a', 'b', 'c'}} to {@code new char[]{'c', 'a', 'b'}}.
   */
  public Permutation(int[] posmap) {
    this(posmap, true);
  }

  /**
   * Creates a new permutation from the given array, using 1-based indexes.
   * @param posmap1based a permutation definition in 1-based one-line notation.
   * @return The permutation defined by {@code posmap1based}.
   */
  static public Permutation perm1(int... posmap1based) {
    return new Permutation(Util.add(posmap1based, -1));
  }

  /**
   * Creates a new <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycle</a>.
   * A single number {@code n} creates the identity of length {@code n + 1}.
   * An emtpy input produces the permutation of length {@code 0}.
   * @param cycle a list of distinct, non-negative numbers
   * @return the cyclic permutation defined by {@code cycle}
   * @throws java.lang.IllegalArgumentException if {@code cycle} contains negative numbers or duplicates
   * @see com.github.methylene.sym.Permutation#identity
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
    return STRICT_FACTORY.sort(distinctInts(length, 4));
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
   * <p>Permutation composition. The following is true for all numbers
   * {@code 0 <= i}:</p>
   * <pre><code>
   *   this.apply(other.apply(i)) == this.comp(other).apply(i)
   * </code></pre>
   * <p>If the other permutation is of a different length, padding will be applied to the shorter of the two
   * permutations, to make the composition meaningful.</p>
   * @param other a permutation
   * @return the product of this instance and {@code other}
   * @see com.github.methylene.sym.Permutation#pad
   * @see com.github.methylene.sym.Permutation#prod
   */
  public Permutation comp(Permutation other) {
    int length = Math.max(posmap.length, other.posmap.length);
    int[] lhs = Util.pad(posmap, length);
    int[] rhs = Util.pad(other.posmap, length);
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = lhs[rhs[i]];
    return new Permutation(result);
  }

  /**
   * Create a permutation of greater {@code length}.
   * If {@code n} is a number such that {@code this.length <= n}, the following holds
   * for all indexes {@code 0 <= i < n}:
   * <pre><code>
   *   this.pad(n).apply(i) = this.apply(i) // if i is less than this.length
   *   this.pad(n).apply(i) // otherwise
   * </code></pre>
   * @param targetLength a number that is not smaller than {@code this.length}
   * @return the padded permutation
   * @throws java.lang.IllegalArgumentException if {@code targetLength} is less than {@code this.length}
   */
  public Permutation pad(int targetLength) {
    if (targetLength < posmap.length)
      throw new IllegalArgumentException("targetLength can not be less than current length");
    if (targetLength == posmap.length)
      return this;
    return new Permutation(Util.pad(posmap, targetLength));
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
   * If {@code n == 0}, the identity permutation of length {@code this.length} is returned.
   * @param n any integer
   * @return the {@code n}th power of this permutation
   */
  public Permutation pow(int n) {
    if (n == 0)
      return identity(posmap.length);
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
    int[][] posmapWithIndex = Util.withIndex(posmap);
    Arrays.sort(posmapWithIndex, COMPARE_2ND);
    int[] result = new int[posmap.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[i] = posmapWithIndex[i][0];
    return new Permutation(result);
  }

  /**
   * Creates a cycle that acts as a delete followed by an insert. Examples:
   * <pre><code>
   *   Permutation.delins(0, 2).pad(5).apply("12345");
   *   => 23145
   *   </code></pre>
   * <pre><code>
   *   Permutation.delins(3, 1).pad(5).apply("12345");
   *   => 14235
   * </code></pre>
   * @param delete a non-negative integer
   * @param insert a non-negative integer
   * @return a permutation of length {@code Math.max(delete, insert) + 1}
   * @see com.github.methylene.sym.Permutation#cycle
   */
  public static Permutation delins(int delete, int insert) {
    if (delete == insert) {
      return identity(delete);
    } else {
      int shift = delete < insert ? -1 : 1;
      int[] c = new int[Math.abs(delete - insert) + 1];
      c[0] = delete;
      for (int k = 1; k < c.length; k += 1) {
        c[k] = insert + shift * (k - 1);
      }
      return cycle(c);
    }
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
    if (i < 0 || i >= posmap.length)
      throw new IllegalArgumentException("bad index: " + i);
    int length = 1;
    int j = i;
    while ((j = apply(j)) != i)
      length += 1;
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
    for (int i = 0; i < posmap.length; i += 1) {
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
   * Write this permutation as a product of non trivial cycles.
   * @return a cycle decomposition of this permutation
   * @see com.github.methylene.sym.Permutation#cycle
   * @see com.github.methylene.sym.Permutation#isCycle
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
      result.add(cycle(orbit).pad(posmap.length));
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
    for (int i = 0; i < posmap.length; i += 1)
      if (posmap[i] != posmap.length - i - 1)
        return false;
    return true;
  }

  /**
   * <p>Determine whether this permutation performs any rearrangement of its input.</p>
   * @return true if this is an identity permutation
   */
  public boolean isIdentity() {
    for (int i = 0; i < posmap.length; i += 1)
      if (posmap[i] != i)
        return false;
    return true;
  }

  /**
   * Get the length an array must have in order for this permutation to rearrange its elements.
   * @return the length of this permutation
   */
  public int length() {
    return posmap.length;
  }

  /**
   * Convert this permutation to a human readable string. This representation may change in the future.
   * @return a String representation of this permutation.
   */
  @Override public String toString() {
    return Arrays.toString(posmap);
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
    return Arrays.equals(posmap, ((Permutation) other).posmap);
  }

  @Override public int hashCode() {
    return Arrays.hashCode(posmap);
  }

  /**
   * Standard java comparison, compatible with {@code equals} in the sense that permutations compare as {@code 0}
   * if and only they are equal.
   * @param other a permutation, not necessarily of the same length
   * @return the result of lexicographic comparison of {@code this.posmap} and {@code other.posmap}
   * @see com.github.methylene.sym.Permutation#equals
   */
  @Override public int compareTo(Permutation other) {
    if (this == other)
      return 0;
    for (int i = 0; i < Math.min(this.posmap.length, other.posmap.length); i += 1)
      if (this.posmap[i] != other.posmap[i])
        return this.posmap[i] - other.posmap[i];
    return other.posmap.length - this.posmap.length;
  }

  /**
   * Get a copy of the internal representation of this permutation.
   * @return A copy of the index map that represents this permutation.
   */
  public int[] getPosmap() {
    return Arrays.copyOf(posmap, posmap.length);
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
    if (i >= posmap.length)
      return i;
    return posmap[i];
  }

    /* ============== apply to arrays ============== */

  /**
   * Rearrange an array. Each element of the return value of this method can be safely cast to the element type
   * of the input.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public Object[] apply(Object[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    Object[] result = new Object[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array. Each element of the return value of this method can be safely cast to the element type
   * of the input.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public Comparable[] apply(Comparable[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    Comparable[] result = new Comparable[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public String[] apply(String[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    String[] result = new String[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public byte[] apply(byte[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    byte[] result = new byte[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public short[] apply(short[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    short[] result = new short[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public int[] apply(int[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    int[] result = new int[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public long[] apply(long[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    long[] result = new long[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public float[] apply(float[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    float[] result = new float[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public double[] apply(double[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    double[] result = new double[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public boolean[] apply(boolean[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange an array.
   * @param input an array of length {@code this.length()}
   * @return the result of applying this permutation to {@code input}
   * @throws java.lang.IllegalArgumentException if {@code input.length < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public char[] apply(char[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("input too short: " + input.length);
    char[] result = new char[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  /**
   * Rearrange a string. More precisely, rearrange the array that is returned from {@link String#getChars}.
   * @param s an string of length {@code this.length()}
   * @return the result of applying this permutation to {@code s}
   * @throws java.lang.IllegalArgumentException if {@code s.length() < this.length()}
   * @see com.github.methylene.sym.Permutation#apply(int)
   */
  public String apply(String s) {
    if (s.length() < posmap.length)
      throw new IllegalArgumentException("s too short: " + s.length());
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
    if (length < posmap.length)
      throw new IllegalArgumentException("input too short: " + length);
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
   * @return the result of rearranging a particular standard array using this instance
   * @see com.github.methylene.sym.Util#symbols
   * @see com.github.methylene.sym.Permutation#apply(String[])
   */
  public String[] apply() {
    return apply(Util.symbols(posmap.length));
  }

}
