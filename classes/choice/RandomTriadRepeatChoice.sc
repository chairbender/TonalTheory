RandomTriadRepeatChoice {
    // index of the note that was repeated
    var <index;

    *new { |index|
        ^super.newCopyArgs(index);
    }

    == { arg that; ^this.compareObject(that, #[\index]) }

	hash {
		^this.instVarHash(#[\index])
	}

    printOn { |stream|
        stream << "RandomTriadRepeatChoice(" << index << ")";
    }

}