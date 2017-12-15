package ca.ece.ubc.cpen221.mp5;


import java.io.*;
import java.net.Socket;

/**
 * YelpDBClient is based off of the FibonacciClient given in class
 * 
 * This is a client that sends various requests to a YelpDBServer and
 * it interprets the replies. 
 * A new YelpDBClient is "open" until the close() method is called,
 * at which point it is "closed" and may not be used further.
 */
public class YelpDBClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    // Rep invariant: socket, in, out != null
    
    /**
     * Make a YelpDBClient and connect it to a server running on
     * hostname at the specified port.
     * @throws IOException if can't connect
     */
    public YelpDBClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    /**
     * Send a request to the server. Requires this is "open".
     * @param x to request x of the server
     * 			x could be any of:
     * 			ADDRESTAURANT
     * 			ADDUSER
     * 			ADDREVIEW
     * 			GETRESTAUARANT
     * 			QUERY
     * @throws IOException if network or server failure
     */
    public void sendRequest(String x) throws IOException {
        out.print(x + "\n");
        out.flush(); // important! make sure x actually gets sent
    }
    
    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     * @return the result of the request
     * 			The result could be a JSON string or a list of 
     * 			JSON strings of the matching restaurants
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("connection terminated unexpectedly");
        }
        
        try {
            return reply;
        } catch (Exception e) {
            throw new IOException("misformatted reply: " + reply);
        }
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    
    
    /**
     * Use a YelpDBServer to:
     * 
     * 1. Add a restaurant with correct format
     * 2. Add a user with correct format
     * 3. Add a review with incorrect format
     * 4. Receive an error for an invalid request
     * 5. To look for all Chinese and Italian
     *    restaurants in the Telegraph Avenue 
     *    neighbourhood that have a price range of 1-2.
     * 6. To look for all Cafes that have a price of 4
     *    and find no matches
     */
    public static void main(String[] args) {
        try {
            YelpDBClient client = new YelpDBClient("localhost", YelpDBServer.YELPDB_PORT);

            String request1 = "ADDRESTAURANT {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"latitude\": 37.867417, \"price\": 1}";
            client.sendRequest(request1);
            System.out.println(request1 + "\n");
            String answer1 = client.getReply();
            System.out.println("Reply: " + answer1 + "\n");
            
            String request2 = "ADDUSER {\"name\": \"Hillary S.\", \"price\": 1}";
            client.sendRequest(request2);
            System.out.println(request2 + "\n");
            String answer2 = client.getReply();
            System.out.println("Reply: " + answer2 + "\n");
            
            String request3 = "ADDREVIEW {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"price\": 1}";
            client.sendRequest(request3);
            System.out.println(request3 + "\n");
            String answer3 = client.getReply();
            System.out.println("Reply: " + answer3 + "\n");
            
            String request4 = "GETUSER 37.867417";
            client.sendRequest(request4);
            System.out.println(request4 + "\n");
            String answer4 = client.getReply();
            System.out.println("Reply: " + answer4 + "\n");
            
            String request5 = "QUERY in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2 ";
            client.sendRequest(request5);
            System.out.println(request5 + "\n");
            String answer5 = client.getReply();
            System.out.println("Reply: " + answer5 + "\n");
            
            String request6 = "QUERY category(Cafes, American (Traditional)) && price = 4 ";
            client.sendRequest(request6);
            System.out.println(request6 + "\n");
            String answer6 = client.getReply();
            System.out.println("Reply: " + answer6 + "\n");
            
            
            client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
