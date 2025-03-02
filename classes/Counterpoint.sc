/*
Counterpoint operations on lines.
*/
Counterpoint {

    /*
    For a key (as defined by its diatonic root TPC), starting note, and ending note,
    returns a line consisting of whole notes where the notes form a step motion
    using the diatonic degrees of the key from the starting note to the ending note
    (including the start and end note). both notes must
    have pitch classes that are the key's diatonic degrees (no accidentals).
    This is typically used as a kind of building block to iterate upon in order to build a complete counterpoint.
    */
    *fullDiatonicStepMotion { |keyDiatonicRootTPC, startNote, endNote|
        var step = if (endNote.isAbove(startNote)) { 1 } { -1};
        var alterationsDict = KeySignature.alterationSemisDict(keyDiatonicRootTPC);
        var diatonicCollection = DiatonicCollection.ofRoot(keyDiatonicRootTPC);
        // safeguard against infinite loop
        var iterations = 0;
        var result = List[];
        var currentNote = startNote;
        if (diatonicCollection.indexOf(startNote.tpc).isNil) {
            Error("diatonic collection starting on " ++ keyDiatonicRootTPC ++ " does not contain TPC of start note " ++ startNote).throw;
        };
        if (diatonicCollection.indexOf(endNote.tpc).isNil) {
            Error("diatonic collection starting on " ++ keyDiatonicRootTPC ++ " does not contain TPC of end note " ++ endNote).throw;
        };
        // walk from start to end, following the diatonic degrees
        while { iterations < 100 && currentNote != endNote } {
            var nextNatural = currentNote.nextNaturalNote(step);
            result.add(LineNote(currentNote, 1));
            currentNote = nextNatural.alterNote(alterationsDict[nextNatural.tpc]);
            iterations = iterations + 1;
        };
        result.add(LineNote(currentNote, 1));

        ^(TTLine(result));
    }

    /*
    fullDiatonicStepMotion with the start and end notes omitted
    */
    *diatonicStepMotion { |keyDiatonicRootTPC, startNote, endNote|
        var fullStepMotion = this.fullDiatonicStepMotion(keyDiatonicRootTPC, startNote, endNote);
        fullStepMotion.lineNotes.pop;
        fullStepMotion.lineNotes.removeAt(0);
        ^fullStepMotion;
    }

    /*
    Given a Key and starting octave,
    returns a line that follows the operational rules:

    1. The final pitch in the basic step motion must be a tonic.
    2. The first pitch must be a tonic triad member a third fifth or octave
    above the final pitch.
    3. These two pitches must be joined by inserting the pitches of intervening diatonic degrees to form
    a descending step motion.

    To put it in simpler terms, this method creates a full diatonic step motion. The first note
    is a 3rd, 5th, or octave above the tonic, and the last note is the tonic. It is filled in via diatonic degrees.
    Every note is a whole note.

    key is the Key to use (e.g. Key(\c, false))
    octave is the octave to play the ending tonic on. firstNoteIntervalNumber
    is either 3, 5, or 8 (representing the first note being a major or minor 3rd (based on the key being major or minor), perfect fifth, or perfect octave above the tonic)."
    */
    *basicStepMotion{ |key, octave, firstNoteIntervalNumber|
        var scale = key.scale(octave);
        var startNote = switch (firstNoteIntervalNumber,
            3, {scale[2]},
            5, {scale[4]},
            8, {scale[0].intervalAbove(\P8)},
            { Error("invalid firstNoteIntervalNumber - must be 3, 5, or 8").throw});
        ^this.fullDiatonicStepMotion(key.tonicTPC, startNote, scale[0])
    }


}
