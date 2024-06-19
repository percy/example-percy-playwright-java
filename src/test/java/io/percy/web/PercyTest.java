package io.percy.web;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.percy.playwright.Percy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PercyTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private Percy percy;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception{
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext();
        page = context.newPage();
        percy = new Percy(page);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    public void addProductToCart() {
        try {
            // Set the browser window size
            page.setViewportSize(1280, 1024);

            // Navigate to bstackdemo
            page.navigate("https://www.bstackdemo.com");

            // Check the title
            PlaywrightAssertions.assertThat(page).hasTitle("StackDemo");

            // Click on the apple product
            page.waitForSelector("//*[@id=\"__next\"]/div/div/main/div[1]/div[1]/label/span");
            page.click("//*[@id=\"__next\"]/div/div/main/div[1]/div[1]/label/span");

            // [percy note: important step]
            // Percy Screenshot 1
            percy.snapshot("snapshot_1");

            // Save the text of the product for later verify
            String productOnScreenText = page.textContent("//*[@id=\"1\"]/p");

            // Click on add to cart button
            page.waitForSelector("//*[@id=\"1\"]/div[4]");
            page.click("//*[@id=\"1\"]/div[4]");

            // See if the cart is opened or not
            page.waitForSelector(".float-cart__content");

            // Get text of product in cart
            String productOnCartText = page.textContent("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]");

            // [percy note: important step]
            // Percy Screenshot 2
            // Adding testcase and options
            Map<String, Object> options = new HashMap<>();
            options.put("testCase", "Should add product to cart");
            percy.snapshot("snapshot_2", options);

            if (productOnScreenText.equals(productOnCartText)) {
                markTestStatus("passed", "Title Matched", page);
            } else {
                markTestStatus("failed", "Title did not match", page);
            }

        } catch (Exception e) {
            System.out.println("Error occurred while executing script: " + e);
        }
    }

    public static void markTestStatus(String status, String reason, Page page) {
        System.out.println("Test Status: " + status + ";Reason: " + reason);
    }
}
