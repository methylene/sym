package com.github.methylene.sym;

import org.junit.Test;

import static com.github.methylene.sym.Transposition.product;
import static com.github.methylene.sym.Transposition.swap;
import static org.junit.Assert.assertEquals;

public class TranspositionTest {

  @Test
  public void testProd() throws Exception {
    Permutation p = product(swap(0, 1), swap(1, 2));
    assertEquals("cab", p.apply("abc"));
  }
}