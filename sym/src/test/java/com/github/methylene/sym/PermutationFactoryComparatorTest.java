package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static com.github.methylene.sym.TestUtil.randomNumbers;
import org.junit.Test;

/* like PermutationFactoryTest, but use the Comparator versions of sort and from */
public class PermutationFactoryComparatorTest {

  private final PermutationFactory nonstrict = PermutationFactory.builder().setValidate(true).build();
  private final PermutationFactory strict = PermutationFactory.builder().setStrict(true).setValidate(true).build();

  static final int REPEAT = 1000;

  static MyInt[] randomMyInts(int maxNumber, int length) {
    return box(randomNumbers(maxNumber, length));
  }

  static MyInt[] box(int[] a) {
    MyInt[] result = new MyInt[a.length];
    for (int i = 0; i < result.length; i += 1) {
      result[i] = new MyInt(a[i]);
    }
    return result;
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

  @Test (expected = IllegalArgumentException.class)
  public void testStrictSortFail() {
    strict.sort(randomMyInts(100, 200), MyInt.COMP);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrictFromFail() {
    MyInt[] a = randomMyInts(100, 200);
    Object[] b = Permutation.random(a.length).apply(a);
    strict.from(a, b, MyInt.COMP);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMismatch() {
    MyInt[] a = randomMyInts(100, 110);
    Object[] b = Permutation.random(a.length).apply(a);

    int[] dupes = TestUtil.duplicateIndexes(b, MyInt.COMP);

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
    nonstrict.from(a, b, MyInt.COMP).apply(a);
  }


}
