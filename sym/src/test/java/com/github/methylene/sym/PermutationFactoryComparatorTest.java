package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static com.github.methylene.sym.Util.randomNumbers;
import static com.github.methylene.sym.MyInt.box;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/* like PermutationFactoryTest, but use the Comparator versions of sort and from */
public class PermutationFactoryComparatorTest {

  private final PermutationFactory nonstrict = PermutationFactory.builder().setParanoia(PermutationFactory.Paranoia.ALWAYS_VALIDATE).build();
  private final PermutationFactory strict = PermutationFactory.builder().setUniquenessConstraint(PermutationFactory.UniquenessConstraint.FORBID_DUPLICATES).setParanoia(PermutationFactory.Paranoia.ALWAYS_VALIDATE).build();

  static final int REPEAT = 1000;

  static MyInt[] randomMyInts(int maxNumber, int length) {
    return box(randomNumbers(maxNumber, length));
  }

  @Test
  public void testSortRandom() {
    MyInt[] a = null;
    for (int i = 0; i < REPEAT; i += 1) {
      a = box(randomNumbers(100, 200));
      assertArrayEquals(Util.sortedCopy(a, MyInt.COMP), nonstrict.sort(a, MyInt.COMP).apply(a));
    }
    for (int i = 0; i < REPEAT; i += 1) {
      a = box(randomNumbers(100, 200));
      assertArrayEquals(Util.sortedCopy(a, MyInt.COMP), nonstrict.sort(a, MyInt.COMP).apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int i = 0; i < REPEAT; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(Util.sortedCopy(a), strict.sort(shuffled).apply(shuffled));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortStrictFail() {
    strict.sort(randomMyInts(100, 200), MyInt.COMP);
  }

  @Test
  public void testFromRandom() {
    for (int i = 0; i < REPEAT; i += 1) {
      int[] a = randomNumbers(100, 200);
      Object[] b = Permutation.random(a.length).apply(box(a));
      assertArrayEquals(b, nonstrict.from(box(a), b, MyInt.COMP).apply(box(a)));
    }
    for (int i = 0; i < REPEAT; i += 1) {
      MyInt[] a = randomMyInts(100, 20);
      Object[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, nonstrict.from(a, b, MyInt.COMP).apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int i = 0; i < REPEAT; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, strict.from(shuffled, a).apply(shuffled));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrictSortFail() {
    strict.sort(randomMyInts(100, 200), MyInt.COMP);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrictFromFail() {
    MyInt[] a = randomMyInts(100, 200);
    Object[] b = Permutation.random(a.length).apply(a);
    strict.from(a, b, MyInt.COMP);
  }

  @Test
  public void testMismatch() {

    for (int _ = 0; _ < 1000; _ += 1) {
      try {
        MyInt[] a = randomMyInts(100, 110);
        Object[] b = Permutation.random(a.length).apply(a);

        int[] bdupes = TestUtil.duplicateIndexes(b, MyInt.COMP);
        int[] adupes = TestUtil.duplicateIndexes(a, MyInt.COMP);

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
        nonstrict.from(a, b, MyInt.COMP).apply(a);
        assertFalse("we should never get here", true);
      } catch (IllegalArgumentException __) {
        // ignore
      }
    }


  }


}
