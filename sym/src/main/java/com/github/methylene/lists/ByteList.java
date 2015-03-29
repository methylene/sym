package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.Util.box;
import static com.github.methylene.sym.Util.unique;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Primitive based lookup list.
 */
public final class ByteList extends LookupList<Byte> {

  private final byte[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private ByteList(byte[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  static ByteList createNewList(byte[] a, Permutation sort) {
    byte[] applied = sort.apply(a);
    byte[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new ByteList(sorted, Util.isSorted(a), sort, sort.invert());
  }

  public static ByteList createNewList(byte[] a) {
    return createNewList(a, Permutation.sort(a));
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
  @SuppressWarnings("unchecked")
  public int[] indexOf(Byte el, int size) {
    final byte b = el;
    final int idx = binarySearch(sorted, b);
    if (idx < 0 || size == 0)
      return EMPTY_INT_ARRAY;
    int[] builder = new int[size < 0 ? DEFAULT_INITIAL_CAPACITY : size];
    int offset = 0;
    int i = 0;
    do {
      builder = ensureCapacity(builder, i + 1);
      builder[i++] = unsort.apply(idx + offset);
    } while ((offset = nextOffset(idx, offset, sorted)) != 0 && (size < 0 || i < size));
    return i == size ? builder : Arrays.copyOf(builder, i);
  }

  @Override
  public Map<Byte, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  public List<Byte> sort() {
    if (ordered)
      return this;
    return Arrays.asList(box(sorted));  }

  @Override
  public List<Byte> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }


  @Override
  public ByteList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.compose(unsort);
      return new ByteList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      byte[] a = p.compose(super.unsort).apply(sorted);
      return createNewList(a, Permutation.sort(a));
    }
  }

  @Override
  public boolean isUnique() {
    return unique;
  }

  @Override
  public boolean isSorted() {
    return ordered;
  }


}
