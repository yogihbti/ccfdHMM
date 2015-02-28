/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baumwelch;

import java.io.IOException;
import kmeans.*;

public class Detector {

    private HMM hmm;
    String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    private int o[];
    private double threshold=0;

    public double getThreshold() {
        return threshold;
    }


    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public Detector(HMM hmm) {
        this.hmm = hmm;
    //this.o=o;
    }

    public void sethmm(HMM hmm) {
        this.hmm = hmm;
    }

    public double[] detect(int o[]) {
        double fwd[][] = hmm.forwardProc(o);
        double bwd[][] = hmm.backwardProc(o);
        double result[] = new double[o.length];
        System.out.println(o.length);


        for (int i = 0; i < o.length; i++) {
            result[i] = 0;
            for (int j = 0; j < hmm.numStates; j++) {

                result[i] = result[i] + fwd[j][i] * bwd[j][i];
            }
        }
        /* for(int k=0;k<result.length;k++)
        {
        System.out.println(" "+result[k]);
        }*/
        return result;



    }

    public boolean fraudEvaluation(double alpha, double customerprofile[],int newTransaction) {
        boolean risk=false;
        /*if(newTransaction<=300)
        {
        risk=false;
        }
        else if(newTransaction>1000)
        {
        risk=true;
        }


        else if (alpha > this.threshold) {
        risk=true;
        }*/
        if (alpha > this.threshold) {
        risk=true;
        }

        return risk;

    }

    public double calculateAlpha(int newTransaction,kMeans km) throws IOException
    {
        double alpha = 0;
        double[] result = null;
        double[] newResult = null;
        double difference;

        result = detect(hmm.readFile());

        //Enter the new Transaction here
        //km.newTransaction(newTransaction);
        km.newTransactionModf(newTransaction);
        newResult=detect(hmm.readFile());
        
        System.out.println("newRsult = " + newResult[0] + " old result = " + result[0]);

        difference = result[0] - newResult[0];
        alpha = difference / result[0];
        System.out.println("alpha="+alpha);

        return alpha;

    }

  
}

