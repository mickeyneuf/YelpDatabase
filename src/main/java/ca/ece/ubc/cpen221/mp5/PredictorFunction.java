package ca.ece.ubc.cpen221.mp5;

import java.util.Set;
import java.util.function.ToDoubleBiFunction;

public class PredictorFunction implements ToDoubleBiFunction<MP5Db<Restaurant>, String> {

	private double a;
	private double b;

	public PredictorFunction(double a, double b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public double applyAsDouble(MP5Db<Restaurant> t, String u) {
		int price = 0;
		if (t instanceof YelpDb) {
			Set<?> rest = ((YelpDb) t).getMatches(u);
			for (Object o : rest) {
				if (o instanceof Restaurant) {
					price = ((Restaurant) o).getPrice();
				}
			}
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
