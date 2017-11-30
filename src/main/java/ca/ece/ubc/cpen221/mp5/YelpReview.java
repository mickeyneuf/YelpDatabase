package ca.ece.ubc.cpen221.mp5;

public class YelpReview implements Review {

	private String json;
	private String review;
	private String reviewID;
	private String url;
	private String userID;
	private String date;
	private Integer rating;

	public YelpReview(Integer reviewID) {

	}

	public YelpReview(String json) {
		this.json = json;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public String getPoster() {
		return userID;
	}

	@Override
	public String getReviewed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return review;
	}

	@Override
	public String getDate() {
		return date;
	}

	public String getReviewID() {
		return reviewID;
	}
	
	public Integer getRating() {
		return rating;
	}

}
