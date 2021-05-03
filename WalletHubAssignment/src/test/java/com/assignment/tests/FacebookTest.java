package com.assignment.tests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.assignment.base.TestBase;

public class FacebookTest extends TestBase{

	public FacebookTest()
	{
		super(); //Call the Test Base class constructor which in turn will perform loading the data from the properties file
	}

	public static void main(String[] args) throws Exception {
		String postMessage = "Stay Home, Stay Safe";
		
		FacebookTest fbtest = new FacebookTest();
		fbtest.setUp();
		System.out.println(driver.getTitle());
		
		
		String loginStatus;
		loginStatus = fbtest.fbLogin(prop.getProperty("fbusername"),prop.getProperty("fbpassword")); //login to facebook application
		if(loginStatus.equalsIgnoreCase("Login Successful"))
		{
			fbtest.fbPost(postMessage); //Post the status message
			fbtest.fbLogout(); //logout of the facebook application
		}
		else
		{
			throw new Exception(loginStatus);
		}
		fbtest.tearDown(); //Close the browser
	}
	
	//Method performs the prerequisites such as open specified browser and navigate to facebook login page
	public void setUp() throws Exception {
		initialization(); 
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.get(prop.getProperty("fburl"));
		if(driver.getTitle().equalsIgnoreCase("Facebook – log in or sign up"))
		{
			System.out.println("Navigated to Login page successfully");
		}
		else
		{
			throw new Exception("Login page is not loaded");
		}
	}

	//Method to perform the facebook login operation
	public String fbLogin(String username,String password)
	{
		WebElement unameField = driver.findElement(By.id("email"));
		unameField.sendKeys(username);

		WebElement pwdField = driver.findElement(By.name("pass"));
		pwdField.sendKeys(password);

		WebElement loginBtn = driver.findElement(By.name("login"));
		loginBtn.click();
		String status="";
		
		//Conditions to validate the login operation
		if(driver.findElements(By.xpath("//a[@aria-label=\"Facebook\"][@role=\"link\"]")).size()>0)
		{
			System.out.println("Homepage is displayed");
			status = "Login Successful";
		}
		else if(driver.findElements(By.xpath("//div[text() = \"The password that you've entered is incorrect. \"]")).size()>0)
		{
			status = "Password is incorrect";
		}
		else if(driver.findElements(By.xpath("//div[contains(text(), \"The email address you entered isn't connected to an account.\")]")).size()>0)
		{
			status = "username not found";
		}
		else if(driver.findElements(By.xpath("//div[contains(text(),\"The email address or mobile number you entered isn't connected to an account. \")]")).size()>0)
		{
			status = "username not found";
		}
		return status;
	}
	
	//Method to post the status message
	public void fbPost(String message) throws Exception
	{
		if(driver.findElements(By.xpath("//div[@aria-label=\"Close\"]")).size()>0)
		{
			driver.findElement(By.xpath("//div[@aria-label=\"Close\"]")).click();
		}
		driver.findElement(By.xpath("//span[contains(text(),\"What's on your mind, \")]")).click();
		WebDriverWait wait=new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath( "//span[text()=\"Create post\"]")));
		String privacyType = driver.findElement(By.xpath("//span[@class = \"l3itjdph cgat1ltu\"]")).getAttribute("innerHTML");
		System.out.println(privacyType);
		
		//Validate the privacy type and update if its not "Only Me"
		if(!privacyType.equalsIgnoreCase("Only me"))
		{
			driver.findElement(By.xpath("//span[@class = \"l3itjdph cgat1ltu\"]/following-sibling::i")).click();
			driver.findElement(By.xpath("//span[text()=\"Only me\"]")).click();
		}
		
		//Enter the message and click on post button
		driver.findElement(By.xpath("//div[contains(@aria-label,\"What's on your mind,\")]//span")).sendKeys(message);
		driver.findElement(By.xpath("//span[text()=\"Post\"]")).click();
		
		//Validate the message is posted or not
		String messagexpath = "//div[@aria-posinset=\"1\"]//div[text()=\""+message+"\"]";
		if(driver.findElements(By.xpath(messagexpath)).size()>0)
		{
			System.out.println("Message is posted successfully");
		}
		else
		{
			throw new Exception("Message is not posted successfully");
		}
	}
	
	//Method to perform the facebook logout operation
	public void fbLogout() throws Exception
	{
		driver.findElement(By.xpath("//div[@aria-label=\"Account\"]")).click();
		driver.findElement(By.xpath("//*[text()=\"Log Out\"]")).click();
		if(driver.findElements(By.id("email")).size()>0)
		{
			System.out.println("logout is successful");
		}
		else
		{
			throw new Exception("logout is un-successful");
		}
	}

	//Method to close the browser
	public void tearDown()
	{
		  driver.quit();
	}

}
