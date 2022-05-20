  /**
   * 
   * @author   	Dimitri Belli
   * @contact  	dimitri.belli@isti.cnr.it
   *
   * @created   May 2022, 20
   * @version	00010L
   * 
   * Copyright C 2022, Free Software Foundation, Inc. <https://fsf.org/>
   * Everyone is permitted to copy and distribute verbatim copies
   * of this license document, but changing it is not allowed.
   *
   * This program is free software: you can redistribute it and/or modify
   * it under the terms of the GNU General Public License as published by
   * the Free Software Foundation, either version 3 of the License, or
   * (at your option) any later version.
   *
   * This program is distributed in the hope that it will be useful,
   * but WITHOUT ANY WARRANTY; without even the implied warranty of
   * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   * GNU General Public License for more details.
   *
   * You should have received a copy of the GNU General Public License
   * along with this program.  If not, see <http://www.gnu.org/licenses/>.
   * 
   * 
   * @description: 	The class provides constructors and methods to generate
   * 				synthetic data sets of multi-variate time series 
   * 				with/without anomalies.
   * 				The random class is used to introduce the right percentage 
   * 				of aleatority to the generation of the signals.
   * 				Temporal patterns have been modeled based on trigonometric 
   * 				functions, randomly selected feature by feature.
   * 				In order to reproduce the anomalies, a little noise is added
   * 				to the synthetic signals generated.
   *  
   */

  import java.io.*;
  import java.util.Arrays;
  import java.util.Random;
  import java.util.Vector;
  import java.lang.IllegalArgumentException;
  import java.nio.file.Files;
  import java.nio.file.Paths;
  import java.nio.file.StandardCopyOption;
  public class MVTSDatasetGenerator{

	/**
	 * 
	 * private global variables
	 * 
  	 * 	@param features: 		(int) number of channels/features of the data set (default value 30)
  	 *  @param sequenceLength:  (int) number of temporal observations per channel/feature (default value 20000)
  	 *  @param anomalies:		(int) number of anomalies to be added to the data set (default value: 0)
  	 *  @param range:			(int[2]) duration range of each anomaly [min, max] (default value: {30, 90})
  	 *  
	 */
	private int features = 30;
  	private int sequenceLength = 20000;
  	private int anomalies = -1; 
  	private int[] range = new int[] {30, 90} ;

  	
  	static final long serialVersionUID = 00010L;
  	
  	
  	/**
  	 * 
  	 *  constructor for data sets with anomalies
  	 * 
  	 * 	@param features: 		(int) number of channels/features of the data set 			(default value: 30)
  	 *  @param sequenceLength: 	(int) number of temporal observations per channel/feature 	(default value: 20000)
  	 *  @param anomalies:		(int) number of anomalies to be added to the data set 		(default value: 0)
  	 *  @param range:			(int[2]) duration range of each anomaly [min, max] 			(default value: {30, 90})
  	 *  
  	 */
  	public MVTSDatasetGenerator(int features, int sequenceLength, int anomalies, int[] range) {
  		this(features, sequenceLength);
  		this.anomalies = anomalies;
  		this.range = range;
  	}
  	
  	/**
  	 * 
  	 *  constructor for data sets with no anomaly
  	 * 
  	 * 	@param features: 		(int) number of channels/features of the data set 			(default value 30)
  	 *  @param sequenceLength: 	(int) number of temporal observations per channel/feature 	(default value 20000)
  	 * 
  	 */
  	//
  	public MVTSDatasetGenerator(int features, int sequenceLength) {
  		this.features = features;
  		this.sequenceLength = sequenceLength;
  	}
  	
  	/**
  	 * 
  	 *  default data set constructor
  	 * 
  	 */
  	public MVTSDatasetGenerator() {}
  	
  	/**
  	 * 
  	 *  accessor method
  	 *  
  	 *  @return the number of features (int) 
  	 */
  	public int getFeatures() {
  		return features;
  	}

  	/**
  	 * 
  	 *  accessor method
  	 *  
  	 *  @return the length of the sequence (int) 
  	 *  
  	 */
  	public int getSequenceLength() {
  		return sequenceLength;
  	}
  	
  	/**
  	 * 
  	 *  accessor method
  	 *  
  	 *  @return the number of anomalies (int) 
  	 *  
  	 */
  	public int getAnomalies() {
  		return anomalies;
  	}
  	
  	/**
  	 * 
  	 *  accessor method
  	 *  
  	 *  @return the range of each anomaly (int[2])
  	 *  
  	 */
  	public int[] getRange() {
  		return range;
  	}

  	/**
  	 * 
  	 *  accessor method that allows to set the number of features
  	 *  
  	 *  @param features: (int) number of channels/features
  	 *  
  	 */
  	public void setFeatures(int features) {
  		this.features = features;
  	}

  	/**
  	 * 
  	 *  accessor method that allows to set the length of the sequences
  	 *  
  	 *  @param sequenceLength: (int) sequence length
  	 *  
  	 */
  	public void setSequenceLength(int sequenceLength) {
  		this.sequenceLength = sequenceLength;
  	}
  	
  	/**
  	 * 
  	 *  accessor method that allows to set the number of anomalies
  	 *  
  	 *  @param anomalies: (int) number of anomalies
  	 *  
  	 */
  	public void setAnomalies(int anomalies) {
  		this.anomalies = anomalies;
  	}

  	/**
  	 * 
  	 *  accessor method that allows to set the duration of the anomalies
  	 *  
  	 *  @param range: (int[2]) duration of the anomalies
  	 *  
  	 */
  	public void setRange(int[] range) {
  		this.range = range;	
  	}
  		
  	/**
  	 * 
  	 *  method that allows to generate a single time series
  	 *  
  	 *  the time series is randomly generated on the basis of a coin toss strategy 
  	 *  that leverages the 0/1 variable sRand: 
  	 *  	- when sRand is 0 the time series is computed as sin[(t-t0)/w] + lambda * epsilon
  	 *		- when sRand is 1 the time series is computed as cos[(t-t0)/w] + lambda * epsilon
  	 *  
  	 *  @return the time series (Vector of Double) 
  	 *  
  	 */
  	public Vector<Double> generateSingleTimeSeries(){
  		
  		// for aleatority
  		Random rd = new Random();

  		// store the synthetic temporal pattern
  		Vector<Double> series = new Vector<Double>();
  		
  		// 0/1 coin
  		int sRand = rd.nextInt(2);
  		
  		// time delay (random value within the range [50, 100])
  		int t0 = rd.nextInt(50) + 50;
  		// frequency (random value within the range [40, 50])
  		double w =  rd.nextDouble() * 10 + 40;
  		// pseudorandom gaussian noise
  		double epsilon = rd.nextGaussian();
  		// scaling factor (abs)
  		double lambda = 0.3;
  		// scaled random gaussian noise 
  		double srgn = lambda * epsilon;
  		// variable to store the periodic cycle
  		double pc;
  		
  		// formula: 
  		// when sRand is 0 -> sin[(t-t0)/w] + lambda * epsilon
  		// when sRand is 1 -> cos[(t-t0)/w] + lambda * epsilon
  		 if(sRand != 1){
  			 for(int t = 0; t < this.sequenceLength; t++){
  				 pc = (t - t0) / w;
  				 series.add(Math.sin(pc) + srgn);  
  			 }
  		 } else {
  			 for(int t = 0; t < this.sequenceLength; t++){
  				 pc = (t - t0) / w;
  				 series.add(Math.cos(pc) + srgn);
  			 }
  		 }
		 
  		 return series;
  	}
  	
  	/**
  	 * 
  	 *  method that allows to generate the multi-variate time series data set
  	 *  
  	 *  the method also checks if it is required the introductio of anomalies
  	 *  and randomly adds them to the series accordingly to the settings of
  	 *  the constructor
  	 *  
  	 *  @return the multivariate time series data set (Vector of Vectors of Double) 
  	 *  
  	 */
  	public Vector<Vector<Double>> generateDataset(){
  		  		
  		// store the data set
  		Vector<Vector<Double>> dataset = new Vector<Vector<Double>>();
  		
  		// fill the dataset
  		for(int i = 0; i < this.features; i++)
  			dataset.add(this.generateSingleTimeSeries());

 		 // check whether anomalies should be added
 		 // note that since the anomaly insertion process is randomized, 
  		 // anomalies can ovelap with each other from series to series
 		 if(this.anomalies != 0) {
 			 
 	  		// for aleatority 
 	  		Random rd = new Random();

 			// get the number of anomalies to be added to the dataset 
 			int temp = this.anomalies;
 			// get the duration of the anomaly
 			int anomalyDuration =  this.range[1] - this.range[0]; 
 			// add a little perturbation to the signal
 	  		double noise;
 	  		// randomly add into the dataset as many anomalies as specified
 	  		
  			while(temp > 0){
  				try {
  					// note that the size of the series should be at least greater than the range interval
  					// otherwise the program raises an exception (to be managed)

  					// pseudorandomly select a series
  					int series = rd.nextInt(dataset.size());
  					// pseudorandomly select a starting position in the series
  					int position = rd.nextInt(dataset.get(series).size() - anomalyDuration);
  					
  					// compute the anomaly as (R1-R2)/(R3+R4)
  					for (int i = 0; i < anomalyDuration; i++) {
  						noise = (rd.nextDouble() - rd.nextDouble()) / (rd.nextDouble() + rd.nextDouble());
  						dataset.get(series).set(position + i, dataset.get(series).get(position + i) + noise);
  					}
  					
  					temp--;
  					
  				} catch(IllegalArgumentException e) {
  					System.out.println("Illega arguent exception: " + e);
  					System.out.println("The size of the series should be at least greater than the range interval.");
  					System.out.println("No anomaly added to the dataset.");
  					// exit from the loop
  					temp = -1;
  				}
  			}
 		 }
 		 
  		return dataset;
  	}
  	
 	/**
  	 * 
  	 *  method that allows to print the information about the constructor's settings
  	 *  
  	 */
   	public void printParams(){
  		System.out.println("Features:\t\t" + this.features);
  		System.out.println("Sequence length:\t" + this.sequenceLength);
  		System.out.println("Anomalies:\t\t" + this.anomalies);
  		System.out.println("Range:\t\t\t" + Arrays.toString(this.range));
  		System.out.println("--------------------------------");
  	}	
  	
 	/**
  	 * 
  	 *  method that allows to print the data set content
  	 *  
  	 */
   	public void printDataset(Vector<Vector<Double>> dataset){
  		for(Vector<Double> series : dataset){
  			for(double ser : series)
  				System.out.print(ser + " ");
  			System.out.println();
  		}
  	}
  	
 	/**
  	 * 
  	 *  method that allows to save the data set into a csv file
  	 *  
  	 *  @param filename (String) the name of the dataset without extension
  	 *  @param dataset  ((Vector of Vectors of Double)) the data set generated 
  	 *  
  	 */
  	public void saveDataset(String filename, Vector<Vector<Double>> dataset) {
  		// check if there exists the output directory in the current position
  		// if no, it creates one
  		File output = new File("output");
  		if(!output.exists())
  			output.mkdir();
  		try {
  			PrintWriter fileout = 
  					new PrintWriter(new BufferedWriter(
  							new FileWriter(filename + ".csv")));
  			for(Vector<Double> series : dataset) {
  				for(int i = 0; i < series.size(); i++)
  					if(i != series.size()-1)
  						fileout.print(series.get(i) + ",");
  					else
  						fileout.print(series.get(i));
  					fileout.println();
  			}
  			fileout.close();
  			// move the data set into the new directory
  	  		Files.move(Paths.get(filename + ".csv").toAbsolutePath(), Paths.get("output/" + filename + ".csv"), StandardCopyOption.REPLACE_EXISTING);
  		} catch(IOException e) {
  			System.out.println("IOException: " + e);
  		}

  	}
  }