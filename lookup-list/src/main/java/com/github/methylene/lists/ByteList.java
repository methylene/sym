package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;

import java.util.Arrays;

/**
 * Primitive based lookup list.
 */
public final class ByteList extends LookupList<Byte> implements Lists.PrimitiveList {
  private final byte[] sorted;

  ByteList(byte[] a, Permutation sort) {
    super(sort);
    this.sorted = sort.apply(a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Byte) el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    byte b = (Byte) el;
    int start = Arrays.binarySearch(sorted, b);
    if (start < 0) {return -1;}
    int direction = start > 0 && sorted[start - 1] == b ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == b) {
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
  public Byte get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexesOf(Byte el) {
    byte b = el;
    int pos = Arrays.binarySearch(sorted, b);
    if (pos < 0) {return emptyIntArray;}
    IntList.Builder builder = new IntList.Builder();
    int offset = 0;
    int direction = 1;
    int current;
    while (sorted[current = pos + offset] == b) {
      builder.add(unsort.apply(current));
      if (direction == 1) {
        if (pos + offset + direction >= sorted.length
            || sorted[pos + offset + 1] != b) {
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
