s.boot;

Quarks.install("https://github.com/smoge/Rational")

// run individual test classes like this
TestTTLine.run;

// scratchpad
var list = TTLine(List[LineNote(\a4, 1)]);
var list2 = TTLine(List[LineNote(\a4, 1)]);
list[0];
list.class == list2.class;
list == list2;
