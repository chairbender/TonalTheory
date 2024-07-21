TestLine : UnitTest {
    test_rearticulate {
        this.assert(LineNote(\a4, 1 %/ 4).note == \a4);
        this.assert(LineNote(\a4, 1 %/ 4).duration == (1 %/ 4));
    }
}
