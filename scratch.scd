s.boot;

Quarks.install("https://github.com/smoge/Rational")

// run individual test classes like this
TestCounterpoint.run;
TestNoteSymbol.run;
TestTTLine.run;

// scratchpad
var line = Counterpoint.fullDiatonicStepMotion(\c, \c4, \b4);
line[0].note;

Platform.userExtensionDir;

