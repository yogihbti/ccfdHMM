/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baumwelch;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmeans.*;

/**
 *
 * @author sudeep
 */
public class Main {

    public static void main(String args[]) {

        int noOfClusters = 3;
        int noOfStates = 6;
        String fileName = "input1.txt";
        kMeans km = new kMeans(noOfClusters, fileName);

        try {
            //System.out.println("before read data");
            km.readData();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }

        km.runKMeans();
        km.outputProcessor();
        System.out.println("Clustering done \n" + km);


        //  Initializing the HMM
        HMM hmm = new HMM(noOfStates, noOfClusters);
        hmm.initializeHMM();
              
        try {
            hmm.train(hmm.readFile(), 100);
            hmm.print();
        } catch (IOException ex) {
        }
        // Detection Phase starts
        Detector detector = new Detector(hmm);
        detector.setThreshold(0.91);
        double alpha = 0;
        try {
        alpha = detector.calculateAlpha(10, km);
        } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        /*double[] result = null;
        double[] newResult = null;
        double difference;
        //double alpha;
        try {
        result = detector.detect(hmm.readFile());
        } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Enter the new Transaction here
        km.newTransaction(1000);
        try {
        newResult = detector.detect(hmm.readFile());
        } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("newRsult = " + newResult[0] + " old result = " + result[0]);

        difference = result[0] - newResult[0];

        alpha = difference / result[0];
        System.out.println("diff is " + alpha);*/
        //we are checking customer profile........

        double customerprofile[] = km.customerProfile(hmm);
        System.out.print("this is our customer profile  " + "<");
        for (int i = 0; i < customerprofile.length; i++) {
            System.out.print(customerprofile[i] + ",");
        }
        System.out.println(">");

        km.commit(detector.fraudEvaluation(alpha, customerprofile,300));


    }
}
