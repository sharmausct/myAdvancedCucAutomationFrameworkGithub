package driver;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DriverFactory {
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (webDriver.get() == null){
            webDriver.set(createDriver());
        }
        return webDriver.get();
    }

    private static WebDriver createDriver(){

        WebDriver driver = null;

        switch (getBrowserType()) {
            case ("chrome"): {
            	//old approach, now driver setup is not required as we have Selenium Manager in Selenium-4
                //System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/main/java/driver/drivers/chromedriver.exe");
                ChromeOptions chromeOptions=new ChromeOptions();
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver=new ChromeDriver(chromeOptions);
                System.out.println("used chrome browser");
                break;
            }
            case ("firefox"): {
            	//old approach, now driver setup is not required as we have Selenium Manager in Selenium-4
                //System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/src/main/java/driver/drivers/geckodriver.exe");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                //firefoxOptions.addArguments("--remote-allow-origins=*");
                driver = new FirefoxDriver(firefoxOptions);
                System.out.println("used firefox browser");
                break;
            }
            case ("edge"): {
            	EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setAcceptInsecureCerts(true);
                edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new EdgeDriver(edgeOptions);
                System.out.println("used edge browser");
                break;
            }
        }
        driver.manage().window().maximize();
        return driver;
    }


    private static String getBrowserType()  {

        String browserType = null;
        
        String browserTypeRemoteValue = System.getProperty("browserType");
        
        try {
        	if (browserTypeRemoteValue == null ||browserTypeRemoteValue.isEmpty()) {
        		Properties properties = new Properties();
        		FileInputStream file = new FileInputStream (System.getProperty("user.dir")+ "/src/main/java/properties/config.properties");
        		properties.load(file);
        		browserType = properties.getProperty("browser").toLowerCase().trim();
        	} else {
        		browserType = browserTypeRemoteValue;
        		
        	}
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return browserType;
    }


    public static void cleanupDriver () {

        webDriver.get().quit();
        webDriver.remove();

    }




}

