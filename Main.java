import java.util.*;
import java.io.*;

public class Main {
	public static void main (String [] args) throws FileNotFoundException{
		System.out.print("Enter filename: ");
		Scanner sc = new Scanner(System.in);
		String filename = sc.next();

		//Read file
		Scanner inputScan = new Scanner(new File(filename));

		String line;
		String[] tokens;
		double[] dimensions;

		int k = Integer.parseInt(inputScan.nextLine());
		LinkedHashMap<double[], Integer> trainingDataMap = new LinkedHashMap<double[], Integer>();
		int dimensionNum = 0;

		//for each line ng training data
		while(inputScan.hasNext()) {
			line = inputScan.nextLine();
			tokens = line.split(" ");
			dimensionNum = tokens.length;
			dimensions = new double[tokens.length];

			//for each token
			for(int i = 0; i < tokens.length; i++) {
				dimensions[i] = Double.parseDouble(tokens[i]);
			}
			trainingDataMap.put(dimensions, 0); //initially, wala pang attached centroid 
		}

		//get centroids 
		ArrayList<Centroid> centroids = new ArrayList<Centroid>();
		int count = 1;
		int index = 0;
		while(count != k+1) {
			// System.out.println("Centroid " + count);
			Random rand = new Random();

			//get index (kung saan nakaposition sa file.)
			index = rand.nextInt(trainingDataMap.size()) + 1;  //+1 kasi index 0 sa input.txt is value of k
		
			dimensions = getCentroid(index, filename);
			
			Centroid newCent = new Centroid(dimensions, count);
			centroids.add(newCent);
			count++;
		}	

		boolean shouldStop = false;
		int a = 0;
		while(a <2) {
			printCentroid(centroids);
			double summation = 0;
			double distance = 0;
			ArrayList<Distance> distances = new ArrayList<Distance>();
		
			int nearestCent = 0;
			//for each data, for each centroid compute distance
			for(double[] trainDimensions : trainingDataMap.keySet()) {

				for(int i = 0; i < centroids.size(); i++) {
					// System.out.println("Distance from Centroid " + centroids.get(i).getCentroidNum());
					summation = 0;
					for(int j = 0; j < dimensionNum; j++) {
						summation += (double) Math.pow((centroids.get(i).getDimensions()[j] - trainDimensions[j]), 2); 
					}

					// System.out.println("Distance: " +  Math.sqrt(summation));
					Distance dist = new Distance(Math.sqrt(summation), centroids.get(i).getCentroidNum());
					distances.add(dist);

				}

				nearestCent = getNearestCentroid(distances);
				System.out.println("nearest cent: " + nearestCent);
				trainingDataMap.put(trainDimensions, nearestCent); //update trainingDataMap's centroid
				
				// System.out.println();
			}

			ArrayList<Centroid> updatedCents = updateCentroid(centroids, trainingDataMap, dimensionNum);

			// check if each centroid have same dimensions from prev
			shouldStop = isEqual(centroids, updatedCents, dimensionNum);
			if(!shouldStop) {
				System.out.println("Ent");
				centroids = updatedCents; //palitan yung value ng prev centroids to updated ones
				System.out.println("dimension[0] of centroid1: " + centroids.get(0).getDimensions()[0]);
			}
			a++;
		}

	}

	public static void printCentroid(ArrayList<Centroid> centroids){
		for(int i = 0; i < centroids.size(); i++) {
			System.out.println("Centroid " + centroids.get(i).getCentroidNum());
			for(int j = 0; j < centroids.get(i).getDimensions().length; j++) {
				System.out.println(centroids.get(i).getDimensions()[j]);
			}
		}
	}

	//check if each centroid have same dimensions from prev
	public static boolean isEqual(ArrayList<Centroid> prevCent, ArrayList<Centroid> currCent, int dimensionNum) {
		for(int i = 0; i < prevCent.size(); i++) {
			for(int j = 0; j < currCent.size(); j++) {
				if(prevCent.get(i).getCentroidNum() == currCent.get(j).getCentroidNum()) { 	//if same centroid num 
					for(int k = 0; k < dimensionNum; k++) {
						if(prevCent.get(i).getDimensions()[k] != currCent.get(j).getDimensions()[k]) return false;
					}																				
				}
			}
		}
		return true;
	}	

	//get updated centroid
	public static ArrayList<Centroid> updateCentroid(ArrayList<Centroid> centroids, LinkedHashMap<double[], Integer> trainingDataMap, int dimensionNum) {
		double newCoordinate = 0;
		int centroidCount = 1;
		double[] newDimensions = new double[dimensionNum];
		ArrayList<Centroid> updatedCents = new ArrayList<Centroid>();

		for(int i = 0; i < centroids.size(); i++) {				//iterate each centroid
			System.out.println("Centroid num: " + centroids.get(i).getCentroidNum());
			for(int j = 0; j < dimensionNum; j++) {				//iterate each dimension. J serves as dimension index

				newDimensions[j] = getCoordinate(centroids.get(i).getCentroidNum(), trainingDataMap, j); 		
				System.out.println("newDimensions : " + newDimensions[j]);
			}
			Centroid newCent = new Centroid(newDimensions, centroidCount);
			updatedCents.add(newCent);
			centroidCount++;
		}
		return updatedCents;
	}

	//get a coordinate for centroid 
	public static double getCoordinate(int centroidNum, LinkedHashMap<double[], Integer> trainingDataMap, int dimensionIndex) {
		double summation = 0;
		int matchedCount = 0; //counts ilan yung nagmatch na same centroidNum

		for(double[] trainDimensions : trainingDataMap.keySet()) { //iterates the key 

			if(trainingDataMap.get(trainDimensions) == centroidNum) {  //gets the value of the current key and checks if equal sa centroid num
				System.out.println("trainDimensions[dimensionIndex]: " + trainDimensions[dimensionIndex]);
				summation += trainDimensions[dimensionIndex];
				matchedCount++; 
			} 
		}
		double result = summation/(double)matchedCount;
		// System.out.println("coordinate: " + result);
		return summation/matchedCount;

	}

	//get the least distance then return its centroidnum
	public static int getNearestCentroid(ArrayList<Distance> distances){
		double least = distances.get(0).getDistance();
		int nearestCent = 0;

		for(int i = 0; i < distances.size(); i++) {
			if(distances.get(i).getDistance() < least) {
				least = distances.get(i).getDistance();
				nearestCent = distances.get(i).getCentroidNum();
			}
		}

		return nearestCent;
	}

	public static double[] getCentroid(int index, String filename) throws FileNotFoundException{
		Scanner inputScan = new Scanner(new File(filename));
		int count = 0;
		double[] dimensions;
		String line;
		String[] tokens;
		int dimensionNum;
		while(inputScan.hasNext()) {
			
			line = inputScan.nextLine();

			if(count == index){ 
				
				//get dimensions
				tokens = line.split(" ");
				dimensionNum = tokens.length;
				dimensions = new double[tokens.length];

				//for each token
				for(int i = 0; i < tokens.length; i++) {
					dimensions[i] = Double.parseDouble(tokens[i]);
				}
				return dimensions;
			}
			count++;
		}
		return null;
	}
}