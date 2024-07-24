TestTTLine : UnitTest {

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

}
