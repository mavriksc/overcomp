package com.mavriksc.types;

public class CharGuess implements Comparable<CharGuess> {
	private String character;
	private Float score;
	
	public CharGuess() {
		super();
		character = "";
		score = 0f;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}
	
	@Override
	public int compareTo(CharGuess o) {
		return o.getScore().compareTo(this.getScore());
	}
	
	
	

}
