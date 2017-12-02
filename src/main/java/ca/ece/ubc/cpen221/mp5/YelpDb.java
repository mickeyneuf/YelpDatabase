package ca.ece.ubc.cpen221.mp5;

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

import javax.json.*;

public class YelpDb implements MP5Db<Restaurant> {

	private ArrayList<YelpUser> userList;
	private ArrayList<Restaurant> restaurantList;
	private ArrayList<YelpReview> reviewList;

	private TreeMap<String, ArrayList<String>> visitedBy;
	private TreeMap<String, ArrayList<String>> restaurantReviews;
	private TreeMap<String, ArrayList<String>> userReviews;
	
	private Integer userID;
	private Integer reviewID;
	private Integer businessID;

	public YelpDb(String restaurantFile, String reviewFile, String userFile) throws IOException {
		Scanner scanUser = new Scanner(new File(userFile));
		Scanner scanRestaurant = new Scanner(new File(restaurantFile));
		Scanner scanReview = new Scanner(new File(reviewFile));
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
	public Set<Restaurant> getMatches(String queryString) {
		if (restaurantList.contains(this.getRestaurant(queryString))) {
			Set<Restaurant> set = new HashSet<Restaurant>();
			set.add(this.getRestaurant(queryString));
			return set;
		}
		
		return new HashSet<>();
	}

	public ArrayList<HashSet<String>> kMeansClusters(int k) {
		Random rand = new Random();
		HashMap<String, ArrayList<String>> clusters = new HashMap<String, ArrayList<String>>();
		ArrayList<HashSet<String>> clusterList = new ArrayList<HashSet<String>>();
		
		// choosing centroids and putting them on map
		// no clusters can be empty because we chose restaurant locations as centroids
		for (int i = 0; i<k; i++) {
			int n = rand.nextInt(this.restaurantList.size()-1)+0;
			clusters.put(restaurantList.get(n).getBusinessID(), new ArrayList<String>());
		}
		
		for(Restaurant r : this.restaurantList) {
			String closest = null;
			double mindistance = Double.MAX_VALUE;
			if(!clusters.keySet().contains(r.getBusinessID())) {
				for (String centroid : clusters.keySet()) {
					double distance = Math.sqrt(Math.pow(r.getLocation().getLatitude()-this.getRestaurant(centroid).getLocation().getLatitude(), 2)+
									  Math.pow(r.getLocation().getLongitude()-this.getRestaurant(centroid).getLocation().getLongitude(), 2));
					if(distance < mindistance) {
						closest = centroid;
						mindistance = distance;
					}
				}
			clusters.get(closest).add(r.getBusinessID());
			}
		}
		
		for (String rest1 : clusters.keySet()) {
			HashSet<String> cluster = new HashSet<String>();
			cluster.add(rest1);
			cluster.addAll(clusters.get(rest1));
			clusterList.add(cluster);
			}
		
		return clusterList;
	}
	
	@Override
	public String kMeansClusters_json(int k) {
		ArrayList<HashSet<String>> setList = this.kMeansClusters(k);
		String json = null;
		int c = 0;
		for (HashSet<String> cluster : setList) {
			for (String r : cluster) {
				json+="{\"x\": "+this.getRestaurant(r).getLocation().getLongitude()
					  +", \"y\": "+this.getRestaurant(r).getLocation().getLatitude()
					  +", \"name\": \"cluster\": "+c+", \"weight\": 1.0}";
			}
			c++;
			if(c<setList.size()) {
				json+=", ";
			}
		}
		return json;
	}
	
	@Override
	public ToDoubleBiFunction<MP5Db<Restaurant>, String> getPredictorFunction(String user) {
		double sxx = 0;
		double sxy = 0;
		double avgx = 0;
		double avgy = 0;

		List<String> restID = reviewList.stream().filter(rev -> rev.getUser().equals(user))
				.map(YelpReview::getReviewed).collect(Collectors.toList());

		List<Integer> prices = restaurantList.stream().filter(rest -> restID.contains(rest.getBusinessID()))
				.map(Restaurant::getPrice).collect(Collectors.toList());

		int totalPrice = prices.stream().reduce(0, (x, y) -> x + y);
		
		List<Integer> ratings = reviewList.stream().filter(rev -> rev.getUser().equals(user))
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
	
	private YelpUser getUser(String userID) {
		List<YelpUser> users = userList.stream().filter(user -> user.getUserID().equals(userID)).collect(Collectors.toList());
		return users.get(0);
	}
	
	public String getUserJSON(String userID) {
		return this.getUser(userID).getJSON();
	}
	
	private YelpReview getReview(String reviewID) {
		List<YelpReview> reviews = reviewList.stream().filter(review -> review.getReviewID().equals(reviewID)).collect(Collectors.toList());
		return reviews.get(0);
	}
	
	public String getReviewJSON(String reviewID) {
		return this.getReview(reviewID).getJSON();
	}
	
	private Restaurant getRestaurant(String businessID) {
		List<Restaurant> restaurants = restaurantList.stream().filter(restaurant -> restaurant.getBusinessID().equals(businessID)).collect(Collectors.toList());
		return restaurants.get(0);
	}
	
	public String getRestaurantJSON(String restaurantID) {
		return this.getRestaurant(restaurantID).getJSON();
	}
	
	public void addUserJSON(String json) {
		YelpUser user = new YelpUser(json);
		this.userList.add(user);
		this.userReviews.put(user.getUserID(), new ArrayList<String>());
	}

	public void addUser(String name) {
		YelpUser user = new YelpUser(userID, name);
		this.userID += 1;
		this.userList.add(user);
		this.userReviews.put(user.getUserID(), new ArrayList<String>());
	}
	
	public void addReview(String date, String userID, String businessID, Integer rating) {
		YelpReview rev = new YelpReview(reviewID.toString(), date, userID, businessID, rating);
		this.getUser(userID).setReviewCount(this.getUser(userID).getReviewCount()+1);
		this.getRestaurant(businessID).setReviewCount(this.getRestaurant(businessID).getReviewCount()+1);
		this.reviewList.add(rev);
		double tUserRatings = this.userReviews.get(userID).stream().mapToDouble(s -> this.getUser(s).getAvgStars()).sum();
		double aUserRatings = (tUserRatings+rating)/this.getUser(userID).getReviewCount();
		this.getUser(userID).setAvgStars(aUserRatings);
		double tRestRatings = this.restaurantReviews.get(businessID).stream().mapToDouble(s -> this.getRestaurant(s).getStars()).sum();
		double aRestRatings = (tRestRatings+rating)/this.getRestaurant(businessID).getReviewCount();
		this.getRestaurant(businessID).setStars(aRestRatings);
		this.reviewID += 1;
		visitedBy.get(businessID.toString()).add(userID.toString());
		this.restaurantReviews.get(businessID.toString()).add(reviewID.toString());
		this.userReviews.get(userID.toString()).add(reviewID.toString());
	}
	
	
	public void addReviewJSON(String json) {
		YelpReview rev = new YelpReview(json);
		this.getUser(rev.getUser()).setReviewCount(this.getUser(rev.getUser()).getReviewCount()+1);
		this.getRestaurant(rev.getReviewed()).setReviewCount(this.getRestaurant(rev.getReviewed()).getReviewCount()+1);
		this.reviewList.add(rev);
		double tUserRatings = this.userReviews.get(rev.getUser()).stream().mapToDouble(s -> this.getUser(s).getAvgStars()).sum();
		double aUserRatings = (tUserRatings+rev.getRating())/this.getUser(rev.getUser()).getReviewCount();
		this.getUser(rev.getUser()).setAvgStars(aUserRatings);
		double tRestRatings = this.restaurantReviews.get(rev.getReviewed()).stream().mapToDouble(s -> this.getRestaurant(s).getStars()).sum();
		double aRestRatings = (tRestRatings+rev.getRating())/this.getRestaurant(rev.getReviewed()).getReviewCount();
		this.getRestaurant(rev.getReviewed()).setStars(aRestRatings);
		visitedBy.get(rev.getReviewed()).add(rev.getUser());
		this.restaurantReviews.get(rev.getReviewed()).add(rev.getReviewID());
		this.userReviews.get(rev.getUser()).add(rev.getReviewID());
		
	}

	public void addRestaurant(String name) {
		Restaurant rest = new Restaurant(this.businessID, name);
		this.restaurantList.add(rest);
		this.businessID += 1;
		this.visitedBy.put(rest.getBusinessID(), new ArrayList<String>());
		this.restaurantReviews.put(rest.getBusinessID(), new ArrayList<String>());
	}
	
	public void addRestaurantJSON(String json) {
		Restaurant rest = new Restaurant(json);
		this.restaurantList.add(rest);
		this.visitedBy.put(rest.getBusinessID(), new ArrayList<String>());
		this.restaurantReviews.put(rest.getBusinessID(), new ArrayList<String>());
	}
	//COMPLETE THIS
	public void removeUser(String userID) {
		this.userList.remove(this.getUser(userID));
	}
	
	public void removeRestaurant(String businessID) {
		for (String userID : restaurantReviews.get(businessID)) {
			this.getUser(userID).set()
		}
	}

}
