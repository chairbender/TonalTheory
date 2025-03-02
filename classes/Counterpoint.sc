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

    /*
    Generates a basic arpeggiation for the bass line, using the operation rules
    of Tonal Theory:
    
    -Final and first pitch must be tonics. 
    - The middle pitch must be a fifth above or fourth below the final tonic. 
    -If the middle pitch is more than a fifth from the first pitch, a 
    triad pitch must be inserted between the middle pitch and first pitch to
    make there be no intervals larger than an octave.
        - But that insertion must follow the rules for bass line secondary structure triad inserts.
    - The starting and ending pitches can't be larger than an octave, since that is undefined
    by westergaardian theory 
        - (triad ptiches are supposed to be inserted, but no operation could be performed 
        because any triad pitch insertion would create a skip larger than an octave
        with one of the notes in the event the middle note is chosen to be
        a fourth below the final pitch). 
    - Whole notes will be used for all notes

    key should be a Key indicating the key to use. 
    
    firstOctave should be an integer indicating the octave of the first tonic. It must be within 1 of lastOctave
    
    octaveOffset should be 1, 0, or -1 indicating whether the final tonic will be the same, an octave up, or an octave
    down
    
    isMiddleAboveFinalPitch should be true if the middle note should be a fifth above the final pitch, otherwise 
    it will be a fourth below it. 

    insertNote should be a note that should be put between the first and middle pitch in the event they are more
    than a fifth apart. It must follow the rules for triad inserts for bass line secondary structures. It
    is ignored if the middle note is a fifth or less apart from the first note.
    TODO: What function to call to see valid inserts?

    TODO: refactor so that we return an "intermediate" that allows choosing a valid middle note rather than applying all
        these constraints to it.
    */
    *basicArpeggiation { |key, firstOctave, octaveOffset, isMiddleAboveFinalPitch, insertNote|
        var firstScale = key.scale(firstOctave);
        var lastOctave = firstOctave + octaveOffset;
        var lastScale = key.scale(lastOctave);
        var firstNote = firstScale[0];
        var lastNote = lastScale[0];
        var middleNote = if (isMiddleAboveFinalPitch) {
            lastScale[4];
        } {
            key.scale(lastOctave - 1)[4];
        };
        if (octaveOffset > 1) {
            Error("first and lastOctave must be within 1 of each other, but weren't").throw;
        };
        if (firstNote.compoundIntervalTo(middleNote).intervalNumber > 5) {
            // first to middle interval is greater than a fifth, so need to insert
            // a triad pitch
            ^TTLine(List[LineNote(firstNote,1), LineNote(insertNote,1), LineNote(middleNote,1), LineNote(lastNote,1)])
        } {
            ^TTLine(List[LineNote(firstNote,1), LineNote(middleNote,1), LineNote(lastNote,1)])
        };
    }




}
