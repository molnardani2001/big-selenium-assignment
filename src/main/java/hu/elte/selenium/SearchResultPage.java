package hu.elte.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SearchResultPage extends PageBase {

    private final By extrasLocator = By.xpath("//div[@class = 'extras_tel_tab prefixbox-filter-container prefixbox-filter-count-8 ']");
    public SearchResultPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public SearchResultPage checkAllExtras() {
        WebElement extras = waitAndReturnWebElement(extrasLocator);
        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
        jsDriver.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", extras);
        jsDriver.executeScript("arguments[0].click();", extras);

        List<WebElement> f = extras.findElements(By.xpath("//div/*[starts-with(@id, '59834-extras_tel_tab')]"));

        return this;
    }
}
