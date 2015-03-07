package com.github.methylene.sym;

import java.util.Comparator;

public class MyInt {

  public static class Comparator implements java.util.Comparator<MyInt> {
    @Override public int compare(MyInt a, MyInt b) {
      return a.n - b.n;
    }
  }

  public static final java.util.Comparator<MyInt> COMP = new Comparator();

  public final int n;

  public MyInt(int n) {
    this.n = n;
  }

  @Override public boolean equals(Object o) {
    return (this == o) || (o != null && (o.getClass() == getClass()) && ((MyInt) o).n == n);
  }

  @Override public int hashCode() {
    return n;
  }
}
