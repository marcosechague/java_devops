package junit;

import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import driver.DriverAbstract;
import e2e_test.cucumber.runner.CucumberRunner;
import util.Constants;
import util.Utils;

@RunWith(Parameterized.class)
public class JunitRunnerTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, String> dictionary;

	public JunitRunnerTest(Map<String, String> dictionary) {
		this.dictionary = dictionary;
	}

	@Parameterized.Parameters
	public static LinkedList<Map<String, String>> data() throws Exception {
    	String platform = Constants.PLATFORM;
		LinkedList<Map<String, String>> browsers = Utils.getNavigators(platform);
		return browsers;
	}

	@Before
	public void setUP() {
		logger.info("dictionary: " + this.dictionary);
		DriverAbstract.setRemoteParameters(dictionary);
	}
	
	@Test
	public void runTest() {
		JUnitCore junit = new JUnitCore();
		
		System.out.println("Constants.ENVIROMENT.toUpperCase(): " + Constants.ENVIROMENT.toUpperCase());
		switch (Constants.ENVIROMENT.toUpperCase()) {
		case Constants.LOCAL_ENVIROMENT:
			junit.run(CucumberRunner.class);
			break;

		case Constants.REMOTE_ENVIROMENT:
			junit.run(ParallelComputer.methods(), CucumberRunner.class);
			break;
		default:
			logger.error("Enviroment not supported");
			break;
		}
	}	
}
