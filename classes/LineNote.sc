/*
A line note is a note in a "line", which is a sequence
of notes each with a given duration. So a line note
is just a note with a duration. Since we are dealing
with music theory and not just note events, a duration
has to be precise and not a floating point value. Therefore,
to define note duration, we use the Rational quark and define
notes in terms of fractions of the measure.
For e.g. 1 %/ 4 is a quarter note.
TODO: For convenience probably want some built in symbols
for common note durations like quarter, whole, etc...
*/
LineNote {
    // as a Note symbol i.e. \a4
    var <>note;
    // as a Rational fraction i.e. 1 %/ 4
    var <>duration;

    *new { |note, duration|
        ^super.newCopyArgs(note, duration);
    }
}

