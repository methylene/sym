package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static com.github.methylene.sym.TestUtil.randomNumbers;
import org.junit.Test;

public class PermutationFactoryTest {

  private final PermutationFactory nonstrict = PermutationFactory.builder().setValidate(true).build();
  private final PermutationFactory strict = PermutationFactory.builder().setStrict(true).setValidate(true).build();

  @Test
  public void testSortRandom() {
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 200);
      assertArrayEquals(Util.sortedCopy(a), nonstrict.sort(a).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 20);
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
      int[] a = randomNumbers(100, 200);
      int[] b = Permutation.random(a.length).apply(a);
      assertArrayEquals(b, nonstrict.from(a, b).apply(a));
    }
    for (int i = 0; i < 100; i += 1) {
      int[] a = randomNumbers(100, 20);
      int[] b = Permutation.random(a.length).apply(a);
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
    int[] a = randomNumbers(100, 200);
    int[] b = Permutation.random(a.length).apply(a);
    strict.from(a, b);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMismatch() {
    int[] a = randomNumbers(100, 110);
    int[] b = Permutation.random(a.length).apply(a);

    int[] dupes = TestUtil.duplicateIndexes(b);

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
    nonstrict.from(a, b).apply(a);
  }

}
