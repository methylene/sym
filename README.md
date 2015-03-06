# sym

Permutation util, enjoy!

    <dependency>
      <groupId>com.github.methylene</groupId>
      <artifactId>sym</artifactId>
      <version>1.0</version>
    </dependency>

### Shuffling an array

    String[] sentence = new String[]{"Check", "out", "this", "great", "library"};
    Permutation.random(sentence.length).apply(sentence);
    => [great, library, Check, this, out]

### Unsorting

Suppose you have the following

    int[] x = new int[]{ 4, 6, 10, -5, 195, 33, 2 };
    int[] y = Arrays.copyOf(x, x.length);
    Arrays.sort(y);

`x` is an array of distinct comparable objects.
`y` is a sorted copy of `x`.
Consider the following problem:
Given an element `y[k]`, we want to find its original position in `x`.
An exhaustive search will solve it:

    int indexOf(int[] x, int el) {
      for (int i = 0; i < x.length; i += 1) {
        if (x[i] == el) return i;
      }
      throw new IllegalArgumentException("not in x: " + el);
    }

    assertEquals(x[indexOf(x, y[k])], y[k]);


The method indexOf solves the problem, but its runtime grows with the length of `x`.
There is a faster way.
Let `P` be the permutation that sorts `x`, i.e. `x[i] == y[P(i)]` for all indexes `i`.
Then

    P i = k
    i = P^-1 k

So we can find `i` by applying the inverse of `P`, which is a constant time operation.
We can use the Permutation class to find the inverse of `P` like this:

    Permutation unsort = Permutation.sort(x).invert();

This is equivalent to the exhaustive search, as shown by the following test:

    for (int k = 0; k < y.length; k += 1) {
      assertEquals(indexOf(x, y[k]), unsort.apply(k));
    }

### CSV data

Suppose you have headered CSV data like this:

    String[] header = new String[]{"country", "area_km2", "population_density_km2", "gdp_per_capita_usd"};
    Object[] row1 = new Object[]{"UK", 243610, 255.6, 38309};
    Object[] row2 = new Object[]{"Lithuania", 65300, 45, 28245};

For some reason, you want to change the column order to "country", "population_density_km2", "gdp_per_capita_usd", "area_km2".
It can be done like this:

    String[] newHeader = new String[]{"country", "population_density_km2", "gdp_per_capita_usd", "area_km2"};
    Permutation reorder = Permutation.from(header, newHeader);
    reorder.apply(row1);
    => [UK, 255.6, 38309, 243610]
    reorder.apply(row2);
    => [Lithuania, 45, 28245, 65300]

Notice how the `Permutation.from(Comparable[], Comparable[])` method is implemented using the unsorting trick. This allows it to run in `O(n log(n))` time.


### Unsorting and binarySearch

Unsorting gives an efficient `indexOf` method for arrays.

How to quickly find the index of a given element `e` in an array (of distinct comparables) `a`?

It's worthwhile keeping a sorted copy of `a` around, along with `a`'s unsort Permutation:

    String[] a = new String[]{"x", "f", "v", "c", "n"};
    Permutation sortA = Permutation.sort(a);
    Permutation unsortA = sortA.invert();
    String[] sortedA = sortA.apply(a);

Making `sortedA` and `unsortA` instance variables then allows the following implementation:

    public int indexOf(String e) {
      return unsortA.apply(Arrays.binarySearch(sortedA, e));
    }

which is roughly as fast as `Arrays.binarySearch`; `unsortA.apply` is just an array lookup.

### Composition

The following static import is assumed:

    import static com.github.methylene.sym.Permutation.*;

Permutations can be composed, as long as they have the same `length`.
The `pad` method can be used to increase the `length` of a given permutation.
In mathematics terms, the `pad` method applies the standard embedding of
`Sym(n)` in `Sym(m)` for `m >= n`.

    char[] abc = { 'a', 'b', 'c' };
    prod(swap(0, 2), swap(0, 1).pad(3)).apply(abc);
    = > cab
