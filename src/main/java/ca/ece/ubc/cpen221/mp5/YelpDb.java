package ca.ece.ubc.cpen221.mp5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;

import javax.json.*;

public class YelpDb implements MP5Db {

	private TreeMap<String, YelpUser> userList;
	private TreeMap<String, Restaurant> restaurantList;
	private TreeMap<String, YelpReview> reviewList;
	private TreeMap<YelpUser, List<Restaurant>> visitedBy;
	private Integer userID;
	private Integer reviewID;
	private Integer businessID;

	public YelpDb(String restaurantFile, String reviewFile, String userFile) throws IOException {
		Scanner scanUser = new Scanner(new File(userFile));
		Scanner scanRestaurant = new Scanner(new File(restaurantFile));
		Scanner scanReview = new Scanner(new File(reviewFile));
		TreeMap<String, YelpUser> user = new TreeMap<String, YelpUser>();
		TreeMap<String, Restaurant> restaurant = new TreeMap<String, Restaurant>();
		TreeMap<String, YelpReview> review = new TreeMap<String, YelpReview>();
		TreeMap<YelpUser, List<Restaurant>> visitedBy = new TreeMap<YelpUser, List<Restaurant>>();
		this.userID = 0;
		this.reviewID = 0;
		this.businessID = 0;

		while (scanUser.hasNext()) {
			YelpUser newUser = new YelpUser(scanUser.nextLine());
			user.put(newUser.getUserID(), newUser);
		}
		scanUser.close();
		this.userList = user;

		while (scanRestaurant.hasNext()) {
			Restaurant rest = new Restaurant(scanRestaurant.nextLine());
			restaurant.put(rest.getBusinessID(), rest);
		}
		scanRestaurant.close();
		this.restaurantList = restaurant;

		while (scanReview.hasNext()) {
			YelpReview rev = new YelpReview(scanReview.nextLine());
			review.put(rev.getReviewID(), rev);
			visitedBy.get(userList.get(rev.getUser())).add(restaurantList.get((rev.getReviewed())));
		}
		scanReview.close();
		this.reviewList = review;
		this.visitedBy = visitedBy;

	}

	/**
	 * Collects all objects inside the database that match the queryString
	 * As of right now, it is assumed that queryString will be a businessID 
	 * and getMatches will return the restaurant that matches the ID.
	 * 
	 * Returns an empty set of no restaurant with a matching ID can be found in
	 * the database
	 * 
	 * @param queryString 
	 *                    a businessID that we are trying to find the restaurant for
	 * 
	 * @return Set 
	 *             the set of all objects that match the queryString; in this case,
	 *             the restaurant with the matching ID
	 * 
	 */
	@Override
	public Set getMatches(String queryString) {
		if (restaurantList.containsKey(queryString)) {
			Set<Restaurant> set = new HashSet<Restaurant>();
			set.add(restaurantList.get(queryString));
			return set;
		}
		
		return new HashSet();
	}

	@Override
	public String kMeansClusters_json(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoubleBiFunction<MP5Db<Restaurant>, String> getPredictorFunction(String user) {
		double sxx = 0;
		double sxy = 0;
		double avgx = 0;
		double avgy = 0;

		List<String> restID = reviewList.values().stream().filter(rev -> rev.getUser() == user)
				.map(YelpReview::getReviewed).collect(Collectors.toList());

		List<Integer> prices = restaurantList.keySet().stream().filter(rest -> restID.contains(rest))
				.map(rest -> restaurantList.get(rest)).map(Restaurant::getPrice).collect(Collectors.toList());

		int totalPrice = prices.stream().reduce(0, (x, y) -> x + y);

		List<Integer> ratings = reviewList.values().stream().filter(rev -> rev.getUser() == user)
				.map(YelpReview::getRating).collect(Collectors.toList());

		int totalRating = ratings.stream().reduce(0, (x, y) -> x + y);

		avgx = totalPrice / restID.size();
		avgy = totalRating / restID.size();

		assert(prices.size() == ratings.size());
		
		for (int i = 0; i < prices.size(); i++) {
			sxx += Math.pow((prices.get(i) - avgx), 2);
			sxy += (prices.get(i) - avgx) * (ratings.get(i) - avgy);

		}

		double b = sxy / sxx;
		double a = avgy - (b * avgx);

		return new PredictorFunction(a,b);
	}

	public void addUser() {
		YelpUser user = new YelpUser(userID);
		this.userList.put(userID.toString(), user);
		this.userID += 1;
		this.visitedBy.put(user, new ArrayList<Restaurant>());
	}

	public void addReview() {

		YelpReview rev = new YelpReview(reviewID);
		this.reviewList.put(reviewID.toString(), rev);
		this.reviewID += 1;
		visitedBy.get(userList.get(rev.getUser())).add(restaurantList.get((rev.getReviewed())));
	}

	public void addRestaurant(String longitude, String latitude) {

		this.restaurantList.put(businessID.toString(), new Restaurant(businessID, latitude, longitude));
		this.businessID += 1;

	}

}
