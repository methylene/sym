package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EmptyLookupList<E> extends LookupList<E> {

  static final LookupList<Object> INSTANCE = new EmptyLookupList<Object>();

  private EmptyLookupList() {
    super(Permutation.identity(), Permutation.identity());
  }

  @Override public List<E> sort() {
    return this;
  }

  @Override public List<E> sortUnique() {
    return this;
  }

  @Override public Map<E, int[]> group() {
    return Collections.emptyMap();
  }

  @Override public LookupList<E> shuffle(Permutation p) {
    return this;
  }

  @Override public boolean isUnique() {
    return true;
  }

  @Override public boolean isSorted() {
    return true;
  }

  @Override public int[] indexOf(E el, int size) {
    return Util.INT_0;
  }

  @Override public E get(int i) {
    throw new ArrayIndexOutOfBoundsException();
  }

  @Override public int size() {
    return 0;
  }

}
