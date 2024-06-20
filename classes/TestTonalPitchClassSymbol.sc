TestTonalPitchClassSymbol : UnitTest {
    test_initPitchClassSymbols {
        this.assert(TonalPitchClassSymbol.naturals[\a] == 0);
        this.assert(TonalPitchClassSymbol.naturals[\f] == 5);
    }

    test_natural_withAlterations {
        this.assert(\as.natural == \a);
        this.assert('a#'.natural == \a);
    }

    test_naturalWithAlterations {
        this.assert('a'.withAlterations(2, "#") == 'a##');
        this.assert('a#'.withAlterations(2, "#") == 'as##');
    }

    test_withAlterationSemis {
        this.assert('a'.withAlterationSemis(2) == 'ass');
        this.assert('a'.withAlterationSemis(-2) == 'abb');
    }

    test_withKeyAlterations {
        this.assert('c'.withKeyAlterations(KeySignature.alterationSemisDict(\c)) == \c);
        this.assert('c'.withKeyAlterations(KeySignature.alterationSemisDict(\fs)) == \cs);
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
        this.assert(\a.nextNatural(1) == \b);
        this.assert(\ass.nextNatural(1) == \b);
        this.assert(\g.nextNatural(1) == \a);
        this.assert(\g.nextNatural(2) == \b);
        this.assert(\g.nextNatural(-2) == \e);
        this.assert(\a.nextNatural(-2) == \f);
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

    test_alterations {
        this.assert(\a.alterations == "");
        this.assert(\b.alterations == "");
        this.assert(\as.alterations == "s");
        this.assert(\ass.alterations == "ss");
        this.assert(\abb.alterations == "bb");
    }

    test_tpcEquals {
        this.assert(\a.tpcEquals(\a));
        this.assert(\a.tpcEquals(\A));
        this.assert(\ass.tpcEquals('A##'));
        this.assert(\abb.tpcEquals(\Abb));
    }
}
