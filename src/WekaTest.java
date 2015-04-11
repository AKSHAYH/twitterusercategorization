import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.SMO;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;
 
public class WekaTest {
	static String results[];
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static Evaluation classify(Classifier model,
			Instances trainingSet, Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);
 
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
 
		return evaluation;
	}
 
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
 
		return 100 * correct / predictions.size();
	}
 
	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
 
		return split;
	}
 
	public void Modelbuild(String filename) throws Exception {
		BufferedReader datafile = readDataFile("TwitterUsers.arff");
		
		Instances data = new Instances(datafile);
		Random rnd = new Random();

		data.randomize(rnd);
		data.setClassIndex(data.numAttributes() - 1);
 
		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(data, 10);
 
		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
 
		// Use a set of classifiers
		Classifier[] models = { 
				new SMO(),
				new J48(), // a decision tree
				new PART(), 
				new DecisionTable(),//decision table majority classifier
				new DecisionStump() //one-level decision tree
		};
		String[] options = weka.core.Utils.splitOptions("-C 0.25 -M 2 -Q 1");
		for(int i=0;i<models[0].getOptions().length;++i)
			System.out.println(models[0].getOptions()[i]);
		// Run for each model
		for (int j = 0; j < 1; j++) {
 
			// Collect every group of predictions for current model in a FastVector
			FastVector predictions = new FastVector();
 
			// For each training-testing split pair, train and test the classifier
			for (int i = 0; i < trainingSplits.length; i++) {
				Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);
 
				predictions.appendElements(validation.predictions());
 
				// Uncomment to see the summary for each training-testing pair.
				// System.out.println(models[j].toString());
			}
 
			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);
 
			// Print current classifier's name and accuracy in a complicated,
			// but nice-looking way.
			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy)
					+ "\n---------------------------------");


			 Classifier cls = new SMO();
			 cls.buildClassifier(data);
			 // evaluate classifier and print some statistics
			 	results = new String [100];
				BufferedReader datafile2 = readDataFile(filename);

				Instances testdata = new Instances(datafile2);
				testdata.setClassIndex(testdata.numAttributes()-1);
				

				StringBuilder newusers = new StringBuilder();
				
				for(int i=0;;i++){
					
				double clsLabel = cls.classifyInstance(testdata.instance(i));
				Instances labeled = new Instances(testdata);
				labeled.instance(i).setClassValue(clsLabel);


				System.out.println(testdata.instance(i).toString());
				System.out.println(labeled.instance(i).stringValue(labeled.numAttributes()-1));
				results[i] = labeled.instance(i).stringValue(labeled.numAttributes()-1);
				int k=0;
				String[] wor = testdata.instance(i).toString().split(",");
				wor[wor.length-1] = results[i];
				//if testing or tracking a user
				for(k=0;k<wor.length-1;k++)
					newusers.append(wor[k]+" ");
					newusers.append(wor[k]+"\n");
				
				int count=0,l=0,flag=0,delt=0;

				if(testdata.instance(i)==testdata.lastInstance()){
					if(i==0)
						break;
					int[] hasher = new int[i+1];
					BufferedReader datafile3 = readDataFile("TwitterUsers.arff");
					StringBuilder lines = new StringBuilder();
					String line = new String();
					while((line=datafile3.readLine())!=null){
						flag=0;
						if(count>10013&&delt<=i){
						for(l=0;l<i+1;l++){
							if(hasher[l]==1)
								continue;
							if(line.contains(results[l])){
								hasher[l] = 1;
								delt++;
								flag=1;
								break;
							}
								
							
						}
						}
						if(flag==0)
						lines.append(line+"\n");
						count++;
					}
					datafile.close();
					datafile3.close();

					File file = new File("TwitterUsers.arff");
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(lines.toString());

					bw.write(newusers.toString());	
					bw.close();
					break;
				}
				}
				

		}
 
	}
}