package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyAccountPage extends PageBase {
    private final By greetingLocator = By.xpath("//div[@id='customer-account-content-container']/div[1]/div[1]");

    private final By leftSideMenuLocator = By.xpath("//div[@id='customer-account-menu-container']/ul");

    private final By logoutLocator = By.xpath("//li/a[@href = 'https://www.notebook.hu/ugyfelkapu/kilepes']");

    public static final String LINK_XPATH_TEMPLATE = "//div[@id='customer-account-menu-container']/ul/li/a[@class = 'd-flex justify-content-between align-items-center' and contains(@href, 'ugyfelkapu/LINK_PATTERN')]";
    private static final String LINK_PATTERN = "LINK_PATTERN";

    private final By informationLinkLocator = By.xpath(LINK_XPATH_TEMPLATE.replace(LINK_PATTERN, "informacio"));

    private final By oldPasswordLocator = By.xpath("//input[@id = 'password' and @type = 'password' and @name = 'password']");

    private final By newPasswordLocator = By.xpath("//input[@id = 'password_re' and @type = 'password' and @name = 'password_re']");

    private final By modifyPasswordButtonLocator = By.xpath("//button[@type = 'submit' and @id='btn_save' and @name = 'change_password']");

    private final By systemMessageLocator = By.xpath("//div[@id = 'sys_msg']/div/div/div[@class = 'messages_item']");
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

    public List<String> openAndGetStaticPagesURL(List<String> pages) {
        List<String> urls = new LinkedList<>();

        for(String page : pages) {
            By linkLocator = By.xpath(LINK_XPATH_TEMPLATE.replace(LINK_PATTERN, page));
            WebElement link = waitAndReturnWebElement(linkLocator);
            link.click();
            urls.add(getUrl());
        }

        return urls;
    }

    public void changePassword(String newPassword) {
        WebElement informationLink = waitAndReturnWebElement(informationLinkLocator);
        informationLink.click();

        WebElement oldPasswordInput = waitAndReturnWebElement(oldPasswordLocator);
        oldPasswordInput.sendKeys(newPassword);

        WebElement newPasswordInput = waitAndReturnWebElement(newPasswordLocator);
        newPasswordInput.sendKeys(newPassword);

        WebElement modifyPasswordButton = waitAndReturnWebElement(modifyPasswordButtonLocator);
        modifyPasswordButton.click();
    }

    public String getSystemMessage() {
        return waitAndReturnWebElement(systemMessageLocator).getText();
    }
}
