import hu.elte.config.ConfigurationReader;
import hu.elte.selenium.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;
    private MyAccountPage myAccountPage;
    private SearchResultPage searchResultPage;
    private StoresPage storesPage;
    private ProductPage productPage;
    private ServicePage servicePage;
    @Before
    public void setUp() throws MalformedURLException {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=hu");
        options.addArguments("--charset=UTF-8");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("excludeSwitches", new String[] { "disable-popup-blocking" });
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10);

        mainPage = new MainPage(driver, wait);
    }

    @Test
    public void testSuccessfulLoginAndLogout() {
        login();
        Assert.assertTrue(ConfigurationReader.readValue("tests.greeting",String.class)
                .equalsIgnoreCase(myAccountPage.getGreeting()));

        List<String> assertMenuItems = ConfigurationReader.readValue("tests.myAccountMenuItems", List.class);

        Assert.assertEquals(assertMenuItems, myAccountPage.getMenuPoints());

        mainPage = myAccountPage.logout();
        Assert.assertEquals(ConfigurationReader.readValue("tests.mainPageTitle", String.class), mainPage.getTitle());
    }

    @Test
    public void hoverTest() {
        login();
        mainPage = loginPage.returnToMainPage();

        List<String> assertMenuItems = ConfigurationReader.readValue("tests.mainPageMenuItems", List.class);
        Assert.assertEquals(assertMenuItems, mainPage.hoverMyAccountAndGetMenuItems());
    }

    @Test
    public void searchAndCheckboxTest() {
        this.driver.get(getFullURL());
        mainPage.exitPopupIfPresent();
        searchResultPage = mainPage.search(ConfigurationReader.readValue("search.product",String.class));
        searchResultPage = searchResultPage.checkAllExtras();

        List<Boolean> checkBoxValues = searchResultPage.readExtrasCheckboxValues();
        Assert.assertEquals(8, checkBoxValues.size());
        checkBoxValues.forEach(Assert::assertTrue);
    }

    @Test
    public void staticPageLoadedTest() {
        this.driver.get(getFullURL());
        mainPage.exitPopupIfPresent();

        storesPage = mainPage.openStoresPage();

        Assert.assertEquals(ConfigurationReader.readValue("tests.storesPageTitle", String.class) ,storesPage.getTitle());
    }

    @Test
    public void staticPagesFromArrayTest() {
        login();

        List<String> pages = ConfigurationReader.readValue("staticPagesToOpen",List.class);
        List<String> pageUrls = myAccountPage.openAndGetStaticPagesURL(pages);

        String prefix = ConfigurationReader.readValue("myAccountPrefix",String.class);
        pages = pages.stream().map(p -> prefix + p).toList();

        Assert.assertEquals(pages, pageUrls);
    }

    @Test
    public void changePasswordFormSendingwithUser() {
        login();
        String oldPassword = ConfigurationReader.readValue("user.password", String.class);
        String newPassword = ConfigurationReader.readValue("user.newPassword", String.class);
        myAccountPage.changePassword(newPassword);

        String sysMsg = ConfigurationReader.readValue("tests.successfullPwChangeMsg", String.class);
        Assert.assertTrue(sysMsg.equalsIgnoreCase(myAccountPage.getSystemMessage()));

        myAccountPage.changePassword(oldPassword);
        Assert.assertTrue(sysMsg.equalsIgnoreCase(myAccountPage.getSystemMessage()));
    }

    @Test
    public void readDropDownInputValue(){
        this.driver.get(getFullURL());
        mainPage.exitPopupIfPresent();
        searchResultPage = mainPage.search(ConfigurationReader.readValue("search.notebook",String.class));
        productPage = searchResultPage.openAsusZenbookProductPage();
        Pair<String, String> nameAndPrice = productPage.readDropDownValues();

        String name = ConfigurationReader.readValue("tests.dropDownValueName", String.class);
        String price = ConfigurationReader.readValue("tests.dropDownValuePrice", String.class);
        Assert.assertEquals(name, nameAndPrice.getKey());
        Assert.assertTrue(nameAndPrice.getValue().startsWith(price));
    }

    @Test
    public void javascriptExecutorTest() throws InterruptedException {
        this.driver.get(getFullURL());
        mainPage.exitPopupIfPresent();
        servicePage = mainPage.openServicePage();

        String message = ConfigurationReader.readValue("tests.alertMessage", String.class);
        String alertedMessage = servicePage.alertUser(message);

        Assert.assertEquals(message, alertedMessage);
    }

    private void login() {
        this.driver.get(getFullURL());
        loginPage = mainPage.clickLogin();

        myAccountPage = loginPage.login();
    }

    private String getFullURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigurationReader.readValue("protocol", String.class));
        sb.append("://");
        sb.append(ConfigurationReader.readValue("domain", String.class));

        return sb.toString();
    }
    @After
    public void tearDown() {
        if(this.driver != null) {
            this.driver.quit();
        }
    }
}
