package com.github.methylene.sym;

import static com.github.methylene.sym.MyInt.box;
import static com.github.methylene.sym.Permutation.*;
import static com.github.methylene.sym.ArrayUtil.randomNumbers;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Legacy tests.
 * All tests in here use the strict factory.
 */
public class PermutationTest {

  /* Check example from constructor javadoc */
  @Test
  public void testAbc() {
    Permutation p = define(1, 2, 0);
    assertArrayEquals(new char[]{'c', 'a', 'b'}, p.apply(new char[]{'a', 'b', 'c'}));
  }

  @Test
  public void testComp() throws Exception {
    Permutation p = Permutation.define(1, 2, 0);
    assertEquals(define(new int[]{1, 2, 0}), p);
    assertArrayEquals(new String[]{"c", "a", "b"}, p.apply(TestUtil.symbols(3)));
    assertArrayEquals(new String[]{"b", "c", "a"}, p.pow(2).apply(TestUtil.symbols(3)));
  }

  /* check defining property of composition */
  @Test
  public void testComp2() throws Exception {
    Permutation p = Permutation.define(1, 2, 0);
    Permutation p2 = Permutation.sorting(new int[]{4, 6, 10, -5, 195, 33, 2});
    for (int i = 0; i < p.length(); i += 1) {
      assertEquals(p2.apply(p.apply(i)), p2.compose(p).apply(i));
    }
  }

  /* check defining property of apply */
  @Test
  public void testApply() {
    int[] a = randomNumbers(100, 200);
    Permutation p = Permutation.random((int) (a.length * Math.random()));
    for (int i = 0; i < a.length; i += 1) {
      assertEquals(p.apply(a)[p.apply(i)], a[i]);
      if (i >= p.length()) {
        assertEquals(a[i], p.apply(a)[i]);
      }
    }
  }

  @Test
  public void testIterable() {
    for (int __ = 0; __ < 100; __++) {
      MyInt[] a = MyInt.box(randomNumbers(100, 50 + (int) (Math.random() * 100)));
      Permutation p = Permutation.random((int) (Math.random() * a.length));
      Object[] applied = p.apply(a);
      List<MyInt> arrayList = new ArrayList<MyInt>(a.length);
      List<MyInt> linkedList = new LinkedList<MyInt>();
      Collections.addAll(arrayList, a);
      Collections.addAll(linkedList, a);
      List<MyInt> arrayListApplied, linkedListApplied;
      List<MyInt> arrayListApplied2, linkedListApplied2;

      // standard
      arrayListApplied = p.apply(arrayList);
      linkedListApplied = p.apply(linkedList);
      for (int i = 0; i < a.length; i += 1) {
        assertEquals(applied[i], arrayListApplied.get(i));
        assertEquals(applied[i], linkedListApplied.get(i));
      }

      // compiled
      Cycles compiled = p.toCycles();
      arrayListApplied2 = compiled.apply(arrayList);
      linkedListApplied2 = compiled.apply(linkedList);
      assertEquals(arrayListApplied, arrayListApplied2);
      assertEquals(linkedListApplied, linkedListApplied2);
    }
  }

  /* gaps in ranking */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGap() throws Exception {
    define(1, 2, 0, 5);
  }

  /* missing zero in ranking */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMissingZero() throws Exception {
    define(1, 2, 3);
  }

  /* duplicates in ranking */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDuplicate() throws Exception {
    int[] ranking = {1, 2, 0, 2, 3};
    assertFalse(Rankings.isValid(ranking));
    define(ranking);
  }

  /* negative number in ranking */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNegative() throws Exception {
    Permutation.define(-1, 0, 1);
  }

  @Test
  public void testInvert() throws Exception {
    Permutation p = Permutation.define(1, 2, 0);
    assertTrue(Permutation.product(p.invert(), p).isIdentity());
    assertTrue(Permutation.product(p, p.invert()).isIdentity());
    assertTrue(Permutation.product(p, p.pow(2)).isIdentity());
    assertTrue(Permutation.product(p.pow(2), p).isIdentity());
    assertTrue(Permutation.product().isIdentity());
    assertTrue(p.pow(0).isIdentity());
    assertFalse(p.pow(1).isIdentity());
    assertFalse(p.pow(2).isIdentity());
    assertEquals(p.pow(0), p.pow(3));
    Assert.assertEquals(p.pow(2), Permutation.product(p, p));
    assertEquals(p.pow(1), p);
    Assert.assertEquals(p.pow(-1), Permutation.product(p, p));
    assertEquals(p.pow(-1), p.invert());
    assertEquals(p.pow(2), p.compose(p));
    Assert.assertArrayEquals(new String[]{"a", "b", "c"}, Permutation.product(p, p.invert()).apply(TestUtil.symbols(3)));
  }

  @Test
  public void testIdentity() {
    assertTrue(Permutation.identity().isIdentity());
    assertTrue(define(new int[]{0, 1, 2, 3, 4}).isIdentity());
    assertTrue(define(new int[]{0, 1, 2, 3, 4}).invert().isIdentity());
  }

  /* test defining property of identity */
  @Test
  public void testIdentity2() {
    Permutation identity = Permutation.identity();
    for (int i = 0; i < 10; i += 1) {
      assertEquals(i, identity.apply(i));
    }
  }

  /* Check defining property of inverse */
  @Test
  public void testInverse2() {
    Permutation p = Permutation.sorting(new int[]{4, 6, 10, -5, 195, 33, 2});
    for (int i = 0; i < p.length(); i += 1) {
      assertEquals(i, p.invert().apply(p.apply(i)));
    }
  }

  @Test
  public void cycleEquality() {
    Assert.assertEquals(Permutation.cycle1(1, 5, 3, 2), Permutation.cycle1(5, 3, 2, 1));
    Assert.assertEquals(Permutation.cycle1(1, 5, 3, 2), Permutation.cycle1(2, 1, 5, 3));
    Assert.assertNotEquals(Permutation.cycle1(1, 5, 3, 2), Permutation.cycle1(1, 5, 2, 3));
  }

  @Test
  public void cycleApply() {
    Assert.assertArrayEquals(new String[]{"b", "c", "e", "d", "a"},
        Permutation.cycle1(1, 5, 3, 2).apply(TestUtil.symbols(5)));
    Assert.assertArrayEquals(new String[]{"c", "b", "e", "d", "a"},
        Permutation.cycle1(1, 5, 3).apply(TestUtil.symbols(5)));
    Assert.assertArrayEquals(new String[]{"c", "a", "b"},
        Permutation.cycle1(1, 2, 3).apply(TestUtil.symbols(3)));
  }

  @Test
  public void testCycleApply() throws Exception {
    Assert.assertArrayEquals(new String[]{"c", "a", "b"},
        Permutation.product(Permutation.cycle1(1, 2), Permutation.cycle1(2, 3)).apply(TestUtil.symbols(3)));
    Assert.assertArrayEquals(new String[]{"c", "a", "b"}, Permutation.cycle1(1, 2, 3).apply(TestUtil.symbols(3)));
    Assert.assertArrayEquals(new String[]{"a", "c", "b"}, Permutation.product(Permutation.cycle1(1, 2),
        Permutation.product(Permutation.cycle1(1, 2), Permutation.cycle1(2, 3))).apply(TestUtil.symbols(3)));
  }

  @Test
  public void testCycleEquals() throws Exception {
    assertTrue(Permutation.product(Permutation.cycle1(1, 2), Permutation.cycle1(2, 1)).isIdentity());
    Assert.assertEquals(Permutation.cycle1(2, 3), Permutation.product(Permutation.cycle1(1, 2),
        Permutation.product(Permutation.cycle1(1, 2), Permutation.cycle1(2, 3))));
  }

  @Test
  public void testCycleLaw() throws Exception {
    Permutation longest = Permutation.cycle1(2, 4, 1, 11, 3);
    Assert.assertEquals(Permutation.product(Permutation.cycle1(2, 4),
        Permutation.cycle1(4, 1, 11, 3)), longest);
  }

  @Test
  public void testSort() throws Exception {
    int[] x = new int[]{4, 6, 10, -5, 195, 33, 2};
    int[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y);
    Permutation p = Permutation.sorting(x);
    for (int i = 0; i < x.length; i += 1) {
      assertEquals(x[i], y[p.apply(i)]);
    }
    assertArrayEquals(y, p.apply(x));
  }

  int indexOf(int[] x, int el) {
    for (int i = 0; i < x.length; i += 1) {
      if (x[i] == el)
        return i;
    }
    throw new IllegalArgumentException("not in x: " + el);
  }

  int indexOf(MyInt[] x, MyInt el) {
    for (int i = 0; i < x.length; i += 1) {
      if (x[i].n == el.n)
        return i;
    }
    throw new IllegalArgumentException("not in x: " + el);
  }

  /* check example from README */
  @Test
  public void testSortInvert() {
    int[] x = new int[]{4, 6, 10, -5, 195, 33, 2};
    Permutation unsort = Permutation.sorting(x).invert();
    int[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y);
    for (int k = 0; k < y.length; k += 1) {
      assertEquals(x[indexOf(x, y[k])], y[k]);
      assertEquals(indexOf(x, y[k]), unsort.apply(k));
    }
  }

  /* check defining property of sorting */
  @Test
  public void testSortRandom() {
    int size = (int) (100 * Math.random());
    int[] distinct = Rankings.random(size);
    int[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    Permutation p = Permutation.sorting(distinct);
    for (int i = 0; i < sorted.length; i += 1) {
      distinct[i] = sorted[p.apply(i)];
    }
  }

  @Test
  public void testSortInvertComparator() {
    MyInt[] x = box(new int[]{4, 6, 10, -5, 195, 33, 2});
    Permutation unsort = Permutation.sorting(x, MyInt.COMP).invert();
    MyInt[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y, MyInt.COMP);
    for (int k = 0; k < y.length; k += 1) {
      assertEquals(x[indexOf(x, y[k])], y[k]);
      assertEquals(indexOf(x, y[k]), unsort.apply(k));
    }
  }

  /* negative index */
  @Test(expected = IllegalArgumentException.class)
  public void testApplyInvalid() {
    Permutation.identity().apply(-1);
  }

  /**
   * @param a Any array of integers
   * @return A sorted copy of {@code a}
   */
  private int[] classicSort(int[] a) {
    int[] result = Arrays.copyOf(a, a.length);
    Arrays.sort(result);
    return result;
  }

  /* Another way of checking that duplicateRejectingFactory().sorting(a).apply(a) sorts a, for distinct array a */
  @Test
  public void testSort1024() {
    int[] a = Rankings.random(1024);
    assertArrayEquals(classicSort(a), Permutation.sorting(a).apply(a));
  }

  @Test
  public void testCycleLength() {
    Permutation swap01 = Transposition.swap(0, 1).toPermutation();
    assertEquals(2, swap01.length());
  }

  @Test
  public void testFromQuickly() {
    Permutation p = Permutation.from(new Comparable[]{1, 2, 3}, new Comparable[]{2, 3, 1});
    assertArrayEquals(new String[]{"b", "c", "a"}, p.apply(TestUtil.symbols(3)));
  }

  /**
   * @param size      How many MyInt objects we want
   * @param maxFactor Controls the size of random numbers that are produced
   * @return Random array of {@code size} distinct integers between {@code 0} and {@code size * maxFactor}
   */
  static MyInt[] distinctMyInts(int size, int maxFactor) {
    int[] ints = Rankings.random(size);
    MyInt[] result = new MyInt[size];
    for (int i = 0; i < size; i += 1) {
      result[i] = new MyInt(ints[i]);
    }
    return result;
  }

  /* check defining property of from */
  private void testFromQuickly2() {
    int size = 2048;
    int[] a = Rankings.random(size);
    Permutation random;
    do {
      random = Permutation.random((int) (Math.random() * size));
    } while (random.isIdentity());
    int[] b = random.apply(a);
    assertFalse(Arrays.equals(a, b));
    assertArrayEquals(Permutation.from(a, b).apply(a), b);
  }

  /* check defining property of from again, on non comparable objects, possibly with null */
  @Test
  public void testFromALot() {
    for (int i = 0; i < 100; i += 1) {
      testFromQuickly2();
    }
  }

  @Test
  public void testMove() {
    assertEquals(identity(), move(5, 5));
    assertEquals("213", Permutation.move(0, 1).apply("123"));
    assertEquals("23145", Permutation.move(0, 2).apply("12345"));
    assertEquals("14235", Permutation.move(3, 1).apply("12345"));
  }

  /* various assertions about Sym(5) */
  @Test
  public void testCyclesAndTranspositions() {
    int sign = 0;
    for (Permutation p : Permutation.symmetricGroup(5).collect(Collectors.toList())) {
      int order = p.order();
      sign += p.toCycles().signature();
      Cycles cycles = p.toCycles();
//      assertEquals(p, CompiledPermutation.product(cycles));
      assertEquals(p, p.toCycles().toPermutation());
      if (p.reverses(5)) {
        assertEquals(2, order);
        assertEquals(1, p.toCycles().signature());
      }
      if (order > 5) {
        assertEquals(6, order);
        assertEquals(2, cycles.numCycles());
      } else if (order == 5) {
        assertEquals(1, cycles.numCycles());
      } else if (order == 4) {
        assertEquals(1, cycles.numCycles());
      } else if (order == 3) {
        assertEquals(1, cycles.numCycles());
      } else if (order == 2) {
        assertTrue(cycles.numCycles() <= 2);
      } else {
        assertTrue(p.isIdentity());
      }
    }
    assertEquals(0, sign);
  }

  /* check edge cases */
  @Test
  public void testZero() {
    Permutation p = identity();
    assertEquals(define(new int[0]), p);
    assertEquals(p, defineCycle());
    assertEquals(0, p.length());
    assertArrayEquals(new int[0], p.apply(new int[0]));
    assertEquals(0, p.toCycles().numCycles());
    assertEquals(identity(), defineCycle(0));
    assertEquals(identity(), defineCycle(1));
    assertEquals(identity(), defineCycle(2));
  }

  /* example from README */
  @Test
  public void testPprod() {
    Permutation c0 = defineCycle(7, 9);
    Permutation c1 = defineCycle(1, 4, 8, 10, 3, 6, 11);
    Permutation c2 = defineCycle(0, 2, 5);
    assertEquals("Hello world!", product(c0, c1, c2).invert().apply(" !Hdellloorw"));
    assertEquals("Hello world!", product(Arrays.asList(c0, c1, c2)).invert().apply(" !Hdellloorw"));
  }

  /* making sure sorting does what we think it does */
  @Test
  public void testDegenerate() {
    int[] a = new int[]{3, 3, 3, 3, 3, 3, 3};
    assertFalse(Permutation.sorting(a).isIdentity());
  }

  @Test
  public void testShift() {
    assertEquals("abccba", Permutation.reverse(3).shift(3).apply("abcabc"));
  }

  @Test
  public void testShift2() {
    for (int __ = 0; __ < 100; __++) {
      Permutation p = Permutation.random(40);
      for (int n = 0; n < 100; n++) {
        for (int j = 0; j < 100; j++) {
          if (j < n) {
            assertEquals(j, p.shift(n).apply(j));
          } else {
            assertEquals(p.shift(n).apply(j), n + p.apply(j - n));
          }
        }
      }
    }
  }

  @Test
  public void testDestructive() {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.randomNumbers(10, 5);
      int[] copy = Arrays.copyOf(a, a.length);
      List<Integer> listCopy = Arrays.asList(ArrayUtil.box(Arrays.copyOf(a, a.length)));
      Permutation p = Permutation.random(5);
      Cycles d = p.toCycles();
      d.clobber(copy);
      d.clobber(listCopy);
      int[] expected = p.apply(a);
      assertArrayEquals(expected, copy);
      assertArrayEquals(ArrayUtil.box(expected), listCopy.toArray(new Integer[listCopy.size()]));
      assertEquals(p, d.toPermutation());
    }
  }

  @Test
  public void testNonDestructive() {
    int[] a = {0, 1, 2, 3, 4};
    Permutation p = define(1, 2, 0, 3, 4).compose(define(0, 1, 2, 4, 3));
    Cycles d = p.toCycles();
    assertArrayEquals(p.apply(a), d.apply(a));
  }


  @Test
  public void testDestructive3() {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.randomNumbers(100, 100);
      int[] copy = Arrays.copyOf(a, a.length);
      List<Integer> listCopy = Arrays.asList(ArrayUtil.box(Arrays.copyOf(a, a.length)));
      Permutation p = Permutation.random(100);
      Cycles d = p.toCycles();
      d.clobber(copy);
      d.clobber(listCopy);
      int[] expected = p.apply(a);
      assertArrayEquals(expected, copy);
      assertArrayEquals(ArrayUtil.box(expected), listCopy.toArray(new Integer[listCopy.size()]));
      assertEquals(p, d.toPermutation());
    }
  }

  @Test
  public void testSorts() {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.randomNumbers(100, 50 + (int) (Math.random() * 100));
      Permutation p = Permutation.sorting(a);
      assertTrue(p.sorts(a));
    }
  }

  @Test
  public void testSymmetricGroupDistinct() {
    for (int n = 1; n < 8; n++) {
      List<Permutation> sym = Permutation.symmetricGroup(n).collect(Collectors.toList());
      long count = sym.stream().count();
      assertThat(sym.stream().distinct().count(), is(count));
      assertThat(count, is(TestUtil.factorial(n)));
    }
    assertThat(Permutation.symmetricGroup(9).count(), is(TestUtil.factorial(9)));
    String[] a = {"Check", "out", "this", "swish", "library"};
    a = Permutation.random(a.length).apply(a);
    System.out.println(Arrays.toString(a));
// => [swish, library, Check, this, out]
  }


}

