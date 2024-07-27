/* A note is just a Tonal Pitch Class with an octave.
The symbol takes the form <letter><alterations><octave>    
The octave cannot be above 9, but it can be negative - due to limitations of
allowed characters when using backslash for symbols in SC, it can be denoted with a
minus sign or the character 'n' - for example \an1 is the same as 'a-1'
*/
NoteSymbol {

    *normalize { |note|
        ^note.asString.toLower.replace("#","s").replace("-","n").asSymbol;
    }

    /* returns the tonal pitch class of the note
    i.e. the note without the octave number*/
    *tpc { |note|
        var noteStr = this.normalize(note).asString;
        var neg = if (noteStr.contains("n")) {3} {2};
        ^noteStr[..(noteStr.size-neg)].asSymbol; 
    }

    /* returns the octave of the note as a number */
    *octave { |note|
        var noteStr = this.normalize(note).asString;
        var neg = if (noteStr.contains("n")) {-1} {1};
        ^noteStr[(noteStr.size-1)].asString.interpret * neg;
    }

    /* Midi value of the note. Returns an integer indicating the amount of semitones the
    note is above C-1 (returns negative if below that note) */
    *semis { |note|
        var normalNote = this.normalize(note);
        // how many semitones is the note in relation to C natural?
        var cSemis = \c.semisTo(normalNote.tpc);
        var octaveSemis = (normalNote.octave + 1) * 12;
        ^cSemis + octaveSemis;
    }

    /* Raise or lower (if semis is negative) the semitones of the note by adding or removing
    alterations */
    *alterNote { |note, semis|
        var tpc = note.tpc;
        var octave = note.octave;
        ^tpc.alterTPC(semis).asNote(octave);
    }

    
    /* Return a count of the number of octaves otherNote is
    above note (so negative if otherNote is below). it is considered an octive or more
    based on the actual note name, not the enharmonic pitch. For example, even though
    C3 B#3 is equivalent to C3 C4, it is not counted as an octave. Only C3 C4 would return 1.
    */
    *octavesTo { |note, otherNote|
        ^((8 * (otherNote.octave - note.octave)) + 
            (note.tpc.letterStepsBetween(otherNote.tpc))).div(8);
    }


    /*returns true if note would appear higher on the staff than otherNote (lower letter and octave),
    even if one note is enharmonically higher than the other in pitch (so E#4 would be below Fb4
    even though it has a higher pitch)
    */
    *isAbove { |note, otherNote|
        if (note.octave > otherNote.octave) {^true};
        if (note.octave < otherNote.octave) {^false};
        ^note.tpc.letterStepsBetween(otherNote.tpc) < 0;
    }

    /* Returns the 'natural' note that is steps higher
    than the given pitch class. Handles octave crossings as well. 
    'Natural' here means without an alteration.
    So, if \Bb4 is given, the 1st next natural will be \C5, the 2n next will be D5, etc...
    If \Bs4 is given, the next natural will be \C5.
    Steps may be negative, to move steps DOWN from the given TPC.
    */
    *nextNatural { |note, steps|
        // how many octaves are we crossing (ignoring octave shift from the starting
        // octave)
        var down = steps < 0;
        var sign = if (down) { -1 } { 1 };
        var octaveShift = (steps.abs.div(7)) * sign;
        var stepsShift = (steps.abs % 7) * sign;
        var finalNaturalTPC = note.tpc.nextNaturalTPC(stepsShift);
        // do we cross C?
        var startOctaveIdx = TonalPitchClassSymbol.octaveNaturals[note.tpc.natural];
        var endOctaveIdx = TonalPitchClassSymbol.octaveNaturals[finalNaturalTPC];
        var crossC = if (down) { startOctaveIdx < endOctaveIdx } { startOctaveIdx > endOctaveIdx };
        octaveShift.postln;
        octaveShift = if (crossC) { octaveShift + sign } { octaveShift };
        ^(finalNaturalTPC.asNote(note.octave + octaveShift));
    }
}

+ Symbol {
    noteEquals{ |otherNote| ^NoteSymbol.normalize(this) == NoteSymbol.normalize(otherNote) }
    isAbove{ |otherNote| ^NoteSymbol.isAbove(this, otherNote) }
    octavesTo{ |otherNote| ^NoteSymbol.octavesTo(this, otherNote) }
    nextNaturalNote { |steps| ^NoteSymbol.nextNatural(this, steps) }
    tpc { ^NoteSymbol.tpc(this) }
    octave { ^NoteSymbol.octave(this) }
    semis { ^NoteSymbol.semis(this) }
    alterNote { |semis| ^NoteSymbol.alterNote(this, semis) }
    compoundIntervalTo { |otherNote| ^IntervalSymbol.compoundIntervalBetween(this,otherNote) }
    simpleIntervalTo { |otherNote| ^IntervalSymbol.simpleIntervalBetween(this,otherNote) }
    intervalAbove { |interval| ^IntervalSymbol.noteAbove(interval,this) }
    intervalBelow { |interval| ^IntervalSymbol.noteBelow(interval,this) }
}
