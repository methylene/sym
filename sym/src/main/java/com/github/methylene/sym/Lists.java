package com.github.methylene.sym;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * <p>This class contains immutable array based implementations of {@link java.util.List}
 * that have efficient {@code indexOf} and {@code contains} methods.</p>
 *
 * <p>Per default it is not possible to construct a list that contains {@code null} values.
 * This can be configured via the setNullPolicy switch.</p>
 *
 * <p>Notes:</p>
 * <ul>
 *   <li>The List implementations do not implement {@link java.util.List#subList}.</li>
 * </ul>
 */
public class Lists {

  private final PermutationFactory factory;

  /**
   * Creates a Searchable that uses the given PermutationFactory for sorting and unsorting.
   * @param permutationFactory used to sort the input arrays
   */
  public Lists(PermutationFactory permutationFactory) {
    this.factory = permutationFactory;
  }

  private static final Lists DEFAULT = new Lists(Permutation.factory());
  private static final Lists UNIQUE = new Lists(Permutation.duplicateRejectingFactory());
  private static final Lists ALLOW_NULL = new Lists(PermutationFactory.builder()
      .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL).build());

  /**
   * Obtain the uniqueness enforcing variant of Searchable. This will refuse to create lists that
   * contain duplicates, and throw an IllegalArgumentException instead.
   * @return a strict searchable
   */
  public static Lists uniqueLists() {
    return UNIQUE;
  }

  public static Lists allowNull() {
    return ALLOW_NULL;
  }

  public abstract class LookupList<E> extends AbstractList<E> implements RandomAccess {

    protected final Permutation unsort;

    protected LookupList(Permutation unsort) {
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
   * Creates a list from the given input.
   * @param a an array
   * @return a list
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public static IntList asList(int... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public static LongList asList(long... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ByteList asList(byte... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static CharList asList(char... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static FloatList asList(float... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static DoubleList asList(double... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   */
  public static ShortList asList(short... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input, using default settings (forbid null, allow duplicates)
   * @param a an array
   * @return a searchable version of the input
   */
  public static ComparableList asList(Comparable... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input, using default settings (forbid null, allow duplicates)
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   */
  public static ComparatorList asList(Comparator comparator, Object... a) {
    return DEFAULT.newList(comparator, a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public IntList newList(int[] a) {
    Permutation sort = factory.sort(a);
    return new IntList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public LongList newList(long[] a) {
    Permutation sort = factory.sort(a);
    return new LongList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public ByteList newList(byte[] a) {
    Permutation sort = factory.sort(a);
    return new ByteList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public CharList newList(char[] a) {
    Permutation sort = factory.sort(a);
    return new CharList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public FloatList newList(float[] a) {
    Permutation sort = factory.sort(a);
    return new FloatList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public DoubleList newList(double[] a) {
    Permutation sort = factory.sort(a);
    return new DoubleList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public ShortList newList(short[] a) {
    Permutation sort = factory.sort(a);
    return new ShortList(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public <E extends Comparable> ComparableList<E> newList(E[] a) {
    Permutation sort = factory.sort(a);
    return new ComparableList<E>(a, sort.apply(a), sort.invert());
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory is strict and the input contains duplicates
   * @see Lists#getPermutationFactory
   */
  public <E> ComparatorList<E> newList(Comparator<E> comparator, E[] a) {
    Permutation sort = factory.sort(a, comparator);
    return new ComparatorList<E>(a, sort.apply(a), comparator, sort.invert());
  }

  public <E> ListBuilder builder(Comparator<E> comparator) {
    return new ComparatorBuilder<E>(comparator);
  }

  public <E extends Comparable> ListBuilder builder() {
    return new ComparableBuilder<E>();
  }


  public final class ByteList extends LookupList<Byte> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Byte) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      byte b = (Byte) el;
      int start = Arrays.binarySearch(sorted, b);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == b ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == b) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

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


  public final class LongList extends LookupList<Long> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Long) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      long n = (Long) el;
      int start = Arrays.binarySearch(sorted, n);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

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


  public final class CharList extends LookupList<Character> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Character) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      char c = (Character) el;
      int start = Arrays.binarySearch(sorted, c);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == c ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == c) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

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


  public final class IntList extends LookupList<Integer> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Integer) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      int n = (Integer) el;
      int start = Arrays.binarySearch(sorted, n);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

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


  public final class FloatList extends LookupList<Float> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Float) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      float f = (Float) el;
      int start = Arrays.binarySearch(sorted, f);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == f ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == f) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }


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


  public final class DoubleList extends LookupList<Double> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Double) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      double d = (Double) el;
      int start = Arrays.binarySearch(sorted, d);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == d ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == d) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }


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


  public final class ShortList extends LookupList<Short> {
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

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Short) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      short n = (Short) el;
      int start = Arrays.binarySearch(sorted, n);
      if (start == -1) {return -1;}
      int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

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


  public final class ComparableList<E extends Comparable> extends LookupList<E> {
    private final Comparable[] original;
    private final Comparable[] sorted;

    private ComparableList(Comparable[] original, Comparable[] sorted, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public Comparable[] getArray() {
      return Arrays.copyOf(original, original.length);
    }

    /**
     * Get a sorted copy of the original array.
     * @return a sorted copy of the original array
     */
    public Comparable[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      Comparable comparable = (Comparable) el;
      int start = Arrays.binarySearch(sorted, comparable);
      if (start == -1) {return -1;}
      int direction = start > 0 && Objects.equals(sorted[start - 1], comparable) ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], comparable)) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public E get(int i) {
      return (E) original[i];
    }

    @Override
    public int size() {
      return original.length;
    }

  }

  public final class ComparatorList<E> extends LookupList<E> {
    private final Object[] original;
    private final Object[] sorted;
    private final Comparator<E> comparator;

    private ComparatorList(Object[] original, Object[] sorted, Comparator<E> comparator, Permutation unsort) {
      super(unsort);
      this.original = original;
      this.sorted = sorted;
      this.comparator = comparator;
    }

    /**
     * Get a copy of the original array.
     * @return a copy of the original array
     */
    public Object[] getArray() {
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
     * Get the comparator used by this instance.
     * @return the comparator
     */
    public Comparator<E> getComparator() {
      return comparator;
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el, (Comparator) comparator);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      int start = Arrays.binarySearch(sorted, el, (Comparator) comparator);
      if (start == -1) {return -1;}
      int direction = start > 0 && Objects.equals(sorted[start - 1], el) ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], el)) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public E get(int i) {
      return (E) original[i];
    }

    @Override
    public int size() {
      return original.length;
    }

  }

  abstract class ListBuilder<E> {
    protected final ArrayList<E> arrayList = new ArrayList<E>();

    public final ListBuilder<E> add(E el) {
      arrayList.add(el);
      return this;
    }

    public ListBuilder<E> addAll(E... els) {
      Collections.addAll(arrayList, els);
      return this;
    }

    public ListBuilder<E> addAll(Iterable<E> els) {
      for (E el : els)
        arrayList.add(el);
      return this;
    }
  }

  public final class ComparableBuilder<E extends Comparable> extends ListBuilder<E> {

    public ComparableList<E> build() {
      Comparable[] a = arrayList.toArray(new Comparable[arrayList.size()]);
      Permutation sort = factory.sort(a);
      return new ComparableList<E>(a, sort.apply(a), sort.invert());
    }

  }


  public final class ComparatorBuilder<E> extends ListBuilder<E> {
    private final Comparator<E> comparator;

    private ComparatorBuilder(Comparator<E> comparator) {this.comparator = comparator;}

    public ComparatorList<E> build() {
      Object[] a = arrayList.toArray(new Object[arrayList.size()]);
      Permutation sort = factory.sort(a, comparator);
      return new ComparatorList<E>(a, sort.apply(a), comparator, sort.invert());
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
