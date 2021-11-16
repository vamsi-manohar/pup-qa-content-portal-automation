package stepDefinations;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import Base.BaseClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class loginPage {

	public WebDriver driver;
	public BaseClass login;
	public Scenario scenario;

	@Before
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-notifications");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--incognito");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		login = new BaseClass(driver);
	}

	@After
	public void teardown(Scenario scenario) {
		this.scenario = scenario;
		if (scenario.isFailed()) {
			TakesScreenshot scrshot = (TakesScreenshot) driver;
			byte[] data = scrshot.getScreenshotAs(OutputType.BYTES);
			scenario.attach(data, "image/png", "ContentPortal");
		}
		driver.close();
	}

	@Given("open the content portal url")
	public void open_the_content_portal_url() throws Exception {
		login.gotoURL();
	}

	@When("user provides valid username and valid password")
	public void user_provides_valid_username_and_valid_password() throws Exception {
		login.userNameAndPassword();
	}

	@Then("user click on login")
	public void user_click_on_login() throws Exception {
		login.submit();
	}

	@Then("user can see the content portal page with title")
	public void user_can_see_the_content_portal_page_with_title() throws Exception {
		login.getPageTitleHomePage();
	}

	@When("user provides Invalid username and Invalid password")
	public void user_provides_Invalid_username_and_Invalid_password() throws Exception {
		login.inValiduserNameAndPassword();
	}

	@Then("verify user cannot able to login")
	public void verify_user_cannot_able_to_login() throws Exception {
		login.getPageTitleHomePage();
	}

}
