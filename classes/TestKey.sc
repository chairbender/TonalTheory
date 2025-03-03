TestKey : UnitTest {

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

}
