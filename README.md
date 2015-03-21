# sym

## Permutation and LookupList

Permutations are objects that encapsulate certain operations on arrays and lists,
such as sorting, shuffling, swapping two elements, or moving an element.

Maven dependency:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.4</version>
</dependency>
````

`LookupList` builds on permutations to provide an immutable implementation of `java.util.List` 
that is optimized for searching.

Its `.indexOf`, `.lastIndexOf` and `.contains` methods will often perform much better than 
other array based lists.

Additionally, `LookupList` provides an efficient `partition` method.

A comparison between LookupList and ArrayList can be made as follows:

    $ mvn clean test -f sym/pom.xml -Pbench

This will print something like
    
    Running com.github.methylene.lists.PerformanceTest
    == list size: 10000
    == repeat: 50
    == return value of .indexOf: -1
    indexOf:                 2626.92
    indexOfjdK:            249112.92
    lastIndexOf:             2224.66
    lastIndexOfJdk         657190.04
    index_relative:     0.0105450973
    lastIndex_relative: 0.0033851091
