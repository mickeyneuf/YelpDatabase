package ca.ece.ubc.cpen221.mp5;

/**
 * A generic interface representing a basic voting system.
 * The representation a structure that contains candidates and votes associated with those candidates.
 * Eg. a map with candidates as keys and votes as the values.
 * Examples: Votes on a comment or review, reactions to a Facebook post
 * 
 */
public interface VotesMap{
	
	 /**
	  * A method to set votes of a candidate in this map
	  * @param candidate
	  * 	String name of thing to set votes for, must be in map
	  * @param votes
	  * 	Integer votes to set vote count to, must be >=0
	  */
	public void setVotes(String candidate, Integer votes);
	
	/**
	 * A method to return the votes of a specified candidate
	 * @param candidate
	 * 		String name of candidate, must be in map
	 * @return
	 * 		Integer votes of this candidate
	 */
	public Integer getVotes(String candidate);
	
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
	public boolean equals(Object other);
	
	/**
	 * A method to return hashCode of this VotesMap
	 * HashCode of a VotesMap is the hashCode of its representation
	 * @return
	 */
	@Override
	public int hashCode();
}
