package com.github.methylene.sym;

import static com.github.methylene.sym.PermutationFactory.sort;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class PermutationFactoryTest {

  static int[] randomNumbers(int maxNumber, int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i +=1) {
      result[i] = (int) (maxNumber * Math.random());
    }
    return result;
  }

  @Test public void testSortRandom() {
    int[] nonDistinct = randomNumbers(100, 200);
    assertArrayEquals(Util.sortedCopy(nonDistinct), sort(nonDistinct).apply(nonDistinct));
  }

}