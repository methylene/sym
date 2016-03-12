### Getting all permutations (symmetric group)
````java
System.out.println(Permutation.symmetricGroup(5).count());
// => 120
````

### Shuffling an array

````java
String[] a = {"Check", "out", "this", "swish", "library"};
a = Permutation.random(a.length).apply(a);
System.out.println(Arrays.toString(a));
// => [this, library, Check, swish, out]
````

### Composition

````java
Permutation s02 = Transposition.swap(0, 2).toPermutation();
Permutation s01 = Transposition.swap(0, 1).toPermutation();
System.out.println(s02.compose(s01).apply("bca"));            
// = > abc
````
### Cycle decomposition, orbits etc

````java
Permutation s = Permutation.sort("Hello world!");
System.out.println(s.toCycles());
// => [[0, 2, 5] [1, 4, 8, 10, 3, 6, 11] [7, 9]]
````

See also the [javadoc](http://methylene.github.io/sym/current/com/github/methylene/sym/package-summary.html)

<!--
* add Kendall tau distance
* add rank: http://rosettacode.org/wiki/Permutations/Rank_of_a_permutation
-->
