package ca.ece.ubc.cpen221.mp5;

public class Restaurant implements Business {

	private String json;
	private String businessID;
	private String name;
	private String url;
	private Integer price;
	private Location location;

	public Restaurant(Integer businessID, String latitude, String longitude) {

		this.businessID = businessID.toString();
		this.location = new Location(longitude, latitude);
	}

	public Restaurant(String json) {
		this.json = json;
	}

	@Override
	public String getBusinessID() {
		return businessID;
	}

	@Override
	public String getBusinessName() {
		return name;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Integer getPrice() {
		return price;
	}

}
