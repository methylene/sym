package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;
import com.github.methylene.sym.PermutationFactory;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * Base class for list implementations.
 */
public abstract class LookupListBase<E> extends AbstractList<E> implements LookupList<E>, RandomAccess {

  protected final int[] emptyIntArray = new int[0];

  protected final int[] unsort;
  protected final int[] sort;

  protected LookupListBase(int[] sort) {
    this.sort = sort;
    this.unsort = PermutationFactory.invert(sort);
  }

  public abstract int[] indexesOf(E el);

}
