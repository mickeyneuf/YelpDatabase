package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;

public class YelpVotes implements VotesMap {
	private HashMap <String, Integer> votes;
	
	public YelpVotes() {
		this.votes = new HashMap<String, Integer>();
		votes.put("cool", 0);
		votes.put("useful", 0);
		votes.put("funny", 0);
	}
	
	public YelpVotes(int cool, int useful, int funny) {
		this.votes = new HashMap<String, Integer>();
		votes.put("cool", cool);
		votes.put("useful", useful);
		votes.put("funny", funny);
	}

	public void setVotes(String candidate, Integer votes) {
		this.votes.put(candidate, votes);
	}

	public Integer getVotes(String candidate) throws NullPointerException{
		int votecount;
		try{
			votecount = votes.get(candidate);
		} catch(NullPointerException e) {
			return -1;
		}
		return votecount;
	}

	public void clear() {
		for (String s : this.votes.keySet()) {
			this.votes.put(s, 0);
		}
	}
	
	public HashMap<String, Integer> getMap(){
		return new HashMap<String, Integer>(this.votes);
	}
	
	@Override
	public String toString() {
		return getMap().toString();
	}
		
	@Override
	public boolean equals(Object other) {
		if (other instanceof YelpVotes) {
			return ((YelpVotes) other).getMap().equals(this.votes);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.hashCode();
	}
	
}
