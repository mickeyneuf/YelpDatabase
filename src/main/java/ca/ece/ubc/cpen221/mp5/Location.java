package ca.ece.ubc.cpen221.mp5;

/**
 * 
 * This class represents a location. At the minimum, it must have a longitude
 * and latitude All other fields initialized to null and can be set
 *
 */
public class Location extends Object {
	private String longitude;
	private String latitude;
	private String state;
	private String city;
	private String neighborhood;
	private String school;

	public Location() {
		this.longitude = "no longitude";
		this.latitude = "no latitude";
		this.state = "no state";
		this.city = "no city";
		this.neighborhood = "no neighborhood";
		this.school = "no school";
	}
	
	public Location(String longitude, String latitude, String state, String city, String neighborhood, String school) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.state = state;
		this.city = city;
		this.neighborhood = neighborhood;
		this.school = school;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
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

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	@Override
	public boolean equals(Object other) {
		if (other instanceof Location) {
			return(((Location) other).getLongitude().equals(this.longitude)&&
				   ((Location) other).getLatitude().equals(this.latitude)&&
				   ((Location) other).getState().equals(this.state)&&
				   ((Location) other).getCity().equals(this.city)&&
				   ((Location) other).getNeighborhood().equals(this.neighborhood)&&
				   ((Location) other).getSchool().equals(this.school));
		}
		return false;
	}
	@Override
	public int hashCode() {
		return Integer.parseInt(this.longitude);
	}
}
