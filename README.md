# sym

Permutations for Java. Get Maven:

    <dependency>
      <groupId>com.github.methylene</groupId>
      <artifactId>sym</artifactId>
      <version>1.5</version>
    </dependency>

### Shuffling an array

    String[] sentence = new String[]{"Check", "out", "this", "great", "library"};
    Permutation.random(sentence.length).apply(sentence);
    => [great, library, Check, this, out]

### Changing column order

Assuming we have a bunch of CSV and a header

    String[] header = new String[]{"country", "area", "pop", "gdp"};
    Object[] row1 = new Object[]{"UK", 243610, 255.6, 38309};
    Object[] row2 = new Object[]{"Lithuania", 65300, 45, 28245};

Given a rearrangement of the header fields, 
it's easy to apply the same reordering to the data rows using `Permutation.from`:

    String[] newHeader = new String[]{"country", "pop", "gdp", "area"};
    Permutation reorder = Permutation.factory().from(header, newHeader);
    reorder.apply(row1);
    => [UK, 255.6, 38309, 243610]
    reorder.apply(row2);
    => [Lithuania, 45, 28245, 65300]

### Searching in an array

Finding the index of a given element `e` in an array `a` is a `O(n)` 
operation at first glance, because we need to do an equality test on each element of `a` in sequence.

If we search more than once in the same array, a HashMap 
that maps each element to its position could be used to speed this up.
Here's another way of doing it.

We make a sorted copy of `a`, along with the `unsortA` permutation,
which maps indexes in `sortedA` back to their original position in `a`.

    String[] a = new String[]{"a", "f", "v", "x", "x", "n"};
    Permutation sortA = Permutation.factory().sort(a);
    Permutation unsortA = sortA.invert();
    String[] sortedA = sortA.apply(a);

`sortedA` is equal to what `Arrays.sort(a)` would produce.
However `sortA.apply(a)` returns a copy and leaves `a` unchanged.

Now we can locate a given string in `a` as follows

    int i = Arrays.binarySearch(sortedA, "x");
    unsortA.apply(i);
    => 3

### Composition

The following static import is assumed:

    import static com.github.methylene.sym.Permutation.*;

Permutations can be composed using `prod`, `comp` or `pow`. 
`prod` and `comp` will throw an `IllegalArgumentException` if the arguments differ in `length`.
The `pad` method can be used to get around this restriction.

    char[] bca = new char[]{ 'b', 'c', 'a' };
    Permutation srt = Permutation.factory().sort(bca);
    srt.pow(3).isIdentity();
    => true

    Permutation hardWork = prod(swap(0, 2), swap(0, 1).pad(3));
    hardWork.apply(bca);
    = > abc

    hardWork.equals(srt);
    => true

Indexes `i >= p.length(), i < m` are not moved by a padded permutation:

    random(3).pad(4).apply(3)
    => 3

# Signature

Finding the [signature](http://en.wikipedia.org/wiki/Parity_of_a_permutation) is easy:

    Permutation.random(5).signature();
    => -1

# Cycle decomposition, orbits etc

For more ideas, see the [javadoc](http://methylene.github.io/sym/)
