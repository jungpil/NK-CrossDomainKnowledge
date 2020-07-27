package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CountMoves {
    private static int landscapeID = -1;
    private static int prevLandscapeID = -1;
    private static double landscapeMax = 0.0d;
    private static double landscapeMin = 0.0d;
    private static int period = -2;
    private static int orgID = -1;
    private static int busStatus = -3;
    private static int isStatus = -3;
    private static int nextDMU = -2;
    private static String location = "";
    private static String prevLocation = "";
    private static double busFitness = 0.0d;
    private static double prevBusFitness = 0.0d;
    private static double isFitness = 0.0d;
    private static double prevIsFitness = 0.0d;
    private static double fitness = 0.0d;
    private static double prevFitness = 0.0d;
    // calc 
    private static int countBusMoves;
    private static int countISMoves;
    private static double busFitGain;
    private static double isFitGain;
    private static boolean aggregate = false; 

    public static void main(String args[]) {
        String infile = args[0];
        String output = args[1];
        
        if (output.equals("total")) {
            aggregate = true;
        }

        try(BufferedReader in = new BufferedReader(new FileReader(infile))) {
            String line = "";
            while ((line = in.readLine()) != null) {
                String[] tokens = line.split("\t");
                landscapeID = Integer.parseInt(tokens[0]);
                landscapeMax = Double.parseDouble(tokens[1]);
                landscapeMin = Double.parseDouble(tokens[2]);
                period = Integer.parseInt(tokens[3]);
                orgID = Integer.parseInt(tokens[4]);
                busStatus = Integer.parseInt(tokens[5]);
                isStatus = Integer.parseInt(tokens[6]);
                nextDMU = Integer.parseInt(tokens[7]);
                location = tokens[8];
                
                busFitness = Double.parseDouble(tokens[9]);
                isFitness = Double.parseDouble(tokens[10]);
                fitness = Double.parseDouble(tokens[11]);
                
                if (landscapeID != prevLandscapeID) { // new run 
                    if (prevLandscapeID != -1) {
                        if (aggregate) {
                            System.out.println(landscapeID + "\t" + landscapeMax + "\t" + landscapeMin + "\t" + countBusMoves + "\t" + countISMoves + "\t" + busFitGain + "\t" + isFitGain);
                        }
                    }
                    prevLandscapeID = landscapeID;
                    prevFitness = fitness;
                    prevBusFitness = busFitness;
                    prevIsFitness = isFitness;
                    // reset 
                    reset();
                }

                // check if bus / is moved
                if (busStatus == 1 && isStatus == -2 && !location.equals(prevLocation)) {
                    countBusMoves++;
                    busFitGain += fitness - prevFitness;
                    if (!aggregate) {
                        System.out.println(landscapeID + "\t" + landscapeMax + "\t" + landscapeMin + "\t" + "bus\t" + (busFitness - prevBusFitness) + "\t" + (fitness - prevFitness));
                    }
                }

                if (isStatus == 1 && busStatus == -2 && !location.equals(prevLocation)) {
                    countISMoves++;
                    isFitGain += fitness - prevFitness;
                    if (!aggregate) {
                        System.out.println(landscapeID + "\t" + landscapeMax + "\t" + landscapeMin + "\t" + "IS\t" + (isFitness - prevIsFitness) + "\t" + (fitness - prevFitness));
                    }
                }

                prevFitness = fitness;
                prevBusFitness = busFitness;
                prevIsFitness = isFitness;
                prevLocation = location;

            }
            if (aggregate) {
                System.out.println(landscapeID + "\t" + landscapeMax + "\t" + landscapeMin + "\t" + countBusMoves + "\t" + countISMoves + "\t" + busFitGain + "\t" + isFitGain);
            }
        }
        catch (IOException e) {
            System.out.println("File Read Error");
        }
        
    }

    private static void reset() {
        countBusMoves = 0;
        countISMoves = 0;
        busFitGain = 0.0d;
        isFitGain = 0.0d;
    }
}