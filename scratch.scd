s.boot;

Quarks.install("https://github.com/smoge/Rational")

// run individual test classes like this
TestCounterpoint.run;
TestNoteSymbol.run;
TestTTLine.run;
TestKey.run;

[3, 4, 5, 6, 7, 1, 2].rotate(2).postln;

// scratchpad
var line = Counterpoint.fullDiatonicStepMotion(\c, \c4, \b4);
line[0].note;

Platform.userExtensionDir;

