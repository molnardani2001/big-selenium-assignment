package hu.elte.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public abstract class PageBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected By bodyLocator = By.tagName("body");
    protected final By acceptPoputLocator = By.cssSelector("[data-cky-tag='accept-button']");
    protected final By mainPageLogoLocator = By.xpath("//span[@id='logo_img']");
    public PageBase(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebElement waitAndReturnWebElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    }

    public void exitPopupIfPresent() {
        WebDriverWait oldWait = wait;
        WebDriverWait newWait = new WebDriverWait(driver, 5);
        this.wait = newWait;

        WebElement proceedHungarianButton = waitAndReturnWebElement(acceptPoputLocator);
        proceedHungarianButton.click();

        this.wait = oldWait;
    }

    public MainPage returnToMainPage() {
        WebElement mainPageLogo = waitAndReturnWebElement(mainPageLogoLocator);
        mainPageLogo.click();

        return new MainPage(driver, wait);
    }

    public List<String> getLiNamesFromUl(WebElement element) {
        List<String> menuItemNames = new ArrayList<>();
        List<WebElement> listItems = element.findElements(By.tagName("li"));

        for(WebElement item : listItems) {
            menuItemNames.add(item.findElement(By.tagName("a")).getText());
        }

        return menuItemNames;
    }

    public String getCurrentPageTitle() {
        return this.driver.getTitle();
    }

    public String getCurrentPageUrl() {
        return this.driver.getCurrentUrl();
    }
}
