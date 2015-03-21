package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.apply;

import java.util.Arrays;

/**
 * Primitive based lookup list.
 */
public final class ByteList extends LookupList<Byte> {
  private final byte[] sorted;

  ByteList(byte[] a, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Byte) el);
    return i < 0 ? -1 : unsort[i];
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
    return unsort[start];
  }

  @Override
  public boolean contains(Object el) {
    return indexOf(el) >= 0;
  }

  @Override
  public Byte get(int i) {
    return sorted[sort[i]];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  @SuppressWarnings("unchecked")
  public int[] indexOf(Byte el, int size) {
    final byte b = el;
    final int pos = Arrays.binarySearch(sorted, b);
    if (pos < 0)
      return EMPTY_INT_ARRAY;
    final boolean varsize = size < 0;
    final Object builder = varsize ? new IntList.Builder() : new int[size];
    int offset = 0;
    int current;
    int i = 0;
    while (sorted[current = pos + offset] == b
        && (varsize || i < size)) {
      if (varsize)
        ((IntList.Builder) builder).add(unsort[current]);
      else
        ((int[]) builder)[i] = unsort[current];
      i++;
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length
            || sorted[next] != b) {
          if (pos > 0)
            offset = -1;
          else
            break;
        } else {
          offset++;
        }
      } else {
        if (current == 0)
          break;
        offset--;
      }
    }
    return varsize ? ((IntList.Builder) builder).get() :
        i == size ? (int[]) builder :
            Arrays.copyOf((int[]) builder, i);
  }

}
