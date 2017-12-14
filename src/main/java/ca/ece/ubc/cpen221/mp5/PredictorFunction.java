package ca.ece.ubc.cpen221.mp5;

import java.util.function.ToDoubleBiFunction;

public class PredictorFunction implements ToDoubleBiFunction<MP5Db<Restaurant>, String> {

	/**
	 * A PredictorFunction takes in two parameters, a and b. It then applies these
	 * parameters to the price of a restaurant from the specified database to
	 * predict the rating a user may give this restaurant, based on its price. The
	 * equation is: rating = b * price + a
	 * 
	 */

	private double a;
	private double b;

	/**
	 * @param a
	 *            a double that will be the y intercept of our function
	 * @param b
	 *            a double that will be multiplied by price in our function
	 * 
	 * 
	 *            The function that is created is of the form: rating = b * price +
	 *            a
	 * 
	 * @throws IllegalArgumentException
	 *             if both a and b are zero. This will happen if the user we are
	 *             predicting the rating of has one or fewer reviews, so an accurate
	 *             prediction cannot be made
	 */
	public PredictorFunction(double a, double b) {
		if (a == 0 && b == 0) {
			throw new IllegalArgumentException();
		}
		this.a = a;
		this.b = b;
	}

	/**
	 * @param database
	 *            An MP5Db<Restaurant> that contains the restaurant we wish to
	 *            predict the rating of
	 * @param businessID
	 *            The business ID of the restaurant we wish to predict the rating of
	 * 
	 * @return a double which is the predicted rating the user will give to the
	 *         restaurant, as computed by our function. The function will return 1
	 *         if the predicted rating is less than 1 and will return 5 if the
	 *         predicted rating is above 5.
	 * 
	 */
	@Override
	public double applyAsDouble(MP5Db<Restaurant> database, String businessID) {
		int price = 0;
		if (database instanceof YelpDb) {
			Restaurant rest;
			try {
				rest = ((YelpDb) database).getRestaurant(businessID);
				price = rest.getPrice();
			} catch (RestaurantNotFoundException e) {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
		double rating = b * price + a;
		if (rating > 5) {
			rating = 5;
		}
		if (rating < 1) {
			rating = 1;
		}
		return rating;
	}
}