package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PermutationTest {

  /* Check example from constructor javadoc */
  @Test public void testAbc() {
    Permutation p = new Permutation(new int[] { 1, 2, 0 });
    assertArrayEquals(new char[] { 'c', 'a', 'b' }, p.apply(new char[] { 'a', 'b', 'c' }));
  }

  @Test public void testComp() throws Exception {
    Permutation p = Permutation.perm1(2, 3, 1);
    assertEquals(new Permutation(new int[] { 1, 2, 0 }), p);
    assertArrayEquals(new String[] { "c", "a", "b" }, p.apply());
    assertArrayEquals(new String[] { "b", "c", "a" }, p.pow(2).apply());
  }

  /* check defining property of composition */
  @Test public void testComp2() throws Exception {
    Permutation p = Permutation.perm1(2, 3, 1);
    Permutation p2 = Permutation.sort(new int[] { 4, 6, 10, -5, 195, 33, 2 });
    p = p.pad(p2.length());
    for (int i = 0; i < p.length(); i += 1) {
      assertEquals(p2.apply(p.apply(i)), p2.comp(p).apply(i));
    }
  }

  /* no gaps are allowed in one-line notation */
  @Test(expected = IllegalArgumentException.class) public void testInvalidGap() throws Exception {
    new Permutation(new int[] { 1, 2, 0, 5 });
  }

  /* constructor is 0-based */
  @Test(expected = IllegalArgumentException.class) public void testInvalidMissingZero() throws Exception {
    new Permutation(new int[] { 1, 2, 3 });
  }

  /* no duplicates are allowed in one-line notation */
  @Test(expected = IllegalArgumentException.class) public void testInvalidDuplicate() throws Exception {
    new Permutation(new int[] { 1, 2, 0, 2, 3 });
  }

  /* no negative numbers allowed in one-line notation */
  @Test(expected = IllegalArgumentException.class) public void testInvalidNegative() throws Exception {
    Permutation.perm1(-1, 0, 1);
  }

  /* test defining property of pad */
  @Test public void testPad() {
    Permutation p = Permutation.sort(new int[] { 4, 6, 10, -5, 195, 33, 2 });
    Permutation padded = p.pad(100);
    for (int i = 0; i < 100; i += 1) {
      if (i < p.length())
        assertEquals(p.apply(i), padded.apply(i));
      else
        assertEquals(i, padded.apply(i));
    }
  }

  @Test public void testCompUneven() throws Exception {
    Permutation p = Permutation.perm1(2, 3, 1).pad(4);
    Permutation p2 = Permutation.perm1(2, 3, 1, 4);
    assertArrayEquals(new String[] { "b", "c", "a", "d" }, p.comp(p2).apply());
    assertArrayEquals(new String[] { "b", "c", "a", "d" }, p2.comp(p).apply());
  }

  @Test public void testInvert() throws Exception {
    Permutation p = Permutation.perm1(2, 3, 1);
    assertTrue(Permutation.prod(p.invert(), p).isIdentity());
    assertTrue(Permutation.prod(p, p.invert()).isIdentity());
    assertTrue(Permutation.prod(p, p.pow(2)).isIdentity());
    assertTrue(Permutation.prod(p.pow(2), p).isIdentity());
    assertTrue(Permutation.prod().isIdentity());
    assertTrue(p.pow(0).isIdentity());
    assertFalse(p.pow(1).isIdentity());
    assertFalse(p.pow(2).isIdentity());
    assertEquals(p.pow(0), p.pow(3));
    Assert.assertEquals(p.pow(2), Permutation.prod(p, p));
    assertEquals(p.pow(1), p);
    Assert.assertEquals(p.pow(-1), Permutation.prod(p, p));
    assertEquals(p.pow(-1), p.invert());
    assertEquals(p.pow(2), p.comp(p));
    Assert.assertArrayEquals(new String[] { "a", "b", "c" }, Permutation.prod(p, p.invert()).apply());
  }

  @Test public void testIdentity() {
    assertTrue(Permutation.identity(5).isIdentity());
    assertTrue(Permutation.identity(5).invert().isIdentity());
    assertTrue(Permutation.identity(0).invert().isIdentity());
    assertTrue(Permutation.prod(Permutation.prod().pad(2), Permutation.identity(2)).isIdentity());
    Assert.assertEquals(5, Permutation.identity(5).length());
  }

  /* test defining property of identity */
  @Test public void testIdentity2() {
    Permutation identity = Permutation.identity(5);
    for (int i = 0; i < identity.length(); i += 1) {
      assertEquals(i, identity.apply(i));
    }
  }

  /* Check defining property of inverse */
  @Test public void testInverse2() {
    Permutation p = Permutation.sort(new int[] { 4, 6, 10, -5, 195, 33, 2 });
    for (int i = 0; i < p.length(); i += 1) {
      assertEquals(i, p.invert().apply(p.apply(i)));
    }
  }

  @Test public void cycleEquality() {
    Assert.assertEquals(Permutation.cycle1(1, 5, 3, 2), Permutation.cycle1(5, 3, 2, 1));
    Assert.assertEquals(Permutation.cycle1(1, 5, 3, 2), Permutation.cycle1(2, 1, 5, 3));
    Assert.assertNotEquals(Permutation.cycle1(1, 5, 3, 2), Permutation.cycle1(1, 5, 2, 3));
  }

  @Test public void cycleApply() {
    Assert.assertArrayEquals(new String[] { "b", "c", "e", "d", "a" }, Permutation.cycle1(1, 5, 3, 2).apply());
    Assert.assertArrayEquals(new String[] { "c", "b", "e", "d", "a" }, Permutation.cycle1(1, 5, 3).apply());
    Assert.assertArrayEquals(new String[] { "c", "a", "b" }, Permutation.cycle1(1, 2, 3).apply());
  }

  @Test public void testCycleApply() throws Exception {
    Assert.assertArrayEquals(new String[] { "c", "a", "b" },
        Permutation.prod(Permutation.cycle1(1, 2).pad(3), Permutation.cycle1(2, 3)).apply());
    Assert.assertArrayEquals(new String[] { "c", "a", "b" }, Permutation.cycle1(1, 2, 3).apply());
    Assert.assertArrayEquals(new String[] { "a", "c", "b" }, Permutation.prod(Permutation.cycle1(1, 2).pad(3),
        Permutation.prod(Permutation.cycle1(1, 2).pad(3), Permutation.cycle1(2, 3))).apply());
  }

  @Test public void testCycleEquals() throws Exception {
    assertTrue(Permutation.prod(Permutation.cycle1(1, 2), Permutation.cycle1(2, 1)).isIdentity());
    Assert.assertEquals(Permutation.cycle1(2, 3), Permutation.prod(Permutation.cycle1(1, 2).pad(3),
        Permutation.prod(Permutation.cycle1(1, 2).pad(3), Permutation.cycle1(2, 3))));
  }

  @Test public void testCycleLaw() throws Exception {
    Permutation longest = Permutation.cycle1(2, 4, 1, 11, 3);
    Assert.assertEquals(Permutation.prod(Permutation.cycle1(2, 4).pad(longest.length()),
        Permutation.cycle1(4, 1, 11, 3).pad(longest.length())), longest);
  }

  @Test public void testSort() throws Exception {
    int[] x = new int[] { 4, 6, 10, -5, 195, 33, 2 };
    int[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y);
    Permutation p = Permutation.sort(x);
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
  @Test public void testSortInvert() {
    int[] x = new int[] { 4, 6, 10, -5, 195, 33, 2 };
    Permutation unsort = Permutation.sort(x).invert();
    int[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y);
    for (int k = 0; k < y.length; k += 1) {
      assertEquals(x[indexOf(x, y[k])], y[k]);
      assertEquals(indexOf(x, y[k]), unsort.apply(k));
    }
  }

  /* check defining property of sort */
  @Test public void testSortRandom() {
    int size = (int) (100 * Math.random());
    int[] distinct = distinctInts(size, 8);
    int[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    Permutation p = Permutation.sort(distinct);
    for (int i = 0; i < sorted.length; i += 1) {
      distinct[i] = sorted[p.apply(i)];
    }
  }

  @Test public void testSortInvertComparator() {
    Comparator<MyInt> comparator = new Comparator<MyInt>() {
      @Override public int compare(MyInt a, MyInt b) {
        return a.n - b.n;
      }
    };
    MyInt[] x = new MyInt[] { new MyInt(4), new MyInt(6), new MyInt(10), new MyInt(-5), new MyInt(195), new MyInt(33),
        new MyInt(2) };
    Permutation unsort = Permutation.sort(x, comparator).invert();
    MyInt[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y, comparator);
    for (int k = 0; k < y.length; k += 1) {
      assertEquals(x[indexOf(x, y[k])], y[k]);
      assertEquals(indexOf(x, y[k]), unsort.apply(k));
    }
  }

  @Test(expected = IllegalArgumentException.class) public void testApplyInvalid() {
    Permutation.identity(3).apply(-1);
  }

  @Test(expected = IllegalArgumentException.class) public void testApplyInvalid2() {
    Permutation.identity(3).apply(3);
  }

  @Test(expected = IllegalArgumentException.class) public void testApplyInvalid3() {
    Permutation.identity(3).apply(new int[] { 1, 2 });
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

  /* Another way of checking that Permutation.sort(a).apply(a) sorts a, for distinct array a */
  @Test public void testSort1024() {
    int[] a = distinctInts(1024, 8);
    assertArrayEquals(classicSort(a), Permutation.sort(a).apply(a));
  }

  @Test public void testCycleLength() {
    Permutation swap01 = Permutation.swap(0, 1);
    assertEquals(2, swap01.length());
  }

  @Test public void testFromQuickly() {
    Permutation p = Permutation.from(new Comparable[] { 1, 2, 3 }, new Comparable[] { 2, 3, 1 });
    assertArrayEquals(new String[] { "b", "c", "a" }, p.apply());
  }

  /**
   * @param size      How many MyInt objects we want
   * @param maxFactor Controls the size of random numbers that are produced
   * @return Random array of {@code size} distinct integers between {@code 0} and {@code size * maxFactor}
   */
  static MyInt[] distinctMyInts(int size, int maxFactor) {
    int[] ints = distinctInts(size, maxFactor);
    MyInt[] result = new MyInt[size];
    for (int i = 0; i < size; i += 1) {
      result[i] = new MyInt(ints[i]);
    }
    return result;
  }

  /* check defining property of from */
  private void testFromQuickly2() {
    int size = 2048;
    int[] a = distinctInts(size, 8);
    Permutation random;
    do {
      random = Permutation.random(size);
    } while (random.isIdentity());
    int[] b = random.apply(a);
    assertFalse(Arrays.equals(a, b));
    assertArrayEquals(Permutation.from(a, b).apply(a), b);
  }

  /* check defining property of from again, on non comparable objects, possibly with null */
  @Test public void testFromALot() {
    for (int i = 0; i < 100; i += 1) {
      testFromQuickly2();
    }
  }

  @Test public void testInsert() {
    assertEquals("23145", Permutation.insert(0, 2).pad(5).apply("12345"));
    assertEquals("14235", Permutation.insert(3, 1).pad(5).apply("12345"));
  }

  /* various assertions about Sym(5) */
  @Test public void testCyclesAndTranspositions() {
    int sign = 0;
    for (Permutation p : TestUtil.permutations(5)) {
      int order = p.order();
      sign += p.signum();
      List<Permutation> cycles = p.toCycles();
      assertEquals(p, Permutation.prod(cycles).pad(5));
      assertEquals(p, Permutation.prod(p.toTranspositions()).pad(5));
      if (p.isReverse()) {
        assertEquals(2, order);
        assertEquals(1, p.signum());
      }
      if (order > 5) {
        assertEquals(6, order);
        assertEquals(2, cycles.size());
      } else if (order == 5) {
        assertEquals(1, cycles.size());
      } else if (order == 4) {
        assertEquals(1, cycles.size());
      } else if (order == 3) {
        assertEquals(1, cycles.size());
      } else if (order == 2) {
        assertTrue(cycles.size() <= 2);
      } else {
        assertTrue(p.isIdentity());
      }
    }
    assertEquals(0, sign);
  }

}
