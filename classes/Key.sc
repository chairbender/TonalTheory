/*
A specific key, just as in normal music theory. It is defined by a tonic tonal pitch class and a mode.
As a reminder, the mode indicates the tonic triad (I III V) of the scale of this key.
*/
Key {
    // as a tonal pitch class symbol i.e. \a
    var <tonicTPC;
    // boolean - major (true) or minor (false)
    var <isMajor;
    

    *new { |tonicTPC, isMajor|
        ^super.newCopyArgs(tonicTPC, isMajor);
    }

    == { arg that; ^this.compareObject(that, #[\tonicTPC, \isMajor]) }

	hash {
		^this.instVarHash(#[\tonicTPC, \isMajor])
	}

    printOn { |stream|
        stream << "Key(" << tonicTPC << "," << isMajor << ")";
    }

    isMinor {
        ^(isMajor.not);
    }

    /*
    Returns the degrees of the Key as an array of TPCs
    */
    degrees {
        if (isMajor) {
            ^DiatonicCollection.ofRoot(tonicTPC);
        };
        // construct the minor from the relative major
        ^(DiatonicCollection.ofRoot(this.relativeMajorTonicTPC).rotate(2));
    }

    /*
    Just like degrees, but converted to the notes of a single-octave scale starting on the indicated octave.
    */
    scale { |octave|
        var curOctave = octave;
        ^(this.degrees.collect({|tpc, i| 
            if (i != 0 && tpc.natural == \c) {
                curOctave = curOctave + 1;
            };
            
            tpc.asNote(curOctave);
        }));
    }

    /*
    Returns the index in the degrees of the scale where "c" occurs - i.e. where the
    octave change occurs (would be 0 for c major).
    */
    cIndex { 
        ^this.degrees.detectIndex({|tpc| tpc.natural == \c});
    }

    /*
    Returns the octave of the scale that note appears in, assuming note
    is in the key. I.e. the result of this could be passed to scale(result)
    and note would be expected to exist in the resulting scale.
    example - a minor oct 3
    a3 b3 c4 d4 e4 f4 g4
    So scaleOctave(a3) would be 3 and scaleOctave c4 would be 3
    octave index = 2
    */
    scaleOctave { |note|
        var degrees = this.degrees;
        var degreeIndex = degrees.indexOf(note.tpc);
        var cIndex = this.cIndex;
        if (degreeIndex.isNil) {
            Error("note" + note + "is not in the key").throw;
        };
        ^(if ((cIndex != 0) && (degreeIndex >= cIndex)) {
            note.octave - 1;
        } {
            note.octave;
        })
    }

    /*
    Returns the note's
    'scale index' for the scale of this key. the note must
    be in the scale, otherwise behavior is undefined.
    The scale index is defined to be 0 at octave 0 of the tonic (IO), then
    1, 2, 3, and so on at II0 III0 IV0. -1, -2, and so on
    corresponds to VII-1, VI-1 and so on."
    */
    scaleIndex { |note|
        var octave = this.scaleOctave(note);
        var scale = this.scale(octave);
        var scaleIdx = scale.indexOf(note);
        ^(scaleIdx + (octave*7));
    }

    /*
    returns true if the key's scale includes the given note,
    i.e. if the note appears unaltered in this key.
    This can be called before calling other methods in
    this class which assume the note is in the key.
    */
    containsNote { |note|
        ^(this.degrees.includes(note.tpc))
    }

    /*
    Returns the !!!0-based!!! degree of the scale of this note, assuming the note exists in the scale.
    i.e. \c3 returns 0 in c major, \d3 returns 1 in c major.
    Remember it's 0-based, not 1 based! So 0 is degree I / tonic!
    In terms of code, its easier to work with 0 based. In terms of human, it can be confusing!
    Apologies!
    */
    noteDegree { |note|
        var octave = this.scaleOctave(note);
        var scale = this.scale(octave);
        ^scale.indexOf(note); 
    }

    /*
    Given a note with alterations beyond that present in the key (i.e. accidentals)
    return the unaltered version of the note.
    For example in the key of c major, passing \fs would return \f.
    */
    unalteredNote { |note|
        ^(this.degrees.detect({ |degree|
            degree.letterStepsBetween(note.tpc) == 0
        }).asNote(note.octave));
    }

    /*
    Given a scale index (as defined in the comments on scaleIndex), returns the note corresponding
    to that scaleIndex.
    */
    noteAtScaleIndex { |scaleIndex|
        var octave = scaleIndex.div(7);
        var degree = scaleIndex % 7;
        ^(this.scale(octave)[degree]);
    }

    /*
    returns a list containing the triad pitches of the key that are within
    an octave of the note
    */
    triadPitchesNear { |note|
        // construct an array containing the max possible range of notes in the scale near
        // the given note then filter them out. TODO: probably not the most efficient way to implement this method.

        // indices in result which are I, III, and V degress
        var scaleIndex = this.scaleIndex(note);
        var result = List[];

        // iterate in an octave range (inclusive) around the candidate
        for (scaleIndex - 7, scaleIndex + 7) { |candidateScaleIndex|
            var candidateNote = this.noteAtScaleIndex(candidateScaleIndex);
            var candidateDegree = this.noteDegree(candidateNote);
            
            if ((candidateDegree == 0) || (candidateDegree == 2) || (candidateDegree == 4)) {
                result.add(candidateNote);
            };
        };
        ^result;
    }

    /*
    Just like LineNote.validTriadInserts, but only returns a single list with valid inserts between
    the two given notes.
    lineType should be the type of line this would be for - see TTLine.type.
    */
    validTriadInsertsBetween{ |firstNote, secondNote, lineType|
        var fakeLine = TTLine(List[LineNote(firstNote,1),LineNote(secondNote,1)], this, lineType);
        ^(fakeLine.validTriadInsertsBefore(1));    
    }

    /*
    If this is a minor key, return the TPC that should be the tonic of its relative
    major.
    For example, given \a, returns \c.
    */
    relativeMajorTonicTPC{
        if (isMajor) {
            Error("this is a major key, can only compute relative major of a minor key").throw;
        } {
            ^(tonicTPC.asNote(4).intervalAbove(\m3).tpc);
        }
    }

    /*
    Given a valid starting note for a secondary line (I, III, or V degree, unaltered), returns an array containing notes that would
    be valid to be an ending note for a secondary line, according to the rule:
    may be any triad pitch no more than an octave from the first.
    */
    validSecondaryEndingNote { |startingNote|
        var startingDegree = this.noteDegree(startingNote);
        var startingScaleIdx = this.scaleIndex(startingNote);

        ^(switch (startingDegree,
            0, { [0, 2, 4, 7, -3, -5, -7] },
            2, { [0, 2, 5, 7, -2, -5, -7] }, 
            4, { [0, 3, 5, 7, -2, -4, -7] }, {
                Error("degree of note must be I, III, or V, was " + (startingDegree+1)).throw;
            }).collect({|scaleIdxOffset|
                this.noteAtScaleIndex(startingScaleIdx + scaleIdxOffset)
            }));
    }
}

