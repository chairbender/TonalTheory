s.boot;

Quarks.install("https://github.com/smoge/Rational")

// run all tests in my project
(
TestDiatonicCollection.run;
TestIntervalSymbol.run;
TestKey.run;
TestKeySignature.run;
TestLineNote.run;
TestNoteSymbol.run;
TestTonalPitchClassSymbol.run;
TestTTLine.run;
)

// simple example
(
    TempoClock.default.tempo = 414/60;
    // generate 4 lines, n measures long
    n = 4;
    ~lines = TTLine.randomCounterpointLines(4*n, Key(\a, false), [5,4,4,3]);
    TTLine.play(~lines);
)

(
    TempoClock.default.tempo = 414/60;
    TTLine.evolve(Key(\c, true), [5,4,4,3], 8*4);
)

(
    TempoClock.default.tempo = 84/60;
    
    p = Pbind(
        \midinote, Pseq([60, 61, 62]),
        \dur, 0.25
    ).play;
    )
