package com.hotels.sym;

import java.util.Arrays;

public final class Permutation {

  final Cycle[] cycles;

  private static final Permutation ONE = new Permutation(new Cycle[0]);

  public Permutation(Cycle[] cycles) {
    this.cycles = cycles;
  }

  public static Permutation cycle(int... ints) {
    return new Permutation(new Cycle[] {new Cycle(ints)});
  }

  public static Permutation comp(Object... objects) {
    if (objects.length == 0) {
      return ONE;
    }
    int n = 0;
    for (Object o : objects) {
      if (o instanceof Cycle) {
        n += 1;
      } else if (o instanceof Permutation) {
        n += ((Permutation) o).cycles.length;
      } else {
        throw new IllegalArgumentException();
      }
    }
    Cycle[] r = new Cycle[n];
    int idx = 0;
    for (Object o : objects) {
      if (o instanceof Cycle) {
        r[idx] = (Cycle) o;
        idx += 1;
      } else if (o instanceof Permutation) {
        System.arraycopy(((Permutation) o).cycles, 0, r, idx, ((Permutation) o).cycles.length);
        idx += ((Permutation) o).cycles.length;
      } else {
        throw new IllegalArgumentException("bad type: " + o);
      }
    }
    return new Permutation(r);
  }

  public Object[] apply() {
    if (cycles.length == 0) {
      return new Object[0];
    }
    int n = -1;
    for (Cycle cycle : cycles) {
      n = Math.max(n, cycle.max());
    }
    Object[] symbols = Cycle.symbols(n);
    for (int i = cycles.length - 1; i >= 0; i -= 1) {
      symbols = cycles[i].apply(symbols);
    }
    return symbols;
  }

  static boolean sorted(Object[] o) {
    if (o.length == 0)
      return true;
    Comparable prev = (Comparable) o[0];
    for (int i = 1; i < o.length; i += 1) {
      if (prev.compareTo(o[i]) >= 0) {
        return false;
      }
    }
    return true;
  }

  public boolean equals(Object o) {
    if (o == null) return false;
    if (o.getClass() == Cycle.class)
      o = comp((Cycle) o);
    if (o.getClass() != Permutation.class)
      return false;
    Object[] thisApply = apply();
    Object[] otherApply = ((Permutation) o).apply();
    return (sorted(thisApply) && sorted(otherApply))
            || Arrays.equals(thisApply, otherApply);
  }

}
