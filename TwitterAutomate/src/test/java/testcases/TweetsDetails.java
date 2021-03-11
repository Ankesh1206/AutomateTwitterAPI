package testcases;
public class TweetsDetails {
	public String tweetId;
	public String tweetDate;
	public String tweetText;
	public String twitterScreenName;
	public String favouriteCount;
	public String retweetCount;
	public String twitterHandlerName;


	public String getTwitterScreenName() {
		return twitterScreenName;
	}
	public void setTwitterScreenName(String twitterScreenName) {
		this.twitterScreenName = twitterScreenName;
	}
	
	public String getTweetId() {
		return tweetId;
	}
	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}
	public String getTweetDate() {
		return tweetDate;
	}
	public void setTweetDate(String tweetDate) {
		this.tweetDate = tweetDate;
	}
	public String getTweetText() {
		return tweetText;
	}
	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}
	public String getTwitterHandlerName() {
		return twitterHandlerName;
	}
	public void setTwitterHandlerName(String twitterHandlerName) {
		this.twitterHandlerName = twitterHandlerName;
	}
	public String getFavouriteCount() {
		return favouriteCount;
	}
	public void setFavouriteCount(String favouriteCount) {
		this.favouriteCount = favouriteCount;
	}
	public String getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(String retweetCount) {
		this.retweetCount = retweetCount;
	}	
}
