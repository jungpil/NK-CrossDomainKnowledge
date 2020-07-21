package obj;

import util.Globals;

public class Location {
	// vector of design choices -- size should be N (Globals.N)
	String[] location;
	
//	public Location(int size) {
//		location = new String[size];
//	}
	
	public Location() { // random location for initializing Organization
		location = new String[Globals.N];
		for (int i = 0; i < Globals.N; i++) {
			location[i] = Integer.toString(Globals.rand.nextInt(2));
		}
	}
	
	public Location(String[] loc) {
		location = new String[Globals.N];
		System.arraycopy(loc, 0, location, 0, loc.length);
	}
	
	public Location(char[] loc) {
		location = new String[Globals.N];
		for (int i = 0; i < location.length; i++) {
			location[i] = Character.toString(loc[i]);
		}
	}
	
	// initialize at a particular location
	public Location(Location aLocation) {
		location = new String[Globals.N];
		for (int i = 0; i < Globals.N; i++) {
			location[i] = aLocation.getLocationAt(i);
		}
	}
	
	public String[] getLocation() {
		return location;
	}
	
	public String getLocationAt(int index) {
		return location[index]; 
	}
	
	// sets changes the location to new location
	public void setLocation(Location aNewLoc) {
		for (int i = 0; i < location.length; i++) {
			location[i] = aNewLoc.getLocationAt(i);
		}
	}
	
	public void setLocation(String[] aNewLocStringArray) {
		for (int i = 0; i < location.length; i++) {
			location[i] = aNewLocStringArray[i];
		}
	}
	
	// for basic NK model where K are the K adjacent policy choices
	public String getLocationAt(int index, int k) {
		String retString = "";
		for (int i = index; i < index + k + 1; i++) { 
			retString += location[i % Globals.N]; 
		}
		return retString;
	}
	
	// compares two locations and returns true if all components are the same
	public boolean isSameAs(Location aLoc) {
		boolean match = true;
		String[] aLocString = aLoc.getLocation();
		for (int i = 0; i < aLocString.length; i++) {
			if (!aLocString[i].equals(location[i])) { // mismatch
				match = false;
				break;
			}
		}
		return match;
	}
	
	//@TODO code
	// for general landscape with influence matrix
	public String getLocationAt(int index, InfluenceMatrix im) {
		String retString = "";
		Interdependence intdep = im.getDependenceAt(index);
		
		for (int i = index; i < index + Globals.N; i++){
			if (intdep.isDependent(i % Globals.N)) {
				retString += location[i % Globals.N];
			}
		}
		return retString;
	}
		
	public static Location getLocationFromInt(int num) {
		String loc = Integer.toBinaryString(num);
		int zeros = Globals.N - loc.length();
		for (int j = 0; j < zeros; j++) {
			loc = "0" + loc;
		}
		char[] locArray = loc.toCharArray();
		return new Location(locArray);
	}
	
	public static String getLocationStringFromInt(int num) {
		String loc = Integer.toBinaryString(num);
		int zeros = Globals.N - loc.length();
		for (int j = 0; j < zeros; j++) {
			loc = "0" + loc;
		}
		return loc;
	}
	
	public String toString() {
		String retString = "";
		for (int i = 0; i < location.length; i++) {
			if (location[i].equals(" ")) {
				retString += "x";
			} else {
				retString += location[i];
			}
		}
		return retString;
	}
	
	// move location to target; make sure that the target only has the elements for which a DMU has authority 
	public void move(Location target) {
		for (int i = 0; i < location.length; i++) {
			if (!target.getLocationAt(i).equals(" ")) {
				location[i] = target.getLocationAt(i);
			}
		}
	}
	
	// random move
	public void move() {
		int r = Globals.rand.nextInt(location.length);
		if (Globals.debug) { System.out.println("position change: " + r);}
		if (location[r].equals("0")) {
			location[r] = "1";
		} else {
			location[r] = "0";
		}
	}
	
	public static void main(String args[]){
		Location l = new Location();
		Location global = new Location(l.getLocation());
		Location local = l;
		System.out.println("initialize\nGlobal: " + global.toString() + "\nLocal:  " + local.toString());
		local.move();
		System.out.println("after move\nGlobal: " + global.toString() + "\nLocal:  " + local.toString());
	}
}
