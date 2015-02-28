/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baumwelch;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.*;
import java.util.StringTokenizer;

/** This class implements a Hidden Markov Model, as well as
the Baum-Welch Algorithm for training HMMs.

 */
public class HMM {

    /** number of states */
    public int numStates;
    /** size of output vocabulary */
    public int sigmaSize;
    /** initial state probabilities */
    public double pi[];
    /** transition probabilities */
    public double a[][];
    /** emission probabilities */
    public double b[][];
    public double[][] fwd;                                      //  forwrd variable as a class variable
    public double[][] bwd;  //  same
    private String phoneNo=null;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    private String clusterResultFileName;

    public String getClusterResultFileName() {
        return clusterResultFileName;
    }

    public void setClusterResultFileName(String clusterResultFileName) {
        this.clusterResultFileName = clusterResultFileName;
    }

    /** initializes an HMM.
    @param numStates number of states
    @param sigmaSize size of output vocabulary
     */
    public HMM(int numStates, int sigmaSize) {
        this.numStates = numStates;
        this.sigmaSize = sigmaSize;

        pi = new double[numStates];
        a = new double[numStates][numStates];
        b = new double[numStates][sigmaSize];

        clusterResultFileName = "clusterResult.txt";
    }

    public void initializeHMM() {

        for(int i=0;i<this.numStates;i++)
        {
         pi[i]=0.5;
        }


        for (int i = 0; i < this.numStates; i++) {
            for (int j = 0; j < this.numStates; j++) {
                a[i][j] = 1.0 / (double) this.numStates;
            }
        }
        for (int i = 0; i < this.numStates; i++) {
            for (int j = 0; j < this.sigmaSize; j++) {
                b[i][j] = 1.0 / (double) this.sigmaSize;
            }
        }
    }

     public void initializeHMM(double piValue[]) {
        for(int i=0;i<this.numStates;i++)
        {
        pi[i]=piValue[i];
        }

        for (int i = 0; i < this.numStates; i++) {
            for (int j = 0; j < this.numStates; j++) {
                a[i][j] = 1.0 / (double) this.numStates;
            }
        }
        for (int i = 0; i < this.numStates; i++) {
            for (int j = 0; j < this.sigmaSize; j++) {
                b[i][j] = 1.0 / (double) this.sigmaSize;
            }
        }
    }

    /** implementation of the Baum-Welch Algorithm for HMMs.
    @param o the training set
    @param steps the number of steps
     */
    public void train(int[] o, int steps) {
        int T = o.length;


        double pi1[] = new double[numStates];
        double a1[][] = new double[numStates][numStates];
        double b1[][] = new double[numStates][sigmaSize];

        for (int s = 0; s < steps; s++) {
            /* calculation of Forward- and Backward Variables from the
            current model */
            fwd = forwardProc(o);
            bwd = backwardProc(o);

            /* re-estimation of initial state probabilities */
            for (int i = 0; i < numStates; i++) {
                pi1[i] = gamma(i, 0, o, fwd, bwd);
            }

            /* re-estimation of transition probabilities */
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    double num = 0;
                    double denom = 0;
                    for (int t = 0; t <= T - 1; t++) {
                        num += p(t, i, j, o, fwd, bwd);
                        denom += gamma(i, t, o, fwd, bwd);
                    }
                    a1[i][j] = divide(num, denom);
                }
            }

            /* re-estimation of emission probabilities */
            for (int i = 0; i < numStates; i++) {
                for (int k = 0; k < sigmaSize; k++) {
                    double num = 0;
                    double denom = 0;

                    for (int t = 0; t <= T - 1; t++) {
                        double g = gamma(i, t, o, fwd, bwd);
                        num += g * (k == o[t] ? 1 : 0);
                        denom += g;
                    }
                    b1[i][k] = divide(num, denom);
                }
            }
            pi = pi1;
            a = a1;
            b = b1;
        }
    }

    /** calculation of Forward-Variables f(i,t) for state i at time
    t for output sequence O with the current HMM parameters
    @param o the output sequence O
    @return an array f(i,t) over states and times, containing
    the Forward-variables.
     */
    public double[][] forwardProc(int[] o) {
        int T = o.length;
        double[][] forward = new double[numStates][T];

        /* initialization (time 0) */
        for (int i = 0; i < numStates; i++) {
            forward[i][0] = pi[i] * b[i][o[0]];
        }

        /* induction */
        for (int t = 0; t <= T - 2; t++) {
            for (int j = 0; j < numStates; j++) {
                forward[j][t + 1] = 0;
                for (int i = 0; i < numStates; i++) {
                    forward[j][t + 1] += (forward[i][t] * a[i][j]);
                }
                forward[j][t + 1] *= b[j][o[t + 1]];
            }
        }

        return forward;
    }

    /** calculation of  Backward-Variables b(i,t) for state i at time
    t for output sequence O with the current HMM parameters
    @param o the output sequence O
    @return an array b(i,t) over states and times, containing
    the Backward-Variables.
     */
    public double[][] backwardProc(int[] o) {
        int T = o.length;
        double[][] backward = new double[numStates][T];

        /* initialization (time 0) */
        for (int i = 0; i < numStates; i++) {
            backward[i][T - 1] = 1;
        }

        /* induction */
        for (int t = T - 2; t >= 0; t--) {
            for (int i = 0; i < numStates; i++) {
                backward[i][t] = 0;
                for (int j = 0; j < numStates; j++) {
                    backward[i][t] += (backward[j][t + 1] * a[i][j] * b[j][o[t + 1]]);
                }
            }
        }

        return backward;
    }

    /** calculation of probability P(X_t = s_i, X_t+1 = s_j | O, m).
    @param t time t
    @param i the number of state s_i
    @param j the number of state s_j
    @param o an output sequence o
    @param fwd the Forward-Variables for o
    @param bwd the Backward-Variables for o
    @return P
     */
    public double p(int t, int i, int j, int[] o, double[][] fwd, double[][] bwd) {
        double num;
        if (t == o.length - 1) {
            num = fwd[i][t] * a[i][j];
        } else {
            num = fwd[i][t] * a[i][j] * b[j][o[t + 1]] * bwd[j][t + 1];
        }

        double denom = 0;

        for (int k = 0; k < numStates; k++) {
            denom += (fwd[k][t] * bwd[k][t]);
        }

        return divide(num, denom);
    }

    /** computes gamma(i, t) */
    public double gamma(int i, int t, int[] o, double[][] fwd, double[][] bwd) {
        double num = fwd[i][t] * bwd[i][t];
        double denom = 0;

        for (int j = 0; j < numStates; j++) {
            denom += fwd[j][t] * bwd[j][t];
        }

        return divide(num, denom);
    }

    /** prints all the parameters of an HMM */
    public void print() {
        DecimalFormat fmt = new DecimalFormat();
        fmt.setMinimumFractionDigits(5);
        fmt.setMaximumFractionDigits(5);

        for (int i = 0; i < numStates; i++) {
            System.out.println("pi(" + i + ") = " + fmt.format(pi[i]));
        }
        System.out.println();

        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                System.out.print("a(" + i + "," + j + ") = " +
                        fmt.format(a[i][j]) + "  ");
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < numStates; i++) {
            for (int k = 0; k < sigmaSize; k++) {
                System.out.print("b(" + i + "," + k + ") = " +
                        fmt.format(b[i][k]) + "  ");
            }
            System.out.println();
        }
    }

    /** divides two doubles. 0 / 0 = 0! */
    public double divide(double n, double d) {
        if (n == 0) {
            return 0;
        } else {
            return n / d;
        }
    }

    public int[] readFile() throws IOException {

        int[] o=null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.clusterResultFileName));
            StringTokenizer st = new StringTokenizer(br.readLine(), " \t\n\r\f,");
            int i = 0;
             o = new int[st.countTokens()];            // create array size for the given no of tokens
            while (st.hasMoreTokens()) {
                o[i] = Integer.parseInt(st.nextToken());         //  create array o
                i++;
            }


        } catch (FileNotFoundException e) {
            System.out.println("input.txt  file not found. Create manually");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Problem reading input.txt");
            System.exit(0);
        }
        
        return o;
    }
}
