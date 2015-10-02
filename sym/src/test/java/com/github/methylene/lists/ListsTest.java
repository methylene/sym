package com.github.methylene.lists;

import com.github.methylene.sym.Permutation;
import static com.github.methylene.sym.Permutation.define;
import static com.github.methylene.lists.LookupList.*;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.*;

import com.github.methylene.sym.Rankings;
import com.github.methylene.sym.TestUtil;
import com.github.methylene.sym.ArrayUtil;
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
   *   int i = Lists.of(a).indexOf(el);
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
    for (int __ = 0; __ < 10000; __ += 1) {
      int maxNumber = 10;
      int[] a = ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] pair = TestUtil.duplicateIndexes(a);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      int el = a[pair[0]];
      int i = of(a).indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  /**
   * This test asserts that lastIndexOf returns the greatest possible index.
   * That is, if
   * <pre><code>
   *   int i = LookupList.of(a).lastIndexOf(el);
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
    for (int __ = 0; __ < 10000; __ += 1) {
      int maxNumber = 10;
      int[] a = ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] pair = TestUtil.duplicateIndexes(a);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      int el = a[pair[0]];
      int i = of(a).lastIndexOf(el);
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
    for (int __ = 0; __ < 10000; __ += 1) {
      int maxNumber = 10;
      MyInt[] a = MyInt.box(ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      int[] pair = TestUtil.duplicateIndexes(a, MyInt.COMP);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      MyInt el = a[pair[0]];
      int i = copyOf(MyInt.COMP, a).indexOf(el);
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
    for (int __ = 0; __ < 10000; __ += 1) {
      int maxNumber = 10;
      MyInt[] a = MyInt.box(ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      int[] pair = TestUtil.duplicateIndexes(a, MyInt.COMP);
      assertNotEquals(pair[0], pair[1]);
      assertEquals(a[pair[0]], a[pair[1]]);
      MyInt el = a[pair[0]];
      int i = copyOf(MyInt.COMP, a).lastIndexOf(el);
      assertEquals(a[i], el);
      for (int j = i + 1; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  @Test
  public void testRandom() throws Exception {
    for (int __ = 0; __ < 1000; __ += 1) {
      int maxNumber = (int) (Math.random() * 100);
      int[] a = ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      LookupList<Integer> searchable = of(a);
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
    of("a", null);
  }


  @Test(expected = NullPointerException.class)
  public void testNull() throws Exception {
    of(1, 2, null);
  }


  @Test
  public void testReadme() {
    String string = "An array with an .indexOf method.";
    byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
    LookupList<Byte> a = of(bytes);
    assertEquals(17, a.indexOf((byte) '.'));
  }

  /* Make sure that changes to the array do not "write through" */
  @Test
  public void testModify() {
    int[] a = {1, 2, 3};
    LookupList<Integer> integers = of(a);
    a[0] = 5;
    assertEquals(1, integers.get(0).intValue());
  }

  @Test
  public void testSubList() {
    for (int __ = 0; __ < 100; __ += 1) {
      int size = 100;
      int[] a = ArrayUtil.randomNumbers(1000, size);
      LookupList<Integer> lookupList = of(a);
      ArrayList<Integer> jdk = new ArrayList<Integer>(a.length);
      Collections.addAll(jdk, ArrayUtil.box(a));
      int from = (int) ((size / 2) * Math.random());
      int to = from + (int) ((size / 2) * Math.random());
      assertEquals(jdk.subList(from, to), lookupList.subList(from, to));
    }
  }

  @Test
  public void testIndexesOf() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] zeros = {0, 1, 6, 7};
    assertArrayEquals(zeros, LookupList.of(a).indexOf(0, -1));
  }

  @Test
  public void testIndexesOf2() {
    for (int __ = 0; __ < 100; __ += 1) {
      int size = 10;
      int maxNumber = 4;
      int[] a = ArrayUtil.randomNumbers(maxNumber, size);
      LookupList<Integer> lookupList = of(a);
      int el = (int) (Math.random() * maxNumber);
      int[] els = lookupList.indexOf(el, -1);
      assertTrue(ArrayUtil.isSorted(els));
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
    for (int __ = 0; __ < 100; __ += 1) {
      int size = 1000;
      int maxNumber = 100;
      int[] a = ArrayUtil.randomNumbers(maxNumber, size);
      IntList lookupList = (IntList) of(a);
      int el = (int) (Math.random() * maxNumber);
      int limit = (int) (10 * Math.random());
      int[] els = lookupList.indexOf(el, limit);
      assertTrue(els.length <= limit);
      assertTrue(ArrayUtil.isSorted(els));
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
    for (int __ = 0; __ < 100; __ += 1) {
      int size = 1000;
      int maxNumber = 100;
      Integer[] a = ArrayUtil.box(ArrayUtil.randomNumbers(maxNumber, size));
      LookupList<Integer> lookupList = copyOf(a);
      Integer el = (int) (Math.random() * maxNumber);
      int[] els = lookupList.indexOf(el, -1);
      assertTrue(ArrayUtil.isSorted(els));
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
    for (int __ = 0; __ < 100; __ += 1) {
      int size = 1000;
      int maxNumber = 100;
      MyInt[] a = MyInt.box(ArrayUtil.randomNumbers(maxNumber, size));
      LookupList<MyInt> lookupList = copyOf(MyInt.COMP, a);
      MyInt el = new MyInt((int) (Math.random() * maxNumber));
      int[] els = lookupList.indexOf(el, -1);
      assertTrue(ArrayUtil.isSorted(els));
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
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.randomNumbers(100, 50);
      int[] duplicate = findDuplicate(of(a));
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
    for (int __ = 0; __ < 100; __++) {
      Integer[] a = ArrayUtil.box(ArrayUtil.randomNumbers(100, 50));
      int[] duplicate = findDuplicate(copyOf(a));
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
    for (int __ = 0; __ < 100; __++) {
      MyInt[] a = MyInt.box(ArrayUtil.randomNumbers(100, 50));
      int[] duplicate = findDuplicate(copyOf(MyInt.COMP, a));
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
    IntList list = (IntList) of(a);
    assertArrayEquals(part_0, list.group().get(0));
    assertArrayEquals(part_1, list.group().get(1));
    assertArrayEquals(part_2, list.group().get(2));
    assertArrayEquals(part_3, list.group().get(3));
    assertNull(list.group().get(4));
  }

  @Test
  public void testPartitions2() {
    for (int __ = 0; __ < 100; __++) {
      int size = 100;
      int[] a = ArrayUtil.randomNumbers(1000, size);
      IntList lookupList = (IntList) of(a);
      int idx = (int) (Math.random() * size);
      assertArrayEquals(lookupList.indexOf(a[idx], -1), lookupList.group().get(a[idx]));
    }
  }

  @Test
  public void testPartitionsBoxed() {
    for (int __ = 0; __ < 100; __++) {
      int size = 100;
      MyInt[] a = MyInt.box(ArrayUtil.randomNumbers(1000, size));
      LookupList<MyInt> lookupList = copyOf(MyInt.COMP, a);
      int idx = (int) (Math.random() * size);
      assertArrayEquals(lookupList.indexOf(a[idx], -1), lookupList.group().get(a[idx]));
    }
  }

  @Test
  public void testUnique() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] sorted = {0, 0, 0, 0, 1, 1, 1, 1, 2, 3};
    int[] uniqued = {0, 1, 2, 3};
    IntList list = (IntList) of(a);
    assertArrayEquals(ArrayUtil.box(sorted), list.sort().toArray(new Integer[1]));
    assertArrayEquals(ArrayUtil.box(uniqued), list.sortUnique().toArray(new Integer[1]));
  }

  @Test
  public void testUnique2() {
    for (int __ = 0; __ < 100; __++) {
      int size = 1000;
      int[] a = ArrayUtil.randomNumbers(100, size);
      LookupList<Integer> lookupList = of(a);
      assertTrue(ArrayUtil.isSorted(lookupList.sort()));
      List<Integer> unique = lookupList.sortUnique();
      assertTrue(ArrayUtil.isSorted(unique));
      Set<Integer> set = new HashSet<Integer>(lookupList.size());
      for (Integer i : unique)
        assertTrue(set.add(i));
    }
  }

  @Test
  public void testUnique3() {
    for (int __ = 0; __ < 100; __++) {
      int size = 1000;
      int[] a = ArrayUtil.randomNumbers(100, size);
      LookupList<MyInt> lookupList = copyOf(MyInt.COMP, MyInt.box(a));
      assertTrue(ArrayUtil.isSorted(MyInt.COMP, lookupList.sort()));
      List<MyInt> unique = lookupList.sortUnique();
      assertTrue(ArrayUtil.isSorted(MyInt.COMP, unique));
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
    LookupList<Integer> list = copyOf(ArrayUtil.box(a));
    assertArrayEquals(ArrayUtil.box(sorted), list.sort().toArray(new Integer[1]));
    assertArrayEquals(ArrayUtil.box(uniqued), list.sortUnique().toArray(new Integer[1]));
  }

  @Test
  public void testUniqueBox2() {
    int[] a = {0, 0, 3, 1, 1, 1, 0, 0, 2, 1};
    int[] sorted = {0, 0, 0, 0, 1, 1, 1, 1, 2, 3};
    int[] uniqued = {0, 1, 2, 3};
    LookupList<MyInt> list = copyOf(MyInt.COMP, MyInt.box(a));
    assertArrayEquals(MyInt.box(sorted), list.sort().toArray(new MyInt[1]));
    assertArrayEquals(MyInt.box(uniqued), list.sortUnique().toArray(new MyInt[1]));
  }

  @Test
  public void testShuffle() {
    int[] a = ArrayUtil.randomNumbers(20, 10);
    IntList list = (IntList) of(a);
    Permutation p = Permutation.random(a.length);
    int[] ranking = p.getRanking();
    Integer[] expected = ArrayUtil.box(Rankings.apply(ranking, a));
    Integer[] actuals = list.shuffle(p).toArray(new Integer[a.length]);
    assertArrayEquals(expected, actuals);
  }

  @Test
  public void testIndexOfShuffle() throws Exception {
    for (int __ = 0; __ < 10000; __ += 1) {
      int maxNumber = 10;
      int[] b = ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] ranking = Rankings.random(b.length);
      int[] a = Rankings.apply(ranking, b);
      int el = a[TestUtil.duplicateIndexes(a)[0]];
      IntList list = ((IntList) of(b)).shuffle(define(ranking));
      int i = list.indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  @Test
  public void testIndexOfShuffleUnique() throws Exception {
    for (int __ = 0; __ < 10000; __ += 1) {
      int[] b = Rankings.random(100);
      int[] ranking = Rankings.random(b.length);
      int[] a = Rankings.apply(ranking, b);
      int el = a[((int) (Math.random() * b.length))];
      IntList list = ((IntList) of(b)).shuffle(define(ranking));
      int i = list.indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  @Test
  public void testEmpty() {
    LookupList<Comparable> empty = of();
    LookupList<Comparable> shuffled = empty.shuffle(Permutation.identity());
    assertTrue(empty.isEmpty());
    assertTrue(shuffled.isEmpty());
    assertTrue(empty.group().isEmpty());
    assertTrue(shuffled.group().isEmpty());
    assertTrue(shuffled.isSorted());
    assertTrue(shuffled.isUnique());
    assertEquals(empty, shuffled);
    assertEquals(-1, shuffled.indexOf("a"));
    assertArrayEquals(new int[]{}, shuffled.indexOf("a", 0));
    assertArrayEquals(new int[]{}, shuffled.indexOf("a", 1));
    assertArrayEquals(new int[]{}, shuffled.indexOf("a", -1));
    assertEquals(EmptyLookupList.class, of().getClass());
  }

  @Test
  public void testSingleton() {
    assertEquals(singletonList(1), of(1));
    assertEquals(singletonList(1), of(1).subList(0, 1));
    assertFalse(of(1).isEmpty());
    assertTrue(of(1).isSorted());
    assertTrue(of(1).isUnique());
    assertEquals(singletonMap(1, new int[]{0}).keySet(), of(1).group().keySet());
    assertEquals(0, of(1).indexOf(1));
    assertEquals(-1, of(1).indexOf(2));
    assertArrayEquals(new int[]{}, of(1).indexOf(1, 0));
    assertArrayEquals(new int[]{0}, of(1).indexOf(1, 1));
    assertArrayEquals(new int[]{0}, of(1).indexOf(1, -1));
    assertArrayEquals(new int[]{}, of(1).indexOf(2, 0));
    assertArrayEquals(new int[]{}, of(1).indexOf(2, 1));
    assertArrayEquals(new int[]{}, of(1).indexOf(2, -1));
    assertEquals(SingleElementLookupList.class, of(1).getClass());
  }

}
