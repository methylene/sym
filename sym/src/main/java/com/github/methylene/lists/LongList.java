package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.ArrayUtil.box;
import static com.github.methylene.sym.ArrayUtil.sortedCopy;
import static com.github.methylene.sym.ArrayUtil.unique;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import com.github.methylene.sym.ArrayUtil;
import com.github.methylene.sym.Permutation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

/**
 * Primitive based lookup list.
 */
public final class LongList extends LookupList<Long> implements RandomAccess, Serializable {

  private static final long serialVersionUID = 1L;

  private final long[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private LongList(long[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = ArrayUtil.isUnique(sorted, true);
  }

  static LongList createNewList(long[] a, Permutation sort) {
    long[] applied = sort.apply(a);
    long[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new LongList(sorted, ArrayUtil.isSorted(a), sort, sort.invert());
  }

  public static LongList createNewList(long[] a) {
    return createNewList(a, Permutation.sort(a));
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Long) el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    long n = (Long) el;
    int start = Arrays.binarySearch(sorted, n);
    if (start < 0) {return -1;}
    int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
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
  public Long get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexOf(Long el, int size) {
    final long n = el;
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
  public Map<Long, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  public List<Long> sort() {
    if (ordered)
      return this;
    return Arrays.asList(box(sorted));
  }

  @Override
  public List<Long> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }

  @Override
  public LongList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.compose(unsort);
      return new LongList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      long[] a = p.compose(super.unsort).apply(sorted);
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


}
