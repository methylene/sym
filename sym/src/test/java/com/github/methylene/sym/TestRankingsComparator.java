package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static com.github.methylene.sym.Util.randomNumbers;
import static com.github.methylene.sym.MyInt.box;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.Comparator;

/* like PermutationFactoryTest, but use the Comparator versions of sort and from */
public class TestRankingsComparator {

  static final int REPEAT = 1000;

  static MyInt[] randomMyInts(int maxNumber, int length) {
    return box(randomNumbers(maxNumber, length));
  }

  @Test
  public void testSortRandom() {
    MyInt[] a = null;
    for (int i = 0; i < REPEAT; i += 1) {
      a = box(randomNumbers(100, 200));
      assertArrayEquals(Util.sortedCopy(a, MyInt.COMP), Permutation.sort(a, MyInt.COMP).apply(a));
    }
    for (int i = 0; i < REPEAT; i += 1) {
      a = box(randomNumbers(100, 200));
      assertArrayEquals(Util.sortedCopy(a, MyInt.COMP), Permutation.sort(a, MyInt.COMP).apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int i = 0; i < REPEAT; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(Util.sortedCopy(a), Permutation.sort(shuffled).apply(shuffled));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFromRandom() {
    for (int i = 0; i < REPEAT; i += 1) {
      int[] a = randomNumbers(100, 200);
      Object[] b = Permutation.random(a.length).apply(box(a));
      assertArrayEquals(b, Permutation.from(box(a), b, (Comparator) MyInt.COMP).apply(box(a)));
    }
    for (int i = 0; i < REPEAT; i += 1) {
      Object[] a = randomMyInts(100, 20);
      Object[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b, (Comparator) MyInt.COMP).apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int i = 0; i < REPEAT; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, Permutation.from(shuffled, a).apply(shuffled));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testMismatch() {

    for (int _ = 0; _ < 1000; _ += 1) {
      try {
        MyInt[] a = randomMyInts(100, 110);
        Object[] b = Permutation.random(a.length).apply(a);

        int[] bdupes = Util.duplicateIndexes(b, MyInt.COMP);
        int[] adupes = Util.duplicateIndexes(a, MyInt.COMP);

        MyInt changed = null;
        // subtly mess things up by changing b,
        // so that all elements in a can still be found in b,
        // but b is not a reordering of a anymore
        if (Math.random() < 0.5) {
          for (int j = 0; j < b.length; j += 1) {
            if (!b[bdupes[0]].equals(b[j])) {
              b[bdupes[0]] = b[j];
              changed = (MyInt) b[j];
              break;
            }
          }
        } else {
          for (int j = 0; j < a.length; j += 1) {
            if (!a[adupes[0]].equals(a[j])) {
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
        Permutation.from(a, b, (Comparator) MyInt.COMP).apply(a);
        assertFalse("we should never get here", true);
      } catch (IllegalArgumentException __) {
        // ignore
      }
    }


  }


}
