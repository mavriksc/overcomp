package com.mavriksc.types;

import java.util.ArrayList;
import java.util.List;

public class CharacterGuesses {
	private List<CharGuess> charAndMatchingStrength;

	public CharacterGuesses() {
		super();
		charAndMatchingStrength = new ArrayList<CharGuess>();
	}

	public List<CharGuess> getCharAndMatchingStrength() {
		return charAndMatchingStrength;
	}

	public void setCharAndMatchingStrength(List<CharGuess> charAndMatchingStrength) {
		this.charAndMatchingStrength = charAndMatchingStrength;
	}
	
	public CharGuess getBestGuess(){
		charAndMatchingStrength.sort(null);
		return charAndMatchingStrength.get(0);
	}

}
