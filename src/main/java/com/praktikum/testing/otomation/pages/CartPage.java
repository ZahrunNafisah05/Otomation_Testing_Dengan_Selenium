package com.praktikum.testing.otomation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

/**
 * Page Object untuk halaman Shopping Cart
 * URL: http://demowebshop.tricentis.com/cart
 */
public class CartPage extends BasePage {

    // Locators
    @FindBy(className = "cart-item-row")
    private List<WebElement> cartItems;

    @FindBy(name = "removefromcart")
    private List<WebElement> removeCheckboxes;

    @FindBy(name = "updatecart")
    private WebElement updateCartButton;

    @FindBy(name = "continueshopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "product-unit-price")
    private List<WebElement> productPrices;

    @FindBy(className = "product-subtotal")
    private List<WebElement> productSubtotals;

    @FindBy(className = "cart-total")
    private WebElement cartTotal;

    @FindBy(name = "itemquantity")
    private List<WebElement> quantityInputs;

    @FindBy(className = "order-summary-content")
    private WebElement emptyCartMessage;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "termsofservice")
    private WebElement termsCheckbox;

    // Constructor
    public CartPage(WebDriver driver) {
        super(driver); // Memanggil constructor BasePage untuk inisialisasi driver & wait
    }

    // Buka halaman cart
    public void goToCartPage() {
        driver.get("http://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));
    }

    // Dapatkan jumlah item di cart
    public int getItemCount() {
        // Tunggu sampai minimal ada elemen terlihat (jika ada item)
        // Kita gunakan try-catch atau check size karena jika kosong, wait ini bisa timeout
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(cartItems));
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    // Hapus item dari cart berdasarkan index baris
    public void removeItem(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < removeCheckboxes.size()) {

            click(removeCheckboxes.get(itemIndex));
            click(updateCartButton);

            // Tunggu sampai tabel refresh (item yang dihapus menjadi stale/hilang)
            // Ini penting untuk menghindari StaleElementReferenceException
            try {
                wait.until(ExpectedConditions.stalenessOf(cartItems.get(0)));
            } catch (Exception e) {
                // Ignore if list becomes empty
            }
        }
    }

    // Update quantity item
    public void updateQuantity(int itemIndex, int quantity) {
        if (itemIndex >= 0 && itemIndex < quantityInputs.size()) {
            WebElement qtyInput = quantityInputs.get(itemIndex);
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));

            click(updateCartButton);
            wait.until(ExpectedConditions.visibilityOf(cartTotal));
        }
    }

    // Dapatkan total cart
    public String getTotal() {
        waitForVisible(cartTotal);
        return getText(cartTotal);
    }

    // Continue shopping
    public void continueShopping() {
        click(continueShoppingButton);
    }

    // Cek apakah cart kosong
    public boolean isEmpty() {
        try {
            return isDisplayed(emptyCartMessage) &&
                    getText(emptyCartMessage).contains("Your Shopping Cart is empty!");
        } catch (Exception e) {
            return false;
        }
    }

    // Checkout
    public void checkout() {
        click(termsCheckbox); // Centang terms of service
        click(checkoutButton);
    }

    // Dapatkan nama produk di cart berdasarkan index
    public String getProductName(int index) {
        if (index >= 0 && index < cartItems.size()) {
            // Mencari elemen nama produk DI DALAM baris item tersebut
            return cartItems.get(index)
                    .findElement(org.openqa.selenium.By.className("product-name"))
                    .getText();
        }
        return "";
    }
}