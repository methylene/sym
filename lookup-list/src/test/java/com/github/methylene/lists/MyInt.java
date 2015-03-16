package com.github.methylene.lists;

/**
 * Example class that needs a Comparator as it is not directly Comparable
 */
public class MyInt {

  public static class MyComparator implements java.util.Comparator<MyInt> {
    @Override public int compare(MyInt a, MyInt b) {
      return a.n - b.n;
    }
  }

  public static class MyNullFriendlyComparator implements java.util.Comparator<MyInt> {
    @Override public int compare(MyInt a, MyInt b) {
      if (a == null)
        return b == null ? 0 : -1;
      if (b == null)
        return 1;
      return a.n - b.n;
    }
  }

  public static final java.util.Comparator<MyInt> COMP = new MyComparator();
  public static final java.util.Comparator<MyInt> NULL_FRIENDLY_COMP = new MyNullFriendlyComparator();

  static MyInt[] box(int[] a) {
    MyInt[] result = new MyInt[a.length];
    for (int i = 0; i < result.length; i += 1) {
      result[i] = new MyInt(a[i]);
    }
    return result;
  }

  public final int n;

  public MyInt(int n) {
    this.n = n;
  }

  public String toString() {
    return Integer.toString(n);
  }

  @Override public boolean equals(Object o) {
    return (this == o) || (o != null && (o.getClass() == getClass()) && ((MyInt) o).n == n);
  }

  @Override public int hashCode() {
    return n;
  }
}
