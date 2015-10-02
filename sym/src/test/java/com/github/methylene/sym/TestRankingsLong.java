package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static com.github.methylene.sym.Rankings.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/* like PermutationFactoryTest, but use the long versions of sort and from */
public class TestRankingsLong {

  static long[] randomNumbers(int maxNumber, int length) {
    long[] result = new long[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = (int) (maxNumber * Math.random());
    }
    return result;
  }

  @Test
  public void testSortRandom() {
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 200);
      assertArrayEquals(ArrayUtil.sortedCopy(a), Permutation.sort(a).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 20);
      assertArrayEquals(ArrayUtil.sortedCopy(a), Permutation.sort(a).apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = TestUtil.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(ArrayUtil.sortedCopy(a), Permutation.sort(shuffled).apply(shuffled));
    }
  }

  @Test
  public void testFromRandom() {
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 200);
      long[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 20);
      long[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, Permutation.from(a, b).apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = TestUtil.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, Permutation.from(shuffled, a).apply(shuffled));
    }
  }

  @Test
  public void testMismatch() {
    long[] a = randomNumbers(100, 110);
    long[] b = Permutation.random(a.length).apply(a);

    int[] dupes = TestUtil.duplicateIndexes(b);

    for (int j = 0; j < b.length; j += 1) {
      if (b[dupes[0]] != b[j]) {
        b[dupes[0]] = b[j];
        break;
      }
    }

    // null because b is not a rearrangement of a
    assertNull(from(a, b));
  }

}
