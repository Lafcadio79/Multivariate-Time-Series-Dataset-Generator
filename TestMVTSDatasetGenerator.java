/*
 *
 * Class for testing the Multivariate Time Series Dataset Generator 
 *
 */


import java.util.Scanner;
import java.util.Vector;
import java.util.Random;
public class TestMVTSDatasetGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		
		// create the objects
		MVTSDatasetGenerator defdat = new MVTSDatasetGenerator();
		MVTSDatasetGenerator nodefdat = new MVTSDatasetGenerator(3,30000);
		MVTSDatasetGenerator df = new MVTSDatasetGenerator(12, 20000);
		
		// test 1 - print settings, modify and print again
		defdat.printParams();
		
		defdat.setFeatures(3);
		defdat.setSequenceLength(50);
		defdat.setAnomalies(2);
		defdat.setRange(new int[]{10,20});
		
		defdat.printParams();
		
		
		// test 2 - print settings, modify and print again
		nodefdat.printParams();
		
		nodefdat.setAnomalies(10);
		nodefdat.setRange(new int[] {20,50});
		
		nodefdat.printParams();
		
		// test 3 - print settings, modify and print again			
		df.printParams();

		df.setFeatures(5);
		df.setSequenceLength(580);
		df.setAnomalies(12);
		df.setRange(new int[]{20,30});

		df.printParams();

		// test 4 - generate the data set, print the data set, save the data set into a csv (output folder)
		Vector<Vector<Double>> dataset = df.generateDataset();
		
		System.out.println("Dataset");
		
		df.printDataset(dataset);
				
		df.saveDataset("dataset", dataset);
		
		in.close();
		
	}

}
