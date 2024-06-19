TestTonalPitchClassSymbol : UnitTest {
   test_initPitchClassSymbols {
      this.assert(TonalPitchClassSymbol.naturals[\a] == 0);
      this.assert(TonalPitchClassSymbol.naturals[\f] == 5);
   }
   
   test_natural_withAccidentals_returnsNatural {
      this.assert(\as.natural == \a);
      this.assert('a#'.natural == \a);
   }

   test_naturalWithAccidentals_returnsWithAccidentals {
      this.assert('a'.withAccidentals(2, "#") == 'a##');
      this.assert('a#'.withAccidentals(2, "#") == 'a###');
   }

   test_numFlats_returnsNumFlats {
      this.assert(\ab.numFlats == 1);
      this.assert(\a.numFlats == 0);
      this.assert(\abb.numFlats == 2);
   }
}
