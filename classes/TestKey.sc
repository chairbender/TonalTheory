TestKey : UnitTest {
    classvar <cKey;

    *initClass {
        Class.initClassTree(TonalPitchClassSymbol);
        cKey = Key(\c, true);
    }

    test_degrees {
        this.assert(Key(\c, true).degrees(1) == [\c, \d, \e, \f, \g, \a, \b]);
        this.assert(Key(\a, false).degrees(1) == [\a, \b, \c, \d, \e, \f, \g]);
    }

    test_scale {
        this.assert(Key(\c, true).scale(4) == [\c4, \d4, \e4, \f4, \g4, \a4, \b4]);
        this.assert(Key(\a, false).scale(4) == [\a4, \b4, \c5, \d5, \e5, \f5, \g5]);
    }

    test_scaleIndex {
        this.assert(Key(\a, false).scaleIndex(\a0) == 0);
        this.assert(Key(\a, false).scaleIndex(\c1) == 2);
        this.assert(Key(\a, false).scaleIndex(\a1) == 7);
        this.assert(Key(\a, false).scaleIndex(\c2) == 9);
        this.assert(Key(\a, false).scaleIndex(\a2) == 14);
        this.assert(Key(\a, false).scaleIndex(\c3) == 16);

        this.assert(Key(\c, true).scaleIndex(\c0) == 0);
        this.assert(Key(\c, true).scaleIndex(\a0) == 5);
        this.assert(Key(\c, true).scaleIndex(\c1) == 7);
        this.assert(Key(\c, true).scaleIndex(\a1) == 12);
        this.assert(Key(\c, true).scaleIndex(\c2) == 14);
        this.assert(Key(\c, true).scaleIndex(\a2) == 19);
    }

    test_triadPitchesNear {
        this.assert(Key(\c, true).triadPitchesNear(\c4) == List[\c3, \e3, \g3, \c4, \e4, \g4, \c5])
    }

    test_validTriadInsertsBetween {
        this.assert(Key(\c, true).validTriadInsertsBetween(\c4, \g4, \primary) == [\g3, \c4, \e4, \g4, \c5, \e5, \g5]);
    }

    test_unalteredNote {
        this.assert(Key(\c, true).unalteredNote(\cs4) == \c4);
        this.assert(Key(\c, true).unalteredNote(\as4) == \a4);
    }

    test_validSecondaryEndingNote {
        this.assert(cKey.validSecondaryEndingNote(\c4) == [\c4, \e4, \g4, \c5, \g3, \e3, \c3]);
        this.assert(cKey.validSecondaryEndingNote(\e4) == [\e4, \g4, \c5, \e5, \c4, \g3, \e3]);
        this.assert(cKey.validSecondaryEndingNote(\g4) == [\g4, \c5, \e5, \g5, \e4, \c4, \g3]);
    }

}
