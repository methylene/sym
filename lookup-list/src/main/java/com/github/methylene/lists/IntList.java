package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;

import java.util.Arrays;
import static com.github.methylene.sym.PermutationFactory.*;

/**
 * Primitive based lookup list.
 */
public final class IntList extends LookupListBase<Integer> implements Lists.PrimitiveList {
  private final int[] sorted;

  IntList(int[] a, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Integer) el);
    return i < 0 ? -1 : unsort[i];
  }

  @Override
  public int lastIndexOf(Object el) {
    int n = (Integer) el;
    int start = Arrays.binarySearch(sorted, n);
    if (start < 0) {return -1;};
    int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
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
  public Integer get(int i) {
    return sorted[sort[i]];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexesOf(Integer el) {
    int n = el;
    int pos = Arrays.binarySearch(sorted, n);
    if (pos < 0) {return emptyIntArray;}
    Builder builder = new Builder();
    int offset = 0;
    int direction = 1;
    int current;
    while (sorted[current = pos + offset] == n) {
      builder.add(unsort[current]);
      if (direction == 1) {
        if (pos + offset + direction >= sorted.length
            || sorted[pos + offset + 1] != n) {
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

  public static final class Builder extends Lists.Builder<Integer> {
    private int[] contents = new int[16];

    @Override
    public IntList build() {
      int[] a = Arrays.copyOf(contents, size);
      return new IntList(a, sort(a));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = Arrays.copyOf(contents, extendedCapacity(contents.length, minCapacity));
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

    int[] get() {
      return Arrays.copyOf(contents, size);
    }

  }

}
