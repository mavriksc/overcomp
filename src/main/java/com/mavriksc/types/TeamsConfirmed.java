package com.mavriksc.types;

import java.util.ArrayList;
import java.util.List;

public class TeamsConfirmed {
	private List<String> theirs;
	private List<String> ours;
	
	public TeamsConfirmed() {
		super();
		theirs = new ArrayList<String>();
		ours = new ArrayList<String>();
	}
	
	public TeamsConfirmed(Teams teams) {
		super();
		theirs = new ArrayList<String>();
		ours = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			theirs.add(teams.getTheirs().getCharacters().get(i).getBestGuess().getCharacter());
			ours.add(teams.getOurs().getCharacters().get(i).getBestGuess().getCharacter());			
		}
	}
	
	public List<String> getTheirs() {
		return theirs;
	}
	public void setTheirs(List<String> theirs) {
		this.theirs = theirs;
	}
	public List<String> getOurs() {
		return ours;
	}
	public void setOurs(List<String> ours) {
		this.ours = ours;
	}
	

}
