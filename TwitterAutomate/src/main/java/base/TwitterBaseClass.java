package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import PublicUtilities.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TwitterBaseClass {
	public static WebDriver driver;
	public static Properties prop;
	
	public TwitterBaseClass()

	{
		try
		{
			prop=new Properties();
			FileInputStream inputConfigFile=new FileInputStream(Constants.filePath);
			prop.load(inputConfigFile);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}



	public static void initialization(String browser)
	{
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
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.get(prop.getProperty("twitterURL"));

	}
}
