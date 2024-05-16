import hu.elte.config.ConfigurationReader;
import hu.elte.selenium.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    /**
     * Setup before every run
     * - webdriver configuration
     * - chrome options configuration
     */
    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=hu");
        options.addArguments("--charset=UTF-8");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);

        mainPage = new MainPage(driver, wait);
    }

    /***
     * Testing successful login and logout process
     */
    @Test
    public void testSuccessfulLoginAndLogout() {
        //Given
        List<String> assertMenuItems = ConfigurationReader.readValue("tests.myAccountMenuItems", List.class);
        String greeting = ConfigurationReader.readValue("tests.greeting",String.class);
        String mainPageTitle = ConfigurationReader.readValue("tests.mainPageTitle", String.class);

        //Action
        login();

        //Then
        Assert.assertTrue(greeting
                .equalsIgnoreCase(myAccountPage.getGreeting()));
        Assert.assertEquals(assertMenuItems, myAccountPage.getMenuPoints());

        //Action
        mainPage = myAccountPage.logout();

        //Then
        Assert.assertEquals(mainPageTitle, mainPage.getCurrentPageTitle());
    }

    /***
     * Hover test at main page for my account menu items
     */
    @Test
    public void hoverTest() {
        //Given
        List<String> assertMenuItems = ConfigurationReader.readValue("tests.mainPageMenuItems", List.class);

        //Action
        login();
        mainPage = loginPage.returnToMainPage();

        //Then
        Assert.assertEquals(assertMenuItems, mainPage.hoverMyAccountAndGetMenuItems());
    }

    /***
     * Searching for a product and filtering it with checkboxes
     * 9 form sending to a server and 8 checkbox checking
     */
    @Test
    public void searchAndCheckboxTest() {
        this.driver.get(getBaseURL());
        mainPage.exitPopupIfPresent();
        searchResultPage = mainPage.search(ConfigurationReader.readValue("search.product",String.class));
        searchResultPage = searchResultPage.checkAllExtras();

        List<Boolean> checkBoxValues = searchResultPage.readExtrasCheckboxValues();
        Assert.assertEquals(8, checkBoxValues.size());
        checkBoxValues.forEach(Assert::assertTrue);
    }

    /***
     * Test if a static page loads after start
     */
    @Test
    public void staticPageLoadedTest() {
        //Given
        String storesPageTitle = ConfigurationReader.readValue("tests.storesPageTitle", String.class);
        String storesPageMainTitle = ConfigurationReader.readValue("tests.storesPageMainTitle", String.class);

        //Action
        openSiteAndExitPopUp();
        storesPage = mainPage.openStoresPage();

        //Then
        Assert.assertEquals(storesPageTitle ,storesPage.getCurrentPageTitle());
        Assert.assertEquals(storesPageMainTitle, storesPage.getMainTitle());
    }

    /***
     * Test for extendable static pages from configuration file's array
     */
    @Test
    public void staticPagesFromArrayTest() {
        //Given
        List<String> pages = ConfigurationReader.readValue("staticPagesToOpen",List.class);
        String prefix = ConfigurationReader.readValue("myAccountPrefix",String.class);
        List<String> pagesWithUrl = pages.stream().map(p -> prefix + p).toList();

        //Action
        login();
        List<String> pageUrls = myAccountPage.openAndGetStaticPagesURL(pages);

        //Then
        Assert.assertEquals(pagesWithUrl, pageUrls);
    }

    /***
     * Form sending with user
     * changes the password and changes back for the other tests to pass with original password
     */
    @Test
    public void changePasswordFormSendingWithUser() {
        //Given
        String oldPassword = ConfigurationReader.readValue("user.password", String.class);
        String newPassword = ConfigurationReader.readValue("user.newPassword", String.class);
        String systemMessage = ConfigurationReader.readValue("tests.successfulPwChangeMessage", String.class);

        //Action
        login();
        myAccountPage.changePassword(newPassword);

        //Then
        Assert.assertTrue(systemMessage.equalsIgnoreCase(myAccountPage.getSystemMessage()));

        //Action
        myAccountPage.changePassword(oldPassword);

        //Then
        Assert.assertTrue(systemMessage.equalsIgnoreCase(myAccountPage.getSystemMessage()));
    }

    /***
     * Searching for a product, opening it and reading possible value from dropdown field
     */
    @Test
    public void readDropDownInputValue(){
        //Given
        String searchProduct = ConfigurationReader.readValue("search.notebook",String.class);
        String name = ConfigurationReader.readValue("tests.dropDownValueName", String.class);
        String price = ConfigurationReader.readValue("tests.dropDownValuePrice", String.class);

        //Action
        openSiteAndExitPopUp();
        searchResultPage = mainPage.search(searchProduct);
        productPage = searchResultPage.openAsusZenbookProductPage();
        Pair<String, String> nameAndPrice = productPage.readDropDownValues();

        //Then
        Assert.assertEquals(name, nameAndPrice.getKey());
        Assert.assertTrue(nameAndPrice.getValue().startsWith(price));
    }

    /***
     * Javascript executor test with alert for user
     */
    @Test
    public void javascriptExecutorTest() {
        //Given
        String message = ConfigurationReader.readValue("tests.alertMessage", String.class);

        //Action
        openSiteAndExitPopUp();
        servicePage = mainPage.openServicePage();
        String alertedMessage = servicePage.alertUser(message);

        //Then
        Assert.assertEquals(message, alertedMessage);
    }

    // login process
    private void login() {
        this.driver.get(getBaseURL());
        loginPage = mainPage.clickLogin();

        myAccountPage = loginPage.login();
    }

    // base URL for page under test
    private String getBaseURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigurationReader.readValue("protocol", String.class));
        sb.append("://");
        sb.append(ConfigurationReader.readValue("domain", String.class));

        return sb.toString();
    }

    // opening base URL and exit popup process
    private void openSiteAndExitPopUp() {
        this.driver.get(getBaseURL());
        mainPage.exitPopupIfPresent();
    }

    // additional sleep for a given ms (presentation purposes only)
    private void sleepFor(long ms) {
        try {
            TimeUnit.SECONDS.sleep(ms);
        }catch (InterruptedException e) { }
    }

    /***
     * Teardown after each test
     */
    @After
    public void tearDown() {
        if(this.driver != null) {
            this.driver.quit();
        }
    }
}
