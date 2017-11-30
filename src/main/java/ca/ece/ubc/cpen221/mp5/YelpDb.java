package ca.ece.ubc.cpen221.mp5;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.ToDoubleBiFunction;
import javax.json.*;

public class YelpDb implements MP5Db {

	private TreeSet<User> userList;
	private TreeSet<Business> restaurantList;
	private TreeSet<Review> reviewList;
	private Integer userID;
	private Integer reviewID;
	private Integer businessID;

	public YelpDb(String userFile, String restaurantFile, String reviewFile) throws IOException {
		Scanner scanUser = new Scanner(new File(userFile));
		Scanner scanRestaurant = new Scanner(new File(restaurantFile));
		Scanner scanReview = new Scanner(new File(reviewFile));
		TreeSet<User> user = new TreeSet<User>();
		TreeSet<Business> restaurant = new TreeSet<Business>();
		TreeSet<Review> review = new TreeSet<Review>();
		this.userID = 0;
		this.reviewID = 0;
		this.businessID = 0;

		while (scanUser.hasNext()) {
			User newUser = new YelpUser(scanUser.nextLine());
			user.add(newUser);
		}
		scanUser.close();

		while (scanRestaurant.hasNext()) {
			Business rest = new Restaurant(scanRestaurant.nextLine());
			restaurant.add(rest);
		}
		scanRestaurant.close();

		while (scanReview.hasNext()) {
			Review rev = new YelpReview(scanReview.nextLine());
			review.add(rev);
		}
		scanReview.close();

		this.userList = user;
		this.restaurantList = restaurant;
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
		// TODO Auto-generated method stub
		return null;
	}

	public void addUser() {
		this.userList.add(new YelpUser(userID));
		this.userID += 1;
	}

	public void addReview() {

		this.reviewList.add(new YelpReview(reviewID));
		this.reviewID += 1;
	}

	public void addRestaurant() {

	}
}
