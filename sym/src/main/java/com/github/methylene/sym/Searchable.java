package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <p>The various {@code *Array} inner classes add efficient {@code indexOf} and {@code contains}
 * methods to standard arrays.</p>
 *
 * <p>The {@code indexOf} method is very similar to {@link java.lang.String#indexOf}.</p>
 */
public class Searchable {

  private final PermutationFactory factory;

  /**
   * Creates a Searchable that uses the given PermutationFactory for sorting and unsorting.
   * @param permutationFactory used to sort the input arrays
   */
  public Searchable(PermutationFactory permutationFactory) {
    this.factory = permutationFactory;
  }

  private static final Searchable STRICT = new Searchable(Permutation.strictFactory());
  private static final Searchable NON_STRICT = new Searchable(Permutation.factory());

  /**
   * Obtain the strict version of Searchable. This will refuse to create searchable versions of arrays that
   * contain duplicates, and throw an IllegalArgumentException instead.
   * @return a strict searchable
   */
  public static Searchable strictSearchable() {
    return STRICT;
  }

  /**
   * Obtain the default Searchable
   * @return the default searchable
   */
  public static Searchable searchable() {
    return NON_STRICT;
  }

  /**
   * Common base class of the {@code *Array} variants.
   */
  public static abstract class AArray {

    protected final Permutation unsort;

    protected AArray(Permutation unsort) {
      this.unsort = unsort;
    }

    /**
     * Get the inverse of a particular permutation which sorts the input array.
     * @return the unsort permutation
     */
    public final Permutation getUnsort() {
      return unsort;
    }

  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public static IntArray searchableArray(int[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public static LongArray searchableArray(long[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ByteArray searchableArray(byte[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static CharArray searchableArray(char[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static FloatArray searchableArray(float[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static DoubleArray searchableArray(double[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ShortArray searchableArray(short[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ComparableArray searchableArray(Comparable[] a) {
    return searchable().array(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   */
  public static ObjectArray searchableArray(Object[] a, Comparator comparator) {
    return searchable().array(a,comparator);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public IntArray array(int[] a) {
    Permutation sort = factory.sort(a);
    return new IntArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public LongArray array(long[] a) {
    Permutation sort = factory.sort(a);
    return new LongArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public ByteArray array(byte[] a) {
    Permutation sort = factory.sort(a);
    return new ByteArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public CharArray array(char[] a) {
    Permutation sort = factory.sort(a);
    return new CharArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public FloatArray array(float[] a) {
    Permutation sort = factory.sort(a);
    return new FloatArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public DoubleArray array(double[] a) {
    Permutation sort = factory.sort(a);
    return new DoubleArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public ShortArray array(short[] a) {
    Permutation sort = factory.sort(a);
    return new ShortArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public ComparableArray array(Comparable[] a) {
    Permutation sort = factory.sort(a);
    return new ComparableArray(sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public ObjectArray array(Object[] a, Comparator comparator) {
    Permutation sort = factory.sort(a, comparator);
    return new ObjectArray(sort.apply(a), comparator, sort.invert());
  }

  /**
   * A searchable array.
   */
  public static final class ByteArray extends AArray {
    private final byte[] sorted;

    private ByteArray(byte[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public byte[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public byte[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el a byte
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(byte el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a byte
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(byte el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class LongArray extends AArray {
    private final long[] sorted;

    private LongArray(long[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public long[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public long[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el a long
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(long el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a long
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(long el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class CharArray extends AArray {
    private final char[] sorted;

    private CharArray(char[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public char[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public char[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el a char
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(char el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a char
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(char el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class IntArray extends AArray {
    private final int[] sorted;

    private IntArray(int[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public int[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public int[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el an int
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(int el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el an int
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(int el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class FloatArray extends AArray {
    private final float[] sorted;

    private FloatArray(float[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public float[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public float[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el a float
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(float el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a float
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(float el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class DoubleArray extends AArray {
    private final double[] sorted;

    private DoubleArray(double[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public double[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public double[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el a double
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(double el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a double
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(double el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class ShortArray extends AArray {
    private final short[] sorted;

    private ShortArray(short[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public short[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public short[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value is {@code el}, or {@code -1} is no such index exists.
     * @param el a short
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     */
    public int indexOf(short el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a short
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(short el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class ComparableArray extends AArray {
    private final Comparable[] sorted;

    private ComparableArray(Comparable[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public Comparable[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public Comparable[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the lowest of the indexes in the array where the value equals {@code el}, or {@code -1} is no such index exists.
     * @param el a comparable
     * @return the lowest index {@code i} so that {@code a[i].equals(el)}, or {@code -1}
     */
    public int indexOf(Comparable el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether an element that's equal to the input is contained in the array.
     * @param el a comparable
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(Comparable el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * A searchable array.
   */
  public static final class ObjectArray extends AArray {
    private final Object[] sorted;
    private final Comparator comparator;

    private ObjectArray(Object[] sorted, Comparator comparator, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
      this.comparator = comparator;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public Object[] getArray() {
      return unsort.apply(sorted);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public Object[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    /**
     * Get the comparator that was used to construct this instance.
     * @return the original comparator
     */
    public Comparator getComparator() {
      return comparator;
    }

    /**
     * Get the lowest of the indexes in the array where the value equals {@code el}, or {@code -1} is no such index exists.
     * @param el an object
     * @return the lowest index {@code i} so that {@code a[i].equals(el)}, or {@code -1}
     */
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el, comparator);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether an element that's equal to the input is contained in the array.
     * @param el a comparable
     * @return true if {@code el} is contained in the array
     */
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }
  }

  /**
   * Return the PermutationFactory that is used to sort and unsort all input arrays.
   * @return the permutation factory
   * @see com.github.methylene.sym.PermutationFactory#getStrictness
   * @see com.github.methylene.sym.PermutationFactory#getParanoia
   */
  public PermutationFactory getPermutationFactory() {
    return factory;
  }

}
