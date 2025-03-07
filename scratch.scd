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
    // generate 4 lines, 16 notes long (says nothing about the number of measures!)
    ~lines = TTLine.randomCounterpointLines(16, Key(\c, false), [5,4,4,3]);
    // prepare for patterns
    ~midinotes = ~lines.collect({|line| line.midinotes});
    ~deltas = ~lines.collect({|line| line.deltas});
    

    //prep the patterns
    ~patterns = ~midinotes.collect({|notes,i|
        Pbind(
            \delta, Pseq(~deltas[i]),
            \midinote, Pseq(notes)
        )
    });

    ~patterns.postln;

    // play
    // TODO: better way to ensure synchornized start time
    TempoClock.default.tempo = 212/60;
    ~patterns.do({|pattern| pattern.play });
)
~deltas[0].postln
~midinotes[0].postln
~patterns[0].play

(
    TempoClock.default.tempo = 84/60;
    
    p = Pbind(
        \midinote, Pseq([60, 61, 62]),
        \dur, 0.25
    ).play;
    )
