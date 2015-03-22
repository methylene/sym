package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.Rankings.sort;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Rankings;
import com.github.methylene.sym.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Comparable based lookup list.
 */
public final class ComparableList<E extends Comparable> extends LookupList<E> {

  private final Comparable[] sorted;

  private final boolean unique;
  private final boolean ordered;

  private ComparableList(Comparable[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  @SuppressWarnings("unchecked")
  static <E extends Comparable> ComparableList<E> createNewList(Comparable[] a, Permutation sort) {
    Comparable[] applied = sort.apply(a);
    Comparable[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new ComparableList<E>(sorted, Util.isSorted(a), sort, sort.invert());
  }

  public static <E extends Comparable> ComparableList<E> createNewList(Comparable[] a) {
    return createNewList(a, Permutation.sort(a));
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
    if (start < 0) {return -1;}
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
  @SuppressWarnings("unchecked")
  public E get(int i) {
    return (E) sorted[sort.apply(i)];
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
      builder[i++] = unsort.apply(idx + offset);
    } while ((offset = nextOffset(idx, offset, sorted)) != 0 && (size < 0 || i < size));
    return i == size ? builder : Arrays.copyOf(builder, i);
  }

  @Override
  public Map<E, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<E> sort() {
    if (ordered)
      return this;
    ArrayList<E> result = new ArrayList<E>(sorted.length);
    for (Comparable el : sorted)
      result.add((E) el);
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<E> sortUnique() {
    if (unique)
      return sort();
    ArrayList<E> result = new ArrayList<E>(sorted.length);
    E previous = (E) sorted[0];
    for (Comparable el : sorted) {
      if (!el.equals(previous)) {
        result.add(previous);
        previous = (E) el;
      }
    }
    result.add(previous);
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public ComparableList<E> shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.comp(unsort);
      return new ComparableList<E>(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      Comparable[] a = p.comp(super.unsort).apply(sorted);
      return createNewList(a, Permutation.sort(a));
    }
  }

  @Override
  public boolean isUnique() {
    return unique;
  }

  @Override
  public boolean isSorted() {
    return ordered;
  }


  /**
   * Convenience list builder
   */
  public static final class Builder<E extends Comparable> extends ListBuilder<E> {
    private Comparable[] contents;

    Builder(int initialCapacity) {
      this.contents = new Comparable[checkPositive(initialCapacity)];
    }

    Builder() {
      this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public LookupList<E> build() {
      Comparable[] a = Arrays.copyOf(contents, size);
      return createNewList(a, Permutation.sort(a));
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
