CS4248 Assignment 2 - POS Tagger
================================
U096901R HENG LOW WEE

Source files:  
1. `build_tagger.java`:  
    Compile by running `javac build_tagger.java`  
    Usage: `java build_tagger <sents.train> <sents.devt> <model_file>`  
    Reads in training instances from `sents.train` and extract information needed for a HMM/Viterbi based POS tagger  
    Creates a Model, and saves it into a serialised object `model_file`  
2. `run_tagger.java`:  
    Compile by running `javac run_tagger.java`  
    Usage : `java run_tagger <sents.test> <model_file> <sents.out>`  
    Reads in serialised object from `model_file` to obtain our precompiled model for POS tagging  
    Reads in test instances from `sents.test` and performs POS tagging line by line. If test instances are already, program writes to stdout the summary of Precision/Recall/F-score values for each POS tag  
    Implements Viterbi algorithm, together with Witten-Bell smoothing to perform POS tagging  
    Writes test instances, tagged with predicted tags, into `sents.out`  
3. `Model.java`:  
    Compile by running `javac Model.java`  
    The class that will capture the essential information for the POS tagger  
    Will be serialised into `model_file` as mentioned above
