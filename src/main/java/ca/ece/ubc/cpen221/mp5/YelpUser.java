package ca.ece.ubc.cpen221.mp5;

import java.util.Random;

public class YelpUser implements User {

	private String json;
	private String UserID;
	private String url;
	private int reviewCount;
	private String name;
	private double avgStars;

	public YelpUser(Integer id) {

		this.UserID = id.toString();
		this.reviewCount = 0;
		this.avgStars = 0.0;
		this.name = "";

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
