package hu.elte.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class MyAccountPage extends PageBase {
    private final By greetingLocator = By.xpath("//div[@id='customer-account-content-container']/div[1]/div[1]");

    private final By leftSideMenuLocator = By.xpath("//div[@id='customer-account-menu-container']/ul");

    private final By logoutLocator = By.xpath("//li/a[@href = 'https://www.notebook.hu/ugyfelkapu/kilepes']");
    public MyAccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getGreeting() {
        WebElement greeting = waitAndReturnWebElement(greetingLocator);
        return greeting.getText().trim();
    }

    public List<String> getMenuPoints() {
        WebElement leftSideMenu = waitAndReturnWebElement(leftSideMenuLocator);
        return getLiNamesFromUl(leftSideMenu);
    }

    public MainPage logout() {
        WebElement logoutButton = waitAndReturnWebElement(logoutLocator);
        logoutButton.click();

        return new MainPage(driver, wait);
    }
}
