package com.github.methylene.lists;

import static com.github.methylene.lists.Lists.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.github.methylene.sym.Util;
import com.github.methylene.sym.PermutationFactory;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
      int i = Lists.asList(a).indexOf(el);
      assertEquals(a[i], el);
      for (int j = 0; j < i; j += 1)
        assertNotEquals(a[j], el);
    }
  }

  /**
   * This test asserts that lastIndexOf returns the greatest possible index.
   * That is, if
   * <pre><code>
   *   int i = Lists.asList(a).lastIndexOf(el);
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
      int i = Lists.asList(a).lastIndexOf(el);
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
      int i = asList(MyInt.COMP, a).indexOf(el);
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
      int i = asList(MyInt.COMP, a).lastIndexOf(el);
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
      LookupList<Integer> searchable = Lists.asList(a);
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
    Lists.asList("a", null);
  }


  @Test(expected = NullPointerException.class)
  public void testNull() throws Exception {
    Lists.asList(1, 2, null);
  }


  @Test
  public void testReadme() {
    String string = "An array with an .indexOf method.";
    byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
    LookupList<Byte> a = Lists.asList(bytes);
    assertEquals(17, a.indexOf((byte) '.'));
  }

  @Test
  public void testExtendedCapacity() {
    int safe = Integer.MAX_VALUE / 3;
    for (int _ = 0; _ < 10000; _ += 1) {
      int oldCapacity = (int) (Math.random() * Integer.MAX_VALUE);
      int minCapacity = oldCapacity + (int) (Math.random() * (Integer.MAX_VALUE - oldCapacity));
      int extended = Lists.Builder.extendedCapacity(oldCapacity, minCapacity);
      assertTrue(extended >= minCapacity);
      if (minCapacity < safe) {
        assertTrue(extended < minCapacity * 3);
      }
    }
  }

  @Test
  public void testBuilder() {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      Integer[] a = Util.box(Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      List<Integer> asList = Lists.asList(a);
      List<Integer> addAll = Lists.<Integer>builder().addAll(a).build();
      List<Integer> jdk = Arrays.asList(a);
      assertEquals(jdk, addAll);
      assertEquals(jdk, asList);
    }
  }

  private long minLength(long... numbers) {
    long result = 0;
    for (long i: numbers)
      result = Math.max(Long.toString(i).length(), result);
    return result;
  }

  @Test
  public void testPerf() {
    long index1 = 0;
    long index2 = 0;
    long index3 = 0;
    long lastIndex = 0;
    long indexJdk1 = 0;
    long indexJdk2 = 0;
    long lastIndexJdk = 0;
    long indexSum = 0;
    int repeat = 16;
    System.out.println("running performance tests...");
    int size = 16384; // knob to turn
    for (int _ = 0; _ < repeat; _ += 1) {
      int maxNumber = 60000;
      int[] a = Util.randomNumbers(maxNumber, size);
      List<Integer> asList = Lists.asList(a);
      List<Integer> jdk = Arrays.asList(Util.box(a));
      Integer candidate = (int) (Math.random() * maxNumber);
      long check, time;
      int i, j;
      if (_ % 2 == 0) {
        check = System.nanoTime();
        i = asList.indexOf(candidate);
        time = System.nanoTime();
        index1 += (time - check);
        check = System.nanoTime();
        j = jdk.indexOf(candidate);
        time = System.nanoTime();
        indexJdk1 += (time - check);
        check = System.nanoTime();
        j = jdk.indexOf(candidate);
        time = System.nanoTime();
        indexJdk2 += (time - check);
        assertEquals(j, i);
        indexSum += i < 0 ? size : i;
        check = System.nanoTime();
        i = asList.lastIndexOf(candidate);
        time = System.nanoTime();
        lastIndex += (time - check);
        check = System.nanoTime();
        j = jdk.lastIndexOf(candidate);
        time = System.nanoTime();
        lastIndexJdk += (time - check);
        assertEquals(j, i);
        check = System.nanoTime();
        i = asList.indexOf(candidate);
        time = System.nanoTime();
        index2 += (time - check);
        i = asList.indexOf((int) (Math.random() * maxNumber));
        time = System.nanoTime();
        index3 += (time - check);
      } else { // same tests in different order
        check = System.nanoTime();
        j = jdk.indexOf(candidate);
        time = System.nanoTime();
        indexJdk1 += (time - check);
        check = System.nanoTime();
        j = jdk.indexOf(candidate);
        time = System.nanoTime();
        indexJdk2 += (time - check);
        check = System.nanoTime();
        i = asList.indexOf(candidate);
        time = System.nanoTime();
        index1 += (time - check);
        assertEquals(j, i);
        indexSum += i < 0 ? size : i;
        check = System.nanoTime();
        i = asList.indexOf(candidate);
        time = System.nanoTime();
        index2 += (time - check);
        i = asList.indexOf((int) (Math.random() * maxNumber));
        time = System.nanoTime();
        index3 += (time - check);
        check = System.nanoTime();
        j = jdk.lastIndexOf(candidate);
        time = System.nanoTime();
        lastIndexJdk += (time - check);
        check = System.nanoTime();
        i = asList.lastIndexOf(candidate);
        time = System.nanoTime();
        lastIndex += (time - check);
        assertEquals(j, i);
      }
    }
    // format and print
    long d = Math.max(6, minLength(index1, index2, index3, indexJdk1, indexJdk2, lastIndex, lastIndexJdk));
    String speedup = Float.toString(((index1 + index2 + index3) / 3f) / ((indexJdk1 + indexJdk2) / 2f));
    if (speedup.length() > d) {speedup = speedup.substring(0, Long.valueOf(d).intValue());}
    String lastIndexSpeedup = Float.toString(((float) lastIndex) / ((float) lastIndexJdk));
    if (lastIndexSpeedup.length() > d) {lastIndexSpeedup = lastIndexSpeedup.substring(0, Long.valueOf(d).intValue());}
    PrintStream out = System.out;
    out.println("== list size: " + size);
    out.println("== avg return value of .indexOf: " + indexSum / repeat);
    out.format("index_1:            %" + d + "d%n", index1);
    out.format("index_2:            %" + d + "d%n", index2);
    out.format("index_3:            %" + d + "d%n", index3);
    out.format("index_jdk_1:        %" + d + "d%n", indexJdk1);
    out.format("index_jdk_2:        %" + d + "d%n", indexJdk2);
    out.format("lastIndex:          %" + d + "d%n", lastIndex);
    out.format("lastIndexJdk:       %" + d + "d%n", lastIndexJdk);
    out.format("index_relative:     %1$" + d + "s %%%n", speedup);
    out.format("lastIndex_relative: %1$" + d + "s %%%n", lastIndexSpeedup);
  }


  /* Make sure that changes to the array do not "write through" */
  @Test
  public void testModify() {
    int[] a = {1, 2, 3};
    LookupList<Integer> integers = Lists.asList(a);
    a[0] = 5;
    assertEquals(1, integers.get(0).intValue());
  }

  @Test
  public void testSubList() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 100;
      int[] a = Util.randomNumbers(1000, size);
      LookupList<Integer> lookupList = Lists.asList(a);
      ArrayList<Integer> jdk = new ArrayList<Integer>(a.length);
      Collections.addAll(jdk, Util.box(a));
      int from = (int) ((size / 2) * Math.random());
      int to = from + (int) ((size / 2) * Math.random());
      assertEquals(jdk.subList(from, to), lookupList.subList(from, to));
    }
  }

  @Test
  public void testIndexesOf() {
    for (int _ = 0; _ < 100; _ += 1) {
      int size = 1000;
      int maxNumber = 100;
      int[] a = Util.randomNumbers(maxNumber, size);
      LookupList<Integer> lookupList = Lists.asList(a);
      int el = (int) (Math.random() * maxNumber);
      int[] els = lookupList.indexesOf(el);
      assertTrue(Util.isSorted(els));
      for (int i = 0; i < a.length; i += 1) {
        if(a[i] == el) {
          assertTrue(Arrays.binarySearch(els, i) >= 0);
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
      ComparableList<Integer> lookupList = Lists.asList(a);
      Integer el = (int) (Math.random() * maxNumber);
      int[] els = lookupList.indexesOf(el);
      assertTrue(Util.isSorted(els));
      for (int i = 0; i < a.length; i += 1) {
        if(a[i].equals(el)) {
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
      ComparatorList<MyInt> lookupList = Lists.asList(MyInt.COMP, a);
      MyInt el = new MyInt((int) (Math.random() * maxNumber));
      int[] els = lookupList.indexesOf(el);
      assertTrue(Util.isSorted(els));
      for (int i = 0; i < a.length; i += 1) {
        if(a[i].equals(el)) {
          assertTrue(Arrays.binarySearch(els, i) >= 0);
        } else {
          assertTrue(Arrays.binarySearch(els, i) < 0);
        }
      }
    }
  } 

}
