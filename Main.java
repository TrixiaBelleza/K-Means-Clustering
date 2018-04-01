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
		while(count != k+1) {
			System.out.println("For Centroid" + count + ":");
			dimensions = new double[dimensionNum];
			
			for(int i = 0; i < dimensionNum; i++) {

				System.out.print("Enter dimension: ");
				dimensions[i] = sc.nextDouble();
			}
			Centroid newCent = new Centroid(dimensions);
			centroids.add(newCent);

			count++;
		}

		double summation = 0;
		double distance = 0;
		//for each data, for each centroid compute distance
		for(double[] trainDimensions : trainingDataMap.keySet()) {
			for(int i = 0; i < centroids.size(); i++) {
				summation = 0;
				for(int j = 0; j < dimensionNum; j++) {
					summation += (double) Math.pow((centroids.get(i).getDimensions()[j] - trainDimensions[j]), 2); 
				}

				System.out.println("Distance: " +  Math.sqrt(summation));
			}
			System.out.println();
		}

	}
}