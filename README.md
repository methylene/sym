# sym

Useful tools for working with sorted data.

Suppose you have the following

    int[] x = new int[]{ 4, 6, 10, -5, 195, 33, 2 };
    int[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y);

x is an array of mutually distinct ints. y is a sorted copy of x.
Consider the following problem.
Given a position k in y, we want to find the original position of y[k].
An exhaustive search will solve this:

    int indexOf(int[] x, int el) {
      for (int i = 0; i < x.length; i += 1) {
        if (x[i] == el) return i;
      }
      throw new IllegalArgumentException("not in x: " + el);
    }

    assertEquals(x[indexOf(x, y[k])], y[k]);


The method indexOf solves the problem, but its runtime grows with the length of x.
We can do better!
Let P be the permutation that sorts x, i.e. x[i] = y[P i] for all indexes i.
Then

    P i = k
    i = P^-1 k

So we can find i by applying the inverse of P, which is a constant time operation.
You can use the Permutation class to find the inverse of P like this:

    Permutation unsort = Permutation.sort(x).invert();

This solves the problem, as shown by the following test:

    for (int k = 0; k < y.length; k += 1) {
      assertEquals(indexOf(x, y[k]), unsort.apply(k));
    }

