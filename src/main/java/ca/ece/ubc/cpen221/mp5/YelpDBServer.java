package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/**
 * FibonacciServerMulti is a server that finds the n^th Fibonacci number given
 * n. It accepts requests of the form: Request ::= Number "\n" Number ::= [0-9]+
 * and for each request, returns a reply of the form: Reply ::= (Number | "err")
 * "\n" where a Number is the request Fibonacci number, or "err" is used to
 * indicate a misformatted request. FinbonacciServerMulti can handle multiple
 * concurrent clients.
 */
public class YelpDBServer{
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
		System.err.println("client connected");

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
				System.err.println("request: " + line);
				try {
					System.err.println(yelp.queryProcessor(line));
				} catch (InvalidInputException e) {
					System.err.println("Reply: ERR: INVALID_REQUEST");
					out.println("ERR\n");
				} catch (ReviewNotFoundException e) {
					// This exception should not ever be thrown since we cannot look for a review,
					// but we must catch it anyway
				} catch (UserNotFoundException e) {
					System.err.println("Reply: ERR: NO_SUCH_USER");
					out.println("ERR\n");
				} catch (RestaurantNotFoundException e) {
					System.err.println("Reply: ERR: NO_SUCH_RESTAURANT");
					out.println("ERR\n");
				} catch (InvalidReviewStringException e) {
					System.err.println("Reply: ERR: INVALID_REVIEW_STRING");
					out.println("ERR\n");
				} catch (InvalidUserStringException e) {
					System.err.println("Reply: ERR: INVALID_USER_STRING");
					out.println("ERR\n");
				} catch (InvalidRestaurantStringException e) {
					System.err.println("Reply: ERR: INVALID_RESTAURANT_STRING");
					out.println("ERR\n");
				} catch (InvalidQueryException e) {
					System.err.println("Reply: ERR: INVALID_REQUEST");
					out.println("ERR\n");
				}
			}
		} finally {
			out.close();
			in.close();
		}
	}

	/**
	 * Start a FibonacciServerMulti running on the default port.
	 */
	public static void main(String[] args) {
		try {
			YelpDBServer server = new YelpDBServer(YELPDB_PORT);
			server.serve();
		} catch (IOException | InvalidInputException e1) {
			e1.printStackTrace();
		}
	}
}
