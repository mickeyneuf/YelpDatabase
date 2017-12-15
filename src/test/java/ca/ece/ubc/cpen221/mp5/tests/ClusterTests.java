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
		ArrayList<HashSet<String>> clusters = yelp.kMeansClusters(26);
		yelp.kMeansClusters_json(15);
		HashMap<ArrayList<Double>, Integer> centroids = new HashMap<ArrayList<Double>, Integer>();
		HashMap<String, Integer> restGroups = new HashMap<String, Integer>();
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
			for (HashSet<String> set : clusters) {
				if (set.contains(rest)&&!centroids.get(closest).equals(clusters.indexOf(set))) {
				}
			}
		}
		} catch (IOException e) {
			fail("no exception expected");
		}
	}
	
}






