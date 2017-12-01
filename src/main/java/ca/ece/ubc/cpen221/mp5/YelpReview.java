package ca.ece.ubc.cpen221.mp5;


public class YelpReview implements Review {

	private String json;
	private String review;
	private String url;
	private String userID;
	private String date;
	private Integer rating;
	private String businessID;
	private YelpVotes votes;
	
	public YelpReview() {
		this.url = "no-url.com";
		this.json = "no json";
		this.review = "no review";
		this.userID = "no UserID";
		this.date = "no date";
		this.businessID = "no businessID";
		this.rating = 0;
		this.votes = new YelpVotes();
	}

	public YelpReview(String json) {
		this.json = json;
		String[] fields = json.split(" ");
		this.businessID = fields[3].replaceAll("\"", "");
		this.votes = new YelpVotes();
		votes.setVotes("cool", Integer.parseInt(fields[6].replaceAll(",", "")));
		votes.setVotes("cool", Integer.parseInt(fields[6].replaceAll(",", "")));
	}

	public String getJSON() {
		return json;
	}

	public void setJSON(String json) {
		this.json = json;
	}

	public String getText() {
		return review;
	}

	public void setText(String review) {
		this.review = review;
	}

	public String getURL() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	@Override 
	public boolean equals(Object other) {
		if(other instanceof YelpReview) {
			return this.url.equals(((YelpReview) other).getURL());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.url.hashCode();
	}
}
