package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.PredictorFunction;
import ca.ece.ubc.cpen221.mp5.YelpDb;


public class PredictorFunctionTest {

	/*
	 * @Test public void test0() throws IOException { YelpDb yelp = new
	 * YelpDb("data/restaurants.json", "data/reviews.json", "data/users.json");
	 * String userID = "wr3JF-LruJ9LBwQTuw7aUg"; String restID =
	 * "1CBs84C-a-cuA3vncXVSAw";
	 * 
	 * yelp.removeRestaurant(restID);
	 * 
	 * PredictorFunction func = (PredictorFunction)
	 * yelp.getPredictorFunction(userID);
	 * 
	 * assertTrue(func.applyAsDouble(yelp, restID) == 3);
	 * 
	 * }
	 */

	@Test
	public void test1() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest1.json", "data/reviewsTest1.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "loBOs5ruFXSNL-ZM29cTrA";

		assertTrue(func.applyAsDouble(yelp, restID) == 2);
	}

	@Test
	public void test2() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest2.json", "data/reviewsTest2.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "rcu96dLkUGU4Naj7GqOHDQ";

		assertTrue(func.applyAsDouble(yelp, restID) == 1);

	}

	@Test
	public void test3() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest3.json", "data/reviewsTest3.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "rcu96dLkUGU4Naj7GqOHDQ";

		assertTrue(func.applyAsDouble(yelp, restID) == 5);
	}

	@Test
	public void test4() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest2.json", "data/restaurantsTest1.json", "data/reviewsTest1.json");
		String userID0 = "VrInnExP20XjZ9QwyyZfvg";
		String userID1 = "NXyhjM2ijeXCq709ngeEcw";

		try {
			yelp.getPredictorFunction(userID0);
		} catch (Exception e) {
			// we expect an exception to be thrown here
		}
		try {
			yelp.getPredictorFunction(userID1);
		} catch (Exception e) {
			// we expect an exception to be thrown here
		}
	}
	
	@Test
	public void test5() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest3.json", "data/reviewsTest3.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "rcu96dLkUGU4Naj7GqOHDQ";

		assertTrue(func.applyAsDouble(yelp, restID) == 5);
	}
}