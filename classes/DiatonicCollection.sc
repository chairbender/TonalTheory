/*Operations on diatonic collections, which are simply arrays
of seven tonal pitch classes starting on a given TPC with this pattern of intervals/steps
from the root note: whole whole half whole whole whole half (where whole and half mean
a major and a minor second interval, respectively). A diatonic collection starting on some
note has the same pitches as a major scale starting on that note.

Note that, given a midi note with two possible TPCs (like D# and Eb),
some libraries alway pick the same TPC.
But, if we are following westergaardian theory, the choice of which tonal pitch class a given
pitch should be referred to by is based on the diatonic collection described by the key signature
that the pitch is being played in.
*/
DiatonicCollection {
    /* Returns a diatonic collection as an array of the
	TPCs making up the collection,
	formed by starting on the given TPC. The returned array also starts on the given TPC.
	Where two TPCs are equivalent (like D# and Eb), the
	TPC chosen will be the one whose accidental appears in the key
	signature that describes that diatonic collection. For the special case of F,
	the flat representation will be used (since we could use sharps or flats
	to get the same diatonic collection, but there are fewer accidentals in the flat version
	which is preferrable). One way to think about it is -
	the key signature for a given starting note is equivalent to the key signature for the
	major scale starting on that note.*/
    *ofRoot { |diatonicRootTPC|
        var alterationsDict = KeySignature.alterationSemisDict(diatonicRootTPC);
        ^Array.fill(7, { |i|
            var nextNatural = diatonicRootTPC.nextNaturalTPC(i);
            nextNatural.alterTPC(alterationsDict[nextNatural]);
        });
    }
}
