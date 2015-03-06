# sym

Permutation util, enjoy!

    <dependency>
      <groupId>com.github.methylene</groupId>
      <artifactId>sym</artifactId>
      <version>1.1</version>
    </dependency>

### Shuffling an array

    String[] sentence = new String[]{"Check", "out", "this", "great", "library"};
    Permutation.random(sentence.length).apply(sentence);
    => [great, library, Check, this, out]

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

### Searching in an array

Finding the index of a given element `e` in an array of _distinct_ objects `a` is a `O(n)` 
operation at first glance, because we need to do an exhaustive search.

If we search more than once in the same array, a `java.util.Map` 
that maps each element to its position can be used to speed this up.

`Permutation` allows another lightweight way of doing this, 
without having to think about `hashCode` or `capacity`.

We make a sorted copy of `a`, along with the `unsortA` permutation, 
which maps indexes in `sortedA` to their original position in `a`:

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
Notice that `Permutation.sort(a)` will currently throw an `IllegalArgumentException`
if `a` contains duplicates.

### Composition

The following static import is assumed:

    import static com.github.methylene.sym.Permutation.*;

Permutations can be composed, however they must have the same `length`.
The `pad` method can be used to get around this.
In mathematics terms, `p.pad(m)` applies the standard embedding of
`Sym(p.length())` in `Sym(m)`, for `p.length() <= m`.

    char[] abc = { 'a', 'b', 'c' };
    prod(swap(0, 2), swap(0, 1).pad(3)).apply(abc);
    = > cab
