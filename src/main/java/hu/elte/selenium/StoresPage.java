package hu.elte.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StoresPage extends PageBase{
    private final By mainTitleLocator = By.xpath("//*[@id='main-container']/main/div[2]/div[1]/p[1]/strong");
    public StoresPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getMainTitle() {
        WebElement mainTitle = waitAndReturnWebElement(mainTitleLocator);
        return mainTitle.getText();
    }
}
