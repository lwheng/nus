//CS4248 Assignment 2
//HENG LOW WEE
//U096901R

import java.io.*;
import java.util.*;

public class build_tagger {

	/**
	 * @param args
	 */
	static String sents_train = "";
	static String sents_devt = "";
	static String model_file = "";
	static String[] pennTreeTags = { "CC", "CD", "DT", "EX", "FW", "IN", "JJ",
			"JJR", "JJS", "LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT", "POS",
			"PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB",
			"VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB", "$",
			"#", "``", "''", "-LRB-", "-RRB-", ",", ".", ":" };
	static String[] pennTreeTagsPlus = { "CC", "CD", "DT", "EX", "FW", "IN",
			"JJ", "JJR", "JJS", "LS", "MD", "NN", "NNS", "NNP", "NNPS", "PDT",
			"POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH",
			"VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB",
			"$", "#", "``", "''", "-LRB-", "-RRB-", ",", ".", ":", "<s>",
			"</s>" };
	// Last 2 are for sentence boundary
	static int[][] posTransitions = new int[pennTreeTagsPlus.length][pennTreeTagsPlus.length];
	static Hashtable<String, Integer> posTagToIndex = new Hashtable<String, Integer>();
	static Hashtable<Integer, String> indexToPosTag = new Hashtable<Integer, String>();
	static Set<String> vocab = new HashSet<String>();
	static Hashtable<String, Hashtable<String, Integer>> distribution = new Hashtable<String, Hashtable<String, Integer>>();
	static float[][] posTransitionsProba = new float[pennTreeTagsPlus.length][pennTreeTagsPlus.length];
	static Hashtable<String, Integer> tagCount = new Hashtable<String, Integer>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 3) {
			System.err
					.println("usage:\tjava build_tagger <sents.train> <sents.devt> <model_file>");
			System.exit(1);
		}
		// Init
		sents_train = args[0];
		sents_devt = args[1];
		model_file = args[2];

		for (int i = 0; i < posTransitions.length; i++) {
			for (int j = 0; j < posTransitions[i].length; j++) {
				posTransitions[i][j] = 0;
			}
		}

		for (int i = 0; i < pennTreeTagsPlus.length; i++) {
			posTagToIndex.put(pennTreeTagsPlus[i], i);
			indexToPosTag.put(i, pennTreeTagsPlus[i]);
		}

		try {
			FileInputStream fstream = new FileInputStream(sents_train);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String head, tail;
			String headWord, headTag, tailWord, tailTag;
			String[] tokens, temp;
			Hashtable<String, Integer> hashTemp;
			while ((strLine = br.readLine()) != null) {
				// Add sentence boundary tags around strLine
				strLine = "<s> " + strLine + " </s>";
				tokens = strLine.split(" ");

				// Proceed to extract info needed for POS Tagging
				for (int i = 0; i < tokens.length - 1; i++) {
					head = tokens[i];
					if (head.equals("<s>")) {
						headTag = head;
						headWord = "";
					} else {
						temp = head.split("/");
						headWord = "";
						for (int j = 0; j < temp.length - 1; j++) {
							headWord += temp[j];
						}
						vocab.add(headWord);
						headTag = temp[temp.length - 1];
					}

					tail = tokens[i + 1];
					if (tail.equals("</s>")) {
						tailTag = "</s>";
						tailWord = "";
					} else {
						temp = tail.split("/");
						tailWord = "";
						for (int j = 0; j < temp.length - 1; j++) {
							tailWord += temp[j];
						}
						vocab.add(tailWord);
						tailTag = temp[temp.length - 1];
					}

					// Enter this transition into the matrix
					posTransitions[posTagToIndex.get(headTag)][posTagToIndex
							.get(tailTag)] += 1;

					// Add into distribution
					if (!headTag.equals("<s>")) {
						if (!distribution.containsKey(headTag)) {
							hashTemp = new Hashtable<String, Integer>();
							distribution.put(headTag, hashTemp);
						}
						hashTemp = distribution.get(headTag);
						if (!hashTemp.containsKey(headWord)) {
							hashTemp.put(headWord, 0);
							distribution.put(headTag, hashTemp);
						}
						hashTemp = distribution.get(headTag);
						hashTemp.put(headWord, hashTemp.get(headWord) + 1);
						distribution.put(headTag, hashTemp);
					}

					// Add into tagCount
					if (!tagCount.containsKey(headTag)) {
						tagCount.put(headTag, 0);
					}
					tagCount.put(headTag, tagCount.get(headTag) + 1);
					if (tailTag.equals("</s>")) {
						if (!tagCount.containsKey(tailTag)) {
							tagCount.put(tailTag, 0);
						}
						tagCount.put(tailTag, tagCount.get(tailTag) + 1);
					}
				}
			}
			in.close();

			// Performing Witten-Bell smoothing now to optimize program
			float proba = 0;
			for (int i = 0; i < posTransitions.length; i++) {
				for (int j = 0; j < posTransitions[i].length; j++) {
					proba = wittenBell_tags(i, j);
					if (proba != Float.NaN) {
						posTransitionsProba[i][j] = proba;
					} else {
						posTransitionsProba[i][j] = (float) -1;
					}
				}
			}

			// Saving extracted info into model
			ObjectOutputStream outputStream = null;
			try {
				outputStream = new ObjectOutputStream(new FileOutputStream(
						model_file));
				Model model = new Model(pennTreeTags, pennTreeTagsPlus,
						posTransitions, posTagToIndex, indexToPosTag, vocab,
						distribution, posTransitionsProba, tagCount);
				outputStream.writeObject(model);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				// Close the ObjectOutputStream
				try {
					if (outputStream != null) {
						outputStream.flush();
						outputStream.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// To perform Witten-Bell smoothing and return the probability for the
	// transition from fromTag to toTag
	private static float wittenBell_tags(int fromTag, int toTag) {
		int check = posTransitions[fromTag][toTag];
		if (check > 0) {
			// ( C_tk1_tk ) / ( C_tk1 + T_tk1 )
			float C_tk1_tk = 0, C_tk1 = 0, T_tk1 = 0;
			C_tk1_tk = check;
			int value = 0;
			for (int i = 0; i < posTransitions[fromTag].length; i++) {
				value = posTransitions[fromTag][i];
				if (value > 0) {
					T_tk1 += 1;
				}
				C_tk1 += posTransitions[fromTag][i];
			}
			return (C_tk1_tk / (C_tk1 + T_tk1));
		} else {
			// ( T_tk1 ) / ( Z_tk1 ( C_tk1 + T_tk1 ))
			float T_tk1 = 0, Z_tk1 = 0, C_tk1 = 0;
			float value = 0;
			for (int i = 0; i < posTransitions[fromTag].length; i++) {
				value = posTransitions[fromTag][i];
				if (value > 0) {
					C_tk1 += value;
					T_tk1 += 1;
				} else {
					Z_tk1 += 1;
				}
			}
			return (T_tk1) / (Z_tk1 * (C_tk1 + T_tk1));
		}
	}

	// To perform Witten-Bell smoothing and return the probability for an
	// observation given a tag
	private static float wittenBell_words(String observation, String tag) {
		Hashtable<String, Integer> hashTemp = distribution.get(tag);
		if (hashTemp.containsKey(observation)) {
			// C_to / ( C_t + T_t )
			float C_to = 0, C_t = 0, T_t = 0;
			C_to = hashTemp.get(observation);
			for (String o : hashTemp.keySet()) {
				C_t += hashTemp.get(o);
				T_t += 1;
			}
			return C_to / (C_t + T_t);
		} else {
			// T_t / ( Z_t ( C_t + T_t ) )
			float T_t = 0, Z_t = 0, C_t = 0;
			for (String o : hashTemp.keySet()) {
				C_t += hashTemp.get(o);
			}
			Set<String> seen = hashTemp.keySet();
			Set<String> unseen = new HashSet<String>();
			unseen.addAll(vocab);
			unseen.removeAll(seen);
			T_t = seen.size();
			Z_t = unseen.size();
			return T_t / (Z_t * (C_t + T_t));
		}
	}
}
