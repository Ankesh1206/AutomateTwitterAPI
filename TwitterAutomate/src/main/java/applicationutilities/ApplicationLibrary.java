package applicationutilities;

import PublicUtilities.Constants;
import PublicUtilities.PublicLibrary;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.NoSuchElementException;

public class ApplicationLibrary {
    public PublicLibrary publicLibrary;
    WebElement element;
    public ApplicationLibrary()
    {
        publicLibrary=new PublicLibrary();
    }

    /**
     * @return Item Price from  Application
     */
    public void navigateToTweet()
    {
        try {
            //element = PublicLibrary.driver.findElement(ApplicationObjects.tweetText);
            publicLibrary.ByVisibleElement(ApplicationObjects.tweetText);
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Item not present in the application");
        }     
    }

    /**
     * Method to compare the items price
     * @param user 
     * @param msg 
     * @throws Exception 
     */
    public void tweetDetailsComparator(Object user, String msg) throws Exception
    {
    	publicLibrary.navigateToUrl(Constants.twitterUrl+user,msg);
    	publicLibrary.implicitWait(15);	
    	
    }

    /**
     * @return Get the Item price from Amazon Application
     */
    public String getTweetText()
    {
        //publicLibrary.clickElement(ApplicationObjects.tweetText);
        try {
            List<WebElement> element = PublicLibrary.driver.findElements(ApplicationObjects.tweetText);
            System.out.println(element.get(0).getText());
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Item not present in the application");
        }
        return element.getText();
    }
    
    
}
