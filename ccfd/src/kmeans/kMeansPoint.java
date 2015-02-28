/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kmeans;

 public class kMeansPoint {


	//Value in dimension x
	private int x;


	//Value in dimension y
	private int y;


	//Assigned cluster
	private int clusterNumber;



	public kMeansPoint(int _x, int _y) {

		this.x = _x;
		this.y = _y;
		this.clusterNumber=0;
	}


	public void assignToCluster(int _clusterNumber) {

		this.clusterNumber = _clusterNumber;

	}


	public int getClusterNumber() {

		return this.clusterNumber;

	}


	public int getX() {

		return this.x;

	}



	public int getY() {

		return this.y;

	}



	public static double distance(kMeansPoint dp1, kMeansPoint dp2) {

		double result = 0;
		double resultX = dp1.getX() - dp2.getX();
		double resultY = dp1.getY() - dp2.getY();
		result = Math.sqrt(resultX*resultX + resultY*resultY);
		return result;

	}


    @Override
	public String toString(){

		return  this.y +" "+ this.clusterNumber ;

	}


	public static void main(String[] args) {

		kMeansPoint dp1 = new kMeansPoint(-3,-4);
		kMeansPoint dp2 = new kMeansPoint(0,4);
		System.out.println(kMeansPoint.distance(dp1, dp2));
		System.out.println(dp1.getX());
		System.out.println(dp2.getY());
		dp1.assignToCluster(7);
		System.out.println(dp1.getClusterNumber());
		dp1.assignToCluster(17);
		System.out.println(dp1.getClusterNumber());
		System.out.println(dp2.getClusterNumber());
		System.out.println(dp1);

	}


}