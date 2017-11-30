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

	public YelpUser(Integer id) {

		this.UserID = id.toString();
		this.reviewCount = 0;
		this.avgStars = 0.0;
		this.name = "";
		this.votes = new HashMap<String, Integer>();

	}

	public YelpUser(String json) {
		this.json = json;

	}

	@Override
	public String getUserID() {
		return UserID;
	}

	@Override
	public String getUserName() {
		return name;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public String getJSON() {
		return json;
	}

}
