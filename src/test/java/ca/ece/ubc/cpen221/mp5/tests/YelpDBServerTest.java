package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.YelpDBClient;
import ca.ece.ubc.cpen221.mp5.YelpDBServer;

public class YelpDBServerTest {

	@Test
	public void test0() throws InterruptedException {
		String correct = "{\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": \"0\", \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
		
		Thread serverThread = new Thread(new Runnable() {
			public void run() {
				try {
					YelpDBServer server = new YelpDBServer(YelpDBServer.YELPDB_PORT);
					server.serve();
				} catch (IOException | InvalidInputException e1) {
					e1.printStackTrace();
				}
			}
		});
		serverThread.start();

		Thread clientThread = new Thread(new Runnable() {
			public void run() {
				try {
					YelpDBClient client = new YelpDBClient("localhost", YelpDBServer.YELPDB_PORT);

					String request = "ADDRESTAURANT {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"latitude\": 37.867417, \"price\": 1}";
					client.sendRequest(request);
					System.out.println(request + "\n");
					String answer = client.getReply();
					System.out.println("Reply: " + answer + "\n");
					assertEquals(correct, answer);
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		clientThread.start();
		serverThread.join();
		clientThread.join();
	}
}
