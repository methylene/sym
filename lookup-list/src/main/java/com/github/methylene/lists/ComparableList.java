package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.Rankings.sort;
import static java.lang.System.arraycopy;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Comparable based lookup list.
 */
public final class ComparableList<E extends Comparable> extends LookupList<E> {
  private final Comparable[] sorted;

  ComparableList(Comparable[] a, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, el);
    return i < 0 ? -1 : unsort[i];
  }

  @Override
  public int lastIndexOf(Object el) {
    Comparable comparable = (Comparable) el;
    int start = Arrays.binarySearch(sorted, comparable);
    if (start < 0) {return -1;}
    int direction = start > 0 && Objects.equals(sorted[start - 1], comparable) ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], comparable)) {
      start = peek;
      peek += direction;
    }
    return unsort[start];
  }

  @Override
  public boolean contains(Object el) {
    return indexOf(el) >= 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E get(int i) {
    return (E) sorted[sort[i]];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexOf(E el, int size) {
    final int idx = binarySearch(sorted, el);
    if (idx < 0 || size == 0)
      return EMPTY_INT_ARRAY;
    int[] builder = new int[size < 0 ? DEFAULT_INITIAL_CAPACITY : size];
    int offset = 0;
    int i = 0;
    do {
      builder = ensureCapacity(builder, i + 1);
      builder[i++] = unsort[idx + offset];
    } while ((offset = nextOffset(idx, offset, sorted)) != 0 && (size < 0 || i < size));
    return i == size ? builder : copyOf(builder, i);
  }

  @Override
  public Map<E, int[]> getPartitions() {
    return Partitions.partition(sorted, unsort);
  }

  public static final class Builder<E extends Comparable> extends ListBuilder<E> {
    private Comparable[] contents;

    Builder(int initialCapacity) {
      this.contents = new Comparable[checkPositive(initialCapacity)];
    }

    Builder() {
      this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public List<E> build() {
      Comparable[] a = Arrays.copyOf(contents, size);
      return new ComparableList<E>(a, sort(a));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = ensureCapacity(contents, minCapacity);
    }

    @Override
    public Builder<E> add(E element) {
      ensureCapacity(size + 1);
      contents[size++] = element;
      return this;
    }

  }


}
