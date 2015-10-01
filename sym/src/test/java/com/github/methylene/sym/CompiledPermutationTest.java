package com.github.methylene.sym;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class CompiledPermutationTest {

  @Test
  public void testUnclobber() throws Exception {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.range(100);
      int[] b = Arrays.copyOf(a, a.length);
      Cycles p = Permutation.random(a.length).toCycles();
      p.clobber(a);
      p.unclobber(a);
      assertArrayEquals(b, a);
    }
  }

  /* test defining property of apply */
  @Test
  public void testApply() {
    for (int __ = 0; __ < 100; __++) {
      int[] a = ArrayUtil.range(100);
      int[] b = Arrays.copyOf(a, a.length);
      Cycles p = Permutation.random(a.length - 10).toCycles();
      int[] c = p.apply(a);
      for (int i = 0; i < a.length; i += 1) {
        assertEquals(c[p.apply(i)], a[i]);
        if (i >= p.length())
          assertEquals(a[i], p.apply(a)[i]);
      }
      assertArrayEquals(b, a);
    }
  }

}
