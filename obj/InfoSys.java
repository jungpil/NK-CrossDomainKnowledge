package obj;

import util.Globals;

public class InfoSys extends DMU {
	private int overlapIndex[] = new int[Globals.isOverlap];

	public InfoSys(Location globalLoc) {
		// set number of pointers to random elements of the InfoSys
//		super();
		// set localKnowledgeIndex
		for (int i = Globals.N / 2; i < Globals.N; i++) {
			localKnowledgeIndex[i - (Globals.N / 2)] = i;  // for InfoSys -> i = Globals.N / 2 through Globals.N
		}
		if (Globals.debug) { System.out.println("knowledge index created for infoSys"); }

		// set overlapIndex
		int[] temp = Globals.rand.nextUniqueIntBetween(0, (Globals.N / 2) - 1, Globals.isOverlap); // for infoSys -> begin = 0; end = Globals.N / 2 - 1
		System.arraycopy(temp, 0, overlapIndex, 0, temp.length);
		if (Globals.debug) { System.out.println("overlap inddex created for infoSys"); }
		// set knowledge & control 
		for (int i = 0; i < Globals.N; i++) { knowledge[i] = false; control[i] = false; } // first set everything to false;
		if (Globals.debug) { System.out.println("knowledge and control reset for infoSys"); }
		for (int i = 0; i < localKnowledgeIndex.length; i++) { knowledge[localKnowledgeIndex[i]] = true; control[localKnowledgeIndex[i]] = true; }
		if (Globals.debug) { System.out.println("knowledge and control created for infoSys"); }
		for (int i = 0; i < overlapIndex.length; i++) { 
			knowledge[overlapIndex[i]] = true;
			if (Globals.authority) { control[overlapIndex[i]] = true; }
		}
		if (Globals.debug) { System.out.println("overlap set for infoSys"); }
		setLocation(globalLoc);
		if (Globals.debug) { System.out.println("set location for infoSys"); }
		resetSearchHistory();
		if (Globals.debug) { System.out.println("reset search history for infoSys"); }
	}

}
