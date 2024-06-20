TestDiatonicCollection : UnitTest {
    test_ofRoot {
        this.assert(DiatonicCollection.ofRoot(\c) == [\c, \d, \e, \f, \g, \a, \b]);
        this.assert(1 == 1);
    }
}
