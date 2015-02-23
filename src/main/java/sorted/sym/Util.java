package sorted.sym;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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

  static List<int[]> permutations(int n) {
    int[] start = new int[n];
    for (int i = 0; i < n; i += 1) {
      start[i] = i + 1;
    }
    Stack<State> stack = new Stack<State>();
    stack.push(new State(new int[0], start));
    List<int[]> result = new LinkedList<int[]>();
    while (!stack.isEmpty()) {
      State state = stack.pop();
      if (state.suffix.length == 0) {
        result.add(state.prefix);
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

}
