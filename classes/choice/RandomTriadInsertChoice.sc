/*
A selection for a random neighbor operation
*/
RandomTriadInsertChoice {
    // index the note was inserted at
    var <index;
    // note that was inserted
    var <note;

    *new { |index, note|
        ^super.newCopyArgs(index, note);
    }

    == { arg that; ^this.compareObject(that, #[\index, \note]) }

	hash {
		^this.instVarHash(#[\index, \note])
	}

    printOn { |stream|
        stream << "RandomTriadInsertChoice(" << index << "," << note << ")";
    }

}