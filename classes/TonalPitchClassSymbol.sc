/* Tonal Pitch classes are represented by tonal pitch class keywords of the form \<naturalnote><alterations>,
where naturalnote is a-g or A-G and alterations is either any number of flats (b) or any number of sharps (# or the character 's', as # is not allowed when using slashes for symbols)
We use the word tonal to describe these pitch classes to indicate that enharmonic equivalence is not assumed,
unlike with standard pitch classes."
*/
TonalPitchClassSymbol {
    // map from TPC symbol to integer value (a-g 0-6)
    classvar <naturals;
    // inverse of naturals (int value to natural symbol)
    classvar <naturalsInverted;
    // number of semitones from a for each natural TPC
    classvar <naturalSemitones;

    *initClass {
        naturals = (a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6); 
        naturalsInverted = naturals.invert;
        naturalSemitones = (a: 0, b: 2, c: 3, d: 5, e: 7, f: 8, g: 10);
    }

    *normalize { |tpc|
        ^tpc.asString.toLower.replace("#","s").asSymbol;
    }

    // return the TPC without alterations
    *natural { |tpc|
        ^this.normalize(tpc).asString[0].asSymbol;
    }

    // append num alterations to tpc, where alteration is a string "#" or "b"
    // does not work if adding flats when tpc already has sharps, and vice versa.
    // for such alterations, use alterTPC instead
    *appendAlterations { |tpc, num, alteration|
        var str;
        if ((alteration == "#" || alteration == "s") && (tpc.flats > 0)) {
            Error("cannot add sharps when tpc already has flats").throw
        };
        if (alteration == "b" && (tpc.sharps > 0)) {
            Error("cannot add flats when tpc already has sharps").throw
        };
        str = this.normalize(tpc).asString ++ Array.fill(num, {alteration}).join;
        ^str.asSymbol;
    }

    /* given a dict mapping from a natural TPC to the
    number of semitone alterations made by the key signature 
    (i.e. the output of KeySignature.alterationSemisDict),
    alter the given natural TPC by the given alterations in the dict */
    *withKeyAlterations { |naturalTPC, alterationsDict|
        ^naturalTPC.alterTPC(alterationsDict[naturalTPC]);
    }

    /* Raise or lower (if semis is negative) the semitones of the tpc by adding or removing
    alterations */
    *alterTPC{ |tpc, semis|
        var normalTPC = this.normalize(tpc); 
        var curFlats = normalTPC.flats;
        var curSharps = normalTPC.sharps;
        var finalFlats = 0, finalSharps = 0;
        if (semis > 0) {
            finalFlats = curFlats - semis;
            finalSharps = curSharps + if (finalFlats < 0) {finalFlats * -1} {0};
            ^if (finalSharps > 0) {
                normalTPC.natural.appendAlterations(finalSharps, "s")
            } {
                normalTPC.natural.appendAlterations(finalFlats, "b")
            };
        } {
            finalSharps = curSharps + semis;
            finalFlats = curFlats + if (finalSharps < 0) {finalSharps * -1} {0};
            ^if (finalFlats > 0) {
                normalTPC.natural.appendAlterations(finalFlats, "b")
            } {
                normalTPC.natural.appendAlterations(finalSharps, "s")
            };
        };
    }

    *flats { |tpc|
        ^this.normalize(tpc).asString.count({ |c, i| (i != 0) && (c == $b) });
    }

    *sharps { |tpc|
        ^this.normalize(tpc).asString.count({ |c| (c == $#) || (c == $s) });
    }

    *naturalIdx { |tpc|
        var natural, idx;
        natural = natural(this.normalize(tpc));
        ^naturals[natural];
    }

    /* Returns the 'natural' pitch class that is steps higher
    than the given pitch class. 'Natural' here means without an alteration.
    So, if \Bb is given, the 1st next natural will be \C, the 2n next will be D, etc...
    If \Bs is given, the next natural will be \C.
    Steps may be negative, to move steps DOWN from the given TPC.
    */
    *nextNatural { |tpc, steps|
        ^naturalsInverted[(this.naturalIdx(tpc)+steps)%7];
    }

    *semisFromA { |tpc|
        ^this.naturalSemitones[this.normalize(this.natural(tpc))] + this.alterationSemis(tpc);
    }

    /* Returns an integer indicating the offset in semitones from tpc to otherTPC, if
    they were notes in the same octave. This accounts for alterations as well.
    The sign of the result will be negative if otherTPC is below tpc, otherwise positive*/
    *semisTo{ |tpc, otherTPC|
        ^this.semisFromA(otherTPC) - this.semisFromA(tpc);
    }

    /* Returns the difference in note letter (ignoring accidentals) from
    otherTPC. For example A.letterStepsTo(C) would be 2 */
    *letterStepsTo{ |tpc, otherTPC|
        ^naturals[otherTPC.natural] - naturals[tpc.natural];
    }

    /* Convert TPC to a note symbol by providing an octave */
    *asNote{ |tpc, octave|
        ^NoteSymbol.normalize((this.normalize(tpc).asString ++ octave.asString).asSymbol);
    }

    /* Returns the alterations of the TPC as a string. Empty string if none. 
    TODO: rethink how alterations are represented based on usage of this. I don't
    like this just being a string. Alterations should probably be symbols, maybe an array?*/
    *alterations{ |tpc|
        ^(if (tpc.asString.size > 1, {tpc.asString[1..]},{""}));
    }

    /* returns the semitone shift caused by the alterations on the tpc,
    - meaning flats, + meaning sharps */
    *alterationSemis{ |tpc|
        ^this.sharps(tpc) - this.flats(tpc);
    }

}

+ Symbol {
    natural {^TonalPitchClassSymbol.natural(this)}
    appendAlterations { |num, alteration| ^TonalPitchClassSymbol.appendAlterations(this, num, alteration)}
    alterTPC { |semis| ^TonalPitchClassSymbol.alterTPC(this, semis)}
    withKeyAlterations { |alterationsDict| ^TonalPitchClassSymbol.withKeyAlterations(this, alterationsDict)}
    flats {^TonalPitchClassSymbol.flats(this)}
    sharps {^TonalPitchClassSymbol.sharps(this)}
    nextNatural {|steps| ^TonalPitchClassSymbol.nextNatural(this, steps)}
    previousNatural {^TonalPitchClassSymbol.previousNatural(this)}
    semisTo {|otherTPC| ^TonalPitchClassSymbol.semisTo(this,otherTPC)}
    letterStepsTo {|otherTPC| ^TonalPitchClassSymbol.letterStepsTo(this,otherTPC)}
    alterations {^TonalPitchClassSymbol.alterations(this)}
    tpcEquals {|otherTPC| ^TonalPitchClassSymbol.normalize(this) == TonalPitchClassSymbol.normalize(otherTPC)}
    asNote {|octave| ^TonalPitchClassSymbol.asNote(this, octave)}
    alterationSemis { ^TonalPitchClassSymbol.alterationSemis(this)}
}
