package ca.ece.ubc.cpen221.mp5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.ToDoubleBiFunction;
import javax.json.*;

public class YelpDb implements MP5Db {

	private TreeMap<String, User> userList;
	private TreeMap<String, Business> restaurantList;
	private TreeMap<String, Review> reviewList;
	private TreeMap<User, List<Business>> visitedBy;
	private Integer userID;
	private Integer reviewID;
	private Integer businessID;

	public YelpDb(String restaurantFile, String reviewFile, String userFile) throws IOException {
		Scanner scanUser = new Scanner(new File(userFile));
		Scanner scanRestaurant = new Scanner(new File(restaurantFile));
		Scanner scanReview = new Scanner(new File(reviewFile));
		TreeMap<String, User> user = new TreeMap<String, User>();
		TreeMap<String, Business> restaurant = new TreeMap<String, Business>();
		TreeMap<String, Review> review = new TreeMap<String, Review>();
		this.userID = 0;
		this.reviewID = 0;
		this.businessID = 0;

		while (scanUser.hasNext()) {
			User newUser = new YelpUser(scanUser.nextLine());
			user.put(newUser.getUserID(), newUser);
		}
		scanUser.close();
		this.userList = user;

		while (scanRestaurant.hasNext()) {
			Business rest = new Restaurant(scanRestaurant.nextLine());
			restaurant.put(rest.getBusinessID(), rest);
		}
		scanRestaurant.close();
		this.restaurantList = restaurant;

		while (scanReview.hasNext()) {
			YelpReview rev = new YelpReview(scanReview.nextLine());
			review.put(rev.getReviewID(), rev);
			visitedBy.get(userList.get(rev.getPoster())).add(restaurantList.get((rev.getReviewed())));
		}
		scanReview.close();
		this.reviewList = review;

	}

	@Override
	public Set getMatches(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String kMeansClusters_json(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoubleBiFunction getPredictorFunction(String user) {

		return null;
	}

	public void addUser() {
		User user = new YelpUser(userID);
		this.userList.put(userID.toString(), user);
		this.userID += 1;
		this.visitedBy.put(user, new ArrayList<Business>());
	}

	public void addReview() {

		Review rev = new YelpReview(reviewID);
		this.reviewList.put(reviewID.toString(), rev);
		this.reviewID += 1;
		visitedBy.get(userList.get(rev.getPoster())).add(restaurantList.get((rev.getReviewed())));
	}

	public void addRestaurant(String longitude, String latitude) {

		this.restaurantList.put(businessID.toString(), new Restaurant(businessID, latitude, longitude));
		this.businessID += 1;

	}

}
