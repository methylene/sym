package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.ArrayUtil.box;
import static com.github.methylene.sym.ArrayUtil.unique;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import com.github.methylene.sym.ArrayUtil;
import com.github.methylene.sym.Permutation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

/**
 * Primitive based lookup list.
 */
public final class CharList extends LookupList<Character> implements RandomAccess, Serializable {

  private final char[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private CharList(char[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = ArrayUtil.isUnique(sorted, true);
  }

  static CharList createNewList(char[] a, Permutation sort) {
    char[] applied = sort.apply(a);
    char[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new CharList(sorted, ArrayUtil.isSorted(a), sort, sort.invert());
  }

  public static CharList createNewList(char[] a) {
    return createNewList(a, Permutation.sort(a));
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Character) el);
    return i < 0 ? -1 : unsort.apply(i);
  }

  @Override
  public int lastIndexOf(Object el) {
    char c = (Character) el;
    int start = Arrays.binarySearch(sorted, c);
    if (start < 0) {return -1;}
    int direction = start > 0 && sorted[start - 1] == c ? -1 : 1;
    int peek = start + direction;
    while (peek >= 0 && peek < sorted.length && sorted[peek] == c) {
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
  public Character get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  @SuppressWarnings("unchecked")
  public int[] indexOf(Character el, int size) {
    final char c = el;
    final int idx = binarySearch(sorted, c);
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
  public Map<Character, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  public List<Character> sort() {
    if (ordered)
      return this;
    return Arrays.asList(box(sorted));
  }

  @Override
  public List<Character> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }

  @Override
  public CharList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.compose(unsort);
      return new CharList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      char[] a = p.compose(super.unsort).apply(sorted);
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
