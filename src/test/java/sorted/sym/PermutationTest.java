package sorted.sym;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static sorted.sym.Permutation.IDENTITY;
import static sorted.sym.Permutation.cycle1;
import static sorted.sym.Permutation.perm1;
import static sorted.sym.Permutation.prod;
import org.junit.Test;


public class PermutationTest {

  @Test
  public void testComp() throws Exception {
    Permutation p = perm1(2, 3, 1);
    assertArrayEquals(new Object[]{"c", "a", "b"}, p.apply());
    assertArrayEquals(new Object[]{"b", "c", "a"}, p.pow(2).apply());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGap() throws Exception {
    perm1(2, 3, 1, 5);
  }

  @Test
  public void testCompUneven() throws Exception {
    Permutation p = perm1(2, 3, 1);
    Permutation p2 = perm1(2, 3, 1, 4);
    assertArrayEquals(new Object[]{"b", "c", "a", "d"}, p.comp(p2).apply());
    assertArrayEquals(new Object[]{"b", "c", "a", "d"}, p2.comp(p).apply());
  }

  @Test
  public void testInvert() throws Exception {
    Permutation p = perm1(2, 3, 1);
    assertEquals(IDENTITY, prod(p.invert(), p));
    assertEquals(IDENTITY, prod(p, p.invert()));
    assertEquals(IDENTITY, prod(p, p.pow(2)));
    assertEquals(IDENTITY, prod(p.pow(2), p));
    assertEquals(IDENTITY, p.pow(3));
    assertArrayEquals(new Object[]{"a", "b", "c"}, prod(p, p.invert()).apply());
  }

  @Test
  public void cycleEquality() {
    assertEquals(cycle1(1, 5, 3, 2), cycle1(5, 3, 2, 1));
    assertEquals(cycle1(1, 5, 3, 2), cycle1(2, 1, 5, 3));
    assertNotEquals(cycle1(1, 5, 3, 2), cycle1(1, 5, 2, 3));
  }

  @Test
  public void cycleApply() {
    assertArrayEquals(new Object[]{"b", "c", "e", "d", "a"}, cycle1(1, 5, 3, 2).apply());
    assertArrayEquals(new Object[]{"c", "b", "e", "d", "a"}, cycle1(1, 5, 3).apply());
    assertArrayEquals(new Object[]{"c", "a", "b"}, cycle1(1, 2, 3).apply());
  }

  @Test
  public void testCycleApply() throws Exception {
    assertArrayEquals(new String[]{"c", "a", "b"}, prod(cycle1(1, 2), cycle1(2, 3)).apply());
    assertArrayEquals(new String[]{"c", "a", "b"}, cycle1(1, 2, 3).apply());
    assertArrayEquals(new String[]{"a", "c", "b"}, prod(cycle1(1, 2),
            prod(cycle1(1, 2), cycle1(2, 3))).apply());
  }

  @Test
  public void testCycleEquals() throws Exception {
    assertEquals(IDENTITY, prod(cycle1(1, 2), cycle1(2, 1)));
    assertEquals(cycle1(2, 3), prod(cycle1(1, 2),
            prod(cycle1(1, 2), cycle1(2, 3))));
  }

  @Test
  public void testCycleLaw() throws Exception {
    assertEquals(prod(cycle1(2, 4), cycle1(4, 1, 11, 3)), cycle1(2, 4, 1, 11, 3));
  }

}
