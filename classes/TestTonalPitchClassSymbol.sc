TestTonalPitchClassSymbol : UnitTest {
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
 
    test_nextNaturalTPC {
        this.assert(\b.nextNaturalTPC(1) == \c);
        this.assert(\c.nextNaturalTPC(-1) == \b);
        this.assert(\b.nextNaturalTPC(8) == \c);
        this.assert(\c.nextNaturalTPC(-8) == \b);
    }   

    test_SemisTo {
        this.assert(\c.semisTo(\c) == 0);
        this.assert(\c.semisTo(\cs) == 1);
        this.assert(\cs.semisTo(\c) == -1);
        this.assert(\c.semisTo(\db) == 1);
        this.assert(\db.semisTo(\c) == -1);

        this.assert(\c.semisTo(\d) == 2);
        this.assert(\d.semisTo(\c) == -2);
        this.assert(\c.semisTo(\b) == 11);
        this.assert(\b.semisTo(\c) == -11);
    }

    test_letterStepsBetween {
        this.assert(\c.letterStepsBetween(\b) == 6);
        this.assert(\b.letterStepsBetween(\c) == -6);
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
