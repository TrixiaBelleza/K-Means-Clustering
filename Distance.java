public class Distance {
	private double distance;
	private int centroidNum;
	public Distance(double distance, int centroidNum) {
		this.distance = distance;
		this.centroidNum = centroidNum;
	}

	//getters
	double getDistance() {
		return this.distance;
	}
	int getCentroidNum() {
		return this.centroidNum;
	}
}