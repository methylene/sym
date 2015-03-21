package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import java.util.Arrays;
import java.util.Map;

/**
 * Primitive based lookup list.
 */
public final class DoubleList extends LookupList<Double> {
  private final double[] sorted;

  DoubleList(double[] a, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Double) el);
    return i < 0 ? -1 : unsort[i];
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
    return unsort[start];
  }


  @Override
  public boolean contains(Object el) {
    return indexOf(el) >= 0;
  }

  @Override
  public Double get(int i) {
    return sorted[sort[i]];
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
      builder[i++] = unsort[idx + offset];
    } while ((offset = nextOffset(idx, offset, sorted)) != 0 && (size < 0 || i < size));
    return i == size ? builder : copyOf(builder, i);
  }

  @Override
  public Map<Double, int[]> getPartitions() {
    return Partitions.partition(sorted, unsort);
  }

}
