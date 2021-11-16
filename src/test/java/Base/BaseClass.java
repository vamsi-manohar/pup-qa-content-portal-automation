package Base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseClass {

	public WebDriver driver;

	PropertiesFileReader obj = new PropertiesFileReader();
	Properties data = obj.getProperty();

	public BaseClass(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//*[@type='text']")
	WebElement UserName;

	@FindBy(xpath = "//*[@type='password']")
	WebElement Password;

	@FindBy(xpath = "//*[text()=' Log in ']")
	WebElement Login;

	@FindBy(xpath = "//*[text()='Settings']")
	WebElement SettingHomePage;

	// Go the Content portal
	public void gotoURL() throws Exception {
		try {
			getAppUrl();
		} catch (Exception e) {
			throw new Exception("Webpage is not working");
		}
	}

	// UserName and Password
	public void userNameAndPassword() throws Exception {

		try {
			driver.navigate().refresh();
			clickObject(UserName);
			UserName.sendKeys(data.getProperty("UserName"));
			clickObject(Password);
			String decodedPassword = decodePassword();
			Password.sendKeys(decodedPassword);
			implicitWait(5);
		} catch (Exception e) {
			takeSnapShot(driver);
			throw new Exception("UserName and Password is not entered");
		}
	}

	// Click on the Element
	public void clickObject(WebElement element) throws Exception {
		if (element.isDisplayed()) {
			element.click();
		} else {
			throw new Exception("Element is not displayed");
		}
	}

	public void submit() throws Exception {

		try {
			clickObject(Login);
			Thread.sleep(5000);
		} catch (Exception e) {
			takeSnapShot(driver);
			throw new Exception("Sumbit button is not displayed");
		}
	}

	public void getPageTitleHomePage() throws Exception {
		try {
			SettingHomePage.isDisplayed();
		} catch (Exception e) {
			takeSnapShot(driver);
			throw new Exception("Home Page title is not displayed");
		}
	}

	public void inValiduserNameAndPassword() throws Exception {

		try {
			UserName.sendKeys(data.getProperty("InvalidUserName"));
			Password.sendKeys(data.getProperty("InvalidPassword"));
		} catch (Exception e) {
			takeSnapShot(driver);
			throw new Exception("UserName and Password is not working");
		}
	}

	// Decode Password
	public String decodePassword() {
		return new String(Base64.getDecoder().decode(data.getProperty("Password").getBytes()));
	}

	// Go to Site
	public void getAppUrl() {
		driver.get(data.getProperty("BaseURI"));
		implicitWait(5);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
	}

	// Implicit Wait
	public void implicitWait(int time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

	// Scroll Into View
	public void scrollIntoView(WebElement element) throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(500);
	}

	// Drop Down
	public void selectValueFromDropdown(WebElement element, String attribute) {
		Select select = new Select(element);
		select.selectByValue(attribute);
	}

	// Element to Disappear
	public boolean waitForElementToDisappear(long timeoutseconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutseconds);
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(
				By.cssSelector("pup-split-button[class='run-status-button split-button']")));
	}

	// Invisible Element
	public boolean waitForInvisibilityOfElement(long timeoutseconds, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutseconds);
		return wait.until(ExpectedConditions.invisibilityOf(element));
	}

	// Element to be clickable
	public void waitForElementToBeClickable(long timeoutseconds, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutseconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	// Wait for visible
	public void waitForElementsToBeVisible(long timeoutseconds, List<WebElement> element) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutseconds);
		wait.until(ExpectedConditions.visibilityOfAllElements(element));
	}

	// Wait for text
	public void waitForTextToBe(long timeoutseconds, WebElement element, String text) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutseconds);
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	// Mouse over
	public void mouseHoverOnWebElement(WebElement element) {
		try {
			Thread.sleep(2000);
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			((JavascriptExecutor) driver).executeScript(mouseOverScript, element);
			Thread.sleep(2000);
		} catch (StaleElementReferenceException e) {
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Click using JavaScript Executor
	public void clickUsingExecutor(WebElement element) throws Exception {
		try {
			if (element.isDisplayed()) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
			}
		} catch (StaleElementReferenceException e) {
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
		}
	}

	// Mouse Over
	public void performMouseHover(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}

	// Text clear
	public void clearTextField(WebElement element) {
		element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		element.sendKeys(Keys.DELETE);
	}

	// Time Stamp
	public void getCurrentTimestamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
	}

	// Take screenshots
	public static void takeSnapShot(WebDriver webdriver) throws Exception {

		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile = new File("/Users/kishorekumar/eclipse-workspace/ContentPortal/ContentPortalProject/screenshot/"
				+ timeStamp + ".png");
		FileUtils.copyFile(SrcFile, DestFile);
	}

}
