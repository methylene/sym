package com.github.methylene.sym;

/**
 * Example class that needs a Comparator as it is not directly Comparable
 */
public class MyInt {

  public static class MyComparator implements java.util.Comparator<MyInt> {
    @Override public int compare(MyInt a, MyInt b) {
//      System.out.println("comparing " + a + " and " + b);
      return a.n - b.n;
    }
  }

  public static final java.util.Comparator<MyInt> COMP = new MyComparator();

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
