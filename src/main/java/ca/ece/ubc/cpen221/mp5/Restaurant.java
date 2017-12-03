package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a Restaurant in Yelp's database. 
 * It implements the generic Business interface.
 *
 * A restaurant contains final fields of Strings representing businessID, and non-final
 * Strings representing restaurant name, url, price and photoURL. It also contains a mutable 
 * Location object representing the location of this restaurant, and a list representing the 
 * categories it can be classified into, as well as a boolean that represents whether or not
 * the restaurant is currently open.
 */

public class Restaurant implements Business {

	private final String businessID;
	private String name;
	private String url;
	private Integer price;
	private Location location;
	private ArrayList<String> categories;
	private boolean open;
	private Double stars;
	private Integer reviewCount;
	private String photoURL;
	
	/*
	 * Abstraction Function:
	 * 		- Represents a Yelp restaurant as an object with the restaurant's info as fields of either
	 * 		  Strings, Integers, ArrayList of Strings, and a location object for restaurant's location
	 * Rep Invariant:
	 * 		- Requires that no fields are null
	 * 		- businessID must be alphabetical or numerical characters
	 * 		- name must be alphabetical characters
	 * 		- price must be between 1 and 5
	 * 		- stars must be between 1 and 5
	 * 		- reviewCount must be >=0
	 * 		- photoURL and url must be in correct url format
	 */
	
	/**
	 * Constructor for Restaurant, that takes in a businessID and a name, and autogenerates the rest
	 * of the information. All other fields must be set using getters and setters
	 * Requires: businessID should be different from other restaurants in same database
	 * 
	 * @param businessID
	 * 		Integer representing businessID
	 * @param name
	 * 		String representing name of business
	 */
	public Restaurant(Integer businessID, String name) {
		this.businessID = businessID.toString();
		this.open = false;
		this.location = new Location();
		this.name = name;
		// url is automatically default url + name with spaces replaced by hyphens
		this.url = "http://www.yelp.com/biz/" + name.replaceAll(" ", "-");
		this.price = 0;
		this.categories = new ArrayList<String>();
		this.photoURL = "no photo";
	}
	/**
	 * Constructor for Restaurant, that takes in JSON-formatted String as parameter and fills all the fields
	 * using info
	 * Requires: String is in correct-JSON format
	 * @throws
	 * 		InvalidInputException, ArrayIndexOutOfBoundsException if string is not correctly formatted
	 * @param businessID
	 * 		Integer representing businessID
	 * @param name
	 * 		String representing name of business
	 */
	public Restaurant(String json) throws InvalidInputException, ArrayIndexOutOfBoundsException {
		// storing open status
		Pattern openPat = Pattern.compile("\"open\": (.*?), \"url\": ");
		Matcher openMat = openPat.matcher(json);
		if (openMat.find()) {
			this.open = Boolean.parseBoolean(openMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find open status");
			throw new InvalidInputException();
		}
		// storing url
		Pattern urlPat = Pattern.compile("\"url\": \"(.*?)\", \"longitude\": ");
		Matcher urlMat = urlPat.matcher(json);
		if (urlMat.find()) {
			this.url = urlMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find URL");
			throw new InvalidInputException();
		}
		// storing longPat
		this.location = new Location(); //create empty location object to store stuff in
		Pattern longPat = Pattern.compile("\"longitude\": (.*?), \"neighborhoods\": ");
		Matcher longMat = longPat.matcher(json);
		if (longMat.find()) {
			this.location.setLongitude(Double.parseDouble(longMat.group(1)));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find longitude");
			throw new InvalidInputException();
		}
		// storing neighborhood
		String neighStr = null;
		Pattern neighPat = Pattern.compile("\"neighborhoods\": \\[(.*?)\\], \"business_id\": ");
		Matcher neighMat = neighPat.matcher(json);
		if (neighMat.find()) {
			neighStr = neighMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find neighborhood");
			throw new InvalidInputException();
		}
		String[] neighArr = neighStr.split(", ");
		for (int i = 0; i < neighArr.length; i++) {
			this.location.getNeighborhoods().add(neighArr[i].replaceAll("\"", ""));
		}
		// storing businessID
		Pattern businessIDpat = Pattern.compile("\"business_id\": \"(.*?)\", \"name\": ");
		Matcher businessIDmat = businessIDpat.matcher(json);
		if (businessIDmat.find()) {
			this.businessID = businessIDmat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find business ID");
			throw new InvalidInputException();
		}
		// storing name
		Pattern namePat = Pattern.compile("\"name\": \"(.*?)\", \"categories\": ");
		Matcher nameMat = namePat.matcher(json);
		if (nameMat.find()) {
			this.name = nameMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find name");
			throw new InvalidInputException();
		}
		// storing categories
		String catStr = null;
		Pattern catPat = Pattern.compile("\"categories\": \\[(.*?)\\], \"state\": ");
		Matcher catMat = catPat.matcher(json);
		if (catMat.find()) {
			catStr = catMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find categories");
			throw new InvalidInputException();
		}
		String[] catArr = catStr.split("\", \"");
		this.categories = new ArrayList<String>();
		for (String s : catArr) {
			this.categories.add(s.replaceAll("\"", ""));
		}
		// storing state
		Pattern statePat = Pattern.compile("\"state\": \"(.*?)\", \"type\": ");
		Matcher stateMat = statePat.matcher(json);
		if (stateMat.find()) {
			this.location.setState(stateMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find state");
			throw new InvalidInputException();
		}
		// storing stars
		Pattern starsPat = Pattern.compile("\"stars\": (.*?), \"city\": ");
		Matcher starsMat = starsPat.matcher(json);
		if (starsMat.find()) {
			this.stars = Double.parseDouble(starsMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find stars");
			throw new InvalidInputException();
		}
		// storing city
		Pattern cityPat = Pattern.compile("\"city\": \"(.*?)\", \"full_address\": ");
		Matcher cityMat = cityPat.matcher(json);
		if (cityMat.find()) {
			this.location.setCity(cityMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find city");
			throw new InvalidInputException();
		}
		// storing fullAddress
		String fullAddrStr = null;
		Pattern fullAddrPat = Pattern.compile("\"full_address\": \"(.*?)\", \"review_count\": ");
		Matcher fullAddrMat = fullAddrPat.matcher(json);
		if (fullAddrMat.find()) {
			fullAddrStr = fullAddrMat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find full address");
			throw new InvalidInputException();
		}
		String[] fullAddrArr = fullAddrStr.split(Pattern.quote("\\n"));
		String[] addr1 = null;
		String[] addr2 = null;
		String[] addr3 = null;
		String[] addr4 = null;
		String[] addr5 = null;
		try {
			if (fullAddrArr.length == 5) {
				this.location.setRoom(fullAddrArr[0]);
				this.location.setBuilding(fullAddrArr[1]);
				this.location.setStreet(fullAddrArr[2]);
				this.location.setArea(fullAddrArr[3]);
				addr5 = fullAddrArr[4].split(" ");
				this.location.setAreaCode(addr5[addr5.length - 1]);
			} else if (fullAddrArr.length == 4) {
				addr1 = fullAddrArr[0].split(" ");
				this.location.setStreetAddress(addr1[0]);
				this.location.setStreet(fullAddrArr[0].replaceAll(addr1[0], ""));
				this.location.setSuite(fullAddrArr[1]);
				this.location.setArea(fullAddrArr[2]);
				addr4 = fullAddrArr[3].split(" ");
				this.location.setAreaCode(addr4[addr4.length - 1]);
			} else if (fullAddrArr.length == 3) {
				addr1 = fullAddrArr[0].split(" ");
				this.location.setStreetAddress(addr1[0]);
				this.location.setArea(fullAddrArr[1]);
				this.location.setStreet(fullAddrArr[0].replaceAll(addr1[0], ""));
				addr3 = fullAddrArr[2].split(" ");
				this.location.setAreaCode(addr3[addr3.length - 1]);
			} else if (fullAddrArr.length == 2) {
				addr1 = fullAddrArr[0].split(" ");
				this.location.setArea(fullAddrArr[0]);
				this.location.setStreet("no street");
				addr2 = fullAddrArr[1].split(" ");
				this.location.setAreaCode(addr2[addr2.length - 1]);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter restaurant info in valid JSON format:\nCould not find full address");
			throw new InvalidInputException();
		}
		// storing reviewCount
		Pattern reviewCountPat = Pattern.compile("\"review_count\": (.*?), \"photo_url\": ");
		Matcher reviewCountMat = reviewCountPat.matcher(json);
		if (reviewCountMat.find()) {
			this.reviewCount = Integer.parseInt(reviewCountMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find review count");
			throw new InvalidInputException();
		}
		// storing photoURL
		Pattern photoURLpat = Pattern.compile("\"photo_url\": \"(.*?)\", \"schools\": ");
		Matcher photoURLmat = photoURLpat.matcher(json);
		if (photoURLmat.find()) {
			this.photoURL = photoURLmat.group(1);
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find photo url");
			throw new InvalidInputException();
		}
		// storing schools
		Pattern schoolsPat = Pattern.compile("\"schools\": \\[\"(.*?)\"\\], \"latitude\": ");
		Matcher schoolsMat = schoolsPat.matcher(json);
		String[] schoolArr = null;
		if (schoolsMat.find()) {
			schoolArr = schoolsMat.group(1).split(", ");
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find schools");
			throw new InvalidInputException();
		}
		for (String s : schoolArr) {
			this.location.getSchools().add(s.replaceAll("\"", ""));
		}
		// storing latitude
		Pattern latPat = Pattern.compile("\"latitude\": (.*?), \"price\": ");
		Matcher latMat = latPat.matcher(json);
		if (latMat.find()) {
			this.location.setLatitude(Double.parseDouble(latMat.group(1)));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find latitude");
			throw new InvalidInputException();
		}
		// storing price
		Pattern pricePat = Pattern.compile("\"price\": (.*?)}");
		Matcher priceMat = pricePat.matcher(json);
		if (priceMat.find()) {
			this.price = Integer.parseInt(priceMat.group(1));
		} else {
			System.out.println("Please enter Yelp User info in valid JSON format:\nCould not find price");
			throw new InvalidInputException();
		}
	}
	/**
	 * A method to return this business' info in JSON format
	 * @return
	 * 		String representing business' info in JSON
	 */
	public String getJSON() {
		String json = "{\"open\": " + this.open + ", \"url\": \"" + this.url + "\", \"longitude\": "
				+ this.location.getLongitude() + ", \"neighborhoods\": [";
		for (int i = 0; i < this.location.getNeighborhoods().size(); i++) {
			json += "\"" + this.location.getNeighborhoods().get(i) + "\"";
			if (i < this.location.getNeighborhoods().size() - 1) {
				json += ", ";
			}
		}
		json += "], \"business_id\": \"" + this.businessID + "\", \"name\": \"" + this.name + "\", \"categories\": [";
		for (int i = 0; i < this.categories.size(); i++) {
			json += "\"" + this.categories.get(i) + "\"";
			if (i < this.categories.size() - 1) {
				json += ", ";
			}
		}
		json += "], \"state\": \"" + this.location.getState() + "\", \"type\": \"business\", \"stars\": " + this.stars
				+ ", \"city\": \"" + this.location.getCity() + "\", \"full_address\": \""
				+ this.location.getFullAddress() + ", \"review_count\": " + this.reviewCount + ", \"photo_url\": \""
				+ this.photoURL + "\", \"schools\": [";
		for (int i = 0; i < this.location.getSchools().size(); i++) {
			json += "\"" + this.location.getSchools().get(i) + "\"";
			if (i < this.location.getSchools().size() - 1) {
				json += ", ";
			}
		}
		json += "], \"latitude\": " + this.location.getLatitude() + ", \"price\": " + this.price + "}";
		return json;
	}
	
	/**
	 * A method that returns the ID of this business
	 * @return
	 * 		String representing this business' ID
	 */
	public String getBusinessID() {
		return this.businessID;
	}
	/**
	 * A method that returns the name of this business
	 * @return
	 * 		String representing this business' name
	 */
	public String getBusinessName() {
		return this.name;
	}
	
	/**
	 * A setter method that sets the name of this business, and updates the url accordingly
	 * @param name
	 * 		String representing new name of this restaurant
	 */
	public void setName(String name) {
		this.name = name;
		this.url = "http://www.yelp.com/biz/" + name.replaceAll(" ", "-");
	}
	
	/**
	 * A getter method that returns the url of this business
	 * @return
	 * 		String representing url of this business
	 */
	public String getURL() {
		return this.url;
	}

	/**
	 * A setter method that sets the url of thisd business
	 * @param url
	 * 		new url of this business
	 */
	public void setURL(String url) {
		this.url = url;
	}
	
	/**
	 * A getter method that returns the price of this business
	 * @return
	 * 		relative Integer price of this business between 1 and 5
	 */
	public Integer getPrice() {
		return this.price;
	}

	/**
	 * A setter method that sets the price of this business
	 * @param price
	 * 		new relative price of this business, must be between 1 and 5
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}

	/**
	 * A getter method to return Location object representing this business' location.
	 * Also allows client to set location fields
	 * @return
	 * 		Location of this business
	 */
	public Location getLocation() {
		return this.location;
	}
	
	/**
	 * A getter method to 
	 * @return
	 */
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
