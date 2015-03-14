package com.github.methylene.sym;

import static com.github.methylene.sym.Searchable.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

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
      int i = Searchable.asList(a).indexOf(el);
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
      int i = asList(MyInt.COMP, a).indexOf(el);
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
      Searchable.IntList searchable = Searchable.uniqueLists().newList(a);
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
      Searchable.IntList searchable = Searchable.asList(a);
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
    Searchable.uniqueLists().newList(ints);
  }

  @Test(expected = NullPointerException.class)
  public void testNullComparable() throws Exception {
    Searchable.asList(new String[]{"a", null});
  }

  @Test
  public void testAllowNull() throws Exception {
    MyInt[] boxes = MyInt.box(Util.randomNumbers(100, 1000));
    boxes[181] = null;
    boxes[278] = null;
    Searchable.ObjectList list = Searchable.allowNull().newList(MyInt.NULL_FRIENDLY_COMP, boxes);
    assertEquals(181, list.indexOf(null));
  }

  @Test(expected = NullPointerException.class)
  public void testDisallowNull() throws Exception {
    Searchable.asList(1, 2, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAllowNullForbidDuplicates() throws Exception {
    Searchable searchable = new Searchable(PermutationFactory.builder()
        .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL)
        .setUniquenessConstraint(PermutationFactory.UniquenessConstraint.FORBID_DUPLICATES).build());
    int size = 100;
    MyInt[] ints = MyInt.box(Util.distinctInts(size, 4));
    int rand = Util.randomNumbers(size / 2, 1)[0];
    ints[rand] = null;
    ints[2 * rand] = null;
    searchable.newList(MyInt.NULL_FRIENDLY_COMP, ints);
  }

  @Test
  public void testAllowNullForbidDuplicatesSuccess() throws Exception {
    Searchable searchable = new Searchable(PermutationFactory.builder()
        .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL)
        .setUniquenessConstraint(PermutationFactory.UniquenessConstraint.FORBID_DUPLICATES).build());
    int size = 100;
    MyInt[] ints = MyInt.box(Util.distinctInts(size, 4));
    int rand = Util.randomNumbers(size / 2, 1)[0];
    ints[rand] = null; // one null is fine
    assertEquals(rand, searchable.newList(MyInt.NULL_FRIENDLY_COMP, ints).indexOf(null));
  }

  @Test
  public void testReadme() {
    String string = "An array with an .indexOf method.";
    byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
    List<Byte> a = Searchable.asList(bytes);
    assertEquals(17, a.indexOf((byte) '.'));
  }

}