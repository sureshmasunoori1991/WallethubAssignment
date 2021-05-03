package com.assignment.tests;

import java.util.List;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assignment.base.TestBase;

public class WallethubReviewTest extends TestBase{
	
	public WallethubReviewTest()
	{
		super();
	}
	
	
	public static void main(String[] args) throws Exception {
		String reviewMessage = "This is an automation script updated message to validate the rating and review message of the insurance company"
							+ " This is an automation script updated message to validate the rating and review message of the insurance company";
		
		WallethubReviewTest whubtest = new WallethubReviewTest();
		whubtest.setUp();
		
		//Calling login function
		String loginStatus="";
		loginStatus = whubtest.WhubLogin(prop.getProperty("whubusername"),prop.getProperty("whubpassword"));
		
		//Validating the login status
		if(loginStatus.equals("Login Successful"))
		{
			driver.navigate().to(prop.getProperty("whubreviewurl"));
			if(driver.getTitle().equalsIgnoreCase("test insurance company metatitle test"))
			{
				System.out.println("Navigation to Test insurance company page is successful");
			}
			else
			{
				throw new Exception( "Navigation to Test insurance company page is un-successful");
			}
			
			driver.findElement(By.xpath("//div[@class=\"left-content\"]//span[text()=\"Reviews\"]")).click();
			
			
			//performing the hover on the stars under reviews section, validating the correct number of stars are highlighted and clicking the 4th star
			List<WebElement> stars = driver.findElements(By.xpath("//review-star[@class=\"rvs-svg\"]//*[@class=\"rvs-star-svg\"]"));
			int starsCount=0;
			Actions actions=new Actions(driver);
			if(stars.size()>0)
			{
				actions.moveToElement(stars.get(0)).perform();
				Thread.sleep(500);
				for(int i=0; i<stars.size()-1;i++)
				{
					actions.moveToElement(stars.get(i)).perform();
					Thread.sleep(500);
					starsCount = whubtest.countOfHighlightedStars();
					if(starsCount != i+1)
					{
						System.out.println("Stars are not highlighted correctly");
					}
					if(i==stars.size()-2)
					{
						stars.get(i).click();
					}
				}
				
				//Validating the page,selecting the Health Insurance from the drop down and posting the review with minimum 200 characters
				if(driver.findElements(By.xpath("//h4[@class=\"wrev-prd-name\"][text()=\"Test Insurance Company\"]")).size()>0)
				{
					System.out.println("Page navigated to Review page");
					driver.findElement(By.xpath("//div[@class=\"dropdown second\"]/span[@class=\"dropdown-placeholder\"]")).click();
					driver.findElement(By.xpath("//li[text()=\"Health Insurance\"]")).click();
					driver.findElement(By.xpath("//textarea[@placeholder=\"Write your review...\"]")).sendKeys(reviewMessage);
					driver.findElement(By.xpath("//div[text()=\"Submit\"]")).click();
					
					WebDriverWait wait = new WebDriverWait(driver,20);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"rvc-header\"]//h4[\"Your review has been posted.\"]")));
					
					if(driver.findElements(By.xpath("//div[@class=\\\"rvc-header\\\"]//h4[\\\"Your review has been posted.\\\"]")).size()>0)
					{
						System.out.println("Review is posted successfully");
					}
					else
					{
						throw new Exception("Review is not posted");
					}
					
					//Navigating to the profile and validating the Review feed is updated
					WebElement usernameElement = driver.findElement(By.xpath("//span[@class=\"brgm-list-title active\"]"));
					WebElement profile = driver.findElement(By.xpath("//a[text()=\"Profile\"]"));
					actions.moveToElement(usernameElement).moveToElement(profile).click().build().perform();
					
					if(driver.findElements(By.xpath("//div[@class=\"pr-rec-texts-container\"]")).size()>0)
					{
						System.out.println("Review feed is updated");
					}
					else
					{
						throw new Exception("Review feed is not posted");
					}
				}
			}
			else
			{
				throw new Exception("Rating is already performed or Star Ratings are not visible");
			}
			whubtest.WhubLogout();
		}
		else
		{
			throw new Exception(loginStatus);
		}
		
		whubtest.tearDown();
	}
	
	//Method to initialize the webdriver and open the browser and navigate to the Wallet hub url
	public void setUp() throws Exception
	{
		initialization();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.get(prop.getProperty("whuburl"));
		if(driver.getTitle().equals("Join WalletHub"))
		{
			System.out.println("URL is navigated successfully");
		}
		else
		{
			throw new Exception("Navigation to Wallet hub login page is un-successful");
		}
	}
	
	//Method to perform the actions to login to the wallet hub application
	public String WhubLogin(String username,String password)
	{
		String status="";
		
		driver.findElement(By.xpath("//a[text()=\"Login\"][@role=\"tab\"]")).click();
		driver.findElement(By.name("em")).sendKeys(username);
		driver.findElement(By.name("pw1")).sendKeys(password);
		
		driver.findElement(By.xpath("//span[text()=\"Login\"]")).click();
		
		if(driver.findElements(By.xpath("//button[text()=\"Edit Profile\"]")).size()>0)
		{
			System.out.println("Profile page is displayed");
			status = "Login Successful";
		}
		else if(driver.findElement(By.xpath("//input[@name=\"em\"]//following-sibling::div")).getText().equals("Invalid email or Password"))
		{
			status = "Login Un-Successful";
		}
		return status;
	}
	
	//Method to find the count of the highlighted stars
	public int countOfHighlightedStars()
	{
		return driver.findElements(By.xpath("//review-star[@class=\"rvs-svg\"]//following-sibling::*[@stroke=\"#4ae0e1\"]")).size();
	}
	
	//Method to perform the logout operation
	public void WhubLogout()
	{
		Actions actions1 = new Actions(driver);
		WebElement usernameElement = driver.findElement(By.xpath("//span[@class=\"brgm-list-title active\"]"));
		WebElement profile = driver.findElement(By.xpath("//a[text()=\"Logout\"]"));
		actions1.moveToElement(usernameElement).moveToElement(profile).click().build().perform();
	}
	
	//Method to close the browser
	public void tearDown()
	{
		driver.quit();
	}

}
