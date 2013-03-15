//CS4248 Assignment 2
//HENG LOW WEE
//U096901R

import java.io.Serializable;
import java.util.*;

public class Model implements Serializable {
	private String[] pennTreeTags;
	private String[] pennTreeTagsPlus;
	private int[][] posTransitions;
	private Hashtable<String, Integer> posTagsToIndex;
	private Hashtable<Integer, String> indexToPosTags;
	private Set<String> vocab;
	private Hashtable<String, Hashtable<String, Integer>> distribution;
	private float[][] posTransitionsProba;
	private Hashtable<String, Integer> tagCount;

	public Model(String[] pennTreeTags, String[] pennTreeTagsPlus,
			int[][] posTransitions, Hashtable<String, Integer> posTagsToIndex,
			Hashtable<Integer, String> indexToPosTags, Set<String> vocab,
			Hashtable<String, Hashtable<String, Integer>> distribution,
			float[][] posTransitionsProba, Hashtable<String, Integer> tagCount) {
		this.pennTreeTags = pennTreeTags;
		this.pennTreeTagsPlus = pennTreeTagsPlus;
		this.posTransitions = posTransitions;
		this.posTagsToIndex = posTagsToIndex;
		this.indexToPosTags = indexToPosTags;
		this.vocab = vocab;
		this.distribution = distribution;
		this.posTransitionsProba = posTransitionsProba;
		this.tagCount = tagCount;
	}

	public String[] getPennTreeTags() {
		return this.pennTreeTags;
	}

	public String[] getPennTreeTagsPlus() {
		return this.pennTreeTagsPlus;
	}

	public int[][] getPosTransitions() {
		return this.posTransitions;
	}

	public Hashtable<String, Integer> getPosTagsToIndex() {
		return this.posTagsToIndex;
	}

	public Hashtable<Integer, String> getIndexToPosTags() {
		return this.indexToPosTags;
	}

	public Set<String> getVocab() {
		return this.vocab;
	}

	public Hashtable<String, Hashtable<String, Integer>> getDistribution() {
		return this.distribution;
	}

	public float[][] getPosTransitionsProba() {
		return this.posTransitionsProba;
	}
	
	public Hashtable<String, Integer> getTagCount() {
		return this.tagCount;
	}
}
