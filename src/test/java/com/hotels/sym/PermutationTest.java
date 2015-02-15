package com.hotels.sym;

import static com.hotels.sym.Permutation.comp;
import static com.hotels.sym.Permutation.cycle;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PermutationTest {

  @Test
  public void testApply() throws Exception {
    assertArrayEquals(new String[]{"c", "a", "b"}, comp(cycle(1, 2), cycle(2, 3)).apply());
    assertArrayEquals(new String[]{"c", "a", "b"}, cycle(1, 2, 3).apply());
    assertArrayEquals(new String[]{"a", "c", "b"}, comp(cycle(1, 2),
            comp(cycle(1, 2), cycle(2, 3))).apply());
  }

  @Test
  public void testEquals() throws Exception {
    assertEquals(comp(), comp(cycle(1, 2), cycle(2, 1)));
    assertEquals(cycle(2, 3), comp(cycle(1, 2),
            comp(cycle(1, 2), cycle(2, 3))));
  }

}
