/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kmeans;

/**
 *
 * @author sudeep
 */
public class cluster {


    // Cluster Number
    private int clusterNumber;
    //Mean data point of this cluster
    private kMeansPoint mean;

    public cluster(int _clusterNumber) {

        this.clusterNumber = _clusterNumber;

    }

    public void setMean(kMeansPoint meanDataPoint) {

        this.mean = meanDataPoint;

    }

    public kMeansPoint getMean() {

        return this.mean;

    }

    public int getClusterNumber() {

        return this.clusterNumber;

    }
    @Override
  public String toString()
  {
     return  "This is Cluster NO="+this.getClusterNumber()+"\n And It's Mean is"+this.getMean().getY();
  }
    


    public static void main(String[] args) {

        cluster c1 = new cluster(1);
        c1.setMean(new kMeansPoint(3, 4));
        System.out.println(c1.getMean());


    }
}
