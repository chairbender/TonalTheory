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
    // generate 4 lines, n measures long
    n = 128;
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
    TempoClock.default.tempo = 414/60;
    Ppar(~patterns).play;

)
~patterns[0].play
~lines[3].postln
~lines.do({|line| line.beats.postln});


// iterative building
(
    var finalMeasures = 16;
    var beats = finalMeasures * 4;
    var waitTime;
    var mutation;
    var max = 0;
    ~lines = TTLine.startLines(Key(\a, false), [5,4,4,3]);
    ~lines.do({|line| if (line.beats > max) { max = line.beats}});
    ~lines.do({|line| line.mutateUntilBeats(max)});
    // TODO: need to "catch up" the shorter lines - add a "mutateUntilBeats" method.
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
        // TODO: precalculate end time or use some async mechanism, so we dont busy-wait until we need to
        Ppar(~patterns).play;

        // wait
        waitTime = ~lines[0].beats.asFloat * (1 / TempoClock.default.tempo);
        waitTime.wait;

        // mutate
        mutation = TTLine.chooseMutation(beats);
        ~lines.do(mutation);
        ~lines[0].postln;
        (~lines[0].beats).postln;

        // TODO: consolidate "catch up" as well into a method
        // catch up shorter lines
        ~lines.do({|line| if (line.beats > max) { max = line.beats}});
        ~lines.do({|line| line.mutateUntilBeats(max)});
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
