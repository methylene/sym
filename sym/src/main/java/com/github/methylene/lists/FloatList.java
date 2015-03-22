package com.github.methylene.lists;

import static com.github.methylene.lists.ListBuilder.DEFAULT_INITIAL_CAPACITY;
import static com.github.methylene.lists.ListBuilder.ensureCapacity;
import static com.github.methylene.sym.Rankings.apply;
import static com.github.methylene.sym.Rankings.nextOffset;
import static com.github.methylene.sym.Util.box;
import static com.github.methylene.sym.Util.sortedCopy;
import static com.github.methylene.sym.Util.unique;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Primitive based lookup list.
 */
public final class FloatList extends LookupList<Float> {

  private final float[] sorted;
  private final boolean unique;
  private final boolean ordered;

  private FloatList(float[] sorted, boolean ordered, Permutation sort, Permutation unsort) {
    super(sort, unsort);
    this.sorted = sorted;
    this.ordered = ordered;
    this.unique = Util.isUnique(sorted);
  }

  static FloatList createNewList(float[] a, Permutation sort) {
    float[] applied = sort.apply(a);
    float[] sorted = applied == a ? Arrays.copyOf(a, a.length) : applied;
    return new FloatList(sorted, Util.isSorted(a), sort, sort.invert());
  }

  public static FloatList createNewList(float[] a) {
    return createNewList(a, Permutation.sort(a));
  }

  @Override
  public int indexOf(Object el) {
    int i = Arrays.binarySearch(sorted, (Float) el);
    return i < 0 ? -1 : unsort.apply(i);
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
    return unsort.apply(start);
  }


  @Override
  public boolean contains(Object el) {
    return indexOf(el) >= 0;
  }

  @Override
  public Float get(int i) {
    return sorted[sort.apply(i)];
  }

  @Override
  public int size() {
    return sorted.length;
  }

  @Override
  public int[] indexOf(Float el, int size) {
    final float f = el;
    final int idx = binarySearch(sorted, f);
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
  public Map<Float, int[]> group() {
    return Group.group(sorted, unsort);
  }

  @Override
  public List<Float> sort() {
    if (ordered)
      return this;
    return Arrays.asList(box(sorted));
  }

  @Override
  public List<Float> sortUnique() {
    if (unique)
      return sort();
    return Arrays.asList(box(unique(sorted)));
  }

  @Override
  public FloatList shuffle(Permutation p) {
    if (unique) {
      Permutation punsort = p.comp(unsort);
      return new FloatList(sorted, punsort.sorts(sorted), punsort.invert(), punsort);
    } else {
      float[] a = p.comp(super.unsort).apply(sorted);
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
