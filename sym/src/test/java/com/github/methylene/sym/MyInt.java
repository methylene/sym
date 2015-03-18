package com.github.methylene.sym;

/**
 * Example class that has a Comparator but is not directly Comparable
 */
public class MyInt {

  public static class MyComparator implements java.util.Comparator<MyInt> {
    @Override public int compare(MyInt a, MyInt b) {
      return a.n - b.n;
    }
  }

  public static final java.util.Comparator<MyInt> COMP = new MyComparator();

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
