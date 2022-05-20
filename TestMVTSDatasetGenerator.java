import java.util.Scanner;
import java.util.Vector;
import java.util.Random;
public class TestMVTSDatasetGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		
		MVTSDatasetGenerator defdat = new MVTSDatasetGenerator();
		
		defdat.printParams();
		
		defdat.setFeatures(3);
		defdat.setSequenceLength(50);
		defdat.setAnomalies(2);
		defdat.setRange(new int[]{10,20});
		
		defdat.printParams();
		
		MVTSDatasetGenerator nodefdat = new MVTSDatasetGenerator(3,30000);
		
		nodefdat.printParams();
		
		nodefdat.setAnomalies(10);
		nodefdat.setRange(new int[] {20,50});
		
		nodefdat.printParams();
		
		MVTSDatasetGenerator df = new MVTSDatasetGenerator(12, 20000);
				
		df.printParams();

		df.setFeatures(5);
		df.setSequenceLength(580);
		df.setAnomalies(12);
		df.setRange(new int[]{20,30});

		df.printParams();
				
		Vector<Vector<Double>> dataset = df.generateDataset();
		
		System.out.println("Dataset");
		
		df.printDataset(dataset);
				
		df.saveDataset("dataset", dataset);
		
		in.close();
		
	}

}
