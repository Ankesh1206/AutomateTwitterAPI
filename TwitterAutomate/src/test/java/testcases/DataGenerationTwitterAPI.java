package testcases;

import static io.restassured.RestAssured.given;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.ExcelRead;

public class DataGenerationTwitterAPI {

	static Properties prop;
	static FileInputStream ip;
	static String userFilePath = "./data/config_properties.properties";
	static String userExcelFilePath = "./data/TwitterUsers.xlsx";

	private static String grantType = "client_credentials";
	private static List<TweetsDetails> listTweet = new ArrayList<TweetsDetails>();
	private static List<FriendsDetails> listFriends = new ArrayList<FriendsDetails>();
	public static List<TweetsDetails> topTweets = new ArrayList<TweetsDetails>();
	public static List<FriendsDetails> topFriends = new ArrayList<FriendsDetails>();
	static String sheetName = "Users";
	static ExcelRead users;
	static int userCount = 0;


	/**
	 * Setting up properties files and excel file for data read
	 * @throws IOException
	 */
	@BeforeTest
	public void setup() throws IOException {
		ip = new FileInputStream(userFilePath);
		prop = new Properties();
		prop.load(ip);
		users = new ExcelRead(userExcelFilePath,sheetName);
	}

	/**
	 * Main runner method to call all the methods
	 * @throws IOException
	 */
	@Test
	public void start() throws IOException {

		String token = getAccessToken();
		System.out.println(token);
		userCount = users.getRowCount();
		getUserTweets(token);
		getUserFriends(token);
		//PublicLibrary.generateReport(topTweets, prop.getProperty("twitterURL"), topFriends);
	}

	/**
	 * Generate the access Token for authorization
	 * @return
	 * @throws IOException
	 */
	public static String getAccessToken() throws IOException {

		RestAssured.baseURI = prop.getProperty("twitterAPI_URI");
		Response response = 
				given()
				.when()
				.formParam("client_id", prop.getProperty("consumerKey"))
				.formParam("client_secret", prop.getProperty("consumerSecret"))
				.formParam("grant_type", grantType)
				.header("Authorisation","Bearer "+encode(prop.getProperty("consumerKey"),prop.getProperty("consumerSecret")))
				.header("content-type","application/x-www-form-urlencoded;charset=UTF-8")
				//.contentType(ContentType.URLENC)
				.post("/oauth2/token").then().statusCode(200).extract().response();

		String twitterToken = response.jsonPath().get("access_token");
		return twitterToken;
	}

	/**
	 * Encoding the ConsumerKey and ConsumerSecret as per API doc
	 * @param consumerKey
	 * @param consumerSecret
	 * @return
	 */
	public static String encode(String consumerKey, String consumerSecret) {
		return new String(Base64.getEncoder().encode((consumerKey+":"+consumerKey).getBytes()));
	}

	/**
	 * Calling Tweets API & Getting tweet details for 100 users
	 * @param twitterToken
	 */
	public static void getUserTweets(String twitterToken) {

		for(int i = 1;i< userCount;i++) {
			Object user = users.getSheetData(i,1);
			System.out.println(user);
			Response respGetTweets = 
					given()
					.when()
					.auth()
					.oauth2(twitterToken)
					.queryParam("screen_name", user)
					.queryParam("include_rts", false)
					.queryParam("exclude_replies", true)
					.queryParam("count", 100)				
					.get("/1.1/statuses/user_timeline.json").then().extract().response();
			getTopTenTweets(respGetTweets);
		}
	}

	/**
	 * Getting the top 10 tweets based on maximum retweet count
	 * @param respGetTweets
	 */
	public static void getTopTenTweets(Response respGetTweets) {

		/*Adding all the tweet details to TweetDetails Object*/
		for(int i = 0; i<100;i++) {

			TweetsDetails tweets = new TweetsDetails();	
			tweets.setTweetId(respGetTweets.jsonPath().get("id["+i+"]") == null ? null : respGetTweets.jsonPath().get("id["+i+"]").toString());
			tweets.setTweetDate(respGetTweets.jsonPath().get("created_at["+i+"]") == null ? null : respGetTweets.jsonPath().get("created_at["+i+"]").toString());
			tweets.setTweetText(respGetTweets.jsonPath().get("text["+i+"]") == null ? null : respGetTweets.jsonPath().get("text["+i+"]").toString());
			tweets.setTwitterScreenName(respGetTweets.jsonPath().get("user["+i+"].screen_name") == null ? null : respGetTweets.jsonPath().get("user["+i+"].screen_name").toString());
			tweets.setFavouriteCount(respGetTweets.jsonPath().get("favorite_count["+i+"]") == null ? null : respGetTweets.jsonPath().get("favorite_count["+i+"]").toString());
			tweets.setRetweetCount(respGetTweets.jsonPath().get("retweet_count["+i+"]") == null ? "0" : respGetTweets.jsonPath().get("retweet_count["+i+"]").toString());
			tweets.setTwitterHandlerName(respGetTweets.jsonPath().get("user["+i+"].name") == null ? "0" : respGetTweets.jsonPath().get("user["+i+"].name").toString());

			listTweet.add(tweets);
		}

		/*Performing sort operation without inbuilt function and Adding the top 10 tweets to topTweets list*/
		for (int i = 0; i < 10; i++) {
			for (int j = listTweet.size() - 1; j > i; j--) {
				if (Integer.parseInt(listTweet.get(i).getRetweetCount()) < Integer.parseInt(listTweet.get(j).getRetweetCount())) {
					TweetsDetails tmp = listTweet.get(i);
					listTweet.set(i, listTweet.get(j));
					listTweet.set(j,tmp);	
				}						
			}
			topTweets.add(listTweet.get(i));

			/*Printing the tweet details*/
			System.out.println("Tweet Date : " + topTweets.get(i).getTweetDate() + ", " 
					+ "Tweet Id : " + topTweets.get(i).getTweetId() + ", "
					+ "Tweet Text : " + topTweets.get(i).getTweetText() + ", "
					+ "TwitterHandler : " + topTweets.get(i).getTwitterHandlerName() + ", "
					+ "Favourite Tweet Count : " + topTweets.get(i).getFavouriteCount() + ", "
					+ "Retweet Count : " + topTweets.get(i).getRetweetCount());
		}

	}

	/**
	 * Call the Friends API to get the 100 friends of the user
	 * @param twitterToken
	 */
	public static void getUserFriends(String twitterToken) {

		
		for(int i = 1;i<userCount;i++) {
			Object user = users.getSheetData(i,1);
			System.out.println(user);
			Response respFriends = 
					given()
					.when()
					.auth()
					.oauth2(twitterToken)
					.queryParam("screen_name", user)
					.queryParam("count", 100)				
					.get("/1.1/friends/list.json").then().extract().response();
			getTopTenFriends(respFriends);
		}
	}

	/**
	 * Getting top 10 friends having the most number of followers
	 * @param respFriends
	 */
	public static void getTopTenFriends(Response respFriends) {
		for(int i = 0; i<100;i++) {

			FriendsDetails friends = new FriendsDetails();	
			friends.setFriendsCount(respFriends.jsonPath().get("users["+i+"].friends_count") == null ? null : respFriends.jsonPath().get("users["+i+"].friends_count").toString());
			friends.setFollowersCount(respFriends.jsonPath().get("users["+i+"].followers_count") == null ? null : respFriends.jsonPath().get("users["+i+"].followers_count").toString());
			friends.setScreenName(respFriends.jsonPath().get("users["+i+"].screen_name") == null ? null : respFriends.jsonPath().get("users["+i+"].screen_name").toString());
			friends.setFriendName(respFriends.jsonPath().get("users["+i+"].name") == null ? null : respFriends.jsonPath().get("users["+i+"].name").toString());

			
			/*Logic to Add friends only if they are not present in the Users list provided*/
			for(int k = 1; k < users.getRowCount(); k++) {
				if(users.getSheetData(k, 1).equals(friends.getScreenName())) {
					break;
				}		
			}
			listFriends.add(friends);
		}

		/*Performing sort operation without inbuilt function and Adding the top 10 friends to topFriends list*/
		for (int i = 0; i < 10; i++) {

			for (int j = listFriends.size() - 1; j > i; j--) {
				if (Integer.parseInt(listFriends.get(i).getFollowersCount()) < Integer.parseInt(listFriends.get(j).getFollowersCount())) {
					FriendsDetails tmp = listFriends.get(i);
					listFriends.set(i, listFriends.get(j));
					listFriends.set(j,tmp);	
				}						
			}
			topFriends.add(listFriends.get(i));

			System.out.println("Friends Name : " + topFriends.get(i).getScreenName() + ", "
					+ "Friends Followers count : " + topFriends.get(i).getFollowersCount() + ", "
					+ "Friends Friend Count : " + topFriends.get(i).getFriendsCount());

		}
	}
}
