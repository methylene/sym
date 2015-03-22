package com.github.methylene.lists;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.github.methylene.sym.Permutation;
import static com.github.methylene.sym.Permutation.perm;
import com.github.methylene.sym.Rankings;
import com.github.methylene.sym.TestUtil;
import com.github.methylene.sym.Util;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListsTest {

  /**
   * This test asserts that indexOf returns the lowest possible index.
   * That is, if
   * <pre><code>
   *   int i = Lists.asList(a).indexOf(el);
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
  public void testIndexOf() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      int[] a = Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] pair = TestUtil.duplicateIndexes(a);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      int el = a[pair[0]];
      int i = LookupList.asList(a).indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  /**
   * This test asserts that lastIndexOf returns the greatest possible index.
   * That is, if
   * <pre><code>
   *   int i = LookupList.asList(a).lastIndexOf(el);
   * </code></pre>
   * and
   * <pre><code>
   *   a[j] == el
   * </code></pre>
   * then
   * <pre><code>
   *   i >= j
   * </code></pre>
   * @throws Exception if the assertion is wrong
   */
  @Test
  public void testLastIndexOf() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      int[] a = Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] pair = TestUtil.duplicateIndexes(a);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      int el = a[pair[0]];
      int i = LookupList.asList(a).lastIndexOf(el);
      assertEquals(a[i], el);
      for (int j = i + 1; j < a.length; j += 1)
        assertNotEquals(a[j], el);
    }
  }


  /**
   * Same as {@link ListsTest#testIndexOf} but for object array
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
      int i = LookupList.asList(MyInt.COMP, a).indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }


  /**
   * Same as {@link ListsTest#testLastIndexOf} but for object array
   * @throws Exception
   */
  @Test
  public void testLastComparator() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      MyInt[] a = MyInt.box(Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      int[] pair = TestUtil.duplicateIndexes(a, MyInt.COMP);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      MyInt el = a[pair[0]];
      int i = LookupList.asList(MyInt.COMP, a).lastIndexOf(el);
      assertEquals(a[i], el);
      for (int j = i + 1; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  @Test
  public void testRandom() throws Exception {
    for (int _ = 0; _ < 1000; _ += 1) {
      int maxNumber = (int) (Math.random() * 100);
      int[] a = Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      LookupList<Integer> searchable = LookupList.asList(a);
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


  @Test(expected = NullPointerException.class)
  public void testNullComparable() throws Exception {
    LookupList.asList("a", null);
  }


  @Test(expected = NullPointerException.class)
  public void testNull() throws Exception {
    LookupList.asList(1, 2, null);
  }


  @Test
  public void testReadme() {
    String string = "An array with an .indexOf method.";
    byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
    LookupList<Byte> a = LookupList.asList(bytes);
    assertEquals(17, a.indexOf((byte) '.'));
  }

  /* Make sure that changes to the array do not "write through" */
  @Test
  public void testModify() {
    int[] a = {1, 2, 3};
    LookupList<Integer> integers = LookupList.asList(a);
    a[0] = 5;
    assertEquals(1, integers.get(0).intValue());
  }

  @Test
  public void testSubList() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 100;
      int[] a = Util.randomNumbers(1000, size);
      LookupList<Integer> lookupList = LookupList.asList(a);
      ArrayList<Integer> jdk = new ArrayList<Integer>(a.length);
      Collections.addAll(jdk, Util.box(a));
      int from = (int) ((size / 2) * Math.random());
      int to = from + (int) ((size / 2) * Math.random());
      assertEquals(jdk.subList(from, to), lookupList.subList(from, to));
    }
  }

  @Test
  public void testIndexesOf() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] zeros = {0, 1, 6, 7};
    assertArrayEquals(zeros, LookupList.asList(a).indexOf(0, -1));
  }

  @Test
  public void testIndexesOf2() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 10;
      int maxNumber = 4;
      int[] a = Util.randomNumbers(maxNumber, size);
      LookupList<Integer> lookupList = LookupList.asList(a);
      int el = (int) (Math.random() * maxNumber);
      int[] els = lookupList.indexOf(el, -1);
      assertTrue(Util.isSorted(els));
      for (int i = 0; i < a.length; i += 1) {
        if (a[i] == el) {
          String msg = String.format("%d, %s", a[i], Arrays.toString(els));
          assertTrue(msg, Arrays.binarySearch(els, i) >= 0);
        } else {
          String msg = String.format("%d, %s, %s", a[i], Arrays.toString(els), Arrays.toString(a));
          assertTrue(msg, Arrays.binarySearch(els, i) < 0);
        }
      }
    }
  }

  @Test
  public void testIndexOfLimit() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 1000;
      int maxNumber = 100;
      int[] a = Util.randomNumbers(maxNumber, size);
      IntList lookupList = (IntList) LookupList.asList(a);
      int el = (int) (Math.random() * maxNumber);
      int limit = (int) (10 * Math.random());
      int[] els = lookupList.indexOf(el, limit);
      assertTrue(els.length <= limit);
      assertTrue(Util.isSorted(els));
      int seen = 0;
      for (int i = 0; i < a.length; i += 1) {
        if (a[i] == el) {
          if (seen < limit) {
            assertTrue(Arrays.binarySearch(els, i) >= 0);
          } else {
            assertTrue(Arrays.binarySearch(els, i) < 0);
          }
          seen++;
        } else {
          assertTrue(Arrays.binarySearch(els, i) < 0);
        }
      }
    }
  }

  @Test
  public void testIndexesOfComparable() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 1000;
      int maxNumber = 100;
      Integer[] a = Util.box(Util.randomNumbers(maxNumber, size));
      LookupList<Integer> lookupList = LookupList.asList(a);
      Integer el = (int) (Math.random() * maxNumber);
      int[] els = lookupList.indexOf(el, -1);
      assertTrue(Util.isSorted(els));
      for (int i = 0; i < a.length; i += 1) {
        if (a[i].equals(el)) {
          assertTrue(Arrays.binarySearch(els, i) >= 0);
        } else {
          assertTrue(Arrays.binarySearch(els, i) < 0);
        }
      }
    }
  }

  @Test
  public void testIndexesOfComparator() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 1000;
      int maxNumber = 100;
      MyInt[] a = MyInt.box(Util.randomNumbers(maxNumber, size));
      LookupList<MyInt> lookupList = LookupList.asList(MyInt.COMP, a);
      MyInt el = new MyInt((int) (Math.random() * maxNumber));
      int[] els = lookupList.indexOf(el, -1);
      assertTrue(Util.isSorted(els));
      for (int i = 0; i < a.length; i += 1) {
        if (a[i].equals(el)) {
          assertTrue(Arrays.binarySearch(els, i) >= 0);
        } else {
          assertTrue(Arrays.binarySearch(els, i) < 0);
        }
      }
    }
  }

  public <E> int[] findDuplicate(LookupList<E> list) {
    for (int i = 0; i < list.size(); i++) {
      int[] indexes = list.indexOf(list.get(i), 2);
      if (indexes.length == 2)
        return indexes;
    }
    // all elements unique
    return null;
  }

  int count(int[] a, int i) {
    int cnt = 0;
    for (int x : a) if (x == i) cnt++;
    return cnt;
  }

  int count(Object[] a, Object i) {
    int cnt = 0;
    for (Object x : a) if (x.equals(i)) cnt++;
    return cnt;
  }

  @Test
  public void testFindDuplicate() {
    for (int _ = 0; _ < 100; _++) {
      int[] a = Util.randomNumbers(100, 50);
      int[] duplicate = findDuplicate(LookupList.asList(a));
      if (duplicate == null) {
        for (int i : a)
          assertEquals(0, count(a, i));
      } else {
        assertTrue(count(a, a[duplicate[0]]) > 1);
      }
    }
  }

  @Test
  public void testFindDuplicateComparable() {
    for (int _ = 0; _ < 100; _++) {
      Integer[] a = Util.box(Util.randomNumbers(100, 50));
      int[] duplicate = findDuplicate(LookupList.asList(a));
      if (duplicate == null) {
        for (Integer i : a)
          assertEquals(0, count(a, i));
      } else {
        assertTrue(count(a, a[duplicate[0]]) > 1);
      }
    }
  }

  @Test
  public void testFindDuplicateComparator() {
    for (int _ = 0; _ < 100; _++) {
      MyInt[] a = MyInt.box(Util.randomNumbers(100, 50));
      int[] duplicate = findDuplicate(LookupList.asList(MyInt.COMP, a));
      if (duplicate == null) {
        for (MyInt i : a)
          assertEquals(0, count(a, i));
      } else {
        assertTrue(count(a, a[duplicate[0]]) > 1);
      }
    }
  }

  @Test
  public void testPartitions() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] part_0 = {0, 1, 6, 7};
    int[] part_1 = {3, 4, 5, 9};
    int[] part_2 = {8};
    int[] part_3 = {2};
    IntList list = (IntList) LookupList.asList(a);
    assertArrayEquals(part_0, list.group().get(0));
    assertArrayEquals(part_1, list.group().get(1));
    assertArrayEquals(part_2, list.group().get(2));
    assertArrayEquals(part_3, list.group().get(3));
    assertNull(list.group().get(4));
  }

  @Test
  public void testPartitions2() {
    for (int _ = 0; _ < 100; _++) {
      int size = 100;
      int[] a = Util.randomNumbers(1000, size);
      IntList lookupList = (IntList) LookupList.asList(a);
      int idx = (int) (Math.random() * size);
      assertArrayEquals(lookupList.indexOf(a[idx], -1), lookupList.group().get(a[idx]));
    }
  }

  @Test
  public void testPartitionsBoxed() {
    for (int _ = 0; _ < 100; _++) {
      int size = 100;
      MyInt[] a = MyInt.box(Util.randomNumbers(1000, size));
      LookupList<MyInt> lookupList = LookupList.asList(MyInt.COMP, a);
      int idx = (int) (Math.random() * size);
      assertArrayEquals(lookupList.indexOf(a[idx], -1), lookupList.group().get(a[idx]));
    }
  }

  @Test
  public void testUnique() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] sorted = {0, 0, 0, 0, 1, 1, 1, 1, 2, 3};
    int[] uniqued = {0, 1, 2, 3};
    IntList list = (IntList) LookupList.asList(a);
    assertArrayEquals(Util.box(sorted), list.sort().toArray(new Integer[1]));
    assertArrayEquals(Util.box(uniqued), list.sortUnique().toArray(new Integer[1]));
  }

  @Test
  public void testUnique2() {
    for (int _ = 0; _ < 100; _++) {
      int size = 1000;
      int[] a = Util.randomNumbers(100, size);
      LookupList<Integer> lookupList = LookupList.asList(a);
      assertTrue(Util.isSorted(lookupList.sort()));
      List<Integer> unique = lookupList.sortUnique();
      assertTrue(Util.isSorted(unique));
      Set<Integer> set = new HashSet<Integer>(lookupList.size());
      for (Integer i : unique)
        assertTrue(set.add(i));
    }
  }

  @Test
  public void testUnique3() {
    for (int _ = 0; _ < 100; _++) {
      int size = 1000;
      int[] a = Util.randomNumbers(100, size);
      LookupList<MyInt> lookupList = LookupList.asList(MyInt.COMP, MyInt.box(a));
      assertTrue(Util.isSorted(MyInt.COMP, lookupList.sort()));
      List<MyInt> unique = lookupList.sortUnique();
      assertTrue(Util.isSorted(MyInt.COMP, unique));
      HashSet<MyInt> set = new HashSet<MyInt>(lookupList.size());
      for (MyInt i : unique)
        assertTrue(set.add(i));
    }
  }

  @Test
  public void testUniqueBox() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] sorted = {0, 0, 0, 0, 1, 1, 1, 1, 2, 3};
    int[] uniqued = {0, 1, 2, 3};
    LookupList<Integer> list = LookupList.asList(Util.box(a));
    assertArrayEquals(Util.box(sorted), list.sort().toArray(new Integer[1]));
    assertArrayEquals(Util.box(uniqued), list.sortUnique().toArray(new Integer[1]));
  }

  @Test
  public void testUniqueBox2() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] sorted = {0, 0, 0, 0, 1, 1, 1, 1, 2, 3};
    int[] uniqued = {0, 1, 2, 3};
    LookupList<MyInt> list = LookupList.asList(MyInt.COMP, MyInt.box(a));
    assertArrayEquals(MyInt.box(sorted), list.sort().toArray(new MyInt[1]));
    assertArrayEquals(MyInt.box(uniqued), list.sortUnique().toArray(new MyInt[1]));
  }

  @Test
  public void testShuffle() {
    int[] a = Util.randomNumbers(20, 10);
    IntList list = (IntList) LookupList.asList(a);
    Permutation p = Permutation.random(a.length);
    int[] ranking = p.getRanking();
    Integer[] expected = Util.box(Rankings.apply(ranking, a));
    Integer[] actuals = list.shuffle(p).toArray(new Integer[a.length]);
    assertArrayEquals(expected, actuals);
  }

  @Test
  public void testIndexOfShuffle() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      int[] b = Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] ranking = Rankings.random(b.length);
      int[] a = Rankings.apply(ranking, b);
      int el = a[TestUtil.duplicateIndexes(a)[0]];
      IntList list = ((IntList) LookupList.asList(b)).shuffle(perm(ranking));
      int i = list.indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  @Test
  public void testIndexOfShuffleUnique() throws Exception {
    for (int _ = 0; _ < 10000; _ += 1) {
      int[] b = Util.distinctInts(100, 2);
      int[] ranking = Rankings.random(b.length);
      int[] a = Rankings.apply(ranking, b);
      int el = a[((int) (Math.random() * b.length))];
      IntList list = ((IntList) LookupList.asList(b)).shuffle(perm(ranking));
      int i = list.indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }


}
