import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseTrackerTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
    }

    @Test
    void testAddExpense() {

        driver.get("http://localhost:8081");

        driver.findElement(By.id("description"))
                .sendKeys("Selenium Test Expense");

        driver.findElement(By.id("amount"))
                .sendKeys("500");

        driver.findElement(By.id("category"))
                .click();

        driver.findElement(By.xpath("//option[text()='Travel']"))
                .click();

        driver.findElement(By.id("addExpense"))
                .click();

        String pageText = driver.getPageSource();

        assertTrue(
                pageText.contains("Selenium Test Expense")
        );

        assertTrue(
                pageText.contains("₹500.00")
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}