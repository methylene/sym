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
    int[] a = Util.sequence(100);
    Cycles p = Permutation.random(100).toCycles();
    p.clobber(a);
    p.unclobber(a);
    assertArrayEquals(Util.sequence(100), a);
  }

}