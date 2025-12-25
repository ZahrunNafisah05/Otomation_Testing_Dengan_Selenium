package com.praktikum.testing.otomation.test.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FormInteractionDemo {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testSuccessfulLogin() {
        System.out.println("\n=== TEST: SUCCESSFUL LOGIN ===");
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println("Navigated to login page");

        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.clear();
        usernameField.sendKeys("tomsmith");
        System.out.println("Entered username: tomsmith");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys("SuperSecretPassword!");
        System.out.println("Entered password");

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        System.out.println("Clicked login button");

        WebElement successMessage = driver.findElement(By.id("flash"));
        String messageText = successMessage.getText();
        System.out.println("Success message: " + messageText);

        Assert.assertTrue(messageText.contains("You logged into a secure area!"), "Success message should be displayed");
        Assert.assertTrue(driver.getCurrentUrl().contains("/secure"), "URL should change to secure page");
        System.out.println("Login test PASSED\n");
    }

    @Test
    public void testInvalidLogin() {
        System.out.println("\n=== TEST: INVALID LOGIN ===");
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("invaliduser");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMessage = driver.findElement(By.id("flash"));
        String errorText = errorMessage.getText();
        System.out.println("Error message: " + errorText);

        Assert.assertTrue(errorText.contains("Your username is invalid!"), "Error message should be displayed for invalid credentials");
        System.out.println("Invalid login test PASSED\n");
    }

    @Test
    public void testEmptyFormSubmission() {
        System.out.println("\n=== TEST: EMPTY FORM SUBMISSION ===");
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMessage = driver.findElement(By.id("flash"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed");
        System.out.println("Empty form test PASSED\n");
    }

    @Test
    public void demonstrateFormElements() {
        System.out.println("\n=== DEMONSTRATING FORM ELEMENTS ===");
        driver.get("https://the-internet.herokuapp.com/inputs");

        WebElement inputField = driver.findElement(By.tagName("input"));
        inputField.sendKeys("12345");
        System.out.println("Entered number: 12345");

        inputField.clear();
        inputField.sendKeys("67890");
        System.out.println("Cleared and entered new number: 67890");

        String value = inputField.getAttribute("value");
        Assert.assertEquals(value, "67890", "Input value should match");
        System.out.println("Verified input value: " + value);
        System.out.println("Form elements test PASSED\n");
    }

    @Test
    public void demonstrateCheckboxes() {
        System.out.println("\n=== DEMONSTRATING CHECKBOXES ===");
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        WebElement checkbox1 = driver.findElement(By.xpath("(//input[@type='checkbox'])[1]"));
        WebElement checkbox2 = driver.findElement(By.xpath("(//input[@type='checkbox'])[2]"));

        System.out.println("Checkbox 1 selected: " + checkbox1.isSelected());
        System.out.println("Checkbox 2 selected: " + checkbox2.isSelected());

        if (!checkbox1.isSelected()) {
            checkbox1.click();
            System.out.println("Checked checkbox 1");
        }
        if (checkbox2.isSelected()) {
            checkbox2.click();
            System.out.println("Unchecked checkbox 2");
        }

        Assert.assertTrue(checkbox1.isSelected(), "Checkbox 1 should be selected");
        Assert.assertFalse(checkbox2.isSelected(), "Checkbox 2 should be unselected");
        System.out.println("Checkbox test PASSED\n");
    }

    @Test
    public void demonstrateDropdown() {
        System.out.println("\n=== DEMONSTRATING DROPDOWN ===");
        driver.get("https://the-internet.herokuapp.com/dropdown");

        WebElement dropdownElement = driver.findElement(By.id("dropdown"));
        org.openqa.selenium.support.ui.Select dropdown = new org.openqa.selenium.support.ui.Select(dropdownElement);

        dropdown.selectByVisibleText("Option 1");
        System.out.println("Selected: Option 1");
        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 1");

        dropdown.selectByValue("2");
        System.out.println("Selected: Option 2 (by value)");
        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 2");

        dropdown.selectByIndex(1);
        System.out.println("Selected: Option 1 (by index)");

        System.out.println("\nAll dropdown options:");
        dropdown.getOptions().forEach(option -> System.out.println(" - " + option.getText()));
        System.out.println("\nDropdown test PASSED\n");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}