package oPL;

import java.util.*;

public class RecordValue extends Hashtable<String,Integer> implements Value{

	private static final long serialVersionUID = 1L;

	public String toString() {
	    String s="[";
	    Enumeration<String> ks = keys();
	    while (ks.hasMoreElements()) {
		   String k = ks.nextElement();
		   s = s + k + " : "+Store.theStore.get(get(k))+", ";
		   if (ks.hasMoreElements()) 
			   s = s + ",";
	    }
	    return s + "]";
	}
}