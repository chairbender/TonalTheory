TestCounterpoint : UnitTest {

    test_fullDiatonicStepMotion_cUp {
        var line = Counterpoint.fullDiatonicStepMotion(\c, \c4, \b4);

        this.assert(line[0].note == \c4);
        this.assert(line[1].note == \d4);
        this.assert(line[2].note == \e4);
        this.assert(line[3].note == \f4);
        this.assert(line[4].note == \g4);
        this.assert(line[5].note == \a4);
        this.assert(line[6].note == \b4);
    }

    test_fullDiatonicStepMotion_cDown {
        var line = Counterpoint.fullDiatonicStepMotion(\c, \c4, \d3);

        this.assert(line[0].note == \c4);
        this.assert(line[1].note == \b3);
        this.assert(line[2].note == \a3);
        this.assert(line[3].note == \g3);
        this.assert(line[4].note == \f3);
        this.assert(line[5].note == \e3);
        this.assert(line[6].note == \d3);
    }

    test_diatonicStepMotion_cUp {
        var line = Counterpoint.diatonicStepMotion(\c, \c4, \b4);

        this.assert(line[0].note == \d4);
        this.assert(line[1].note == \e4);
        this.assert(line[2].note == \f4);
        this.assert(line[3].note == \g4);
        this.assert(line[4].note == \a4);
    }

    test_diatonicStepMotion_cDown {
        var line = Counterpoint.diatonicStepMotion(\c, \c4, \d3);

        this.assert(line[0].note == \b3);
        this.assert(line[1].note == \a3);
        this.assert(line[2].note == \g3);
        this.assert(line[3].note == \f3);
        this.assert(line[4].note == \e3);
    }
}
