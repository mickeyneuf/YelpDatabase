package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.function.ToDoubleBiFunction;

import org.junit.Test;
import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.PredictorFunction;
import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantNotFoundException;
import ca.ece.ubc.cpen221.mp5.ReviewNotFoundException;
import ca.ece.ubc.cpen221.mp5.UserNotFoundException;
import ca.ece.ubc.cpen221.mp5.YelpDb;

public class PredictorFunctionTest {

	/*
	 * Testing that the correct prediction is generated for a given restaurant when they entire database is used
	 * 
	 */
	@Test
	public void test0() throws IOException, InvalidInputException, RestaurantNotFoundException, ReviewNotFoundException, UserNotFoundException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		String userID = "1mdwR2US8Z_CBWYuFMWWNg";
		String restID = "1CBs84C-a-cuA3vncXVSAw";
		String reviewID = "0hrSPeBHXvtMV4aY0jzgPg";
 
		yelp.removeRestaurant(restID);
		
		assertEquals(false, yelp.containsRestaurant(restID));
		assertEquals(false, yelp.getReviewsUser(userID).contains(reviewID));

		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);

		assertTrue(func.applyAsDouble(yelp, restID) == 3);

	}

	
	/*
	 * Testing that the correct prediction is generated for a small linear subset
	 * 
	 */
	@Test
	public void test1() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest1.json", "data/reviewsTest1.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "loBOs5ruFXSNL-ZM29cTrA";

		assertTrue(func.applyAsDouble(yelp, restID) == 2);
	}

	/*
	 * Testing that the prediction is 1 if the data suggests it should be less than 1
	 * 
	 * 
	 */
	@Test
	public void test2() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest2.json", "data/reviewsTest2.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "rcu96dLkUGU4Naj7GqOHDQ";

		assertTrue(func.applyAsDouble(yelp, restID) == 1);

	}

	
	/*
	 * Testing that the prediction is 5 if the data suggests it should be more than 5 
	 * 
	 * 
	 */
	@Test
	public void test3() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest3.json", "data/reviewsTest3.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "rcu96dLkUGU4Naj7GqOHDQ";

		assertTrue(func.applyAsDouble(yelp, restID) == 5);
	}

	/*
	 * Testing that an exception is thrown if the user has one or fewer reviews
	 * 
	 */
	@Test
	public void test4() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest2.json", "data/restaurantsTest1.json", "data/reviewsTest1.json");
		String userID0 = "VrInnExP20XjZ9QwyyZfvg";
		String userID1 = "NXyhjM2ijeXCq709ngeEcw";

		try {
			yelp.getPredictorFunction(userID0);
			fail("We expect an exception");
		} catch (Exception e) {
			// we expect an exception to be thrown here
		}
		try {
			yelp.getPredictorFunction(userID1);
			fail("We expect an exception");
		} catch (Exception e) {
			// we expect an exception to be thrown here
		}
	}

	/*
	 * A sanity check
	 * 
	 */
	@Test
	public void test5() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest3.json", "data/reviewsTest3.json");
		String userID = "_NH7Cpq3qZkByP5xR4gXog";
		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(userID);
		String restID = "rcu96dLkUGU4Naj7GqOHDQ";

		assertTrue(func.applyAsDouble(yelp, restID) == 5);
	}
	
	
	/*
	 * Another sanity check
	 * 
	 */
	@Test
	public void test6() throws IOException, InvalidInputException {
		YelpDb test = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		ToDoubleBiFunction<MP5Db<Restaurant>, String> i = test.getPredictorFunction("Djk49JjpKl9HQNpmiX669Q");
		assertTrue(i.applyAsDouble(test, "TUIDRJ_rUkdmYPSRAAEsPg") == 3.166666666666667);
	}
}
