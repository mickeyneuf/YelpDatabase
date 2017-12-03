package ca.ece.ubc.cpen221.mp5;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class YelpUser implements User {

	private final String userID;
	private String url;
	private Integer reviewCount;
	private String name;
	private Double avgStars;
	private YelpVotes votes;
	
	
	public YelpUser(Integer userID, String name) {
		this.userID = userID.toString();
		this.url = "http://www.yelp.com/user_details?userid="+this.userID;
		this.reviewCount = 0;
		this.avgStars = 0.0;
		this.name = name;
		this.votes = new YelpVotes();
	}

	public YelpUser(String json) throws InvalidInputException, ArrayIndexOutOfBoundsException {
		Pattern urlPat = Pattern.compile("\"url\": \"(.*?)\", \"votes\": ");
		Matcher urlMat = urlPat.matcher(json);
		if(urlMat.find()) {
			this.url = urlMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find url");
			throw new InvalidInputException();
		}
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
		Pattern reviewCountPat = Pattern.compile("\"review_count\": (.*?), \"type\": ");
		Matcher reviewCountMat = reviewCountPat.matcher(json);
		if(reviewCountMat.find()) {
			this.reviewCount = Integer.parseInt(reviewCountMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find review count");
			throw new InvalidInputException();
		}
		Pattern userIDPat = Pattern.compile("\"user_id\": \"(.*?)\", \"name\": ");
		Matcher userIDMat = userIDPat.matcher(json);
		if(userIDMat.find()) {
			this.userID = userIDMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find userID");
			throw new InvalidInputException();
		}	
		Pattern namePat = Pattern.compile("\"name\": \"(.*?)\", \"average_stars\": ");
		Matcher nameMat = namePat.matcher(json);
		if(nameMat.find()) {
			this.name = nameMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find name");
			throw new InvalidInputException();
		}
		Pattern starsPat = Pattern.compile("\"average_stars\": (.*?)}");
		Matcher starsMat = starsPat.matcher(json);
		if(starsMat.find()) {
			this.avgStars = Double.parseDouble(starsMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find average stars");
			throw new InvalidInputException();
		}
	}

	@Override
	public String getJSON() {
		String json = "{\"url\": \""+this.url+"\", \"votes\": ";
		if (this.reviewCount>0) {
			json += "{\"funny\": "+this.votes.getVotes("funny")+", \"useful\": "+this.votes.getVotes("useful")+", \"cool\": "+this.votes.getVotes("cool")+"}";
		}else{
			json += "{}";
		}
		json+=", \"review_count\": "+this.reviewCount.toString()+", \"type\": \"user\", \"user_id\": \""
		+this.userID+"\", \"name\": \""+this.name+"\", \"average_stars\": "+this.avgStars.toString()+"}";
		return json;
	}
	
	public String getUserID() {
		return userID;
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

	public Integer getVotes(String reaction) {
		return this.votes.getVotes(reaction);
	}
	
	public void setVotes(String reaction, Integer votes) {
		this.votes.setVotes(reaction, votes);
	}
	
	public boolean equals(Object other) {
		if(other instanceof YelpUser) {
			return this.userID.equals(((YelpUser) other).getUserID());
		}
		return false;
	}
	
	public int hashCode() {
		return this.userID.hashCode();
	}
}
