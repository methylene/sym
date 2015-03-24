package com.github.methylene.sym;

import java.util.Arrays;
import java.util.List;

/**
 * A transposition that modifies the things it is applied to.
 */
final class DestructiveTransposition {

  private final int j;
  private final int k;

  static final int CACHE_SIZE = 10;
  private static final DestructiveTransposition[][] CACHE = new DestructiveTransposition[CACHE_SIZE][];

  static {
    for (int j = 0; j < CACHE_SIZE; j++) {
      CACHE[j] = new DestructiveTransposition[CACHE_SIZE];
      for (int k = 0; k < CACHE_SIZE; k++)
        if (j != k) {CACHE[j][k] = new DestructiveTransposition(j, k);}
    }
  }

  private DestructiveTransposition(int j, int k) {
    this.j = j;
    this.k = k;
  }

  static DestructiveTransposition create(int j, int k) {
    if (j < 0 || k < 0)
      Util.negativeFailure();
    if (j == k)
      throw new IllegalArgumentException("arguments must not be equal");
    if (j < CACHE_SIZE && k < CACHE_SIZE)
      return CACHE[j][k];
    return new DestructiveTransposition(j, k);
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(int[] array) {
    int temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(byte[] array) {
    byte temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(char[] array) {
    char temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(short[] array) {
    short temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(float[] array) {
    float temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(double[] array) {
    double temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(long[] array) {
    long temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * @param array an array
   */
  void apply(Object[] array) {
    Object temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input list.
   * The input list must support {@link java.util.List#set(int, Object)}.
   * @param list an array
   * @throws java.lang.UnsupportedOperationException if the input list is not mutable
   */
  @SuppressWarnings("unchecked")
  void apply(List<?> list) {
    Object temp = list.get(k);
    ((List) list).set(k, list.get(j));
    ((List) list).set(j, temp);
  }

  int length() {
    return Math.max(j, k);
  }

  int getFirst() {
    return j;
  }

  int getSecond() {
    return k;
  }

  Permutation toPermutation() {
    return Permutation.swap(j, k);
  }

  public String toString() {
    return String.format("(%d %d)", j, k);
  }

}
