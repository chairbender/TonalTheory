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
        ^tpc.asString.toLower.asSymbol;
    }

    // return the TPC without accidentals
    *natural { |tpc|
        ^this.normalize(tpc).asString[0].asSymbol;
    }

    // append num accidentals to tpc
    *withAccidentals { |tpc, num, accidental|
        var str = this.normalize(tpc).asString ++ Array.fill(num, {accidental}).join;
        ^str.asSymbol;
    }

    *numFlats { |tpc|
        ^this.normalize(tpc).asString.count({ |c, i| (i != 0) && (c == $b) });
    }

    *numSharps { |tpc|
        ^this.normalize(tpc).asString.count({ |c| (c == $#) || (c == $s) });
    }

    *naturalIdx { |tpc|
        var natural, idx;
        natural = natural(this.normalize(tpc));
        ^naturals[natural];
    }

    /* Returns the 'natural' pitch class that is one higher
    than the given pitch class. 'Natural' here means without an accidental.
    So, if \Bb is given, the next natural will be \C. If \Bs is given, the next natural will
    be \C
    */
    *nextNatural { |tpc|
        ^naturalsInverted[(this.naturalIdx(tpc)+1)%7];
    }

    /* inverse of nextNatural */
    *previousNatural { |tpc|
        ^naturalsInverted[(this.naturalIdx(tpc)-1)%7];
    }
    
    *semisFromA { |tpc|
        ^this.naturalSemitones[this.normalize(this.natural(tpc))] + numSharps(tpc) - numFlats(tpc);
    }

    /* Returns an integer indicating the offset in semitones of otherTPC from tpc, if
    they were notes in the same octave. This accounts for accidentals as well.
    The sign of the result will be negative if otherTPC is below tpc, otherwise positive*/
    *semitonesTo{ |tpc, otherTPC|
        ^(this.semisFromA(otherTPC) - this.semisFromA(tpc));
    }
}

+ Symbol {
    natural {^TonalPitchClassSymbol.natural(this)}
    withAccidentals { |num, accidental| ^TonalPitchClassSymbol.withAccidentals(this, num, accidental)}
    numFlats {^TonalPitchClassSymbol.numFlats(this)}
    numSharps {^TonalPitchClassSymbol.numSharps(this)}
    nextNatural {^TonalPitchClassSymbol.nextNatural(this)}
    previousNatural {^TonalPitchClassSymbol.previousNatural(this)}
    semitonesTo {|otherTPC| ^TonalPitchClassSymbol.semitonesTo(this,otherTPC)}
}
