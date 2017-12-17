package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.InvalidInputException;
import ca.ece.ubc.cpen221.mp5.YelpDBClient;
import ca.ece.ubc.cpen221.mp5.YelpDBServer;

public class YelpDBServerTest {

	@Test
	public void test0() {
		String correct1 = "{\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": \"0\", \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 0, \"city\": \"Berkeley\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.867417, \"price\": 1}";
		String correct2 = "{\"url\": \"http://www.yelp.com/user_details?userid=0\", \"votes\": {}, \"review_count\": 0, \"type\": \"user\", \"user_id\": \"0\", \"name\": \"Hillary S.\", \"average_stars\": 0}";
		String correct3 = "ERR: INVALID REVIEW STRING";
		String correct4 = "ERR: ILLEGAL REQUEST";
		String correct5 = "{\"open\": true, \"url\": \"http://www.yelp.com/biz/lotus-house-berkeley\", \"longitude\": -122.258216, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"business_id\": \"XBPMMfMchDlxZG-qSsSdtw\", \"name\": \"Lotus House\", \"categories\": [\"Food\", \"Coffee & Tea\", \"Chinese\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": \"2517A Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94704\", \"review_count\": 160, \"photo_url\": \"http://s3-media3.ak.yelpcdn.com/bphoto/w4ig8KmeCt9wYkeYrDehIA/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.868035, \"price\": 2}";
		String correct6 = "ERR: NO MATCH";
		String correct7 = "ERR: NO SUCH USER";
		String correct8 = "ERR: NO SUCH RESTAURANT";
		String correct9 = "ERR: INVALID USER STRING";
		String correct10 = "ERR: INVALID RESTAURANT STRING";
				
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

					String request1 = "ADDRESTAURANT {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"latitude\": 37.867417, \"price\": 1}";
					client.sendRequest(request1);
					String answer1 = client.getReply();
					assertEquals(correct1, answer1);
					
		            String request2 = "ADDUSER {\"name\": \"Hillary S.\", \"price\": 1}";
		            client.sendRequest(request2);
		            String answer2 = client.getReply();
		            assertEquals(correct2, answer2);
		            
		            String request3 = "ADDREVIEW {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"price\": 1}";
		            client.sendRequest(request3);
		            String answer3 = client.getReply();
		            assertEquals(correct3, answer3);
		            
		            String request4 = "GETUSER 37.867417";
		            client.sendRequest(request4);
		            String answer4 = client.getReply();
		            assertEquals(correct4, answer4);
		            
		            String request5 = "QUERY in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2 ";
		            client.sendRequest(request5);
		            String answer5 = client.getReply();
		            assertTrue(answer5.contains(correct5));
		            
		            String request6 = "QUERY category(Cafes, American (Traditional)) && price = 4 ";
		            client.sendRequest(request6);
		            String answer6 = client.getReply();
		            assertEquals(correct6, answer6);
		            
		            String request7 = "ADDREVIEW {\"business_id\": \"gclB3ED6uk6viWlolSb_uA\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 1, \"user_id\": \"_NH7Cpq\", \"date\": \"2006-07-26\"}";
		            client.sendRequest(request7);
		            String answer7 = client.getReply();
		            assertEquals(correct7, answer7);
		            
		            String request8 = "ADDREVIEW {\"business_id\": \"lolSb_uA\", \"text\": \"The pizza is terrible, but if you need a place to watch a game or just down some pitchers, this place works.\\n\\nOh, and the pasta is even worse than the pizza.\", \"stars\": 1, \"user_id\": \"_NH7Cpq3qZkByP5xR4gXog\", \"date\": \"2006-07-26\"}";
		            client.sendRequest(request8);
		            String answer8 = client.getReply();
		            assertEquals(correct8, answer8);
		            
		            String request9 = "ADDUSER {\"name\": \"Mikayla\", yedsdf}";
		            client.sendRequest(request9);
		            String answer9 = client.getReply();
		            assertEquals(correct9, answer9);
		            
		            String request10 = "ADDRESTAURANT {\"open\": true, \"url\": \"http://www.yelp.com/biz/hummingbird-express-berkeley\", \"longitude\": -122.2602129, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": \"LqVC-mJ3GQyKg\", \"name\": \"Hummingbird Express\", \"categories\": [\"Mediterranean\", \"Sandwiches\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": \"1842 Euclid Ave\\nUC Campus Area\\nBerkeley, CA 94709\", \"review_count\": 16, \"photo_url\": \"http://ak.yelpcdn.com/gfx/blank_biz_medium.gif\", \"schools\": University of California at Berkeley, \"latitude\": 37.8755024, \"price\": 2}";
		            client.sendRequest(request10);
		            String answer10 = client.getReply();
		            assertEquals(correct10, answer10);
		            
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		clientThread.start();
		
		try {
		clientThread.join();
		} catch (Exception e) {
			fail("This thread should not have been interrupted.");
		}
		
		serverThread.interrupt();
	}
	
	@Test
	public void test1() {
		Thread serverThread = new Thread(new Runnable() {
			public void run() {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				System.setOut(new PrintStream(out));
				Scanner scanner = new Scanner(System.in);
				String[] String = {"20"};
				YelpDBServer.main(String);
			}
		});
		serverThread.start();
		
		Thread clientThread = new Thread(new Runnable() {
			public void run() {
				try {
					YelpDBClient client = new YelpDBClient("localhost", YelpDBServer.YELPDB_PORT);

					String request1 = "ADDRESTAURANT {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"latitude\": 37.867417, \"price\": 1}";
					client.sendRequest(request1);
					String answer1 = client.getReply();
	            
		            
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		clientThread.start();
		
		try {
		clientThread.join();
		} catch (Exception e) {
			fail("This thread should not have been interrupted.");
		}
		
		serverThread.interrupt();
	}
}
