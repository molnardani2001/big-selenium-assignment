package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage extends PageBase {
    private final By logoutButtonLocator = By.xpath("//button[@type='submit' and @name='frontend_logout_submit']");

    private final By playstationHoverLocator = By.xpath("//a[@href='/playstation/']");

    private final By PS5ConsoleLocator = By.xpath("//*[@id=\"menu\"]/ul/li[10]/div/div/div[1]/ul/li[1]/a");
    public MainPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        waitAndReturnWebElement(bodyLocator);
    }

    public void navigateToPlaystation5Page() {
        this.driver.get(getSpecificDomain());
        WebElement playstationHover = waitAndReturnWebElement(playstationHoverLocator);

        Actions actions = new Actions(driver);

        actions.moveToElement(playstationHover);
        WebElement playstation5Hover = waitAndReturnWebElement(PS5ConsoleLocator);

        actions.moveToElement(playstation5Hover);

        actions.click().build().perform();
    }

    public void logout() {
        WebElement logoutButton = waitAndReturnWebElement(logoutButtonLocator);
        logoutButton.click();
    }

    @Override
    public String getSpecificDomain() {
        return
                getDomain()
                        + ConfigurationReader.readValue("pages.mainPage", String.class);
    }
}
