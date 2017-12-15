package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class represents a location. It can be created as empty, and have its fields filled with
 * its methods, or it can be created with parameters representing all of its fields.
 *
 */
public class Location {
	private double longitude;
	private double latitude;
	private String state;
	private String city;
	private ArrayList<String> neighborhoods;
	private ArrayList<String> schools;
	private String streetAddress;
	private String street;
	private String area;
	private String areaCode;
	private String suite;
	private String building;
	private String room;
	
	
	
	public Location() {
		this.longitude = 0.0;
		this.latitude = 0.0;
		this.state = "no state";
		this.city = "no city";
		this.neighborhoods = new ArrayList<String>();
		this.schools = new ArrayList<String>();
		this.streetAddress = "no street address";
		this.street = "no street";
		this.area = "no area";
		this.areaCode = "no area code";
		this.suite = "no suite";
		this.building = "no building";
		this.room = "no room";
	}
	
	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ArrayList<String> getNeighborhoods() {
		return neighborhoods;
	}
	
	public void setNeighborhoods(List<String> neighborhoods) {
		this.neighborhoods = (ArrayList<String>) neighborhoods;
	}

	public ArrayList<String> getSchools() {
		return schools;
	}
	
	public void setSchools(List<String> schools) {
		this.schools = (ArrayList<String>) schools;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getFullAddress() {
		String json = "";
		if(!this.room.equals("no room")) {
			json+=this.room+"\\n";
		}
		if (!this.building.equals("no building")) {
			json+=this.building+"\\n";
		}
		if (!this.streetAddress.equals("no street address")){
			json+=this.streetAddress;
		}
		if(!this.street.equals("no street")) {
			json+=this.street;
		}
		if (!this.suite.equals("no suite")) {
			json+="\\n"+this.suite;
		}
		if(!json.isEmpty()) {
			json+="\\n";
		}
		json+=this.area+"\\n";
		return json+this.city+", "+this.state+" "+this.areaCode+"\"";
	}
	
	public String getSuite() {
		return this.suite;
	}
	
	public void setSuite(String suite) {
		this.suite=suite;
	}
	@Override
	public boolean equals(Object other) {
		if (other instanceof Location) {
			return(((Location) other).getLongitude().equals(this.longitude)&&
				   ((Location) other).getLatitude().equals(this.latitude)&&
				   ((Location) other).getState().equals(this.state)&&
				   ((Location) other).getCity().equals(this.city)&&
				   ((Location) other).getNeighborhoods().equals(this.neighborhoods)&&
				   ((Location) other).getSchools().equals(this.schools))&&
				   ((Location) other).getFullAddress().equals(this.getFullAddress());
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.getFullAddress().hashCode();
	}
}
