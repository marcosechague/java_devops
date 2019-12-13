package util;

public class Constants {
	
	private Constants() {}
	
	public static final String LOCAL_ENVIROMENT = "LOCAL";
	public static final String REMOTE_ENVIROMENT = "REMOTE";
	
	public static final String FIREFOX_BROWSER = "FIREFOX";
	public static final String CHROME_BROWSER = "CHROME";

	public static final String URL_WEBAPP = System.getProperty("backend") == null ? "http://localhost:4200" : System.getProperty("backend");
	
	public static final String PROXY_LOCAL = System.getProperty("NAME_PROXY_SAUCECONNECT") == null ? "localhost" : System.getProperty("NAME_PROXY_SAUCECONNECT");
	
	public static final String ENVIROMENT = System.getProperty("environment") == null ? LOCAL_ENVIROMENT : System.getProperty("environment");
	public static final String PLATFORM = System.getProperty("platform") == null ? "windows_10_chrome_65" : System.getProperty("platform");
	public static final String SAUCE_PROXY = System.getProperty("saucelabs.proxy") == null ? "" : System.getProperty("saucelabs.proxy");
	public static final String SAUCE_ENDPOINT = System.getProperty("saucelabs.endpoint") == null ? "" : System.getProperty("saucelabs.endpoint");
	
	public static final String BROWSER = System.getProperty("browser") == null ? FIREFOX_BROWSER : System.getProperty("browser");
	
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String CUCUMBER_REPORT_DIR = USER_DIR + "/target/cucumber-reports/";
	
	public static final String OS_NAME = System.getProperty("os.name");
}
