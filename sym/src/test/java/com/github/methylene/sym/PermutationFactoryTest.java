package com.github.methylene.sym;

import static com.github.methylene.sym.PermutationFactory.from;
import static com.github.methylene.sym.PermutationFactory.sort;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class PermutationFactoryTest {

  static int[] randomNumbers(int maxNumber, int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = (int) (maxNumber * Math.random());
    }
    return result;
  }

  @Test
  public void testSortRandom() {
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 200);
      assertArrayEquals(Util.sortedCopy(a), sort(a).validate().apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 20);
      assertArrayEquals(Util.sortedCopy(a), sort(a).validate().apply(a));
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
      sort(randomNumbers(100, 200), true);
  }

  @Test
  public void testFromRandom() {
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 200);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, from(a, b).validate().apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 20);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, from(a, b).validate().apply(a));
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
    int[] a = randomNumbers(100, 200);
    int[] b = Permutation.random(a.length).apply(a);
    from(a, b, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMismatch() {
    int[] a = randomNumbers(100, 110);
    int[] b = Permutation.random(a.length).apply(a);

    int[] dupes = Util.duplicateIndexes(b);

    // subtly mess things up by changing b,
    // so that all elements in a can still be found in b,
    // but b is not a reordering of a anymore
    for (int j = 0; j < b.length; j += 1) {
      if (b[dupes[0]] != b[j]) {
        b[dupes[0]] = b[j];
        break;
      }
    }

    // this should throw an exception
    from(a, b).apply(a);
  }

}
