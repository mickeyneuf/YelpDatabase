package ca.ece.ubc.cpen221.mp5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YelpReview implements Review {

	private String userID;
	private String businessID;
	private String reviewID;
	private String review;
	private String date;
	private Integer rating;
	private YelpVotes votes;
	
	public YelpReview(Integer reviewID, String date, Integer userID, Integer businessID) {
		this.review = "no text";
		this.userID = userID.toString();
		this.date = date;
		this.businessID = businessID.toString();
		this.rating = 0;
		this.reviewID = reviewID.toString();
		this.votes = new YelpVotes();
	}

	public YelpReview(String json) {
		Pattern businessIDpat = Pattern.compile("\"business_id\": \"(.*?)\", \"votes\"");
		Matcher businessIDmat = businessIDpat.matcher(json);
		while (businessIDmat.find()) {
			this.businessID = businessIDmat.group(1);
		}
		Pattern votesPat = Pattern.compile("\"votes\": (.*?), \"review_id\"");
		Matcher votesMat = votesPat.matcher(json);
		String voteStr = null;
		while (votesMat.find()){
			voteStr = votesMat.group(1);
		}
		String[] voteArr = voteStr.split(" ");
		this.votes = new YelpVotes(Integer.parseInt(voteArr[1].replaceAll(",", "")), 
								   Integer.parseInt(voteArr[3].replaceAll(",", "")), 
								   Integer.parseInt(voteArr[5].replaceAll("}", "")));
		Pattern reviewIDpat = Pattern.compile("\"review_id\": \"(.*?)\", \"text\"");
		Matcher reviewIDmat = reviewIDpat.matcher(json);
		while (reviewIDmat.find()) {
			this.reviewID = reviewIDmat.group(1);	
		}
		
		Pattern textPat = Pattern.compile("\"text\": \"(.*?)\", \"stars\": ");
		Matcher textMat = textPat.matcher(json);
		while (textMat.find()) {
			this.review = textMat.group(1);
		}
		
		Pattern starsPat = Pattern.compile("\"stars\": (.*?), \"user_id\": ");
		Matcher starsMat = starsPat.matcher(json);
		while (starsMat.find()) {
			this.rating = Integer.parseInt(starsMat.group(1));
		}
		
		Pattern userIDpat = Pattern.compile("\"user_id\": \"(.*?)\", \"date\": ");
		Matcher userIDmat = userIDpat.matcher(json);
		while (userIDmat.find()) {
			this.userID = userIDmat.group(1);
		}
		
		Pattern datePat = Pattern.compile("\"date\": \"(.*?)\"\\");
		Matcher dateMat = datePat.matcher(json);
		while (dateMat.find()) {
			this.date = dateMat.group(1);
		}
	}

	public String getJSON() {
		return "{\"type\": \""+this.review+"\", \"business_id\": \""+this.businessID+
				"\", \"votes\": {\"cool\": "+this.votes.getVotes("cool").toString()+
				", \"useful\": "+this.votes.getVotes("useful").toString()+", \"funny\": "
				+this.votes.getVotes("funny").toString()+"}, \"review_id\": \""+this.reviewID
				+"\", \"text\": \""+this.review+"\", \"stars\": "+this.getRating().toString()
				+", \"user_id\": \""+this.userID+"\", \"date\": \""+this.date+"\"}";
				
	}
	
	public String getReviewID() {
		return this.reviewID;
	}
	
	public void setReviewID(String reviewID) {
		this.reviewID = reviewID;
	}
	
	public String getText() {
		return review;
	}

	public void setText(String review) {
		this.review = review;
	}

	public String getUser() {
		return userID;
	}

	public void setUser(String userID) {
		this.userID = userID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReviewed() {
		return businessID;
	}

	public void setReviewed(String businessID) {
		this.businessID = businessID;
	}
	
	public Integer getVotes(String reaction) {
		return this.votes.getVotes(reaction);
	}
	
	public void setVotes(String reaction, Integer votes) {
		this.votes.setVotes(reaction, votes);
	}
	
	@Override 
	public boolean equals(Object other) {
		if(other instanceof YelpReview) {
			return this.reviewID.equals(((YelpReview) other).getReviewID());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.reviewID.hashCode();
	}
}
