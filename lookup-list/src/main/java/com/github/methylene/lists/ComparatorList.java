package com.github.methylene.lists;

import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;


/**
 * Comparator based lookup list.
 */
public final class ComparatorList<E> extends LookupList<E> {
  private final Object[] sorted;
  private final Comparator<E> comparator;

  ComparatorList(Object[] a, Comparator<E> comparator, int[] sort) {
    super(sort);
    this.sorted = apply(sort, a);
    this.comparator = comparator;
  }

  /**
   * Get the comparator used by this instance.
   * @return the comparator
   */
  public Comparator<E> getComparator() {
    return comparator;
  }

  @Override
  public int indexOf(Object el) {
    @SuppressWarnings("unchecked")
    int i = Arrays.binarySearch(sorted, el, (Comparator) comparator);
    return i < 0 ? -1 : unsort[i];
  }

  @Override
  public int lastIndexOf(Object el) {
    @SuppressWarnings("unchecked")
    int start = Arrays.binarySearch(sorted, el, (Comparator) comparator);
    if (start < 0) {return -1;}
    int direction = start > 0 && Objects.equals(sorted[start - 1], el) ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], el)) {
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
    @SuppressWarnings("unchecked")
    final int pos = Arrays.binarySearch(sorted, el, (Comparator) comparator);
    if (pos < 0)
      return emptyIntArray;
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

  public static final class Builder<E> extends ListBuilder<E> {
    private final Comparator<E> comparator;
    private Object[] contents;

    Builder(Comparator<E> comparator, int initialCapacity) {
      if (initialCapacity < 0)
        throw new IllegalArgumentException("initial capacity can not be negative");
      this.comparator = comparator;
      this.contents = new Object[initialCapacity];
    }

    Builder(Comparator<E> comparator) {
      this(comparator, DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ComparatorList<E> build() {
      Object[] a = Arrays.copyOf(contents, size);
      return new ComparatorList<E>(a, comparator, sort(a, (Comparator) comparator));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = Arrays.copyOf(contents, extendedCapacity(contents.length, minCapacity));
    }

    @Override
    public Builder<E> add(E el) {
      ensureCapacity(size + 1);
      contents[size++] = el;
      return this;
    }

  }


}
