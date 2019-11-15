package explore.demos.java.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceIntegrationTest.class);

    protected static boolean RUN_HTMLUNIT;

    protected static boolean RUN_IE;

    protected static boolean RUN_FIREFOX;

    protected static boolean RUN_CHROME;

    protected static boolean RUN_OPERA;

    protected static String SELENIUM_HUB_URL;

    protected static String TARGET_SERVER_URL;

    @BeforeAll
    public static void initEnvironment() {

        RUN_HTMLUNIT = getConfigurationProperty("RUN_HTMLUNIT", "test.run.htmlunit", true);

        logger.info("running the tests in HtmlUnit: " + RUN_HTMLUNIT);

        RUN_IE = getConfigurationProperty("RUN_IE", "test.run.ie", false);

        logger.info("running the tests in Internet Explorer: " + RUN_IE);

        RUN_FIREFOX = getConfigurationProperty("RUN_FIREFOX", "test.run.firefox", false);

        logger.info("running the tests in Firefox: " + RUN_FIREFOX);

        RUN_CHROME = getConfigurationProperty("RUN_CHROME", "test.run.chrome", false);

        logger.info("running the tests in Chrome: " + RUN_CHROME);

        RUN_OPERA = getConfigurationProperty("RUN_OPERA", "test.run.opera", false);

        logger.info("running the tests in Opera: " + RUN_OPERA);

        SELENIUM_HUB_URL = getConfigurationProperty(
            "SELENIUM_HUB_URL", "test.selenium.hub.url", "http://localhost:4444/wd/hub");

        logger.info("using Selenium hub at: " + SELENIUM_HUB_URL);

        TARGET_SERVER_URL = getConfigurationProperty(
            "TARGET_SERVER_URL", "test.target.server.url", "http://localhost:8080/");

        logger.info("using target server at: " + TARGET_SERVER_URL);
    }

    private static String getConfigurationProperty(String envKey, String sysKey, String defValue) {

        String retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = sysValue;
        } else if (envValue != null) {
            retValue = envValue;
        }
        return retValue;
    }

    private static boolean getConfigurationProperty(String envKey, String sysKey, boolean defValue) {

        boolean retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = Boolean.parseBoolean(sysValue);
        } else if (envValue != null) {
            retValue = Boolean.parseBoolean(envValue);
        }
        return retValue;
    }

    @Test
    public void testHtmlUnit()
        throws MalformedURLException, IOException {

        Assumptions.assumeTrue(RUN_HTMLUNIT);

        logger.info("executing test in htmlunit");

        WebDriver driver = null;

        try {
            driver = new HtmlUnitDriver(true);
            testHello(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testIE()
        throws MalformedURLException, IOException {

        Assumptions.assumeTrue(RUN_IE);

        logger.info("executing test in internet explorer");

        WebDriver driver = null;
        try {
            Capabilities browser = new InternetExplorerOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
            testHello(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testFirefox()
        throws MalformedURLException, IOException {

        Assumptions.assumeTrue(RUN_FIREFOX);

        logger.info("executing test in firefox");

        WebDriver driver = null;
        try {
            Capabilities browser = new FirefoxOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
            testHello(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testChrome()
        throws MalformedURLException, IOException {

        Assumptions.assumeTrue(RUN_CHROME);

        logger.info("executing test in chrome");

        WebDriver driver = null;
        try {
            Capabilities browser = new ChromeOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
            testHello(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    public void testOpera()
        throws MalformedURLException, IOException {

        Assumptions.assumeTrue(RUN_OPERA);

        logger.info("executing test in opera");

        WebDriver driver = null;
        try {
            Capabilities browser = new OperaOptions();
            driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), browser);
            testHello(driver, TARGET_SERVER_URL);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void testHello(WebDriver driver, String baseUrl) {

        WebElement body = (new WebDriverWait(driver, 10)).until(
            d -> {
                d.get(baseUrl + "hello");
                return d.findElement(By.xpath("/html/body"));
            });

        assertEquals("Hello!", body.getText(), "HelloGreeting service should respond with 'Hello!' greeting");
    }
}
