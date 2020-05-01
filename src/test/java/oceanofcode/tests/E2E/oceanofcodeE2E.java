package oceanofcode.tests.E2E;

import static privateConstants.privateConstants.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.seljup.SeleniumExtension;

@ExtendWith(SeleniumExtension.class)
public class oceanofcodeE2E {
	ChromeDriver driver;

    public oceanofcodeE2E(ChromeDriver driver) {
        this.driver = driver;
    }
    
	@Test
    public void testWithChrome() {
        // Use Chrome in this test
		driver.get("https://www.codingame.com/ide/puzzle/ocean-of-code");
        //assertThat(driver.getTitle()).isEqualTo("Selenium-Jupiter: JUnit 5 extension for Selenium");
        
        driver.findElementsByName("email");
        WebElement email = driver.findElementByName("email");
        WebElement password = driver.findElementByName("password");
        
        email.sendKeys(CODINGAME_EMAIL);
        password.sendKeys(CODINGAME_PASSWORD);
    }

}
