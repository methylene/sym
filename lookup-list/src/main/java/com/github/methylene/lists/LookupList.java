package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.invert;
import static com.github.methylene.sym.Rankings.sort;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.RandomAccess;

/**
 * <p>Base class for immutable, null-rejecting, array based implementations of {@link java.util.List}
 * that have efficient search methods: {@code indexOf}, {@code lastIndexOf} and {@code contains}.</p>
 *
 * <p>These lists are similar to {@link java.util.ArrayList} in that they store an array internally,
 * but in this case, the array is sorted. This means that the list can only contain things that can be compared:
 * Primitives or Comparables. Building a list from arbitrary objects is only possible if a suitable Comparator
 * is also provided.</p>
 *
 * <p>As a rule of thumb, performance gains over the search methods of ArrayList are getting significant
 * as soon as the return value of {@code .indexOf} is larger than about 20, or if the list has more
 * than 20 elements and {@code indexOf} returns {@code -1}.</p>
 */
public abstract class LookupList<E> extends AbstractList<E> implements RandomAccess {

  protected final int[] emptyIntArray = new int[0];

  protected final int[] unsort;
  protected final int[] sort;

  protected LookupList(int[] sort) {
    this.sort = sort;
    this.unsort = invert(sort);
  }

  /**
   * Find at most {@code size} indexes {@code i} where
   * <pre><code>
   *   this.get(i).equals(el)
   * </code></pre>
   * The returned array will always be sorted.
   * If {@code size < 0}, all such indexes are found and returned.
   * If {@code el} is not in the list, this returns an empty array.
   * @param el an object
   * @param size a number
   * @return the array of all indexes where the value equals {@code el}
   */
  public abstract int[] indexOf(E el, int size);

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupList<Integer> asList(int... a) {
    return new IntList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LongList asList(long... a) {
    return new LongList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static ByteList asList(byte... a) {
    return new ByteList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static CharList asList(char... a) {
    return new CharList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static FloatList asList(float... a) {
    return new FloatList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static DoubleList asList(double... a) {
    return new DoubleList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static ShortList asList(short... a) {
    return new ShortList(a, sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a list
   */
  public static <E extends Comparable> ComparableList<E> asList(E... a) {
    return new ComparableList<E>(a, sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param comparator a comparator
   * @param a an array
   * @return a list
   */
  public static <E> ComparatorList<E> asList(Comparator<E> comparator, E... a) {
    if (comparator == null)
      throw new IllegalArgumentException("comparator can not be null");
    return new ComparatorList<E>(a, comparator, sort(a, comparator));
  }

  public static <E> ListBuilder<E> builder(Comparator<E> comparator) {
    return new ComparatorList.Builder<E>(comparator);
  }

  public static <E extends Comparable> ListBuilder<E> builder() {
    return new ComparableList.Builder<E>();
  }



}
