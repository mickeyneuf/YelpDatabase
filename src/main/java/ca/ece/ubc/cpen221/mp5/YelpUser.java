package ca.ece.ubc.cpen221.mp5;

import java.util.HashMap;

public class YelpUser implements User {

	private String json;
	private String UserID;
	private String url;
	private int reviewCount;
	private String name;
	private double avgStars;
	private HashMap<String, Integer> votes;
	
	
	public YelpUser(Integer userID) {
		this.json = "no JSON";
		this.url = "no-url.com";
		this.UserID = userID.toString();
		this.reviewCount = 0;
		this.avgStars = 0.0;
		this.name = "no name";
		this.votes = new HashMap<String, Integer>();

	}

	public YelpUser(String json) {
		this.json = json;

	}
	public String getJSON() {
		return json;
	}

	public void setJSON(String json) {
		this.json = json;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getUserName() {
		return name;
	}

	public void setUserName(String name) {
		this.name = name;
	}

	public double getAvgStars() {
		return avgStars;
	}

	public void setAvgStars(double avgStars) {
		this.avgStars = avgStars;
	}

	public HashMap<String, Integer> getVotes() {
		return votes;
	}

	public void setVotes(HashMap<String, Integer> votes) {
		this.votes = votes;
	}

	public boolean equals(Object other) {
		if(other instanceof YelpUser) {
			return this.UserID.equals(((YelpUser) other).getUserID());
		}
		return false;
	}
	
	public int hashCode() {
		return this.UserID.hashCode();
	}
}
