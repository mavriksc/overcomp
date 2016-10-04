package com.mavriksc.types;


public class Teams {
	private Team theirs;
	private Team ours;

	public Teams() {
		super();
		theirs = new Team();
		ours = new Team();
		
	}

	public Team getTheirs() {
		return theirs;
	}

	public void setTheirs(Team theirs) {
		this.theirs = theirs;
	}

	public Team getOurs() {
		return ours;
	}

	public void setOurs(Team ours) {
		this.ours = ours;
	}

	

}
