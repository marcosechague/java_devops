package driver;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public abstract class DriverAbstract {

	protected static Map<String, String> _dictionary;
	
	protected WebDriver driver;
	
	protected static DesiredCapabilities _userCapabilities;
	
	public static void setRemoteParameters (Map<String, String> dictionary) {
		_dictionary = dictionary;
	}
	
	public void finishDriver() {
		this.driver.quit();
	}
	
	public static void setUserCapabilities(DesiredCapabilities userCapabilities) {
		_userCapabilities = userCapabilities;
	}
	
	public abstract WebDriver loadDriver();
	public abstract WebDriver loadDriver(String jobName);
	
}
