package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Comparable based lookup list.
 */
public final class ComparableList<E extends Comparable> extends LookupList<E> {
  private final Comparable[] sorted;

  ComparableList(Comparable[] a, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, el);
    return i < 0 ? -1 : unsort[i];
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
    return unsort[start];
  }

  @Override
  public boolean contains(Object el) {
    return indexOf(el) >= 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E get(int i) {
    return (E) sorted[sort[i]];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexOf(E el, int size) {
    final int pos = Arrays.binarySearch(sorted, el);
    if (pos < 0)
      return EMPTY_INT_ARRAY;
    final boolean varsize = size < 0;
    final Object builder = varsize ? new IntList.Builder() : new int[size];
    int offset = 0;
    int current;
    int i = 0;
    while (el.equals(sorted[current = pos + offset])
        && (varsize || i < size)) {
      if (varsize)
        ((IntList.Builder) builder).add(unsort[current]);
      else
        ((int[]) builder)[i] = unsort[current];
      i++;
      if (offset >= 0) {
        int next = current + 1;
        if (next >= sorted.length
            || !el.equals(sorted[next])) {
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

  public static final class Builder<E extends Comparable> extends ListBuilder<E> {
    private Comparable[] contents;

    Builder(int initialCapacity) {
      if (initialCapacity < 0)
        throw new IllegalArgumentException("initial capacity can not be negative");
      this.contents = new Comparable[initialCapacity];
    }

    Builder() {
      this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public List<E> build() {
      Comparable[] a = Arrays.copyOf(contents, size);
      return new ComparableList<E>(a, sort(a));
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
