/*
Counterpoint operations on lines.
*/
Counterpoint {

    /*
    For a key (as defined by its diatonic root TPC), starting note, and ending note,
    returns a line consisting of whole notes where the notes form a step motion
    using the diatonic degrees of the key from the starting note to the ending note
    (including the start and end note). both notes must
    have pitch classes that are the key's diatonic degrees (no accidentals)
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
            Error("diatonic collection starting on " ++ keyDiatonicRootTPC ++ " does not contain TPC of start note " ++ startNote);
        };
        if (diatonicCollection.indexOf(endNote.tpc).isNil) {
            Error("diatonic collection starting on " ++ keyDiatonicRootTPC ++ " does not contain TPC of end note " ++ endNote);
        };
        // walk from start to end, following the diatonic degrees
        while { iterations < 100 && currentNote != endNote } {
            var nextNatural = currentNote.nextNatural(step);
            currentNote = currentNote.alterNote(alterationsDict[nextNatural.tpc]);
            result.add(currentNote);
            iterations = iterations + 1;
        };
        ^result;
    }

}
