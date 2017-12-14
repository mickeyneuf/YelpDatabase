package ca.ece.ubc.cpen221.mp5;


import java.io.*;
import java.net.Socket;

/**
 * FibonacciClient is a client that sends requests to the FibonacciServer
 * and interprets its replies.
 * A new FibonacciClient is "open" until the close() method is called,
 * at which point it is "closed" and may not be used further.
 */
public class YelpDBClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    // Rep invariant: socket, in, out != null
    
    /**
     * Make a FibonacciClient and connect it to a server running on
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
     * @param x to find Fibonacci(x)
     * @throws IOException if network or server failure
     */
    public void sendRequest(String x) throws IOException {
        out.print(x + "\n");
        out.flush(); // important! make sure x actually gets sent
    }
    
    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     * @return the requested Fibonacci number
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
     * Use a FibonacciServer to find the first N Fibonacci numbers.
     */
    public static void main(String[] args) {
        try {
            YelpDBClient client = new YelpDBClient("localhost", YelpDBServer.YELPDB_PORT);

            String request = "ADDRESTAURANT {\"open\": true, \"url\": \"http://www.thisbusiness.com/yelp\", \"longitude\": -122.260408, \"neighborhoods\": [\"UC Campus Area\"], \"name\": \"The Spot\", \"categories\": [\"Cafes\", \"Restaurants\"], \"full_address\": \"2400 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94701\", \"review_count\": 2, \"photo_url\": \"http://thisbusiness.com/yelp/photo\", \"latitude\": 37.867417, \"price\": 1}";
            client.sendRequest(request);
            System.out.println(request + "\n");
            String answer = client.getReply();
            System.out.println("Reply: " + answer + "\n");
            
            // send the requests to find the first N Fibonacci numbers
          /*for (int x = 1; x <= N; ++x) {
                client.sendRequest(x);
                System.out.println("fibonacci("+x+") = ?");
            }*/
            
            // collect the replies
        /*for (int x = 1; x <= N; ++x) {
                BigInteger y = client.getReply();
                System.out.println("fibonacci("+x+") = "+y);
            }*/
            
            client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
