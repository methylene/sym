package sorted.sym;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class UtilTest {

  @Test
  public void testCombinations() throws Exception {
    List<int[]> permutations = Util.permutations(3);
    assertEquals(6, permutations.size());
    Set<Permutation> perms = new HashSet<Permutation>();
    for (int[] ints: permutations) {
      assertTrue(perms.add(Permutation.perm1(ints)));
      assertEquals(3, ints.length);
    }
    for (int[] ints: Util.permutations(3)) {
      assertFalse(perms.add(Permutation.perm1(ints)));
    }
  }
}