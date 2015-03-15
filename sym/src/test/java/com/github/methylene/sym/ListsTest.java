package com.github.methylene.sym;

import static com.github.methylene.sym.Lists.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
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
  public void testStrictRandom() throws Exception {
    for (int _ = 0; _ < 1000; _ += 1) {
      int maxNumber = (int) (Math.random() * 100);
      int[] a = Util.distinctInts(maxNumber, (int) (Math.random() * 10 + 2));
      Lists.IntList searchable = Lists.uniqueLists().newList(a);
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
      Lists.IntList searchable = Lists.asList(a);
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
    Lists.uniqueLists().newList(ints);
  }

  @Test(expected = NullPointerException.class)
  public void testNullComparable() throws Exception {
    Lists.asList("a", null);
  }

  @Test
  public void testAllowNull() throws Exception {
    MyInt[] boxes = MyInt.box(Util.randomNumbers(100, 1000));
    boxes[181] = null;
    boxes[278] = null;
    Lists.ComparatorList list = Lists.allowNull().newList(MyInt.NULL_FRIENDLY_COMP, boxes);
    assertEquals(181, list.indexOf(null));
  }

  @Test(expected = NullPointerException.class)
  public void testDisallowNull() throws Exception {
    Lists.asList(1, 2, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAllowNullForbidDuplicates() throws Exception {
    Lists lists = new Lists(PermutationFactory.builder()
        .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL)
        .setUniquenessConstraint(PermutationFactory.UniquenessConstraint.FORBID_DUPLICATES).build());
    int size = 100;
    MyInt[] ints = MyInt.box(Util.distinctInts(size, 4));
    int rand = Util.randomNumbers(size / 2, 1)[0];
    ints[rand] = null;
    ints[2 * rand] = null;
    lists.newList(MyInt.NULL_FRIENDLY_COMP, ints);
  }

  @Test
  public void testAllowNullForbidDuplicatesSuccess() throws Exception {
    Lists lists = new Lists(PermutationFactory.builder()
        .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL)
        .setUniquenessConstraint(PermutationFactory.UniquenessConstraint.FORBID_DUPLICATES).build());
    int size = 100;
    MyInt[] ints = MyInt.box(Util.distinctInts(size, 4));
    int rand = Util.randomNumbers(size / 2, 1)[0];
    ints[rand] = null; // one null is fine
    assertEquals(rand, lists.newList(MyInt.NULL_FRIENDLY_COMP, ints).indexOf(null));
  }

  @Test
  public void testReadme() {
    String string = "An array with an .indexOf method.";
    byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
    List<Byte> a = Lists.asList(bytes);
    assertEquals(17, a.indexOf((byte) '.'));
  }


  @Test
  public void testExtendedCapacity() {
    int safe = Integer.MAX_VALUE / 3;
    for (int _ = 0; _ < 10000; _ += 1) {
      int oldCapacity = (int) (Math.random() * Integer.MAX_VALUE);
      int minCapacity = oldCapacity + (int) (Math.random() * (Integer.MAX_VALUE - oldCapacity));
      int extended = Lists.ListBuilder.extendedCapacity(oldCapacity, minCapacity);
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
      Integer[] a = Util.boxedRandomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
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
    for (int _ = 0; _ < repeat; _ += 1) {
      int maxNumber = 60000;
      int size = 16384; // knob to turn
      Integer[] a = Util.boxedRandomNumbers(maxNumber, size);
      List<Integer> asList = Lists.asList(a);
      List<Integer> jdk = Arrays.asList(a);
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
    long d = minLength(index1, index2, index3, indexJdk1, indexJdk2, lastIndex, lastIndexJdk);
    System.out.println("== avg return value of .indexOf: " + indexSum / repeat + " ==");
    System.out.format("index_1:      %" + d + "d%n", index1);
    System.out.format("index_2:      %" + d + "d%n", index2);
    System.out.format("index_3:      %" + d + "d%n", index3);
    System.out.format("index_jdk_1:  %" + d + "d%n", indexJdk1);
    System.out.format("index_jdk_2:  %" + d + "d%n", indexJdk2);
    System.out.format("lastIndex:    %" + d + "d%n", lastIndex);
    System.out.format("lastIndexJdk: %" + d + "d%n", lastIndexJdk);
  }

  @Test
  public void testBuilderComparatorNull() {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      MyInt[] a = MyInt.box(Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      for (int i = 0; i < (Math.random() * a.length); i += 1)
        a[(int) (Math.random() * a.length)] = null;
      List<MyInt> asList = Lists.allowNull().newList(MyInt.NULL_FRIENDLY_COMP, a);
      List<MyInt> addAll = Lists.allowNull().newBuilder(MyInt.NULL_FRIENDLY_COMP).addAll(a).build();
      List<MyInt> jdk = Arrays.asList(a);
      assertEquals(jdk, addAll);
      assertEquals(jdk, asList);
    }
  }


}
