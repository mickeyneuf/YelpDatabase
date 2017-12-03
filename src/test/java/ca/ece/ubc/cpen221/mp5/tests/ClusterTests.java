package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;
import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.Restaurant;
import ca.ece.ubc.cpen221.mp5.RestaurantNotFoundException;
import ca.ece.ubc.cpen221.mp5.YelpDb;


public class ClusterTests {
	@Test
	public void test0() throws IOException, InvalidInputException, RestaurantNotFoundException, ArithmeticException{
		try {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		System.out.println(yelp.kMeansClusters_json(10));
		
		} catch (Exception e) {
			
		}
	}
	
}






