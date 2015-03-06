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
Given an index `k` in `y`, we want to find the original position in `x` of `y[k]`.

An exhaustive search will solve it:

    int originalIndex(int k) {
      for (int i = 0; i < x.length; i += 1) {
        if (x[i] == y[k]) return i;
      }
      throw new IllegalArgumentException("not in x: " + el);
    }


This method finds the original index, but its runtime depends on the length of `x`.
There is a faster way.
Let `P` be the permutation that sorts `x`, i.e. `x[i] == y[P(i)]` for all indexes `i`.
Then

    P i = k
    i = P^-1 k

So we can find `i` by applying the inverse of `P`.
We can use the Permutation class to find the inverse of `P` like this:

    Permutation unsortX = Permutation.sort(x).invert();

Then the following constant time method is equivalent:

    int originalIndex(int k) {
      unsortX.apply(k);
    }

### Changing colum order

Suppose you have a bunch of CSV rows and a header:

    String[] header = new String[]{"country", "area", "pop", "gdp"};
    Object[] row1 = new Object[]{"UK", 243610, 255.6, 38309};
    Object[] row2 = new Object[]{"Lithuania", 65300, 45, 28245};

The columns are easily rearranged:

    String[] newHeader = new String[]{"country", "pop", "gdp", "area"};
    Permutation reorder = Permutation.from(header, newHeader);
    reorder.apply(row1);
    => [UK, 255.6, 38309, 243610]
    reorder.apply(row2);
    => [Lithuania, 45, 28245, 65300]

Notice how the `Permutation.from(Comparable[], Comparable[])` method is implemented using the unsorting trick. 
This allows it to run in `O(n log(n))` time.


### Searching in an array

Unsorting gives an efficient `indexOf` method for arrays.

How to quickly find the index of a given element `e` in an array (of distinct comparables) `a`?
One way is to build a `java.util.Map` that maps each element to its position.
Boxing all the indexes to use as `Map` values may not be the most elegant solution.
Fortunately `Permutation` allows another lightweight way of doing this.
We make a sorted copy of `a`, along with the `unsortA` permutation:

    String[] a = new String[]{"x", "f", "v", "c", "n"};
    Permutation sortA = Permutation.sort(a);
    Permutation unsortA = sortA.invert();
    String[] sortedA = sortA.apply(a);

This allows the following implementation

    public int indexOf(String e) {
      int i = Arrays.binarySearch(sortedA, e);
      return i < 0 ? i : unsortA.apply(i);
    }

which is roughly as fast as `Arrays.binarySearch`; `unsortA.apply(i)` is just an array lookup.

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
