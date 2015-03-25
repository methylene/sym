package com.github.methylene.sym;

import static com.github.methylene.sym.Util.checkLength;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An operation that shuffles arrays and lists.

 * This implementation can be faster and use less memory than an equivalent
 * {@link Permutation}, especially if there are few transpositions but their maximum <i>length</i> is large.
 *
 * The clobber method can be used to modify arrays and list &quot;in place&quot;.
 */
public final class CompiledPermutation {

  private static final CompiledPermutation IDENTITY = new CompiledPermutation(new Transposition[0]);

  private final Transposition[] transpositions;
  private final int length;

  private CompiledPermutation(Transposition[] transpositions) {
    int length = 0;
    for (Transposition t : transpositions)
      length = Math.max(length, t.length());
    this.transpositions = transpositions;
    this.length = length;
  }

  /**
   * Get the identity permutation.
   * @return the identity permutation
   */
  public static CompiledPermutation identity() {
    return IDENTITY;
  }

  /**
   * Define a new operation as a list of transpositions.
   * @param transpositions a list of transpositions
   * @return the operation defined by the input
   */
  public static CompiledPermutation create(List<Transposition> transpositions) {
    if (transpositions.isEmpty())
      return IDENTITY;
    return new CompiledPermutation(transpositions.toArray(new Transposition[transpositions.size()]));
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(int[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      int temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(byte[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      byte temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(char[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      char temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(short[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      short temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(float[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      float temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(double[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      double temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(long[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      long temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(Object[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--) {
      Object temp = array[transpositions[i].k];
      array[transpositions[i].k] = array[transpositions[i].j];
      array[transpositions[i].j] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input list.
   * The input list must support {@link List#set(int, Object)}.
   * @param list a list
   * @throws UnsupportedOperationException if the input list is not mutable
   * @throws IllegalArgumentException if {@code list.size() < this.length()}
   */
  public <E> void clobber(List<E> list) {
    checkLength(length, list.size());
    for (int i = transpositions.length - 1; i != -1; i--) {
      E temp = list.get(transpositions[i].k);
      list.set(transpositions[i].k, list.get(transpositions[i].j));
      list.set(transpositions[i].j, temp);
    }
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
   * @param a a list of size not less than {@code this.length()}
   * @return the result of applying this permutation to {@code a}
   * @throws java.lang.IllegalArgumentException if {@code a.size() < this.length()}
   */
  public <E> List<E> apply(List<E> a) {
    ArrayList<E> copy = new ArrayList<E>(a);
    clobber(copy);
    return copy;
  }

  /**
   * Move an index.
   * @param j a non-negative number
   * @return the moved index
   * @throws java.lang.IllegalArgumentException if {@code j < 0}
   */
  public int apply(int j) {
    for (int i = transpositions.length - 1; i != -1; i--)
      j = transpositions[i].apply(j);
    return j;
  }

  /**
   * Composing with another permutation creates a new operation.
   * @param other another permutation
   * @return the composition or product
   */
  public CompiledPermutation comp(CompiledPermutation other) {
    if (transpositions.length == 0)
      return other;
    if (other.transpositions.length == 0)
      return this;
    Transposition[] t = Arrays.copyOf(transpositions, transpositions.length + other.transpositions.length);
    System.arraycopy(other.transpositions, 0, t, transpositions.length, other.transpositions.length);
    return new CompiledPermutation(t);
  }

  /**
   * Take the product of the input operations, in order.
   * @param permutations an array of permutations
   * @return the composition or product
   */
  public static CompiledPermutation prod(CompiledPermutation... permutations) {
    CompiledPermutation result = IDENTITY;
    for (CompiledPermutation permutation : permutations)
      result = result.comp(permutation);
    return result;
  }

  /**
   * Take the product of the input operations, in order.
   * @param permutations a list of permutations
   * @return the composition or product
   */
  public static CompiledPermutation prod(List<CompiledPermutation> permutations) {
    CompiledPermutation result = IDENTITY;
    for (CompiledPermutation permutation : permutations)
      result = result.comp(permutation);
    return result;
  }

  /**
   * Get the transpositions that define this operation.
   * @return a copy of the array of transpositions
   */
  public Transposition[] getTranspositions() {
    return Arrays.copyOf(transpositions, transpositions.length);
  }

  /**
   * Uncompile this operation.
   * @return a ranking-based version of this operation
   */
  public Permutation toPermutation() {
    Permutation p = Permutation.identity();
    for (Transposition t : transpositions)
      p = p.comp(t.toPermutation());
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
