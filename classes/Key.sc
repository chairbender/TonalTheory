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

    /*
    Returns the degrees of the Key as an array of TPCs
    */
    degrees {
        var relativeMajorRoot;
        if (isMajor) {
            ^DiatonicCollection.ofRoot(tonicTPC);
        };
        // construct the minor from the relative major
        relativeMajorRoot = tonicTPC.asNote(4).intervalAbove(\m3).tpc;
        ^(DiatonicCollection.ofRoot(relativeMajorRoot).rotate(2));
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
        ^(if (degreeIndex < cIndex) {
            note.octave;
        } {
            note.octave - 1;
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
    returns an array containing the triad pitches of the key that are within
    an octave of the note
    */
    triadPitchesNear { |note|
        // construct an array containing the max possible range of notes in the scale near
        // the given note then filter them out. TODO: probably not the most efficient way to implement this method.

        // indices in result which are I, III, and V degress
        var triadIndices = Set[0, 2, 4, 7, 9, 11, 14, 16, 18];
        var result = this.scale(note.octave-1);
        result = result.addAll(this.scale(note.octave));
        result = result.addAll(this.scale(note.octave+1));
        ^(result.select({|scaleNote, i|
            triadIndices.includes(i) && scaleNote.octavesTo(note).abs == 0
        }));    
    }
}

