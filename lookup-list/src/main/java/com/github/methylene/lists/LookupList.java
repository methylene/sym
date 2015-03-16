package com.github.methylene.lists;

import java.util.List;

public interface LookupList<E> extends List<E> {

  /**
   * Find all indexes in sequence {@code i} where
   * <pre><code>
   *   this.get(i).equals(el)
   * </code></pre>
   * The return value will always be sorted.
   * If {@code el} is not in the list, this returns an empty array.
   * @param e an object
   * @return an increasing list of all indexes where the value equals {@code el}
   */
  public int[] indexesOf(E e);

}
