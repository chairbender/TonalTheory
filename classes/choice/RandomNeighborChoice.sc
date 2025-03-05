/*
A selection for a random neighbor operation
*/
RandomNeighborChoice {
    // index of the chosen first note of the neighbor operation within the line
    var <index;
    // whether it was chosen to go up or down
    var <up;

    *new { |index, up|
        ^super.newCopyArgs(index, up);
    }

    == { arg that; ^this.compareObject(that, #[\index, \up]) }

	hash {
		^this.instVarHash(#[\index, \up])
	}

    printOn { |stream|
        stream << "RandomNeighborChoice(" << index << "," << up << ")";
    }

}