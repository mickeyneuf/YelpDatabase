package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * YelpDBServer is a server that was based off of the FibonacciServerMulti given
 * in class.
 * 
 * 
 * Requests that it accepts include: GETRESTAURANT <business id>: This returns
 * the restaurant with the specified business ID ADDUSER <user information>:
 * This adds a new user to the YelpDb database and returns the JSON string
 * associated with this new user once it has been created ADDRESTAURANT
 * <restaurant information>: This adds a new restaurant to the YelpDb database
 * and returns the JSON string ADDREVIEW <review information>: This adds a new
 * review to the YelpDb database and returns the JSON string And various search
 * strings: Clients can search for all restaurants that conform to specific
 * criteria. For example: "QUERY (in(Telegraph Ave) && (category(Chinese) ||
 * category(Italian)) && price <= 2)" would return a list of Chinese and Italian
 * restaurants in the Telegraph Avenue neighbourhood that have a price range of
 * 1-2.
 * 
 * This server will reply with the JSON string of the restaurant requested, the
 * JSON string of the user, restaurant, or review that was added, or a set of
 * Restaurants that matched the specific query.
 * 
 * This server has several error messages which correspond to various errors:
 * "ERR: INVALID_REQUEST" is used to handle incorrectly formatted request
 * strings or invalid queries. "ERR: NO SUCH USER" is used if the client tries
 * to create a review for a user that does not exist in the database. "ERR: NO
 * SUCH RESTAURANT" is used if the business ID used in a GETRESTAURANT request
 * does not match an existing restaurant, or if the client tries to create a
 * review for a restaurant that does not exist. "ERR: INVALID REVIEW STRING" is
 * used if the JSON string for a new review is not correctly formatted. "ERR:
 * INVALID USER STRING" is used if the JSON string for a new user is not
 * correctly formatted. "ERR: INVALID RESTAURANT STRING" is used if the JSON
 * string for a new restaurant is not correctly formatted. "ERR: NO MATCH" is
 * used if the query yields no matches.
 * 
 * 
 * This server can handle multiple concurrent clients.
 */
public class YelpDBServer {
	/** Default port number where the server listens for connections. */
	public static final int YELPDB_PORT = 4949;

	private ServerSocket serverSocket;
	private YelpDb yelp;

	// Rep invariant: serverSocket != null
	//
	// Thread safety argument:
	// TODO YELPDB_PORT
	// TODO serverSocket
	// TODO socket objects
	// TODO readers and writers in handle()
	// TODO data in handle()

	/**
	 * Make a YelpDBServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 * @throws InvalidInputException
	 */
	public YelpDBServer(int port) throws IOException, InvalidInputException {
		serverSocket = new ServerSocket(port);
		YelpDb yelp = new YelpDb("data/users.json", "data/restaurants.json", "data/reviews.json");
		this.yelp = yelp;
	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
			final Socket socket = serverSocket.accept();
			// create a new thread to handle that client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(socket);
						} finally {
							socket.close();
						}
					} catch (IOException ioe) {
						// this exception wouldn't terminate serve(),
						// since we're now on a different thread, but
						// we still need to handle it
						ioe.printStackTrace();
					}
				}
			});
			// start the thread
			handler.start();
		}
	}

	/**
	 * Handle one client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.err.println("Client connected");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and that buffer it so that we can read a line at a time
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the
		// socket output stream, and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it.
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

		try {
			// each request is a single line containing a request
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				System.err.println("Request: " + line);
				try {
					String reply = yelp.queryProcessor(line);
					System.err.println("Reply: " + reply);
					out.println(reply);

				} catch (InvalidInputException e) {
					System.err.println("Reply: ERR: INVALID_REQUEST");
					out.println("ERR: INVALID REQUEST\n");

				} catch (ReviewNotFoundException e) {
					// This exception should not ever be thrown since we cannot look for a review,
					// but we must catch it anyway

				} catch (UserNotFoundException e) {
					System.err.println("Reply: ERR: NO_SUCH_USER");
					out.println("ERR: NO SUCH USER");

				} catch (RestaurantNotFoundException e) {
					System.err.println("Reply: ERR: NO_SUCH_RESTAURANT");
					out.println("ERR: NO SUCH RESTAURANT");

				} catch (InvalidReviewStringException e) {
					System.err.println("Reply: ERR: INVALID_REVIEW_STRING");
					out.println("ERR: INVALID REVIEW STRING");

				} catch (InvalidUserStringException e) {
					System.err.println("Reply: ERR: INVALID_USER_STRING");
					out.println("ERR: INVALID USER STRING");

				} catch (InvalidRestaurantStringException e) {
					System.err.println("Reply: ERR: INVALID_RESTAURANT_STRING");
					out.println("ERR: INVALID RESTAURANT STRING");

				} catch (InvalidQueryException e) {
					System.err.println("Reply: ERR: INVALID_REQUEST");
					out.println("ERR: INVALID REQUEST");

				} catch (NoMatchException e) {
					System.err.println("Reply: ERR: NO_MATCH");
					out.println("ERR: NO MATCH");
				}
			}
		} finally {
			System.err.println("Client disconnected");
			out.close();
			in.close();
		}
	}

	/**
	 * Start a YelpDBServer running on the port specified as input to the function.
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String port = null;
		
		do {
			port = scan.next();
		} while (port == null || port.isEmpty());
		scan.close();
		
		
		if (Integer.valueOf(port) == YELPDB_PORT) {
			try {
				YelpDBServer server = new YelpDBServer(YELPDB_PORT);
				server.serve();
			} catch (IOException | InvalidInputException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				YelpDBServer server = new YelpDBServer(Integer.valueOf(port));
				server.serve();
			} catch (IOException | InvalidInputException e) {
				e.printStackTrace();
			}
		}
	}
}
