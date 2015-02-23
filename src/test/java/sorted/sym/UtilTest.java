package sorted.sym;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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
    assertEquals(Permutation.IDENTITY, center.get(0));
  }

  @Test
  public void testCommutator() throws Exception {
    Iterable<Permutation> commutator = Util.commutator(Util.permutations(5));
    HashSet<Permutation> commutatorSet = new HashSet<Permutation>();
    for (Permutation p: commutator)
      commutatorSet.add(p);
    assertEquals(60, commutatorSet.size());
  }

}