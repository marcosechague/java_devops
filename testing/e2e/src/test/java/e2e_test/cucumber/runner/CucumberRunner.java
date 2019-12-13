package e2e_test.cucumber.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/features",
		glue = { "e2e_test.cucumber.step" },
		plugin = { "pretty", "json:target/cucumber-reports/cucumber.json" }
)
public class CucumberRunner {
}