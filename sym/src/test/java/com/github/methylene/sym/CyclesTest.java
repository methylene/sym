package com.github.methylene.sym;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CyclesTest {

  @Test
  public void testUnApply() throws Exception {
    Cycles p = Permutation.random(100).toCycles();
    for (int i = -1; i <= p.length(); i++)
      assertEquals(i, p.unApply(p.apply(i)));
  }

  @Test
  public void testUnclobber() {
    int[] a = ArrayUtil.range(100);
    Cycles p = Permutation.random(100).toCycles();
    p.clobber(a);
    p.unclobber(a);
    assertArrayEquals(ArrayUtil.range(100), a);
  }

  @Test
  public void testProduct() {
    Permutation p = Permutation.random(100);
    Permutation q = Permutation.random(100);
    Permutation compose = p.compose(q);
  }

}
