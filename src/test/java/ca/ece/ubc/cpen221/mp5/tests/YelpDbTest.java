package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import org.junit.Test;
import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.InvalidQueryException;
import ca.ece.ubc.cpen221.mp5.InvalidRestaurantStringException;
import ca.ece.ubc.cpen221.mp5.InvalidReviewStringException;
import ca.ece.ubc.cpen221.mp5.InvalidUserStringException;
import ca.ece.ubc.cpen221.mp5.NoMatchException;
import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantNotFoundException;
import ca.ece.ubc.cpen221.mp5.Review;
import ca.ece.ubc.cpen221.mp5.ReviewNotFoundException;
import ca.ece.ubc.cpen221.mp5.UserNotFoundException;
import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpReview;
import ca.ece.ubc.cpen221.mp5.YelpUser;
import ca.ece.ubc.cpen221.mp5.YelpVotes;

public class YelpDbTest {

	/**
	 * Testing that the parsers work correctly
	 * 
	 */
	@Test
	public void test0() throws IOException, InvalidInputException {
		Scanner scanUser = new Scanner(new File("data/users.json"));
		Scanner scanRestaurant = new Scanner(new File("data/restaurants.json"));
		Scanner scanReview = new Scanner(new File("data/reviews.json"));
		while (scanUser.hasNext()) {
			String thisLine1 = scanUser.nextLine();
			YelpUser newUser = new YelpUser(thisLine1);
			assertEquals(thisLine1, newUser.getJSON());
		}
		scanUser.close();

		while (scanReview.hasNext()) {
			String thisLine3 = scanReview.nextLine();
			YelpReview newReview = new YelpReview(thisLine3);
			assertEquals(thisLine3, newReview.getJSON());

		}
		scanReview.close();

		while (scanRestaurant.hasNext()) {
			String thisLine2 = scanRestaurant.nextLine();
			Restaurant newRestaurant = new Restaurant(thisLine2);
			assertEquals(thisLine2, newRestaurant.getJSON());
		}
		scanRestaurant.close();

	}

	/*
	 * Testing that we can create new users and that they are equal if they have the
	 * same userID
	 * 
	 */
	@Test
	public void test1() throws InvalidInputException {
		String string = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, \"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}";
		YelpUser user1 = new YelpUser(string);
		YelpUser user2 = new YelpUser(string);

		assertTrue(user1.equals(user2));
	}

	/*
	 * Testing that we can create new restaurants and that they are equal if they
	 * have the same businessID
	 * 
	 */
	@Test
	public void test2() throws InvalidInputException {
		String string = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
		Restaurant rest1 = new Restaurant(string);
		Restaurant rest2 = new Restaurant(string);

		assertTrue(rest1.equals(rest2));
	}

	/*
	 * Testing that we can create new reviews and that they are equal if they have
	 * the same reviewID
	 * 
	 */
	@Test
	public void test3() throws InvalidInputException {
		try {
			String string = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}";
			YelpReview rev1 = new YelpReview(string);
			YelpReview rev2 = new YelpReview(string);
			assertTrue(rev1.equals(rev2));
		} catch (Exception e) {
			fail("no exception expected!");
		}
	}

	/*
	 * Testing that we can return JSON strings for reviews in the correct format
	 * 
	 */
	@Test
	public void test4() {
		try {
			String json = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}";
			YelpReview rev = new YelpReview(json);
			assertEquals(json, rev.getJSON());
		} catch (Exception e) {
			fail("no exception expected!");
		}
	}

	/*
	 * Testing that we can return JSON strings for users and that they are in the
	 * correct format
	 * 
	 */
	@Test
	public void test5() throws InvalidInputException {
		String json = "{\"url\": \"http://www.yelp.com/user_details?userid=zkjy_XoVgR2EFjLjtzFDNw\", \"votes\": {\"funny\": 1, \"useful\": 8, \"cool\": 4}, \"review_count\": 7, \"type\": \"user\", \"user_id\": \"zkjy_XoVgR2EFjLjtzFDNw\", \"name\": \"Elise B.\", \"average_stars\": 3.28571428571429}";
		YelpUser elise = new YelpUser(json);
		assertEquals(json, elise.getJSON());
	}

	@Test
	public void test6() throws InvalidInputException {
		String json = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/peking-express-berkeley\", \"longitude\": -122.2581978, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"1E2MQLWfwpsId185Fs2gWw\", \"name\": \"Peking Express\", \"categories\": [\"Chinese\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.5, \"city\": \"Berkeley\", \"full_address\": \"2516 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\", \"review_count\": 10, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/BPMBr2aiOEpVioLb-RurJQ/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867768, \"price\": 1}";
		Restaurant PekingExpress = new Restaurant(json);
		assertEquals(json, PekingExpress.getJSON());
	}

	/*
	 * Testing that we can remove a user and that they are no longer in the database
	 * 
	 * 
	 */
	@Test
	public void test7() throws IOException, InvalidInputException, UserNotFoundException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		assertEquals(true, yelp.containsRestaurant("gclB3ED6uk6viWlolSb_uA"));
		assertEquals(true, yelp.containsUser("QScfKdcxsa7t5qfE0Ev0Cw"));
		yelp.removeUser("QScfKdcxsa7t5qfE0Ev0Cw");

		assertEquals(false, yelp.containsUser("QScfKdcxsa7t5qfE0Ev0Cw"));
	}

	/*
	 * Testing that we can create a new review without a JSON string and that all of
	 * the fields update correctly
	 * 
	 */
	@Test
	public void test8() throws InvalidInputException {
		YelpReview rev = new YelpReview("BqI8sjcMaGFHM27XEef7DQ", "2014-08-01", "up-pp980QobXgJU4ETTsYw",
				"PC-x4Om-XXGqFn0ludTykw", 5);
		YelpReview rev2 = new YelpReview(
				"{\"type\": \"review\", \"business_id\": \"PC-x43m-XXGqFn0ludTykw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"BqI8sjcMa4929227XEef7DQ\", \"text\": \"I'm rounding up from 2.5 stars since they're making an effort to support sustainable providers and reduce their carbon footprint.  That said, the food was only adequate and most definitely overpriced.  I got the \"two eggs with meat\" breakfast, plus a coffee.  The sausage patties were on the dry side and everything else was just ok, though the home fries were nice and crispy and the herbs were a nice touch.  Nothing really to complain about except for the price of $12.50 + tip.  More expensive than Fat Apples and Sam's Log Cabin and both have better food.  Maybe I should have tried one of the specials or the apparently otherworldly Alameda, but I feel like the standard breakfast plate serves as a pretty good assessment for any cafe like this.\", \"stars\": 3, \"user_id\": \"up-pp980QobXgJU4ETTsYw\", \"date\": \"2010-08-13\"}");
		YelpUser user = new YelpUser(
				"{\"url\": \"http://www.yelp.com/user_details?userid=QScfKdcxsa7t5qfE0Ev0Cw\", \"votes\": {\"funny\": 3, \"useful\": 17, \"cool\": 4}, \"review_count\": 37, \"type\": \"user\", \"user_id\": \"QScfKdcxsa7t5qfE0Ev0Cw\", \"name\": \"Erin C.\", \"average_stars\": 3.83783783783784}");
		rev.setText(
				"I'm rounding up from 2.5 stars since they're making an effort to support sustainable providers and reduce their carbon footprint.  That said, the food was only adequate and most definitely overpriced.  I got the \"two eggs with meat\" breakfast, plus a coffee.  The sausage patties were on the dry side and everything else was just ok, though the home fries were nice and crispy and the herbs were a nice touch.  Nothing really to complain about except for the price of $12.50 + tip.  More expensive than Fat Apples and Sam's Log Cabin and both have better food.  Maybe I should have tried one of the specials or the apparently otherworldly Alameda, but I feel like the standard breakfast plate serves as a pretty good assessment for any cafe like this.");
		rev.setDate("2010-08-13");
		rev.setRating(3);
		rev.setVotes("cool", 0);
		rev.setVotes("useful", 0);
		rev.setVotes("funny", 0);
		rev2.setText("Something shorter");

		assertEquals("2010-08-13", rev2.getDate());
		assertEquals("Something shorter", rev2.getText());
		assertEquals(
				"{\"type\": \"review\", \"business_id\": \"PC-x4Om-XXGqFn0ludTykw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"BqI8sjcMaGFHM27XEef7DQ\", \"text\": \"I'm rounding up from 2.5 stars since they're making an effort to support sustainable providers and reduce their carbon footprint.  That said, the food was only adequate and most definitely overpriced.  I got the \"two eggs with meat\" breakfast, plus a coffee.  The sausage patties were on the dry side and everything else was just ok, though the home fries were nice and crispy and the herbs were a nice touch.  Nothing really to complain about except for the price of $12.50 + tip.  More expensive than Fat Apples and Sam's Log Cabin and both have better food.  Maybe I should have tried one of the specials or the apparently otherworldly Alameda, but I feel like the standard breakfast plate serves as a pretty good assessment for any cafe like this.\", \"stars\": 3, \"user_id\": \"up-pp980QobXgJU4ETTsYw\", \"date\": \"2010-08-13\"}",
				rev.getJSON());
		assertEquals(false, rev.equals(rev2));
		assertEquals(false, rev.equals(user));
		assertEquals(rev.hashCode(), rev.hashCode());

	}

	/*
	 * Testing that we can create a new User without a JSON string and all the
	 * fields update correctly
	 * 
	 */
	@Test
	public void test9() {
		YelpUser user = new YelpUser(123456, "Me");
		assertEquals(
				"{\"url\": \"http://www.yelp.com/user_details?userid=123456\", \"votes\": {}, \"review_count\": 0, \"type\": \"user\", \"user_id\": \"123456\", \"name\": \"Me\", \"average_stars\": 0}",
				user.getJSON());
		user.setURL("http://yelp.com/user_details?userid=nope_im_done_here");
		assertEquals("http://yelp.com/user_details?userid=nope_im_done_here", user.getURL());
		assertEquals("Me", user.getUserName());
		YelpReview rev = new YelpReview("BqI8sjcMaGFHM27XEef7DQ", "2014-08-01", "up-pp980QobXgJU4ETTsYw",
				"PC-x4Om-XXGqFn0ludTykw", 5);

		YelpUser user2 = new YelpUser(123456, "You");

		assertTrue(user.hashCode() == user2.hashCode());

		assertEquals(false, user.equals(rev));

	}

	/*
	 * Testing that we can create a restaurant without a JSON string and that all
	 * the fields update correctly
	 * 
	 */
	@Test
	public void test10() {
		Restaurant rest = new Restaurant(43929293, "This Place");
		rest.setBusinessName("My House");
		YelpUser user = new YelpUser(123456, "You");

		assertEquals("http://www.yelp.com/biz/My-House", rest.getURL());
		assertEquals("My House", rest.getBusinessName());
		rest.setURL("http://www.yelp.com/biz/My_House");
		assertEquals("http://www.yelp.com/biz/My_House", rest.getURL());

		rest.setPrice(3);
 
		assertTrue(3 == rest.getPrice());

		assertTrue(rest.getCategories().isEmpty());
		rest.setOpen(false);

		assertEquals(false, rest.isOpen());

		rest.setPhotoURL("Error 404: No image found");

		assertEquals("Error 404: No image found", rest.getPhotoURL());

		assertEquals(false, rest.equals(user));

	}

	/*
	 * Testing that we can create a YelpVotes map and that all the fields update
	 * correctly
	 * 
	 */
	@Test
	public void test11() {
		YelpVotes votes = new YelpVotes(1, 2, 3);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("cool", 1);
		map.put("useful", 2);
		map.put("funny", 3);

		assertEquals(map, votes.getMap());
		assertTrue(votes.hashCode() == votes.hashCode());
		assertEquals(false, votes.equals(map));
		assertTrue(votes.equals(votes));
	}

	/*
	 * Testing methods that add reviews, users, and restaurants to a database
	 * directly and that the various lists and maps update correctly
	 * 
	 */
	@Test
	public void test12() throws IOException, InvalidInputException, UserNotFoundException, ReviewNotFoundException,
			RestaurantNotFoundException {
		YelpDb yelp = new YelpDb("data/usersTest1.json", "data/restaurantsTest1.json", "data/reviewsTest1.json");
		assertEquals(new HashSet<>(), yelp.getMatches("247377"));
		assertEquals(
				"{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, \"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}",
				yelp.getUserJSON("_NH7Cpq3qZkByP5xR4gXog"));
		assertEquals(
				"{\"type\": \"review\", \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 1, \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"date\": \"2006-07-26\"}",
				yelp.getReviewJSON("0a-pCW4guXIlWNpVeBHChg"));
		assertEquals(
				"{\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}",
				yelp.getRestaurantJSON("gclB3ED6uk6viWlolSb_uA"));
		yelp.addUser("Me");
		yelp.addUserJSON(
				"{\"url\": \"http://www.yelp.com/user_details?userid=ePerljemmmGHi-DAZ6ZELQ\", \"votes\": {\"funny\": 22, \"useful\": 28, \"cool\": 11}, \"review_count\": 68, \"type\": \"user\", \"user_id\": \"ePerljemmmGHi-DAZ6ZELQ\", \"name\": \"Chester R.\", \"average_stars\": 3.39705882352941}");
		yelp.addUserJSON(
				"{\"url\": \"http://www.yelp.com/user_details?userid=HJctT_mE9kGJSdaMo5PD5w\", \"votes\": {\"funny\": 15, \"useful\": 44, \"cool\": 24}, \"review_count\": 25, \"type\": \"user\", \"user_id\": \"HJctT_mE9kGJSdaMo5PD5w\", \"name\": \"Phillip B.\", \"average_stars\": 3.56}");
		yelp.addRestaurant("My Restaurant");
		yelp.addRestaurantJSON(
				"{\"open\": true, \"url\": \"http://www.yelp.com/biz/chipotle-mexican-grill-berkeley-4\", \"longitude\": -122.2590498, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"tzRNk1hqooBV3i-Q_yKP7g\", \"name\": \"Chipotle Mexican Grill\", \"categories\": [\"Fast Food\", \"Mexican\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": \"2311 Telegraph Ave\\nTelegraph Ave\\nBerkeley, CA 94704\", \"review_count\": 190, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/xOTrsHOnDHi4flRHUp9DOA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8683664, \"price\": 2}");
		yelp.addRestaurantJSON(
				"{\"open\": true, \"url\": \"http://www.yelp.com/biz/the-sunny-side-caf%C3%A9-berkeley-2\", \"longitude\": -122.2658691, \"neighborhoods\": [\"Downtown Berkeley\", \"UC Campus Area\"], \"business_id\": \"PC-x4Om-XXGqFn0ludTykw\", \"name\": \"The Sunny Side Caf\\u00e9\", \"categories\": [\"Breakfast & Brunch\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.5, \"city\": \"Berkeley\", \"full_address\": \"2136 Oxford St\\nDowntown Berkeley\\nBerkeley, CA 94704\", \"review_count\": 175, \"photo_url\": \"http://s3-media3.ak.yelpcdn.com/bphoto/NMgMutTLuSShNGQYt9NRJQ/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8701853, \"price\": 3}");
		yelp.addReview("2013-02-05", "ePerljemmmGHi-DAZ6ZELQ", "tzRNk1hqooBV3i-Q_yKP7g", 3);
		assertTrue(yelp.getUsersRestaurant("tzRNk1hqooBV3i-Q_yKP7g").contains("ePerljemmmGHi-DAZ6ZELQ"));

		yelp.addReviewJSON(
				"{\"type\": \"review\", \"business_id\": \"PC-x4Om-XXGqFn0ludTykw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"mEfgf8MzFl6YitoVprh71w\", \"text\": \"Food was nice. I got a Bacon avacoda omelette. The price is a little high but you get a decent amount of food. They are a bit forgetful on somethings. My main gripe with the place is the flies. I spent more time swatting than eating.\", \"stars\": 3, \"user_id\": \"HJctT_mE9kGJSdaMo5PD5w\", \"date\": \"2012-09-03\"}");

		//assertTrue(yelp.getReviewsRestaurant("tzRNk1hqooBV3i-Q_yKP7g").contains("1"));

	}
	
	@Test 
	public void test13() throws IOException, InvalidInputException, RestaurantNotFoundException, UserNotFoundException, ReviewNotFoundException, InvalidReviewStringException, InvalidUserStringException, InvalidRestaurantStringException, InvalidQueryException, NoMatchException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		assertEquals("{\"url\": \"http://www.yelp.com/user_details?userid=0\", \"votes\": {}, \"review_count\": 0, \"type\": \"user\", \"user_id\": \"0\", \"name\": \"Sathish G.\", \"average_stars\": 0}", yelp.queryProcessor("ADDUSER {\"favorite color\": \"yellow\", \"name\": \"Sathish G.\", \"hobbies\": \"golf\", \"favorite food\": \"egg\"}"));
		assertEquals(yelp.getRestaurantJSON("WXKx2I2SEzBpeUGtDMCS8A"), yelp.queryProcessor("GETRESTAURANT WXKx2I2SEzBpeUGtDMCS8A"));
		assertEquals("{\"open\": true, \"url\": \"http://www.yelp.com/biz/pancake\", \"longitude\": -121.53281948723, \"neighborhoods\": [\"Downtown Berkeley\", \"UC Campus Area\"], \"business_id\": \"0\", \"name\": \"pancake store\", \"categories\": [\"pancake\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 0, \"city\": \"Berkeley\", \"full_address\": \"9999 pancake St\\nDowntown Berkeley\\nBerkeley, CA 99999\", \"review_count\": 0, \"photo_url\": \"http://s3-media3.ak.yelpcdn.com/bphoto/pancake.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 32.342895435, \"price\": 1}", yelp.queryProcessor("ADDRESTAURANT {\"open\": true, \"url\": \"http://www.yelp.com/biz/pancake\", \"longitude\": -121.53281948723, \"neighborhoods\": [\"Downtown Berkeley\", \"UC Campus Area\"], \"name\": \"pancake store\", \"categories\": [\"pancake\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"city\": \"Berkeley\", \"full_address\": \"9999 pancake St\\nDowntown Berkeley\\nBerkeley, CA 99999\", \"review_count\": 0, \"photo_url\": \"http://s3-media3.ak.yelpcdn.com/bphoto/pancake.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 32.342895435, \"price\": 1}"));
		assertEquals("{\"type\": \"review\", \"business_id\": \"0\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0\", \"text\": \"it was pretty good\", \"stars\": 5, \"user_id\": \"0\", \"date\": \"2017-12-13\"}", yelp.queryProcessor("ADDREVIEW {\"type\": \"review\", \"business_id\": \"0\", \"text\": \"it was pretty good\", \"stars\": 5, \"user_id\": \"0\", \"date\": \"2017-12-13\", \"time\": \"6:02 pm\"}"));
		try {
			yelp.queryProcessor("ADDUSER {\"name\": \"mike w.\", favorite food: eggs}");
			fail("Exception expected!");
		} catch (InvalidUserStringException e) {
			//do nothing
		}
		try {
			yelp.queryProcessor("ADDUSER {\"name\": \"mike w.\", \"favorite food\": \"eggs\"}");
		} catch (InvalidUserStringException e) {
			fail("No Exception expected!");
		}
		System.out.println(yelp.queryProcessor("in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2"));
		System.out.println(yelp.queryProcessor("category(Sandwiches) && price < 2 && rating > 4"));
		System.out.println(yelp.queryProcessor("category(Mexican) && price < 2 && rating >= 4"));
		try {
			System.out.println(yelp.queryProcessor("GETUSER ksadasd"));
			
			fail("expected an exception");
		} catch (InvalidQueryException e) {
			//do nothing
		}
	}

}
