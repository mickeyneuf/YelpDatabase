
import static org.junit.Assert.*;

import java.io.IOException;

import javax.json.*;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpReview;
import ca.ece.ubc.cpen221.mp5.YelpUser;

public class YelpDbTest {

	@Test
	public void test0() throws IOException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");

	}

	@Test
	public void test1() {
		String string = "{\"url\": \"http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog\", \"votes\": {\"funny\": 35, \"useful\": 21, \"cool\": 14}, \"review_count\": 29, \"type\": \"user\", \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"name\": \"Chris M.\", \"average_stars\": 3.89655172413793}";
		YelpUser user1 = new YelpUser(string);
		YelpUser user2 = new YelpUser(string);

		assertTrue(user1.equals(user2));
	}
	
	@Test
	public void test2() {
		String string = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/cafe-3-berkeley\", \"longitude\": -122.260408, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"name\": \"Cafe 3\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 2.0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 9, \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/AaHq1UzXiT6zDBUYrJ2NKA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
		Restaurant rest1 = new Restaurant(string);
		Restaurant rest2 = new Restaurant(string);
		
		assertTrue(rest1.equals(rest2));
	}
	
	@Test
	public void test3() {
		String string = "{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"0a-pCW4guXIlWNpVeBHChg\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}";
		YelpReview rev1 = new YelpReview(string);
		YelpReview rev2 = new YelpReview(string);
		
		assertTrue(rev1.equals(rev2));
	}

}