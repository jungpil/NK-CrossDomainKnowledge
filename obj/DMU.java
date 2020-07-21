package obj;

import java.util.Collections;
import java.util.Vector;
import util.Globals;

public class DMU {
	protected Location globalLocation;
	protected Location localLoc; 
	protected int localKnowledgeIndex[] = new int[Globals.N / 2];
	protected boolean knowledge[] = new boolean[Globals.N];
	protected boolean control[] = new boolean[Globals.N];
	protected Location moveTo;

//	private int index;
//	private boolean move; 
	private Vector<Location> neighbors;
	
	public DMU() {
		
	}
	
	public DMU(int idx, Location loc, String type) {
//		index = idx; // idx = {0, 1} for {bus, IS}
//		move = false;
		// initialize search history
//		resetSearchHistory(); // empty tried; create new vector of neighbors
//		localFitness = getFitness(location, "local");
	}

	/**
	 * accessors
	 */
	
	public Location getLocation() {
		return localLoc;
	}
	
	public String[] getLocationString() {
		return localLoc.getLocation();
	}
	
	public void setLocation(String[] locationStr) {
		localLoc = new Location(locationStr);
	}
	
	public double getFitness() {
		return Globals.landscape.getFitness(localLoc);
	}
	
	public boolean[] getControlFilter() {
		return control;
	}
	
	// SEARCH
	public Location search() {
		moveTo = null;
//		boolean success = false;
		int numRemainingNeighbors = neighbors.size();
		int r = Globals.rand.nextInt(numRemainingNeighbors);
		Location neighbor = (Location)neighbors.remove(r); // need to find global location for neighbor as well
		String[] neighborGlobalLocString = new String[Globals.N];
		for (int i = 0; i < Globals.N; i++) {
			 if (neighbor.getLocationAt(i).equals(" ")) {
				 neighborGlobalLocString[i] = globalLocation.getLocationAt(i);
			 } else {
				 neighborGlobalLocString[i] = neighbor.getLocationAt(i);
			 }
		}
		Location neighborGlobalLocation = new Location(neighborGlobalLocString);
		
		double localFitness = 0d;
		double neighborFitness = 0d;
		if (Globals.localAssessment.equals("gl2000")) {
			localFitness = Globals.landscape.getFitness(localLoc);
			neighborFitness = Globals.landscape.getFitness(neighbor);
		} else if (Globals.localAssessment.equals("ac2010")) {
			localFitness = Globals.landscape.getFitness(globalLocation, knowledge);
			neighborFitness = Globals.landscape.getFitness(neighborGlobalLocation, knowledge); // need to find global location for neighbor as well
		}
//		System.out.println("localFitness:\t" + localFitness);
//		System.out.println("neighborFitness(" + r + "):\t" + neighborFitness);

		if (neighborFitness > localFitness) {
			// replace localLoc with neighbor & reset tried vector (no need to create new Location object)
//			localLoc.setLocation(neighbor); // set it now or later?
			// since moveTo was null before, we need to initialize it first and then set it's location
			moveTo = new Location(neighbor);
//			moveTo.setLocation(neighbor);
			resetSearchHistory();
//			success = true;
		}
		return moveTo;
	}

	public void resetHistory() {
		resetSearchHistory();
	}

	protected void resetSearchHistory() {
		neighbors = new Vector<Location>();
//		if (orgType.equals("joint2")) {
//			setNeighbors2();
//		} else {
			setNeighbors();
//		}
//		printNeighbors();
	}
	
	private void setNeighbors() {
		for (int i = 0; i < Globals.N; i++) {
			String[] neighborLocString = new String[Globals.N];
			boolean add = false;
			for (int j = 0; j < Globals.N; j++) {
				if (i == j) {
					if (localLoc.getLocationAt(j).equals("1")) {
						neighborLocString[j] = "0"; add = true;
					} else if (localLoc.getLocationAt(j).equals("0")) {
						neighborLocString[j] = "1"; add = true;
					} // else locationAt is blank so do nothing
				} else { // all other i != j
					neighborLocString[j] = localLoc.getLocationAt(j);
				}
			}
			if (add) { neighbors.add(new Location(neighborLocString)); }
		}
		Collections.shuffle(neighbors);  // shuffle so that order of retrieval is randomized
		
	}

//	public boolean hasMoved() {
//		return move;
//	}
	
	public boolean isLocalOptimum() {
		return neighbors.isEmpty();
	}
	

	private void printNeighbors() {
		for (Location neighbor : neighbors) {
			System.out.println(neighbor.toString());
		}
	}
	
	// debug only
	public void start() {
		resetSearchHistory();
	}

	protected void setLocation(Location loc) {
		globalLocation = loc;
		String[] localLocString = new String[Globals.N];
		
		for (int i = 0; i < Globals.N; i++) {
			if (knowledge[i]) {
				localLocString[i] = loc.getLocationAt(i);
			} else {
				localLocString[i] = " ";
			}
		}
		localLoc = new Location(localLocString);
	}
		
	public void printAllNeighbors() {
		printNeighbors();
	}

	// for debug purposes only
	public static void main(String args[]) {
		Globals.createLandscape(0);
		Location l = new Location();
		System.out.println(l.toString());
		Business b = new Business(l);
		InfoSys is = new InfoSys(l);
		System.out.println(b.getLocation().toString());
		System.out.println(is.getLocation().toString());
	}
}
