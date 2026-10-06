package sk.testlab.cvicenie02;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import sk.testlab.cvicenie02.support.BrowserFactory;
import sk.testlab.cvicenie02.support.TestConfig;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private TestConfig config;


    @BeforeEach
    void setUp() {
        // Load URL, credentials and browser settings.
        config = TestConfig.load();

        // Start a fresh browser.
        driver = BrowserFactory.create(config);

        // Prepare explicit waiting.
        wait = new WebDriverWait(driver, config.timeout);
    }


    @AfterEach
    void tearDown() {
        // Close the browser after the test.
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    void createReferenceOrder() throws IOException {

        try {

            // Open the application.
            driver.get(config.baseUrl);


            // -------------------------
            // LOGIN
            // -------------------------

            // Open login form.
            click("nav-login");

            // Enter email.
            type("login-email", config.email);

            // Enter password.
            type("login-password", config.password);

            // Submit login.
            click("login-submit");

            // Wait until user is logged in.
            visible("current-user");


            // -------------------------
            // PRODUCTS
            // -------------------------

            // Add first keyboard.
            click("add-to-cart-KB-001");

            // Wait for cart update.
            wait.until(
                    ExpectedConditions.textToBe(
                            testId("cart-count"), "1"
                    )
            );

            // Add second keyboard.
            click("add-to-cart-KB-001");

            // Wait for cart update.
            wait.until(
                    ExpectedConditions.textToBe(
                            testId("cart-count"), "2"
                    )
            );

            // Add one mouse.
            click("add-to-cart-MS-001");

            // Wait for cart update.
            wait.until(
                    ExpectedConditions.textToBe(
                            testId("cart-count"), "3"
                    )
            );


            // -------------------------
            // CART
            // -------------------------

            // Open shopping cart.
            click("nav-cart");

            // Select courier delivery.
            new Select(
                    visible("delivery-method")
            ).selectByValue("courier");


            // -------------------------
            // COUPON
            // -------------------------

            // Remember current price element.
            WebElement oldQuote = visible("quote-total");

            // Enter coupon.
            type("coupon-input", "STUDENT10");

            // Apply coupon.
            click("coupon-apply");

            // Wait until application refreshes the price.
            wait.until(
                    ExpectedConditions.stalenessOf(oldQuote)
            );

            // Wait for new price.
            visible("quote-total");

            // -------------------------
            // CHECKOUT
            // -------------------------

            // Continue to checkout.
            click("proceed-checkout");

            // Wait for checkout form.
            visible("checkout-form");

            // Enter customer name.
            type("customer-name", "Student Testovaci");

            // Enter customer email.
            type("customer-email", "student@example.test");

            // Enter address.
            type("customer-address", "Testovacia 12");

            // Enter city.
            type("customer-city", "Nitra");

            // Enter postal code.
            type("customer-postal-code", "949 01");

            // Accept terms.
            click("accept-terms");


            // -------------------------
            // CREATE ORDER
            // -------------------------

            // Submit the order.
            click("place-order");

            // -------------------------
            // SAVE UUID
            // -------------------------

            // Read generated order UUID.
            String orderId =
                    visible("order-id")
                            .getText()
                            .trim();

            // Save UUID into a file.
            saveOrderId(orderId);


            // -------------------------
            // READ ACTUAL RESULT
            // -------------------------

            // Find final order price.
            WebElement total =
                    visible("order-total");

            // Read price in cents.
            String actualCents =
                    total.getDomAttribute("data-cents");

            // Read visible price.
            String actualVisiblePrice =
                    normalizeSpaces(total.getText());

            // Read order status.
            String actualStatus =
                    visible("order-status")
                            .getDomAttribute("data-status");


            // -------------------------
            // ASSERTIONS
            // -------------------------

            // Verify all required results.
            assertAll(
                    "Reference order",

                    () -> assertFalse(
                            orderId.isBlank(),
                            "Order UUID must be displayed."
                    ),

                    () -> assertEquals(
                            "8563",
                            actualCents,
                            "Price in cents is incorrect."
                    ),

                    () -> assertEquals(
                            "85,63 €",
                            actualVisiblePrice,
                            "Visible price is incorrect."
                    ),

                    () -> assertEquals(
                            "created",
                            actualStatus,
                            "Order status is incorrect."
                    )
            );

        } catch (RuntimeException | AssertionError error) {

            // Save screenshot before browser closes.
            takeScreenshot("failure.png");

            // Keep the original test failure.
            throw error;
        }
    }

    // Create locator using data-testid.
    private By testId(String id) {
        return By.cssSelector(
                "[data-testid='" + id + "']"
        );
    }

    // Wait until element is visible.
    private WebElement visible(String id) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        testId(id)
                )
        );
    }

    // Wait until element is clickable and click.
    private void click(String id) {
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        testId(id)
                )
        ).click();
    }

    // Clear input and enter text.
    private void type(String id, String value) {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(
                        testId(id)
                )
        );

        element.clear();
        element.sendKeys(value);
    }

    // Save generated order UUID.
    private void saveOrderId(String orderId) throws IOException {

        Path directory =
                Path.of("target", "artifacts");

        Files.createDirectories(directory);

        Files.writeString(
                directory.resolve("order-id.txt"),
                orderId
        );
    }

    // Save browser screenshot.
    private void takeScreenshot(String fileName) {

        try {
            Path directory =
                    Path.of("target", "artifacts");

            Files.createDirectories(directory);

            File screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            Files.copy(
                    screenshot.toPath(),
                    directory.resolve(fileName)
            );

        } catch (Exception e) {
            System.err.println(
                    "Screenshot could not be saved: "
                            + e.getMessage()
            );
        }
    }

    // Normalize normal and non-breaking spaces.
    private String normalizeSpaces(String text) {
        return text
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }
}