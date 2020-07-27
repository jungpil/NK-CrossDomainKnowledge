package app;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateConf {
// periods=250
// runs=200
// outfile=/Users/jungpil/misq-koverlap/code/results/a_n16k15_8_8_0.txt
// influenceMatrix=/Users/jungpil/misq-koverlap/code/inf/n16k15.txt
// numOrgs=100
// N=16
// busOverlap=8
// isOverlap=8
// authority=false
// orgType=agile
// numSubOrgs=2
// reportLevel=final
// debug=false
// fitnessCalc=ac2010

    private static String path = "/Users/jungpil/git/misq-koverlap/code/";
    private static int[] busOverlap = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}; 
    private static int[] isOverlap = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}; 
    // private static String[] infMat = new String[]{"7ll", "7ur", "7bal"};
    private static String[] infMat = new String[]{"7ll"};
    
    public static void main(String args[]) {
        for (int m = 0; m < infMat.length; m++) {
            for (int b = 0; b < busOverlap.length; b++) {
                for (int i = 0; i < isOverlap.length; i++) {
                    // for (int rep = 0; rep < 10; rep++) {
                        // String outfilename = path + "results/a_n16k" + infMat[m] + "-" + rep + "_" + busOverlap[b] + "_" + isOverlap[i] + "_0.txt";
                        String outfilename = path + "results500/a_n16k" + infMat[m] + "_" + busOverlap[b] + "_" + isOverlap[i] + "_0.txt";
                        // String conffilename = path + "conf/a_n16k" + infMat[m] + "-" + rep + "_" + busOverlap[b] + "_" + isOverlap[i] + "_0.conf";
                        String conffilename = path + "conf/a_n16k" + infMat[m] + "_" + busOverlap[b] + "_" + isOverlap[i] + "_0.conf";
                        // String inffilename = path + "inf/" + infMat[m] + "-" + rep + ".txt";
                        String inffilename = path + "inf/" + infMat[m] + ".txt";
                        
                        try {
                            PrintWriter pw = new PrintWriter(new FileOutputStream(conffilename, false), true);
                            pw.println("periods=500\nruns=100");
                            pw.println("outfile=" + outfilename);
                            pw.println("influenceMatrix=" + inffilename);
                            pw.println("numOrgs=100\nN=16");
                            pw.println("busOverlap=" + busOverlap[b]);
                            pw.println("isOverlap=" + isOverlap[i]);
                            pw.println("authority=false\norgType=agile\nnumSubOrgs=2\nreportLevel=final\ndebug=false\nfitnessCalc=gl2000");
                            pw.close();
                        } catch (IOException io) {
                            System.err.println(io.getMessage());
                            io.printStackTrace();
                        }
                    // }
                }
            }
    
        }
    }

}