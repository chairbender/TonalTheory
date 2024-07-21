/*For dealing with intervals
The interval keyword naming convention is as follows \<quality><number>,
where quality is a (augmented) d (diminished) m (minor), P (perfect) or M (major).
There can be any number of a's or d's to indicate things like doubly
or triply augmented/diminished intervals (but not in combination
with m, P, M, or combining a + d). number is any integer
greater than or equal to 1.

The term "line of fifths" comes up a lot. This link has 
a more detailed explanation as to what this is: https://music.stackexchange.com/a/17365/10617
*/
IntervalSymbol {
    classvar <lineOfFifthsNumbers;
    classvar <lineOfFifthsLetters;

    *initClass {
        // pattern of numbers that repeat in the line of fifths
        // intervals, with the 0th index of this list starting
        // on the center of the LOF
        lineOfFifthsNumbers = [1, 5, 2, 6, 3, 7, 4];
        // line of fifths letter orders, for the line of fifths centered on \d. To center
        // it on a different letter, we'd rotate it forwards or backwards.
        lineOfFifthsLetters = [\f, \c, \g, \d, \a, \e, \b];
    }

    *validate { |interval|
        var intervalStr = interval.asString;
        var quality = if (intervalStr.size < 2) {
            Error("interval must have both a quality and number").throw
        } {
            intervalStr[..intervalStr.size-2]
        };
        var number = intervalStr[intervalStr.size-1].interpret;
        var augCount = 0, dimCount = 0;
        if (number < 1) {Error("interval number must be 1 or higher").throw};
        quality.do({|c, i|
            if ([$a, $d, $m, $P, $M].includes(c).not) {
                Error("interval quality must contain only a, d, m, P, or M").throw
            };
            if ($a == c) {augCount = augCount + 1};
            if ($d == c) {dimCount = dimCount + 1};
            if ((augCount > 1) || (dimCount > 1) && ([$m, $P, $M].includes(c))) {
                Error("interval quality cannot contain m, P, or M in combination with a / d").throw
            };
            if ((augCount > 1) && (dimCount > 1)) {
                Error("interval quality cannot contain both a and d").throw
            };
        });
    }

    /*Given an index into the line of fifths, returns the quality abbreviation
    (m for minor, d for diminished, P for perfect, M for major, a for augmented,
    dd for doubly diminished, aa for doubly augmented and so on) as a string,
    for the interval name that would be on the line of fifths at that point
    (with idx 0 being the 'center' (where it is PU), negative values going towards minor and diminished,
    and positive values going towards major and augmented.
    The qualitative descriptors in the line of fifths are arranged with 3 perfects in the center, 4 minors/majors on each side,
    followed by 7 diminished/augmented, and then 7 doubly-diminished/doubly-augmented, and so forth.
    */
    *lineOfFifthsQuality { |idx|
        if ((idx >= -1) && (idx <= 1)) {^"P"};
        if ((idx >= -5) && (idx <= -2)) {^"m"};
        if ((idx <= 5) && (idx >= 2)) {^"M"};
        if (idx <= -6) { 
            ^Array.fill(((idx + 6) * -1).div(7) + 1, "d").join;
        } { 
            ^Array.fill(((idx - 6)).div(7) + 1, "a").join;
        }
    }

    /*Given an index into the line of fifths, returns the number of the interval at that point, (with 1 being unison)
    0 is defined to be 1, and the pattern as one goes lower than 1 is 4, 7, 3, 6, 2, 5, U (repeats).
    As the index increases, the pattern is 5, 2, 6, 3, 7, 4, 1 (repeats).
    */
    *lineOfFifthsNumber { |idx|
        ^if (idx >= 0) {
            lineOfFifthsNumbers[idx % 7]
        } {
            lineOfFifthsNumbers[6 - (((idx + 1) * -1) % 7)]
        }
    }

    /* Given an index into the line of fifths, return an interval name.
    The 0th index is the 'center' of the line of fifths (the point where the interval
    is PU). integers below that walk 'down' the line of fifths (towards minor then diminished intevals),
    and integers above that walk 'up'.*/
    *lineOfFifthsInterval { |idx|
        ^(this.lineOfFifthsQuality(idx) ++ this.lineOfFifthsNumber(idx).asString).asSymbol;
    }

    /*
    Starting on the (natural) letter 'tpc' in the line of fifths and walking 'offset' steps, return the letter we arrive at.
    */
    *lineOfFifthsLetter { |tpc, offset|
        var finalIdx = lineOfFifthsLetters.indexOf(tpc.natural) + offset;
        ^lineOfFifthsLetters[finalIdx % 7];
    }

    /*The difference in index between the letter names of the given TPCs
    (with the index coming from the circle of fifths).*/
    *lineOfFifthsStepsTo { |startingTPC, offsetTPC|
        ^lineOfFifthsLetters.indexOf(offsetTPC.natural) - lineOfFifthsLetters.indexOf(startingTPC.natural);
    }

    /*Returns the index of tpc on the line of fifths
    centered at centerTPC
    The index is defined to be 0 at the center and increasingly positive as
    sharps are added, increasingly negative as flats are added.
    So for example, the line of fifths centered at F looks like
    (and note the pattern continues in each direction, adding more
    sharps and flats after each loop through the letters):
    -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4  5  6  7
    Fb Cb Gb Db A  E   B F C G D A# E# B# F#
    */
    *lineOfFifthsTPCIndex{ |centerTPC, tpc|
        ^this.lineOfFifthsStepsTo(centerTPC, tpc) + ((tpc.alterationSemis - centerTPC.alterationSemis) * 7);
    }

    /*Return the alteration semitones of the tonal pitch class that exists at the given index
    in the line of fifths centered on tpc, as a number of semitones up (positive, i.e. sharps)
    or down (negative, i.e. flats).
    Another way of putting it is this walks on the line of fifths
    <index> steps, and returns the alterations, added to the alterations of TPC
    For example, the LOF centered at F looks like this 
    (note the actual center pitch class LETTER doesn't matter for this function as the pattern of alterations doesn't depend on the center letter, though it DOES depend on the ALTERATIONS of the center TPC):
    -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4  5  6  7
    Fb Cb Gb Db A  E   B F C G D A# E# B# F#
    So index = 3 returns 0 (there is no alteration on "D").
    But index 4 would return 1, and -4 would return -1 (corresponding to the A# / Db on the above line at index 4 / -4 .

    Now a more complicated example. What if the center TPC has alterations in it already? For example, let's say the center is F#. Then
    the LOF looks like this:
    -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4  5  6  7
    F C G D A#  E#   B# F# C# G# D# A## E## B## F##
    So index 3 returns 1, index 4 / -4 returns 2, 0.
    Basically, the alterations are just added to the alterations
    of the center TPC.
    */
    *lineOfFifthsAlterationSemis{ |tpc, index|
        // -3 to +3 are the "0 alterations" range. So let's say -3 is the "start"
        // and modulo from there
        var adjustedIdx = index + 3;
        // now how many times are we looping through the letters
        // in either direction? That gives us the
        // number of alterations to add / subtract from the center tpc
        var loops = adjustedIdx.div(7);
        ^(loops + tpc.alterationSemis);
    }

    /* get the tonal pitch class at the given index
    for the line of fifths centered on centerTPC
    */
    *lineOfFifthsTPC { |centerTPC, index|
        ^this.lineOfFifthsLetter(centerTPC, index).alterTPC(this.lineOfFifthsAlterationSemis(centerTPC, index));
    }

    /* return the quality of the provided interval symbol, as a string */
    *quality{ |interval|
        ^interval.asString.reject({|c| "0123456789".includes(c)});
    }

    /* return the number of the interval as an integer */
    *intervalNumber { |interval|
        ^interval.asString.select({|c| "0123456789".includes(c)}).interpret;
    }

    /*Returns the simple interval from which the compound interval is
    composed. If compound interval is not compound, just returns the interval.
    Anything above an interval number of 8 is considered a compound interval.
    */
    *simpleInterval { |interval|
        var number = interval.intervalNumber;
        var simpleNumber = if (number > 8) {
            ((number - 2) % 7) + 2

        } {number};
        ^ (interval.quality ++ simpleNumber).asSymbol;
    }

    /* given an interval quality as a string, inverts it
    (M becomes m, a/aa/... becomes d/dd/..., P remains the same) */
    *invertQuality { |qualityStr|
        if (qualityStr.beginsWith("M")) {^qualityStr.replace("M", "m")};
        if (qualityStr.beginsWith("m")) {^qualityStr.replace("m", "M")};
        if (qualityStr.beginsWith("d")) {^qualityStr.replace("d", "a")};
        if (qualityStr.beginsWith("a")) {^qualityStr.replace("a", "d")};
    }

    /*Returns the complement/inversion of the interval.
    If the interval is compound, just treats as the simple interval from which
    it is compounded (basically shifts everything to within an octave).
    */
    *invertInterval { |interval|
        var simpleInterval = interval.simpleInterval;
        var quality = interval.quality;
        var invertQuality = this.invertQuality(quality);
        var number = simpleInterval.intervalNumber;
        var invertNumber = 9 - number;
        ^(invertQuality ++ invertNumber).asSymbol;
    }

    /*Returns the interval name for the interval between the notes.
    Always defines the interval with respect to the lower note, so order
    of the parameters doesn't matter
    For intervals larger than an octave, uses an
    interval name like M10 or P12
    */
    *compoundIntervalBetween{ |note, otherNote|
        var higherNote = if (note.isAbove(otherNote)) {note} {otherNote};
        var lowerNote = if (note.isAbove(otherNote)) {otherNote} {note};
        var octaves = lowerNote.octavesTo(higherNote);
        var tpcIdx = this.lineOfFifthsTPCIndex(note.tpc, otherNote.tpc);
        var baseInterval = this.lineOfFifthsInterval(tpcIdx);
        var finalQuality = baseInterval.quality;
        var finalNumber = (7 * octaves) + baseInterval.intervalNumber;
        ^(finalQuality ++ finalNumber).asSymbol;
    }

    /* Like interval-keyword, but uses the simple interval from which the compound interval
    is formed for intervals larger than 8. So an interval of a major tenth would return :M3
    */
    *simpleIntervalBetween{ |note, otherNote|
        ^this.compoundIntervalBetween(note, otherNote).simpleInterval;
    }

    /*Given an interval name, returns the index of the interval in the line of fifths
    (i.e. with index 0 being at the center and the interval being P1).
    For intervals with an interval number of 8 or larger, changes the interval
    number to be mod 7 (i.e. brings the interval to within an octave)
    */
    *lineOfFifthsIntervalIndex { |interval|
        var intervalNumber = interval.number;
        var adjustedIntervalNumber = if (intervalNumber >= 8) {
            ((intervalNumber - 1) % 7) + 1;
        } {intervalNumber};
        var qualityStr = interval.quality;
        if (qualityStr == "P") {
            ^([4, 1, 5].indexOf(adjustedIntervalNumber) - 1)
        };
        if (qualityStr == "m") {
            ^([2, 6, 3, 7].indexOf(adjustedIntervalNumber) - 5)
        };
        if (qualityStr == "M") {
            ^([2, 6, 3, 7].indexOf(adjustedIntervalNumber) + 2)
        };
        if (qualityStr.includes("d")) {
            var occurrences = qualityStr.count({|c| c == $d});
            var simpleNum = -1 * [5, 1, 4, 7, 3, 6, 2].indexOf(adjustedIntervalNumber);
            ^(occurrences * -7) + simpleNum + 1;
        };
        if (qualityStr.includes("a")) {
            var occurrences = qualityStr.count({|c| c == $d});
            var simpleNum = [4, 1, 5, 2, 6, 3, 7].indexOf(adjustedIntervalNumber);
            ^(occurrences * 7) + simpleNum - 1;
        };
    }

    /* Returns the TPC indicating the center of the line-of-fifths given
    an interval name and tonal-pitch-class the interval should have in that line of fifths
    */
    *lineOfFifthsCenter { |interval, tpc|
        var intervalIdx = this.lineOfFifthsIntervalIndex(interval);
        ^this.lineOfFifthsTPC(tpc, -1 * intervalIdx);
    }

    /* Returns the note that is the given interval
    above the given note, with the correct enharmonic name.
    */
    *noteAbove { |interval, note|
        var octaves = interval.intervalNumber.div(8);
        var intervalIdx = this.lineOfFifthsIntervalIndex(interval);
        var nextTPC = this.lineOfFifthsTPC(note.tpc, intervalIdx);
        var octaveAdjust = if (note.tpc.letterStepsTo(nextTPC) < 0) {1} {0};
        var finalOctaves = note.octave + octaves + octaveAdjust;
        ^nextTPC.asNote(finalOctaves);
    }

    /* Returns the note that is the given interval
    below the given note, with the correct enharmonic name.
    */
    *noteBelow { |interval, note|
        var octaves = interval.intervalNumber.div(8);
        var bottomTPC = this.lineOfFifthsCenter(interval, note.tpc);
        var octaveAdjust = if (bottomTPC.letterStepsTo(note.tpc) < 0) {1} {0};
        var finalOctaves = note.octave - (octaves + octaveAdjust);
        ^bottomTPC.asNote(finalOctaves);
    }

    /* Returns true if the given interval is consonant (as defined by westergaardian theory).
    Consonant intervals are: P1 P8 P5 P4 (when the low note is not the lowest note sounding) M3 m3
    M6 and m6. isLowestSoundingNote indicates to the function whether the low note of the interval
    is the lowest note sounding in whatever context the interval appears in (for example if there is a third line
    where a note is playing below the two notes in the interval, isLowestSoundingNote should be set to false)
    */
    *isConsonant{ |interval, isLowestSoundingNote|
        ^Set[\P1, \P8, \P5, \M3, \m3, \M6, \m6].includes(interval) || (isLowestSoundingNote.not && interval == \P4);
    }
}

+ Symbol {
    lineOfFifthsStepsTo { |otherInterval| ^IntervalSymbol.lineOfFifthsStepsTo(this, otherInterval) }
    quality { ^IntervalSymbol.quality(this) }
    intervalNumber { ^IntervalSymbol.intervalNumber(this) }
}
