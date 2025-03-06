TestTTLine : UnitTest {
    classvar <cKey;
    classvar <aMinorKey;

    *initClass {
        cKey = Key(\c, true);
        aMinorKey = Key(\a, false);
    }

    test_rearticulate_singleInHalf {
        var line = TTLine(List[ 
            LineNote(\a4, 1)
        ]);
        line.rearticulate(0, 1 %/ 2);

        this.assert(line[0].note == \a4);
        this.assert(line[0].duration == (1 %/ 2));
        this.assert(line[1].note == \a4);
        this.assert(line[1].duration == (1 %/ 2));
    }

    test_rearticulate_singleUneven {
        var line = TTLine(List[ 
            LineNote(\a4, 1)
        ]);
        line.rearticulate(0, 1 %/ 4);

        this.assert(line[0].note == \a4);
        this.assert(line[0].duration == (1 %/ 4));
        this.assert(line[1].note == \a4);
        this.assert(line[1].duration == (3 %/ 4));
    }

    test_rearticulate_threeMiddle {
        var line = TTLine(List[ 
            LineNote(\a4, 1),
            LineNote(\b4, 1),
            LineNote(\c4, 1)
        ]);
        line.rearticulate(1, 1 %/ 2);

        this.assert(line[0].note == \a4);
        this.assert(line[0].duration == 1);
        this.assert(line[1].note == \b4);
        this.assert(line[1].duration == (1 %/ 2));
        this.assert(line[2].note == \b4);
        this.assert(line[2].duration == (1 %/ 2));
        this.assert(line[3].note == \c4);
        this.assert(line[3].duration == 1);
    }

    test_neighbor_minimalUp {
        var line = TTLine(List[ 
            LineNote(\a4, 1),
            LineNote(\a4, 1)
        ]);
        line.neighbor(0, 1 %/ 2, true, \M2);

        this.assert(line[0].note == \a4);
        this.assert(line[0].duration == (1 %/ 2));
        this.assert(line[1].note == \b4);
        this.assert(line[1].duration == (1 %/ 2));
        this.assert(line[2].note == \a4);
        this.assert(line[2].duration == 1);
    }

    test_neighbor_minimalDown {
        var line = TTLine(List[ 
            LineNote(\a4, 1),
            LineNote(\a4, 1)
        ]);
        line.neighbor(0, 1 %/ 2, false, \M2);

        this.assert(line[0].note == \a4);
        this.assert(line[0].duration == (1 %/ 2));
        this.assert(line[1].note == \g4);
        this.assert(line[1].duration == (1 %/ 2));
        this.assert(line[2].note == \a4);
        this.assert(line[2].duration == 1);
    }

    test_neighbor_cDown {
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\c4, 1)
        ], cKey);
        var expected = TTLine(List[
            LineNote(\c4, 1 %/ 2),
            LineNote(\b3, 1 %/ 2),
            LineNote(\c4, 1),
        ], cKey);
        line.neighbor(0, 1%/2, false, \m2);
        this.assert(line == expected);
    }

    test_arpeggiateInterval_minimalUp {
        var line = TTLine(List[ 
            LineNote(\a4, 1)
        ]);
        line.arpeggiateInterval(0, \P5, 1 %/ 2, true);

        this.assert(line[0].note == \a4);
        this.assert(line[0].duration == (1 %/ 2));
        this.assert(line[1].note == \e5);
        this.assert(line[1].duration == (1 %/ 2));
    }

    test_arpeggiateInterval_minimalDown {
        var line = TTLine(List[ 
            LineNote(\e5, 1)
        ]);
        line.arpeggiateInterval(0, \P5, 1 %/ 2, false);

        this.assert(line[0].note == \e5);
        this.assert(line[0].duration == (1 %/ 2));
        this.assert(line[1].note == \a4);
        this.assert(line[1].duration == (1 %/ 2));
    }

    test_validNeighborIndices {
        var line = TTLine(List[ 
            LineNote(\c5, 1),
            LineNote(\c5, 1),
            LineNote(\c5, 1)
        ]);

        this.assert(line.validNeighborIndices() == List[0, 1])
    }

    test_fullDiatonicStepMotion_cUp {
        var line = TTLine.fullDiatonicStepMotion(cKey, \c4, \b4);
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
        var line = TTLine.fullDiatonicStepMotion(cKey, \c4, \d3);
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
        var line = TTLine.diatonicStepMotion(cKey, \c4, \b4);
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
        var line = TTLine.diatonicStepMotion(cKey, \c4, \d3);
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
        var line = TTLine.basicStepMotion(Key(\c, true), 4, 3);
        var expected = TTLine(List[
            LineNote(\e4, 1),
            LineNote(\d4, 1),
            LineNote(\c4, 1),
        ]);
        this.assert(line == expected);
    }

    test_basicStepMotion_8 {
        var line = TTLine.basicStepMotion(Key(\c, true), 4, 8);
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
        var line = TTLine.basicArpeggiation(Key(\c, true), 4, 0, true);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\c4, 1)
        ]);
        this.assert(line == expected);
    }

    test_basicArpeggiation_down {
        var line = TTLine.basicArpeggiation(Key(\c, true), 4, 0, false);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g3, 1),
            LineNote(\c4, 1)
        ]);
        this.assert(line == expected);
    }

    test_basicArpeggiation_octaveUp {
        var line = TTLine.basicArpeggiation(Key(\c, true), 4, 1, false);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\c5, 1)
        ]);
        this.assert(line == expected);
    }

    test_validTriadInsertsBefore_repetition {
        var key = Key(\c, true);
        var line = TTLine(
            List[
                LineNote(\c4, 1),
                LineNote(\c4, 1),
            ],
            key, \primary);
        this.assert(line.validTriadInsertsBefore(0) == [\c3, \e3, \g3, \c4, \e4, \g4, \c5]);
        this.assert(line.validTriadInsertsBefore(1) == [\c3, \e3, \g3, \c4, \e4, \g4, \c5]);
    }

    test_validTriadInsertsBefore_interval {
        var key = Key(\c, true);
        var line = TTLine(
            List[
                LineNote(\c4, 1),
                LineNote(\g4, 1),
            ],
            key, \primary);
        this.assert(line.validTriadInsertsBefore(0) == [\c3, \e3, \g3, \c4, \e4, \g4, \c5]);
        this.assert(line.validTriadInsertsBefore(1) == [\g3, \c4, \e4, \g4, \c5]);
    }

    test_validTriadInserts {
        var key = Key(\c, true);
        var line = TTLine(
            List[
                LineNote(\c4, 1),
                LineNote(\g4, 1),
            ],
            key, \primary); 
        var dict = line.validTriadInserts;
        this.assert(dict[0] == [\c3, \e3, \g3, \c4, \e4, \g4, \c5]);
        this.assert(dict[1] == [\g3, \c4, \e4, \g4, \c5]); 
    }

    // TODO: not sure if these are actually correct
    test_validStepMotionInserts_simple {
        var key = Key(\c, true);
        var line = TTLine(
            List[
                LineNote(\c4, 1),
                LineNote(\g4, 1),
            ],
            key, \primary); 
        this.assert(line.validStepMotionInserts(8) == List[0]);
    }

    test_validStepMotionInserts_complex {
        var key = Key(\c, true);
        var line = TTLine(
            List[
                LineNote(\c4, 1),
                LineNote(\g4, 1),
                LineNote(\c5, 1),
                LineNote(\a4, 1),
            ],
            key, \primary); 
        this.assert(line.validStepMotionInserts(8) == List[0, 1, 2]);
        this.assert(line.validStepMotionInserts(2) == List[1, 2]);
    }

    test_validTriadRepeats {
        var key = Key(\c, true);
        var line = TTLine(
            List[
                LineNote(\c4, 1),
                LineNote(\g4, 1),
                LineNote(\a4, 1),
                LineNote(\c5, 1),
            ],
            key, \primary); 
        this.assert(line.validTriadRepeats == [0, 1, 3]);
    }

    test_counterpointStepMotion {
        var line = TTLine.counterpointStepMotion(\c4, \c5, cKey);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1),
            LineNote(\f4, 1),
            LineNote(\g4, 1),
            LineNote(\a4, 1),
            LineNote(\b4, 1),
            LineNote(\c5, 1),
        ]);
        this.assert(line == expected);
    }

    test_counterpointStepMotion_raiseCase1 {
        var line = TTLine.counterpointStepMotion(\e4, \a4, aMinorKey);
        var expected = TTLine(List[
            LineNote(\e4, 1),
            LineNote(\fs4, 1),
            LineNote(\gs4, 1),
            LineNote(\a4, 1),
        ]);
        this.assert(line == expected);
    }

    test_counterpointStepMotion_raiseCase2 {
        var line = TTLine.counterpointStepMotion(\e4, \g4, aMinorKey);
        var expected = TTLine(List[
            LineNote(\e4, 1),
            LineNote(\fs4, 1),
            LineNote(\g4, 1),
        ]);
        this.assert(line == expected);
    }

    test_counterpointStepMotion_raiseCase3 {
        var line = TTLine.counterpointStepMotion(\gs4, \e4, aMinorKey);
        var expected = TTLine(List[
            LineNote(\gs4, 1),
            LineNote(\fs4, 1),
            LineNote(\e4, 1),
        ]);
        this.assert(line == expected);
    }

    test_randomNeighborChoice_simpleDown {
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\c4, 1)
        ], cKey);
        var expected = TTLine(List[
            LineNote(\c4, 1 %/ 2),
            LineNote(\b3, 1 %/ 2),
            LineNote(\c4, 1),
        ], cKey);
        thisThread.randSeed = 123;
        this.assert(line.randomNeighbor == RandomNeighborChoice(0, false));
        this.assert(line == expected);
    }

    test_randomNeighborChoice_simpleUp {
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\c4, 1)
        ], cKey);
        var expected = TTLine(List[
            LineNote(\c4, 1 %/ 2),
            LineNote(\d4, 1 %/ 2),
            LineNote(\c4, 1),
        ], cKey);
        thisThread.randSeed = 2;
        this.assert(line.randomNeighbor == RandomNeighborChoice(0, true));
        this.assert(line == expected);
    }

    test_randomTriadInsert_primary {
        var choice;
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1)
        ], cKey, \primary);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g3, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1)
        ], cKey, \primary);
        thisThread.randSeed = 2;
        choice = line.randomTriadInsert;
        this.assert(choice == RandomTriadInsertChoice(1, \g3));
        this.assert(line == expected);
    }

    test_randomTriadInsert_lower{
        var choice;
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1)
        ], cKey, \primary);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1)
        ], cKey, \primary);
        thisThread.randSeed = 6;
        choice = line.randomTriadInsert;
        this.assert(choice == RandomTriadInsertChoice(1, \g4));
        this.assert(line == expected);
    }

    test_randomStepMotion {
        var choice;
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\c4, 1)
        ], cKey, \primary);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\g4, 1),
            LineNote(\f4, 1),
            LineNote(\e4, 1),
            LineNote(\d4, 1),
            LineNote(\c4, 1),
        ], cKey, \primary);
        thisThread.randSeed = 6;
        choice = line.randomStepMotionInsert(100);
        this.assert(choice == RandomStepMotionChoice(1));
        this.assert(line == expected);
    }

    test_randomTriadRepeat{
        var choice;
        var line = TTLine(List[ 
            LineNote(\c4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1)
        ], cKey, \primary);
        var expected = TTLine(List[
            LineNote(\c4, 1),
            LineNote(\d4, 1),
            LineNote(\e4, 1),
            LineNote(\e4, 1)
        ], cKey, \primary);
        thisThread.randSeed = 6;
        choice = line.randomTriadRepeat;
        this.assert(choice == RandomTriadRepeatChoice(2));
        this.assert(line == expected);
    }

}