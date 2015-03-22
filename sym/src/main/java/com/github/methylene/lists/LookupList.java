package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.invert;
import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Rankings;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

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
  public static LookupList<Integer> asList(int... a) {
    return IntList.createNewList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Long> asList(long... a) {
    return new LongList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Byte> asList(byte... a) {
    return new ByteList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Character> asList(char... a) {
    return new CharList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Float> asList(float... a) {
    return new FloatList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Double> asList(double... a) {
    return new DoubleList(a, Permutation.sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list representation of the input
   */
  public static LookupList<Short> asList(short... a) {
    return new ShortList(a, Permutation.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a list representation of the input
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  @SafeVarargs
  public static <E extends Comparable> LookupList<E> asList(E... a) {
    return new ComparableList<E>(a, Permutation.sort(a));
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
   * @param list a list
   * @return a LookupList
   */
  public static <E extends Comparable> LookupList<E> copyOf(Iterable<E> list) {
    ComparableList.Builder<E> builder = new ComparableList.Builder<E>();
    builder.addAll(list);
    return builder.build();
  }

  /**
   * Creates a list from the given input.
   * @param comparator a comparator
   * @param a an array
   * @return a list representation of the input
   * @throws java.lang.NullPointerException if the input contains a {@code null} element
   */
  @SafeVarargs
  public static <E> LookupList<E> asList(Comparator<E> comparator, E... a) {
    if (comparator == null)
      throw new IllegalArgumentException("comparator can not be null");
    return new ComparatorList<E>(a, comparator, Permutation.sort(a, comparator));
  }

  /**
   * Create a list builder.
   * @param comparator a comparator
   * @return a new builder
   */
  public static <E> ListBuilder<E> builder(Comparator<E> comparator) {
    return new ComparatorList.Builder<E>(comparator);
  }

  public LookupList<E> rearrange(Permutation p) {
    //TODO
    return this;
  }

  /**
   * Create a list builder.
   * @return a new builder
   */
  public static <E extends Comparable> ListBuilder<E> builder() {
    return new ComparableList.Builder<E>();
  }

}
