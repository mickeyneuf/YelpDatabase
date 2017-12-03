package ca.ece.ubc.cpen221.mp5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a review for the restaurant-review website Yelp, and implements 
 * the generic Review interface
 * 
 * A review contains final fields of Strings representing reviewID, userID, businessID, a
 * non-final string field representing the review text, another representing the date it 
 * was written, an Integer rating, and a YelpVotes object representing the votes this review
 * has been given. 
 * 
 */
public class YelpReview implements Review {

	private final String reviewID;
	private final String userID;
	private final String businessID;
	private String review;
	private String date;
	private Integer rating;
	private YelpVotes votes;
	
	/*
	 *  Abstraction Function:
	 * 		-   Represents a Yelp review as an object with the review's info as fields of either
	 * 			Strings, or Integers (or a YelpVotes object for votes), all of which can be returned 
	 * 			to clients and some of which can be set by clients.
	 * 
	 * Rep Invariant:
	 * 		-   reviewID, userID, and businessID contains only alphabetical (upper or lower case)
	 * 			and numerical characters.
	 * 		-   date is in YYYY-MM-DD format
	 * 		-   no fields can be null or empty
	 * 		-   rating is an integer between 1 and 5
	 */
	
	/**
	 * Constructor to create a YelpReview object with certain parameters, rather than a full JSON string.
	 * Review text and votes must be set after creation, using getter and setter methods of this class.
	 * 
	 * Requires:
	 * 			- reviewID, businessID, userID contain only numeric and alphabetical characters
	 *			- date is in MM/DD/YY format
	 *			- 1<rating<5
	 *			- no parameters null or empty
	 *			- reviewID of this review is not the same as another review in the database
	 * 
	 * @param reviewID
	 * 				String representing ID of this review
	 * @param date
	 * 				String representing date on which review was posted (or last edited)
	 * @param userID
	 * 				String representing the ID of user who wrote this review
	 * @param businessID
	 * 				String representing the ID of the business this review is written for
	 * @param rating
	 * 				Integer representing the rating of this review
	 */
	public YelpReview (String reviewID, String date, String userID, String businessID, Integer rating) {
		this.review = "no text";
		this.userID = userID;
		this.date = date;
		this.businessID = businessID;
		this.rating = rating;
		this.reviewID = reviewID;
		this.votes = new YelpVotes(0, 0, 0);
	}

	/**
	 * Constructor to create a YelpReview object from a JSON string containing information to fill all the fields.
	 * Contains a built in parser that extracts info from JSON string based on JSON grammar and fills this object's
	 * fields with the info
	 * 
	 * Requires: String is in correct-JSON format for Yelp reviews
	 * 			 Throws an InvalidFormatException if not
	 * 
	 * @param json
	 * 			A JSON-formatted String representing all the info for this review
	 * 		
	 */
	public YelpReview(String json) throws InvalidInputException, ArrayIndexOutOfBoundsException{
		// locates and stores businessID of this review based on JSON grammar and format
		Pattern businessIDpat = Pattern.compile("\"business_id\": \"(.*?)\", \"votes\": ");
		Matcher businessIDmat = businessIDpat.matcher(json);
		// if a businessID cannot be found in this format, throw exception
		if(businessIDmat.find()) {
			this.businessID = businessIDmat.group(1);
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find BusinessID");
			throw new InvalidInputException();
		}
		// locates and stores votes in a YelpVotes object based on JSON grammar and format
		Pattern votesPat = Pattern.compile("\"votes\": (.*?), \"review_id\"");
		Matcher votesMat = votesPat.matcher(json);
		String voteStr = null;
		
		if(votesMat.find()) {
			voteStr = votesMat.group(1);
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find votes");
			throw new InvalidInputException();
		}
		// storing "cool", "useful", funny votes in a YelpVotes object by parsing integers
		// catches ArrayIndexOutOfBoundsException, throws new
		String[] voteArr = voteStr.split(" ");
		try {
		this.votes = new YelpVotes(Integer.parseInt(voteArr[1].replaceAll(",", "")), 
								   Integer.parseInt(voteArr[3].replaceAll(",", "")), 
								   Integer.parseInt(voteArr[5].replaceAll("}", "")));
		}catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find votes");
			throw new InvalidInputException();
		}
		// storing reviewID
		Pattern reviewIDpat = Pattern.compile("\"review_id\": \"(.*?)\", \"text\"");
		Matcher reviewIDmat = reviewIDpat.matcher(json);
		if(reviewIDmat.find()) {
			this.reviewID = reviewIDmat.group(1);	
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find review ID");
			throw new InvalidInputException();
		}
		// storing review text
		Pattern textPat = Pattern.compile("\"text\": \"(.*?)\", \"stars\": ");
		Matcher textMat = textPat.matcher(json);
		if(textMat.find()) {
			this.review = textMat.group(1);
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find review text");
			throw new InvalidInputException();
		}
		// storing rating
		Pattern starsPat = Pattern.compile("\"stars\": (.*?), \"user_id\": ");
		Matcher starsMat = starsPat.matcher(json);
		if(starsMat.find()) {
			this.rating = Integer.parseInt(starsMat.group(1));
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find rating");
			throw new InvalidInputException();
		}
		//storing user ID
		Pattern userIDpat = Pattern.compile("\"user_id\": \"(.*?)\", \"date\": ");
		Matcher userIDmat = userIDpat.matcher(json);
		if(userIDmat.find()) {
			this.userID = userIDmat.group(1);
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find userID");
			throw new InvalidInputException();
		}
		// storing review date
		Pattern datePat = Pattern.compile("\"date\": \"(.*?)\"}");
		Matcher dateMat = datePat.matcher(json);
		if(dateMat.find()) {
			this.date = dateMat.group(1);
		} else {
			System.out.println("Please enter Yelp Review info in valid JSON format:\nCould not find date");
			throw new InvalidInputException();
		}
	}
	/**
	 * Method for returning JSON-formatted string representing this review
	 * 
	 * @return
	 * 		this review as a JSON string
	 */
	@Override
	public String getJSON() {
		return "{\"type\": \"review\", \"business_id\": \""+this.businessID+
				"\", \"votes\": {\"cool\": "+this.votes.getVotes("cool").toString()+
				", \"useful\": "+this.votes.getVotes("useful").toString()+", \"funny\": "
				+this.votes.getVotes("funny").toString()+"}, \"review_id\": \""+this.reviewID
				+"\", \"text\": \""+this.review+"\", \"stars\": "+this.getRating().toString()
				+", \"user_id\": \""+this.userID+"\", \"date\": \""+this.date+"\"}";		
	}
	
	/**
	 * Getter method that returns this reviewID as a string
	 * @return
	 * 		String representing this reviewID
	 */
	@Override
	public String getReviewID() {
		return this.reviewID;
	}
	
	/**
	 * Getter method that returns this review's text
	 * @return	
	 * 		String representing text in this review
	 */
	@Override
	public String getText() {
		return review;
	}

	/**
	 * Setter method that sets the text in this review
	 * @param review
	 * 		String representing text to go in this review
	 */
	public void setText(String review) {
		this.review = review;
	}

	/**
	 * Getter method that returns the ID of the user who posted this review
	 * @return
	 * 		String representing ID of poster
	 */
	@Override
	public String getUser() {
		return userID;
	}
	/**
	 * Getter method that returns string representation of date review was posted
	 * @return
	 * 		String of date of review posting
	 */
	
	@Override
	public String getDate() {
		return date;
	}
	
	/**
	 * Setter method that sets date this review was posted or updated
	 * Requires: Date cannot be earlier than former date, and is in YYYY-MM-DD format
	 * @param date
	 * 		String representing date this review was posted or edited
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Getter method that returns Integer rating for this review
	 * @return
	 * 		this review's rating
	 */
	public Integer getRating() {
		return rating;
	}
	
	/**
	 * Setter method that sets Integer rating for this review
	 * @param 
	 * 		rating to be set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * Getter method that returns ID of restaurant being reviewed
	 * @return
	 * 		String businessID representing restaurant of this review
	 */
	@Override
	public String getReviewed() {
		return businessID;
	}
	
	/**
	 * Getter method to return the vote count for a specified reaction of this review
	 * Requires: reaction is one of "cool", "useful", or "funny", not including the ""
	 * @param reaction
	 * 		cool, useful, or funny
	 * @return
	 * 		Integer number of votes for this reaction for this review
	 */
	public Integer getVotes(String reaction) {
		return this.votes.getVotes(reaction);
	}
	
	/**
	 * Setter method to set the vote count for a specified reaction of this review
	 * Requires: - reaction is one of "cool", "useful", or "funny", not including the ""
	 *  		 - votes>=0
	 * @param reaction
	 * 		cool, useful, or funny
	 * @param votes
	 * 		Integer number of votes for reaction of this review
	 */
	public void setVotes(String reaction, Integer votes) {
		this.votes.setVotes(reaction, votes);
	}
	
	/**
	 * A method to check equality of this Business with another object
	 * Two Businesses are defined as equal if their ID is equal
	 * @param
	 * 		other object to be compared
	 * @return
	 * 		true if this is equal to other, false otherwise
	 */
	@Override 
	public boolean equals(Object other) {
		if(other instanceof YelpReview) {
			return this.reviewID.equals(((YelpReview) other).getReviewID());
		}
		return false;
	}

	/**
	 * A method to return hashCode of this business
	 * A business' hashCode is represented by the sum of the numeric values
	 * of the characters in its business ID
	 * @return
	 * 		int representing sum of numerical values of characters in this business' ID
	 */	
	@Override
	public int hashCode() {
		return this.reviewID.hashCode();
	}
}
