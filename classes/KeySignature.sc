/*
Handles stuff involving key signatures -more focusing on the accidentals in the signature than the notes in the signatures.
*/
KeySignature {
    // tonal pitch classes composing the respective circles
    classvar <circleOfFlats;
    classvar <circleOfSharps;

    *initClass {
        circleOfSharps = #[f, c, g, d, a, e, b];
        circleOfFlats = #[b, e, a, d, g, c, f];
    }

    /* Returns the natural (i.e. no alterations) tonal pitch class of the sharp that would appear as the
    nth sharp in a key signature with n sharps. */
    *nthSharp { |n|
        var idx;
        if (n < 1, {Error("n must be 1 or higher").throw});
        idx = n - 1;
        ^circleOfSharps[idx%7];
    }
}
