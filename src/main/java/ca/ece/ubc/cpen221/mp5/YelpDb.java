package ca.ece.ubc.cpen221.mp5;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.json.*;

public class YelpDb implements MP5Db<Restaurant> {

	/**
	 * 
	 * YelpDb is a database that contains YelpUsers, YelpReviews, and Restaurants.
	 * It is represented by various maps and lists that contain the users, reviews
	 * and restaurants and how they relate to one another, ie. which users have
	 * written reviews for certain restaurants.
	 * 
	 * Requires: that no object being placed inside the database is null that the
	 * files used to create the database are not empty or null and that they contain
	 * proper JSON formatted strings
	 * 
	 * 
	 * 
	 */

	private ArrayList<YelpUser> userList;
	private ArrayList<Restaurant> restaurantList;
	private ArrayList<YelpReview> reviewList;
	private ArrayList<YelpUser> deletedUserList;

	private TreeMap<String, ArrayList<String>> visitedBy;
	private TreeMap<String, ArrayList<String>> restaurantReviews;
	private TreeMap<String, ArrayList<String>> userReviews;

	private Integer userID;
	private Integer reviewID;
	private Integer businessID;

	public YelpDb(String userFile, String restaurantFile, String reviewFile) throws IOException, InvalidInputException {
		Scanner scanUser = new Scanner(new File(userFile));
		Scanner scanRestaurant = new Scanner(new File(restaurantFile));
		Scanner scanReview = new Scanner(new File(reviewFile));
		ArrayList<YelpUser> deleted = new ArrayList<YelpUser>();
		userList = new ArrayList<YelpUser>();
		restaurantList = new ArrayList<Restaurant>();
		reviewList = new ArrayList<YelpReview>();
		visitedBy = new TreeMap<String, ArrayList<String>>();
		restaurantReviews = new TreeMap<String, ArrayList<String>>();
		userReviews = new TreeMap<String, ArrayList<String>>();

		this.userID = 0;
		this.reviewID = 0;
		this.businessID = 0;

		while (scanUser.hasNext()) {
			YelpUser newUser = new YelpUser(scanUser.nextLine());
			this.userList.add(newUser);
			this.userReviews.put(newUser.getUserID(), new ArrayList<String>());
		}
		scanUser.close();

		while (scanRestaurant.hasNext()) {
			Restaurant newRestaurant = new Restaurant(scanRestaurant.nextLine());
			this.restaurantList.add(newRestaurant);
			this.restaurantReviews.put(newRestaurant.getBusinessID(), new ArrayList<String>());
			this.visitedBy.put(newRestaurant.getBusinessID(), new ArrayList<String>());
		}
		scanRestaurant.close();

		while (scanReview.hasNext()) {
			YelpReview newReview = new YelpReview(scanReview.nextLine());
			this.reviewList.add(newReview);
			this.visitedBy.get(newReview.getReviewed()).add(newReview.getUser());
			this.restaurantReviews.get(newReview.getReviewed()).add(newReview.getReviewID());
			this.userReviews.get(newReview.getUser()).add(newReview.getReviewID());
		}
		scanReview.close();
		this.deletedUserList = deleted;

	}

	/**
	 * Collects all objects inside the database that match the queryString As of
	 * right now, it is assumed that queryString will be a businessID and getMatches
	 * will return the restaurant that matches the ID.
	 * 
	 * Returns an empty set of no restaurant with a matching ID can be found in the
	 * database
	 * 
	 * @param queryString
	 *            a businessID that we are trying to find the restaurant for
	 * 
	 * @return Set the set of all objects that match the queryString; in this case,
	 *         the restaurant with the matching ID
	 * 
	 * 
	 */
	@Override
	public Set<Restaurant> getMatches(String queryString) {
		try {
			if (restaurantList.contains(this.getRestaurant(queryString))) {
				Set<Restaurant> set = new HashSet<Restaurant>();
				set.add(this.getRestaurant(queryString));
				return set;
			}
		} catch (Exception e) {
			return new HashSet<>();
		}
		return new HashSet<>();

	}

	/**
	 * Method that computes and represents a k-means clustering of the restaurants
	 * in this database. Helper method for kMeansClusters_json.
	 * 
	 * @param k
	 *            number of centroids/clusters
	 * @return A list of non-empty sets of restaurant IDs representing the clusters
	 * @throws RestaurantNotFoundException
	 */
	/**
	 * Method that computes and represents a k-means clustering of the restaurants
	 * in this database. Helper method for kMeansClusters_json.
	 * 
	 * @param k
	 *            number of centroids/clusters
	 * @return A list of non-empty sets of restaurant IDs representing the clusters
	 * @throws RestaurantNotFoundException
	 * @throws InvalidInputException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public synchronized ArrayList<HashSet<String>> kMeansClusters(int k)
			throws RestaurantNotFoundException, ArrayIndexOutOfBoundsException {
		boolean noEmpty = true;
		Random rand = new Random();
		HashMap<ArrayList<Double>, ArrayList<String>> clusters = new HashMap<ArrayList<Double>, ArrayList<String>>();
		ArrayList<HashSet<String>> clusterList = new ArrayList<HashSet<String>>();
		// choosing centroids and putting them on map
		// no clusters can be empty because we chose restaurant locations as centroids
		do {
			noEmpty = true;
			while (clusters.keySet().size() < k) {
				int n = rand.nextInt(this.restaurantList.size());
				ArrayList<Double> centro = new ArrayList<Double>();
				centro.add(restaurantList.get(n).getLocation().getLongitude());
				centro.add(restaurantList.get(n).getLocation().getLatitude());
				clusters.put(centro, new ArrayList<String>());
			}

			// make the centroid restaurants keys and non-centroid restaurants values of a
			// map
			// add non-centroid values to the centroid to which they are closest, by
			// calculating
			// euclidean distance with longitudes and latitudes
			for (Restaurant r : this.restaurantList) {
				ArrayList<Double> closest = null;
				double mindistance = Double.MAX_VALUE;
				for (ArrayList<Double> centro : clusters.keySet()) {
					double distance = Math.sqrt(Math.pow(r.getLocation().getLatitude() - centro.get(1), 2)
							+ Math.pow(r.getLocation().getLongitude() - centro.get(0), 2));
					if (distance < mindistance) {
						closest = centro;
						mindistance = distance;
					}
				}
				clusters.get(closest).add(r.getBusinessID());
			}
			boolean good = true;
			do {
				good = true;
				for (ArrayList<Double> centro : clusters.keySet()) {
					double avgX = 0;
					double avgY = 0;
					for (String rest : clusters.get(centro)) {
						avgX += this.getRestaurant(rest).getLocation().getLongitude();
						avgY += this.getRestaurant(rest).getLocation().getLatitude();
					}
					avgX = avgX / clusters.get(centro).size();
					avgY = avgY / clusters.get(centro).size();
					centro = new ArrayList<Double>();
					centro.add(avgX);
					centro.add(avgY);
				}
				for (Restaurant rest : this.restaurantList) {
					double mindist = Double.MAX_VALUE;
					ArrayList<Double> closest = null;
					for (ArrayList<Double> centro : clusters.keySet()) {
						double distance = Math.sqrt(Math.pow(rest.getLocation().getLatitude() - centro.get(1), 2)
								+ Math.pow(rest.getLocation().getLongitude() - centro.get(0), 2));
						if (distance < mindist) {
							closest = centro;
							mindist = distance;
						}
					}
					if (clusters.get(closest).contains(rest.getBusinessID())) {
					}
					if (!clusters.get(closest).contains(rest.getBusinessID())) {
						good = false;
						for (ArrayList<Double> centro : clusters.keySet()) {
							if (clusters.get(centro).contains(rest.getBusinessID())) {
								clusters.get(centro).remove(rest.getBusinessID());
							}
						}
						clusters.get(closest).add(rest.getBusinessID());
					}
				}
			} while (!good);
			for (ArrayList<Double> centro : clusters.keySet()) {
				if (clusters.get(centro).isEmpty()) {
					noEmpty = false;
					break;
				}
			}
		} while (!noEmpty);
		for (ArrayList<Double> a : clusters.keySet()) {
			HashSet<String> clusterSet = new HashSet<String>();
			clusterSet.addAll(clusters.get(a));
			clusterList.add(clusterSet);
		}
		// for each key, add the key to a set, and all its values to the same set
		// add this set to a list
		// return list, which should contain k-sets and include all restaurants in
		// database only once in a set
		return clusterList;
	}

	/**
	 * Cluster objects into k clusters using k-means clustering
	 * 
	 * @param k
	 *            number of clusters to create (0 < k <= number of objects)
	 * @return a String, in JSON format, that represents the clusters, or null if a
	 *         restaurant is no longer found in the database
	 */

	@Override
	public synchronized String kMeansClusters_json(int k) {
		ArrayList<HashSet<String>> setList;
		try {
			setList = this.kMeansClusters(k);
			String json = "";
			int c = 0;
			int i = 0;
			// for each set
			for (HashSet<String> cluster : setList) {
				for (String r : cluster) {
					json += "{\"x\": " + this.getRestaurant(r).getLocation().getLongitude() + ", \"y\": "
							+ this.getRestaurant(r).getLocation().getLatitude() + ", \"name\": "
							+ this.getRestaurant(r).getBusinessName() + "\", \"cluster\": " + c + ", \"weight\": 1.0}";
					if (i < restaurantList.size() - 1) {
						json += ", ";
					}
					i++;
				}
				c++;
			}
			return json;
		} catch (RestaurantNotFoundException e) {
			return null;
		}
	}

	/**
	 * Creates a linear function to determine the rating a user will give a
	 * restaurant based on its price. The method analyzes the users past ratings for
	 * restaurants and the prices of those restaurants to predict what kind of a
	 * rating the user will give a different restaurant.
	 * 
	 * It calculates an a and b value and passes these values to the constructor for
	 * PredictorFunction. The applyAsDouble function will calculate the rating as
	 * follows: rating = b * price + a
	 * 
	 * If the user being analyzed has one or fewer reviews, a and b are both zero,
	 * and the PredictorFunction should throw an exception when it is being applied,
	 * as we cannot accurately predict a rating without sufficient data.
	 * 
	 * @param user
	 *            the userID of the user we wish to predict the rating of
	 * 
	 * @return ToDoubleBiFunction<MP5Db<Restaurant>, String> The function which will
	 *         predict a users rating. The data base is the database which contains
	 *         the restaurant we want to predict the rating of, and the string is
	 *         the ID of that restaurant
	 * 
	 */
	@Override
	public synchronized ToDoubleBiFunction<MP5Db<Restaurant>, String> getPredictorFunction(String user) {
		double sxx = 0;
		double sxy = 0;

		List<String> restID = reviewList.stream().filter(rev -> rev.getUser().equals(user)).map(YelpReview::getReviewed)
				.collect(Collectors.toList()); // collecting all of the restaurants this user has reviewed

		List<Integer> prices = restaurantList.stream().filter(rest -> restID.contains(rest.getBusinessID()))
				.map(Restaurant::getPrice).collect(Collectors.toList()); // collecting a list of prices of the
																			// restaurants reviewed

		int totalPrice = prices.stream().reduce(0, (x, y) -> x + y); // calculating the total price

		List<Integer> ratings = reviewList.stream().filter(rev -> rev.getUser().equals(user)).map(YelpReview::getRating)
				.collect(Collectors.toList()); // collecting a list of the ratings of the reviews by this user

		int totalRating = ratings.stream().reduce(0, (x, y) -> x + y); // calculating the total ratings

		if (restID.isEmpty() || restID.size() == 1) { // if this user has one or zero reviews, we pass a = 0 and b = 0
														// to the function constructor, so the appropriate exception
														// will be thrown
			double a = 0;
			double b = 0;
			return new PredictorFunction(a, b);
		}

		double avgx = totalPrice / restID.size(); // computing the average price
		double avgy = totalRating / restID.size(); // computing the average rating

		assert (prices.size() == ratings.size()); // ensuring we have the same number of reviews as restaurants.
													// This is mostly a sanity check

		for (int i = 0; i < prices.size(); i++) { // computing sxx and sxy
			sxx += Math.pow((prices.get(i) - avgx), 2); // sxx is the sum of all (xi - mean(x))^2
			sxy += (prices.get(i) - avgx) * (ratings.get(i) - avgy); // sxy is the sum of all (xi - mean(x))*(yi -
																		// mean(y))

		}

		double b = sxy / sxx; // computing a and b and then passing them to the constructor for the function
		double a = avgy - (b * avgx);

		return new PredictorFunction(a, b);
	}

	/**
	 * 
	 * @param userID
	 *            the userID of the User we want to return
	 * @return the user with a matching ID
	 * @throws UserNotFoundException
	 *             if the user does not exist in the database
	 */

	private synchronized YelpUser getUser(String userID) throws UserNotFoundException {
		List<YelpUser> users = userList.stream().filter(user -> user.getUserID().equals(userID))
				.collect(Collectors.toList());
		if (users.isEmpty()) {
			throw new UserNotFoundException();
		}
		return users.get(0);
	}

	/**
	 * @param userID
	 *            the userID of the User whos JSON string we wish to return
	 * @return the JSON string of the user
	 * @throws UserNotFoundException
	 *             if the user does not exist in the database
	 */
	public synchronized String getUserJSON(String userID) throws UserNotFoundException {
		return this.getUser(userID).getJSON();
	}

	/**
	 * @param reviewID
	 *            the reviewID of the review we want to return
	 * @return the review we want to return
	 * @throws ReviewNotFoundException
	 *             if the review does not exist in the database
	 */
	private synchronized YelpReview getReview(String reviewID) throws ReviewNotFoundException {
		List<YelpReview> reviews = reviewList.stream().filter(review -> review.getReviewID().equals(reviewID))
				.collect(Collectors.toList());
		if (reviews.isEmpty()) {
			throw new ReviewNotFoundException();
		}
		return reviews.get(0);
	}

	/**
	 * @param reviewID
	 *            the reviewID of the review whos JSON string we wish to return
	 * @return the JSON string of this review
	 * @throws ReviewNotFoundException
	 *             if this review does not exist in the database
	 */
	public synchronized String getReviewJSON(String reviewID) throws ReviewNotFoundException {
		return this.getReview(reviewID).getJSON();
	}

	/**
	 * @param businessID
	 *            the businessID of the restaurant we want to return
	 * @return the restaurant we want to return
	 * @throws RestaurantNotFoundException
	 *             if the restaurant does not exist in the database
	 */
	private synchronized Restaurant getRestaurant(String businessID) throws RestaurantNotFoundException {
		List<Restaurant> restaurants = restaurantList.stream()
				.filter(restaurant -> restaurant.getBusinessID().equals(businessID)).collect(Collectors.toList());
		if (restaurants.isEmpty()) {
			throw new RestaurantNotFoundException();
		}
		return restaurants.get(0);
	}

	/**
	 * @param businessID
	 *            the businessID of the restaurant whose JSON string we wish to
	 *            return
	 * @return the JSON string of this restaurant
	 * @throws RestaurantNotFoundException
	 *             if the restaurant does not exist in the database
	 */
	public synchronized String getRestaurantJSON(String businessID) throws RestaurantNotFoundException {
		return this.getRestaurant(businessID).getJSON();
	}

	/**
	 * @param json
	 *            the JSON formatted string of the user we wish to add
	 * @throws InvalidInputException
	 *             if the string is not proper JSON format
	 */
	public synchronized void addUserJSON(String json) throws InvalidInputException {
		YelpUser user = new YelpUser(json);
		this.userList.add(user);
		this.userReviews.put(user.getUserID(), new ArrayList<String>());
	}

	/**
	 * @param name
	 *            the name of the user we wish to enter into the database
	 */
	public synchronized String addUser(String name) {
		YelpUser user = new YelpUser(userID, name);
		String userID = this.userID.toString();
		this.userID += 1;
		this.userList.add(user);
		this.userReviews.put(user.getUserID(), new ArrayList<String>());
		return userID;
	}

	/**
	 * @param date
	 *            the date of this review
	 * @param userID
	 *            the userID of the user who wrote the review
	 * @param businessID
	 *            the businessID of the restaurant this review is written about
	 * @param rating
	 *            the rating this restaurant was given in this review
	 * @throws UserNotFoundException
	 *             if this user does not exist in the database
	 * @throws RestaurantNotFoundException
	 *             if this restaurant does not exist in the database
	 */
	public synchronized String addReview(String date, String userID, String businessID, Integer rating)
			throws UserNotFoundException, RestaurantNotFoundException {
		YelpReview rev = new YelpReview(reviewID.toString(), date, userID, businessID, rating);
		this.getUser(userID).setReviewCount(this.getUser(userID).getReviewCount() + 1);
		this.getRestaurant(businessID).setReviewCount(this.getRestaurant(businessID).getReviewCount() + 1);
		this.reviewList.add(rev);
		double tUserRatings = this.userReviews.get(userID).stream().mapToDouble(s -> {
			try {
				return this.getUser(s).getAvgStars();
			} catch (UserNotFoundException e1) {
				return 0.0;
			}
		}).sum();
		double aUserRatings = (tUserRatings + rating) / this.getUser(userID).getReviewCount();
		this.getUser(userID).setAvgStars(aUserRatings);

		String reviewID = "";
		try {
			double tRestRatings = this.restaurantReviews.get(businessID).stream().mapToDouble(s -> {
				try {
					return this.getRestaurant(s).getStars();
				} catch (RestaurantNotFoundException e) {
					return 0.0;
				}
			}).sum();
			double aRestRatings = (tRestRatings + rating) / this.getRestaurant(businessID).getReviewCount();
			this.getRestaurant(businessID).setStars(aRestRatings);
			reviewID = this.reviewID.toString();
			this.reviewID += 1;
			visitedBy.get(businessID.toString()).add(userID.toString());
			this.restaurantReviews.get(businessID.toString()).add(reviewID.toString());
			this.userReviews.get(userID.toString()).add(reviewID.toString());
		} catch (Exception e) {

		}
		return reviewID;
	}

	/**
	 * @param json
	 *            the JSON formatted string of the review we wish to add to the
	 *            database
	 * @throws InvalidInputException
	 *             if this string is not in proper JSON format
	 * @throws UserNotFoundException
	 *             if this user does not exist in the database
	 * @throws RestaurantNotFoundException
	 *             if this restaurant does not exist in the database
	 */
	public synchronized void addReviewJSON(String json)
			throws InvalidInputException, UserNotFoundException, RestaurantNotFoundException {
		YelpReview rev = new YelpReview(json);
		this.getUser(rev.getUser()).setReviewCount(this.getUser(rev.getUser()).getReviewCount() + 1);
		this.getRestaurant(rev.getReviewed())
				.setReviewCount(this.getRestaurant(rev.getReviewed()).getReviewCount() + 1);
		this.reviewList.add(rev);
		double tUserRatings = this.userReviews.get(rev.getUser()).stream().mapToDouble(s -> {
			try {
				return this.getUser(s).getAvgStars();
			} catch (UserNotFoundException e) {
				return 0.0;
			}
		}).sum();
		double aUserRatings = (tUserRatings + rev.getRating()) / this.getUser(rev.getUser()).getReviewCount();
		this.getUser(rev.getUser()).setAvgStars(aUserRatings);
		double tRestRatings = this.restaurantReviews.get(rev.getReviewed()).stream().mapToDouble(s -> {
			try {
				return this.getRestaurant(s).getStars();
			} catch (RestaurantNotFoundException e) {
				return 0.0;
			}
		}).sum();
		double aRestRatings = (tRestRatings + rev.getRating()) / this.getRestaurant(rev.getReviewed()).getReviewCount();
		this.getRestaurant(rev.getReviewed()).setStars(aRestRatings);
		visitedBy.get(rev.getReviewed()).add(rev.getUser());
		this.restaurantReviews.get(rev.getReviewed()).add(rev.getReviewID());
		this.userReviews.get(rev.getUser()).add(rev.getReviewID());

	}

	/**
	 * @param name
	 *            the name of the restaurant we wish to add
	 */
	public synchronized String addRestaurant(String name) {
		Restaurant rest = new Restaurant(this.businessID, name);
		this.restaurantList.add(rest);
		this.businessID += 1;
		this.visitedBy.put(rest.getBusinessID(), new ArrayList<String>());
		this.restaurantReviews.put(rest.getBusinessID(), new ArrayList<String>());
		return this.businessID.toString();
	}

	/**
	 * @param json
	 *            the JSON formatted string of the restaurant we wish to add
	 * @throws InvalidInputException
	 *             if this string is not proper JSON format
	 */
	public synchronized void addRestaurantJSON(String json) throws InvalidInputException {
		Restaurant rest = new Restaurant(json);
		this.restaurantList.add(rest);
		this.visitedBy.put(rest.getBusinessID(), new ArrayList<String>());
		this.restaurantReviews.put(rest.getBusinessID(), new ArrayList<String>());
	}

	/**
	 * This method removes a user from the database. The users review count, average
	 * stars, and votes are wiped to zero. The userName is changed to "Deleted User"
	 * but the userID remains the same. The user is moved to a deletedUser list and
	 * removed from the userList
	 * 
	 * All of the user's reviews remain in the database
	 * 
	 * 
	 * @param userID
	 *            the user we wish to remove from the database
	 * @throws UserNotFoundException
	 *             if this user did not exist in the database to begin with
	 */
	public synchronized void removeUser(String userID) throws UserNotFoundException {
		YelpUser user = getUser(userID);
		this.userList.remove(user);
		this.deletedUserList.add(user);
		user.setAvgStars(0.0);
		user.setReviewCount(0);
		user.setUserName("Deleted User");
		user.setVotes("cool", 0);
		user.setVotes("funny", 0);
		user.setVotes("useful", 0);
	}

	/**
	 * This method removes a restaurant from the database. All reviews written about
	 * this restaurant are removed from the database and the review count, average
	 * stars and vote count of the users who wrote reviews about this restaurant are
	 * adjusted.
	 * 
	 * This restaurant is removed from the restaurantList, the RestaurantReviewsList
	 * and the visitedByList.
	 * 
	 * @param businessID
	 *            the businessID of the business we wish to remove
	 * @throws RestaurantNotFoundException
	 *             if this restaurant did not exist to begin with
	 * @throws ReviewNotFoundException
	 *             if a review for this restaurant does not exist
	 * @throws UserNotFoundException
	 *             if a user who wrote a review for this restaurant does not exist
	 */
	public synchronized void removeRestaurant(String businessID)
			throws RestaurantNotFoundException, ReviewNotFoundException, UserNotFoundException {
		ArrayList<String> killList = new ArrayList<String>();
		Restaurant rest = getRestaurant(businessID);
		for (YelpReview r : reviewList) {
			if (r.getReviewed().equals(businessID)) {
				killList.add(r.getReviewID());
			}
		}
		for (String ID : killList) {
			removeReview(ID);
		}
		this.restaurantReviews.remove(businessID);
		this.visitedBy.remove(businessID);
		this.restaurantList.remove(rest);

	}

	/**
	 * This method removes a review from the database. The review count, and average
	 * stars are adjusted for the restaurant and user associated with this review,
	 * as well as the votes for the user.
	 * 
	 * This review is removed from the reviewsList, restaurantReviewsList,
	 * userReviewsList and the user and restaurant are removed from the
	 * visitedByList
	 * 
	 * @param reviewID
	 *            the review ID of the review we wish to remove
	 * @throws ReviewNotFoundException
	 *             if the review did not exist to begin with
	 * @throws UserNotFoundException
	 *             if the user does not exist
	 * @throws RestaurantNotFoundException
	 *             if the restaurant does not exist
	 */
	public synchronized void removeReview(String reviewID)
			throws ReviewNotFoundException, UserNotFoundException, RestaurantNotFoundException {
		YelpReview rev = getReview(reviewID);
		int rating = getReview(reviewID).getRating();

		YelpUser user = getUser(getReview(reviewID).getUser());
		int revCountUser = user.getReviewCount();
		user.setReviewCount(revCountUser - 1);

		double userStars = user.getAvgStars() * revCountUser;
		user.setAvgStars((userStars - rating) / (revCountUser - 1));

		int totalCool = user.getVotes("cool");
		int totalUseful = user.getVotes("useful");
		int totalFunny = user.getVotes("funny");
		int cool = rev.getVotes("cool");
		int useful = rev.getVotes("useful");
		int funny = rev.getVotes("funny");

		user.setVotes("cool", totalCool - cool);
		user.setVotes("useful", totalUseful - useful);
		user.setVotes("funny", totalFunny - funny);

		Restaurant rest = getRestaurant(getReview(reviewID).getReviewed());
		int revCountRest = rest.getReviewCount();
		rest.setReviewCount(revCountRest - 1);

		double restStars = rest.getStars() * revCountRest;
		rest.setStars((restStars - rating) / (revCountRest - 1));

		this.reviewList.remove(rev);
		this.userReviews.get(rev.getUser()).remove(reviewID);
		this.restaurantReviews.get(rev.getReviewed()).remove(reviewID);
		this.visitedBy.get(rev.getReviewed()).remove(rev.getUser());

	}

	/**
	 * @param userID
	 *            the userID of the user we are checking to see if exists in the
	 *            database
	 * @return true if the user is in the database, false otherwise
	 */
	public synchronized boolean containsUser(String userID) {
		for (YelpUser y : this.userList) {
			if (y.getUserID().equals(userID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param businessID
	 *            the businessID of the restaurant we are checking to see if exists
	 *            in the database
	 * @return true if the restaurant exists, false otherwise
	 */
	public synchronized boolean containsRestaurant(String businessID) {
		for (Restaurant r : this.restaurantList) {
			if (r.getBusinessID().equals(businessID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param userID
	 *            the userID of the user whos reviews we want
	 * @return a list of the reviewIDs of reviews written by this user
	 */
	public synchronized ArrayList<String> getReviewsUser(String userID) {
		return new ArrayList<String>(this.userReviews.get(userID));
	}

	/**
	 * @param businessID
	 *            the businessID of the restaurant whose reviews we want
	 * @return a list of reviewIDs of reviews written about this restaurant
	 */
	public synchronized ArrayList<String> getReviewsRestaurant(String businessID) {
		return new ArrayList<String>(this.restaurantReviews.get(businessID));
	}

	/**
	 * @param businessID
	 *            the businessID of the restaurant of which we are finding the users
	 *            that have written a review about it
	 * @return a list of userIDs of users who have written reviews about this
	 *         restaurant
	 */
	public synchronized ArrayList<String> getUsersRestaurant(String businessID) {
		return new ArrayList<String>(this.visitedBy.get(businessID));
	}

	/**
	 * A method called by a server to process queries from clients. Currently
	 * supports GETRESTAURANT, ADDUSER, ADDRESTAURANT and ADDREVIEW queries, as well
	 * as structured queries to search for restaurants that conform to certain
	 * conditions
	 * 
	 * @param queryString
	 *            A string representing the query to be processed
	 * @return responseString A string representing the response to the query
	 * 
	 * @throws RestaurantNotFoundException
	 * @throws InvalidInputException
	 * @throws UserNotFoundException
	 * @throws ReviewNotFoundException
	 * @throws InvalidReviewStringException
	 * @throws InvalidUserStringException
	 * @throws InvalidRestaurantStringException
	 * @throws InvalidQueryException
	 * @throws NoMatchException
	 * 
	 */
	public synchronized String queryProcessor(String queryString)
			throws RestaurantNotFoundException, InvalidInputException, UserNotFoundException, ReviewNotFoundException,
			InvalidReviewStringException, InvalidUserStringException, InvalidRestaurantStringException,
			InvalidQueryException, JsonException, NoMatchException {
		// checking if this is a get restaurant query
		Pattern gRestPat = Pattern.compile("GETRESTAURANT (.*?)");
		Matcher gRestMat = gRestPat.matcher(queryString);
		if (gRestMat.find()) {
			try {
				String ID = queryString.split(" ")[1];
				return this.getRestaurantJSON(ID);
			} catch (RestaurantNotFoundException e) {
				throw new RestaurantNotFoundException();
			}
		}
		// checking if this is an add user query
		Pattern aUserPat = Pattern.compile("ADDUSER \\{(.*?)\\}");
		Matcher aUserMat = aUserPat.matcher(queryString);
		// need to figure out how to validate rest of json string
		if (aUserMat.find()) {
			String json = aUserMat.group(1); // validate this (check if extra info is in json format, then ignore it)
			try {
				JsonReader jsonReader = Json.createReader(new StringReader("{" + json + "}"));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
			} catch (Exception e) {
				throw new InvalidUserStringException();
			}
			Pattern namePat = Pattern.compile("\"name\": \"(.*?)\"}");
			Matcher nameMat = namePat.matcher(queryString);
			// if string includes name+other info
			Pattern namePat2 = Pattern.compile("\"name\": \"(.*?)\", ");
			Matcher nameMat2 = namePat2.matcher(queryString);
			if (nameMat2.find()) {
				String ID = this.addUser(nameMat2.group(1)); // save generated id
				try {
					return this.getUserJSON(ID);
				} catch (UserNotFoundException e) {
					// do nothing, it will be there
				}
			} else if (nameMat.find()) {
				String ID = this.addUser(nameMat.group(1));
				try {
					return this.getUserJSON(ID);
				} catch (UserNotFoundException e) {
					// do nothing, it will be there
				}
			} else {
				// there was no correctly formatted name in the string, this should also be
				// shown when json format invalid
				throw new InvalidUserStringException();
			}
		}

		// checks if this is an add restaurant query
		Pattern aRestPat = Pattern.compile("ADDRESTAURANT \\{(.*?)\\}");
		Matcher aRestMat = aRestPat.matcher(queryString);
		if (aRestMat.find()) {
			String json = aRestMat.group(1);
			JsonObject object = null;
			try {
				JsonReader jsonReader = Json.createReader(new StringReader("{" + json + "}"));
				object = jsonReader.readObject();
				jsonReader.close();
			} catch (Exception e) {
				throw new InvalidRestaurantStringException();
			}
			// ensures that json does not include business id or stars, we can add an option
			// to ignore these if they were added though
			try {
				json = "{\"open\": " + object.get("open") + ", \"url\": \"" + object.getString("url")
						+ "\", \"longitude\": " + object.get("longitude") + ", \"neighborhoods\": [";
				for (JsonValue j : (JsonArray) object.get("neighborhoods")) {
					json += j;
					if (((JsonArray) object.get("neighborhoods"))
							.indexOf(j) != ((JsonArray) object.get("neighborhoods")).size() - 1) {
						json += ", ";
					}
				}
				json += "], \"business_id\": \"" + this.businessID + "\", \"name\": " + object.get("name")
						+ ", \"categories\": [";
				for (JsonValue s : (JsonArray) object.get("categories")) {
					json += s;
					if (((JsonArray) object.get("categories"))
							.indexOf(s) != ((JsonArray) object.get("categories")).size() - 1) {
						json += ", ";
					}
				}
				json += "], \"state\": \"CA\", \"type\": \"business\", \"stars\": 0, \"city\": \"Berkeley\", \"full_address\": "
						+ object.get("full_address") + ", \"review_count\": " + object.getInt("review_count")
						+ ", \"photo_url\": " + object.get("photo_url")
						+ ", \"schools\": [\"University of California at Berkeley\"], \"latitude\": "
						+ object.get("latitude") + ", \"price\": " + object.getInt("price") + "}";
			} catch (Exception e) {
				throw new InvalidRestaurantStringException();
			}
			String ID = this.businessID.toString();
			this.businessID++;
			try {
				this.addRestaurantJSON(json);
				return this.getRestaurantJSON(ID);
			} catch (InvalidInputException e) {
				throw new InvalidRestaurantStringException();
			}
		}

		// checks if this is an add review query
		Pattern aRevPat = Pattern.compile("ADDREVIEW \\{(.*?)\\}");
		Matcher aRevMat = aRevPat.matcher(queryString);
		if (aRevMat.find()) {
			String json = aRevMat.group(1);
			JsonObject object = null;
			try {
				JsonReader jsonReader = Json.createReader(new StringReader("{" + json + "}"));
				object = jsonReader.readObject();
				jsonReader.close();
			} catch (Exception e) {
				throw new InvalidReviewStringException();
			}
			try {
				json = "{\"type\": \"review\", \"business_id\": " + object.get("business_id")
						+ ", \"votes\": {\"cool\": 0, \"useful\": 0, \"funny\": 0}, \"review_id\": \"" + this.reviewID
						+ "\", \"text\": " + object.get("text") + ", \"stars\": " + object.get("stars")
						+ ", \"user_id\": " + object.get("user_id") + ", \"date\": " + object.get("date") + "}";
			} catch (Exception e) {
				throw new InvalidReviewStringException();
			}
			String ID = this.reviewID.toString();
			this.reviewID++;
			try {
				this.addReviewJSON(json);
				return this.getReviewJSON(ID);
			} catch (InvalidInputException | UserNotFoundException | RestaurantNotFoundException
					| ReviewNotFoundException e) {
				if (e instanceof InvalidInputException) {
					throw new InvalidReviewStringException();
				}
				if (e instanceof UserNotFoundException) {
					throw new UserNotFoundException();
				}
				if (e instanceof RestaurantNotFoundException) {
					throw new RestaurantNotFoundException();
				}
			}
		} else if (queryString.contains("in(") | queryString.contains("category(") | queryString.contains("price")
				| queryString.contains("rating")) {
			return queryHelper(queryString).toString();
		} else {
			throw new InvalidQueryException();
		}
		throw new InvalidQueryException();
	}

	/**
	 * This method will parse a structured query string and return a set of
	 * restaurants that is a subset of this database' restaurantList that conforms
	 * to conditions specified in the input strings
	 * 
	 * @param conditions
	 *            queryString that outline the conditions of the desired restaurants
	 * @return jsonPool a set of json-formatted strings containing all the info of
	 *         the restauarants conforming to the conditions
	 * @throws InvalidQueryException
	 * @throws NoMatchException
	 */
	public synchronized HashSet<String> queryHelper(String conditions) throws InvalidQueryException, NoMatchException {
		HashSet<Restaurant> curPool = new HashSet<Restaurant>(this.restaurantList);
		String queryString = conditions;
		// one level of format checking: checks that there are spaces on either side of each double ampersand
		if (conditions.split("&&").length!=conditions.split(" && ").length){
			throw new InvalidQueryException();
		}
		// We process the query one subquery at a time, and remove each one from the
		// queryString once we have
		// appropriately filtered the restaurant list
		while (!queryString.equals("")) {
			// split subqueries based on double ampersand
			String curCond = queryString.split(" && ")[0];
			String toReplace = queryString.contains("&&") ? curCond + " && " : curCond;
			// save and remove current subquery from query string
			String newQuery = queryString.replace(toReplace, "");
			queryString = newQuery;
			// if subquery contains an "or"
			if (curCond.contains("||")) {
				// checks that spaces are included around ||
				if (!curCond.contains(" || ")) {
					throw new InvalidQueryException();
				}
				String[] orConds = curCond.split(" \\|\\| ");
				HashSet<Restaurant> newPool = new HashSet<Restaurant>();
				// for all conditions in or statement, filter current restaurant set, then add
				// them to a new pool
				for (int i = 0; i < orConds.length; i++) {
					String thisCond = orConds[i];
					if (i==0 && thisCond.startsWith("(")) {
						thisCond = thisCond.substring(1, thisCond.length());
					}
					else if (i==orConds.length-1 && thisCond.endsWith("))")) {
						thisCond = thisCond.substring(0, thisCond.length()-1);
					}
					HashSet<Restaurant> condPool = (HashSet<Restaurant>) oneQuery(thisCond, curPool);
					newPool.addAll(condPool);
				}
				curPool = newPool;
			} else {
				// if not or statement, then subquery requires just a single filter
				curPool = (HashSet<Restaurant>) oneQuery(curCond, curPool);
			}
			// if we've filtered set down to 0, we can give up
			if (curPool.size() == 0) {
				throw new NoMatchException();
			}
		}
		// take current pool of restaurants and add json strings of each restaurant to a
		// new set
		HashSet<String> jsonPool = new HashSet<String>();
		for (Restaurant r : curPool) {
			jsonPool.add(r.getJSON());
		}
		return jsonPool;
	}

	/**
	 * A helper method that processes one query and filters a pool based on the
	 * condition in that query, returning a new pool
	 * 
	 * @param condition
	 *            query string containing filtering condition
	 * @param pool
	 *            current restaurant pool
	 * @return new restaurant pool after filtering
	 * @throws InvalidQueryException
	 * 			If query not in correct format
	 */
	private Set<Restaurant> oneQuery(String condition, Set<Restaurant> pool) throws InvalidQueryException {
		// checks if this is a category condition, and checks that it is formatted
		// correctly
		if (condition.contains("category(") && condition.split("\\) ").length == 1 &&
			condition.split("\\(")[0].replaceAll("\\(", "").equals("category") && 
			condition.endsWith(")")) {
			Pattern catPat = Pattern.compile("category\\((.*?)\\)");
			Matcher catMat = catPat.matcher(condition);
			if (catMat.find()) {
				String category = catMat.group(1);
				// filters all restaurants in current pool that are in specified category
				return pool.stream().filter(restaurant -> restaurant.getCategories().contains(category))
						.collect(Collectors.toSet());
			} else {
				// if the condition does not match this pattern, throw exception
				throw new InvalidQueryException();
			}

		}
		// checks if this is a name condition, and checks that it is formatted correctly
		if (condition.contains("name(") && condition.split("\\) ").length == 1 &&
			condition.split("\\(")[0].replaceAll("\\(", "").equals("name") && 
			condition.endsWith(")")) {
			Pattern namePat = Pattern.compile("name\\((.*?)\\)");
			Matcher nameMat = namePat.matcher(condition);
			if (nameMat.find()) {
				String name = nameMat.group(1);
				// filters all restaurants in current pool that have specified name
				return pool.stream().filter(restaurant -> restaurant.getBusinessName().equals(name))
						.collect(Collectors.toSet());
			} else {
				// if the condition does not match this pattern, throw exception
				throw new InvalidQueryException();
			}
		}
		// checks if this is an in condition, and checks that it is formatted correctly
		if (condition.contains("in(") && condition.split("\\) ").length == 1 &&
			condition.split("\\(")[0].replaceAll("\\(", "").equals("in") && 
			condition.endsWith(")")) {
			Pattern inPat = Pattern.compile("in\\((.*?)\\)");
			Matcher inMat = inPat.matcher(condition);
			if (inMat.find()) {
				String area = condition.split("\\(")[1].replaceAll("\\)", "");
				// filters current pool for restaurants in specified neighborhood
				return pool.stream().filter(restaurant -> restaurant.getLocation().getNeighborhoods().contains(area))
						.collect(Collectors.toSet());
			} else {
				// if the condition does not match this pattern, throw exception
				throw new InvalidQueryException();
			}
		}
		// checks if this is a rating condition
		if (condition.contains("rating")) {
			String[] conditionArr = condition.split(" ");
			// partially checks format of condition, throwing exception if this condition
			// violated
			if (conditionArr.length != 3 && !conditionArr[0].replaceAll("\\(", "").equals("rating")) {
				throw new InvalidQueryException();
			}
			try {
				// retrieves rating integer from string (exception is thrown if expected
				// location of rating is not an integer)
				Integer rating = Integer.parseInt(conditionArr[2].replaceAll("\\)", ""));
				if (rating > 5 | rating < 1) {
					throw new InvalidQueryException();
				}
				// filters pool based on rating being in specified range depending on symbol
				if (conditionArr[1].equals(">=")) {
					return pool.stream().filter(restaurant -> restaurant.getStars() >= rating)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals("<=")) {
					return pool.stream().filter(restaurant -> restaurant.getStars() <= rating)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals(">")) {
					return pool.stream().filter(restaurant -> restaurant.getStars() > rating)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals("<")) {
					return pool.stream().filter(restaurant -> restaurant.getStars() < rating)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals("=")) {
					return pool.stream().filter(restaurant -> restaurant.getStars() == (double) rating)
							.collect(Collectors.toSet());
				}
			} catch (NumberFormatException e) {
				// if we tried to parse a rating that wasn't an integer, throw an exception
				throw new InvalidQueryException();
			}
		}
		// checks if this is a price condition
		if (condition.contains("price")) {
			String[] conditionArr = condition.split(" ");
			// partially checks format of condition, throwing exception if this condition
			// violated
			if (conditionArr.length != 3 && !conditionArr[0].replaceAll("\\(", "").equals("price")) {
				throw new InvalidQueryException();
			}
			try {
				// retrieves price integer from string (exception is thrown if expected location
				// of price is not an integer)
				Integer price = Integer.parseInt(conditionArr[2].replaceAll("\\)", ""));
				if (price > 5 | price < 1) {
					throw new InvalidQueryException();
				}
				// filters pool based on price being in specified range depending on symbol
				if (conditionArr[1].equals(">=")) {
					return pool.stream().filter(restaurant -> restaurant.getPrice() >= price)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals("<=")) {
					return pool.stream().filter(restaurant -> restaurant.getPrice() <= price)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals(">")) {
					return pool.stream().filter(restaurant -> restaurant.getPrice() > price)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals("<")) {
					return pool.stream().filter(restaurant -> restaurant.getPrice() < price)
							.collect(Collectors.toSet());
				}
				if (conditionArr[1].equals("=")) {
					return pool.stream().filter(restaurant -> restaurant.getPrice() == (double) price)
							.collect(Collectors.toSet());
				}
			} catch (NumberFormatException e) {
				// if we tried to parse a price that wasn't an integer, throw an exception
				throw new InvalidQueryException();
			}
		}
		// if the condition did not meet any of these descriptions, throw exception
		throw new InvalidQueryException();
	}

}


