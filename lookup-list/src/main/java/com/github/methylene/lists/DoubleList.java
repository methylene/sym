package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;

import java.util.Arrays;
import java.util.List;

/**
 * Primitive based lookup list.
 */
public final class DoubleList extends LookupList<Double> implements Lists.PrimitiveList {
  private final double[] sorted;

  DoubleList(double[] a, Permutation sort) {
    super(sort);
    this.sorted = sort.apply(a);
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
  public int[] indexesOf(Double el) {
    double d = el;
    int pos = Arrays.binarySearch(sorted, d);
    if (pos < 0) {return emptyIntArray;}
    IntList.Builder builder = new IntList.Builder();
    int offset = 0;
    int direction = 1;
    int current;
    while (sorted[current = pos + offset] == d) {
      builder.add(unsort.apply(current));
      if (direction == 1) {
        if (pos + offset + direction >= sorted.length
            || sorted[pos + offset + 1] != d) {
          if (pos > 0) {
            offset = -1;
            direction = -1;
          } else {
            break;
          }
        } else {
          offset += 1;
        }
      } else {
        if (pos + offset == 0) break;
        offset -= 1;
      }
    }
    return builder.get();
  }

}
