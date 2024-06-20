TestKeySignature : UnitTest {

    test_nthSharp {
        this.assert(KeySignature.nthSharp(1) == \f);
        this.assert(KeySignature.nthSharp(7) == \b);
        this.assert(KeySignature.nthSharp(8) == \f);
    }
}
