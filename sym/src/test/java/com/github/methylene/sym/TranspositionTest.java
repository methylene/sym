package com.github.methylene.sym;

import org.junit.Test;

import static com.github.methylene.sym.Transposition.product;
import static com.github.methylene.sym.Transposition.swap;
import static org.junit.Assert.*;

public class TranspositionTest {

  @Test
  public void testProd() throws Exception {
    Permutation p = product(swap(0, 1), swap(1, 2));
    assertEquals("cab", p.apply("abc"));
  }

  @Test
  public void testCommute() throws Exception {
    Transposition.DefaultTranspositionFactory factory = new Transposition.DefaultTranspositionFactory(10);
    for (int __ = 0; __ < 10; __++) {
      Transposition p = Transposition.random(factory, 10);
      Transposition q = Transposition.random(factory, 10);
      if (product(p, q).equals(product(q, p))) {
        assertTrue(p.commutesWith(q));
        assertTrue(q.commutesWith(p));
      } else {
        assertFalse("p="+p+",q="+q, p.commutesWith(q));
        assertFalse(q.commutesWith(p));
      }
    }
  }

}
