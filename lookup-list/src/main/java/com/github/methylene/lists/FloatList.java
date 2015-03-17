package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.apply;

import java.util.Arrays;

/**
 * Primitive based lookup list.
 */
public final class FloatList extends LookupList<Float> {
  private final float[] sorted;

  FloatList(float[] a, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Float) el);
    return i < 0 ? -1 : unsort[i];
  }

  @Override
  public int lastIndexOf(Object el) {
    float f = (Float) el;
    int start = Arrays.binarySearch(sorted, f);
    if (start < 0) {return -1;}
    int direction = start > 0 && sorted[start - 1] == f ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == f) {
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
  public Float get(int i) {
    return sorted[sort[i]];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexesOf(Float el) {
    float f = el;
    int pos = Arrays.binarySearch(sorted, f);
    if (pos < 0) {return emptyIntArray;}
    IntList.Builder builder = new IntList.Builder();
    int offset = 0;
    int direction = 1;
    int current;
    while (sorted[current = pos + offset] == f) {
      builder.add(unsort[current]);
      if (direction == 1) {
        if (pos + offset + direction >= sorted.length
            || sorted[pos + offset + 1] != f) {
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
