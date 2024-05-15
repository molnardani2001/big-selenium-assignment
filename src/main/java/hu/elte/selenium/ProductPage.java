package hu.elte.selenium;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage extends PageBase {
    private final By dropdownLocator = By.xpath("//div[@id = 'extension-list']//div[@id = 'extension-selected-2']");
    private final By dropdownEntryLocator = By.xpath("//div[@data-id = '4402113_1']");
    public ProductPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public Pair<String, String> readDropDownValues() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000)");
        WebElement dropDown = waitAndReturnWebElement(dropdownLocator);
        dropDown.click();

        WebElement dropdownEntry = waitAndReturnWebElement(dropdownEntryLocator);

        WebElement name = dropdownEntry.findElement(By.xpath("//div[@data-id = '4402113_1']/div[@class = 'name']"));
        WebElement price = dropdownEntry.findElement(By.xpath("//div[@data-id = '4402113_1']/div[@class = 'price']")) ;

        return Pair.of(name.getText().trim(), price.getText().trim());
    }
}
