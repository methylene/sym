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
import com.github.methylene.sym.Util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

/**
 * Primitive based lookup list.
 */
public final class ShortList extends LookupList<Short> implements RandomAccess, Serializable {

  private static final long serialVersionUID = 1L;

  private final short[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private ShortList(short[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  static ShortList createNewList(short[] a, Permutation sort) {
    short[] applied = sort.apply(a);
    short[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new ShortList(sorted, Util.isSorted(a), sort, sort.invert());
  }

  public static ShortList createNewList(short[] a) {
    return createNewList(a, Permutation.sort(a));
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Short) el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    short n = (Short) el;
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
  public Short get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexOf(Short el, int size) {
    final short n = el;
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
  public Map<Short, int[]> group() {
    return Group.group(sorted, unsort);
  }


  @Override
  public List<Short> sort() {
    if (ordered)
      return this;
    return Arrays.asList(Util.box(sorted));
  }

  @Override
  public List<Short> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }


  @Override
  public ShortList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.compose(unsort);
      return new ShortList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      short[] a = p.compose(super.unsort).apply(sorted);
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
