package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Restaurant implements Business {

	private String businessID;
	private String name;
	private String url;
	private Integer price;
	private Location location;
	private ArrayList<String> categories;
	private boolean open;
	private Double stars;
	private Integer reviewCount;
	private String photoURL;
	

	public Restaurant(Integer businessID) {
		this.businessID = businessID.toString();
		this.open=false;
		this.location = new Location();
		this.name = "no name";
		this.url = "http://www.yelp.com/biz/"+businessID.toString();
		this.price = 0;
		this.categories = new ArrayList<String>();
		this.photoURL="no photo";
	}

	public Restaurant(String json) {
		Pattern openPat = Pattern.compile("\"open\": (.*?), \"url\": ");
		Matcher openMat = openPat.matcher(json);
		while(openMat.find()) {
			this.open = Boolean.parseBoolean(openMat.group(1));
		}
		
		Pattern urlPat = Pattern.compile("\"url\": \"(.*?)\", \"longitude\": ");
		Matcher urlMat = urlPat.matcher(json);
		this.url = urlMat.group(1);
		
		this.location = new Location();
		Pattern longPat = Pattern.compile("\"longitude\": (.*?), \"neighborhoods\": ");
		Matcher longMat = longPat.matcher(json);
		this.location.setLongitude(urlMat.group(1));
		
		String neighStr = null;
		Pattern neighPat = Pattern.compile("\"neighborhoods\": [(.*?)], \"business_id\": ");
		Matcher neighMat = neighPat.matcher(json);
		neighStr = longMat.group(1);
		String[] neighArr = neighStr.split(", ");
		for(int i = 0; i<neighArr.length; i++) {
			this.location.getNeighborhoods().add(neighArr[i].replaceAll("\"", ""));
		}
		
		Pattern businessIDpat = Pattern.compile("\"business_id\": \"(.*?)\", \"name\": ");
		Matcher businessIDmat = businessIDpat.matcher(json);
		this.businessID = businessIDmat.group(1);
		
		Pattern namePat = Pattern.compile("\"name\": \"(.*?)\", \"categories\": ");
		Matcher nameMat = namePat.matcher(json);
		this.name = nameMat.group(1);
		
		String catStr = null;
		Pattern catPat = Pattern.compile("\"categories\": [(.*?)], \"state\": ");
		Matcher catMat = catPat.matcher(json);
		catStr = catMat.group(1);
		
		String[] catArr = catStr.split(", ");
		for(int i = 0; i<catArr.length; i++) {
			this.categories.add(catArr[i].replaceAll("\"", ""));
		}
		
		Pattern statePat = Pattern.compile("\"state\": \"(.*?)\", \"type\": ");
		Matcher stateMat = statePat.matcher(json);
		while(stateMat.find()) {
			this.location.setState(stateMat.group(1));
		}
		
		Pattern starsPat = Pattern.compile("\"stars\": (.*?), \"city\": ");
		Matcher starsMat = starsPat.matcher(json);
		while (starsMat.find()) {
			this.stars = Double.parseDouble(starsMat.group(1));
		}
		
		Pattern cityPat = Pattern.compile("\"city\": \"(.*?)\", \"full_address\": ");
		Matcher cityMat = cityPat.matcher(json);
		while(cityMat.find()) {
			this.location.setCity(cityMat.group(1));
		}
		
		String fullAddrStr = null;
		Pattern fullAddrPat = Pattern.compile("\"full_address\": \"(.*?)\", \"review_count\": ");
		Matcher fullAddrMat = fullAddrPat.matcher(json);
		while(fullAddrMat.find()) {
			fullAddrStr = fullAddrMat.group(1);
		}
		
		String[] fullAddrArr = fullAddrStr.split("\n");
		String[] addr1 = fullAddrArr[0].split(" ");
		this.location.setStreetAddress(addr1[0]);
		
		this.location.setStreet(fullAddrArr[0].replaceAll(addr1[0], ""));
		this.location.setArea(addr1[1]);
		String[] addr3 = fullAddrArr[2].split(" ");
		String areaCode = addr3[addr3.length];
		this.location.setAreaCode(areaCode);
		
		Pattern reviewCountPat = Pattern.compile("\"review_count\": (.*?), \"photo_url\": ");
		Matcher reviewCountMat = reviewCountPat.matcher(json);
		this.reviewCount = Integer.parseInt(reviewCountMat.group(1));
		
		Pattern photoURLpat = Pattern.compile("\"photo_url\": \"(.*?)\", \"schools\": ");
		Matcher photoURLmat = photoURLpat.matcher(json);
		while(photoURLmat.find()) {
			this.photoURL
		}
		
		
	}

	public String getBusinessID() {
		return businessID;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Double getStars() {
		return stars;
	}

	public void setStars(Double stars) {
		this.stars = stars;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getBusinessName() {
		return name;
	}
	
	public void setBusinessName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object other) {
		if (other instanceof Restaurant) {
			return ((Restaurant) other).getBusinessID().equals(this.businessID);
		}
		return false;
	}
	
	public int hashCode() {
		return this.businessID.hashCode();
	}
}
