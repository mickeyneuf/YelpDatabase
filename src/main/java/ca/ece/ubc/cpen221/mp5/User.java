package ca.ece.ubc.cpen221.mp5;

/* 
 * This is a generic interface to represent a User of a web service or forum.
 */
public interface User {
	
	/**
	 * A method to return this user's ID
	 * @return
	 * 		String representing User's ID
	 */
	String getUserID();
	
	/**
	 * A method to return this user's username
	 * @return 
	 * 		String representing User's username
	 */
	String getUserName();
	
	/**
	 * A method to return this user's url
	 * @return
	 * 		String representing User's url
	 */
	String getURL();
	
	/**
	 * A method to return this user's info in JSON format
	 * @return
	 * 		String representing user info in JSON
	 */
	String getJSON();
	
	/**
	 * A method to check equality of this User with another object
	 * Two users are defined as equal if their ID is equal
	 * @param
	 * 		other object to be compared
	 * @return
	 * 		true if this is equal to other, false otherwise
	 */
	@Override
	boolean equals(Object other);
	
}
