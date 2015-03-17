package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.sort;

import java.util.Arrays;

/**
 * Primitive based lookup list.
 */
public final class IntList extends LookupList<Integer> {
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
    if (start < 0) {return -1;}
    ;
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
  @SuppressWarnings("unchecked")
  public int[] indexOf(Integer el, int size) {
    final int n = el;
    final int pos = Arrays.binarySearch(sorted, n);
    if (pos < 0)
      return emptyIntArray;
    final boolean varsize = size < 0;
    final Object builder = varsize ? new Builder() : new int[size];
    int offset = 0;
    int current;
    int i = 0;
    while (sorted[current = pos + offset] == n
        && (varsize || i < size)) {
      if (varsize)
        ((Builder) builder).add(unsort[current]);
      else
        ((int[]) builder)[i] = unsort[current];
      i++;
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length
            || sorted[next] != n) {
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
    return varsize ? ((Builder) builder).get() :
        i == size ? (int[]) builder :
            Arrays.copyOf((int[]) builder, i);
  }

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
