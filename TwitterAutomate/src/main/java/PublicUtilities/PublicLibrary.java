package PublicUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import testcases.FriendsDetails;
import testcases.TweetsDetails;

public class PublicLibrary {

	public static WebDriver driver;
	private WebElement element;

	/**
	 * @param browser Type of Browser
	 * @throws InterruptedException 
	 */
	public void setUp(String browser)  {
		if(browser.trim().equalsIgnoreCase(Constants.chromeBrowser))
		{
			WebDriverManager.chromedriver();

			driver=new ChromeDriver();
		}
		else if(browser.trim().equalsIgnoreCase(Constants.firfoxBrowser))
		{
			WebDriverManager.firefoxdriver();
			driver=new FirefoxDriver();
		}
		else
		{
			System.out.println("Wrong Browser Type");
		}
	}

	/**
	 * Method to Maximize the Browser
	 */
	public void maximizeBrowser()
	{
		driver.manage().window().maximize();
	}

	/**
	 * Method to close the Browser
	 */
	public static void quitBrowser(WebDriver driver)
	{
		driver.close();
		driver.quit();
	}

	/**
	 * @param url Application URL
	 * @throws Exception 
	 */
	public void navigateToUrl(String url,String msg) throws Exception
	{
		implicitWait(Constants.shortWait);
		driver.get(url);
		implicitWait(Constants.longWait);
		System.out.println(driver.getTitle());
		takeSnapShot(driver, "./Screenshots/test.png");
		implicitWait(Constants.longWait);
	}



	/**
	 * @param locatorValue element Locator value
	 * Method to click the element
	 */
	public void clickElement(By locatorValue)
	{

		element=driver.findElement(locatorValue);
		if(element.isEnabled() && element.isDisplayed())
		{
			explicitWait(driver,Constants.longWait,element);
			element.click();
		}
	}


	public void ByVisibleElement(By locator) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement Element = driver.findElement(By.xpath("//span[contains(text(),'"+locator+"')]"));
		js.executeScript("arguments[0].scrollIntoView();", Element);
		System.out.println(Element.getText());
	}

	/**
	 * Method to Enter the Text
	 * @param locatorValue Element Locator Value
	 * @param textValue String to be entered
	 */
	public void setText(By locatorValue,String textValue)
	{
		element=driver.findElement(locatorValue);
		if(element.isEnabled() && element.isDisplayed())
		{
			explicitWait(driver,Constants.shortWait,element);
			element.sendKeys(textValue);
		}
	}

	/**
	 * @param waitTime Wait Time in seconds
	 */
	public void implicitWait(long waitTime)
	{
		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * @param driver 
	 * @param waitTime Wait time in seconds
	 * @param element element value
	 */
	public static void explicitWait(WebDriver driver, long waitTime,WebElement element)
	{
		WebDriverWait wait=new WebDriverWait(driver,waitTime);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{

		TakesScreenshot scrShot =((TakesScreenshot)webdriver);
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile=new File(fileWithPath);
		FileUtils.copyFile(SrcFile, DestFile,true);

	}

	/**
	 * Generate a HTML report
	 * @param tweetScreenShotPath 
	 * @param screenshotPath 
	 * @throws IOException 
	 */
	public static void generateReport(List<TweetsDetails> topTweets,String twitterURL, String screenshotPath, List<String> tweetScreenShotPath, List<FriendsDetails> topFriends) throws IOException {
		try {
			StringBuilder buf = new StringBuilder();
			buf.append("<html>" +
					"<body>" +
					"<table border=\"2\" align=\"right\">" +
					"<tr>" +
					"<th>TwitterHandle</th>" +
					"<th>TwitterHandleURL</th>" +
					"<th>TweetScreenShot/Path</th>" +
					"<th>TopTweets</th>" +
					"<th>TopFriends</th>" +
					"</tr>");
			for (int i = 0; i < 10; i++) {
				buf.append("<tr><td>")
				.append(topTweets.get(i).getTwitterHandlerName())
				.append("</td><td>")
				.append(twitterURL+topTweets.get(i).getTwitterScreenName())
				.append("</td><td>")
				.append(screenshotPath + " <img src='"+screenshotPath+"' alt='"+topTweets.get(i).getTwitterHandlerName()+" Screenshot'/>")
				.append("</td><td>")
				.append(topTweets.get(i).getTweetId() + " : " + topTweets.get(i).getTweetText())
				.append("</td><td>")
				.append(topFriends.get(i).getFriendName())
				.append("</td></tr>");
			}
			buf.append("</table>" +
					"</body>" +
					"</html>");
			String twitterHtmlReport = buf.toString();
			File newHtmlFile = new File("./TwitterAPIReport/TwitterAPIReport.html");
			FileUtils.writeStringToFile(newHtmlFile, twitterHtmlReport, Charset.defaultCharset());
		}
		catch (Exception e) {
			System.out.println("Exception while writing to file : " + e.getMessage());		
		}
	}
}