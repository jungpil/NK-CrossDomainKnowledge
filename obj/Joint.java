package obj;

public class Joint extends Organization {

	public Joint(int id) {
		super(id);					// constructor of super class (sets location)
		orgType = "joint";		// set orgType (parent)
	}
	
	public void run() {
		next = next(lastDMU);
		
		int other = Math.abs(next - 1);
		if (next != -1) {
			// moveTo is just a localLocation so I need to figure out the full location to update the org's location
			Location moveTo = units[next].search(); 
			if (moveTo == null) { // search was unsuccessful
				// check if next is at local optimum
				if (units[next].isLocalOptimum()) {
					// if yes, 
					//		set own (next) searchstatus to -1
					searchStatus[next] = -1;
					// 		check if other's search status == -2
				} else {
					// if no, 
					//		set own (next) search status to 0
					searchStatus[next] = 0;
				}
				
			} else { // search was successful and moveTo is the new localLocation for the business unit (DMU) -- business or infosys
				// set Org's location to match moveTo 
				updateLocation(moveTo, next);
				// update own (next) localLocation
				units[next].setLocation(location);
				// update other's localLocaiotn
				units[other].setLocation(location);
				// set own (next) searchstatus to 1
				searchStatus[next] = 1;
				// reset own (next) searchhistory
				units[next].resetHistory();
			}
		} else {
			completed = true;
			// this should never happen --> could not find next move (i.e., both bus and IS are at local optima)
		}
		lastDMU = next;
	}
	
	private int next(int last) {
		int nextIdx = -1;
		if (last == -2) {
			nextIdx = 0;
		} else if (last == -1) {
			nextIdx = -1;
		} else if (searchStatus[last] == -1) {
			nextIdx = -1;
		} else if (searchStatus[last] == 0 || searchStatus[last] == 1) {
			nextIdx = last;
		}
		return nextIdx;
	}
}
