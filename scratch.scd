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


(
    // generate 4 lines, n measures long
    n = 4;
    ~lines = TTLine.randomCounterpointLines(4*n, Key(\a, false), [5,4,4,3]);
    // prepare for patterns
    ~midinotes = ~lines.collect({|line| line.midinotes});
    ~deltas = ~lines.collect({|line| line.deltas});
    

    //prep the patterns
    ~patterns = ~midinotes.collect({|notes,i|
        Pbind(
            \delta, Pseq(~deltas[i]),
            \sustain, Pseq(~deltas[i]),
            \midinote, Pseq(notes)
        )
    });

    ~patterns.postln;

    // play
    // TODO: better way to ensure synchornized start time
    TempoClock.default.tempo = 414/60;
    Ppar(~patterns).play;

)
~patterns[0].play
~lines[3].postln
~lines.do({|line| line.beats.postln});

(
    ~line = TTLine.randomLowerLine(Key(\a, false), 4, 4*16);
    ~line.postln;
    ~line.beats.postln;
)
(
    ~line = TTLine.randomBasicArpeggiation(4, Key(\a, false));
    ~line.postln;
)

(
    TempoClock.default.tempo = 84/60;
    
    p = Pbind(
        \midinote, Pseq([60, 61, 62]),
        \dur, 0.25
    ).play;
    )
