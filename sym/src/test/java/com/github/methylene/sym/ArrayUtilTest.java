package com.github.methylene.sym;

import static com.github.methylene.sym.CycleUtil.cyclic;
import static com.github.methylene.sym.Permutation.product;
import static com.github.methylene.sym.TestUtil.*;
import static com.github.methylene.sym.Permutation.define;
import static com.github.methylene.sym.Permutation.symmetricGroup;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArrayUtilTest {

  @Test
  public void testCombinations() throws Exception {
    List<Permutation> permutations = symmetricGroup(3).collect(Collectors.toList());
    assertEquals(6, permutations.size());
    Set<Permutation> perms = new HashSet<Permutation>();
    for (Permutation perm : permutations) {
      assertTrue(perms.add(perm));
    }
    for (Permutation perm : symmetricGroup(3).collect(Collectors.toList())) {
      assertFalse(perms.add(perm));
    }
  }

  @Test
  public void testCartesian() throws Exception {
    int total = 0;
    int offDiagonal = 0;
    List<Permutation> a = symmetricGroup(3).collect(Collectors.toList());
    for (Permutation[] permutation : cartesian(a, a)) {
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
    List<Permutation> a = symmetricGroup(5).collect(Collectors.toList());
    List<Permutation> center = center(a);
    assertEquals(1, center.size());
    assertTrue(center.get(0).isIdentity());
  }

  @Test
  public void testClosed() throws Exception {
    Permutation id = Permutation.define(0, 1, 2, 3);
    Permutation p = Permutation.define(1, 0, 2, 3);
    Permutation k = Permutation.define(0, 1, 3, 2);
    Permutation p2 = Permutation.define(1, 2, 0, 3);
    assertTrue(isClosed(Arrays.asList(id)));
    assertTrue(isClosed(Arrays.asList(id, p)));
    assertTrue(isClosed(Arrays.asList(id, p2, p2.pow(2))));
    assertTrue(isClosed(Arrays.asList(id, p, k, Permutation.product(p, k))));
    assertFalse(isClosed(Arrays.asList(id, p2)));
    assertFalse(isClosed(Arrays.asList(p)));
    assertFalse(isClosed(Arrays.asList(id, p, p2)));
    assertTrue(Permutation.product(p, k).pow(2).isIdentity());
  }

  @Test
  public void testCommutator5() throws Exception {
    assertEquals(120l, symmetricGroup(5).count());
    assertTrue(isClosed(symmetricGroup(5).collect(Collectors.toList())));
    assertEquals(60, commutator(symmetricGroup(5).collect(Collectors.toList())).size());
    assertTrue(isClosed(commutator(symmetricGroup(5).collect(Collectors.toList()))));
    assertEquals(60, commutator(commutator(symmetricGroup(5).collect(Collectors.toList()))).size());
    assertTrue(isClosed(commutator(commutator(symmetricGroup(5).collect(Collectors.toList())))));
  }

  @Test
  public void testCommutator4() throws Exception {
    assertEquals(24, symmetricGroup(4).collect(Collectors.toList()).size());
    assertTrue(isClosed(symmetricGroup(4).collect(Collectors.toList())));
    assertEquals(12, commutator(symmetricGroup(4).collect(Collectors.toList())).size());
    assertTrue(isClosed(commutator(symmetricGroup(4).collect(Collectors.toList()))));
    assertEquals(4, commutator(commutator(symmetricGroup(4).collect(Collectors.toList()))).size());
    assertTrue(isClosed(commutator(commutator(symmetricGroup(4).collect(Collectors.toList())))));
    assertEquals(1, commutator(commutator(commutator(symmetricGroup(4).collect(Collectors.toList())))).size());
    assertTrue(isClosed(commutator(commutator(commutator(symmetricGroup(4).collect(Collectors.toList()))))));
  }

  @Test
  public void testCommutatorEven() throws Exception {
    for (int i = 3; i < 7; i += 1) {
      List<Permutation> sym = symmetricGroup(i).collect(Collectors.toList());
      assertEquals(factorial(i), sym.size());
      assertEquals(0, signatureSum(sym));
      assertEquals(sym.size() / 2, signatureSum(commutator(sym)));
    }
  }

  @Test
  public void testDistinctInts() {
    for (int i = 0; i < 1000; i += 1) {
      int[] ints = Rankings.random((int) (Math.random() * 1024));
      assertTrue(isDistinct(ints));
    }
  }

  @Test
  public void testRandomExtreme() {
    int radius = (int) (50 * Math.random()) + 50;
    HashSet<Integer> seen = new HashSet<Integer>(radius);
    for (int i = 0; i < 1000; i += 1) {
      int[] ints = ArrayUtil.randomNumbers(Integer.MIN_VALUE, Integer.MIN_VALUE + radius, 100);
      for (int a: ints) {
        assertTrue(String.format("%d %d %d", radius, a, Integer.MIN_VALUE + radius), a <= Integer.MIN_VALUE + radius);
        seen.add(a);
      }
    }
    for (int i = 0; i <= radius; i++) {
      assertTrue(seen.contains(Integer.MIN_VALUE + i));
    }
    seen = new HashSet<Integer>(radius);
    for (int i = 0; i < 1000; i += 1) {
      int[] ints = ArrayUtil.randomNumbers(Integer.MAX_VALUE - radius, Integer.MAX_VALUE, 100);
      for (int a: ints) {
        assertTrue((Integer.MAX_VALUE - a) + " " + radius, a >= Integer.MAX_VALUE - radius);
        seen.add(a);
      }
    }
    for (int i = 0; i <= radius; i++) {
      assertTrue(seen.contains(Integer.MAX_VALUE - radius));
    }
  }

  @Test
  public void testRandom() {
    for (int radius = 3; radius < 10; radius++) {
      for (int low = -10; low < 4; low++) {
        HashSet<Integer> seen = new HashSet<Integer>(radius);
        for (int i = 0; i < 100; i += 1) {
          int[] ints = ArrayUtil.randomNumbers(low, low + radius, 10);
          for (int a: ints) {
            assertTrue(String.format("%d %d", low, a), a <= low + radius);
            seen.add(a);
          }
        }
        for (int i = 0; i <= radius; i++) {
          assertTrue(seen.contains(low + i));
        }
      }
    }
  }

  @Test
  public void testDuplicateIndexes() {
    int[] ints = duplicateIndexes(new int[]{1, 2, 1});
    assertTrue(Arrays.equals(new int[]{0, 2}, ints) || Arrays.equals(new int[]{2, 0}, ints));
  }

  @Test
  public void testDuplicateIndexes2() throws Exception {
    for (int i = 0; i < 1000; i += 1) {
      int maxNumber = 100;
      int[] ints = ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20));
      int[] pair = duplicateIndexes(ints, 0);
      assertTrue(count(ints, ints[pair[0]]) > 1);
      assertEquals(ints[pair[0]], ints[pair[1]]);
      assertEquals(ArrayUtil.indexOf(ints, ints[pair[0]], 0), pair[0]);
    }
  }

  @Test
  public void testDuplicateIndexes3() {
    int[] ints = {0, 1, 4, 1, 2, 6, 5, 2, 0, 0, 6, 0};
    assertEquals(1, duplicateIndexes(ints, 0)[0]);
  }

  @Test
  public void testDuplicateIndexes4() throws Exception {
    for (int i = 0; i < 1000; i += 1) {
      int maxNumber = 100;
      MyInt[] ints = MyInt.box(ArrayUtil.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      int[] pair = duplicateIndexes(ints, MyInt.COMP);
      assertTrue(count(ints, ints[pair[0]]) > 1);
      assertEquals(ints[pair[0]], ints[pair[1]]);
    }
  }

  @Test
  public void testFactorial() {
    assertEquals(1, factorial(0));
    assertEquals(1, factorial(1));
    assertEquals(2, factorial(2));
    assertEquals(6, factorial(3));
    assertEquals(24, factorial(4));
    assertEquals(120, factorial(5));
    assertEquals(19, factorial(19) / factorial(18));
    assertEquals(20, factorial(20) / factorial(19));
  }

  @Test
  public void testFindCommutator() {
    Permutation p = define(cyclic(1, 2));
    Permutation q = define(cyclic(0, 1));
    assertEquals(define(1, 2, 0), product(p.invert(), q.invert(), p, q));
  }

  @Test
  public void testEvenCommutator() {
    Permutation p = define(cyclic(0, 4, 1));
    Permutation q = define(cyclic(0, 3, 2, 1, 4));
    assertEquals(define(1, 2, 0), product(p.invert(), q.invert(), p, q));
  }

  @Test
  public void testEvenCommutator2() {
    Permutation p = define(cyclic(0, 3, 1));
    Permutation q = define(cyclic(0, 4, 2, 1, 3));
    assertEquals(define(1, 2, 0), product(p.invert(), q.invert(), p, q));
  }

  @Test
  public void testRange() {
    assertArrayEquals(new int[]{10, 9, 8, 7}, ArrayUtil.range(10, 7, true));
    assertArrayEquals(new int[]{10, 9, 8}, ArrayUtil.range(10, 7, false));
    assertArrayEquals(new int[]{7, 8, 9, 10}, ArrayUtil.range(7, 10, true));
    assertArrayEquals(new int[]{7, 8, 9}, ArrayUtil.range(7, 10, false));
    assertArrayEquals(new int[]{7}, ArrayUtil.range(7, 7, true));
    assertArrayEquals(new int[]{}, ArrayUtil.range(7, 7, false));
  }

  @Test
  public void testUnique() {
    int[] a = {8, 5, 7, 2, 9, 4, 1, 6, 0, 3};
    assertTrue(ArrayUtil.isUnique(a));
  }

  @Test
  public void testCut() {
    int[] a = {8, 5, 7, 2, 9, 4, 1, 6, 0, 3};
    assertArrayEquals(new int []{5, 7, 2, 9, 4, 1, 6, 0, 3}, ArrayUtil.cut(a, 0));
    assertArrayEquals(new int []{8, 7, 2, 9, 4, 1, 6, 0, 3}, ArrayUtil.cut(a, 1));
    assertArrayEquals(new int []{8, 5, 7, 2, 9, 4, 1, 6, 0}, ArrayUtil.cut(a, 9));
  }

  @Test
  public void testPaste() {
    int[] a = {8, 5, 7, 2, 9, 4, 1, 6, 0, 3};
    assertArrayEquals(new int []{0, 8, 5, 7, 2, 9, 4, 1, 6, 0, 3}, ArrayUtil.paste(a, 0, 0));
    assertArrayEquals(new int []{8, 0, 5, 7, 2, 9, 4, 1, 6, 0, 3}, ArrayUtil.paste(a, 1, 0));
    assertArrayEquals(new int []{8, 5, 7, 2, 9, 4, 1, 6, 0, 0, 3}, ArrayUtil.paste(a, 9, 0));
    assertArrayEquals(new int []{8, 5, 7, 2, 9, 4, 1, 6, 0, 3, 0}, ArrayUtil.paste(a, 10, 0));
  }

}
