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

    test_alterationSemisDict {
        this.assert(KeySignature.alterationSemisDict(\c) == (a:0,b:0,c:0,d:0,e:0,f:0,g:0));
        this.assert(KeySignature.alterationSemisDict(\G) == (a:0,b:0,c:0,d:0,e:0,f:1,g:0));
        this.assert(KeySignature.alterationSemisDict(\Fs) == (a:1,b:0,c:1,d:1,e:1,f:1,g:1));
        this.assert(KeySignature.alterationSemisDict(\F) == (a:0,b:-1,c:0,d:0,e:0,f:0,g:0));
        this.assert(KeySignature.alterationSemisDict(\Gb) == (a:-1,b:-1,c:-1,d:-1,e:-1,f:0,g:-1));
    }
}
