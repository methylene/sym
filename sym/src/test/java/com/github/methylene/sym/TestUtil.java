package com.github.methylene.sym;

import java.util.*;

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
  static List<Permutation> sym(int n) {
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
        result.push(Permutation.define(Util.add(state.prefix, -1)));
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
            return new Permutation[]{pa, pb};
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
            return Permutation.product(p[0].invert(), p[1].invert(), p[0], p[1]);
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
        if (!a.compose(b).equals(b.compose(a)))
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
      if (!set.contains(p[0].compose(p[1])) || !set.contains(p[1].compose(p[0])))
        return false;
    return true;
  }



  static int count(int[] a, int i) {
    int c = 0;
    for (int j : a)
      if (j == i)
        c += 1;
    return c;
  }

  static int count(Object[] a, Object i) {
    int c = 0;
    for (Object j : a)
      if (j.equals(i))
        c += 1;
    return c;
  }

  static int signatureSum(List<Permutation> permutations) {
    int result = 0;
    for (Permutation p : permutations)
      result += p.toCycles().signature();
    return result;
  }



  static <E extends Comparable> boolean isDistinct(Iterable<E> input) {
    Iterable<E> distinct = distinct(input);
    Iterator it = distinct.iterator();
    for(E __: input) {
      if (!it.hasNext())
        return false;
      it.next();
    }
    assert !it.hasNext();
    return true;
  }

  static <E extends Comparable> boolean isDistinct(int[] input) {
    int max = 0;
    for (int i: input) {
      if (i < 0)
        Util.negativeFailure();
      max = Math.max(max, i);
    }
    boolean[] test = new boolean[max + 1];
    for (int i: input) {
      if (test[i])
        return false;
      test[i] = true;
    }
    return true;
  }

  /**
   * Find a pair of duplicate indexes.
   * @param input
   * @param start the index where to start looking in the array
   * @return A pair {@code i, j} of indexes so that {@code input[i] == input[j]}
   * @throws java.lang.IllegalArgumentException if no duplicates were found
   */
  public static int[] duplicateIndexes(int[] input, int start) {
    int max = 0;
    for (int j : input)
      max = Math.max(max, j);
    int[] test = new int[max + 1];
    Arrays.fill(test, -1);
    for (int __ : input) {
      if (test[input[start]] == -1)
        test[input[start]] = start;
      else
        return new int[]{test[input[start]], start};
      start = (start + 1) % input.length;
    }
    throw new IllegalArgumentException("no duplicates found");
  }

  public static int[] duplicateIndexes(int[] input) {
    return duplicateIndexes(input, (int) (Math.random() * input.length));
  }

  public static int[] duplicateIndexes(Object[] input, Comparator comp) {
    @SuppressWarnings("unchecked")
    Map<Object, Integer> test = new TreeMap<Object, Integer>(comp);
    int start = (int) (Math.random() * input.length);
    for (Object __ : input) {
      if (!test.containsKey(input[start])) {
        test.put(input[start], start);
      } else {
        return new int[]{test.get(input[start]), start};
      }
      start = (start + 1) % input.length;
    }
    throw new IllegalArgumentException("no duplicates found");
  }

  public static int[] duplicateIndexes(long[] input, int start) {
    int max = 0;
    for (long j : input)
      max = Math.max(max, (int) j);
    int[] test = new int[max + 1];
    Arrays.fill(test, -1);
    for (long __ : input) {
      if (test[(int) input[start]] == -1)
        test[(int) input[start]] = start;
      else
        return new int[]{test[(int) input[start]], start};
      start = (start + 1) % input.length;
    }
    throw new IllegalArgumentException("no duplicates found");
  }

  public static int[] duplicateIndexes(long[] input) {
    return duplicateIndexes(input, (int) (Math.random() * input.length));
  }

  /**
   * Calculates the factorial.
   * @param n a nonnegative number
   * @return the factorial of {@code n}
   * @throws java.lang.IllegalArgumentException if n is negative
   */
  public static long factorial(int n) {
    if (n < 0) {throw new IllegalArgumentException("negative number is not allowed");}
    if (n > 20) {throw new IllegalArgumentException("preventing long overflow");}
    long seed = 1;
    for (int i = 1; i <= n; i += 1)
      seed = seed * i;
    return seed;
  }

  static Permutation[] findCommutator(Permutation permutation, List<Permutation> group) {
    for (Permutation[] p: cartesian(group, group)) {
      if (Permutation.product(p[0].invert(), p[1].invert(), p[0], p[1]).equals(permutation))
        return p;
    }
    return null;
  }

}
