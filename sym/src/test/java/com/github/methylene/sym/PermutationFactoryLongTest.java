package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/* like PermutationFactoryTest, but use the long versions of sort and from */
public class PermutationFactoryLongTest {

  private final PermutationFactory nonstrict = PermutationFactory.builder().setParanoia(PermutationFactory.Paranoia.ALWAYS_VALIDATE).build();
  private final PermutationFactory strict = PermutationFactory.builder().setUniquenessConstraint(PermutationFactory.UniquenessConstraint.FORBID_DUPLICATES).setParanoia(PermutationFactory.Paranoia.ALWAYS_VALIDATE).build();

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
      assertArrayEquals(Util.sortedCopy(a), nonstrict.sort(a).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 20);
      assertArrayEquals(Util.sortedCopy(a), nonstrict.sort(a).apply(a));
    }
  }

  @Test
  public void testSortStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(Util.sortedCopy(a), strict.sort(shuffled).apply(shuffled));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSortStrictFail() {
    strict.sort(randomNumbers(100, 200));
  }

  @Test
  public void testFromRandom() {
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 200);
      long[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, nonstrict.from(a, b).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      long[] a = randomNumbers(100, 20);
      long[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, nonstrict.from(a, b).apply(a));
    }
  }

  @Test
  public void testFromStrict() {
    for (int i = 0; i < 100; i += 1) {
      String[] a = Util.symbols(100);
      String[] shuffled = Permutation.random(a.length).apply(a);
      assertArrayEquals(a, strict.from(shuffled, a).apply(shuffled));
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testFromFail() {
    long[] a = randomNumbers(100, 200);
    long[] b = Permutation.random(a.length).apply(a);
    strict.from(a, b);
  }

  @Test(expected = IllegalArgumentException.class)
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
    nonstrict.from(a, b).apply(a);
  }

}
