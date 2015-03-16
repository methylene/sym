package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import static com.github.methylene.sym.PermutationFactory.*;

/**
 * <p>This class contains immutable, null-rejecting, array based implementations of {@link java.util.List}
 * that have efficient {@code indexOf}, {@code lastIndexOf} and {@code contains} methods.</p>
 *
 * <p>Notes:</p>
 * <ul>
 *   <li>Performance gains over {@code java.util.ArrayList} are more significant when the return value of
 *   {@code .indexOf} is large, if the list is big and {@code indexOf} returns {@code -1},
 *   or if the {@code equals} method of the list elements is expensive.
 *   <li>Binary search is used for quick lookup, so all values in the lists must be Comparables,
 *   or a Comparator must be provided. If nulls are allowed, ony the Comparator makes sense, otherwise
 *   {@link java.util.Arrays#binarySearch} will throw an Exception.</li>
 *   <li>Some of the implementations below use arrays of primitives directly and avoid boxing.</li>
 * </ul>
 */
public class Lists {

  /**
   * Marker interface for {@link java.util.List} implementations that are backed by primitive arrays.
   */
  public static interface PrimitiveList {}


  /**
   * Creates a primitive list from the given input.
   * @param a an array
   * @return a list
   */
  public static LookupListBase<Integer> asList(int... a) {
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

  public static <E> Builder<E> builder(Comparator<E> comparator) {
    return new ComparatorList.Builder<E>(comparator);
  }

  public static <E extends Comparable> Builder<E> builder() {
    return new ComparableList.Builder<E>();
  }

  abstract static class Builder<E> {
    protected int size = 0;

    static int extendedCapacity(int oldCapacity, int minCapacity) {
      if (minCapacity < 0)
        throw new IllegalArgumentException("must not be negative");
      int newCapacity = oldCapacity;
      while (newCapacity < minCapacity) {
        int next = newCapacity + (newCapacity >> 1) + 1;
        if (next < newCapacity)
          next = Integer.MAX_VALUE;
        newCapacity = next;
      }
      return newCapacity;
    }

    protected abstract void ensureCapacity(int minCapacity);

    protected abstract Builder<E> add(E el);

    public Builder<E> addAll(E... els) {
      ensureCapacity(size + els.length);
      for (E el : els) {add(el); }
      return this;
    }

    public Builder<E> addAll(Collection<E> els) {
      ensureCapacity(size + els.size());
      for (E el : els) { add(el); }
      return this;
    }

    public abstract List<E> build();

  }





}
