public class Centroid{
	private double[] dimensions;
	private int centroidNum;

	public Centroid(double[] dimensions, int centroidNum) {
		this.dimensions = dimensions;
		this.centroidNum = centroidNum;
	}

	double[] getDimensions(){
		return this.dimensions;
	}

	int getCentroidNum(){
		return this.centroidNum;
	}

	//Setters
	void setCoordinate(double coordinate, int index) {
		this.dimensions[index] = coordinate;
	}

}