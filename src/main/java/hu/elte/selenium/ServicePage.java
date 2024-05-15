package hu.elte.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServicePage extends PageBase{
    public ServicePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String alertUser(String message) {
        ((JavascriptExecutor) driver).executeScript("alert('" + message + "');");

        Alert alert = driver.switchTo().alert();
        String alertMessage = alert.getText();

        alert.accept();
        return alertMessage;
    }
}
