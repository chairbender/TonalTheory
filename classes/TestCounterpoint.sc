TestCounterpoint : UnitTest {

    test_fullDiatonicStepMotion_cUp {
        var line = Counterpoint.fullDiatonicStepMotion(\c, \c4, \b4);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1),
            LineNote(\f4, 1),
            LineNote(\g4, 1),
            LineNote(\a4, 1),
            LineNote(\b4, 1),
        ]);
        this.assert(line == expected);
    }

    test_fullDiatonicStepMotion_cDown {
        var line = Counterpoint.fullDiatonicStepMotion(\c, \c4, \d3);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\b3, 1),
            LineNote(\a3, 1),
            LineNote(\g3, 1),
            LineNote(\f3, 1),
            LineNote(\e3, 1),
            LineNote(\d3, 1),
        ]);
        this.assert(line == expected);
    }

    test_diatonicStepMotion_cUp {
        var line = Counterpoint.diatonicStepMotion(\c, \c4, \b4);
        var expected = TTLine(List[
            LineNote(\d4, 1),
            LineNote(\e4, 1),
            LineNote(\f4, 1),
            LineNote(\g4, 1),
            LineNote(\a4, 1)
        ]);
        this.assert(line == expected);
    }

    test_diatonicStepMotion_cDown {
        var line = Counterpoint.diatonicStepMotion(\c, \c4, \d3);
        var expected = TTLine(List[
            LineNote(\b3, 1),
            LineNote(\a3, 1),
            LineNote(\g3, 1),
            LineNote(\f3, 1),
            LineNote(\e3, 1)
        ]);
        this.assert(line == expected);
    }

    test_basicStepMotion_3 {
        var line = Counterpoint.basicStepMotion(Key(\c, true), 4, 3);
        var expected = TTLine(List[
            LineNote(\e4, 1),
            LineNote(\d4, 1),
            LineNote(\c4, 1),
        ]);
        this.assert(line == expected);
    }

    test_basicStepMotion_8 {
        var line = Counterpoint.basicStepMotion(Key(\c, true), 4, 8);
        var expected = TTLine(List[
            LineNote(\c5, 1),
            LineNote(\b4, 1),
            LineNote(\a4, 1),
            LineNote(\g4, 1),
            LineNote(\f4, 1),
            LineNote(\e4, 1),
            LineNote(\d4, 1),
            LineNote(\c4, 1),
        ]);
        this.assert(line == expected);
    }

    test_basicArpeggiation_up {
        var line = Counterpoint.basicArpeggiation(Key(\c, true), 4, 0, true);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\c4, 1)
        ]);
        this.assert(line == expected);
    }

    test_basicArpeggiation_down {
        var line = Counterpoint.basicArpeggiation(Key(\c, true), 4, 0, false);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g3, 1),
            LineNote(\c4, 1)
        ]);
        this.assert(line == expected);
    }

    test_basicArpeggiation_octaveUp {
        var line = Counterpoint.basicArpeggiation(Key(\c, true), 4, 1, false);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\c5, 1)
        ]);
        this.assert(line == expected);
    }
}
