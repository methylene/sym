package com.github.methylene.sym;

import static com.github.methylene.sym.Util.checkLength;
import static com.github.methylene.sym.Util.negativeFailure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An operation that swaps two elements of an array or list.
 */
public final class Transposition {

  /**
   * A factory that creates transpositions.
   * Transpositions are immutable, so various caching strategies can be considered.
   */
  public interface TranspositionFactory {
    Transposition swap(int j, int k);
  }

  public static final TranspositionFactory NON_CACHING_FACTORY = new DefaultTranspositionFactory(0);

  final int j;
  final int k;

  /**
   * A simple caching factory that maintains a permanent cache of transpositions below the configured length.
   */
  public static final class DefaultTranspositionFactory implements TranspositionFactory {

    private final Transposition[][] cache;

    /**
     * Create a new factory. If {@code maxCachedLength > 0}, the transpositions returned by the
     * {@code swap} method will be cached and reused if their {@code length} is {@code <= maxCachedLength}.
     * The cache will permanently store up to {@code maxCachedLength * (maxCachedLength - 1)} transpositions.
     * @param maxCachedLength the maximum index that is moved by a cached transposition
     */
    public DefaultTranspositionFactory(int maxCachedLength) {
      this.cache = new Transposition[maxCachedLength][];
      for (int j = 1; j < maxCachedLength; j++)
        cache[j] = new Transposition[j];
    }

    /**
     * Get a transposition operation that swaps the element at the given indexes.
     * @param j a non-negative number
     * @param k a non-negative number that must not be the same as {@code j}
     * @return a transposition operation
     * @throws java.lang.IllegalArgumentException if the arguments are equal or negative
     */
    public Transposition swap(int j, int k) {
      if (j < 0 || k < 0)
        Util.negativeFailure();
      if (j == k)
        throw new IllegalArgumentException("arguments must not be equal");
      if (k > j) {
        // make sure that j is larger than k
        int temp = k;
        k = j;
        j = temp;
      }
      if (j < cache.length) {
        if (cache[j][k] == null)
          cache[j][k] = new Transposition(j, k);
        return cache[j][k];
      }
      return new Transposition(j, k);
    }

  }

  /**
   * Create a new transposition operation that swaps the elements at the given indexes.
   * @param j a non-negative number
   * @param k a non-negative number
   * @return the transposition that swaps the elements at {@code j} and {@code k}
   * @throws IllegalArgumentException if {@code j < 0}, {@code k < 0} or {@code j == k}
   */
  public static Transposition swap(int j, int k) {
    return NON_CACHING_FACTORY.swap(j, k);
  }

  private Transposition(int j, int k) {
    assert j > k;
    this.j = j;
    this.k = k;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(int[] array) {
    checkLength(j, array.length);
    int temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(byte[] array) {
    byte temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(char[] array) {
    char temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(short[] array) {
    short temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(float[] array) {
    float temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(double[] array) {
    double temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(long[] array) {
    long temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws java.lang.IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(Object[] array) {
    Object temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input list.
   * The input list must support {@link java.util.List#set(int, Object)}.
   * @param list an array
   * @throws java.lang.UnsupportedOperationException if the input list is not mutable
   * @throws java.lang.IllegalArgumentException if {@code list.size() < this.length()}
   */
  public <E> void clobber(List<E> list) {
    E temp = list.get(k);
    list.set(k, list.get(j));
    list.set(j, temp);
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public int[] apply(int[] a) {
    int[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public byte[] apply(byte[] a) {
    byte[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public char[] apply(char[] a) {
    char[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public short[] apply(short[] a) {
    short[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public float[] apply(float[] a) {
    float[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public double[] apply(double[] a) {
    double[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public long[] apply(long[] a) {
    long[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new array. This method does not modify the input.
   * @param a an array of length not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.length < this.length()}
   */
  public <E> E[] apply(E[] a) {
    E[] copy = Arrays.copyOf(a, a.length);
    clobber(copy);
    return copy;
  }

  /**
   * Apply this operation to produce a new list. This method does not modify the input.
   * @param a an list of size not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.size() < this.length()}
   */
  public <E> List<E> apply(List<E> a) {
    ArrayList<E> copy = new ArrayList<E>(a.size());
    for (int i = 0; i < a.size(); i++)
      copy.set(i, a.get(apply(i)));
    return copy;
  }

  /**
   * Move an index.
   * @param i a non-negative number
   * @return the moved index
   * @throws IllegalArgumentException if {@code i} is negative
   */
  public int apply(int i) {
    if (i < 0)
      negativeFailure();
    return i == j ? k : i == k ? j : i;
  }

  public boolean commutesWith(Transposition other) {
    return this.j != other.j
        && this.k != other.k
        || this.j == other.j
        && this.k == other.k;
  }

  /**
   * Return the minimum number of elements that an array or list must have, in order for this operation to
   * be applicable.
   * @return the length of this operation
   */
  public int length() {
    return j + 1;
  }

  /**
   * Return the first index to be swapped.
   * This is always greater than the second.
   * @return a non-negative number
   * @see #second()
   */
  public int first() {
    return j;
  }

  /**
   * Return the second index to be swapped.
   * @return a non-negative number
   */
  public int second() {
    return k;
  }

  /**
   * Get a permutation version of this operation.
   * @return a permutation
   */
  public Permutation toPermutation() {
    return Permutation.cycle(j, k);
  }

  public String toString() {
    return String.format("(%d %d)", j, k);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transposition that = (Transposition) o;
    return (j == that.j && k == that.k);
  }

  @Override
  public int hashCode() {
    int result = j;
    result = 31 * result + k;
    return result;
  }

}
