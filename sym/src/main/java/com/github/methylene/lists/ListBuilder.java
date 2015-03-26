package com.github.methylene.lists;

import static java.util.Arrays.copyOf;

import java.util.Collection;
import java.util.List;

/**
 * Base class for list builders in this package.
 */
public abstract class ListBuilder<E> {

  protected static final int DEFAULT_INITIAL_CAPACITY = 16;

  protected int size = 0;

  protected void incrementSize(int n) {
    size += n;
  }

  public static int extendedCapacity(int oldCapacity, int minCapacity) {
    if (minCapacity < 0)
      throw new IllegalArgumentException("min capacity can not be negative");
    int newCapacity = oldCapacity;
    while (newCapacity < minCapacity) {
      int next = newCapacity + (newCapacity >> 1) + 1;
      if (next < newCapacity)
        next = Integer.MAX_VALUE;
      newCapacity = next;
    }
    return newCapacity;
  }

  static int checkPositive(int initialCapacity) {
    if (initialCapacity <= 0)
      throw new IllegalArgumentException("initial capacity must be positive");
    return initialCapacity;
  }

  public static int[] ensureCapacity(int[] a, int minCapacity) {
    return (minCapacity > a.length) ? copyOf(a, extendedCapacity(a.length, minCapacity)) : a;
  }

  public static Object[] ensureCapacity(Object[] a, int minCapacity) {
    return (minCapacity > a.length) ? copyOf(a, extendedCapacity(a.length, minCapacity)) : a;
  }

  public static Comparable[] ensureCapacity(Comparable[] a, int minCapacity) {
    return (minCapacity > a.length) ? copyOf(a, extendedCapacity(a.length, minCapacity)) : a;
  }

  protected abstract void ensureCapacity(int minCapacity);

  protected abstract ListBuilder<E> add(E el);

  /**
   * Add all elements of {@code elements} to the list
   * @param elements
   * @return the builder
   */
  public abstract ListBuilder<E> addAll(E... elements);

  /**
   * Add all elements of {@code elements} to the list
   * @param elements
   * @return the builder
   */
  public final ListBuilder<E> addAll(Iterable<E> elements) {
    for (E el : elements) { add(el); }
    return this;
  }

  /**
   * Return a new LookupList based on the contents of this builder.
   * @return a new list
   */
  public abstract LookupList<E> build();

}
