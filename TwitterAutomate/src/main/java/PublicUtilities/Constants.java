package PublicUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	//Application URL
    public static final String twitterUrl="https://www.twitter.com/";
    public static final String filePath = "./data/config_properties.properties";
    //static String datetime = new SimpleDateFormat("yyyyMMddHHmm'.png'").format(new Date());
    static String datetime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    public static  String screenShotsPath = "./Screenshots/testTweet"+datetime;
    public static  String tweetScreenShotsPath = "./TweetScreenshots/testTweet"+datetime;


    //Browser Options
    public static final String chromeBrowser="CHROME";
    public static final String firfoxBrowser="FIREFOX";


    //Wait Time Value
    public static final long shortWait =10;
    public static final long mediumWait =15;
    public static final long longWait =20;


    //Item Name
    public static final String searchItemName ="";
}
