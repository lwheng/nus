//CS4248 Assignment 2
//HENG LOW WEE
//U096901R

import java.io.*;
import java.util.*;

public class run_tagger {

	/**
	 * @param args
	 */
	static String sents_test = "";
	static String model_file = "";
	static String sents_out = "";

	static String[] pennTreeTags;
	static String[] pennTreeTagsPlus;
	static int posTransitions[][];
	static Hashtable<String, Integer> posTagsToIndex;
	static Hashtable<Integer, String> indexToPosTags;
	static Set<String> vocab;
	static Hashtable<String, Hashtable<String, Integer>> distribution;
	static float[][] posTransitionsProba;
	static Hashtable<String, Integer> tagCount;
	static int[][] results;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1) {
			System.err
					.println("usage:\tjava run_tagger <sents.test> <model_file> <sents.out>");
			System.exit(1);
		}

		sents_test = args[0];
		model_file = args[1];
		sents_out = args[2];

		try {
			// Load model_file
			ObjectInputStream inputStream = null;
			inputStream = new ObjectInputStream(new FileInputStream(model_file));
			Object obj = null;
			obj = inputStream.readObject();
			Model model = null;
			if (obj instanceof Model) {
				model = (Model) obj;
				pennTreeTags = model.getPennTreeTags();
				// pennTreeTagsPlus = model.getPennTreeTagsPlus();
				// posTransitions = model.getPosTransitions();
				posTagsToIndex = model.getPosTagsToIndex();
				indexToPosTags = model.getIndexToPosTags();
				vocab = model.getVocab();
				distribution = model.getDistribution();
				posTransitionsProba = model.getPosTransitionsProba();
				tagCount = model.getTagCount();
				results = new int[pennTreeTags.length][pennTreeTags.length];
			}

			// Init results for Precision/Recall/F-score
			for (int i = 0; i < results.length; i++) {
				for (int j = 0; j < results[i].length; j++) {
					results[i][j] = 0;
				}
			}

			// Open sents.test file and run tagger on sentences
			FileInputStream fstream = new FileInputStream(sents_test);
			FileWriter outstream = new FileWriter(sents_out);
			BufferedWriter out = new BufferedWriter(outstream);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String tagged, untagged, prediction;
			String[] observations, correctTags, predictedTags;
			String c, p;
			boolean taggedTest = false;
			while ((tagged = br.readLine()) != null) {
				untagged = stripTrainingInstance(tagged);
				observations = untagged.split(" ");
				predictedTags = viterbi(observations, pennTreeTags);
				prediction = outputPrinter(observations, predictedTags);
				out.write(prediction);

				// Add to results only if tagged; For Precision/Recall/F-score
				if (isTagged(tagged)) {
					taggedTest = true;
					correctTags = getTags(tagged);
					// Now we have correctTags and predictedTags
					for (int i = 0; i < correctTags.length; i++) {
						c = correctTags[i];
						p = predictedTags[i];
						results[posTagsToIndex.get(p)][posTagsToIndex.get(c)] += 1;
					}
				}
			}

			// For Precision/Recall/F-score
			if (taggedTest) {
				float tp = 0, horizontal = 0, vertical = 0;
				float precision, recall, f;
				float precisionSum = 0, recallSum = 0, fSum = 0;
				float precisionN = 0, recallN = 0, fN = 0;
				for (int s = 0; s < pennTreeTags.length; s++) {
					horizontal = (float) 0;
					vertical = (float) 0;
					tp = (float) results[s][s];
					for (int j = 0; j < results[s].length; j++) {
						horizontal += (float) results[s][j];
					}
					for (int j = 0; j < results.length; j++) {
						vertical += (float) results[j][s];
					}
					precision = tp / horizontal;
					recall = tp / vertical;
					f = 2 * ((precision * recall) / (precision + recall));
					if (!Float.isNaN(precision)) {
						precisionSum += precision;
						precisionN += 1;
					}
					if (!Float.isNaN(recall)) {
						recallSum += recall;
						recallN += 1;
					}
					if (!Float.isNaN(f)) {
						fSum += f;
						fN += 1;
					}
					System.out.println(indexToPosTags.get(s) + "\t" + precision
							+ "\t" + recall + "\t" + f);
				}
				System.out.println("\t" + precisionSum / precisionN + "\t"
						+ recallSum / recallN + "\t" + fSum / fN);
			}

			// Close the input stream
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// The Viterbi algorithm. Returns the predicted tags
	private static String[] viterbi(String[] observations, String[] stateGraph) {
		int T = observations.length; // # of words in sentence
		int N = stateGraph.length; // # of POS Tags
		float[][] viterbi = new float[N][T];
		int[][] backpointer = new int[N][T];

		// Init
		for (int s = 0; s < N; s++) {
			viterbi[s][0] = (float) (Math
					.log(posTransitionsProba[posTagsToIndex.get("<s>")][s]) + Math
					.log(wittenBell_words(observations[0], stateGraph[s])));

			backpointer[s][0] = 0; // simply points back to <s>
		}

		// Recursive
		float max, temp;
		int back;
		for (int t = 1; t < T; t++) {
			for (int s = 0; s < N; s++) {
				// max = -1;
				max = -Float.MAX_VALUE;
				temp = 0;
				back = 0;
				for (int sPrime = 0; sPrime < N; sPrime++) {
					// temp = viterbi[sPrime][t - 1]
					// * posTransitionsProba[sPrime][s]
					// * wittenBell_words(observations[t],
					// stateGraph[s]);
					temp = viterbi[sPrime][t - 1]
							+ (float) (Math.log(posTransitionsProba[sPrime][s]) + Math
									.log(wittenBell_words(observations[t],
											stateGraph[s])));
					if (temp > max) {
						max = temp;
						back = sPrime;
					}
				}
				viterbi[s][t] = max;
				backpointer[s][t] = back;
			}
		}

		// Termination
		float last;
		int lastpoint = 0;
		max = -1;
		max = -Float.MAX_VALUE;
		temp = 0;
		for (int s = 0; s < N; s++) {
			// temp = viterbi[s][T - 1]
			// * wittenBell_tags(s, posTagsToIndex.get("</s>"));
			temp = viterbi[s][T - 1]
					+ (float) Math.log(posTransitionsProba[s][posTagsToIndex
							.get("</s>")]);
			if (temp > max) {
				max = temp;
				lastpoint = s;
			}
		}
		last = max;

		String[] predictedTags = new String[T];
		for (int t = T - 1; t >= 0; t--) {
			predictedTags[t] = stateGraph[lastpoint];
			lastpoint = backpointer[lastpoint][t];
		}

		// return backtrace path
		return predictedTags;
	}

	// To perform Witten-Bell smoothing and return the probability for an
	// observation given a tag
	private static float wittenBell_words(String observation, String tag) {
		Hashtable<String, Integer> hashTemp = distribution.get(tag);
		if (hashTemp.containsKey(observation)) {
			// C_to / ( C_t + T_t )
			float C_to = 0, C_t = 0, T_t = 0;
			C_to = hashTemp.get(observation);
			// for (String o : hashTemp.keySet()) {
			// C_t += hashTemp.get(o);
			// }
			C_t = tagCount.get(tag);
			T_t = hashTemp.keySet().size();
			return C_to / (C_t + T_t);
		} else {
			// T_t / ( Z_t ( C_t + T_t ) )
			float T_t = 0, Z_t = 0, C_t = 0;
			// for (String o : hashTemp.keySet()) {
			// C_t += hashTemp.get(o);
			// }
			C_t = tagCount.get(tag);
			Set<String> seen = hashTemp.keySet();
			T_t = seen.size();
			Z_t = vocab.size() - seen.size();
			return T_t / (Z_t * (C_t + T_t));
		}
	}

	private static String outputPrinter(String[] observations, String[] tags) {
		String output = "";
		for (int i = 0; i < observations.length; i++) {
			output += observations[i] + "/" + tags[i] + " ";
		}
		return output.trim() + "\n";
	}

	private static String stripTrainingInstance(String instance) {
		String output = "";
		String[] tokens = instance.split(" ");
		boolean tagged = true;
		// First check if sentence is tagged
		for (String t : tokens) {
			if (!t.contains("/")) {
				tagged = false;
				break;
			}
		}
		if (!tagged) {
			return instance;
		} else {
			String[] temp;
			for (String t : tokens) {
				temp = t.split("/");
				for (int i = 0; i < temp.length - 1; i++) {
					output += temp[i] + " ";
				}
			}
			return output.trim();
		}
	}

	private static boolean isTagged(String instance) {
		String[] tokens = instance.split(" ");
		// First check if sentence is tagged
		for (String t : tokens) {
			if (!t.contains("/")) {
				return false;
			}
		}
		return true;
	}

	private static String[] getTags(String tagged) {
		String[] tokens = tagged.split(" ");
		String[] output = new String[tokens.length];
		String[] temp;
		String tag, t;
		for (int i = 0; i < tokens.length; i++) {
			t = tokens[i];
			temp = t.split("/");
			tag = temp[temp.length - 1];
			output[i] = tag;
		}
		return output;
	}
}
