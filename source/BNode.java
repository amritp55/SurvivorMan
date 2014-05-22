// BNODE (made previously)
// ======================
// I used this class to keep track of the high scores
// and allow the TreeSet to sort the scores according
// score first, and then name second

import java.util.*;
import java.io.*;

// This class was mainly used to compare the words alphabetically if their
// frequency are the same (therefore implementing Comparable)

class BNode implements Comparable{
	private String name;
	private int score;
	
	public BNode (String nam, int scr){
		name = nam;
		score = scr;
	}
	// here is the comparision
	public int compareTo (Object other){
		BNode comparing = (BNode) other;
		// if the score is the same, compare the names
		if (score == comparing.score){
			return name.compareTo (comparing.name);
		}
		// else use the score
		else{
			return (int) ((score-comparing.score));
		}
	}
	public int getScore(){
		return score;
	}

	public String toString (){
		return name;
	}
}