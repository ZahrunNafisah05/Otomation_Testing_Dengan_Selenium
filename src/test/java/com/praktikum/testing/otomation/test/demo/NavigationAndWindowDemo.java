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

import java.util.Set;

public class NavigationAndWindowDemo {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void demonstrateNavigation() {
        System.out.println("\n=== NAVIGATION COMMANDS ===");

        // Navigate to page
        driver.get("https://the-internet.herokuapp.com/");
        System.out.println("   Navigated to homepage");
        System.out.println("   Current URL: " + driver.getCurrentUrl());

        // Click on a link
        driver.findElement(By.linkText("Form Authentication")).click();
        System.out.println("   Clicked Form Authentication link");
        System.out.println("   Current URL: " + driver.getCurrentUrl());

        // Navigate back
        driver.navigate().back();
        System.out.println("   Navigated back");
        System.out.println("   Current URL: " + driver.getCurrentUrl());

        // Navigate forward
        driver.navigate().forward();
        System.out.println("   Navigated forward");
        System.out.println("   Current URL: " + driver.getCurrentUrl());

        // Refresh page
        driver.navigate().refresh();
        System.out.println("   Page refreshed");

        // Navigate to specific URL
        driver.navigate().to("https://the-internet.herokuapp.com/dropdown");
        System.out.println("   Navigated to dropdown page");
        System.out.println("   Current URL: " + driver.getCurrentUrl());

        System.out.println("\n   Navigation test PASSED\n");
    }

    @Test
    public void demonstrateMultipleWindows() {
        System.out.println("\n=== MULTIPLE WINDOWS HANDLING ===");

        // Navigate to multiple windows page
        driver.get("https://the-internet.herokuapp.com/windows");

        // Store original window handle
        String originalWindow = driver.getWindowHandle();
        System.out.println("   Original window handle: " + originalWindow);

        // Verify only one window is open
        Assert.assertEquals(driver.getWindowHandles().size(), 1);

        // Click link to open new window
        driver.findElement(By.linkText("Click Here")).click();
        System.out.println("   Clicked to open new window");

        // Wait for new window
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get all window handles
        Set<String> allWindows = driver.getWindowHandles();
        System.out.println("   Total windows open: " + allWindows.size());

        // Switch to new window
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                System.out.println("   Switched to new window");
                break;
            }
        }

        // Verify new window content
        String newWindowText = driver.findElement(By.tagName("h3")).getText();
        System.out.println("   New window heading: " + newWindowText);
        Assert.assertEquals(newWindowText, "New Window");

        // Close new window
        driver.close();
        System.out.println("   Closed new window");

        // Switch back to original window
        driver.switchTo().window(originalWindow);
        System.out.println("   Switched back to original window");

        // Verify we're back on original page
        String originalHeading = driver.findElement(By.tagName("h3")).getText();
        Assert.assertEquals(originalHeading, "Opening a new window");

        System.out.println("\n   Multiple windows test PASSED\n");
    }

    @Test
    public void demonstrateIframes() {
        System.out.println("\n=== IFRAME HANDLING ===");

        driver.get("https://the-internet.herokuapp.com/iframe");

        // Find and switch to iframe
        WebElement iframe = driver.findElement(By.id("mce_0_ifr"));
        driver.switchTo().frame(iframe);
        System.out.println("   Switched to iframe");

        // Interact with content inside iframe
        WebElement editor = driver.findElement(By.id("tinymce"));
        String originalText = editor.getText();
        System.out.println("   Original text: " + originalText);

        // Clear and type new text
        editor.clear();
        editor.sendKeys("Testing iframe interaction with Selenium!");
        System.out.println("   Entered new text in iframe");

        // Verify text changed
        String newText = editor.getText();
        Assert.assertEquals(newText, "Testing iframe interaction with Selenium!");

        // Switch back to main content
        driver.switchTo().defaultContent();
        System.out.println("   Switched back to main content");

        // Verify we can interact with main page
        WebElement heading = driver.findElement(By.tagName("h3"));
        Assert.assertEquals(heading.getText(), "An iFrame containing the TinyMCE WYSIWYG Editor");

        System.out.println("\n   Iframe test PASSED\n");
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