package ca.ece.ubc.cpen221.mp5;
/*
 * This is a generic interface to represent a review for a service, business or purchase
 * Examples: Yelp, Amazon, RottenTomatoes
 */
public interface Review {
	/**
	 * A method to return the url of this review
	 * @return
	 * 		String representing url of this review
	 */		
	String getURL();
	
	/**
	 * A method to return the ID of the poster of this review
	 * @return
	 * 		String representing ID of review poster
	 */	
	String getPoster();

	/**
	 * A method that returns the ID of the thing being reviewed
	 * eg. business, product etc.
	 * @return
	 * 		String representing ID of reviewed thing
	 */
	String getReviewed();
	
	/**
	 * A method that returns a string representing the text in this review
	 * @return
	 * 		String of text in this review
	 */		
	String getText();
	
	/**
	 * A method that returns string representation of date review was posted
	 * @return
	 * 		String of date of review posting
	 */
	String getDate();
	
	/**
	 * A method to check equality of this Review with another object
	 * Two reviews are defined as equal if their url is equal
	 * @param
	 * 		other object to be compared
	 * @return
	 * 		true if this is equal to other, false otherwise
	 */
	boolean equals(Object other);
	
}
