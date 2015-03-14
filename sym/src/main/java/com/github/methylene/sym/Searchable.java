package com.github.methylene.sym;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * <p>This class contains immutable array based implementations of {@link java.util.List}
 * that have efficient {@code indexOf} and {@code contains} methods.</p>
 *
 * <p>Per default it is not possible to construct a list that contains {@code null} values.
 * This can be configured via the setNullPolicy switch.</p>
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

  private static final Searchable DEFAULT = new Searchable(Permutation.factory());
  private static final Searchable UNIQUE = new Searchable(Permutation.duplicateRejectingFactory());
  private static final Searchable ALLOW_NULL = new Searchable(PermutationFactory.builder()
      .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL).build());

  /**
   * Obtain the uniqueness enforcing variant of Searchable. This will refuse to create lists that
   * contain duplicates, and throw an IllegalArgumentException instead.
   * @return a strict searchable
   */
  public static Searchable uniqueLists() {
    return UNIQUE;
  }

  public static Searchable allowNull() {
    return ALLOW_NULL;
  }

  /**
   * Common base class of the {@code *Array} variants.
   */
  public abstract class AArray<E> extends AbstractList<E> {

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

    /**
     * Get the uniqueness setting of this list.
     * If the uniqueness setting is
     * {@link com.github.methylene.sym.PermutationFactory.UniquenessConstraint#FORBID_DUPLICATES},
     * this list will not contain two elements that are equal, and can be used as a set.
     * @return the uniqueness constraint
     */
    public PermutationFactory.UniquenessConstraint getUniquenessConstraint() {
      return factory.getUniquenessConstraint();
    }

    /**
     * Get the null policy of this list.
     * If the null policy is {@link com.github.methylene.sym.PermutationFactory.NullPolicy#ALLERGIC},
     * this list will not contain any null values.
     * @return the null policy
     */
    public PermutationFactory.NullPolicy getNullPolicy() {
      return factory.getNullPolicy();
    }

  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public static IntList asList(int... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public static LongList asList(long... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ByteList asList(byte... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static CharList asList(char... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static FloatList asList(float... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static DoubleList asList(double... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ShortList asList(short... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input, using default settings (forbid null, allow duplicates)
   * @param a an array
   * @return a searchable version of the input
   */
  public static ComparableList asList(Comparable... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a searchable array from the given input, using default settings (forbid null, allow duplicates)
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   */
  public static ObjectList asList(Comparator comparator, Object... a) {
    return DEFAULT.newList(comparator, a);
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public IntList newList(int[] a) {
    Permutation sort = factory.sort(a);
    return new IntList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public LongList newList(long[] a) {
    Permutation sort = factory.sort(a);
    return new LongList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public ByteList newList(byte[] a) {
    Permutation sort = factory.sort(a);
    return new ByteList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public CharList newList(char[] a) {
    Permutation sort = factory.sort(a);
    return new CharList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public FloatList newList(float[] a) {
    Permutation sort = factory.sort(a);
    return new FloatList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public DoubleList newList(double[] a) {
    Permutation sort = factory.sort(a);
    return new DoubleList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public ShortList newList(short[] a) {
    Permutation sort = factory.sort(a);
    return new ShortList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public <E extends Comparable> ComparableList<E> newList(E[] a) {
    Permutation sort = factory.sort(a);
    return new ComparableList<E>(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a searchable array from the given input.
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see com.github.methylene.sym.Searchable#getPermutationFactory
   */
  public <E> ObjectList<E> newList(Comparator<E> comparator, E[] a) {
    Permutation sort = factory.sort(a, comparator);
    return new ObjectList<E>(a, sort.apply(a), comparator, sort.invert());
  }

  /**
   * A searchable array.
   */
  public final class ByteList extends AArray<Byte> {
    private final byte[] original;
    private final byte[] sorted;

    private ByteList(byte[] original, byte[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public byte[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @throws java.lang.ClassCastException if the argument is not a Byte
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Byte) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a byte
     * @return true if {@code el} is contained in the array
     * @throws java.lang.ClassCastException if the argument is not a Byte
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Byte get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }
  }

  /**
   * A searchable array.
   */
  public final class LongList extends AArray<Long> {
    private final long[] original;
    private final long[] sorted;

    private LongList(long[] original, long[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public long[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @throws java.lang.ClassCastException if the argument is not a Long
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Long) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a long
     * @return true if {@code el} is contained in the array
     * @throws java.lang.ClassCastException if the argument is not a Long
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Long get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }
  }

  /**
   * A searchable array.
   */
  public final class CharList extends AArray<Character> {
    private final char[] original;
    private final char[] sorted;

    private CharList(char[] original, char[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public char[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @throws java.lang.ClassCastException if the argument is not a Character
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Character) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a char
     * @return true if {@code el} is contained in the array
     * @throws java.lang.ClassCastException if the argument is not a Character
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Character get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }
  }

  /**
   * A searchable array.
   */
  public final class IntList extends AArray<Integer> {
    private final int[] original;
    private final int[] sorted;

    private IntList(int[] original, int[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public int[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @param el an Integer
     * @return the lowest index {@code i} so that {@code a[i] == el}, or {@code -1}
     * @throws java.lang.ClassCastException if the argument is not an Integer
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Integer) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in this list.
     * @param el an Integer
     * @return true if {@code el} is contained in this list
     * @throws java.lang.ClassCastException if the argument is not an Integer
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Integer get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }

  }

  /**
   * A searchable array.
   */
  public final class FloatList extends AArray<Float> {
    private final float[] original;
    private final float[] sorted;

    private FloatList(float[] original, float[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public float[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @throws java.lang.ClassCastException if the argument is not a Float
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Float) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a float
     * @return true if {@code el} is contained in the array
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Float get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }
  }

  /**
   * A searchable array.
   */
  public final class DoubleList extends AArray<Double> {
    private final double[] original;
    private final double[] sorted;

    private DoubleList(double[] original, double[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public double[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @throws java.lang.ClassCastException if the argument is not a Double
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Double) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a double
     * @return true if {@code el} is contained in the array
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Double get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }
  }

  /**
   * A searchable array.
   */
  public final class ShortList extends AArray<Short> {
    private final short[] original;
    private final short[] sorted;

    private ShortList(short[] original, short[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public short[] getArray() {
      return Arrays.copyOf(original, original.length);
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
     * @throws java.lang.ClassCastException if the argument is not a Short
     */
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Short) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether the input is contained in the array.
     * @param el a short
     * @return true if {@code el} is contained in the array
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Short get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }

  }

  /**
   * A searchable array.
   */
  public final class ComparableList<E extends Comparable> extends AArray<E> {
    private final E[] original;
    private final Comparable[] sorted;

    private ComparableList(E[] original, Comparable[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public E[] getArray() {
      return Arrays.copyOf(original, original.length);
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
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether an element that's equal to the input is contained in the array.
     * @param el a comparable
     * @return true if {@code el} is contained in the array
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public E get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }

  }

  /**
   * A searchable array.
   */
  public final class ObjectList<E> extends AArray<E> {
    private final E[] original;
    private final Object[] sorted;
    private final Comparator comparator;

    private ObjectList(E[] original, Object[] sorted, Comparator comparator, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
      this.comparator = comparator;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public E[] getArray() {
      return Arrays.copyOf(original, original.length);
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
    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el, comparator);
      return i < 0 ? -1 : unsort.apply(i);
    }

    /**
     * Test whether an element that's equal to the input is contained in the array.
     * @param el a comparable
     * @return true if {@code el} is contained in the array
     */
    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public E get(int i) {
      return original[i];
    }

    @Override
    public int size() {
      return original.length;
    }

  }

  /**
   * Return the PermutationFactory that is used to sort and unsort all input arrays.
   * @return the permutation factory
   * @see com.github.methylene.sym.PermutationFactory#getUniquenessConstraint
   * @see com.github.methylene.sym.PermutationFactory#getParanoia
   */
  public PermutationFactory getPermutationFactory() {
    return factory;
  }

}
