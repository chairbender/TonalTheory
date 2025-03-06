RandomStepMotionChoice {
    // index of the start note of the step motion, after which the step motion will be inserted
    var <index;

    *new { |index|
        ^super.newCopyArgs(index);
    }

    == { arg that; ^this.compareObject(that, #[\index]) }

	hash {
		^this.instVarHash(#[\index])
	}

    printOn { |stream|
        stream << "RandomStepMotionChoice(" << index << ")";
    }

}