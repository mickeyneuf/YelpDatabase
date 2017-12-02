package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	

	public Restaurant(Integer businessID, String name) {
		this.businessID = businessID.toString();
		this.open=false;
		this.location = new Location();
		this.name = name;
		this.url = "http://www.yelp.com/biz/"+businessID.toString();
		this.price = 0;
		this.categories = new ArrayList<String>();
		this.photoURL="no photo";
	}

	public Restaurant(String json) {
		Pattern openPat = Pattern.compile("\"open\": (.*?), \"url\": ");
		Matcher openMat = openPat.matcher(json);
		openMat.find();
		this.open = Boolean.parseBoolean(openMat.group(1));
		
		Pattern urlPat = Pattern.compile("\"url\": \"(.*?)\", \"longitude\": ");
		Matcher urlMat = urlPat.matcher(json);
		urlMat.find();
		this.url = urlMat.group(1);
		
		this.location = new Location();
		Pattern longPat = Pattern.compile("\"longitude\": (.*?), \"neighborhoods\": ");
		Matcher longMat = longPat.matcher(json);
		longMat.find();
		this.location.setLongitude(Double.parseDouble(longMat.group(1)));
		
		String neighStr = null;
		Pattern neighPat = Pattern.compile("\"neighborhoods\": \\[(.*?)\\], \"business_id\": ");
		Matcher neighMat = neighPat.matcher(json);
		neighMat.find();
		neighStr = neighMat.group(1);
		String[] neighArr = neighStr.split(", ");
		for(int i = 0; i<neighArr.length; i++) {
			this.location.getNeighborhoods().add(neighArr[i].replaceAll("\"", ""));
		}
		
		Pattern businessIDpat = Pattern.compile("\"business_id\": \"(.*?)\", \"name\": ");
		Matcher businessIDmat = businessIDpat.matcher(json);
		businessIDmat.find();
		this.businessID = businessIDmat.group(1);
		
		Pattern namePat = Pattern.compile("\"name\": \"(.*?)\", \"categories\": ");
		Matcher nameMat = namePat.matcher(json);
		nameMat.find();
		this.name = nameMat.group(1);
		
		String catStr = null;
		Pattern catPat = Pattern.compile("\"categories\": \\[(.*?)\\], \"state\": ");
		Matcher catMat = catPat.matcher(json);
		catMat.find();
		catStr = catMat.group(1);
		
		String[] catArr = catStr.split(", ");
		this.categories = new ArrayList<String>();
		for(String s : catArr) {
			this.categories.add(s.replaceAll("\"", ""));
		}
		
		Pattern statePat = Pattern.compile("\"state\": \"(.*?)\", \"type\": ");
		Matcher stateMat = statePat.matcher(json);
		stateMat.find();
			this.location.setState(stateMat.group(1));
		
		Pattern starsPat = Pattern.compile("\"stars\": (.*?), \"city\": ");
		Matcher starsMat = starsPat.matcher(json);
		starsMat.find();
			this.stars = Double.parseDouble(starsMat.group(1));
		
		Pattern cityPat = Pattern.compile("\"city\": \"(.*?)\", \"full_address\": ");
		Matcher cityMat = cityPat.matcher(json);
		cityMat.find();
		this.location.setCity(cityMat.group(1));
		
		String fullAddrStr = null;
		Pattern fullAddrPat = Pattern.compile("\"full_address\": \"(.*?)\", \"review_count\": ");
		Matcher fullAddrMat = fullAddrPat.matcher(json);
		fullAddrMat.find();
		fullAddrStr = fullAddrMat.group(1);
		
		System.out.println(fullAddrStr);
		
		String[] fullAddrArr = fullAddrStr.split(Pattern.quote("\\n"));
		String[] addr1 = fullAddrArr[0].split(" ");
		this.location.setStreetAddress(addr1[0]);
		this.location.setArea(fullAddrArr[1]);
		this.location.setStreet(fullAddrArr[0].replaceAll(addr1[0], ""));
		String[] addr3 = fullAddrArr[2].split(" ");
		
		String areaCode = addr3[addr3.length-1];
		this.location.setAreaCode(areaCode);
		
		Pattern reviewCountPat = Pattern.compile("\"review_count\": (.*?), \"photo_url\": ");
		Matcher reviewCountMat = reviewCountPat.matcher(json);
		reviewCountMat.find();
		this.reviewCount = Integer.parseInt(reviewCountMat.group(1));
		reviewCountMat.find();
		
		Pattern photoURLpat = Pattern.compile("\"photo_url\": \"(.*?)\", \"schools\": ");
		Matcher photoURLmat = photoURLpat.matcher(json);
		photoURLmat.find();
		this.photoURL = photoURLmat.group(1);
		
		Pattern schoolsPat = Pattern.compile("\"schools\": \\[\"(.*?)\"\\], \"latitude\": ");
		Matcher schoolsMat = schoolsPat.matcher(json);
		schoolsMat.find();
		String[] schoolArr = schoolsMat.group(1).split(", ");
		for (String s : schoolArr) {
			this.location.getSchools().add(s.replaceAll("\"", ""));
		}
		
		Pattern latPat = Pattern.compile("\"latitude\": (.*?), \"price\": ");
		Matcher latMat = latPat.matcher(json);
		latMat.find();
		this.location.setLatitude(Double.parseDouble(latMat.group(1)));
		
		Pattern pricePat = Pattern.compile("\"price\": (.*?)}");
		Matcher priceMat = pricePat.matcher(json);
		priceMat.find();
		this.price = Integer.parseInt(priceMat.group(1));
	}

	public String getJSON() {
		String json = "{\"open\": "+this.open+", \"url\": \""+this.url+"\", \"longitude\": "+this.location.getLongitude()
					  +", \"neighborhoods\": [";
		for(int i = 0; i < this.location.getNeighborhoods().size(); i++) {
			json+= "\"" + this.location.getNeighborhoods().get(i) + "\"";
			if(i<this.location.getNeighborhoods().size()-1) {
				json+=", ";
			}
		}
		json+= "], \"business_id\": \"" + this.businessID + "\", \"name\": \""+this.name+"\", \"categories\": [";
		for(int i = 0; i < this.categories.size(); i++) {
			json+= "\"" + this.categories.get(i)+"\"";
			if(i<this.categories.size()-1) {
				json+= ", ";
			}
		}
		json+="], \"state\": \""+this.location.getState()+"\", \"type\": \"business\", \"stars\": "+this.stars+", \"city\": \""
				+this.location.getCity()+"\", \"full_address\": \""+this.location.getFullAddress()+", \"review_count\": "
				+this.reviewCount+", \"photo_url\": \""+this.photoURL+"\", \"schools\": [";
		for(int i = 0; i < this.location.getSchools().size(); i++) {
			json+= "\""+this.location.getSchools().get(i)+"\"";
			if(i<this.location.getSchools().size()-1) {
				json+= ", ";
			}
		}
		json+= "], \"latitude\": "+this.location.getLatitude()+", \"price\": "+this.price+"}";
		return json;
	}
	
	public String getBusinessID() {
		return businessID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.url = "http://www.yelp.com/biz/"+name.replaceAll(" ", "-");
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
