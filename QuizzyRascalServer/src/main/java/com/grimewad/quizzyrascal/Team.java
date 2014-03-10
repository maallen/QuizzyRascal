package com.grimewad.quizzyrascal;

public class Team {
	
	public static final String TEAM_NAME_KEY = "teamName";
	public static final String NUM_OF_MEMBERS_KEY ="numOfMembers";

	private String teamName;
	private int numOfMembers;
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public int getNumOfMembers() {
		return numOfMembers;
	}
	
	public void setNumOfMembers(int numOfMembers) {
		this.numOfMembers = numOfMembers;
	}
}
