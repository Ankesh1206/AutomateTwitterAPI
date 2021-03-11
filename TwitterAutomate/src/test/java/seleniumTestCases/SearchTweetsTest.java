package seleniumTestCases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import PublicUtilities.Constants;
import PublicUtilities.PublicLibrary;
import base.TwitterBaseClass;
import pages.TwitterHomePage;
import testcases.DataGenerationTwitterAPI;
import testcases.FriendsDetails;

public class SearchTweetsTest extends TwitterBaseClass{

	TwitterHomePage homepage;
	DataGenerationTwitterAPI twitterData;
	PublicLibrary lib;
	public SearchTweetsTest()
	{
		super();
		twitterData = new DataGenerationTwitterAPI();
		lib = new PublicLibrary();
	}

	@BeforeTest
	public void setUp()
	{
		try {
			twitterData.setup();
			twitterData.start();
			initialization(Constants.chromeBrowser);
			homepage=new TwitterHomePage();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Search the tweets by taking data from the Top 10 tweets list from DataGeneration
	 */

	@Test
	public void searchTweetsTest()
	{
		try {
			String screenShotPath = "";
			String tweetScreenShotPath = "";
			List<String> tweetScreenShotList = new ArrayList<String>();
			for(int i = 0; i < DataGenerationTwitterAPI.topTweets.size();i++) {
				String screenName = DataGenerationTwitterAPI.topTweets.get(i).getTwitterScreenName();

				driver.get(prop.getProperty("twitterURL")+screenName);
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				String homePageTitle=homepage.getTitle();
				if(homePageTitle.contains(DataGenerationTwitterAPI.topTweets.get(i).getTwitterHandlerName())) {
					System.out.println("Landed on correct page : "+homePageTitle);
				}
				else {
					System.out.println("Wrong page " +homePageTitle);
				}
				WebElement username = driver.findElement(By.xpath("//div[@class='css-1dbjc4n r-18u37iz r-1wbh5a2']//span[text()='@"+screenName+"']"));
				PublicLibrary.explicitWait(driver,5, username);

				screenShotPath = Constants.screenShotsPath+"_"+screenName+".png";
				PublicLibrary.takeSnapShot(driver, screenShotPath);
				PublicLibrary.explicitWait(driver,5, username);

				JavascriptExecutor js = (JavascriptExecutor) driver;

				String tweetMsg = DataGenerationTwitterAPI.topTweets.get(i).getTweetText();
				int dataSize = driver.findElements(By.xpath("//span[contains(text(),'"+tweetMsg.substring(2,10)+"')]")).size();

				tweetScreenShotPath = Constants.tweetScreenShotsPath+"_"+screenName+"_"+i+".png";
				while (true){
					js.executeScript("javascript:window.scrollBy(0,1500)");
					PublicLibrary.explicitWait(driver,10, driver.findElement(By.xpath("//span[contains(text(),'"+tweetMsg.substring(2,10)+"')]")));
					js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//span[contains(text(),'"+tweetMsg.substring(2,10)+"')]")));
					if (driver.findElements(By.xpath("//span[contains(text(),'"+tweetMsg+"')]")).size()==dataSize) 
					{
						PublicLibrary.takeSnapShot(driver, tweetScreenShotPath);
						break;}
					dataSize = driver.findElements(By.xpath("//span[contains(text(),'"+tweetMsg+"')]")).size();
				}

				//Creating list to update the links one by one traversing through the list
				tweetScreenShotList.add(tweetScreenShotPath);
			}


			PublicLibrary.generateReport(DataGenerationTwitterAPI.topTweets,   prop.getProperty("twitterURL"), screenShotPath, tweetScreenShotList, DataGenerationTwitterAPI.topFriends);

			/*Call the Search Friend method*/
			//searchFriend();

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	/**
	 * Search a random friend from list and search
	 */
	public void searchFriend()
	{
		try {
			Random rand = new Random();
			List<FriendsDetails> friends = DataGenerationTwitterAPI.topFriends;
			/*Get Random location in the list and pick one*/
			int location = rand.nextInt(friends.size());
			String randomFriend = friends.get(location).getScreenName();
			By userSearchFieldLocator = By.xpath("//input[@data-testid='SearchBox_Search_Input']");
			lib.setText(userSearchFieldLocator, randomFriend);
			driver.findElement(userSearchFieldLocator).sendKeys(Keys.ENTER);

			/*Select the friend if displayed*/
			By friendsNameLocator = By.xpath("//div[@class='css-1dbjc4n r-1awozwy r-18u37iz r-1wtj0ep']//span[text()='"+friends.get(location).getFriendName()+"']");
			lib.clickElement(friendsNameLocator);
			String homePageTitle=homepage.getTitle();
			if(homePageTitle.contains(friends.get(location).getFriendName())) {
				System.out.println("Landed on correct page : "+homePageTitle);
			}
			else {
				System.out.println("Wrong page " +homePageTitle);
			}

			/*Click to get Following accounts*/
			By followingLinkLocator = By.xpath("//span[text()='Following']");
			lib.clickElement(followingLinkLocator);

			System.out.println("Oops!! It asks to login.. :( ");

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}


	@AfterTest
	public void tearDown()
	{
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		PublicLibrary.quitBrowser(driver);

	}
}
