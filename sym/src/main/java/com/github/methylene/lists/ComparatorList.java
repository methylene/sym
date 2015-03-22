package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.Rankings.sort;
import static java.util.Arrays.copyOf;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Rankings;
import com.github.methylene.sym.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Comparator based lookup list.
 */
public final class ComparatorList<E> extends LookupList<E> {

  private final Object[] sorted;
  private final Comparator<E> comparator;
  private final boolean unique;
  private final boolean ordered;

  private ComparatorList(Object[] sorted, boolean ordered, Permutation sort, Permutation unsort, Comparator<E> comparator) {
    super(sort, unsort);
    this.comparator = comparator;
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  static <E> ComparatorList<E> createNewList(Comparator<E> comparator, Object[] a, Permutation sort) {
    Object[] applied = sort.apply(a);
    Object[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new ComparatorList<E>(sorted, Util.isSorted(comparator, a), sort, sort.invert(), comparator);
  }

  public static <E> ComparatorList<E> createNewList(Comparator<E> comparator, Object[] a) {
    return createNewList(comparator, a, Permutation.sort(a, comparator));
  }

  /**
   * Get the comparator used by this instance.
   * @return the comparator
   */
  public Comparator<E> getComparator() {
    return comparator;
  }

  @Override
  public int indexOf(Object el) {
    @SuppressWarnings("unchecked")
    int i = Arrays.binarySearch(sorted, el, (Comparator) comparator);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    @SuppressWarnings("unchecked")
    int start = Arrays.binarySearch(sorted, el, (Comparator) comparator);
    if (start < 0) {return -1;}
    int direction = start > 0 && Objects.equals(sorted[start - 1], el) ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], el)) {
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
    @SuppressWarnings("unchecked")
    final int idx = Arrays.binarySearch(sorted, el, (Comparator) comparator);
    if (idx < 0)
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
    for (Object el : sorted)
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
    for (Object el : sorted) {
      if (!el.equals(previous)) {
        result.add(previous);
        previous = (E) el;
      }
    }
    result.add(previous);
    return result;
  }

  @Override
  public ComparatorList<E> shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.comp(unsort);
      return new ComparatorList<E>(sorted, punsort.sorts(comparator, sorted), punsort.invert(), punsort, comparator);
    } else {
      Object[] a = p.comp(super.unsort).apply(sorted);
      return createNewList(comparator, a, Permutation.sort(a, comparator));
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
  public static final class Builder<E> extends ListBuilder<E> {

    private final Comparator<E> comparator;
    private Object[] contents;

    Builder(Comparator<E> comparator, int initialCapacity) {
      this.comparator = comparator;
      this.contents = new Comparable[checkPositive(initialCapacity)];    }

    Builder(Comparator<E> comparator) {
      this(comparator, DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ComparatorList<E> build() {
      Object[] a = Arrays.copyOf(contents, size);
      return createNewList(comparator, a, Permutation.sort(a, (Comparator) comparator));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = ensureCapacity(contents, minCapacity);
    }

    @Override
    public Builder<E> add(E el) {
      ensureCapacity(size + 1);
      contents[size++] = el;
      return this;
    }

  }


}
