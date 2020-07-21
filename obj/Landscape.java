	package obj;

import java.util.ArrayList;
import util.Globals;
import util.MersenneTwisterFast;

public class Landscape {
	private InfluenceMatrix im; 
	private int cases; // no need if we just use im.numCases()
	private double[] fitnessContribs;
	private double[] fitness;
	private double landscapeMaxFitness;
	private double landscapeMinFitness;
	private long landscapeID; 
	
	public Landscape(int lndscpID, InfluenceMatrix inf) {
		landscapeID = (long)lndscpID;
		MersenneTwisterFast rnd = new MersenneTwisterFast(landscapeID);
		im = inf;
		//im.print();
		cases = im.numCases(); // NEED?
//		System.out.println("cases: " + cases);
		fitnessContribs = new double[cases];
		for (int i = 0; i < cases; i++) {
			double d = rnd.nextDouble();
//			System.out.println(d);
			fitnessContribs[i] = d;
 		}
		setFitnessLandscape(); // sets the fitness landscape and sets max fitness level
	}
	public long getLandscapeID() {
		return landscapeID;
	}
	
	public int numCases() {
		return im.numCases();
	}

	/**
	 * returns fitness for a position with cognitive simplifiaction
	 * @param l
	 * @return
	 */
	public double getFitness(Location l) {
		double fitness = 0d;
//		System.out.println(l.toString());
		for (int i = 0; i < Globals.N; i++) {
			String s = l.getLocationAt(i, im);
//			System.out.println(i + "," + K + ":" + s);
//			System.out.println(i + ":" + s);
			double d = getFitnessContribution(s, i);
			fitness += d;
		}
		fitness = fitness / Globals.N;
		return fitness;
	}
	
	/**
	 * return fitness for a position as per almirall and casadesus-masanell
	 * @param l
	 * @param know boolean array of which elements the DMU has knowledge e.g., [true, true, true, true, false, true, true, false] for Business
	 * @return fitness value 
	 */
	public double getFitness(Location l, boolean[] know) {
		double fitness = 0d;
		for (int i = 0; i < Globals.N; i++) {
			if (know[i]) {
				String s = l.getLocationAt(i, im);
				double d = getFitnessContribution(s, i);
				fitness += d;
			}
		}
		fitness = fitness / Globals.N;
		return fitness;
	}
	
	private void setFitnessLandscape() {
		double maxFitness = 0d;
		double minFitness = 1d;
		fitness = new double[(int)(Math.pow(2, Globals.N))];
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = getFitness(Location.getLocationFromInt(i));
			if (fitness[i] > maxFitness) {
				maxFitness = fitness[i];
			}
			if (fitness[i] < maxFitness) {
				minFitness = fitness[i];
			}
		}
		landscapeMaxFitness = maxFitness;
		landscapeMinFitness = minFitness;
	}
	
	/**
	 * returns the maximum fitness value for the landscape
	 */
	public double getMaxFitness() {
		return landscapeMaxFitness;
	}

	public double getMinFitness() {
		return landscapeMinFitness;
	}

    /**
     * used by 
     * private method that sets the fitness contribution for a particular 
     * location (string representation)   
     */
	private double getFitnessContribution(String locationString, int place) {
		// since locationString may contain a " ", we need to fill those spaces with 1s and 0s
		// i.e., if space exists, remove entry and replace with combinations of 0/1 replacing the space
		ArrayList<String> list = new ArrayList<String>(); 
		list.add(locationString);
		
		boolean done = false;
		while (!done) {
			boolean found = false;
			for (int i = 0; i < list.size(); i++) {
				String listItem = list.get(i);
				int idx = listItem.indexOf(' '); // position of ' ' in string listItem
				if (idx > -1) { // found ' '
					found = true;
					list.remove(i);
					char[] stringArray = listItem.toCharArray();
					String s0 = ""; String s1 = "";
					for (int j = 0; j < stringArray.length; j++) {
						if (idx == j) {
							s0 += "0"; s1 += "1";
						} else {
							s0 += stringArray[j]; s1 += stringArray[j];
						}
					}
					list.add(s0); list.add(s1);
					break;
				} 
			} 
			if (!found) { done = true; }
		}
		// now list should contain all combinations of matching policy choices (e.g., "  1" -> "001", "011", "101", "111")
		
		double retVal = 0d; int cnt = 0;
		for (int i = 0; i < list.size(); i++) {
			String listItem = list.get(i);
			int fitnessContribIndex = im.getStartPosition(place) + Integer.parseInt(listItem, 2);
			double d = fitnessContribs[fitnessContribIndex];
//			System.out.println(listItem + "\t" + fitnessContribIndex + "\t" + d);
			retVal += d;
			cnt++;
		}
		retVal = retVal / cnt; // get average
		return retVal;
	}
	
    /**
     * Utility for printing all possible fitness values for given landscape   
     */
	public void printLandscapeFitness() {
		for (int i = 0; i < fitness.length; i++) {
			System.out.println(Location.getLocationStringFromInt(i) + "\t" + fitness[i]);
		}
	}

    /**
     * main method for testing purposes only.  
     */

	public static void main(String args[]) {
		InfluenceMatrix infmat = new InfluenceMatrix("conf/n8.txt");
		Landscape l = new Landscape(0, infmat);
//		String[] locationStringArray = {" ", "0", "1", "1", "0", " ", "1", "1", "0", "0"}; // 0011001100
//		String[] locationStringArray = {" ", "0", "1", "1", "0", " ", " ", "0", "1", "1", "0", " ", " ", "0", "1", "1"}; // 0011001100
//		String[] locationStringArray = {"0", "1", "0", "1", "0", "0", "0", "0", "0", "1", "0", "1", "0", "0", "0", "1"};
//		String[] locationStringArray = {"0", "1", "0", "1", "0", "0", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "};
//		String[] n1 = {"1", "1", "0", "1", "0", "0", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //11010000
//		String[] n2 = {"0", "0", "0", "1", "0", "0", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //00010000
//		String[] n3 = {"0", "1", "1", "1", "0", "0", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //01110000
//		String[] n4 = {"0", "1", "0", "0", "0", "0", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //01000000
//		String[] n5 = {"0", "1", "0", "1", "1", "0", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //01011000
//		String[] n6 = {"0", "1", "0", "1", "0", "1", "0", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //01010100
//		String[] n7 = {"0", "1", "0", "1", "0", "0", "1", "0", " ", " ", " ", " ", " ", " ", " ", " "}; //01010010
//		String[] n8 = {"0", "1", "0", "1", "0", "0", "0", "1", " ", " ", " ", " ", " ", " ", " ", " "}; //01010001
		String[] n1 = {"1", " ", " ", "1", " ", "0", "0", "0"}; //11010000
//		String[] n2 = {"0", "0", "0", "1", "0", "0", "0", "0"}; //00010000
//		String[] n3 = {"0", "1", "1", "1", "0", "0", "0", "0"}; //01110000
//		String[] n4 = {"0", "1", "0", "0", "0", "0", "0", "0"}; //01000000
//		String[] n5 = {"0", "1", "0", "1", "1", "0", "0", "0"}; //01011000
//		String[] n6 = {"0", "1", "0", "1", "0", "1", "0", "0"}; //01010100
//		String[] n7 = {"0", "1", "0", "1", "0", "0", "1", "0"}; //01010010
//		String[] n8 = {"0", "1", "0", "1", "0", "0", "0", "1"}; //01010001
//		Location loc = new Location(locationStringArray);
		Location n1loc = new Location(n1);
//		Location n2loc = new Location(n2);
//		Location n3loc = new Location(n3);
//		Location n4loc = new Location(n4);
//		Location n5loc = new Location(n5);
//		Location n6loc = new Location(n6);
//		Location n7loc = new Location(n7);
//		Location n8loc = new Location(n8);
//		System.out.println(l.getFitness(loc));
//		System.out.println("finding out test fitness");
		System.out.println(l.getFitness(n1loc));
//		System.out.println(l.getFitness(n2loc));
//		System.out.println(l.getFitness(n3loc));
//		System.out.println(l.getFitness(n4loc));
//		System.out.println(l.getFitness(n5loc));
//		System.out.println(l.getFitness(n6loc));
//		System.out.println(l.getFitness(n7loc));
//		System.out.println(l.getFitness(n8loc));
//		l.printLandscapeFitness();
	}
}
