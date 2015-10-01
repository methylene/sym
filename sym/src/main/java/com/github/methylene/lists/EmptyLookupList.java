package com.github.methylene.lists;

import com.github.methylene.sym.ArrayUtil;
import com.github.methylene.sym.Permutation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

class EmptyLookupList<E> extends LookupList<E> implements RandomAccess, Serializable {

  private static final long serialVersionUID = 1L;
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
    return ArrayUtil.INT_0;
  }

  @Override public E get(int i) {
    throw new ArrayIndexOutOfBoundsException();
  }

  @Override public int size() {
    return 0;
  }

}
