package sorted.sym;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

class Util {

  static final Comparator<int[]> COMPARE_2ND = new Comparator<int[]>() {
    public int compare(int[] a, int[] b) {
      return a[1] - b[1];
    }
  };

  static int[] pad(int[] unpadded, int targetLength) {
    if (targetLength <= unpadded.length)
      return unpadded;
    int[] result = new int[targetLength];
    System.arraycopy(unpadded, 0, result, 0, unpadded.length);
    for (int i = unpadded.length; i < targetLength; i += 1) {
      result[i] = i;
    }
    return result;
  }

  static void validate(int[] ints) {
    int[] poscnt = new int[ints.length];
    for (int i : ints) {
      if (i < 0 || i >= ints.length)
        throw new IllegalArgumentException("invalid input: " + Arrays.toString(ints));
      if (poscnt[i] != 0)
        throw new IllegalArgumentException("invalid input: " + Arrays.toString(ints));
      poscnt[i] = 1;
    }
  }

  static String[] symbols(int n) {
    String[] r = new String[n];
    String s = "a";
    for (int i = 0; i < n; i += 1) {
      r[i] = s;
      s = nextString(s);
    }
    return r;
  }

  static String nextString(String s) {
    char last = s.charAt(s.length() - 1);
    if (last == 'z') {
      int nflip = 1;
      while (s.length() > nflip
              && s.charAt(s.length() - 1 - nflip) == 'z')
        nflip += 1;
      if (nflip == s.length()) {
        StringBuilder news = new StringBuilder();
        for (int i = 0; i < nflip; i += 1)
          news.append('a');
        return news.append('a').toString();
      } else {
        StringBuilder news = new StringBuilder(s.substring(0, s.length() - nflip - 1));
        news.append((char) (s.charAt(s.length() - 1 - nflip) + 1));
        for (int i = 0; i < nflip; i += 1)
          news.append('a');
        return news.toString();
      }
    } else {
      return s.substring(0, s.length() - 1) + ((char) (last + 1));
    }
  }

  static class State {
    final int[] prefix;
    final int[] suffix;

    State(int[] prefix, int[] suffix) {
      this.prefix = prefix;
      this.suffix = suffix;
    }
  }

  static List<Permutation> permutations(int n) {
    int[] start = new int[n];
    for (int i = 0; i < n; i += 1) {
      start[i] = i + 1;
    }
    Stack<State> stack = new Stack<State>();
    stack.push(new State(new int[0], start));
    List<Permutation> result = new LinkedList<Permutation>();
    while (!stack.isEmpty()) {
      State state = stack.pop();
      if (state.suffix.length == 0) {
        result.add(Permutation.perm1(state.prefix));
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
      @Override
      public Iterator<Permutation[]> iterator() {
        return new Iterator<Permutation[]>() {
          int idxa = 0;
          int idxb = 0;

          @Override
          public boolean hasNext() {
            return idxa < a.size();
          }

          @Override
          public Permutation[] next() {
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

          @Override
          public void remove() {
            throw new IllegalAccessError();
          }
        };
      }
    };
  }


  public static List<Permutation> commutator(final List<Permutation> input) {
    List<Permutation> result = new LinkedList<Permutation>();
    for (Permutation p : distinct(commutatorIterable(input)))
      result.add(p);
    return result;
  }

  public static Iterable<Permutation> commutatorIterable(final List<Permutation> input) {
    return new Iterable<Permutation>() {
      @Override
      public Iterator<Permutation> iterator() {
        List<Permutation> inlist = Arrays.asList(input.toArray(new Permutation[input.size()]));
        final Iterator<Permutation[]> cartesian = cartesian(inlist, inlist).iterator();
        return new Iterator<Permutation>() {
          @Override
          public boolean hasNext() {
            return cartesian.hasNext();
          }

          @Override
          public Permutation next() {
            Permutation[] p = cartesian.next();
            return Permutation.prod(p[0].invert(), p[1].invert(), p[0], p[1]);
          }

          @Override
          public void remove() {
            throw new IllegalAccessError();
          }
        };
      }
    };
  }

  public static <E extends Comparable> Iterable<E> distinct(final Iterable<E> input) {
    return new Iterable<E>() {
      @Override
      public Iterator<E> iterator() {
        final TreeSet<E> set = new TreeSet<E>();
        final Iterator<E> it = input.iterator();
        return new Iterator<E>() {
          E current = null;

          @Override
          public boolean hasNext() {
            while (it.hasNext()) {
              E candidate = it.next();
              if (set.add(candidate)) {
                current = candidate;
                return true;
              }
            }
            return false;
          }

          @Override
          public E next() {
            return current;
          }

          @Override
          public void remove() {
            throw new IllegalAccessError();
          }
        };
      }
    };
  }

  public static List<Permutation> center(final List<Permutation> input) {
    LinkedList<Permutation> result = new LinkedList<Permutation>();
    outer:
    for (Permutation a : input) {
      for (Permutation b : input)
        if (!a.comp(b).equals(b.comp(a))) continue outer;
      result.add(a);
    }
    return result;
  }

}
