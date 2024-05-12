package hu.elte.selenium;

import hu.elte.config.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends PageBase {
    private final By emailInputLocator = By.xpath("//input[@type='email' and @name='user_name_or_email']");
    private final By passwordInputLocator = By.xpath("//input[@type='password' and @name='user_password']");
    private final By loginButtonLocator = By.xpath("//input[@type='submit' and @name='frontend_login_submit']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        waitAndReturnWebElement(bodyLocator);
    }

    public MainPage loginWithCredentials(String email, String password) {
        this.driver.get(getSpecificDomain());
        WebElement emailInput = waitAndReturnWebElement(emailInputLocator);
        emailInput.sendKeys(email);

        WebElement passwordInput = waitAndReturnWebElement(passwordInputLocator);
        passwordInput.sendKeys(password);

        WebElement loginButton = waitAndReturnWebElement(loginButtonLocator);
        loginButton.click();

        clickRandom();

        return new MainPage(driver, wait);
    }

    public MainPage login() {
        return loginWithCredentials(ConfigurationReader.readValue("user.email", String.class),
                ConfigurationReader.readValue("user.password", String.class));
    }

    @Override
    public String getSpecificDomain() {
        return
                getDomain()
                + ConfigurationReader.readValue("pages.loginPage", String.class);
    }
}
