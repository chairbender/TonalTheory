/*
Symbol wrapper for key, for easier syntax - \c for c minor, \C for c major.
*/
+ Symbol {
    noteEquals{ |otherNote| ^NoteSymbol.normalize(this) == NoteSymbol.normalize(otherNote) }
    isAbove{ |otherNote| ^NoteSymbol.isAbove(this, otherNote) }
    octavesTo{ |otherNote| ^NoteSymbol.octavesTo(this, otherNote) }
    nextNaturalNote { |steps| ^NoteSymbol.nextNatural(this, steps) }
    tpc { ^NoteSymbol.tpc(this) }
    octave { ^NoteSymbol.octave(this) }
    semis { ^NoteSymbol.semis(this) }
    midi { ^NoteSymbol.semis(this) }
    alterNote { |semis| ^NoteSymbol.alterNote(this, semis) }
    compoundIntervalTo { |otherNote| ^IntervalSymbol.compoundIntervalBetween(this,otherNote) }
    simpleIntervalTo { |otherNote| ^IntervalSymbol.simpleIntervalBetween(this,otherNote) }
    intervalAbove { |interval| ^IntervalSymbol.noteAbove(interval,this) }
    intervalBelow { |interval| ^IntervalSymbol.noteBelow(interval,this) }
}

