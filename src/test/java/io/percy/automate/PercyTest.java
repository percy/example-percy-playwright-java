package io.percy.automate;


import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.percy.playwright.Percy;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PercyTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private Percy percy;

    public String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public String AUTOMATE_KEY =  System.getenv("BROWSERSTACK_ACCESS_KEY");

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception{
        playwright = Playwright.create();
        JsonObject capabilitiesObject = new JsonObject();
        capabilitiesObject.addProperty("browser", "chrome");    // allowed browsers are `chrome`, `edge`, `playwright-chromium`, `playwright-firefox` and `playwright-webkit`
        capabilitiesObject.addProperty("browser_version", "latest");
        capabilitiesObject.addProperty("os", "osx");
        capabilitiesObject.addProperty("os_version", "ventura");
        capabilitiesObject.addProperty("name", "Percy Playwright Java Example");
        capabilitiesObject.addProperty("build", "percy-playwright-java");
        capabilitiesObject.addProperty("browserstack.username", USERNAME);
        capabilitiesObject.addProperty("browserstack.accessKey", AUTOMATE_KEY);
        String caps = URLEncoder.encode(capabilitiesObject.toString(), "utf-8");
        String ws_endpoint = "wss://cdp.browserstack.com/playwright?caps=" + caps;
        browser = playwright.chromium().connect(ws_endpoint);
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
            percy.screenshot("screenshot_1");

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
            options.put("fullPage", true);
            percy.screenshot("screenshot_2", options);

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
        Object result;
        result = page.evaluate("_ => {}", "browserstack_executor: { \"action\": \"setSessionStatus\", \"arguments\": { \"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
    }
}
