package obj;

import util.Globals;

public class Business extends DMU {
	protected int overlapIndex[] = new int[Globals.busOverlap];

	public Business(Location globalLoc) {
		// set number of pointers to random elements of the InfoSys
//		super();
		// set localKnowledgeIndex
		for (int i = 0; i < Globals.N / 2; i++) {
			localKnowledgeIndex[i] = i;  // for InfoSys -> i = Globals.N / 2 through Globals.N
		}
		// set overlapIndex
		int[] temp = Globals.rand.nextUniqueIntBetween(Globals.N / 2, Globals.N - 1, Globals.busOverlap); // for infoSys -> begin = 0; end = Globals.N / 2 - 1
		System.arraycopy(temp, 0, overlapIndex, 0, temp.length);
		// set knowledge & control 
		for (int i = 0; i < Globals.N; i++) { knowledge[i] = false; control[i] = false; } // first set everything to false;
		for (int i = 0; i < localKnowledgeIndex.length; i++) { knowledge[localKnowledgeIndex[i]] = true; control[localKnowledgeIndex[i]] = true; }
		for (int i = 0; i < overlapIndex.length; i++) { 
			knowledge[overlapIndex[i]] = true;
			if (Globals.authority) { control[overlapIndex[i]] = true; }
		}
		// set control
		setLocation(globalLoc);
		resetSearchHistory();
	}

}
