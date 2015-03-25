package com.github.methylene.sym;

import java.util.List;

/**
 * A transposition that modifies the things it is applied to.
 */
public final class Transposition {

  private final int j;
  private final int k;

  public static final class Factory {

    private final Transposition[][] cache;

    public Factory(int cacheSize) {
      this.cache = new Transposition[cacheSize][];
      for (int j = 1; j < cacheSize; j++)
        cache[j] = new Transposition[j];
    }

    /**
     * Get a transposition operation that swaps the element at the given indexes.
     * @param j a non-negative number
     * @param k a non-negative number that must not be the same as {@code j}
     * @return a transposition operation
     * @throws java.lang.IllegalArgumentException if the arguments are equal or negative
     */
    public Transposition create(int j, int k) {
      if (j < 0 || k < 0)
        Util.negativeFailure();
      if (j == k)
        throw new IllegalArgumentException("arguments must not be equal");
      if (k > j) {
        // keep cache small: make sure that k is less than j
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

  private Transposition(int j, int k) {
    this.j = j;
    this.k = k;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not validate the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(int[] array) {
    int temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(byte[] array) {
    byte temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(char[] array) {
    char temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(short[] array) {
    short temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(float[] array) {
    float temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(double[] array) {
    double temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(long[] array) {
    long temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input array.
   * This method does not check the length condition {@code array.length >= this.length()},
   * an ArrayIndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param array an array
   * @throws java.lang.ArrayIndexOutOfBoundsException if {@code array.length < this.length()}
   */
  public void clobber(Object[] array) {
    Object temp = array[k];
    array[k] = array[j];
    array[j] = temp;
  }

  /**
   * Apply this operation by modifying the input list.
   * The input list must support {@link java.util.List#set(int, Object)}.
   * This method does not check the length condition {@code list.size() >= this.length()},
   * an IndexOutOfBoundsException will be the consequence if it is not satisfied.
   * @param list an array
   * @throws java.lang.UnsupportedOperationException if the input list is not mutable
   * @throws java.lang.IndexOutOfBoundsException if {@code list.size() < this.length()}
   */
  @SuppressWarnings("unchecked")
  public void clobber(List<?> list) {
    Object temp = list.get(k);
    ((List) list).set(k, list.get(j));
    ((List) list).set(j, temp);
  }

  /**
   * Return the minimum number of elements that an array or list must have, in order for this operation to
   * be applicable.
   * @return the length of this operation
   */
  public int length() {
    return Math.max(j, k);
  }

  /**
   * Return the first index to be swapped.
   * @return a non-negative number
   */
  public int getFirst() {
    return j;
  }

  /**
   * Return the second index to be swapped.
   * @return a non-negative number
   */
  public int getSecond() {
    return k;
  }

  /**
   * Get a nondestructive version of this operation.
   * @return a nondestructive version of this operation
   */
  public Permutation toPermutation() {
    return Permutation.swap(j, k);
  }

  public String toString() {
    return String.format("(%d %d)", j, k);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transposition that = (Transposition) o;
    return  (j == that.j && k == that.k);
  }

  @Override
  public int hashCode() {
    int result = j;
    result = 31 * result + k;
    return result;
  }

}
