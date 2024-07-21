TestTonalPitchClassSymbol : UnitTest {
    test_initPitchClassSymbols {
        this.assert(TonalPitchClassSymbol.naturals[\a] == 0);
        this.assert(TonalPitchClassSymbol.naturals[\f] == 5);
    }

    test_natural_appendAlterations {
        this.assert(\as.natural == \a);
        this.assert('a#'.natural == \a);
    }

    test_naturalWithAlterations {
        this.assert('a'.appendAlterations(2, "#") == 'a##');
        this.assert('a#'.appendAlterations(2, "#") == 'as##');
    }

    test_alterTPC {
        this.assert('a'.alterTPC(2) == 'ass');
        this.assert('a'.alterTPC(-2) == 'abb');
    }

    test_withKeyAlterations {
        this.assert('c'.withKeyAlterations(KeySignature.alterationSemisDict(\c)) == \c);
        this.assert('c'.withKeyAlterations(KeySignature.alterationSemisDict(\fs)) == \cs);
    }

    test_flats {
        this.assert(\ab.flats == 1);
        this.assert(\a.flats == 0);
        this.assert(\abb.flats == 2);
    }

    test_sharps {
        this.assert(\as.sharps == 1);
        this.assert(\a.sharps == 0);
        this.assert(\ass.sharps == 2);
        this.assert('a##'.sharps == 2);
    }

    test_nextNatural {
        this.assert(\a.nextNatural(1) == \b);
        this.assert(\ass.nextNatural(1) == \b);
        this.assert(\g.nextNatural(1) == \a);
        this.assert(\g.nextNatural(2) == \b);
        this.assert(\g.nextNatural(-2) == \e);
        this.assert(\a.nextNatural(-2) == \f);
    }
 
    test_nextOctaveNatural {
        this.assert(\b.nextOctaveNatural(1) == \c);
        this.assert(\c.nextOctaveNatural(-1) == \b);
        this.assert(\b.nextOctaveNatural(8) == \c);
        this.assert(\c.nextOctaveNatural(-8) == \b);
    }   

    test_semisTo {
        this.assert(\a.semisTo(\a) == 0);
        this.assert(\a.semisTo(\as) == 1);
        this.assert(\as.semisTo(\a) == -1);
        this.assert(\a.semisTo(\bb) == 1);
        this.assert(\bb.semisTo(\a) == -1);

        this.assert(\a.semisTo(\b) == 2);
        this.assert(\b.semisTo(\a) == -2);
        this.assert(\a.semisTo(\g) == 10);
        this.assert(\g.semisTo(\a) == -10);
    }

    test_octaveSemisTo {
        this.assert(\c.octaveSemisTo(\c) == 0);
        this.assert(\c.octaveSemisTo(\cs) == 1);
        this.assert(\cs.octaveSemisTo(\c) == -1);
        this.assert(\c.octaveSemisTo(\db) == 1);
        this.assert(\db.octaveSemisTo(\c) == -1);

        this.assert(\c.octaveSemisTo(\d) == 2);
        this.assert(\d.octaveSemisTo(\c) == -2);
        this.assert(\c.octaveSemisTo(\b) == 11);
        this.assert(\b.octaveSemisTo(\c) == -11);
    }

    test_octaveLetterStepsTo {
        this.assert(\c.octaveLetterStepsTo(\b) == 6);
        this.assert(\b.octaveLetterStepsTo(\c) == -6);
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

    test_asNote {
        this.assert(\a.asNote(1) == \a1);
        this.assert(\a.asNote(-1) == \an1);
    }
}
