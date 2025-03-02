
/*
A line (TT short for TonalTheory as Line exists
already in SC) is a sequence of line notes. Chords / intervals
are only produced by simlatenously-sounding lines and are
not present in an individual line.
This class provides the general Line object as well as
operations to transform lines within the context of Tonal Theory.

Why not just have Line extend List so we
could easily do all the many operations SC allows?
Because there are operations on List that would break the invariants of what a Line is. Instead, Line encapsulates and tries to maintain those invariants.
*/
TTLine {
    // List of LineNote
    var <lineNotes;

    *new { |lineNotes|
        if (lineNotes.isKindOf(List).not) {
            Error("lineNotes must be a kind of List but was" + lineNotes.class).throw;
        };
        ^super.newCopyArgs(lineNotes);
    }

	== { arg that; ^this.compareObject(that, #[\lineNotes]) }

	hash {
		^this.instVarHash(#[\lineNotes])
	}

    at { |i|
        ^lineNotes[i];
    }

    printOn { |stream|
        lineNotes.printOn(stream);
    }

    /*
    Rearticulate the note at the given index in this line.
    (the note is split into two notes whose total duration equals the original note's duration,
    and who are the same note as the original). The duration of the first note in the
    rearticulation is equal to firstDuration. firstDuration must be a duration less than
    the duration of the rearticulated note in the given line.
    */
    rearticulate { |index, firstDuration|
        var targetNote = lineNotes[index];
        var lastDuration = targetNote.duration - firstDuration;
        lineNotes[index] = LineNote(targetNote.note, firstDuration);
        lineNotes.insert(index + 1, LineNote(targetNote.note, lastDuration));
    }

    /*
    Given an index pointing at the first note of a rearticulation in the line (articulationIndex),
    inserts a neighbor between the rearticulation notes. A neighbor is simply a note a minor or major second
    above or below the two notes, caused by rearticulating the first note
    of the rearticulation, then moving it up or down a second. if 'up' is true, the neighbor will be above, otherwise it will be
    below. The duration of the neighbor will be 'neighborDuration', and it must be less than
    the duration of the first note of the rearticulation. 'interval' must be \m2 or \M2.
    */
    neighbor { |articulationIndex, neighborDuration, up, interval|
        var articulationNote1 = lineNotes[articulationIndex];
        var articulationNote2 = lineNotes[articulationIndex + 1];
        var neighborNote = if (up, { articulationNote1.note.intervalAbove(interval) }, { articulationNote1.note.intervalBelow(interval) });
        if (neighborDuration >= articulationNote1.duration) {
            Error("neighborDuration " ++ neighborDuration ++ " must be less than duration of first note of articulation " ++ articulationNote1.duration).throw;
        };
        if (articulationNote1.note != articulationNote2.note) {
            Error("articulationIndex must point at first note of an articulation, but did not. note1: " ++ articulationNote1.note ++ " note2: " ++ articulationNote2.note).throw;
        };
        if (interval != \m2 && interval != \M2) {
            Error("interval must be \m2 or \M2, but was: " ++ interval).throw;
        };
        this.rearticulate(articulationIndex, neighborDuration);
        lineNotes[articulationIndex + 1].note = neighborNote;
    }

    /*
    Given an index into the line, and a note forming a consonant interval
    with the note at that index (westergaardian theory consonance), returns a new line with
    an arpeggiation performed at that index using the given note.
    In an arpeggiation, the arpeggiated note is rearticulated (with
    firstDuration as the duration of the first note of the rearticulation), and then the second note of the rearticulateion is set to 'arpeggiateNote'. 
    So all of the same requirements of rearticulation also apply to
    this method.
    'up' indicates whether the arpeggiation should go from low
    note to high note or vice versa. 'firstDuration' must be less than the duration of the note
    at the given index in the line. 'arpeggiateNote' must form a westergaardian theory consonant interval with the note being arpeggiated.
    (see the isConsonant method of IntervalSymbol).
    Note that whether the note is consonant depends on whether it is 
    the lowest sounding note (if multiple lines are sounding). 
    As this method has no idea what other lines are sounding, it
    will accept values which are consonant regardless of being the 
    lowest sounding note.
    */
    arpeggiateNote { |index, arpeggiateNote, firstDuration, up|
        var targetNote = lineNotes[index];
        var arpeggiateInterval = targetNote.note.compoundIntervalTo(arpeggiateNote);
        var lowNote = if (arpeggiateNote.isAbove(targetNote.note), { targetNote.note }, { arpeggiateNote });
        var highNote = if (arpeggiateNote.isAbove(targetNote.note), { arpeggiateNote }, { targetNote.note });
        var finalFirstNote = if (up, { lowNote }, { highNote });
        var finalLastNote = if (up, { highNote }, { lowNote });
        if (arpeggiateInterval.isConsonant(true).not && arpeggiateInterval.isConsonant(false).not) {
            Error("interval between targeted note " ++ targetNote.note ++ " and arpeggiateNote " ++ arpeggiateNote ++ " is " ++ arpeggiateInterval ++ " which is not consonant").throw;
        };
        this.rearticulate(index, firstDuration);
        lineNotes[index].note = finalFirstNote;
        lineNotes[index + 1].note = finalLastNote;
    }

    /*
    Like arpeggiateNote, but the interval is specified rather than
    the note of the arpeggiation. The interval is applied to the note
    at the target index to determine the arppegiateNote. When 'up' is true the interval will be up.
    */
    arpeggiateInterval { |index, interval, firstDuration, up|
        var targetNote = lineNotes[index];
        var arpeggiateNote = if (up, {targetNote.note.intervalAbove(interval)}, {targetNote.note.intervalBelow(interval)});
        this.arpeggiateNote(index, arpeggiateNote, firstDuration, up);
    }

    /*
    Returns a list containing the indices of
    notes in the line that are repetitions of the same pitch (the index of the first note
    of the repetition), and thus valid targets of the "neighbor" operation. 
    Durations are not considered when considering valid neighbor indexes.
    */
    validNeighborIndices{
        var result = List[];
        lineNotes.do({ |lineNote, i|
           if (i < (lineNotes.size - 1)) {
            if (lineNote.note == lineNotes[i+1].note) {
                result.add(i);
               };
           }; 
        });
        ^result;
    }

}
