package ca.ece.ubc.cpen221.mp5;
/*
 * A generic interface representing a business in a database. 
 * eg. restaurant
 */
public interface Business {
/**
 * A method that returns the ID of this business
 * @return
 * 		String representing this business' ID
 */
String getBusinessID();

/**
 * A method that returns the name of this business
 * @return
 * 		String representing this business' name
 */
String getBusinessName();

/**
 * A method that returns the url of this business
 * @return
 * 		String representing business url
 */
String getURL();

/**
 * A method to return this business' info in JSON format
 * @return
 * 		String representing business' info in JSON
 */
String getJSON();

/**
 * A method that returns the location of this business
 * @return 
 * 		Location object representing this business' location
 */
Location getLocation();

/**
 * A method that returns true if this business is open and false otherwise
 * @return
 * 		A method that returns true if this business is open and false otherwise
 */
boolean isOpen();

/**
 * A method to check equality of this Business with another object
 * Two Businesses are defined as equal if their ID is equal
 * @param
 * 		other object to be compared
 * @return
 * 		true if this is equal to other, false otherwise
 */
@Override
boolean equals(Object other);

/**
 * A method to return hashCode of this business
 * A business' hashCode is represented by the sum of the numeric values
 * of the characters in its business ID
 * @return
 * 		int representing sum of numerical values of characters in this business' ID
 */		
@Override
int hashCode();


}
