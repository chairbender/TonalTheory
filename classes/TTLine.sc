
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
        ^super.newCopyArgs(lineNotes);
    }

    at { |i|
        ^lineNotes[i];
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
        if (neighborDuration < articulationNote1.duration) {
            Error("neighborDuration " ++ neighborDuration ++ " must be less than duration of first note of articulation " ++ articulationNote1.duration);
        };
        if (articulationNote1.note != articulationNote2.note) {
            Error("articulationIndex must point at first note of an articulation, but did not. note1: " ++ articulationNote1.note ++ " note2: " ++ articulationNote2.note);
        };
        if (interval != \m2 && interval != \M2) {
            Error("interval must be \m2 or \M2, but was: " ++ interval);
        };
        this.rearticulate(articulationIndex, neighborDuration);
        lineNotes[articulationIndex + 1].note = neighborNote;
    }

}
