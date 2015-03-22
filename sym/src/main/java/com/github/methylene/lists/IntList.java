package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.Rankings.sort;
import static com.github.methylene.sym.Util.box;
import static com.github.methylene.sym.Util.unique;
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

/**
 * Primitive based lookup list.
 */
public final class IntList extends LookupList<Integer> {

  private final int[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private IntList(int[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  static IntList createNewList(int[] a, Permutation sort) {
    int[] applied = sort.apply(a);
    int[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new IntList(sorted, Util.isSorted(a), sort, sort.invert());
  }

  public static IntList createNewList(int[] a) {
    return createNewList(a, Permutation.sort(a));
  }

  @Override
  public int indexOf(Object el) {
    int i = binarySearch(sorted, (Integer) el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    int n = (Integer) el;
    int idx = binarySearch(sorted, n);
    if (idx < 0)
      return -1;
    int direction = idx > 0 && sorted[idx - 1] == n ? -1 : 1;
    int peek = idx + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
      idx = peek;
      peek += direction;
    }
    return unsort.apply(idx);
  }

  @Override
  public boolean contains(Object el) {
    return indexOf(el) >= 0;
  }

  @Override
  public Integer get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  @SuppressWarnings("unchecked")
  public int[] indexOf(Integer el, final int size) {
    final int n = el;
    final int idx = binarySearch(sorted, n);
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
  public List<Integer> sort() {
    if (ordered)
      return this;
    return Arrays.asList(box(sorted));
  }

  @Override
  public List<Integer> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }

  @Override
  public Map<Integer, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  public IntList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.comp(unsort);
      return new IntList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      int[] a = p.comp(super.unsort).apply(sorted);
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
  public static final class Builder extends ListBuilder<Integer> {

    private int[] contents;

    Builder(int initialCapacity) {
      if (initialCapacity < 0)
        throw new IllegalArgumentException("initial capacity can not be negative");
      this.contents = new int[initialCapacity];
    }

    Builder() {
      this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public IntList build() {
      int[] a = Arrays.copyOf(contents, size);
      return createNewList(a, Permutation.sort(a));
    }


    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = ensureCapacity(contents, minCapacity);
    }

    @Override
    public Builder add(Integer element) {
      ensureCapacity(size + 1);
      contents[size++] = element;
      return this;
    }

    public Builder add(int element) {
      ensureCapacity(size + 1);
      contents[size++] = element;
      return this;
    }

  }

}
