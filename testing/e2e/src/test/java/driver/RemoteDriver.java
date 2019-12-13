package driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import util.Constants;

public class RemoteDriver extends DriverAbstract {

	private static final Logger LOGGER = Logger.getLogger(RemoteDriver.class);

	//private String jobName;

	private static final String USERNAME = getUserSauceLabs();
	private static final String ACCESSKEY = getKeySauceLabs();

	private final static String LOCAL_URL = "http://"+Constants.PROXY_LOCAL+":4445/wd/hub";
	private static final String URL = "https://ondemand.saucelabs.com:443/wd/hub";

	private String sessionId;
	
	/*
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	*/
	
	private RemoteDriver() {
	}

	public static RemoteDriver getInstance() {
		return new RemoteDriver();
	}

	@Override
	public WebDriver loadDriver(String jobName) {
		try {

			LOGGER.info("Loading Remote driver to connect to SauceLabs");

			DesiredCapabilities capabilities = loadDefaultCapabilities();
			capabilities.setCapability("name", jobName);
			
			LOGGER.info("Connection to SauceLabs using the tunnel identifier "
					+ (Constants.SAUCE_PROXY.isEmpty() ? "EMPTY" : Constants.SAUCE_PROXY));
			
			if (!Constants.SAUCE_PROXY.isEmpty())
				capabilities.setCapability("tunnelIdentifier", Constants.SAUCE_PROXY);
			
			this.driver = new RemoteWebDriver(getUrlSauceLabs(), capabilities);

			this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			this.sessionId = ((RemoteWebDriver) this.driver).getSessionId().toString();
			
			//this.jobName = jobName;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(Arrays.toString(e.getStackTrace()));
		}

		return this.driver;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getUsername() {
		return USERNAME;
	}

	public String getAccessKey() {
		return ACCESSKEY;
	}

	private DesiredCapabilities loadDefaultCapabilities() {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		for (String key : _dictionary.keySet()) {
			capabilities.setCapability(key, _dictionary.get(key));
		}

		return capabilities;
	}

	public static String getUserSauceLabs() {
		if (System.getenv("SAUCE_USER") == null) {
			if (System.getProperty("SAUCE_USER") == null) {
				return "EMPTY";
			}
			return System.getProperty("SAUCE_USER");
		}
		return System.getenv("SAUCE_USER");
	}

	public static String getKeySauceLabs() {
		if (System.getenv("SAUCE_KEY") == null) {
			if (System.getProperty("SAUCE_KEY") == null) {
				return "EMPTY";
			}
			return System.getProperty("SAUCE_KEY");
		}
		return System.getenv("SAUCE_KEY");
	}

	private URL getUrlSauceLabs() {
		String endpoint = getURLInit();
		try {
			URL url = new URL(endpoint);
			if (url.getUserInfo() == null) {
				StringBuilder newUrl = new StringBuilder(url.getProtocol()).append("://").append(USERNAME).append(":")
						.append(ACCESSKEY).append("@").append(url.getHost());
				if (url.getPort() > 0) {
					newUrl.append(":").append(url.getPort());
				}
				newUrl.append(url.getPath());
				return new URL(newUrl.toString());
			} else {
				return url;
			}
		} catch (MalformedURLException e) {
			LOGGER.trace(Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}

	private String getURLInit() {
		if (Constants.SAUCE_ENDPOINT.isEmpty()) {
			if (Constants.SAUCE_PROXY.isEmpty()) {
				return URL;
			} else {
				return LOCAL_URL;
			}
		} else {
			return Constants.SAUCE_ENDPOINT;
		}
	}

	@Override
	public WebDriver loadDriver() {
		// TODO Auto-generated method stub
		return null;
	}
}
