package com.mavriksc.types;

import java.util.ArrayList;
import java.util.List;

public class Team {
	private List<CharacterGuesses> characters;

	public Team() {
		super();
		characters = new ArrayList<CharacterGuesses>();
	}

	public List<CharacterGuesses> getCharacters() {
		return characters;
	}

	public void setCharacters(List<CharacterGuesses> characters) {
		this.characters = characters;
	}
	

}
