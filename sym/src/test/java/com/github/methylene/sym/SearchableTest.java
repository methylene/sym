package com.github.methylene.sym;

import static com.github.methylene.sym.Searchable.searchableArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import java.nio.charset.Charset;

public class SearchableTest {

  /**
   * This test asserts that indexOf always returns the lowest possible index, if there is a choice.
   * That is, if
   * <pre><code>
   *   int i = searchableArray(a).indexOf(el);
   * </code></pre>
   * and
   * <pre><code>
   *   a[j] == el
   * </code></pre>
   * then
   * <pre><code>
   *   i <= j
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
      int i = searchableArray(a).indexOf(el);
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
      int i = searchableArray(a, MyInt.COMP).indexOf(el);
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
      if (i == -1)
        for (int j : a)
          assertNotEquals(j, el);
      else
        assertEquals(a[i], el);
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
        for (int j : a)
          assertNotEquals(j, el);
      } else {
        assertEquals(a[i], el);
        for (int j = 0; j < i; j += 1)
          assertNotEquals(a[j], el);
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrictFail() throws Exception {
    int[] ints = Util.randomNumbers(100, 105);
    Searchable.strictSearchable().array(ints);
  }

  @Test
  public void testReadme() {
    String string = "An array with an .indexOf method.";
    byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
    Searchable.ByteArray a = Searchable.searchableArray(bytes);
    assertEquals(17, a.indexOf((byte) '.'));
  }

}