
/*
A line (TT short for TonalTheory as Line exists
already in SC) is a sequence of line notes in the context of a particular key. Chords / intervals
are only produced by simlatenously-sounding lines and are
not present in an individual line.
This class provides the general Line object as well as
operations to transform lines within the context of Tonal Theory.

Why not just have Line extend List so we
could easily do all the many operations SC allows?
Because there are operations on List that would break the invariants of what a Line is. Instead, Line encapsulates and tries to maintain those invariants.
*/
TTLine {
    // List of LineNote
    var <lineNotes;
    // Key the line is in
    var <key;
    // type of the line within the counterpoint - \primary (upper), \secondary (other upper), \lower (lower)
    // Note that both primary and secondary lines are considered "upper lines"
    var <type;

    *new { |lineNotes, key, type|
        if (lineNotes.isKindOf(List).not) {
            Error("lineNotes must be a kind of List but was" + lineNotes.class).throw;
        };
        ^super.newCopyArgs(lineNotes, key, type);
    }

	== { arg that; ^this.compareObject(that, #[\lineNotes, \key, \type]) }

	hash {
		^this.instVarHash(#[\lineNotes, \key, \type])
	}

    at { |i|
        ^lineNotes[i];
    }

    printOn { |stream|
        stream << "TTLine(";
        lineNotes.printOn(stream);
        stream << "," << key << "," << type << ")";
    }

    /*
    Number of notes in this line;
    */
    size {
        ^(lineNotes.size);
    }

    /*
    sum of beats in this line
    */
    beats {
        ^(lineNotes.collect({|lineNote| lineNote.duration * 4}).sum);

    }

    isUpperLine {
        ^((type == \primary) || (type == \secondary));
    }

    isLowerLine {
        ^(this.isUpperLine.not);
    }

    /*
    Rearticulate the note at the given index in this line. This preserves the total beats in the line.
    (the note is split into two notes whose total duration equals the original note's duration,
    and who are the same note as the original). The duration of the first note in the
    rearticulation is equal to firstDuration. firstDuration must be a duration less than
    the duration of the rearticulated note in the given line.
    */
    rearticulate { |index, firstDuration|
        var targetNote = lineNotes[index];
        var lastDuration = targetNote.duration - firstDuration;
        lineNotes[index] = LineNote(targetNote.note, firstDuration);
        lineNotes.insert(index + 1, LineNote(targetNote.note, lastDuration));
    }

    /*
    Preserves the number of beats in the line as the note is split.
    Given an index pointing at the first note of a rearticulation in the line (articulationIndex),
    inserts a neighbor between the rearticulation notes. A neighbor is simply a note a minor or major second
    above or below the two notes, caused by rearticulating the first note
    of the rearticulation, then moving it up or down a second. if 'up' is true, the neighbor will be above, otherwise it will be
    below. The note will be split, with the duration of the first note being'firstDuration', and it must be less than
    the duration of the first note of the rearticulation. 'interval' must be \m2 or \M2.
    */
    neighbor { |articulationIndex, firstDuration, up, interval|
        var articulationNote1 = lineNotes[articulationIndex];
        var articulationNote2 = lineNotes[articulationIndex + 1];
        var neighborNote = if (up, { articulationNote1.note.intervalAbove(interval) }, { articulationNote1.note.intervalBelow(interval) });
        if (firstDuration >= articulationNote1.duration) {
            Error("firstDuration " ++ firstDuration ++ " must be less than duration of first note of articulation " ++ articulationNote1.duration).throw;
        };
        if (articulationNote1.note != articulationNote2.note) {
            Error("articulationIndex must point at first note of an articulation, but did not. note1: " ++ articulationNote1.note ++ " note2: " ++ articulationNote2.note).throw;
        };
        if ((interval != \m2) && (interval != \M2)) {
            Error("interval must be \m2 or \M2, but was: " ++ interval).throw;
        };
        this.rearticulate(articulationIndex, firstDuration);
        lineNotes[articulationIndex + 1].note = neighborNote;
    }

    /*
    Given an index into the line, and a note forming a consonant interval
    with the note at that index (westergaardian theory consonance), returns a new line with
    an arpeggiation performed at that index using the given note.
    In an arpeggiation, the arpeggiated note is rearticulated (with
    firstDuration as the duration of the first note of the rearticulation), and then the second note of the rearticulateion is set to 'arpeggiateNote'. 
    So all of the same requirements of rearticulation also apply to
    this method.
    'up' indicates whether the arpeggiation should go from low
    note to high note or vice versa. 'firstDuration' must be less than the duration of the note
    at the given index in the line. 'arpeggiateNote' must form a westergaardian theory consonant interval with the note being arpeggiated.
    (see the isConsonant method of IntervalSymbol).
    Note that whether the note is consonant depends on whether it is 
    the lowest sounding note (if multiple lines are sounding). 
    As this method has no idea what other lines are sounding, it
    will accept values which are consonant regardless of being the 
    lowest sounding note.
    */
    arpeggiateNote { |index, arpeggiateNote, firstDuration, up|
        var targetNote = lineNotes[index];
        var arpeggiateInterval = targetNote.note.compoundIntervalTo(arpeggiateNote);
        var lowNote = if (arpeggiateNote.isAbove(targetNote.note), { targetNote.note }, { arpeggiateNote });
        var highNote = if (arpeggiateNote.isAbove(targetNote.note), { arpeggiateNote }, { targetNote.note });
        var finalFirstNote = if (up, { lowNote }, { highNote });
        var finalLastNote = if (up, { highNote }, { lowNote });
        if (arpeggiateInterval.isConsonant(true).not && arpeggiateInterval.isConsonant(false).not) {
            Error("interval between targeted note " ++ targetNote.note ++ " and arpeggiateNote " ++ arpeggiateNote ++ " is " ++ arpeggiateInterval ++ " which is not consonant").throw;
        };
        this.rearticulate(index, firstDuration);
        lineNotes[index].note = finalFirstNote;
        lineNotes[index + 1].note = finalLastNote;
    }

    /*
    Like arpeggiateNote, but the interval is specified rather than
    the note of the arpeggiation. The interval is applied to the note
    at the target index to determine the arppegiateNote. When 'up' is true the interval will be up.
    */
    arpeggiateInterval { |index, interval, firstDuration, up|
        var targetNote = lineNotes[index];
        var arpeggiateNote = if (up, {targetNote.note.intervalAbove(interval)}, {targetNote.note.intervalBelow(interval)});
        this.arpeggiateNote(index, arpeggiateNote, firstDuration, up);
    }

    /*
    Returns a list containing the indices of
    notes in the line that are repetitions of the same pitch (the index of the first note
    of the repetition), and thus valid targets of the "neighbor" operation. 
    Durations are not considered when considering valid neighbor indexes.
    */
    validNeighborIndices {
        var result = List[];
        lineNotes.do({ |lineNote, i|
           if (i < (lineNotes.size - 1)) {
            if (lineNote.note == lineNotes[i+1].note) {
                result.add(i);
               };
           }; 
        });
        ^result;
    }

    /*
    Returns an array containing the notes
    that would be valid to insert before the note at
    the given index in the line
    TODO: Note that currently, we assume if the line is a lower line, it is the lowest sounding note - otherwise
    it is NOT the lowest sounding note. So this assumes there are no "voice crossings". We should make sure this assumption actually holds.
    */
    validTriadInsertsBefore { |index|
        var note = lineNotes[index].note;
        var prevNote = if (index == 0) { note } { lineNotes[index-1].note };
        var validTriads = key.triadPitchesNear(note);
        // check for consonance with the note that will be before and after the triad insert
        ^(validTriads.select({ |triadNote|
            var prevInterval = prevNote.simpleIntervalTo(triadNote);
            var afterInterval = note.simpleIntervalTo(triadNote); 
            var afterCompoundInterval = note.compoundIntervalTo(triadNote);
            prevInterval.isConsonant(this.isLowerLine) &&
                afterInterval.isConsonant(this.isLowerLine) &&
                (afterCompoundInterval.intervalNumber <= 8);
        }));
    }

    /*
    Returns a Dictionary of integers to array of notes, where
    each integer represents an index in targetLine (of a note), and
    the list it maps to indicates the notes that could be inserted
    BEFORE that note,, following the rules of westergaardian theory counterpoint
    for the upper and bass lines.

    The rules are:
    - Any triad pitch may precede the first pitch or be inserted between
    two consecutive pitches as long as:
        - no dissonant skip
        - no skip larger than an octave is created 
        - (and we'll consider a perfect fourth skip in an upper line
    to be consonant). 
    
    If this is an upper line being
    true also means inserts are allowed before the first note, otherwise they aren't
    */
    validTriadInserts {
        // iterate through the line and find all possible places to insert,
        // considering only inserting at the start if its an upper line
        var start = if (this.isUpperLine) { 0 } { 1 };
        var result = Dictionary();
        for (start, lineNotes.size - 1) { |i|
            var inserts = this.validTriadInsertsBefore(i);
            if (inserts.size != 0) {
                result.put(i, inserts);
            };
        };
        ^result;
    }

    /*
    Returns a list containing the indexes of notes where the
    following note forms a skip (interval a 3rd or larger) with the note at the specified index,
    and where the number of beats that would be inserted between the two notes
    in a step motion is <= beats (4 beats per note because all inserted notes are going to be whole notes)
    */
    validStepMotionInserts { |beats|
        var result = List[];
        for (0, lineNotes.size-2) {|i|
            var curNote = lineNotes[i].note;
            var nextNote = lineNotes[i+1].note;
            var interval = curNote.compoundIntervalTo(nextNote);


            if ((((interval.intervalNumber - 2)*4) <= beats) &&
                (interval.intervalNumber > 2)) {
                result.add(i);
            };
        };
        ^result;
    }

    /*
    Returns an array containing the indexes of notes that are triad pitches of the key,
    meaning those notes are allowed to be repeated w.r.t. the rules of counterpoint.
    */
    validTriadRepeats {
        ^(lineNotes.selectIndices({|lineNote,i|
            var noteDegree = key.noteDegree(lineNote.note);
            (noteDegree == 0) || (noteDegree == 2) || (noteDegree == 4);
        }));
    }

    /*
    Preserves the total beats in the line.
    Mutates the line. Performs a random neighbor operation on a valid target (i.e. rearticulate the first note of an existing
    rearticulation, then raise/lower the new 2nd note) 
    Rules are followed:
        - the neighbor is always a member of the diatonic collection
            - except when it is the lower neighbor to the tonic in a minor key, in which
            case it is altered so as to be a minor 2nd from the tonic.
    
    The targeted note will be split in half (half going to the neighbor). 
    Returns a RandomNeighborChoice instance indicating what random choice was made, or nil if
    there was no valid target;
    */
    randomNeighbor { 
        var chosenIndex = this.validNeighborIndices.choose;
        var up = 0.5.coin;

        if (chosenIndex.isNil.not) {
            ^(this.neighbor_(chosenIndex, up))
        };
    }

    neighbor_{|chosenIndex, up|
        var chosenNote = lineNotes[chosenIndex].note;
        var chosenNoteDur = lineNotes[chosenIndex].duration;
        var firstDuration = chosenNoteDur * (1 %/ 2);
        var chosenNoteScaleIdx = key.scaleIndex(chosenNote);
        var chosenNoteDegree = key.noteDegree(chosenNote);

        // lower neighbor to the tonic in a minor key?
        var interval = if (up.not && (chosenNoteDegree == 1) && (key.isMinor)) {
            \m2
        } {
            // figure out the interval name we need to use to perform the operation
            var offset = if (up) {1} {-1};
            var neighborNote = key.noteAtScaleIndex(chosenNoteScaleIdx + offset);
            chosenNote.compoundIntervalTo(neighborNote)
        };
        this.neighbor(chosenIndex, firstDuration, up, interval);
        ^(RandomNeighborChoice(chosenIndex,up));
    }

    /*
    Mutates the line. Following rules for upper / lower line triad inserts (depending on this line's line type),
    randomly inserts a triad pitch of the given key (I III or V)
    between two pitches or before the first pitch, but never creating
    a dissonant skip or a skip larger than an octave. The duration
    of the inserted pitch is always a whole note
    Returns a RandomTriadInsertChoice indicating the choice that was made, or nil
    if there were no valid targets
    */
    randomTriadInsert {
        var validInserts = this.validTriadInserts;
        if (validInserts.notEmpty) {
            var chosenIndex = validInserts.keys.choose;
            var validNeighborNotes = validInserts[chosenIndex];
            if (validNeighborNotes.notEmpty) {
                var chosenNote = validNeighborNotes.choose;
                ^(this.triadInsert_(chosenIndex, chosenNote));
            }
        };
    }

    triadInsert_{|chosenIndex,chosenNote|
        lineNotes.insert(chosenIndex,LineNote(chosenNote,1));
        ^(RandomTriadInsertChoice(chosenIndex,chosenNote));
    }

    /*
    Mutates the line. Inserts a step motion between 2 random consecutive notes that form a skip.
    Uses the diatonic degrees for the step motion except for the following rules, where it instead
    uses the raised 6th / 7th in a minor key:
    - rising step motion from 5th degree to the tonic
    - rising step motion from the 5th degree to the 7th
    - falling step motion from the raised 7th to the fifth

    The duration of the inserted notes are all whole notes.
    Does nothing + returns nil if the target line has no valid places to insert. Otherwise, returns
    RandomStepMotionChoice indicating the choice. The duration of the notes inserted are all whole notes.
    beats means only step motions that would insert <= beats beats will be inserted
    */
    randomStepMotionInsert {|beats|
        var validIndices = this.validStepMotionInserts(beats - this.beats);
        if (validIndices.notEmpty) {
            var chosenIndex = validIndices.choose;
            ^(this.stepMotionInsert_(chosenIndex))
        };
    }

    stepMotionInsert_{|chosenIndex|
        var startLineNote = lineNotes[chosenIndex];
        var startNote = startLineNote.note;
        var endLineNote = lineNotes[chosenIndex+1];
        var endNote = endLineNote.note;
        var stepMotionLineNotes = TTLine.counterpointStepMotion(startNote, endNote, key, type).lineNotes;
        var result;
        // restore the original durations of the start / end note because the counterPointStepMotion puts them
        // at whole note
        stepMotionLineNotes[0] = startLineNote;
        stepMotionLineNotes[stepMotionLineNotes.size-1] = endLineNote;

        // I can't find a way to insert one list into the other at a specific place, so this will have to do instead...
        // start with the run up to the startNote, minus the start note itself since its already included in the
        // step motion line
        // TODO: maybe a more efficient way to do this - this creates quite a few "intermediate" copies...
        result = if (chosenIndex > 0) { lineNotes.copyRange(0,chosenIndex-1) } { List[] };
        // insert step motion line
        result.addAll(stepMotionLineNotes);
        // insert everything after, excluding the endNote from the original line since it's already at the end
        // of the inserted step motion
        if (chosenIndex < (lineNotes.size-2)) {
            result.addAll(lineNotes.copyRange(chosenIndex+2,lineNotes.size-1))
        };
        lineNotes = result;
        // restore the original durations of the start / end note
        ^(RandomStepMotionChoice(chosenIndex));
    }

    /*
    Adds 4 beats.
    randomly repeats a triad note in the line, always using a whole note
    for the new note. Returns RandomTriadRepeatChoice indicating the choice, or nil if no valid target.
    */
    randomTriadRepeat {
        var validIndices = this.validTriadRepeats;
        if (validIndices.notEmpty) {
            var chosenIndex = validIndices.choose;
            ^(this.triadRepeat_(chosenIndex));

        };
    }

    triadRepeat_{|chosenIndex|
        var chosenNote = lineNotes[chosenIndex].note;
        lineNotes.insert(chosenIndex+1, LineNote(chosenNote,1));
        ^(RandomTriadRepeatChoice(chosenIndex));
    }

    /*
    Returns an array consisting of the note midi values. Use with .deltas for
    Patterns.
    */
    midinotes {
        ^(lineNotes.collect({|lineNote|
            lineNote.note.midi
        }));
    }

    /*
    Returns an array consisting of the beats of each note. Use with .midinotes for
    Patterns. A note duration of 1 == a whole note, i.e. 4 beats 
    (all this stuff currently assumes 4/4 time)
    TODO: Does it work without conversion to float?
    */
    deltas {
        ^(lineNotes.collect({|lineNote|
            (lineNote.duration*4).asFloat
        }));
    }

    /*
    Randomly mutate the line until it reaches the indicated number of beats
    weights sets the weightings to use when choosing (see mutate)
    */
    mutateUntilBeats{|beats,weights=([0.25,0.25,0.25,0.25])|
        while {this.beats < beats} {
            this.mutate(beats, weights);
        };
    }

    /*
    Returns a list of RandomChoice (RandomNeighborChoice, RandomStepMotionChoice, RandomTriadInsertChoice,
    or RandomTriadRepeatChoice), representing all of the possible different mutations that could be performed
    on the current line. Any of the given elements can then be performed via
    calling mutate(randomChoice). Note that calling it would immediately invalidate the list, so you would need to call
    validMutations again to get a new set of choices.
    beats sets the max allowed total beats.
    */
    validMutations{|beats|
        ^(if (this.beats < beats) {
            var choices = List[];
            var neighborIndices = this.validNeighborIndices;
            var repeatIndices = this.validTriadRepeats;
            var insertDict = this.validTriadInserts;
            var stepMotionIndices = this.validStepMotionInserts(beats - this.beats);
            neighborIndices.do{|i| 
                choices.add(RandomNeighborChoice(i, true));
                choices.add(RandomNeighborChoice(i, false));
            };
            repeatIndices.do{|i| 
                choices.add(RandomTriadRepeatChoice(i))
            };
            insertDict.keysValuesDo{|index, validNotes| 
                validNotes.do{|validNote|
                    choices.add(RandomTriadInsertChoice(index, validNote))
                }
            };
            stepMotionIndices.do{|i| 
                choices.add(RandomStepMotionChoice(i))
            };
            ^choices;
        } { List[] })
    }

    /*
    Perform the mutation defined by randomChoice (see validMutations).
    Undefined behavior if not a valid choice for the current line.
    */
    performMutation{|randomChoice|
        case {randomChoice.isKindOf(RandomNeighborChoice)} {
            this.neighbor_(randomChoice.index, randomChoice.up);
        } { randomChoice.isKindOf(RandomStepMotionChoice)} {
            this.stepMotionInsert_(randomChoice.index);
        } { randomChoice.isKindOf(RandomTriadRepeatChoice)} {
            this.triadRepeat_(randomChoice.index);
        } { randomChoice.isKindOf(RandomTriadInsertChoice)} {
            this.triadInsert_(randomChoice.index, randomChoice.note);
        } { Error("unrecognized random choice class" + randomChoice.class).throw };
    }

    /*
    Chooses and applies a valid random mutation to a line.
    weights indicates the weightings to use for each kind of mutation.

    There are 4 choices (in order): triad repeat, neighbor, triad insert, step motion insert.
    triad repeat adds a whole note.
    neighbor splits a note in half (preserving total line length in beats)
    triad insert inserts a whole note
    step motion insert inserts 1 or more whole notes

    Unlike *chooseMutation, this will always perform a valid mutation (unless none exist at all for the line),
    instead of potentially doing nothing if it chooses a mutation type that has no valid options.
    */
    mutate{|beats, weights=([0.25,0.25,0.25,0.25])| 
        var mutationTypes = [RandomTriadRepeatChoice, RandomNeighborChoice, RandomTriadInsertChoice, RandomStepMotionChoice];
        var choices = this.validMutations(beats);
        var validChoiceTypes = Set[];
        var chosenType;
        var choice;
        choices.do{|choice| validChoiceTypes.add(choice.class)};
        if (validChoiceTypes.size == 0) {^nil};
        if (validChoiceTypes.size != 4) {
            // rebalance the weights to account for the nonexistent options
            var surplus = 0;
            var zeroCount = 0;
            mutationTypes.do{|mutationType, i|
                if (validChoiceTypes.includes(mutationType).not) {
                    surplus = surplus + weights[i];
                    weights[i] = 0;
                };
                if (weights[i] == 0) { zeroCount = zeroCount + 1 };
            };
            if (zeroCount == 4) {^nil};
            weights.do{|weight,i| 
                if (weight != 0) {
                    weights[i] = weight + (surplus / (4 - zeroCount));
                };
            };
        };
        chosenType = mutationTypes.wchoose(weights);
        choice = choices.select{|choice| choice.isKindOf(chosenType)}.choose;
        this.performMutation(choice);
        ^choice;
    }

    /*
    Given an array of TTLines, plays them in a basic way.
    Same args as Pattern.play (also same defaulting behavior)
    */
    *play{|lines, clock, protoEvent, quant|
        this.pPar(lines).play(clock, protoEvent, quant);
    }

    /*
    Just like pBinds, but wraps in a Ppar so they're ready to play in unison, without
    actually playing them.
    */
    *pPar{|lines,sustain=true|
        ^(Ppar(this.pBinds(lines,sustain)));
    }

    /*
    Given an array of TTLines, returns an array of Pbinds, representing a basic pattern built from each of the lines.
    Passing this to Ppar would play the counterpoint as written in a very basic manner.
    The events in the pattern only have \delta, \sustain (unless sustain=false), and \midinote set. Any additional customizations
    could be done using Pbindf / Pchain / Pset etc...
    */
    *pBinds{|lines,sustain=true|
        ^(lines.collect({|line|
            var deltas = line.deltas;
            var pat = Pbind(
                \delta, Pseq(deltas),
                \midinote, Pseq(line.midinotes)
            );
            if (sustain) { pat = Pbindf(pat, \sustain, Pseq(deltas))};
            pat;
        }))
    }

    /*
    More of a one-off toy than a utility.
    Starts with a set of lines and iteratively mutates and auditions them after each
    mutation.
    key gives the key.
    octaves is the octaves to use for each line (and the length of this also indicates the number of lines).
    beats is the total number of beats to ultimately reach.
    weights sets the weightings to use when choosing (see mutate).
    If it's a 1d array, it will be applied to each line. If it's a 2d array, there should be one
    array per line, and those will be used as the weights for that line only. For example
    [
        [] line 1 weights
        [] line 2 wieghts
        [] line 3 weights
        [] line 4 weights
    ] 
    */
    *evolve{|key, octaves, beats, weights=([0.25,0.25,0.25,0.25])|
        var lines = this.startLinesSameLength(key, octaves, weights);
        var normWeights = this.normalizeWeights(lines, weights);
        var waitTime, lastBeats, mutation, chosenLineIdx;

        while {lines[0].beats < beats} {
            //play
            this.play(lines, quant: 4);
    
            lastBeats = lines[0].beats.asFloat;
    
            // mutate
            // pick a line to pick a "mutation type" from, then catch up the others
            chosenLineIdx = (0..3).choose;
            lines[chosenLineIdx].mutate(beats, normWeights[chosenLineIdx]);
            this.mutateUntilSameLength(lines,normWeights);
    
            // wait for next audition
            ((lastBeats + 4) * (1 / TempoClock.default.tempo)).wait;
        };
        TTLine.play(lines, quant: 4);
    }

    /*
    expand weights to match lines size if it's 1d, otherwise do nothing.
    */
    *normalizeWeights{|lines,weights=([0.25,0.25,0.25,0.25])|
        ^(if (weights.maxSizeAtDepth(1) == 1) {
            weights!(lines.size)
        } { weights });
    }

    /*
    Given an array of TTLines, mutates lines as needed, randomly, until they are all the same number of beats
    as the line with the longes number of beats
    weights sets the weightings to use when choosing (see mutate).
    If it's a 1d array, it will be applied to each line. If it's a 2d array, there should be one
    array per line, and those will be used as the weights for that line only. For example
    [
        [] line 1 weights
        [] line 2 wieghts
        [] line 3 weights
        [] line 4 weights
    ]
    */
    *mutateUntilSameLength{|lines,weights=([0.25,0.25,0.25,0.25])|
        var max = -1;
        var normWeights = this.normalizeWeights(weights);
        lines.do({|line| if (line.beats > max) { max = line.beats}});
        lines.do({|line,i| line.mutateUntilBeats(max,normWeights[i])});
    }

    /*
    Not recommended as it can choose options that have no valid targets, wasting time - use .mutate instead.

    Returns a function that applies a mutation to a line, ensuring the function will not
    generate a line with more than the specified number of beats.
    This can then be applied to a line for e.g. via result.value(someLine);
    Weights sets the probabilities to use when choosing. Must sum to 1.0.
    There are 4 choices (in order): triad repeat, neighbor, triad insert, step motion insert.
    triad repeat adds a whole note.
    neighbor splits a note in half (preserving total line length in beats)
    triad insert inserts a whole note
    step motion insert inserts 1 or more whole notes
    Keep in mind this has no knowledge of the line it is potentially being passed to, so it
    may choose a mutation that has no valid options for the given line (in which case nothing will be done)
    */
    *chooseMutation{|beats, weights=([0.25,0.25,0.25,0.25])| 
        ^([
            {|line| line.randomTriadRepeat},
            {|line| line.randomNeighbor},
            {|line| line.randomTriadInsert},
            {|line| line.randomStepMotionInsert(beats - line.beats)}
        ].wchoose(weights));
    }


    /*
    Just like startLines, but if lines end up not same length, mutates shorter lines
    until they match the length of the longest line.
    weights sets the weightings to use when choosing (see mutate).
    If it's a 1d array, it will be applied to each line. If it's a 2d array, there should be one
    array per line, and those will be used as the weights for that line only. For example
    [
        [] line 1 weights
        [] line 2 wieghts
        [] line 3 weights
        [] line 4 weights
    ]
    */
    *startLinesSameLength{|key, octaves, weights=([0.25,0.25,0.25,0.25])|
        var lines = this.startLines(key, octaves);
        this.mutateUntilSameLength(lines, weights);
        ^lines;
    }

    /*
    like randomCounterPointLines but generates only the starting line for each line type.
    The lines are not guaranteed to be the same length - use startLinesSameLength instead if you
    want that.
    Apply further mutations yourself, such as using .mutate.
    */
    *startLines{|key, octaves|
        ^(octaves.collect({|octave, i|
            switch (i,
                0, { this.basicStepMotion(key, \primary, octave, #[3,5,8].choose) },
                (octaves.size-1), { this.randomBasicArpeggiation(octave, key) },
                { this.randomSecondaryBasicStructure(key, octave, 100) }
            )
        }));
    }

    /*
    Generates an array of random counterpoint lines with 'beats' number of beats

    octaves should be an array indicating the octaves to use for each line,
    and the size determines the number of lines generated. The first line is
    always primary, and last line is always lower. All other lines are secondary.
    key determines the key of the counterpoint.
    weights sets the weightings to use when choosing (see mutate).
    If it's a 1d array, it will be applied to each line. If it's a 2d array, there should be one
    array per line, and those will be used as the weights for that line only. For example
    [
        [] line 1 weights
        [] line 2 wieghts
        [] line 3 weights
        [] line 4 weights
    ]
    */
    *randomCounterpointLines{|beats, key, octaves, weights=([0.25,0.25,0.25,0.25])|
        var normWeights = this.normalizeWeights(weights);
        ^(octaves.collect({|octave, i|
            switch (i,
                0, { this.randomPrimaryLine(key, octave, beats,normWeights[i]) },
                (octaves.size-1), { this.randomLowerLine(key, octave, beats,normWeights[i]) },
                { this.randomSecondaryLine(key, octave, beats,normWeights[i]) }
            )
        }));
    }

    /*
    Generates a random primary line in the given key at the given
    octave, randomly performing any allowed operation some number of times.
    key-vector indicates the key, octave indicates the octave of the starting note,
    and beats determines the total beats of the resulting line
    weights sets the weightings to use when choosing mutations (see mutate).
    */
    *randomPrimaryLine {|key, octave, beats, weights=([0.25,0.25,0.25,0.25])|
        var allowedFinalNotes = [3,5,8].select({|interval|
            (interval * 4) <= beats
        });
        var line = this.basicStepMotion(key, \primary, octave, allowedFinalNotes.choose, beats);
        line.mutateUntilBeats(beats, weights);
        ^line;
    }

    /*
    Just like randomPrimaryLine, but for a secondary line
    */
    *randomSecondaryLine{|key, octave, beats, weights=([0.25,0.25,0.25,0.25])|
        var line = this.randomSecondaryBasicStructure(key, octave, beats);
        line.mutateUntilBeats(beats, weights);
        ^line;
    }

    /*
    Line randomPrimaryLine, but generates a lower line.
    */
    *randomLowerLine {|key, octave, beats, weights=([0.25,0.25,0.25,0.25])|
        var line = this.randomBasicArpeggiation(octave, key);
        line.mutateUntilBeats(beats, weights);
        ^line;
    }

    /*
    Creates a random starting basic structure for a secondary line. Rules are:
    - the final pitch must be a tonic triad member
    - the first pitch must be a tonic triad member no more than an octave from the final pitch
    
    firstOctave is the octave to use for the first note
    */
    *randomSecondaryBasicStructure{|key,firstOctave,beats|
        var chosenFirstDegree = #[0,2,4].choose;
        var chosenFirstNote = key.scale(firstOctave)[chosenFirstDegree];
        var chosenFinalNote = key.validSecondaryEndingNote(chosenFirstNote).choose;
        var line = TTLine(List[LineNote(chosenFirstNote,1),LineNote(chosenFinalNote,1)],key,\secondary);
        if (0.5.coin) {
            if (chosenFirstNote == chosenFinalNote) {
                line.randomNeighbor;
            } {
                line.randomStepMotionInsert(beats);
            }
        };
        ^line;
    }


    /*
    Creates a random basic arpeggiation line, following the rules for lower lines, in the given key.
    First octave is used as the octave of the first tonic.
    */
    *randomBasicArpeggiation {|firstOctave, key|
        var octaveOffset = #[-1, 0, 1].choose;
        var chosenLastOctave = firstOctave + octaveOffset;
        var chosenMiddleUp = 0.5.coin;
        var firstScale = key.scale(firstOctave);
        var lastScale = key.scale(chosenLastOctave);
        var firstNote = firstScale[0];
        var middleNote = if (chosenMiddleUp) { lastScale[4] } { lastScale[4].tpc.asNote(chosenLastOctave-1)};
        // will the middle note be more than a 5th from the 1st note? if so, we need to pick an insert note
        // otherwise we can choose whatever - it won't be used by basicArpeggiation.
        var insertNote = if (firstNote.compoundIntervalTo(middleNote).intervalNumber > 5) {
            // randomly pick an insert note because basicArpeggiation will use it
            var chosenInsert = key.validTriadInsertsBetween(firstNote, middleNote, \lower).choose;
            if (chosenInsert.isNil) { Error("chose nil insert, this should never happe").throw};
            chosenInsert
        } { \c4 };
        ^(this.basicArpeggiation(key, firstOctave, octaveOffset, chosenMiddleUp, insertNote))
    }

    /*
    Returns a step motion TTLine starting on the first note and ending on the ending note, all
    notes being a whole note.
    It's allowed for startNote / endNote to have alterations (i.e. accidentals) beyond that implied by the key!
    
    The rules for upper and lower line step motion are followed in generating this (the rules are the same):
    
    Uses diatonic degrees except for special cases where it uses the raised 6th or 7th degree in a minor key:
    - a rising step motion from the 5th degree to the tonic
    - a rising step motion from the 5th degree to the 7th
    - a falling step motion from the raised seventh to the fifth

    lineType is the line type to assign the returned line.
    */
    *counterpointStepMotion { |startNote, endNote, key, lineType|
        // since startNote or endNote could have alterations, we need to generate a diatonicStepMotion
        // using their UN-altered form!
        var startNoteUnaltered = key.unalteredNote(startNote);
        var endNoteUnaltered = key.unalteredNote(endNote);
        var line = this.fullDiatonicStepMotion(key, lineType, startNoteUnaltered, endNoteUnaltered);
        var startDegree = key.noteDegree(startNoteUnaltered);
        var endDegree = key.noteDegree(endNoteUnaltered);
        // use raised 6th or 7th in case any of the special conditions are met
        if (key.isMinor) {
            // minor key
            case {endNote.isAbove(startNote)} {
                // rising motion
                case {(startDegree == 4) && (endDegree == 0)} {
                    //rising from 5th to tonic,
                    // raise the 6th and 7th degree
                    line[1].note = line[1].note.alterNote(1);
                    line[2].note = line[2].note.alterNote(1);
                }
                {(startDegree == 4) && (endDegree == 6)} {
                    // rising from fifth to 7th
                    // raise the 6th
                    line[1].note = line[1].note.alterNote(1);
                };
            } {startNote.isAbove(endNote) && (endDegree == 4)} {
                var lowerStartNote = startNote.alterNote(-1);
                // use a function as the 2nd operand for short-circuiting
                if (key.containsNote(lowerStartNote) && 
                    { key.noteDegree(lowerStartNote) == 6 }) {
                    // falling from raised 7th  to the 5th
                    // raise the 6th
                    line[1].note = line[1].note.alterNote(1);
                };
            };
        };
        // restore the alterations to the start / end note
        // since the step motion was generated with their unaltered forms
        line[0].note = startNote;
        line[line.size-1].note = endNote;
        ^line;
    }

    /*
    For a key, starting note, and ending note,
    returns a TTLine consisting of whole notes where the notes form a step motion
    using the diatonic degrees of the key from the starting note to the ending note
    (including the start and end note). both notes must
    have pitch classes that are the key's diatonic degrees (no accidentals).
    This is typically used as a kind of building block to iterate upon in order to build a complete counterpoint.
    
    lineType is the line type to assign the returned line.
    */
    *fullDiatonicStepMotion { |key, lineType, startNote, endNote|
        var step = if (endNote.isAbove(startNote)) { 1 } { -1};
        var alterationsDict = KeySignature.alterationSemisDictOfKey(key);
        var degrees = key.degrees;
        // safeguard against infinite loop
        var iterations = 0;
        var result = List[];
        var currentNote = startNote;
        if (degrees.indexOf(startNote.tpc).isNil) {
            Error("key " ++ key ++ " does not contain TPC of start note " ++ startNote).throw;
        };
        if (degrees.indexOf(endNote.tpc).isNil) {
            Error("key " ++ key ++ " does not contain TPC of end note " ++ endNote).throw;
        };
        // walk from start to end, following the diatonic degrees
        while { iterations < 100 && currentNote != endNote } {
            var nextNatural = currentNote.nextNaturalNote(step);
            result.add(LineNote(currentNote, 1));
            currentNote = nextNatural.alterNote(alterationsDict[nextNatural.tpc]);
            iterations = iterations + 1;
        };
        result.add(LineNote(currentNote, 1));

        ^(TTLine(result,key,lineType));
    }

    /*
    fullDiatonicStepMotion with the start and end notes omitted
    */
    *diatonicStepMotion { |key, lineType, startNote, endNote|
        var fullStepMotion = this.fullDiatonicStepMotion(key, lineType, startNote, endNote);
        fullStepMotion.lineNotes.pop;
        fullStepMotion.lineNotes.removeAt(0);
        ^fullStepMotion;
    }

    /*
    Given a Key and starting octave,
    returns a TTLine that follows the operational rules:

    1. The final pitch in the basic step motion must be a tonic.
    2. The first pitch must be a tonic triad member a third fifth or octave
    above the final pitch.
    3. These two pitches must be joined by inserting the pitches of intervening diatonic degrees to form
    a descending step motion.

    To put it in simpler terms, this method creates a full diatonic step motion. The first note
    is a 3rd, 5th, or octave above the tonic, and the last note is the tonic. It is filled in via diatonic degrees.
    Every note is a whole note.

    key is the Key to use (e.g. Key(\c, false))
    lineType is the line type to assign the returned line.
    octave is the octave to play the ending tonic on. firstNoteIntervalNumber
    is either 3, 5, or 8 (representing the first note being a major or minor 3rd (based on the key being major or minor), perfect fifth, or perfect octave above the tonic)."
    */
    *basicStepMotion{ |key, lineType, octave, firstNoteIntervalNumber|
        var scale = key.scale(octave);
        var startNote = switch (firstNoteIntervalNumber,
            3, {scale[2]},
            5, {scale[4]},
            8, {scale[0].intervalAbove(\P8)},
            { Error("invalid firstNoteIntervalNumber - must be 3, 5, or 8").throw});
        ^this.fullDiatonicStepMotion(key, lineType, startNote, scale[0])
    }

    /*
    Generates a basic arpeggiation as a TTLine for the bass line, using the operation rules
    of Tonal Theory:
    
    -Final and first pitch must be tonics. 
    - The middle pitch must be a fifth above or fourth below the final tonic. 
    -If the middle pitch is more than a fifth from the first pitch, a 
    triad pitch must be inserted between the middle pitch and first pitch to
    make there be no intervals larger than an octave.
        - But that insertion must follow the rules for bass line secondary structure triad inserts.
    - The starting and ending pitches can't be larger than an octave, since that is undefined
    by westergaardian theory 
        - (triad ptiches are supposed to be inserted, but no operation could be performed 
        because any triad pitch insertion would create a skip larger than an octave
        with one of the notes in the event the middle note is chosen to be
        a fourth below the final pitch). 
    - Whole notes will be used for all notes

    key should be a Key indicating the key to use. 
    
    firstOctave should be an integer indicating the octave of the first tonic. It must be within 1 of lastOctave
    
    octaveOffset should be 1, 0, or -1 indicating whether the final tonic will be the same, an octave up, or an octave
    down
    
    isMiddleAboveFinalPitch should be true if the middle note should be a fifth above the final pitch, otherwise 
    it will be a fourth below it. 

    insertNote should be a note that should be put between the first and middle pitch in the event they are more
    than a fifth apart. It must follow the rules for triad inserts for bass line secondary structures. It
    is ignored if the middle note is a fifth or less apart from the first note.
    TODO: What function to call to see valid inserts?

    TODO: refactor so that we return an "intermediate" that allows choosing a valid middle note rather than applying all
        these constraints to it.
    */
    *basicArpeggiation { |key, firstOctave, octaveOffset, isMiddleAboveFinalPitch, insertNote|
        var firstScale = key.scale(firstOctave);
        var lastOctave = firstOctave + octaveOffset;
        var lastScale = key.scale(lastOctave);
        var firstNote = firstScale[0];
        var lastNote = lastScale[0];
        var middleNote = if (isMiddleAboveFinalPitch) {
            lastScale[4];
        } {
            key.scale(lastOctave - 1)[4];
        };
        if (octaveOffset > 1) {
            Error("first and lastOctave must be within 1 of each other, but weren't").throw;
        };
        if (firstNote.compoundIntervalTo(middleNote).intervalNumber > 5) {
            // first to middle interval is greater than a fifth, so need to insert
            // a triad pitch
            ^TTLine(List[LineNote(firstNote,1), LineNote(insertNote,1), LineNote(middleNote,1), LineNote(lastNote,1)],key,\lower)
        } {
            ^TTLine(List[LineNote(firstNote,1), LineNote(middleNote,1), LineNote(lastNote,1)],key,\lower)
        };
    }

}
