package com.github.methylene.sym;

import static com.github.methylene.sym.Util.checkLength;
import static com.github.methylene.sym.Util.negativeFailure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *   An operation that shuffles arrays and lists.
 * </p>
 *
 * <p>
 *   This implementation is based on cyclic decomposition and will often use less memory than an equivalent
 *   {@link Permutation}. In some cases it can be faster when applied to a list or array, and it can optionally
 *   be applied in a destructive manner, see the {@code clobber} and {@code unclobber} methods.
 * </p>
 *
 * <p>
 *   Applying it to a single index is {@code O(n)} whereas {@link Permutation} does this in constant time.
 *   For this reason, {@link com.github.methylene.lists.LookupList} does not use this implementation internally.
 * </p>
 */
public final class CompiledPermutation {

  private static final CompiledPermutation IDENTITY = new CompiledPermutation(new Transposition[0], Permutation.Orbits.EMPTY);

  //  private final Transposition[] transpositions;
  private final int[] transp;
  private final int length;
  private final int[][] cycles;

  private CompiledPermutation(Transposition[] transpositions, Permutation.Orbits orbits) {
    int length = 0;
    this.transp = new int[transpositions.length * 2];
    for (int i = 0; i < transpositions.length; i++) {
      Transposition t = transpositions[i];
      length = Math.max(length, t.j);
      transp[2 * i] = t.j;
      transp[2 * i + 1] = t.k;
    }
    this.cycles = orbits.orbits;
    this.length = length + 1;
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
  public static CompiledPermutation create(List<Transposition> transpositions, Permutation.Orbits orbits) {
    if (transpositions.isEmpty())
      return IDENTITY;
    return new CompiledPermutation(transpositions.toArray(new Transposition[transpositions.size()]), orbits);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(int[] array) {
    checkLength(length, array.length);
    for (int i = 0; i < cycles.length; i++) {
      for (int j = cycles[i].length - 2; j >= 0; j--) {
        int temp = array[cycles[i][j + 1]];
        array[cycles[i][j + 1]] = array[cycles[i][j]];
        array[cycles[i][j]] = temp;
      }
    }
  }

  public void unclobber(int[] array) {
    checkLength(length, array.length);
    for (int i = 0; i < transp.length; i += 2) {
      int temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(byte[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      byte temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(char[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      char temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(short[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      short temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(float[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      float temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(double[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      double temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(long[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      long temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
    }
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   * @throws IllegalArgumentException if {@code array.length < this.length()}
   */
  public void clobber(Object[] array) {
    checkLength(length, array.length);
    for (int i = transp.length - 2; i != -2; i -= 2) {
      Object temp = array[transp[i + 1]];
      array[transp[i + 1]] = array[transp[i]];
      array[transp[i]] = temp;
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
    for (int i = transp.length - 2; i != -2; i -= 2) {
      E temp = list.get(transp[i + 1]);
      list.set(transp[i + 1], list.get(transp[i]));
      list.set(transp[i], temp);
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
   * Move an index. This method will not fail if the input is negative, but just return it unchanged.
   * @param i a number
   * @return the moved index
   */
  public int apply(int i) {
    for (int n = transp.length - 2; n != -2; n -= 2)
      i = i == transp[n] ? transp[n + 1] : i == transp[n + 1] ? transp[n] : i;
    return i;
  }

  /**
   * Uncompile this operation.
   * @return a ranking-based version of this operation
   */
  public Permutation toPermutation() {
    int[] ranking = Util.sequence(length);
    unclobber(ranking);
    return Permutation.perm(ranking);
  }

  /**
   * Composing with another permutation creates a new operation.
   * @param other another permutation
   * @return the composition or product
   */
  public Permutation comp(CompiledPermutation other) {
    if (transp.length == 0)
      return other.toPermutation();
    if (other.transp.length == 0)
      return this.toPermutation();
    return this.toPermutation().comp(other.toPermutation());
  }

  /**
   * Take the product of the input operations, in order.
   * @param permutations an array of permutations
   * @return the composition or product
   */
  public static Permutation prod(CompiledPermutation... permutations) {
    Permutation result = Permutation.identity();
    for (CompiledPermutation permutation : permutations)
      result = result.comp(permutation.toPermutation());
    return result;
  }

  /**
   * Take the product of the input operations, in order.
   * @param permutations a list of permutations
   * @return the composition or product
   */
  public static Permutation prod(List<CompiledPermutation> permutations) {
    Permutation result = Permutation.identity();
    for (CompiledPermutation permutation : permutations)
      result = result.comp(permutation.toPermutation());
    return result;
  }

  @Override
  public String toString() {
    if (transp.length == 0)
      return "[]";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < transp.length; i += 2)
      sb.append(String.format(" (%d %d)", transp[i], transp[i + 1]));
    return '[' + sb.substring(1) + ']';
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
