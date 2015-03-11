package com.github.methylene.sym;

import static com.github.methylene.sym.TestUtil.cartesian;
import static com.github.methylene.sym.TestUtil.center;
import static com.github.methylene.sym.TestUtil.commutator;
import static com.github.methylene.sym.TestUtil.isClosed;
import static com.github.methylene.sym.TestUtil.sym;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.github.methylene.sym.TestUtil.signatureSum;
import static com.github.methylene.sym.TestUtil.factorial;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilTest {

  @Test
  public void testCombinations() throws Exception {
    List<Permutation> permutations = sym(3);
    assertEquals(6, permutations.size());
    Set<Permutation> perms = new HashSet<Permutation>();
    for (Permutation perm: permutations) {
      assertTrue(perms.add(perm));
    }
    for (Permutation perm: sym(3)) {
      assertFalse(perms.add(perm));
    }
  }

  @Test
  public void testCartesian() throws Exception {
    int total = 0;
    int offDiagonal = 0;
    List<Permutation> a = sym(3);
    for (Permutation[] permutation: cartesian(a, a)) {
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
    List<Permutation> a = sym(5);
    List<Permutation> center = center(a);
    assertEquals(1, center.size());
    assertTrue(center.get(0).isIdentity());
  }

  @Test
  public void testClosed() throws Exception {
    Permutation id = Permutation.perm1(1, 2, 3, 4);
    Permutation p = Permutation.perm1(2, 1, 3, 4);
    Permutation k = Permutation.perm1(1, 2, 4, 3);
    Permutation p2 = Permutation.perm1(2, 3, 1, 4);
    assertTrue(isClosed(Arrays.asList(id)));
    assertTrue(isClosed(Arrays.asList(id, p)));
    assertTrue(isClosed(Arrays.asList(id, p2, p2.pow(2))));
    assertTrue(isClosed(Arrays.asList(id, p, k, Permutation.prod(p, k))));
    assertFalse(isClosed(Arrays.asList(id, p2)));
    assertFalse(isClosed(Arrays.asList(p)));
    assertFalse(isClosed(Arrays.asList(id, p, p2)));
    assertTrue(Permutation.prod(p, k).pow(2).isIdentity());
  }

  @Test
  public void testCommutator5() throws Exception {
    assertEquals(120, sym(5).size());
    assertTrue(isClosed(sym(5)));
    assertEquals(60, commutator(sym(5)).size());
    assertTrue(isClosed(commutator(sym(5))));
    assertEquals(60, commutator(commutator(sym(5))).size());
    assertTrue(isClosed(commutator(commutator(sym(5)))));
  }

  @Test
  public void testCommutator4() throws Exception {
    assertEquals(24, sym(4).size());
    assertTrue(isClosed(sym(4)));
    assertEquals(12, commutator(sym(4)).size());
    assertTrue(isClosed(commutator(sym(4))));
    assertEquals(4, commutator(commutator(sym(4))).size());
    assertTrue(isClosed(commutator(commutator(sym(4)))));
    assertEquals(1, commutator(commutator(commutator(sym(4)))).size());
    assertTrue(isClosed(commutator(commutator(commutator(sym(4))))));
  }

  @Test
  public void testCommutatorEven() throws Exception {
    for (int i = 3; i < 7; i += 1) {
      List<Permutation> sym = sym(i);
      assertEquals(factorial(i), sym.size());
      assertEquals(0, signatureSum(sym));
      assertEquals(sym.size() / 2, signatureSum(commutator(sym)));
    }
  }

  @Test
  public void testDistinctInts() {
    for (int i = 0; i < 1000; i += 1) {
      int[] ints = Util.distinctInts((int) (Math.random() * 1024), (int) (Math.random() * 10) + 2);
      assertTrue(TestUtil.isDistinct(ints));
    }
  }

}
