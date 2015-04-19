package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
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
public final class DoubleList extends LookupList<Double> implements RandomAccess, Serializable {

  private static final long serialVersionUID = 1L;
  private final double[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private DoubleList(double[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  static DoubleList createNewList(double[] a, Permutation sort) {
    double[] applied = sort.apply(a);
    double[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new DoubleList(sorted, Util.isSorted(a), sort, sort.invert());
  }

  public static DoubleList createNewList(double[] a) {
    return createNewList(a, Permutation.sort(a));
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Double) el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    double d = (Double) el;
    int start = Arrays.binarySearch(sorted, d);
    if (start < 0) {return -1;}
    int direction = start > 0 && sorted[start - 1] == d ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == d) {
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
  public Double get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexOf(Double el, int size) {
    final double f = el;
    final int idx = binarySearch(sorted, f);
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
  public Map<Double, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  public List<Double> sort() {
    if (ordered)
      return this;
    return Arrays.asList(box(sorted));
  }

  @Override
  public List<Double> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }

  @Override
  public DoubleList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.compose(unsort);
      return new DoubleList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      double[] a = p.compose(super.unsort).apply(sorted);
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
