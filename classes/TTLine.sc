
/*
A line (TT short for TonalTheory as Line exists
already in SC) is a sequence of line notes in the context of a particular key. Chords / intervals
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
    // Key the line is in
    var <key;
    // type of the line within the counterpoint - \primary (upper), \secondary (other upper), \lower (lower)
    // Note that both primary and secondary lines are considered "upper lines"
    var <type;

    *new { |lineNotes, key, type|
        if (lineNotes.isKindOf(List).not) {
            Error("lineNotes must be a kind of List but was" + lineNotes.class).throw;
        };
        ^super.newCopyArgs(lineNotes, key, type);
    }

	== { arg that; ^this.compareObject(that, #[\lineNotes, \key, \type]) }

	hash {
		^this.instVarHash(#[\lineNotes, \key, \type])
	}

    at { |i|
        ^lineNotes[i];
    }

    printOn { |stream|
        lineNotes.printOn(stream);
    }

    /*
    Number of notes in this line;
    */
    size {
        ^(lineNotes.size);
    }

    isUpperLine {
        ^((type == \primary) || (type == \secondary));
    }

    isLowerLine {
        ^(this.isUpperLine.not);
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
    validNeighborIndices {
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

    /*
    Returns an array containing the notes
    that would be valid to insert before the note at
    the given index in the line
    TODO: Note that currently, we assume if the line is a lower line, it is the lowest sounding note - otherwise
    it is NOT the lowest sounding note. So this assumes there are no "voice crossings". We should make sure this assumption actually holds.
    */
    validTriadInsertsBefore { |index|
        var note = lineNotes[index].note;
        var prevNote = if (index == 0) { note } { lineNotes[index-1].note };
        var validTriads = key.triadPitchesNear(note);
        // check for consonance with the note that will be before and after the triad insert
        ^(validTriads.select({ |triadNote|
            var prevInterval = prevNote.compoundIntervalTo(triadNote);
            var afterInterval = note.compoundIntervalTo(triadNote); 
            prevInterval.isConsonant(this.isLowerLine) &&
                afterInterval.isConsonant(this.isLowerLine) &&
                (afterInterval.intervalNumber <= 8);
        }));
    }

    /*
    Returns a Dictionary of integers to array of notes, where
    each integer represents an index in targetLine (of a note), and
    the list it maps to indicates the notes that could be inserted
    BEFORE that note,, following the rules of westergaardian theory counterpoint
    for the upper and bass lines.

    The rules are:
    - Any triad pitch may precede the first pitch or be inserted between
    two consecutive pitches as long as:
        - no dissonant skip
        - no skip larger than an octave is created 
        - (and we'll consider a perfect fourth skip in an upper line
    to be consonant). 
    
    If this is an upper line being
    true also means inserts are allowed before the first note, otherwise they aren't
    */
    validTriadInserts {
        // iterate through the line and find all possible places to insert,
        // considering only inserting at the start if its an upper line
        var start = if (this.isUpperLine) { 0 } { 1 };
        var result = Dictionary();
        for (start, lineNotes.size - 1) { |i|
            var inserts = this.validTriadInsertsBefore(i);
            if (inserts.size != 0) {
                result.put(i, inserts);
            };
        };
        ^result;
    }

    /*
    Returns an list containing the indexes of notes where the
    following note forms a skip (interval a 3rd or larger) with the note at the specified index,
    and where the number of notes that would be inserted between the two notes
    in a step motion is <= limit."
    */
    validStepMotionInserts { |limit|
        var result = List[];
        for (0, lineNotes.size-2) {|i|
            var curNote = lineNotes[i].note;
            var nextNote = lineNotes[i+1].note;
            var interval = curNote.compoundIntervalTo(nextNote);

            if ((interval.intervalNumber <= (limit + 2)) &&
                (interval.intervalNumber > 2)) {
                result.add(i);
            };
        };
        ^result;
    }

    /*
    Returns an array containing the indexes of notes that are triad pitches of the key,
    meaning those notes are allowed to be repeated w.r.t. the rules of counterpoint.
    */
    validTriadRepeats {
        ^(lineNotes.selectIndices({|lineNote,i|
            var noteDegree = key.noteDegree(lineNote.note);
            (noteDegree == 0) || (noteDegree == 2) || (noteDegree == 4);
        }));
    }

    /*
    Returns a step motion TTLine starting on the first note and ending on the ending note, all
    notes being a whole note.
    It's allowed for startNote / endNote to have alterations (i.e. accidentals) beyond that implied by the key!
    
    The rules for upper and lower line step motion are followed in generating this (the rules are the same):
    
    Uses diatonic degrees except for special cases where it uses the raised 6th or 7th degree in a minor key:
    - a rising step motion from the 5th degree to the tonic
    - a rising step motion from the 5th degree to the 7th
    - a falling step motion from the raised seventh to the fifth
    */
    *counterpointStepMotion { |startNote, endNote, key|
        // since startNote or endNote could have alterations, we need to generate a diatonicStepMotion
        // using their UN-altered form!
        var startNoteUnaltered = key.unalteredNote(startNote);
        var endNoteUnaltered = key.unalteredNote(endNote);
        var line = this.fullDiatonicStepMotion(key, startNoteUnaltered, endNoteUnaltered);
        var startDegree = key.noteDegree(startNoteUnaltered);
        var endDegree = key.noteDegree(endNoteUnaltered);
        line.postln;
        startDegree.postln;
        endDegree.postln;
        // use raised 6th or 7th in case any of the special conditions are met
        if (key.isMajor.not) {
            // minor key
            case {endNote.isAbove(startNote)} {
                // rising motion
                case {(startDegree == 4) && (endDegree == 0)} {
                    //rising from 5th to tonic,
                    // raise the 6th and 7th degree
                    line[1].note = line[1].note.alterNote(1);
                    line[2].note = line[2].note.alterNote(1);
                }
                {(startDegree == 4) && (endDegree == 6)} {
                    // rising from fifth to 7th
                    // raise the 6th
                    line[1].note = line[1].note.alterNote(1);
                };
            } {startNote.isAbove(endNote) && (endDegree == 4)} {
                var lowerStartNote = startNote.alterNote(-1);
                // use a function as the 2nd operand for short-circuiting
                if (key.containsNote(lowerStartNote) && 
                    { key.noteDegree(lowerStartNote) == 6 }) {
                    // falling from raised 7th  to the 5th
                    // raise the 6th
                    line[1].note = line[1].note.alterNote(1);
                };
            };
        };
        // restore the alterations to the start / end note
        // since the step motion was generated with their unaltered forms
        line[0].note = startNote;
        line[line.size-1].note = endNote;
        line.postln;
        ^line;
    }

    /*
    For a key, starting note, and ending note,
    returns a TTLine consisting of whole notes where the notes form a step motion
    using the diatonic degrees of the key from the starting note to the ending note
    (including the start and end note). both notes must
    have pitch classes that are the key's diatonic degrees (no accidentals).
    This is typically used as a kind of building block to iterate upon in order to build a complete counterpoint.
    */
    *fullDiatonicStepMotion { |key, startNote, endNote|
        var step = if (endNote.isAbove(startNote)) { 1 } { -1};
        var alterationsDict = KeySignature.alterationSemisDictOfKey(key);
        var degrees = key.degrees;
        // safeguard against infinite loop
        var iterations = 0;
        var result = List[];
        var currentNote = startNote;
        if (degrees.indexOf(startNote.tpc).isNil) {
            Error("key " ++ key ++ " does not contain TPC of start note " ++ startNote).throw;
        };
        if (degrees.indexOf(endNote.tpc).isNil) {
            Error("key " ++ key ++ " does not contain TPC of end note " ++ endNote).throw;
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
    *diatonicStepMotion { |key, startNote, endNote|
        var fullStepMotion = this.fullDiatonicStepMotion(key, startNote, endNote);
        fullStepMotion.lineNotes.pop;
        fullStepMotion.lineNotes.removeAt(0);
        ^fullStepMotion;
    }

    /*
    Given a Key and starting octave,
    returns a TTLine that follows the operational rules:

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
        ^this.fullDiatonicStepMotion(key, startNote, scale[0])
    }

    /*
    Generates a basic arpeggiation as a TTLine for the bass line, using the operation rules
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
