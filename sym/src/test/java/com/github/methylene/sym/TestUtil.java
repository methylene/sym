package com.github.methylene.sym;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class TestUtil {

  static class State {
    final int[] prefix;
    final int[] suffix;

    State(int[] prefix, int[] suffix) {
      this.prefix = prefix;
      this.suffix = suffix;
    }
  }

  /**
   * Returns the list of all possible permutations of given length
   * @param n length of permutations to generate
   * @return all possible permutations of length {@code n}
   */
  static List<Permutation> permutations(int n) {
    int[] start = new int[n];
    for (int i = 0; i < n; i += 1) {
      start[i] = i + 1;
    }
    Stack<State> stack = new Stack<State>();
    stack.push(new State(new int[0], start));
    LinkedList<Permutation> result = new LinkedList<Permutation>();
    while (!stack.isEmpty()) {
      State state = stack.pop();
      if (state.suffix.length == 0) {
        result.push(Permutation.perm1(state.prefix));
      } else {
        for (int i = 0; i < state.suffix.length; i += 1) {
          int[] newPrefix = new int[state.prefix.length + 1];
          System.arraycopy(state.prefix, 0, newPrefix, 0, state.prefix.length);
          newPrefix[state.prefix.length] = state.suffix[i];
          int[] newSuffix = new int[state.suffix.length - 1];
          if (i != 0)
            System.arraycopy(state.suffix, 0, newSuffix, 0, i);
          if (i < state.suffix.length - 1)
            System.arraycopy(state.suffix, i + 1, newSuffix, i, state.suffix.length - 1 - i);
          stack.push(new State(newPrefix, newSuffix));
        }
      }
    }
    return result;
  }

  static Iterable<Permutation[]> cartesian(final List<Permutation> a, final List<Permutation> b) {
    return new Iterable<Permutation[]>() {
      @Override public Iterator<Permutation[]> iterator() {
        return new Iterator<Permutation[]>() {
          int idxa = 0;
          int idxb = 0;

          @Override public boolean hasNext() {
            return idxa < a.size();
          }

          @Override public Permutation[] next() {
            Permutation pa = a.get(idxa);
            Permutation pb = b.get(idxb);
            if (b.size() - idxb == 1) {
              idxb = 0;
              idxa += 1;
            } else {
              idxb += 1;
            }
            return new Permutation[] { pa, pb };
          }

          @Override public void remove() {
            throw new IllegalAccessError();
          }
        };
      }
    };
  }

  static List<Permutation> commutator(final List<Permutation> input) {
    LinkedList<Permutation> result = new LinkedList<Permutation>();
    for (Permutation p : distinct(commutatorIterable(input)))
      result.push(p);
    return result;
  }

  static Iterable<Permutation> commutatorIterable(final List<Permutation> input) {
    return new Iterable<Permutation>() {
      @Override public Iterator<Permutation> iterator() {
        List<Permutation> inlist = Arrays.asList(input.toArray(new Permutation[input.size()]));
        final Iterator<Permutation[]> cartesian = cartesian(inlist, inlist).iterator();
        return new Iterator<Permutation>() {
          @Override public boolean hasNext() {
            return cartesian.hasNext();
          }

          @Override public Permutation next() {
            Permutation[] p = cartesian.next();
            return Permutation.prod(p[0].invert(), p[1].invert(), p[0], p[1]);
          }

          @Override public void remove() {
            throw new IllegalAccessError();
          }
        };
      }
    };
  }

  static <E extends Comparable> Iterable<E> distinct(final Iterable<E> input) {
    return new Iterable<E>() {
      @Override public Iterator<E> iterator() {
        final TreeSet<E> set = new TreeSet<E>();
        final Iterator<E> it = input.iterator();
        return new Iterator<E>() {
          E current = null;

          @Override public boolean hasNext() {
            while (it.hasNext()) {
              E candidate = it.next();
              if (set.add(candidate)) {
                current = candidate;
                return true;
              }
            }
            return false;
          }

          @Override public E next() {
            return current;
          }

          @Override public void remove() {
            throw new IllegalAccessError();
          }
        };
      }
    };
  }

  static List<Permutation> center(final List<Permutation> input) {
    LinkedList<Permutation> result = new LinkedList<Permutation>();
    outer:
    for (Permutation a : input) {
      for (Permutation b : input)
        if (!a.comp(b).equals(b.comp(a)))
          continue outer;
      result.push(a);
    }
    return result;
  }

  static boolean isClosed(final List<Permutation> permutations) {
    TreeSet<Permutation> set = new TreeSet<Permutation>();
    for (Permutation p : permutations)
      set.add(p);
    for (Permutation[] p : cartesian(permutations, permutations))
      if (!set.contains(p[0].comp(p[1])) || !set.contains(p[1].comp(p[0])))
        return false;
    return true;
  }


  static int[] duplicateIndexes(int[] input) {
    int max = 0;
    for (int i : input)
      max = Math.max(max, i);
    int[] test = new int[max + 1];
    Arrays.fill(test, -1);
    int i = (int) (Math.random() * input.length);
    for (int k = 0; k < input.length; k += 1) {
      if (test[(int) input[i]] == -1) {
        test[(int) input[i]] = i;
      } else {
        return new int[] { test[(int) input[i]], i };
      }
      if (k > input.length)
        break;
      i += 1;
      i = i % input.length;
    }
    return new int[0];
  }

  static int[] duplicateIndexes(Object[] input, Comparator comp) {
    Map<Object, Integer> test = new TreeMap<Object, Integer>(comp);
    for (int i = 0; i < input.length; i += 1) {
      if (!test.containsKey(input[i])) {
        test.put(input[i], i);
      } else {
        return new int[] { test.get(input[i]), i };
      }
    }
    return new int[0];
  }

  static int[] duplicateIndexes(long[] input) {
    int max = 0;
    for (long i : input)
      max = (int) Math.max(max, i);
    int[] test = new int[max + 1];
    Arrays.fill(test, -1);
    int i = (int) (Math.random() * input.length);
    for (int k = 0; k < input.length; k += 1) {
      if (test[(int) input[i]] == -1) {
        test[(int) input[i]] = i;
      } else {
        return new int[] { test[(int) input[i]], i };
      }
      if (k > input.length)
        break;
      i += 1;
      i = i % input.length;
    }
    return new int[0];
  }

  static int[] randomNumbers(int maxNumber, int length) {
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1) {
      result[i] = (int) (maxNumber * Math.random());
    }
    return result;
  }

  static int count(int[] a, int i) {
    int c = 0;
    for (int j: a)
      if (j == i)
        c += 1;
    return c;
  }

  static int count(Object[] a, Object i) {
    int c = 0;
    for (Object j: a)
      if (j.equals(i))
        c += 1;
    return c;
  }

  @Test public void testDuplicateIndexes() {
    int[] ints = duplicateIndexes(new int[]{1, 2, 1});
    assertTrue(Arrays.equals(new int[]{0, 2}, ints) || Arrays.equals(new int[]{2, 0}, ints));
  }

}
