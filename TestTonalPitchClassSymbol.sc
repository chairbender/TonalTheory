TestTonalPitchClassSymbol : UnitTest {
    test_initPitchClassSymbols {
        this.assert(TonalPitchClassSymbol.naturals[\a] == 0);
        this.assert(TonalPitchClassSymbol.naturals[\f] == 5);
    }

    test_natural_withAccidentals {
        this.assert(\as.natural == \a);
        this.assert('a#'.natural == \a);
    }

    test_naturalWithAccidentals {
        this.assert('a'.withAccidentals(2, "#") == 'a##');
        this.assert('a#'.withAccidentals(2, "#") == 'a###');
    }

    test_numFlats {
        this.assert(\ab.numFlats == 1);
        this.assert(\a.numFlats == 0);
        this.assert(\abb.numFlats == 2);
    }

    test_numSharps {
        this.assert(\as.numSharps == 1);
        this.assert(\a.numSharps == 0);
        this.assert(\ass.numSharps == 2);
        this.assert('a##'.numSharps == 2);
    }

    test_nextNatural {
        this.assert(\a.nextNatural == \b);
        this.assert(\ass.nextNatural == \b);
        this.assert(\g.nextNatural == \a);
    }
    
    test_previousNatural {
        this.assert(\a.previousNatural == \g);
        this.assert(\ass.previousNatural == \g);
        this.assert(\g.previousNatural == \f);
    }

    test_semitonesTo {
        this.assert(\a.semitonesTo(\a) == 0);
        this.assert(\a.semitonesTo(\as) == 1);
        this.assert(\as.semitonesTo(\a) == -1);
        this.assert(\a.semitonesTo(\bb) == 1);
        this.assert(\bb.semitonesTo(\a) == -1);

        this.assert(\a.semitonesTo(\b) == 2);
        this.assert(\b.semitonesTo(\a) == -2);
        this.assert(\a.semitonesTo(\g) == 10);
        this.assert(\g.semitonesTo(\a) == -10);
    }
}
