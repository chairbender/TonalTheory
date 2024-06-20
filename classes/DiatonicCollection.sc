/*Operations on diatonic collections, which are simply collections
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
    // array of the TPCs composing this diatonic collection, in
    // order from starting to ending TPC
    var <tonalPitchClasses;

    // TODO: Depends on key signature
}
