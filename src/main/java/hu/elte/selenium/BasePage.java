package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public void setUp() throws MalformedURLException{
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL(ConfigurationReader.readValue("selenium.host", String.class)), options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10);
        this.driver.get("https://www.konzolvilag.hu/");
    }
}
