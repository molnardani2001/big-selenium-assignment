package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends PageBase {
    private final By myAccountLocator = By.xpath("//a[@id='user_menu_account' and @href='https://www.notebook.hu/ugyfelkapu/fiokom']");

    private final By loginLocator = By.xpath("//a[@href='https://www.notebook.hu/ugyfelkapu/bejelentkezes']");

    private final By menuItemsLocator = By.xpath("//div[@class='user-menu menu']/ul");

    private final By searchBarLocator = By.xpath("//input[@type = 'text' and @class = 'form-control mr-sm-2 tf_key prefixbox-autocomplete-input-desktop']");

    public MainPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage clickLogin() {
        exitPopupIfPresent();
        hoverMyAccount();

        WebElement loginButton = waitAndReturnWebElement(loginLocator);
        loginButton.click();

        return new LoginPage(driver, wait);
    }

    public List<String> hoverMyAccountAndGetMenuItems() {
        List<String> menuItemNames = new ArrayList<>();

        hoverMyAccount();
        WebElement menu = waitAndReturnWebElement(menuItemsLocator);
        return getLiNamesFromUl(menu);
    }

    private void hoverMyAccount() {
        Actions actions = new Actions(driver);
        WebElement myAccountHover = waitAndReturnWebElement(myAccountLocator);

        actions.moveToElement(myAccountHover).perform();
    }

    public SearchResultPage search(String text) {
        WebElement searchBar = waitAndReturnWebElement(searchBarLocator);
        searchBar.sendKeys(text);
        searchBar.sendKeys(Keys.ENTER);

        return new SearchResultPage(driver, wait);
    }
}
