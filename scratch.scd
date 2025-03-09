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


// iterative building
(
    var finalMeasures = 16;
    var beats = finalMeasures * 4;
    var waitTime;
    var mutation;
    var max = 0;
    ~lines = TTLine.startLines(Key(\a, false), [5,4,4,3]);
    TTLine.mutateUntilSameLength(~lines);
    TempoClock.default.tempo = 414/60;

    while {~lines[0].beats < beats} {
        // TODO: consolidate this into a utility function in the class itself
        // audition
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
        //play
        // TODO: precalculate end time or use some async mechanism, so we dont busy-wait until we need to and we start
        // exactly on time
        Ppar(~patterns).play;

        // wait
        waitTime = ~lines[0].beats.asFloat * (1 / TempoClock.default.tempo);
        waitTime.wait;

        // mutate
        mutation = TTLine.chooseMutation(beats);
        ~lines.do(mutation);
        ~lines[0].postln;
        (~lines[0].beats).postln;
        TTLine.mutateUntilSameLength(~lines);

    };
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
    //play
    Ppar(~patterns).play;

)



(
    TempoClock.default.tempo = 84/60;
    
    p = Pbind(
        \midinote, Pseq([60, 61, 62]),
        \dur, 0.25
    ).play;
    )
