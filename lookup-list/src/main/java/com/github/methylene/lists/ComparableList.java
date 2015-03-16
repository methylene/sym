package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Comparable based lookup list.
 */
public final class ComparableList<E extends Comparable> extends LookupListBase<E> {
  private final Comparable[] sorted;

  ComparableList(Comparable[] a, Permutation sort) {
    super(sort);
    this.sorted = sort.apply(a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    Comparable comparable = (Comparable) el;
    int start = Arrays.binarySearch(sorted, comparable);
    if (start < 0) {return -1;}
    int direction = start > 0 && Objects.equals(sorted[start - 1], comparable) ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], comparable)) {
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
  @SuppressWarnings("unchecked")
  public E get(int i) {
    return (E) sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexesOf(E el) {
    int pos = Arrays.binarySearch(sorted, el);
    if (pos < 0) {return emptyIntArray;}
    IntList.Builder builder = new IntList.Builder();
    int offset = 0;
    int direction = 1;
    int current;
    while (Objects.equals(sorted[current = pos + offset], el)) {
      builder.add(unsort.apply(current));
      if (direction == 1) {
        if (pos + offset + direction >= sorted.length
            || !Objects.equals(sorted[pos + offset + 1], el)) {
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

  public static final class Builder<E extends Comparable> extends Lists.Builder<E> {
    private Comparable[] contents = new Comparable[16];

    Builder() {}

    @Override
    public List<E> build() {
      Comparable[] a = Arrays.copyOf(contents, size);
      return new ComparableList<E>(a, Permutation.factory().sort(a));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = Arrays.copyOf(contents, extendedCapacity(contents.length, minCapacity));
    }

    @Override
    public Builder<E> add(E element) {
      ensureCapacity(size + 1);
      contents[size++] = element;
      return this;
    }

  }


}
