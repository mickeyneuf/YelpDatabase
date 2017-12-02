package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.PredictorFunction;
import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpUser;

public class PredictorFunctionTest {

	@Test
	public void test0() throws IOException {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		YelpUser user = new YelpUser(
				"{\"url\": \"http://www.yelp.com/user_details?userid=wr3JF-LruJ9LBwQTuw7aUg\", \"votes\": {\"funny\": 2, \"useful\": 5, \"cool\": 3}, \"review_count\": 15, \"type\": \"user\", \"user_id\": \"wr3JF-LruJ9LBwQTuw7aUg\", \"name\": \"Katie W.\", \"average_stars\": 3.6}");
		Restaurant rest = new Restaurant(
				"{\"open\": true, \"url\": \"http://www.yelp.com/biz/la-vals-pizza-berkeley\", \"longitude\": -122.2603641, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"name\": \"La Val's Pizza\", \"categories\": [\"Pizza\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.5, \"city\": \"Berkeley\", \"full_address\": \"1834 Euclid Ave\\nUC Campus Area\\nBerkeley, CA 94709\", \"review_count\": 218, \"photo_url\": \"http://s3-media2.ak.yelpcdn.com/bphoto/m7_y0Xsf_9yzli5aLEnquQ/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8755322, \"price\": 1}");
		String id = user.getUserID();
		String restID = rest.getBusinessID();

		yelp.removeRestaurant(restID);

		PredictorFunction func = (PredictorFunction) yelp.getPredictorFunction(id);

		assertTrue(func.applyAsDouble(yelp, restID) == 3);

	}

}
