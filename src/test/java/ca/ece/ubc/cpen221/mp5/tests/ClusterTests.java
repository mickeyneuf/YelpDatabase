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
	// This test creates a YelpDb instance and checks that the kMeansClusters and kMeansClusters_json methods work correctly
	// It does this by checking that every restaurant in a cluster is not closer to another cluster's centroid
	public void test0() throws IOException, InvalidInputException, RestaurantNotFoundException, ArithmeticException{
		try {
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		ArrayList<HashSet<String>> clusters = yelp.kMeansClusters(26);
		yelp.kMeansClusters_json(15);
		HashMap<ArrayList<Double>, Integer> centroids = new HashMap<ArrayList<Double>, Integer>();
		HashMap<String, Integer> restGroups = new HashMap<String, Integer>();
		// calculating centroids of each cluster
		for (HashSet<String> cluster : clusters) {
			double xCoord = 0;
			double yCoord = 0;
			for (String rest : cluster) {
				Restaurant r = new Restaurant(yelp.getRestaurantJSON(rest));
				xCoord += r.getLocation().getLongitude();
				yCoord += r.getLocation().getLatitude();
			}
			xCoord = xCoord/cluster.size();
			yCoord = yCoord/cluster.size();
			ArrayList<Double> centroid = new ArrayList<Double>();
			centroid.add(xCoord);
			centroid.add(yCoord);
			centroids.put(centroid, clusters.indexOf(cluster));
		}
		// checks that all restaurants in list are in the cluster whose centroid they are closest to 
		for (String rest : restGroups.keySet()) {
			double mindist = Double.MAX_VALUE;
			ArrayList<Double> closest = null;
			for (ArrayList<Double> centroid : centroids.keySet()) {
				Restaurant r = new Restaurant (yelp.getRestaurantJSON(rest));
				double distance = Math.sqrt(Math.pow(r.getLocation().getLatitude() - centroid.get(1), 2)
						+ Math.pow(r.getLocation().getLongitude() - centroid.get(0), 2));
				if(distance<mindist) {
					mindist = distance;
					closest = centroid;
				}
				int group = -1;
				for (HashSet<String> set : clusters) {
					if(set.contains(rest)) 	{
						group = clusters.indexOf(set);
					}
				}
				restGroups.put(rest, group);
			}
			// if the restaurant is not closest to the right centroid, test fails
			for (HashSet<String> set : clusters) {
				if (set.contains(rest)&&!centroids.get(closest).equals(clusters.indexOf(set))) {
					fail("Restaurant was closer to another centroid!");
				}
			}
		}
		// for the kmeansClusters_json method, because it only converts the result of 
		// kmeansClusters to a string of json strings, we only test that the number of clusters is correct
		for (int i = 0 ; i < 10 ; i++) {
			assertTrue(yelp.kMeansClusters_json(10).contains("\"cluster\": "+i));
		}
		} catch (IOException e) {
			fail("no exception expected");
		}
	}
	
}






