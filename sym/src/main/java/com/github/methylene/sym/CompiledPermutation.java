package com.github.methylene.sym;

import static com.github.methylene.sym.Util.checkLength;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A list of transpositions.
 */
public final class CompiledPermutation {

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
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(int[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(byte[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(char[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(short[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(float[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(double[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(long[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(Object[] array) {
    checkLength(length, array.length);
    for (int i = transpositions.length - 1; i != -1; i--)
      transpositions[i].clobber(array);
  }

  /**
   * Apply this operation by modifying the input list.
   * The input list must support {@link List#set(int, Object)}.
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   * @param list an array
   * @throws UnsupportedOperationException if the input list is not mutable
   * @throws IllegalArgumentException if {@code list.size() < this.length()}
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
    ArrayList<E> copy = new ArrayList<E>(a);
    clobber(copy);
    return copy;
  }

  public int apply(int j) {
    for (int i = transpositions.length - 1; i != -1; i--)
      j = transpositions[i].apply(j);
    return j;
  }

  public CompiledPermutation comp(CompiledPermutation other) {
    if (transpositions.length == 0)
      return other;
    if (other.transpositions.length == 0)
      return this;
    Transposition[] t = Arrays.copyOf(transpositions, transpositions.length + other.transpositions.length);
    System.arraycopy(other.transpositions, 0 , t, transpositions.length, other.transpositions.length);
    return new CompiledPermutation(t);
  }

  public static CompiledPermutation prod(CompiledPermutation... permutations) {
    CompiledPermutation result = IDENTITY;
    for (CompiledPermutation permutation : permutations)
      result = result.comp(permutation);
    return result;
  }

  public static CompiledPermutation prod(List<CompiledPermutation> permutations) {
    CompiledPermutation result = IDENTITY;
    for (CompiledPermutation permutation : permutations)
      result = result.comp(permutation);
    return result;
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

  public boolean isIdentity() {
    return transpositions.length == 0;
  }

}
