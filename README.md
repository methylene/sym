# sym

Permutations for Java. Maven coordinates:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.1</version>
</dependency>
````

# Shuffling an array

````java
String[] sentence = new String[]{"Check", "out", "this", "great", "library"};
Permutation.random(sentence.length).apply(sentence);
// => [great, library, Check, this, out]
````

# Changing column order

Assuming we have a bunch of CSV and a header

````java
String[] header = new String[]{"country", "area", "pop", "gdp"};
Object[] row1 = new Object[]{"UK", 243610, 255.6, 38309};
Object[] row2 = new Object[]{"Lithuania", 65300, 45, 28245};
````

Given a rearrangement of the header fields, 
we can use `Permutation.from` to find the permutation that rearranges `header` into `newHeader`

````java
String[] newHeader = new String[]{"country", "pop", "gdp", "area"};
Permutation rearrange = Permutation.factory().from(header, newHeader);
````

Now we can use the same permutation on the rows

````java
rearrange.apply(row1);
// => [UK, 255.6, 38309, 243610]
rearrange.apply(row2);
// => [Lithuania, 45, 28245, 65300]
````

# Searching an array

First we get a permutation that sorts `a`

````java
String[] a = new String[]{"a", "f", "v", "x", "x", "n"};
Permutation sort = Permutation.factory().sort(a);
````
Now we use the `sort` permutation to sort `a`, and also calculate the permutation that undoes the sorting

````java
String[] sorted = sort.apply(a);
Permutation unsort = sort.invert();
````
Now we can get the index of a given string in `a` as follows:

````java
int i = Arrays.binarySearch(sorted, "x");
unsort.apply(i);
// => 3
````

# Composition

Permutations can be composed using the `comp` method.

````java
char[] bca = new char[]{ 'b', 'c', 'a' };
Permutation t02 = Permutation.swap(0, 2);
Permutation t01 = Permutation.swap(0, 1);
System.out.println(t02.comp(t01).apply(bca));
// = > abc
````

# Padding

Padding is a generic way to build longer permutations from shorter ones.

Indexes `i >= p.length(), i < m` are not moved by a padded permutation `p.padding(m)`:

````java
Permutation cycle = Permutation.cycle(0, 1, 2, 3);
System.out.println(cycle.apply(0));
// => 1
System.out.println(cycle.apply(3));
// => 0
System.out.println(cycle.padding(10).apply(6));
// => 6
````

In version 1.9.0, implicit padding was added to the `comp` and `apply` methods,
so it's rarely necessary to apply padding explicitly.


# Cycle decomposition, orbits etc

Consider the following

````java
static int[] findCycle(Permutation p) {
  for (int i = 0; i < p.length(); i += 1)
    if (p.orbit(i).length > 1)
      return p.orbit(i);
  return null;
}

public static void main(String[] args) {
  Permutation s = Permutation.factory().sort("Hello world!");
  for (Permutation p: s.toCycles())
    System.out.println(Arrays.toString(findCycle(p)));
}
````

This finds a permutation that sorts the string `"Hello world!"` (there are several)
and then prints its decomposition into <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycles</a>:

    [7, 9]
    [1, 4, 8, 10, 3, 6, 11]
    [0, 2, 5]

This can be verified by the following code:

````java
Permutation c = Permutation.prod(cycle(9, 7),
                                 cycle(11, 6, 3, 10, 8, 4, 1),
                                 cycle(5, 2, 0));
System.out.println(c.apply(" !Hdellloorw"));
// => Hello world!
````

For more ideas, see the [javadoc](http://methylene.github.io/sym/current/com/github/methylene/sym/package-summary.html).
