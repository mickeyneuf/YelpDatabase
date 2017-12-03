package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;
/**
 * This class represents the votes for the reactions "cool", "useful", and "funny" for
 * a YelpReview or YelpUser, and implements the VotesMap interface. 
 *
 */
public class YelpVotes implements VotesMap {
	private HashMap <String, Integer> votes;
	
	/*
	 * Abstraction Function: 
	 * 			- Represents votes and reactions as a HashMap of Strings
	 * 			  and Integers such that the reaction strings are keys and the 
	 * 			  vote counts are values
	 * Rep Invariant:
	 * 			- Requires int cool, useful, and funny >=0, and correspond to the votes
	 * 			  on reviews and users in the same database
	 */
	/**
	 * Constructor for YelpVotes that creates a new YelpVotes object
	 * @param cool
	 * 		number of votes for "cool"
	 * @param useful
	 * 		number of votes for "useful"
	 * @param funny
	 * 		number of votes for "funny"
	 */
	public YelpVotes(int cool, int useful, int funny) {
		this.votes = new HashMap<String, Integer>();
		votes.put("cool", cool);
		votes.put("useful", useful);
		votes.put("funny", funny);
	}

	/**
	 * A setter method to set the votes for a specified reaction
	 * @param reaction
	 * 		reaction to set votes for, must be either "cool", "useful", or "funny"
	 * @param votes
	 * 		number of votes to set for reaction, must correspond to votes on reviews IN the database
	 */
	@Override
	public void setVotes(String reaction, Integer votes) {
		this.votes.put(reaction, votes);
	}
	
	/**
	 * A getter method to get the votes for a specified reaction
	 * @param reaction
	 * 		String representing reaction to get votes for
	 * @return
	 * 		vote count for this reaction 		
	 */
	@Override
	public Integer getVotes(String reaction) {
		int votecount;
		votecount = votes.get(reaction);
		return votecount;
	}

	/**
	 * Getter method that a copy of this YelpVotes votes HashMap,
	 * mostly used for determining equality
	 * @return
	 * 		new HashMap of Strings and Integers that is equal to 
	 * 		this YelpVotes' votes HashMap
	 */
	public HashMap<String, Integer> getMap(){
		return new HashMap<String, Integer>(this.votes);
	}
		
	/**
	 * A method to determine equality of this and another Object
	 * Two VotesMaps are defined as equal if all keys and values 
	 * are equal
	 * @param other
	 * 		Object to be compared
	 * @return
	 * 		true if equal, false otherwise		
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof YelpVotes) {
			return ((YelpVotes) other).getMap().equals(this.votes);
		}
		return false;
	}
	
	/**
	 * A method to return hashCode of this VotesMap
	 * HashCode of a VotesMap is the hashCode of its representation
	 * @return
	 */
	@Override
	public int hashCode() {
		return votes.hashCode();
	}
	
}
