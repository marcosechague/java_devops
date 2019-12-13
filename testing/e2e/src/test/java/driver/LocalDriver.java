package driver;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import util.Constants;

public class LocalDriver extends DriverAbstract {

	private static final Logger LOGGER = Logger.getLogger(LocalDriver.class);
	
	private static final String EXTENSION = Constants.OS_NAME.toLowerCase().contains("win") ? ".exe" : "";
	
	private static final String FIREFOXDRIVERPATH = "src/test/resources/webdrivers/geckodriver" + EXTENSION;
	private static final String CHROMEDRIVERPATH = "src/test/resources/webdrivers/chromedriver" + EXTENSION;
	private static final String GEOLOCATION_JSON_FF = "src/test/resources/webdrivers/geolocation-ff.json";
	
	private LocalDriver() { }
	
	public static LocalDriver getInstance() {
		return new LocalDriver();
	}
	
	@Override
	public WebDriver loadDriver() {
		
		switch (Constants.BROWSER.toUpperCase()) {
		case Constants.FIREFOX_BROWSER:
			
			LOGGER.info("Loading Firefox driver instance");

			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("geo.enabled", true);
			profile.setPreference("geo.provider.use_corelocation", true);
			profile.setPreference("geo.prompt.testing", true);
			profile.setPreference("geo.prompt.testing.allow", true);
			profile.setPreference("geo.wifi.uri",new File(GEOLOCATION_JSON_FF).toURI().toString());
			
			System.setProperty("webdriver.gecko.driver", FIREFOXDRIVERPATH);

			FirefoxOptions firefoxOptions = null;

			if(_userCapabilities != null) {
				firefoxOptions = new FirefoxOptions(_userCapabilities);
				firefoxOptions.setProfile(profile);
				this.driver = new FirefoxDriver(firefoxOptions);
			}
			else {
				firefoxOptions = new FirefoxOptions();
				firefoxOptions.setProfile(profile);
				this.driver = new FirefoxDriver(firefoxOptions);	
			}
			break;

		case Constants.CHROME_BROWSER:
			
			LOGGER.info("Loading Chrome driver instance");
			
			System.setProperty("webdriver.chrome.driver", CHROMEDRIVERPATH);
			if(_userCapabilities != null) {
				ChromeOptions opts = new ChromeOptions();
				opts.merge(_userCapabilities);
				this.driver = new ChromeDriver(opts);	
			}
			else {
				this.driver = new ChromeDriver();
			}
			break;
		default:
			LOGGER.error("Browser not supported");
			break;
		}
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.driver.manage().window().maximize();
		return driver;
	}

	@Override
	public WebDriver loadDriver(String jobName) {
		// TODO Auto-generated method stub
		return null;
	}

}
