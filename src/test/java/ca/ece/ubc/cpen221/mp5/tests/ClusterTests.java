package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.PredictorFunction;
import ca.ece.ubc.cpen221.mp5.YelpDb;


public class ClusterTests {
	@Test
	public void test0(){
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
	}
	
}
