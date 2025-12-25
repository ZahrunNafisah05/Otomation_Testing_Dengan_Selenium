package com.praktikum.testing.otomation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object untuk Registration Page
 * URL: http://demowebshop.tricentis.com/register
 */
public class RegistrationPage extends BasePage {

    // --- Locators ---
    @FindBy(linkText = "Register")
    private WebElement registerLink;

    @FindBy(id = "gender-male")
    private WebElement genderMaleRadio;

    @FindBy(id = "gender-female")
    private WebElement genderFemaleRadio;

    @FindBy(id = "FirstName")
    private WebElement firstNameField;

    @FindBy(id = "LastName")
    private WebElement lastNameField; // Typo fix: LactName -> LastName

    @FindBy(id = "Email")
    private WebElement emailField;

    @FindBy(id = "Password")
    private WebElement passwordField;

    @FindBy(id = "ConfirmPassword")
    private WebElement confirmPasswordField;

    @FindBy(id = "register-button")
    private WebElement registerButton;

    @FindBy(className = "result")
    private WebElement successMessage;

    @FindBy(css = "div.page-title h1")
    private WebElement pageTitle;

    // --- Validation Error Messages ---
    @FindBy(css = "span[for='FirstName']")
    // Note: Selector disesuaikan dengan standar DemoWebShop, bisa juga #FirstName-error tergantung versi
    private WebElement firstNameError;

    @FindBy(css = "span[for='LastName']")
    private WebElement lastNameError;

    @FindBy(css = "span[for='Email']")
    private WebElement emailError;

    @FindBy(css = "span[for='Password']")
    private WebElement passwordError;

    @FindBy(css = "span[for='ConfirmPassword']")
    private WebElement confirmPasswordError;

    // Constructor
    public RegistrationPage(WebDriver driver) {
        super(driver); // Panggil constructor BasePage
    }

    // --- Actions ---

    // Buka halaman registrasi
    public void navigateToRegisterPage() {
        navigateTo("http://demowebshop.tricentis.com/");
        click(registerLink);
        wait.until(ExpectedConditions.urlContains("/register"));
    }

    // Pilih Gender
    public void selectGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }

        if (gender.equalsIgnoreCase("Male")) {
            click(genderMaleRadio);
        } else if (gender.equalsIgnoreCase("Female")) {
            click(genderFemaleRadio);
        } else {
            throw new IllegalArgumentException("Gender must be 'Male' or 'Female'");
        }
    }

    // Input Data Methods
    public void enterFirstName(String firstName) {
        enterText(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        enterText(lastNameField, lastName);
    }

    public void enterEmail(String email) {
        enterText(emailField, email);
    }

    public void enterPassword(String password) {
        enterText(passwordField, password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        enterText(confirmPasswordField, confirmPassword);
    }

    public void clickRegisterButton() {
        click(registerButton);
    }

    // Method Komplit: Register User
    public void registerUser(String gender, String firstName, String lastName,
                             String email, String password) {
        selectGender(gender);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password); // Confirm pass biasanya sama dengan pass
        clickRegisterButton();

        // Tunggu: Entah sukses ATAU ada error muncul
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(successMessage),
                    ExpectedConditions.visibilityOf(emailError),
                    ExpectedConditions.visibilityOf(firstNameError)
            ));
        } catch (Exception e) {
            // Ignore timeout, assertion di test class yang akan menangani
        }
    }

    // --- Verifications / Getters ---

    public String getSuccessMessage() {
        try {
            waitForVisible(successMessage);
            return getText(successMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isRegistrationSuccessful() {
        try {
            return isDisplayed(successMessage) &&
                    getSuccessMessage().contains("Your registration completed");
        } catch (Exception e) {
            return false;
        }
    }

    // --- Error Message Getters ---

    public String getFirstNameError() {
        try { return getText(firstNameError); } catch (Exception e) { return ""; }
    }

    public String getLastNameError() {
        try { return getText(lastNameError); } catch (Exception e) { return ""; }
    }

    public String getEmailError() {
        try { return getText(emailError); } catch (Exception e) { return ""; }
    }

    public String getPasswordError() {
        try { return getText(passwordError); } catch (Exception e) { return ""; }
    }

    public String getConfirmPasswordError() {
        try { return getText(confirmPasswordError); } catch (Exception e) { return ""; }
    }

    // Cek apakah ada validasi error yang muncul
    public boolean hasValidationErrors() {
        return !getFirstNameError().isEmpty() ||
                !getLastNameError().isEmpty() ||
                !getEmailError().isEmpty() ||
                !getPasswordError().isEmpty() ||
                !getConfirmPasswordError().isEmpty();
    }
}