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

    *initClass {
        naturals = (a: 0, b: 1, c: 2, d: 3, e: 4, f: 5); 
        naturalsInverted = naturals.collect
    }

    // return the TPC without accidentals
    *natural { |tpc|
        ^tpc.asString[0].asSymbol;
    }

    // append num accidentals to tpc
    *withAccidentals { |tpc, num, accidental|
        var str = tpc.asString ++ Array.fill(num, {accidental}).join;
        ^str.asSymbol;
    }

    *numFlats { |tpc|
        ^tpc.asString.count({ |c| c == $b });
    }

    *numSharps { |tpc|
        ^tpc.asString.count({ |c| (c == $#) || (c == $s) });
    }

    /* Returns the 'natural' pitch class that is one higher
    than the given pitch class. 'Natural' here means without an accidental.
    So, if \Bb is given, the next natural will be \C. If \Bs is given, the next natural will
    be \C
    */
    *nextNaturalTPC { |tpc|
        var natural = natural(tpc);
        var idx = naturals[natural];
        ^naturalsInverted[(idx+1)];
    }

}

+ Symbol {
    natural {^TonalPitchClassSymbol.natural(this)}
    withAccidentals { |num, accidental| ^TonalPitchClassSymbol.withAccidentals(this, num, accidental)}
    numFlats {^TonalPitchClassSymbol.numFlats(this)}
    numSharps {^TonalPitchClassSymbol.numSharps(this)}
    nextNaturalTPC {^TonalPitchClassSymbol.nextNaturalTPC(this)}
}
