package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import base.TwitterBaseClass;

public class TwitterHomePage extends TwitterBaseClass{


	WebElement searchTweet;


	//initialize the page factory elements inside a method

	public TwitterHomePage()
	{
		PageFactory.initElements(driver, this);
	}


	//Actions
	public String getTitle() throws InterruptedException
	{
		Thread.sleep(10000);		
		System.out.println(driver.getTitle());
		return driver.getTitle();
	}

	public void searchTweets() {
		searchTweet = driver.findElement(By.xpath(""));
	}


}
