TestDiatonicCollection : UnitTest {
    test_ofRoot {
        this.assert(DiatonicCollection.ofRoot(\c) == [\c, \d, \e, \f, \g, \a, \b]);
        this.assert(DiatonicCollection.ofRoot(\f) == [\f, \g, \a, \bb, \c, \d, \e]);
        this.assert(DiatonicCollection.ofRoot(\fs) == [\fs, \gs, \as, \b, \cs, \ds, \es]);
        this.assert(DiatonicCollection.ofRoot(\gb) == [\gb, \ab, \bb, \cb, \db, \eb, \f]);
    }
}
