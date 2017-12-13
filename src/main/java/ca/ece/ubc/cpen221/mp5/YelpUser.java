package ca.ece.ubc.cpen221.mp5;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class represents a Yelp User and implements the User interface.
 * 
 * A Yelp user is represented its fields, which include a final userID String,
 * a url and name string, an integer representing their review count, a Double representing
 * their average stars, and a YelpVotes object representing their votes.
 * 
 * A YelpUser can be created with a constructor that takes a string in JSON format containing
 * all the field information, or with a constructor that takes a userID Integer and a name String
 * and autogenerates the other fields. All fields except the userID can be set by setter methods, and
 * all fields can be returned by getter methods. 
 * 
 *
 */
public class YelpUser implements User {

	private final String userID;
	private String url;
	private Integer reviewCount;
	private String name;
	private Double avgStars;
	private YelpVotes votes;
	
	/*
	 * Abstraction Function:
	 * 			- Represents a Yelp user as an object with its information as fields, of either
	 * 			  Strings, Integers or Doubles, and a YelpVotes object.
	 * Rep Invariant:
	 * 			- no fields may be null
	 * 			- reviewCount must be >=0, and must correspond to number of reviews of this user in the
	 * 			  database, unless this user has been deleted (reviewCount=0)
	 * 			- avgStars must be between 1 and 5, and correspond to reviews this user has written in the
	 * 			  database, unless this user has been deleted (avgStars=0)
	 * 			- votes must only contain "cool", "useful", and "funny", and number for all of them 
	 * 			  must >=0, and correspond with the votes on reviews that this user has written IN the database
	 * 		      unless this user has been deleted (all votes=0)
	 */
	
	/**
	 * Constructor for YelpUser, that takes in userID and name as parameters and autogenerates the rest.
	 * Other fields must be changed manually with setter methods
	 * @param userID
	 * 		ID of this user
	 * @param name
	 * 		name of this user
	 */
	
	public YelpUser(Integer userID, String name) {
		this.userID = userID.toString();
		// a user's url is simply the default + their userID
		this.url = "http://www.yelp.com/user_details?userid="+this.userID;
		this.reviewCount = 0;
		this.avgStars = 0.0;
		this.name = name;
		this.votes = new YelpVotes(0, 0, 0);
	}
	
	/**
	 * Constructor for YelpUser, that takes in a correctly-formatted JSON string as a parameter
	 * and uses its info to fill its fields
	 * @param json
	 * 		JSON-formatted string that must have correct format
	 * @throws InvalidInputException
	 * 		If JSON string format is not correct
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If JSON string format is not correct
	 */
	public YelpUser(String json) throws InvalidInputException, ArrayIndexOutOfBoundsException {
		// storing URL
		Pattern urlPat = Pattern.compile("\"url\": \"(.*?)\", \"votes\": ");
		Matcher urlMat = urlPat.matcher(json);
		if(urlMat.find()) {
			this.url = urlMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find url");
			throw new InvalidInputException();
		}
		// storing votes
		Pattern votesPat = Pattern.compile("\"votes\": (.*?), \"review_count\": ");
		Matcher votesMat = votesPat.matcher(json);
		String voteStr = null;
		if(votesMat.find()) {
				voteStr = votesMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find votes");
			throw new InvalidInputException();
		}
		String[] voteArr = voteStr.split(" ");
		try {
		this.votes = new YelpVotes(Integer.parseInt(voteArr[5].replaceAll("}", "")), 
								   Integer.parseInt(voteArr[3].replaceAll(",", "")), 
								   Integer.parseInt(voteArr[1].replaceAll(",", "")));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find votes");
			throw new InvalidInputException();
		}
		// storing review count
		Pattern reviewCountPat = Pattern.compile("\"review_count\": (.*?), \"type\": ");
		Matcher reviewCountMat = reviewCountPat.matcher(json);
		if(reviewCountMat.find()) {
			this.reviewCount = Integer.parseInt(reviewCountMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find review count");
			throw new InvalidInputException();
		}
		// storing userID
		Pattern userIDPat = Pattern.compile("\"user_id\": \"(.*?)\", \"name\": ");
		Matcher userIDMat = userIDPat.matcher(json);
		if(userIDMat.find()) {
			this.userID = userIDMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find userID");
			throw new InvalidInputException();
		}	
		// storing name
		Pattern namePat = Pattern.compile("\"name\": \"(.*?)\", \"average_stars\": ");
		Matcher nameMat = namePat.matcher(json);
		if(nameMat.find()) {
			this.name = nameMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find name");
			throw new InvalidInputException();
		}
		// storing stars
		Pattern starsPat = Pattern.compile("\"average_stars\": (.*?)}");
		Matcher starsMat = starsPat.matcher(json);
		if(starsMat.find()) {
			this.avgStars = Double.parseDouble(starsMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find average stars");
			throw new InvalidInputException();
		}
	}

	
	/**
	 * A method for getting the JSON string for this user
	 * 
	 * @return the JSON string that represents this user
	 * 
	 */
	@Override
	public String getJSON() {
		String json = "{\"url\": \""+this.url+"\", \"votes\": ";
		if (this.reviewCount>0) {
			json += "{\"funny\": "+this.votes.getVotes("funny")+", \"useful\": "+this.votes.getVotes("useful")+", \"cool\": "+this.votes.getVotes("cool")+"}";
		}else{
			json += "{}";
		}
		json+=", \"review_count\": "+this.reviewCount.toString()+", \"type\": \"user\", \"user_id\": \""
		+this.userID+"\", \"name\": \""+this.name+"\", \"average_stars\": "+(this.avgStars==0 ? "0" : this.avgStars.toString())+"}";
		return json;
	}
	/**
	 * A method to return this user's ID
	 * @return
	 * 		String representing User's ID
	 */
	@Override
	public String getUserID() {
		return userID;
	}

	/**
	 * A method to return this user's url
	 * @return
	 * 		String representing User's url
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * A setter method to set this user's url
	 * @param url
	 * 		url string, must be in correct url format
	 */
	public void setURL(String url) {
		this.url = url;
	}
	
	/**
	 * A getter method that returns number of reviews user has written
	 * @return
	 * 		int number of reviews
	 */
	public int getReviewCount() {
		return reviewCount;
	}
	
	/**
	 * A setter method that sets the number of reviews user has written
	 * Requires: number corresponds with the number of reviews this user has written IN
	 * THE DATABASE
	 * @param reviewCount
	 * 		int number of reviews
	 */
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	
	/**
	 * A method to return this user's username
	 * @return 
	 * 		String representing User's username
	 */
	@Override
	public String getUserName() {
		return name;
	}

	/**
	 * A setter method to set this user's username
	 * @param name
	 * 		String representing user's username
	 */
	public void setUserName(String name) {
		this.name = name;
	}

	/**
	 * A getter method that returns average stars of user's reviews
	 * @return
	 * 		double representing average review stars
	 */
	public double getAvgStars() {
		return avgStars;
	}
	
	/**
	 * A setter method that sets the average stars of user's reviews
	 * Requires: average stars corresponds with reviews of this user IN the database
	 * @param avgStars
	 * 		double representing average review stars
	 */
	public void setAvgStars(double avgStars) {
		this.avgStars = avgStars;
	}

	/**
	 * A getter method that returns the votes a user has received for a specified reaction
	 * @param reaction
	 * 		String representing reaction we are looking for count of, must be one of "cool", "useful" or "funny"
	 * @return
	 * 		Integer representing votes of specified reaction that user received
	 */
	public Integer getVotes(String reaction) {
		return this.votes.getVotes(reaction);
	}
	
	/**
	 * A setter method that sets the votes a user has received for a specified reaction
	 * Requires: votes corresponds to the the votes user has received on reviews IN the database
	 * @param reaction
	 * 		String representing reaction to set, must be one of "cool", "useful", or "funny"
	 * @param votes
	 * 		Integer representing votes of specified reaction to set
	 */
	public void setVotes(String reaction, Integer votes) {
		this.votes.setVotes(reaction, votes);
	}
	
	/**
	 * A method to check equality of this User with another object
	 * Two users are defined as equal if their ID is equal
	 * @param
	 * 		other object to be compared
	 * @return
	 * 		true if this is equal to other, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(other instanceof YelpUser) {
			return this.userID.equals(((YelpUser) other).getUserID());
		}
		return false;
	}
	
	/**
	 * A method to return hashCode of this User
	 * A User's hashCode is represented by the sum of the numeric values
	 * of the characters in its user ID
	 * @return
	 * 		int representing sum of numerical values of characters in this user's ID
	 */	
	public int hashCode() {
		return this.userID.hashCode();
	}
}
