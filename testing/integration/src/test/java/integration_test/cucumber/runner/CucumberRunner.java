package integration_test.cucumber.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/features",
        plugin = { "pretty", "json:target/cucumber-reports/cucumber.json", "html:target/cucumber-reports/cucumber.html" },
		glue = { 
				"integration_test.cucumber.spring.testing" // por defecto el paquete
				})
public class CucumberRunner {
}