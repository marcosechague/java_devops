package e2e_test.cucumber.step;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import driver.LocalDriver;
import driver.RemoteDriver;
import e2e_test.cucumber.page.InitPage;
import e2e_test.cucumber.page.LoginPage;
import util.Constants;
import util.SauceUtils;

public class DummySteps {

	private static final Logger logger = Logger.getLogger(DummySteps.class);

	private LoginPage loginPage;
	private InitPage initPage;

	private WebDriver driver;
	
	@Given("un cliente que ingresa a la pagina login")
	public void el_usuario_con_tag() {
		this.loginPage = new LoginPage(this.driver);
		this.loginPage.open();
		
		assertEquals("The site web is loaded", this.loginPage.getBannerTitle(), "Login App Curso");
	}

	@When("el cliente ingresa sus credenciales {string} y clave {string}")
	public void el_ingresa_a_la_banca_por_internet_con_la_url(String user, String pass) throws Throwable {
		this.loginPage.writeUser(user);
		this.loginPage.writePassword(pass);

		WebDriverWait wait = new WebDriverWait(this.driver, 10); //seconds
		wait.until(ExpectedConditions.textToBePresentInElementValue(this.loginPage.getTxtUser(), user));
	}
	
	@Then("se ingresa al sistema con pagina inicial del mapa")
	public void se_muestran_sus_cuentas_disponibles() throws InterruptedException {
		this.loginPage.clickEntrar();

		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		this.initPage = new InitPage(this.driver);
		assertNotNull(this.initPage.getCboOptions());

		Thread.sleep(5000);
	}	
	
	@Before
	public void setUp(final Scenario scenario) throws Exception {

		switch (Constants.ENVIROMENT) {
		case Constants.LOCAL_ENVIROMENT:
			this.driver = LocalDriver.getInstance().loadDriver();
			break;
		
		case Constants.REMOTE_ENVIROMENT:
			this.driver = RemoteDriver.getInstance().loadDriver(scenario.getName());
			break;
		default:
			logger.error("Enviroment not supported");
			break;
		}
	}
	
	@After
	public void after(final Scenario scenario) throws Exception {
		if(this.driver == null) return;
		
		switch (Constants.ENVIROMENT) {
		case Constants.LOCAL_ENVIROMENT: break;

		case Constants.REMOTE_ENVIROMENT:
			final String sessionId = ((RemoteWebDriver) this.driver).getSessionId().toString();
			logger.info("sessionId: " + sessionId);
			logger.info("scenario-Id: " + scenario.getId());
			logger.info("scenario-Name: " + scenario.getName());
			logger.info("scenario-Status: " + scenario.getStatus());
			scenario.embed(
					MessageFormat.format("'{'\"sessionId\":\"{0}\", \"browserName\": \"{1}\", \"version\": \"{2}\"'}'",
							sessionId, ((RemoteWebDriver) driver).getCapabilities().getBrowserName(),
							((RemoteWebDriver) driver).getCapabilities().getVersion()).getBytes(),
					"application/json");
	        
			SauceUtils.UpdateResults(RemoteDriver.getUserSauceLabs(), RemoteDriver.getKeySauceLabs(), !scenario.isFailed(), sessionId);
			logger.info("SauceOnDemandSessionID="+ sessionId + "job-name="+ scenario.getName());
			break;
		default:
		logger.error("Enviroment not supported");
			break;
		}
		
		this.driver.quit();
	}
}
