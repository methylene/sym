package com.github.methylene.sym;

import static com.github.methylene.sym.ArrayUtil.randomNumbers;
import static com.github.methylene.sym.Rankings.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import java.util.Arrays;

public class TestRankings {

  @Test
  public void testSortRandom() {
    for (int __ = 0; __ < 100; __ += 1) {
      int[] a = randomNumbers(100, 200);
      assertArrayEquals(ArrayUtil.sortedCopy(a), Permutation.sorting(a).apply(a));
    }
    for (int __ = 0; __ < 100; __ += 1) {
      int[] a = randomNumbers(100, 20);
      assertArrayEquals(ArrayUtil.sortedCopy(a), Permutation.sorting(a).apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int __ = 0; __ < 100; __ += 1) {
      String[] a = TestUtil.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(ArrayUtil.sortedCopy(a), Permutation.sorting(shuffled).apply(shuffled));
    }
  }

  @Test
  public void testFromRandom() {
    for (int __ = 0; __ < 100; __ += 1) {
      int[] a = randomNumbers(100, 200);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b).apply(a));
    }
    for (int __ = 0; __ < 100; __ += 1) {
      int[] a = randomNumbers(100, 20);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b).apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int __ = 0; __ < 100; __ += 1) {
      String[] a = TestUtil.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, Permutation.from(shuffled, a).apply(shuffled));
    }
  }


  @Test
  public void testMismatch() {
    for (int __ = 0; __ < 1000; __ += 1) {
      int[] a = randomNumbers(100, 110);
      int[] b = apply(random(a.length), a);

      int[] bdupes = TestUtil.duplicateIndexes(b);
      int[] adupes = TestUtil.duplicateIndexes(a);

      int changed = -1;
      // subtly mess things up by changing b,
      // so that all elements in a can still be found in b,
      // but b is not a reordering of a anymore
      if (Math.random() < 0.5) {
        for (int j = 0; j < b.length; j += 1) {
          if (b[bdupes[0]] != b[j]) {
            b[bdupes[0]] = b[j];
            changed = b[j];
            break;
          }
        }
      } else {
        for (int j = 0; j < a.length; j += 1) {
          if (a[adupes[0]] != a[j]) {
            a[adupes[0]] = a[j];
            changed = a[j];
            break;
          }
        }
      }
      int bc = TestUtil.count(b, changed);
      int ac = TestUtil.count(a, changed);
      assertNotEquals(bc, ac);
      assertTrue(ac > 0);
      assertTrue(bc > 0);

      // null because b is not a rearrangement of a
      assertNull(from(a, b));
    }
  }

  @Test
  public void testSort() {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.randomNumbers(100, (int) (Math.random() * 1000));
      int[] sort = sorting(a);
      int[] sorted = apply(sort, a);
      int[] unsort = invert(sort);
      int[] hopefullyIdentity = comp(sort, unsort);
      assertTrue(ArrayUtil.isSorted(hopefullyIdentity));
      assertTrue(ArrayUtil.isSorted(sorted));
      for (int el : a) {
        assertEquals(ArrayUtil.indexOf(a, el, 0), unsort[Arrays.binarySearch(sorted, el)]);
      }
    }
  }

  @Test
  public void testNextOffset() {
    int[] sorted = {0, 0, 1, 3, 3, 3, 4, 4};
    assertEquals(1, nextOffset(0, 0, sorted));
    assertEquals(-1, nextOffset(1, 0, sorted));
    assertEquals(1, nextOffset(3, 0, sorted));
    assertEquals(2, nextOffset(3, 1, sorted));
    assertEquals(1, nextOffset(4, 0, sorted));
    assertEquals(-1, nextOffset(4, 1, sorted));
    assertEquals(-1, nextOffset(5, 0, sorted));
    assertEquals(-2, nextOffset(5, -1, sorted));
    assertEquals(1, nextOffset(6, 0, sorted));
    assertEquals(-1, nextOffset(7, 0, sorted));
  }

  @Test
  public void testDecompose() {
    for (int __ = 0; __ < 100; __++) {
      Permutation p = Permutation.random(100);
      assertEquals(p, p.toCycles().toPermutation());
    }
  }

  @Test
  public void testSorts() {
    int[] ranking = {0, 3, 1, 4, 2};
    int[] a = {0, 4, 2, 4, 3};
    assertTrue(ArrayUtil.isSorted(Rankings.apply(ranking, a)));
    assertTrue(Rankings.sorts(ranking, a));
  }

  @Test
  public void testSorts2() {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.randomNumbers(100, 100 + (int) (100 * (Math.random() - 0.8)));
      int[] ranking = Rankings.sorting(a);
      assertTrue(ArrayUtil.isSorted(Rankings.apply(ranking, a)));
      assertTrue(Rankings.sorts(ranking, a));
    }
  }

}
