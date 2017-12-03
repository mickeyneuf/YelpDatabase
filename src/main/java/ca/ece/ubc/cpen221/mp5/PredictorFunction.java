package ca.ece.ubc.cpen221.mp5;

import java.util.Set;
import java.util.function.ToDoubleBiFunction;

public class PredictorFunction implements ToDoubleBiFunction<MP5Db<Restaurant>, String> {

	private double a;
	private double b;

	
	/**
	 * @param a 
	 *           a double that will be the y intercept of our function
	 * @param b
	 *           a double that will be multiplied by price in our function
	 *           
	 *           
	 * The function that is created is of the form: rating = b * price + a
	 * 
	 * @throws IllegalArgumentException if both a and b are zero. This will happen if 
	 *                                  the user we are predicting the rating of has 
	 *                                  one or fewer reviews, so an accurate prediction 
	 *                                  cannot be made
	 */
	public PredictorFunction(double a, double b) {
		if (a == 0 && b == 0) {
			throw new IllegalArgumentException();
		}
		this.a = a;
		this.b = b;
	}

	
	/**
	 * @param t 
	 *          An MP5Db<Restaurant> that contains the restaurant we wish to predict the rating of
	 * @param u 
	 *          The business ID of the restaurant we wish to predict the rating of
	 * 
	 * @return 
	 *          a double which is the predicted rating the user will give to the restaurant, as 
	 *          computed by our function. The function will return 1 if the predicted rating is
	 *          less than 1 and will return 5 if the predicted rating is above 5.
	 * 
	 */
	@Override
	public double applyAsDouble(MP5Db<Restaurant> t, String u) {
		int price = 0;
		if (t instanceof YelpDb) {
			Set<?> rest = ((YelpDb) t).getMatches(u);
			for (Object o : rest) {
				if (o instanceof Restaurant) {
					price = ((Restaurant) o).getPrice();
				}
				else {
					throw new IllegalArgumentException();
				}
			}
		}
		else {
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