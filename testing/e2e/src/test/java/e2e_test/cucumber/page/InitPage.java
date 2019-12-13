package e2e_test.cucumber.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import util.Constants;

public class InitPage {

	private WebDriver driver;

	private String URL = Constants.URL_WEBAPP + "/app/mapa";
	
	@FindBy(id = "cboTipoNegocio")
	private WebElement cboTipoNegocio;
	
	public InitPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void open() {
		this.driver.get(URL);
	}

	public String getUrlPage(){
		return this.URL;
	}

	public WebElement getCboOptions(){
		return this.cboTipoNegocio;
	}
}
