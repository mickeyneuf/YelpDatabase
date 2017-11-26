Caelin Preston
Mikayla Neufeld
November 23, 2017
MP5 Database Design Plan Notes:
Datatypes we will design:
	Make Object data types for:
- User (create and call a parser that will fill these fields from dataset)
	o	Field that represents user in json format as string
	o	Define a user based on their id (how to determine equality)
	o	url field
	o	user id field
	o	votes fields:
			funny
			useful
			cool
	o	review count field
	o	User id field
	o	User name field
	o	Average stars field
- Restaurant (create and call a parser that will fill these fields from dataset)
	o	Field that represents restaurant in json format as string
	o	Define a restaurant based on its business id (how to determine equality)
	o	Boolean open field
	o	url field
	o	location field (maybe make a new location object with following fields)
			longitude field
			latitude field
			neighborhood field
			full address field
			city field
			schools field
			state field
	o	name field
	o	category field (list of categories it is in)
	o	stars field
	o	review count field
	o	photo url field
	o	price field
- Review (create and call a parser that will fill these fields from dataset)
	o	Field that represents review in json format as string
	o	Define review based on url (this is the only defining characteristic that will be different for every review, and this is how we compare equality)
	o	Business id
	o	url field
	o	votes fields:
			funny
			useful
			cool
	o	review text field
	o	user id field
	o	stars field
	o	date field	
Lists that will be fields of yelpDB datatype:
	Three lists for:
- Restaurants
	o	Organized alphabetically by default
	o	Have a method that returns them ordered from best to worst based on ratings
	o	Have a method that returns them ordered by number of reviews
- Users
	o	Organize them by user ID ascending order by default
	o	Have a method that returns them ordered by number of reviews
	o	Have a method that returns list of users in alphabetical order
	o	Have a method that returns list of users ordered by average stars
- Reviews
	o	Alphabetically by restaurant name by default
	o	Have a method that groups them based on reviewer, and organizes based on ascending user ids
	o	Have a method that returns them based on star rating
Methods of yelpDB:
Creators:
- Constructor that fills list fields, taking json databases as input and call parsers we have to make
Observers:
- Getter and setter methods for all fields if we want them protected
- Search method, for keywords/strings, to search all lists (maybe using stream patterns)
	o	Let it specify what list we are searching (and return type)
	o	Let it specify what fields in the objects we want to search 
	o	Let it search lists that it creates to refine search
	o	For certain searches, let it specify a range as a string:
			Eg. all restaurants >3 stars
			All restaurants <price 4
			Returns all restaurants in that radius starting with restaurant closest to gps location and ending with farthest
			Returns all restaurants on a street
			Returns all restaurants of a certain category
- Method that returns all reviews of a restaurant
- Method that returns all reviews from a user
- Method that returns all restaurants that a user has been to
- Method that returns all users that have been to a restaurant
- Consider having maps:
	o	Restaurants as keys, lists of reviews as values
	o	Users as keys, lists of reviews as values
	o	Restaurants as keys, list of users as values
	o	Users as keys, list of restaurants as values
	o	Either create these maps as fields in constructor, and make sure to update them every time something is added or removed
	o	Or create and return these maps when requested using methods, so we only have to update main lists
- Emptiness checker methods
- Appropriate contains() methods, to get info from maps
Mutators:
- Add User/Restaurant/Review
	o	Make sure to change relevant fields in all related objects
- Remove User/Restaurant/Review
	o	Make sure to change relevant fields in all related objects
- Relevant Setter methods for fields of objects:
	o	User:
			Update review count
			Update average stars
			Update votes
			Update user name
	o	Restaurant:
			Update open field
			Update location fields
			Update name field
			Update category field (list of categories it is in)
			Update stars field
			Update review count field
			Update photo url field
	o	Review:
			Update votes fields
			If editing allowed:
			•	Update text field
			•	Update stars field
			•	Update date field
Abstraction Functions/Rep Invariants:
- Represent databases as list of users
- Represent a user/review/restaurant as an object containing all the relevant fields
	o	Requires each line in databases contains all fields 


