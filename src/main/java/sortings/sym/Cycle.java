package sortings.sym;

import java.util.Arrays;

final class Cycle {

  private final int[] indexes; // 0 - based
  private final int[] sorted; // sort ( indexes )
  private final int[] pos; // sorted[i] = indexes[pos[i]]

  static int min(int[] ints) {
    int r = ints[0];
    for (int i : ints)
      r = Math.min(r, i);
    return r;
  }

  static int max(int[] ints) {
    int r = ints[0];
    for (int i : ints)
      r = Math.max(r, i);
    return r;
  }

  static void validate(int[] ints) {
    if (min(ints) < 1) {
      throw new IllegalArgumentException("less than 1: " + Arrays.toString(ints));
    }
    int[] test = new int[max(ints) + 1];
    for (int i : ints) {
      if (test[i] == 0) {
        test[i] = 1;
      } else {
        throw new IllegalArgumentException("repeated: " + i);
      }
    }
  }

  // rotate min element into start position
  static int[] rotate(int[] ints) {
    int min = min(ints);
    int idx = -1;
    for (int i = 0; i < ints.length; i += 1) {
      if (ints[i] == min) {
        idx = i;
        break;
      }
    }
    int[] r = new int[ints.length];
    int invidx = ints.length - idx;
    for (int i = 0; i < ints.length; i += 1) {
      r[(i + invidx) % ints.length] = ints[i];
    }
    return r;
  }

  public Cycle(int[] ints) {
    validate(ints);
    ints = rotate(ints);
    int[] indexes = new int[ints.length];
    for (int i = 0; i < ints.length; i += 1) {
      indexes[i] = ints[i] - 1;
    }
    this.indexes = indexes;
    int[] sorted = Arrays.copyOf(indexes, indexes.length);
    Arrays.sort(sorted);
    this.sorted = sorted;
    int[] pos = new int[indexes.length];
    for (int i = 0; i < pos.length; i += 1) {
      pos[Arrays.binarySearch(sorted, indexes[i])] = i;
    }
    this.pos = pos;
  }

  public Object[] apply(Object... o) {
    Object[] r = new Object[o.length];
    int current = 0;
    for (int i = 0; i < o.length; i += 1) {
      if (Arrays.binarySearch(sorted, i) < 0) {
        r[i] = o[i];
      } else {
        int newpos = (pos[current] + 1) % pos.length;
        r[indexes[newpos]] = o[i];
        current += 1;
      }
    }
    return r;
  }

  public Object[] apply() {
    return apply(symbols(max(indexes) + 1));
  }

  public static String[] symbols(int n) {
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

  public int length() {
    return indexes.length;
  }

  public int max() {
    return max(indexes) + 1; // +1 to make it 1 - based
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(indexes[0] + 1);
    for (int i = 1; i < indexes.length; i += 1) {
      sb.append(' ').append(indexes[i] + 1);
    }
    return '(' + sb.substring(1) + ')';
  }

}
