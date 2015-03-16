package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * Base class for list implementations.
 */
public abstract class LookupListBase<E> extends AbstractList<E> implements LookupList<E>, RandomAccess {

  protected final int[] emptyIntArray = new int[0];

  protected final Permutation unsort;
  protected final Permutation sort;

  protected LookupListBase(Permutation sort) {
    this.sort = sort;
    this.unsort = sort.invert();
  }

  /**
   * Get the inverse of a particular permutation which sorts this list.
   * @return the unsort permutation
   */
  public final Permutation getUnsort() {
    return unsort;
  }

  /**
   * Get a particular permutation which sorts this list.
   * @return the sort permutation
   */
  public final Permutation getSort() {
    return sort;
  }



  public abstract int[] indexesOf(E el);

}
