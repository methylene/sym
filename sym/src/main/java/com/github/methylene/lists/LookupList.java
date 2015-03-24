package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.invert;
import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Rankings;

import java.util.*;

/**
 * <p>LookupLists are immutable, null-rejecting, array based implementation of {@link java.util.List}
 * that are optimized for search. Its {@code indexOf}, {@code lastIndexOf} and {@code contains}
 * methods will often run faster than other array based list implementations.</p>
 *
 * <p>LookupList stores a sorted array internally, so binary search can be used.</p>
 *
 * <p>
 * Consequentially, the list can only contain things that can be compared:
 * primitives or Comparables. Building a list from arbitrary objects is also possible if a suitable Comparator
 * is provided.</p>
 *
 * <p>Instances of this list are slower to create and use more memory than {@link java.util.ArrayList}.
 * This is because the backing array has to be sorted when the list is created, and for each element in the list,
 * an extra 8 bytes (two ints) are used to store its original position.</p>
 *
 * <p>The speedup of the search methods depends on the size of the list, and also on the cost of the
 * {@code equals} method of its elements.</p>
 */
public abstract class LookupList<E> extends AbstractList<E> implements RandomAccess {

  static final int[] EMPTY_INT_ARRAY = new int[0];

  protected final Permutation unsort;
  protected final Permutation sort;

  static int checkNonnegative(int i) {
    if (i < 0)
      throw new IllegalArgumentException("negative number is not allowed");
    return i;
  }

  protected LookupList(Permutation sort, Permutation unsort) {
    this.sort = sort;
    this.unsort = unsort;
  }

  protected LookupList(Permutation sort) {
    this(sort, sort.invert());
  }

  /**
   * Return a new list that contains the elements of this list in natural order.
   * @return the sorted list
   */
  public abstract List<E> sort();


  /**
   * Returns a new list that contains the elements of this list in natural order,
   * with duplicates removed.
   * @return the unique sorted list
   */
  public abstract List<E> sortUnique();


  /**
   * Group the elements in this list.
   * This returns a map where each distinct value in the list is mapped to an array of all the indexes
   * where it appears. This array of indexes is in ascending order.
   * @return the grouped list
   */
  public abstract Map<E, int[]> group();

  public abstract LookupList<E> shuffle(Permutation p);

  public abstract boolean isUnique();

  public abstract boolean isSorted();


  /**
   * Find at most {@code size} indexes {@code i} where
   * <pre><code>
   *   this.get(i).equals(el)
   * </code></pre>
   * <p>The return value is in natural order.</p>
   * <p>This method could be used to find duplicates in the list as follows:</p>
   * <pre><code>
   *   public <E> E findDuplicate(LookupList<E> list) {
   *     for (E el: list) {
   *       if (list.indexOf(el, 2).length == 2) {
   *         return el;
   *       }
   *     }
   *     return null;
   *   }
   * </code></pre>
   * If {@code size < 0}, all indexes of {@code el} are returned.
   * If {@code el} is not in the list, this returns an empty array.
   * @param el an object
   * @param size a number
   * @return an array of length {@code size} or less, or an array of all indexes of {@code el} if {@code size < 0}
   */
  public abstract int[] indexOf(E el, int size);

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Integer> of(int... a) {
    return IntList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Long> of(long... a) {
    return LongList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Byte> of(byte... a) {
    return ByteList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Character> of(char... a) {
    return CharList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Float> of(float... a) {
    return FloatList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Double> of(double... a) {
    return DoubleList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Short> of(short... a) {
    return ShortList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Returns the empty list.
   * @return the empty list
   */
  @SuppressWarnings("unchecked")
  public static <E> LookupList<E> of() {
    return (LookupList<E>) EmptyLookupList.EMPTY;
  }

  /**
   * Creates a list from the given input.
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0) {
    return new ComparableList.Builder<E>(1).add(e0).build();
  }

  /**
   * Creates a list from the given input.

   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1) {
    return new ComparableList.Builder<E>(2).add(e0).add(e1).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2) {
    return new ComparableList.Builder<E>(3).add(e0).add(e1).add(e2).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3) {
    return new ComparableList.Builder<E>(4).add(e0).add(e1).add(e2).add(e3).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4) {
    return new ComparableList.Builder<E>(5).add(e0).add(e1).add(e2).add(e3).add(e4).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5) {
    return new ComparableList.Builder<E>(6).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5, E e6) {
    return new ComparableList.Builder<E>(7).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    return new ComparableList.Builder<E>(8).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(e7).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    return new ComparableList.Builder<E>(9).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(e7).add(e8).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
    return new ComparableList.Builder<E>(10).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(e7).add(e8).add(e9).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
    return new ComparableList.Builder<E>(11).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(e7).add(e8).add(e9).add(e10).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> of(E e0, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E... es) {
    return new ComparableList.Builder<E>(11 + es.length).add(e0).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(e7).add(e8).add(e9).add(e10).addAll(es).build();
  }

  /**
   * Creates a list from the given input.
   *
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E extends Comparable> LookupList<E> copyOf(E[] a) {
    return ComparableList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Create a new list from the given input. If the input is already an instance of LookupList, it is returned unchanged.
   * @param list a list
   * @return a LookupList
   */
  public static <E extends Comparable> LookupList<E> copyOf(Collection<E> list) {
    if (list instanceof LookupList)
      return (LookupList<E>) list;
    ComparableList.Builder<E> builder = new ComparableList.Builder<E>(list.size());
    builder.addAll(list);
    return builder.build();
  }

  /**
   * Create a new list from the given input.
   * @param iterable a iterable
   * @return a LookupList
   */
  public static <E extends Comparable> LookupList<E> copyOf(Iterable<E> iterable) {
    ComparableList.Builder<E> builder = new ComparableList.Builder<E>();
    builder.addAll(iterable);
    return builder.build();
  }

  /**
   * Create a new list from the given input.
   * @param iterator a iterator
   * @return a LookupList
   */
  public static <E extends Comparable> LookupList<E> copyOf(Iterator<E> iterator) {
    ComparableList.Builder<E> builder = new ComparableList.Builder<E>();
    while (iterator.hasNext())
      builder.add(iterator.next());
    return builder.build();
  }

  /**
   * Creates a list from the given input.
   * @param comparator a comparator
   * @param a an array
   * @return a list representation of the input
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  public static <E> LookupList<E> copyOf(Comparator<E> comparator, Object[] a) {
    if (comparator == null)
      throw new IllegalArgumentException("comparator can not be null");
    return ComparatorList.createNewList(comparator, a, Permutation.sort(a, comparator));
  }

  /**
   * Create a new list from the given input. If the input is already an instance of LookupList, it is returned unchanged.
   * @param comparator a comparator
   * @param list a list
   * @return a LookupList
   */
  public static <E> LookupList<E> copyOf(Comparator<E> comparator, Collection<E> list) {
    if (list instanceof LookupList)
      return (LookupList<E>) list;
    ComparatorList.Builder<E> builder = new ComparatorList.Builder<E>(comparator, list.size());
    builder.addAll(list);
    return builder.build();
  }

  /**
   * Create a new list from the given input.
   * @param comparator a comparator
   * @param iterable a iterable
   * @return a LookupList
   */
  public static <E> LookupList<E> copyOf(Comparator<E> comparator, Iterable<E> iterable) {
    ComparatorList.Builder<E> builder = new ComparatorList.Builder<E>(comparator);
    builder.addAll(iterable);
    return builder.build();
  }

  /**
   * Create a new list from the given input.
   * @param comparator a comparator
   * @param iterator a iterator
   * @return a LookupList
   */
  public static <E> LookupList<E> copyOf(Comparator<E> comparator, Iterator<E> iterator) {
    ComparatorList.Builder<E> builder = new ComparatorList.Builder<E>(comparator);
    while (iterator.hasNext())
      builder.add(iterator.next());
    return builder.build();
  }



  /**
   * Create a list builder.
   * @param comparator a comparator
   * @return a new builder
   */
  public static <E> ListBuilder<E> builder(Comparator<E> comparator) {
    return new ComparatorList.Builder<E>(comparator);
  }

  /**
   * Create a list builder.
   * @param comparator a comparator
   * @param initialCapacity initial builder capacity
   * @return a new builder
   */
  public static <E> ListBuilder<E> builder(Comparator<E> comparator, int initialCapacity) {
    return new ComparatorList.Builder<E>(comparator, initialCapacity);
  }

  /**
   * Create a list builder.
   * @return a new builder
   */
  public static <E extends Comparable> ListBuilder<E> builder() {
    return new ComparableList.Builder<E>();
  }

  /**
   * Create a list builder.
   * @param initialCapacity initial builder capacity
   * @return a new builder
   */
  public static <E extends Comparable> ListBuilder<E> builder(int initialCapacity) {
    return new ComparableList.Builder<E>(initialCapacity);
  }

}
