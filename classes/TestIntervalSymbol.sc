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

    test_compoundIntervalBetween {
        this.assert(\a4.compoundIntervalTo(\b4) == \M2);
        this.assert(\a4.compoundIntervalTo(\c4) == \m3);
        this.assert(\a3.compoundIntervalTo(\a3) == \P1);
        this.assert(\a3.compoundIntervalTo(\a4) == \P8);
        this.assert(\a3.compoundIntervalTo(\cs4) == \M10);
        this.assert(\es4.compoundIntervalTo(\Fb4) == \dd2);
        this.assert(\g3.compoundIntervalTo(\a4) == \M2);
        this.assert(\a4.compoundIntervalTo(\g3) == \M2);
        this.assert(\cs4.compoundIntervalTo(\c5) == \d8);
        this.assert(\c4.compoundIntervalTo(\cs5) == \a8);
    }
}   
