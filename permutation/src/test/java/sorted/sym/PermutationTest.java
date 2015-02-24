package sorted.sym;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static sorted.sym.Permutation.cycle1;
import static sorted.sym.Permutation.identity;
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
    assertTrue(prod(p.invert(), p).isIdentity());
    assertTrue(prod(p, p.invert()).isIdentity());
    assertTrue(prod(p, p.pow(2)).isIdentity());
    assertTrue(prod(p.pow(2), p).isIdentity());
    assertTrue(prod().isIdentity());
    assertTrue(p.pow(0).isIdentity());
    assertFalse(p.pow(1).isIdentity());
    assertFalse(p.pow(2).isIdentity());
    assertEquals(p.pow(0), p.pow(3));
    assertEquals(p.pow(2), prod(p, p));
    assertEquals(p.pow(1), p);
    assertEquals(p.pow(-1), prod(p, p));
    assertEquals(p.pow(-1), p.invert());
    assertEquals(p.pow(2), p.comp(p));
    assertArrayEquals(new Object[]{"a", "b", "c"}, prod(p, p.invert()).apply());
  }

  @Test
  public void testIdentity() {
    assertTrue(identity(5).isIdentity());
    assertTrue(identity(5).invert().isIdentity());
    assertTrue(identity(0).invert().isIdentity());
    assertTrue(prod(prod(), identity(2)).isIdentity());
    assertEquals(5, identity(5).length());
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
    assertTrue(prod(cycle1(1, 2), cycle1(2, 1)).isIdentity());
    assertEquals(cycle1(2, 3), prod(cycle1(1, 2),
            prod(cycle1(1, 2), cycle1(2, 3))));
  }

  @Test
  public void testCycleLaw() throws Exception {
    assertEquals(prod(cycle1(2, 4), cycle1(4, 1, 11, 3)), cycle1(2, 4, 1, 11, 3));
  }

}
