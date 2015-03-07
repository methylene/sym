package com.github.methylene.sym;

import static com.github.methylene.sym.PermutationFactory.from;
import static com.github.methylene.sym.PermutationFactory.sort;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/* mostly duplicate code from PermutationFactoryTest, not pretty */
public class PermutationFactoryTestComparator {

  static MyInt[] randomNumbers(int maxNumber, int length) {
    MyInt[] result = new MyInt[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = new MyInt((int) (maxNumber * Math.random()));
    }
    return result;
  }

  @Test
  public void testSortRandom() {
    for (int i = 0; i < 100; i += 1) {
      MyInt[] a = randomNumbers(100, 200);
      assertArrayEquals(Util.sortedCopy(a, MyInt.COMP), sort(a, MyInt.COMP).validate().apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      MyInt[] a = randomNumbers(100, 20);
      assertArrayEquals(Util.sortedCopy(a, MyInt.COMP), sort(a, MyInt.COMP).validate().apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(Util.sortedCopy(a), sort(shuffled, true).validate().apply(shuffled));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortStrictFail() {
      sort(randomNumbers(100, 200), MyInt.COMP, true);
  }

  @Test
  public void testFromRandom() {
    for (int i = 0; i < 100; i += 1) {
      MyInt[] a = randomNumbers(100, 200);
      Object[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, from(a, b, MyInt.COMP).validate().apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      MyInt[] a = randomNumbers(100, 20);
      Object[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, from(a, b, MyInt.COMP).validate().apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, from(shuffled, a, true).validate().apply(shuffled));
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testFromFail() {
    MyInt[] a = randomNumbers(100, 200);
    Object[] b = Permutation.random(a.length).apply(a);
    from(a, b, MyInt.COMP, true);
  }

  /* testMismatch missing */

}
