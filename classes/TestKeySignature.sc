TestKeySignature : UnitTest {

    test_nthSharp {
        this.assert(KeySignature.nthSharp(1) == \f);
        this.assert(KeySignature.nthSharp(7) == \b);
        this.assert(KeySignature.nthSharp(8) == \f);
    }

    test_nthFlat {
        this.assert(KeySignature.nthFlat(1) == \b);
        this.assert(KeySignature.nthFlat(7) == \f);
        this.assert(KeySignature.nthFlat(8) == \b);
    }
}
