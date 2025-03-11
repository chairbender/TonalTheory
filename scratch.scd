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

// "key progression"
(
    var beatsPerKey = 4*4;
    var octaves = [5,4,4,3];
    var keyProg = [
        // I
        TTLine.randomCounterpointLines(beatsPerKey, Key(\c, true), octaves),
        // V
        TTLine.randomCounterpointLines(beatsPerKey, Key(\g, true), octaves),
        // vi
        TTLine.randomCounterpointLines(beatsPerKey, Key(\a, false), octaves),
        // IV
        TTLine.randomCounterpointLines(beatsPerKey, Key(\f, true), octaves)
    ];
    TempoClock.default.tempo = 414/60;
    Pseq(keyProg.collect({|lines|
        TTLine.pPar(lines)
    }), inf).play;
)

(
    TempoClock.default.tempo = 84/60;
    
    p = Pbind(
        \midinote, Pseq([60, 61, 62]),
        \dur, 0.25
    ).play;
)

(
    var a = [1,2,3];
    var b = [[1,2,3],[4,5,6]];
    a.maxSizeAtDepth(0).postln;
    a.maxSizeAtDepth(1).postln;
    b.maxSizeAtDepth(0).postln;
    b.maxSizeAtDepth(1);

)
