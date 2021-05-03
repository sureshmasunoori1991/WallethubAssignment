package com.assignment.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBase {
	public static WebDriver driver;
	public static Properties prop;
	
	
	public TestBase()
	{
		//Below code is to load the data from the properties file
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+ "/src/main/java/com"
					+ "/assignment/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Method is to open the browser based on the value in the properties file, maximize the window and delete the cookies
	public static void initialization() throws IOException{
		String browserName = prop.getProperty("browser");
		if(browserName.equals("chrome")){
			
			  System.setProperty("webdriver.chrome.driver",prop.getProperty("driversPath")+"\\chromedriver.exe"); 
			  ChromeOptions options= new ChromeOptions(); 
			  options.addArguments("--disable-notifications"); //to disable the location notification 
			  driver = new ChromeDriver(options);
			
			/* Below code is to kill the existing chrome processes and open the local chrome browser with existing extensions
			 * Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T"); 
			 * String username = System.getProperty("user.name");
			 *  
			 * System.setProperty("webdriver.chrome.driver",prop.getProperty("driversPath")+"\\chromedriver.exe"); 
			 * ChromeOptions options = new ChromeOptions();
			 * options.addArguments("--noerrdialogs");
			 * options.addArguments("user-data-dir=C:\\Users\\"+username+"\\AppData\\Local\\Google\\Chrome\\User Data"); 
			 * options.addArguments("--start-maximized");
			 * driver = new ChromeDriver(options);
			 */	
		}
		else if(browserName.equals("FF")){
			System.setProperty("webdriver.gecko.driver", prop.getProperty("driversPath")+"\\geckodriver.exe");	
			driver = new FirefoxDriver(); 
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
	}
	
}
