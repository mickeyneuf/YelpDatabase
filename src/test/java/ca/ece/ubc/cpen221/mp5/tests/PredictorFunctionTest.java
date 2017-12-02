package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.PredictorFunction;
import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpUser;

public class PredictorFunctionTest {

	/*@Test
	public void test0() throws IOException {
		YelpDb yelp = new YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");
		String userID = "wr3JF-LruJ9LBwQTuw7aUg";
		String restID = "1CBs84C-a-cuA3vncXVSAw";

		yelp.removeRestaurant(restID);

		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);

		assertTrue(func.applyAsDouble(yelp, restID) == 3);

	}*/
	
	
	@Test
	public void test1() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/restaurantsTest.json", "data/reviewsTest.json", "data/usersTest.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "loBOs5ruFXSNL-ZM29cTrA";
		
		assertTrue(func.applyAsDouble(yelp, restID) == 2);
	}

}
