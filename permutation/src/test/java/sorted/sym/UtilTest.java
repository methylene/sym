package sorted.sym;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static sorted.sym.Util.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilTest {

  @Test
  public void testCombinations() throws Exception {
    List<Permutation> permutations = permutations(3);
    assertEquals(6, permutations.size());
    Set<Permutation> perms = new HashSet<Permutation>();
    for (Permutation perm: permutations) {
      assertTrue(perms.add(perm));
    }
    for (Permutation perm: permutations(3)) {
      assertFalse(perms.add(perm));
    }
  }

  @Test
  public void testCartesian() throws Exception {
    int total = 0;
    int offDiagonal = 0;
    List<Permutation> a = permutations(3);
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
    List<Permutation> a = permutations(5);
    List<Permutation> center = center(a);
    assertEquals(1, center.size());
    assertTrue(center.get(0).isIdentity());
  }

  @Test
  public void testCommutator5() throws Exception {
    assertEquals(120, permutations(5).size());
    assertTrue(isClosed(permutations(5)));
    assertEquals(60, commutator(permutations(5)).size());
    assertTrue(isClosed(commutator(permutations(5))));
    assertEquals(60, commutator(commutator(permutations(5))).size());
    assertTrue(isClosed(commutator(commutator(permutations(5)))));
  }

  @Test
  public void testCommutator4() throws Exception {
    assertEquals(24, permutations(4).size());
    assertTrue(isClosed(permutations(4)));
    assertEquals(12, commutator(permutations(4)).size());
    assertTrue(isClosed(commutator(permutations(4))));
    assertEquals(4, commutator(commutator(permutations(4))).size());
    assertTrue(isClosed(commutator(commutator(permutations(4)))));
    assertEquals(1, commutator(commutator(commutator(permutations(4)))).size());
    assertTrue(isClosed(commutator(commutator(commutator(permutations(4))))));
  }

}