package com.hotels.sym;

import static com.hotels.sym.Permutation.cycle;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import java.util.Arrays;

public class CycleTest {

  @Test
  public void equality() {
    assertEquals(cycle(1, 5, 3, 2), cycle(5, 3, 2, 1));
    assertEquals(cycle(1, 5, 3, 2), cycle(2, 1, 5, 3));
    assertNotEquals(cycle(1, 5, 3, 2), cycle(1, 5, 2, 3));
  }

  @Test
  public void apply() {
    assertArrayEquals(new Object[]{"b", "c", "e", "d", "a"}, cycle(1, 5, 3, 2).apply());
    assertArrayEquals(new Object[]{"c", "b", "e", "d", "a"}, cycle(1, 5, 3).apply());
    assertArrayEquals(new Object[]{"c", "a", "b"}, cycle(1, 2, 3).apply());
  }

}
