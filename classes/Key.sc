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
}

