package hu.elte.selenium;

import org.apache.xpath.operations.Bool;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.LinkedList;
import java.util.List;

public class SearchResultPage extends PageBase {
    private final By extrasLocator = By.xpath("//div[@class = 'extras_tel_tab prefixbox-filter-container prefixbox-filter-count-8 ']");
    private final By extrasCheckboxesLocator = By.xpath("//div[@class='extras_tel_tab prefixbox-filter-container prefixbox-filter-count-8 prefixbox-open']//input[@type = 'checkbox']");
    private final By notebookProductLocator = By.xpath("//a[@href= 'https://www.notebook.hu/asus-zenbook-14-oled-um3402-um3402ya-km226-348395']");
    public SearchResultPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public SearchResultPage checkAllExtras() {
        scrollToExtras();
        List<WebElement> checkboxes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(extrasCheckboxesLocator));

        for(int i = 0; i < checkboxes.size(); ++i) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxes.get(i));
            scrollToExtras();
            checkboxes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(extrasCheckboxesLocator));
        }

        return this;
    }

    private void scrollToExtras() {
        WebElement extras = waitAndReturnWebElement(extrasLocator);
        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;

        jsDriver.executeScript(
                "arguments[0].scrollIntoView({ block: 'center'});",
                extras
        );

        extras.click();
    }

    public List<Boolean> readExtrasCheckboxValues() {
        List<WebElement> checkBoxes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(extrasCheckboxesLocator));
        return checkBoxes.stream().map(WebElement::isSelected).toList();
    }

    public ProductPage openAsusZenbookProductPage() {
        WebElement product = waitAndReturnWebElement(notebookProductLocator);
        product.click();

        return new ProductPage(driver, wait);
    }
}
