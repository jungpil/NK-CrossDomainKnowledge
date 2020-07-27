package app;

import util.Globals;
import obj.Organization;
import obj.Iterative;
import obj.Agile;
import obj.Sequential;
import obj.Joint;
import java.util.Vector;
import java.util.Iterator;
import java.io.PrintWriter;
import util.StatCalc;

public class Simulation {
	private static Vector<Organization> organizations; //= new Vector<Organization>();
	
	public static void main(String args[]) {
		String configFile = setConfigFile(args);
		Globals.loadGlobals(configFile);
		if (Globals.debug) { System.out.println("configFile loaded"); }

		for (int j = 0; j < Globals.numRuns; j++) {
			// create landscape
			Globals.createLandscape(j);
			//Globals.landscape.printLandscapeFitness();
			if (Globals.debug) { System.out.println("landscape created at " + j); }
			
			organizations = new Vector<Organization>();
			// create numOrgs organizations
			
			for (int i = 0; i < Globals.numOrgs; i++) {
				if (Globals.orgType.equals("iterative")) {
					organizations.add(new Iterative(i));
				} else if (Globals.orgType.equals("agile")) {
					organizations.add(new Agile(i));
				} else if (Globals.orgType.equals("sequential")) {
					organizations.add(new Sequential(i));
				} else if (Globals.orgType.equals("joint")) {
					organizations.add(new Joint(i));
				} else {
					System.err.println("Unknown orgType: " + Globals.orgType);
					System.exit(0);
				}
			}
			if (Globals.debug) { System.out.println("orgs created for landscape " + j); }
			
			if (Globals.reportLevel.equals("details")) {
				reportDetails(Globals.out, -1);
			} else if (Globals.reportLevel.equals("summary")) {
				reportSummary(Globals.out, -1);
			}
			if (Globals.debug) { System.out.println("initialized for landscape " + j); }
			// run
			if (Globals.periods == -1) {
				runUntilEnd();
			} else {
				run();
			}
			if (Globals.debug) { System.out.println("finished running for landscape " + j); }
//			System.out.println("landscape:\t" + j + "\t" + Globals.landscape.getMaxFitness());
//			Globals.landscape.printLandscapeFitness();
			Globals.landscape = null;
		}
	}
	
	private static void run() {
		for (int t = 0; t < Globals.periods + 1; t++) {
			if (Globals.debug) {
				System.out.println("Simulation.run()\tperiod:\t" + t);
			}
			for (Organization org : organizations) {
//				org.run(t);
				org.run();
			}
			if (Globals.reportLevel.equals("details")) {
				reportDetails(Globals.out, t);
			} else if (Globals.reportLevel.equals("summary")) {
				reportSummary(Globals.out, t);
			} else if (Globals.reportLevel.equals("final")) {
				if (t == Globals.periods) {
					reportSummary(Globals.out, t);
				}
			}
		}
	}

	private static void runUntilEnd() {
		int t = 0; 
		while (!allEnded()) {
			for (Organization org : organizations) {
//				org.run(t);
				org.run();
			}
			if (Globals.reportLevel.equals("details")) {
				reportDetails(Globals.out, t);
			} else if (Globals.reportLevel.equals("summary")) {
				reportSummary(Globals.out, t);
			}
			t++;
		}
	}
	
	private static boolean allEnded() {
		boolean retBool = true;
		for (Organization org : organizations) {
			if (!org.finished()) {
				retBool = false; 
				break;
			}
		}
		return retBool;
	}
	
	private static String setConfigFile(String[] args) {
		String retString = "";
		if (args.length > 1) {
			System.err.println("Need at most one argument (config file).  Try again.");
			System.exit(0);
		} else if (args.length == 0) {
			retString = "";
			
		} else {
			retString = args[0];
		}
		return retString;
	}
	
	private static void reportDetails(int period) {
		for (Organization org : organizations) {
			// System.out.print(Globals.landscapeMax + "\t" + Globals.landscapeMin + "\t");
			org.printDetails(period);
		}
	}
	
	private static void reportDetails(PrintWriter pw, int period) {
		for (Organization org : organizations) {
			// pw.print(Globals.landscapeMax + "\t" + Globals.landscapeMin + "\t");
			org.printDetails(pw, period);
		}
	}

	private static void reportSummary(int period) {
		// calc average and report average for landscape
		StatCalc stat = new StatCalc();
		int completed = 0;
		for(Organization org : organizations) {
			stat.enter(org.getOrgFitness());
//			stat.enter(org.getFitness());
			if (org.finished()) { completed++; }
		}
		System.out.println(Globals.landscape.getLandscapeID() + "\t" + period + "\t" + completed + "\t" + stat.getMean() + "\t" + stat.getStandardDeviation() + "\t" + stat.getMin() + "\t" + stat.getMax());
	}

	private static void reportSummary(PrintWriter pw, int period) {
		// calc average and report average for landscape
		// calc average and report average for landscape
		StatCalc stat = new StatCalc();
		int completed = 0;
		for(Organization org : organizations) {
			stat.enter(org.getOrgFitness() / Globals.landscapeMax);
//			stat.enter(org.getFitness());
			if (org.finished()) { completed++; }
		}
		pw.println(Globals.landscape.getLandscapeID() + "\t" + Globals.landscapeMax + "\t" + Globals.landscapeMin + "\t" + period + "\t" + completed + "\t" + stat.getMean() + "\t" + stat.getStandardDeviation() + "\t" + stat.getMin() + "\t" + stat.getMax() + "\t" + stat.getCount());
	}
}
