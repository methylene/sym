package com.github.methylene.lists;

import static java.util.Arrays.copyOf;

import java.util.Collection;
import java.util.List;

public abstract class ListBuilder<E> {

  protected static final int DEFAULT_INITIAL_CAPACITY = 16;

  protected int size = 0;

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

  public static int[] ensureCapacity(int[] a, int minCapacity) {
    return (minCapacity > a.length) ? copyOf(a, extendedCapacity(a.length, minCapacity)) : a;
  }

  protected abstract void ensureCapacity(int minCapacity);

  protected abstract ListBuilder<E> add(E el);

  public final ListBuilder<E> addAll(E... els) {
    ensureCapacity(size + els.length);
    for (E el : els) {add(el); }
    return this;
  }

  public final ListBuilder<E> addAll(Collection<E> els) {
    ensureCapacity(size + els.size());
    for (E el : els) { add(el); }
    return this;
  }

  public abstract List<E> build();

}
