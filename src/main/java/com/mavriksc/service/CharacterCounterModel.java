package com.mavriksc.service;

import java.util.ArrayList;
import java.util.List;

public class CharacterCounterModel{
	private List<String> characters;
	private int charCount;
	private float[][] counterMatrix;

	public CharacterCounterModel(){
		//init the char list and counter matrix
		characters = new ArrayList<String>();
		//get char from csv
		charCount = characters.size();
		counterMatrix= new float [charCount][charCount];
		//fill matrix
	}

	public List<String> bestSwitchForPlayer(List<String> playerTeam,List<String> otherTeam){
		return null;
	}

	public List<String> bestSingleSwitch(List<String> playerTeam,List<String> otherTeam){
		return null;
	}

	public List<String> bestOverAllComp(List<String> playerTeam,List<String> otherTeam){
		return null;
	}
	public int indexForCharacter(String c){
		return characters.indexOf(c);
	}
	
	public float[][] otherTeamMatrix(List<String> t){
		float[][] m = new float[6][charCount];
		for (int i = 0; i < t.size(); i++) {
			m[i] = getCol(counterMatrix,i);
		}
		return m; 
	}
	
	public float[] getCol(float[][] m, int colNum){
		float[] c = new float[charCount];
		for (int i = 0; i < m.length; i++) {
			c[i] = m[i][colNum];
		}
		return c;
	}



}