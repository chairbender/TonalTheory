TestIntervalSymbol : UnitTest {

    test_lineOfFifthsQuality {
        this.assert(IntervalSymbol.lineOfFifthsQuality(0) == "P");
        this.assert(IntervalSymbol.lineOfFifthsQuality(1) == "P");
        this.assert(IntervalSymbol.lineOfFifthsQuality(-1) == "P");
        this.assert(IntervalSymbol.lineOfFifthsQuality(-2) == "m");
        this.assert(IntervalSymbol.lineOfFifthsQuality(2) == "M");
        this.assert(IntervalSymbol.lineOfFifthsQuality(6) == "a");
        this.assert(IntervalSymbol.lineOfFifthsQuality(-6) == "d");
        this.assert(IntervalSymbol.lineOfFifthsQuality(12) == "a");
        this.assert(IntervalSymbol.lineOfFifthsQuality(-12) == "d");
        this.assert(IntervalSymbol.lineOfFifthsQuality(-13) == "dd");
        this.assert(IntervalSymbol.lineOfFifthsQuality(13) == "aa");
        this.assert(IntervalSymbol.lineOfFifthsQuality(21) == "aaa");
        this.assert(IntervalSymbol.lineOfFifthsQuality(-21) == "ddd");
    }

    test_lineOfFifthsNumber {
        this.assert(IntervalSymbol.lineOfFifthsNumber(0) == 1);
        this.assert(IntervalSymbol.lineOfFifthsNumber(-1) == 4);
        this.assert(IntervalSymbol.lineOfFifthsNumber(-6) == 5);
        this.assert(IntervalSymbol.lineOfFifthsNumber(-7) == 1);
        this.assert(IntervalSymbol.lineOfFifthsNumber(-8) == 4);

        this.assert(IntervalSymbol.lineOfFifthsNumber(1) == 5);
        this.assert(IntervalSymbol.lineOfFifthsNumber(6) == 4);
        this.assert(IntervalSymbol.lineOfFifthsNumber(7) == 1);
        this.assert(IntervalSymbol.lineOfFifthsNumber(8) == 5);
    }

    test_lineOfFifthsLetter {
        this.assert(IntervalSymbol.lineOfFifthsLetter(\f, 0) == \f);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\f, -1) == \b);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\f, 6) == \b);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\f, 7) == \f);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\c, 0) == \c);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\c, -1) == \f);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\c, 6) == \f);
        this.assert(IntervalSymbol.lineOfFifthsLetter(\c, 7) == \c);
    }

    test_lineOfFifthsStepsTo {
        this.assert(IntervalSymbol.lineOfFifthsStepsTo(\c, \c) == 0);
        this.assert(IntervalSymbol.lineOfFifthsStepsTo(\c, \g) == 1);
        this.assert(IntervalSymbol.lineOfFifthsStepsTo(\c, \f) == -1);
    }

    test_lineOfFifthsTPCIndex {
        this.assert(IntervalSymbol.lineOfFifthsTPCIndex(\c, \c) == 0);
        this.assert(IntervalSymbol.lineOfFifthsTPCIndex(\c, \cs) == 7);

        this.assert(IntervalSymbol.lineOfFifthsTPCIndex(\f, \b) == 6);
        this.assert(IntervalSymbol.lineOfFifthsTPCIndex(\f, \bb) == -1);
        this.assert(IntervalSymbol.lineOfFifthsTPCIndex(\f, \cb) == -6);
    }

    test_lineOfFifthsAlterationSemis {
        this.assert(IntervalSymbol.lineOfFifthsAlterationSemis(\f, 3) == 0);
        this.assert(IntervalSymbol.lineOfFifthsAlterationSemis(\f, 4) == 1);
        this.assert(IntervalSymbol.lineOfFifthsAlterationSemis(\f, -4) == -1);
    }

    test_lineOfFifthsTPC {
        this.assert(IntervalSymbol.lineOfFifthsTPC(\f, 3) == \d);
        this.assert(IntervalSymbol.lineOfFifthsTPC(\f, 4) == \as);
        this.assert(IntervalSymbol.lineOfFifthsTPC(\f, -3) == \a);
        this.assert(IntervalSymbol.lineOfFifthsTPC(\f, -4) == \db);

        this.assert(IntervalSymbol.lineOfFifthsTPC(\fs, 3) == \ds);
        this.assert(IntervalSymbol.lineOfFifthsTPC(\fs, 4) == \ass);
        this.assert(IntervalSymbol.lineOfFifthsTPC(\fs, -3) == \as);
        this.assert(IntervalSymbol.lineOfFifthsTPC(\fs, -4) == \d);
    }

    test_simpleInterval {
        this.assert(IntervalSymbol.simpleInterval(\m3) == \m3);
        this.assert(IntervalSymbol.simpleInterval(\d8) == \d8);
        this.assert(IntervalSymbol.simpleInterval(\M10) == \M3);
    }

    test_invertQuality {
        this.assert(IntervalSymbol.invertQuality("d") == "a");
        this.assert(IntervalSymbol.invertQuality("M") == "m");
    }

    test_invertInterval {
        this.assert(IntervalSymbol.invertInterval(\m3) == \M6);
        this.assert(IntervalSymbol.invertInterval(\m6) == \M3);
        this.assert(IntervalSymbol.invertInterval(\M10) == \m6);
    }

    test_compoundIntervalBetween {
        this.assert(\a4.compoundIntervalTo(\b4) == \M2);
        this.assert(\a4.compoundIntervalTo(\c4) == \M6);
        this.assert(\a3.compoundIntervalTo(\a3) == \P1);
        this.assert(\a3.compoundIntervalTo(\a4) == \P8);
        this.assert(\es4.compoundIntervalTo(\Fb4) == \dd2);
        this.assert(\cs4.compoundIntervalTo(\c5) == \d8);
        this.assert(\c4.compoundIntervalTo(\cs5) == \a8);
        this.assert(\a3.compoundIntervalTo(\cs4) == \M3);
        this.assert(\g3.compoundIntervalTo(\a4) == \M9);
        this.assert(\a4.compoundIntervalTo(\g3) == \M9);
    }

    test_simpleIntervalBetween {
        this.assert(\g3.simpleIntervalTo(\a4) == \M2);
    }

    test_noteAbove {
        this.assert(\M2.noteAbove(\a4) == \b4);
        this.assert(\m3.noteAbove(\a4) == \c5);
        this.assert(\P8.noteAbove(\a3) == \a4);
        this.assert(\P1.noteAbove(\a3) == \a3);
        this.assert(\M10.noteAbove(\a3) == \cs5);
        this.assert(\dd2.noteAbove(\es4) == \fbb4);
        this.assert(\M2.noteAbove(\g3) == \a3);
        this.assert(\d8.noteAbove(\cs4) == \c5);
        this.assert(\a8.noteAbove(\c4) == \cs5);
    }
}
