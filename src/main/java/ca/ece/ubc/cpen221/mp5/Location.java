package ca.ece.ubc.cpen221.mp5;

import java.util.ArrayList;

/**
 * 
 * This class represents a location. At the minimum, it must have a longitude
 * and latitude All other fields initialized to null and can be set
 *
 */
public class Location extends Object {
	private Double longitude;
	private Double latitude;
	private String state;
	private String city;
	private ArrayList<String> neighborhoods;
	private ArrayList<String> schools;
	private String streetAddress;
	private String street;
	private String area;
	private String areaCode;
	
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
	}
	
	public Location(Double longitude, Double latitude, String state, String city, String neighborhood, String school,
					String streetAddress, String street, String area, String areaCode) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.state = state;
		this.city = city;
		this.neighborhoods = new ArrayList<String>();
		this.neighborhoods.add(neighborhood);
		this.schools = new ArrayList<String>();
		this.schools.add(school);
		this.streetAddress = streetAddress;
		this.street = street;
		this.area = area;
		this.areaCode = areaCode;
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

	public ArrayList<String> getSchools() {
		return schools;
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
		return this.streetAddress+""+this.street+"\\n"+this.area+"\\n"+this.city+", "+this.state+" "+this.areaCode+"\"";

	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Location) {
			return(((Location) other).getLongitude().equals(this.longitude)&&
				   ((Location) other).getLatitude().equals(this.latitude)&&
				   ((Location) other).getState().equals(this.state)&&
				   ((Location) other).getCity().equals(this.city)&&
				   ((Location) other).getNeighborhoods().equals(this.neighborhoods)&&
				   ((Location) other).getSchools().equals(this.schools));
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.longitude.hashCode();
	}
}
