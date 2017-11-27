package ca.ece.ubc.cpen221.mp5;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;
import javax.json.*;

public class YelpDb implements MP5Db {
	
	private List<User> userList;
	private List<Business> restaurantList;
	private List<Review> reviewList;

	
	public YelpDb (String userFile, String restaurantFile, String reviewFile ) throws IOException {
		Scanner scanUser = new Scanner(new File(userFile));
		Scanner scanRestaurant = new Scanner(new File(restaurantFile));
		Scanner scanReview = new Scanner(new File(reviewFile));
		List<User> user = new ArrayList<User>();
		List<Business> restaurant = new ArrayList<Business>();
		List<Review> review = new ArrayList<Review>();
		
		while (scanUser.hasNext() ) {
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

}
