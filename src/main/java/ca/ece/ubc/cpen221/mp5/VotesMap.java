package ca.ece.ubc.cpen221.mp5;

/**
 * A generic interface representing a basic voting system.
 * The representation a structure that contains candidates and votes associated with those candidates.
 * Eg. a map with candidates as keys and votes as the values.
 * Examples: Votes on a comment or review, reactions to a Facebook post
 * 
 *
 */
public interface VotesMap{
	
	public void setVotes(String candidate, Integer votes);
	
	public Integer getVotes(String candidate);
	
	public void clear();
	
	@Override
	public String toString();
	
	@Override
	public boolean equals(Object other);
	
	@Override
	public int hashCode();
}
