import hu.elte.config.ConfigurationReader;
import hu.elte.selenium.LoginPage;
import hu.elte.selenium.MainPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

public class MainTests {
    private WebDriver driver;
    private WebDriverWait wait;

    private LoginPage loginPage;
    private MainPage mainPage;
    @Before
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=hu");
        options.addArguments("--charset=UTF-8");
        options.addArguments("--no-sandbox");
        driver = new RemoteWebDriver(new URL(ConfigurationReader.readValue("selenium.host", String.class)), options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10);

        mainPage = new MainPage(driver, wait);
        loginPage = new LoginPage(driver, wait);
    }
    @Test
    public void testSuccessfulLoginAndLogout() {
        mainPage = loginPage.login();

        By greetingLocator = By.xpath("//*[@id=\"header\"]/div[2]/ul/li[5]");
        WebElement greeting = loginPage.waitAndReturnWebElement(greetingLocator);

        Assert.assertTrue(greeting.getText().endsWith("kedves Molnar Daniel!"));

        mainPage.logout();

        greeting = loginPage.waitAndReturnWebElement(greetingLocator);
        Assert.assertFalse(greeting.getText().endsWith("kedves Molnar Daniel!"));
    }

    @Test
    public void testHoverAndNavigate() {
        mainPage.navigateToPlaystation5Page();

        By h1Locator = By.tagName("h1");
        WebElement h1 = mainPage.waitAndReturnWebElement(h1Locator);

        System.out.println(h1.getText());
    }

    @After
    public void tearDown() {
        if(this.driver != null) {
            this.driver.quit();
        }
    }
}
