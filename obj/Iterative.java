package obj;

public class Iterative extends Organization {
	//protected Location location; // superclass Organization has location as global location 

	public Iterative(int id) {
		super(id);					// constructor of super class (sets location)
		orgType = "iterative";		// set orgType (parent)
	}
	
	public void run() {
		next = next(lastDMU);
		
		int other = Math.abs(next - 1);
		if (next != -1) {
			Location moveTo = units[next].search(); // moveTo is just a localLocation so I need to figure out the full location to update the org's location
			if (moveTo == null) { // search was unsuccessful
				// check if next is at local optimum
				if (units[next].isLocalOptimum()) {
					// if yes, 
					//		set own (next) searchstatus to -1
					searchStatus[next] = -1;
					// 		check if other's search status == -2
					if (searchStatus[other] == -2) {
						//		if yes, 
						//				reset other's search history
						units[other].resetHistory();
					}
				} else {
					// if no, 
					//		set own (next) search status to 0
					searchStatus[next] = 0;
				}
				
			} else { // search was successful and moveTo is the new localLocation for the business unit (DMU) -- business or infosys
				// set Org's location to match moveTo; 
				updateLocation(moveTo, next);
				// update own (next) localLocation
				units[next].setLocation(location);
				// update other's localLocaiotn
				units[other].setLocation(location);
				// set own (next) searchstatus to 1
				searchStatus[next] = 1;
				// reset own (next) searchhistory
				units[next].resetHistory();
				// set other's searchstatus to -2;
				searchStatus[other] = -2;
			}
		} else {
			completed = true;
			// this should never happen --> could not find next move (i.e., both bus and IS are at local optima)
		}
		lastDMU = next;
	}

	private int next(int last) { // same as next sequential --> difference will be in resetting -1 to -2 during search
		int nextIdx = -1;
		int other = Math.abs(last - 1);

		if (last == -2) { // simulation hasn't started yet
			nextIdx = 0; // business should start
		} else if (last == -1) { // simulation has ended
			nextIdx = -1;
		} else { // DMU is still searching
			if (searchStatus[last] == 0 || searchStatus[last] == 1) { 
				nextIdx = last;
			} else if (searchStatus[last] == -1) {
				if (searchStatus[other] == -1) {
					nextIdx = -1;
				} else {
					nextIdx = other;
				}
			}
		}
		return nextIdx;
	}

}
