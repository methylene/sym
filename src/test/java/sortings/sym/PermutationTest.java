package sortings.sym;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Test;

public class PermutationTest {

  @Test
  public void testApply() throws Exception {
    Assert.assertArrayEquals(new String[]{"c", "a", "b"}, Permutation.comp(Permutation.cycle(1, 2), Permutation.cycle(2, 3)).apply());
    Assert.assertArrayEquals(new String[]{"c", "a", "b"}, Permutation.cycle(1, 2, 3).apply());
    Assert.assertArrayEquals(new String[]{"a", "c", "b"}, Permutation.comp(Permutation.cycle(1, 2),
            Permutation.comp(Permutation.cycle(1, 2), Permutation.cycle(2, 3))).apply());
  }

  @Test
  public void testEquals() throws Exception {
    Assert.assertEquals(Permutation.comp(), Permutation.comp(Permutation.cycle(1, 2), Permutation.cycle(2, 1)));
    Assert.assertEquals(Permutation.cycle(2, 3), Permutation.comp(Permutation.cycle(1, 2),
            Permutation.comp(Permutation.cycle(1, 2), Permutation.cycle(2, 3))));
  }

}
