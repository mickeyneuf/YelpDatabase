import static org.junit.Assert.*;

import java.io.IOException;

/*import javax.json.*;*/

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDb;
import ca.ece.ubc.cpen221.mp5.YelpUser;
import ca.ece.ubc.cpen221.mp5.YelpVotes;

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
		YelpVotes yv = new YelpVotes();
		System.out.println(yv);
	}

}
