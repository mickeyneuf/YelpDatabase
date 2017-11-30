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
			visitedBy.get(userList.get(rev.getPoster())).add(restaurantList.get((rev.getReviewed())));
		}
		scanReview.close();
		this.reviewList = review;
		this.visitedBy = visitedBy;

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
		List<Restaurant> restaurants = visitedBy.get(userList.get(user));
		double sxx = 0;
		double syy = 0;
		double sxy = 0;
		double avgx = 0;
		double avgy = 0;
		
		/*
		 * We should probably use map filter reduce here... I have the list of restaurants reviewed by the user
		 * but we still need the rating of the review by that user for that restaurant
		 * which will take lots of searching and iteration without map filter reduce i think
		 * 
		 */

		for (Restaurant r : restaurants) {
			avgx += restaurantList.get(r.getBusinessID()).getPrice();
			for (String yr : reviewList.keySet()) {
				if (reviewList.get(yr).getPoster().equals(user) && reviewList.get(yr).getReviewed().equals(r.getBusinessID())) {
					avgy += reviewList.get(yr).getRating();
				}
			}
		}
		
		return null;
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
		visitedBy.get(userList.get(rev.getPoster())).add(restaurantList.get((rev.getReviewed())));
	}

	public void addRestaurant(String longitude, String latitude) {

		this.restaurantList.put(businessID.toString(), new Restaurant(businessID, latitude, longitude));
		this.businessID += 1;

	}

}
