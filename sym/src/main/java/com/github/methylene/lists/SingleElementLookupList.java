package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.Util;

import java.util.*;

class SingleElementLookupList<E> extends LookupList<E> {

  final E el;

  SingleElementLookupList(E el) {
    super(Permutation.identity(), Permutation.identity());
    if (el == null)
      throw new NullPointerException("found null");
    this.el = el;
  }

  @Override public int size() {
    return 1;
  }

  @Override public boolean isEmpty() {
    return false;
  }

  @Override public boolean contains(Object o) {
    return el.equals(o);
  }

  @Override public Iterator<E> iterator() {
    return new Iterator<E>() {
      boolean done;

      @Override
      public boolean hasNext() {
        return !done;
      }

      @Override
      public E next() {
        if (done) {
          throw new NoSuchElementException();
        }
        done = true;
        return el;
      }

      @Override public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override public E get(int i) {
    if (i != 0)
      throw new IndexOutOfBoundsException();
    return el;
  }

  @Override public int indexOf(Object o) {
    return el.equals(o) ? 0 : -1;
  }

  @Override public int lastIndexOf(Object o) {
    return el.equals(o) ? 0 : -1;
  }

  @Override public List<E> sort() {
    return this;
  }

  @Override public List<E> sortUnique() {
    return this;
  }

  @Override public Map<E, int[]> group() {
    return Collections.singletonMap(el, new int[]{0});
  }

  @Override public LookupList<E> shuffle(Permutation p) {
    if (!p.isIdentity())
      throw new IllegalArgumentException("length mismatch");
    return this;
  }

  @Override public boolean isUnique() {
    return true;
  }

  @Override public boolean isSorted() {
    return true;
  }

  @Override public int[] indexOf(E el, int size) {
    if (size != 0 && this.el.equals(el))
      return new int[]{0};
    return Util.INT_0;
  }
}
