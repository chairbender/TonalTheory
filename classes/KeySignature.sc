/*
Handles stuff involving key signatures -more focusing on the accidentals in the signature than the notes in the signatures.
*/
KeySignature {
    // tonal pitch classes composing the respective circles
    classvar <flatKeys;
    classvar <sharpKeys;
    classvar <circleOfFifths;

    *initClass {
        sharpKeys = #[f, c, g, d, a, e, b];
        flatKeys = #[b, e, a, d, g, c, f];
        circleOfFifths = #[c, g, d, a, e, b, f];
    }

    *nthAccidental { |n, circle|
        var idx;
        if (n < 1, {Error("n must be 1 or higher").throw});
        idx = n - 1;
        ^circle[idx%7];
    }

    /* Returns the natural (i.e. no alterations) tonal pitch class of the sharp that would appear as the
    nth sharp in a key signature with n sharps. */
    *nthSharp { |n|
        ^this.nthAccidental(n, sharpKeys);
    }

    /* flat version of nthSharp */
    *nthFlat { |n|
        ^this.nthAccidental(n, flatKeys);
    }

    /* walk n steps around the circle of fifths, starting at c,
    and return the TPC at the final step */
    *circleSteps { |n|
        ^circleOfFifths[n%7];
    }


    /* For the diatonic collection starting on diatonicRootTPC
    (this is the same as a major scale in the key of diatonicRootTPC),
    returns a map from natural TPC to the alteration semitones this key would apply to the given naturtpc.
    Positive values indicate sharps, negative indicates flats. For example, the diatonic 
    collection starting on G sharp has a double sharp for F, so the dict would have
    an entry of \F->2
    */
    *alterationSemisDict { |diatonicRootTPC| 
        var alterations = (a: 0, b: 0, c: 0, d: 0, e: 0, f: 0, g: 0);
        // flat or sharp key
        var circleDirection = if ((diatonicRootTPC.tpcEquals(\f)) || (diatonicRootTPC.flats > 0), {-1}, {1});
        var alteration = if (circleDirection > 0, {"#"}, {"b"});
        // rotate on the circle until we reach the diatonic root
        // accumulating alterations as we go
        var steps = 0; 
        var circleTPC = \c, keyTPC = \c, alteredTPC = \c;
        while { alteredTPC.tpcEquals(diatonicRootTPC).not} {
            if (steps > 100) {Error("infinite loop failsafe").throw}; 
            // take a step
            steps = steps + circleDirection;
            circleTPC = this.circleSteps(steps); 
            // accumulate the alteration of the current sharp/flat of the signature
            keyTPC = if (circleDirection > 0, {this.nthSharp(steps)}, {this.nthFlat(steps * -1)});   
            alterations[keyTPC] = alterations[keyTPC] + circleDirection;
            alteredTPC = circleTPC.alterTPC(alterations[circleTPC]); 
        };
        ^alterations;
    }

    /*
    Just like alterationSemisDict, but you can pass a key instead of
    a root TPC. If the key is minor, it will correctly use the relative major key
    signature.
    */
    *alterationSemisDictOfKey{ |key|
        if (key.isMinor) {
            ^(this.alterationSemisDict(key.relativeMajorTonicTPC))
        } {
            ^(this.alterationSemisDict(key.tonicTPC))
        };
    }
}
