package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends PageBase {
    private final By emailInputLocator = By.xpath("//input[@type='text' and @id='email' and @name = 'email']");
    private final By passwordInputLocator = By.xpath("//input[@type='password' and @id='password' and @name = 'password']");
    private final By loginButtonLocator = By.xpath("//button[@type='submit' and @name='login']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        waitAndReturnWebElement(bodyLocator);
    }

    public MyAccountPage loginWithCredentials(String email, String password) {
        WebElement emailInput = waitAndReturnWebElement(emailInputLocator);
        emailInput.sendKeys(email);

        WebElement passwordInput = waitAndReturnWebElement(passwordInputLocator);
        passwordInput.sendKeys(password);

        WebElement loginButton = waitAndReturnWebElement(loginButtonLocator);
        loginButton.click();

        return new MyAccountPage(driver, wait);
    }

    public MyAccountPage login() {
        String username = ConfigurationReader.readValue("user.email", String.class);
        String password = ConfigurationReader.readValue("user.password", String.class);
        return loginWithCredentials(username,password);
    }
}
