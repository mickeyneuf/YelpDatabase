package ca.ece.ubc.cpen221.mp5;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

public class YelpDb implements MP5Db<Restaurant> {

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
	public synchronized Set<Restaurant> getMatches(String queryString) {
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
	 * Method that computes and represents a k-means clustering of the restaurants in this database.
	 * Helper method for kMeansClusters_json.
	 * @param k
	 * 		number of centroids/clusters
	 * @return
	 * 		A list of non-empty sets of restaurant IDs representing the clusters
	 * @throws RestaurantNotFoundException
	 * @throws InvalidInputException 
	 * @throws ArrayIndexOutOfBoundsException 
	 */
	public synchronized ArrayList<HashSet<String>> kMeansClusters(int k) throws RestaurantNotFoundException, ArrayIndexOutOfBoundsException {
		Random rand = new Random();
		HashMap<ArrayList<Double>, ArrayList<String>> clusters = new HashMap<ArrayList<Double>, ArrayList<String>>();
		ArrayList<HashSet<String>> clusterList = new ArrayList<HashSet<String>>();
		// choosing centroids and putting them on map
		// no clusters can be empty because we chose restaurant locations as centroids
		while (clusters.keySet().size() < k) {
			int n = rand.nextInt(this.restaurantList.size()+1);
			ArrayList<Double> centr = new ArrayList<Double>();
			centr.add(restaurantList.get(n).getLocation().getLongitude());
			centr.add(restaurantList.get(n).getLocation().getLatitude());
			clusters.put(centr, new ArrayList<String>());
		}
		// make the centroid restaurants keys and non-centroid restaurants values of a map
		// add non-centroid values to the centroid to which they are closest, by calculating
		// euclidean distance with longitudes and latitudes
		for (Restaurant r : this.restaurantList) {
			ArrayList<Double> closest = null;
			double mindistance = Double.MAX_VALUE;
				for (ArrayList<Double> centro : clusters.keySet()) {
					double distance = Math.sqrt(Math.pow(
							r.getLocation().getLatitude() - centro.get(1), 2)
							+ Math.pow(r.getLocation().getLongitude()
									- centro.get(0), 2));
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
			for (Restaurant r : this.restaurantList) {
				double x = r.getLocation().getLongitude();
				double y = r.getLocation().getLatitude();
				double mindist = Double.MAX_VALUE;
				ArrayList<Double> newCent = null;
				for(ArrayList<Double> centroi : clusters.keySet()) {
					if (Math.sqrt(Math.pow(x - centroi.get(0), 2)+ Math.pow(y - centroi.get(0), 2))<mindist){
						mindist = Math.sqrt(Math.pow(x - centroi.get(0), 2)+ Math.pow(y- centroi.get(0), 2));
						newCent = centroi;
					}
				}
				if(clusters.get(newCent).contains(r.getBusinessID())){
					clusters.get(newCent).remove(r.getBusinessID());
					for(ArrayList<Double> a : clusters.keySet()) {
						if(clusters.get(a).contains(r.getBusinessID())) {
							clusters.get(a).remove(r.getBusinessID());
							good = false;
						}
					}
				}
			}
			for (ArrayList<Double> a : clusters.keySet()) {
				if(clusters.get(a).isEmpty()) {
					good = false;
					break;
				}
			}
		}while(!good);
		
		for (ArrayList<Double> a : clusters.keySet()) {
			HashSet<String> clusterSet = new HashSet<String>();
			clusterSet.addAll(clusters.get(a));
			clusterList.add(clusterSet);
		}
		return clusterList;
	}
	
	/**
	 * Cluster objects into k clusters using k-means clustering
	 * 
	 * @param k
	 *            number of clusters to create (0 < k <= number of objects)
	 * @return a String, in JSON format, that represents the clusters, or null if a restaurant is no longer
	 * 		   found in the database 
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

	private synchronized YelpUser getUser(String userID) throws UserNotFoundException {
		List<YelpUser> users = userList.stream().filter(user -> user.getUserID().equals(userID))
				.collect(Collectors.toList());
		if (users.isEmpty()) {
			throw new UserNotFoundException();
		}
		return users.get(0);
	}

	public synchronized String getUserJSON(String userID) throws UserNotFoundException {
		return this.getUser(userID).getJSON();
	}

	private synchronized YelpReview getReview(String reviewID) throws ReviewNotFoundException {
		List<YelpReview> reviews = reviewList.stream().filter(review -> review.getReviewID().equals(reviewID))
				.collect(Collectors.toList());
		if (reviews.isEmpty()) {
			throw new ReviewNotFoundException();
		}
		return reviews.get(0);
	}

	public synchronized String getReviewJSON(String reviewID) throws ReviewNotFoundException {
		return this.getReview(reviewID).getJSON();
	}

	private synchronized Restaurant getRestaurant(String businessID) throws RestaurantNotFoundException {
		List<Restaurant> restaurants = restaurantList.stream()
				.filter(restaurant -> restaurant.getBusinessID().equals(businessID)).collect(Collectors.toList());
		if (restaurants.isEmpty()) {
			throw new RestaurantNotFoundException();
		}
		return restaurants.get(0);
	}

	public synchronized String getRestaurantJSON(String restaurantID) throws RestaurantNotFoundException {
		return this.getRestaurant(restaurantID).getJSON();
	}

	public synchronized void addUserJSON(String json) throws InvalidInputException {
		YelpUser user = new YelpUser(json);
		this.userList.add(user);
		this.userReviews.put(user.getUserID(), new ArrayList<String>());
	}

	public synchronized void addUser(String name) {
		YelpUser user = new YelpUser(userID, name);
		this.userID += 1;
		this.userList.add(user);
		this.userReviews.put(user.getUserID(), new ArrayList<String>());
	}

	public synchronized void addReview(String date, String userID, String businessID, Integer rating)
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
			this.reviewID += 1;
			visitedBy.get(businessID.toString()).add(userID.toString());
			this.restaurantReviews.get(businessID.toString()).add(reviewID.toString());
			this.userReviews.get(userID.toString()).add(reviewID.toString());
		} catch (Exception e) {

		}
	}

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

	public synchronized void addRestaurant(String name) {
		Restaurant rest = new Restaurant(this.businessID, name);
		this.restaurantList.add(rest);
		this.businessID += 1;
		this.visitedBy.put(rest.getBusinessID(), new ArrayList<String>());
		this.restaurantReviews.put(rest.getBusinessID(), new ArrayList<String>());
	}

	public void addRestaurantJSON(String json) throws InvalidInputException {
		Restaurant rest = new Restaurant(json);
		this.restaurantList.add(rest);
		this.visitedBy.put(rest.getBusinessID(), new ArrayList<String>());
		this.restaurantReviews.put(rest.getBusinessID(), new ArrayList<String>());
	}

	public void removeUser(String userID) throws UserNotFoundException {
		YelpUser user = getUser(userID);
		this.userList.remove(user);
		this.deletedUserList.add(user);
		user.setAvgStars(0.0);
		user.setReviewCount(0);
	}

	public synchronized void removeRestaurant(String businessID) throws RestaurantNotFoundException, ReviewNotFoundException, UserNotFoundException {
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
	
	public synchronized boolean containsUser(String userID) {
		for (YelpUser y : this.userList) {
			if(y.getUserID().equals(userID)) {
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean containsRestaurant(String businessID) {
		for (Restaurant r : this.restaurantList) {
			if(r.getBusinessID().equals(businessID)) {
				return true;
			}
		}
		return false;
	}
	
	public synchronized ArrayList<String> getReviewsUser(String userID) {
		return new ArrayList<String>(this.userReviews.get(userID));
	}
	
	public synchronized ArrayList<String> getReviewsRestaurant(String businessID) {
		return new ArrayList<String>(this.restaurantReviews.get(businessID));
	}
	
	public synchronized ArrayList<String> getUsersRestaurant(String businessID) {
		return new ArrayList<String>(this.visitedBy.get(businessID));
	}

}