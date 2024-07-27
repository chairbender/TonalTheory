TestNoteSymbol : UnitTest {
    test_tpc {
        this.assert(\abbb4.tpc == \abbb);
        this.assert(\a4.tpc == \a);
        this.assert(\abn1.tpc == \ab);
        this.assert(\an1.tpc == \a);
    }

    test_octave {
        this.assert(\a4.octave == 4);
        this.assert(\an1.octave == -1);
        this.assert(\abbn1.octave == -1);
    }

    test_semis {
        this.assert(\cn1.semis == 0);
        this.assert(\cbn1.semis == -1);
        this.assert(\csn1.semis == 1);
        this.assert(\bn1.semis == 11);
        this.assert(\an1.semis == 9);
        this.assert(\c0.semis == 12);
        this.assert(\css1.semis == 26);
    }

    test_alterNote {
        this.assert(\a4.alterNote(2) == \ass4);
        this.assert(\abb4.alterNote(2) == \a4);
        this.assert(\ass4.alterNote(2) == \assss4);
        this.assert(\a4.alterNote(-2) == \abb4);
        this.assert(\ass4.alterNote(-2) == \a4);
        this.assert(\abb4.alterNote(-2) == \abbbb4);
        this.assert(\ass4.alterNote(-4) == \abb4);
        this.assert(\abb4.alterNote(4) == \ass4);
    }


    test_octavesTo {
        this.assert(\c4.octavesTo(\c5) == 1);
        this.assert(\b3.octavesTo(\c4) == 0);
    }

    test_isAbove {
        this.assert(\b4.isAbove(\c4));
        this.assert(\a4.isAbove(\c4));
        this.assert(\b4.isAbove(\a4));

        this.assert(\a3.isAbove(\c4).not);
        this.assert(\a3.isAbove(\cs4).not);
    }

    test_nextNaturalNote {
        this.assert(\c4.nextNaturalNote(1) == \d4);
        this.assert(\c4.nextNaturalNote(-1) == \b3);
        this.assert(\d4.nextNaturalNote(-7) == \d3);
        this.assert(\d4.nextNaturalNote(-8) == \c3);
        this.assert(\d4.nextNaturalNote(-9) == \b2);
        this.assert(\b4.nextNaturalNote(7) == \b5);
        this.assert(\b4.nextNaturalNote(8) == \c6);
    }
}
