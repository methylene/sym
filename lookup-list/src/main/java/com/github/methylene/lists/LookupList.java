package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.invert;
import static com.github.methylene.sym.Rankings.sort;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

/**
 * <p>Base class for immutable, null-rejecting, array based implementations of {@link java.util.List}
 * that have efficient search methods: {@code indexOf}, {@code lastIndexOf} and {@code contains}.</p>
 *
 * <p>These lists are similar to {@link java.util.ArrayList} in that they store an array internally,
 * but in this case, the array is sorted, so binary search can be used.
 * This means that the list can only contain things that can be compared:
 * primitives or Comparables. Building a list from arbitrary objects is also possible if a suitable Comparator
 * is provided.</p>
 *
 * <p>Instances of this list are slower to create, because of the sorting step, and use more memory than other lists.
 * For each element in the list, an extra 8 bytes (2 int) are needed to store its original position.</p>
 *
 * <p>The speedup of the search methods depends on the size of the list and the cost of the {@code equals} method of
 * its elements. If the list is not very small, the {@code indexOf} methods will often save many calls to {@code equals}.</p>
 */
public abstract class LookupList<E> extends AbstractList<E> implements RandomAccess {

  protected final int[] emptyIntArray = new int[0];

  protected final int[] unsort;
  protected final int[] sort;

  protected LookupList(int[] sort) {
    this.sort = sort;
    this.unsort = invert(sort);
  }

  public static final class Partition<E> {
    public E getKey() {
      return key;
    }

    public int[] getIndexes() {
      return indexes;
    }

    private final E key;
    private final int[] indexes;
    public Partition(E key, int[] indexes) {
      this.key = key;
      this.indexes = indexes;
    }

  }

  public abstract List<Partition> getPartitions();

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
