package junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import integration_test.cucumber.runner.CucumberRunner;

@RunWith(Parameterized.class)
public class JunitRunnerTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String testName;

	public JunitRunnerTest(String name) {
		this.testName = name;
	}

	@Parameterized.Parameters
	public static Object[] data() {
    	return new Object[] { "first test" };
	}

	@Before
	public void setUP() {
		logger.info("example: " + this.testName);
	}
	
	@Test
	public void runTest() {
		JUnitCore junit = new JUnitCore();
		junit.run(CucumberRunner.class);
	}	
}
