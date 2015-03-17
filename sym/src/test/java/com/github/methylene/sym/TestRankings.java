package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static com.github.methylene.sym.Util.randomNumbers;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestRankings {

  @Test
  public void testSortRandom() {
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 200);
      assertArrayEquals(Util.sortedCopy(a), Permutation.sort(a).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 20);
      assertArrayEquals(Util.sortedCopy(a), Permutation.sort(a).apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(Util.sortedCopy(a), Permutation.sort(shuffled).apply(shuffled));
    }
  }

  @Test
  public void testFromRandom() {
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 200);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 20);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b).apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, Permutation.from(shuffled, a).apply(shuffled));
    }
  }


  @Test
  public void testMismatch() {
    for (int _ = 0; _ < 1000; _ += 1) {
      try {

        int[] a = randomNumbers(100, 110);
        int[] b = Permutation.random(a.length).apply(a);

        int[] bdupes = Util.duplicateIndexes(b);
        int[] adupes = Util.duplicateIndexes(a);

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


        // this should throw an exception
        Permutation.from(a, b).apply(a);
        assertFalse("we should never get here", true);
      } catch (IllegalArgumentException __) {
        // ignore
      }
    }
  }

}
