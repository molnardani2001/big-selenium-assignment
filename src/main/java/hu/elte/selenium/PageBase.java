package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public abstract class PageBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected By bodyLocator = By.tagName("body");

    public PageBase(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebElement waitAndReturnWebElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    }

    public String getDomain() {
        return ConfigurationReader.readValue("protocol", String.class)
                + "://"
                + ConfigurationReader.readValue("domain", String.class);
    }

    public abstract String getSpecificDomain();

    protected void clickRandom() {
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();

        Random random = new Random();
        Actions actions = new Actions(driver);
        actions.moveByOffset(random.nextInt(width), random.nextInt(height)).click().build().perform();
    }
}
