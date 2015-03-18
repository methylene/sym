# sym

## Permutations and lookup lists

[Permutations](sym) are objects that encapsulate certain operations on arrays and lists,
such as sorting, shuffling, swapping two elements, or moving an element.

Maven dependency:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.4</version>
</dependency>
````
[Lookup lists](lookup-list) are built on permutations.
These are immutable `java.util.List` implementations with efficient `.indexOf`, `.lastIndexOf` and `.contains` methods.

