import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseTrackerTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
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