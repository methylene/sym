package com.github.methylene.sym;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.github.methylene.sym.Searchable.searchableArray;
import static com.github.methylene.sym.Searchable.IntArray;
import static com.github.methylene.sym.Searchable.ObjectArray;

public class SearchableTest {

  /**
   * This test asserts that indexOf always returns the lowest possible index, if there is a choice.
   * That is, if
   * <pre><code>
   *   int i = searchableArray(a).indexOf(el);
   * </code></pre>
   * and
   * <pre><code>
   *   a[j] = el;
   * </code></pre>
   * then
   * <pre><code>
   *   i <= j;
   * </code></pre>
   * @throws Exception if the assertion is wrong
   */
  @Test
  public void testFirst() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      int[] a = Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] pair = TestUtil.duplicateIndexes(a);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      int el = a[pair[0]];
      IntArray searchable = searchableArray(a);
      int i = searchable.indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }


  /**
   * Same as {@link com.github.methylene.sym.SearchableTest#testFirst} but for object array
   * @throws Exception
   */
  @Test
  public void testFirstComparator() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      MyInt[] a = MyInt.box(Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      int[] pair = TestUtil.duplicateIndexes(a, MyInt.COMP);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      MyInt el = a[pair[0]];
      ObjectArray searchable = searchableArray(a, MyInt.COMP);
      int i = searchable.indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  @Test
  public void testStrictRandom() throws Exception {
    for (int _ = 0; _ < 1000; _ += 1) {
      int maxNumber = (int) (Math.random() * 100);
      int[] a = Util.distinctInts(maxNumber, (int) (Math.random() * 10 + 2));
      Searchable.IntArray searchable = Searchable.strictSearchable().array(a);
      int el = (int) (maxNumber * Math.random());
      int i = searchable.indexOf(el);
      if (i == -1) {
        for (int j: a)
          assertNotEquals(j, el);
      } else {
        assertEquals(a[i], el);
      }
    }
  }

  @Test
  public void testRandom() throws Exception {
    for (int _ = 0; _ < 1000; _ += 1) {
      int maxNumber = (int) (Math.random() * 100);
      int[] a = Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      Searchable.IntArray searchable = Searchable.searchableArray(a);
      int el = (int) (maxNumber * Math.random());
      int i = searchable.indexOf(el);
      if (i == -1) {
        for (int j: a)
          assertNotEquals(j, el);
      } else {
        assertEquals(a[i], el);
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrictFail() throws Exception {
    int[] ints = Util.randomNumbers(100, 105);
    Searchable.strictSearchable().array(ints);
  }

}