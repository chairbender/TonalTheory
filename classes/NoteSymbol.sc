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
}

+ Symbol {
    noteEquals{ |otherNote| ^NoteSymbol.normalize(this) == NoteSymbol.normalize(otherNote) }
    tpc { ^NoteSymbol.tpc(this) }
    octave { ^NoteSymbol.octave(this) }
    semis { ^NoteSymbol.semis(this) }
    alterNote { |semis| ^NoteSymbol.alterNote(this, semis) }
}
