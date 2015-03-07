package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilTest {

  @Test
  public void testCombinations() throws Exception {
    List<Permutation> permutations = Util.permutations(3);
    assertEquals(6, permutations.size());
    Set<Permutation> perms = new HashSet<Permutation>();
    for (Permutation perm: permutations) {
      assertTrue(perms.add(perm));
    }
    for (Permutation perm: Util.permutations(3)) {
      assertFalse(perms.add(perm));
    }
  }

  @Test
  public void testCartesian() throws Exception {
    int total = 0;
    int offDiagonal = 0;
    List<Permutation> a = Util.permutations(3);
    for (Permutation[] permutation: Util.cartesian(a, a)) {
      total += 1;
      if (permutation[0] != permutation[1]) {
        offDiagonal += 1;
      }
    }
    assertEquals(36, total);
    assertEquals(30, offDiagonal);
  }

  @Test
  public void testCenter() throws Exception {
    List<Permutation> a = Util.permutations(5);
    List<Permutation> center = Util.center(a);
    assertEquals(1, center.size());
    assertTrue(center.get(0).isIdentity());
  }

  @Test
  public void testClosed() throws Exception {
    Permutation id = Permutation.perm1(1, 2, 3, 4);
    Permutation p = Permutation.perm1(2, 1, 3, 4);
    Permutation k = Permutation.perm1(1, 2, 4, 3);
    Permutation p2 = Permutation.perm1(2, 3, 1, 4);
    assertTrue(Util.isClosed(Arrays.asList(id)));
    assertTrue(Util.isClosed(Arrays.asList(id, p)));
    assertTrue(Util.isClosed(Arrays.asList(id, p2, p2.pow(2))));
    assertTrue(Util.isClosed(Arrays.asList(id, p, k, Permutation.prod(p, k))));
    assertFalse(Util.isClosed(Arrays.asList(id, p2)));
    assertFalse(Util.isClosed(Arrays.asList(p)));
    assertFalse(Util.isClosed(Arrays.asList(id, p, p2)));
    assertTrue(Permutation.prod(p, k).pow(2).isIdentity());
  }

  @Test
  public void testCommutator5() throws Exception {
    Assert.assertEquals(120, Util.permutations(5).size());
    assertTrue(Util.isClosed(Util.permutations(5)));
    Assert.assertEquals(60, Util.commutator(Util.permutations(5)).size());
    assertTrue(Util.isClosed(Util.commutator(Util.permutations(5))));
    Assert.assertEquals(60, Util.commutator(Util.commutator(Util.permutations(5))).size());
    assertTrue(Util.isClosed(Util.commutator(Util.commutator(Util.permutations(5)))));
  }

  @Test
  public void testCommutator4() throws Exception {
    Assert.assertEquals(24, Util.permutations(4).size());
    assertTrue(Util.isClosed(Util.permutations(4)));
    Assert.assertEquals(12, Util.commutator(Util.permutations(4)).size());
    assertTrue(Util.isClosed(Util.commutator(Util.permutations(4))));
    Assert.assertEquals(4, Util.commutator(Util.commutator(Util.permutations(4))).size());
    assertTrue(Util.isClosed(Util.commutator(Util.commutator(Util.permutations(4)))));
    Assert.assertEquals(1, Util.commutator(Util.commutator(Util.commutator(Util.permutations(4)))).size());
    assertTrue(Util.isClosed(Util.commutator(Util.commutator(Util.commutator(Util.permutations(4))))));
  }

  @Test public void testDuplicateIndexes() {
    int[] ints = Util.duplicateIndexes(new int[] { 1, 2, 1 });
    assertArrayEquals(new int[]{0, 2}, ints);
  }

}