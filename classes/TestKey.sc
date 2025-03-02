TestKey : UnitTest {

    test_degrees {
        this.assert(Key(\c, true).degrees(1) == [\c, \d, \e, \f, \g, \a, \b]);
        this.assert(Key(\a, false).degrees(1) == [\a, \b, \c, \d, \e, \f, \g]);
    }

    test_scale {
        this.assert(Key(\c, true).scale(4) == [\c4, \d4, \e4, \f4, \g4, \a4, \b4]);
        this.assert(Key(\a, false).scale(4) == [\a4, \b4, \c5, \d5, \e5, \f5, \g5]);
    }
}
