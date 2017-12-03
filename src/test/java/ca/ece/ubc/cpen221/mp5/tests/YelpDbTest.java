package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import javax.json.*;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpReview;
import ca.ece.ubc.cpen221.mp5.YelpUser;

public class YelpDbTest {

	@Test
	public void test0() throws IOException, InvalidInputException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
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

	/*@Test
	public void test1() {
		String string = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, \"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}";
		YelpUser user1 = new YelpUser(string);
		YelpUser user2 = new YelpUser(string);

		assertTrue(user1.equals(user2));
	}*/
	
	/*@Test
	public void test2() {
		String string = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
		Restaurant rest1 = new Restaurant(string);
		Restaurant rest2 = new Restaurant(string);
		
		assertTrue(rest1.equals(rest2));
	}*/
	
	@Test
	public void test3() throws InvalidInputException{
		try {
		String string = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}";
		YelpReview rev1 = new YelpReview(string);
		YelpReview rev2 = new YelpReview(string);
		assertTrue(rev1.equals(rev2));
		} catch (Exception e) {
			fail("no exception expected!");
		}
	}
	
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
	
	/*@Test
	public void test5() {
		String json = "{\"url\": \"http://www.yelp.com/user_details?userid=zkjy_XoVgR2EFjLjtzFDNw\", \"votes\": {\"funny\": 1, \"useful\": 8, \"cool\": 4}, \"review_count\": 7, \"type\": \"user\", \"user_id\": \"zkjy_XoVgR2EFjLjtzFDNw\", \"name\": \"Elise B.\", \"average_stars\": 3.28571428571429}";
		YelpUser elise = new YelpUser(json);
		assertEquals(json, elise.getJSON());
	}*/	
	/*@Test
	public void test6() {
		String json = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/peking-express-berkeley\", \"longitude\": -122.2581978, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"1E2MQLWfwpsId185Fs2gWw\", \"name\": \"Peking Express\", \"categories\": [\"Chinese\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.5, \"city\": \"Berkeley\", \"full_address\": \"2516 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\", \"review_count\": 10, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/BPMBr2aiOEpVioLb-RurJQ/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867768, \"price\": 1}";
		Restaurant PekingExpress = new Restaurant(json);
		assertEquals(json, PekingExpress.getJSON());*/

	@Test
	public void test7() throws InvalidInputException, IOException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		String json = yelp.kMeansClusters_json(10);
		
	}
	
	@Test
	public void test1() throws IOException, InvalidInputException {
		YelpDb test = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		ToDoubleBiFunction<MP5Db<Restaurant>, String> i = test.getPredictorFunction("Djk49JjpKl9HQNpmiX669Q");
		System.out.println(i.applyAsDouble(test, "TUIDRJ_rUkdmYPSRAAEsPg"));
	}
}

