package obj;

import java.io.PrintWriter;

import util.Globals;

public class Organization {
//	private int period;  // NO NEED ANYMORE
	protected int index; 
	protected String orgType;
	protected Location location; 
	protected DMU[] units = new DMU[Globals.numSubOrgs];  // initialize number of DMUs to Globals.numSubOrgs
	protected int[] searchStatus = new int[Globals.numSubOrgs]; // -2 for not started; -1 for local optimum; 0 for failed search; 1 for moved
	protected boolean completed;
	protected int lastDMU;
	protected int next = -1; // focal DMU (whose turn is it to search)?
	protected boolean lastPrinted = false;

	
	public Organization(int idx) {
		index = idx;
		// orgType = "whatever"; set by subclass
		location = new Location(); // random location to start with
		units[0] = new Business(location);
		if (Globals.debug) { System.out.println("Business DMU " + idx + " created"); }
		searchStatus[0] = -2;   // DO I NEED THIS?
		units[1] = new InfoSys(location);
		if (Globals.debug) { System.out.println("InfoSys DMU " + idx + " created"); }
		searchStatus[1] = -2;   // DO I NEED THIS?
		
		/* period = 0; */
		completed = false;
		lastDMU = -2;
	}
	
	public boolean finished() {
		return completed;
	}

	public void run() {} // implemented by subclasses
	
	public double getOrgFitness() {
		return Globals.landscape.getFitness(location);
	}

	protected void updateLocation(Location dmuLocalLoc, int dmuIndex) {
		// get the filter from the DMU and update the location 
		boolean filter[] = new boolean[Globals.N];
		System.arraycopy(units[dmuIndex].getControlFilter(), 0, filter, 0, Globals.N);
		String[] newGlobalLocation = new String[Globals.N];
		for (int i = 0; i < Globals.N; i++) {
			if (filter[i]) { 
				newGlobalLocation[i] = dmuLocalLoc.getLocationAt(i);
			} else {
				newGlobalLocation[i] = location.getLocationAt(i);
			}
		}
		location.setLocation(newGlobalLocation);		
	}
		
	// PRINTERS
	public void printDetails(int period) {
		double globalFitness = Globals.landscape.getFitness(location);
		double[] localFitness = new double[Globals.numSubOrgs];
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			localFitness[i] = units[i].getFitness();
		}
		String searchStatusString = "";
		String localFitnessString = "";
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			searchStatusString += searchStatus[i] + "\t";
			localFitnessString += localFitness[i] + "\t";
		}
		
		if (!completed) {
			System.out.println(period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
		} else {
			if (!lastPrinted) {
				System.out.println(period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
				lastPrinted = true;
			}
		}
	}
	
	public void printDetails(PrintWriter pw, int period) {
		double globalFitness = Globals.landscape.getFitness(location);
		double[] localFitness = new double[Globals.numSubOrgs];
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			localFitness[i] = units[i].getFitness();
		}
		String searchStatusString = "";
		String localFitnessString = "";
		for (int i = 0; i < Globals.numSubOrgs; i++) {
			searchStatusString += searchStatus[i] + "\t";
			localFitnessString += localFitness[i] + "\t";
		}
		
		if (!completed) {
			pw.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
		} else {
			if (!lastPrinted) {
				pw.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + index + "\t" + searchStatusString + next + "\t" + location.toString() + "\t" + localFitnessString + globalFitness);
				lastPrinted = true;
			}
		}
	}
	
	public static void main(String args[]) {
		Globals.createLandscape(0);
//		Location l = new Location();
//		System.out.println("initial location: " + l.toString());
		Organization o = new Organization(0);
		o.printLocation();
		o.printDMUNeighbors(0);
		o.printDMUNeighbors(1);
		
	}
	
	public DMU getDMU(int i) {
		return units[i];
	}
	
	public void printDMUNeighbors(int i) {
		System.out.println("unit: " + i);
		units[i].printAllNeighbors();
	}
	
	public void printLocation() {
		System.out.println(location.toString());
	}

}
