package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.invert;
import static com.github.methylene.sym.Rankings.sort;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.Map;
import java.util.RandomAccess;

/**
 * <p>Base class for all List implementations in this package.</p>
 *
 * <p>LookupList is an immutable, null-rejecting, array based implementation of {@link java.util.List}
 * that is optimized for search. Its {@code indexOf}, {@code lastIndexOf} and {@code contains}
 * methods will often perform much better than those of other array based list implementations.</p>
 *
 * <p>LookupList implementations store a sorted array internally, so binary search can be used.</p>
 *
 * <p>
 * Consequentially, the list can only contain things that can be compared:
 * primitives or Comparables. Building a list from arbitrary objects is also possible if a suitable Comparator
 * is provided.</p>
 *
 * <p>Instances of this list are slower to create and use more memory than ArrayList.
 * For each element in the list, an extra 8 bytes (two ints) are needed to store its original position.</p>
 *
 * <p>The speedup of the search methods depends on the size of the list, and also on the cost of the
 * {@code equals} method of its elements.</p>
 */
public abstract class LookupList<E> extends AbstractList<E> implements RandomAccess {

  public static final int[] EMPTY_INT_ARRAY = new int[0];

  protected final int[] unsort;
  protected final int[] sort;

  protected LookupList(int[] sort) {
    this.sort = sort;
    this.unsort = invert(sort);
  }

  public abstract Map<E, int[]> getPartitions();

  /**
   * Find at most {@code size} indexes {@code i} where
   * <pre><code>
   *   this.get(i).equals(el)
   * </code></pre>
   * The returned array will always be sorted.
   * For example, this method could be used to find duplicates in the list as follows:
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
   * If {@code size < 0}, all indexes are returned.
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
  public static LookupList<Long> asList(long... a) {
    return new LongList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupList<Byte> asList(byte... a) {
    return new ByteList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupList<Character> asList(char... a) {
    return new CharList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupList<Float> asList(float... a) {
    return new FloatList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupList<Double> asList(double... a) {
    return new DoubleList(a, sort(a));
  }

  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupList<Short> asList(short... a) {
    return new ShortList(a, sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a list
   */
  public static <E extends Comparable> LookupList<E> asList(E... a) {
    return new ComparableList<E>(a, sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param comparator a comparator
   * @param a an array
   * @return a list
   */
  public static <E> LookupList<E> asList(Comparator<E> comparator, E... a) {
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
