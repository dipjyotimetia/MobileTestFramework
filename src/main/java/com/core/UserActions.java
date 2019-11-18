package com.core;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import com.relevantcodes.extentreports.LogStatus;
import com.reporting.ExtentReports.ExtentTestManager;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * @author dipjyoti.metia
 */
public class UserActions extends DriverManager {

    private static final Faker faker = new Faker();
    private static String datetimeabc = null;
    private static int counter = 0;
    private Dictionary dicttoread = new Hashtable();
    private Logger logger = LogManager.getLogger(UserActions.class);

    /**
     * Capture screenshot
     *
     * @param name Screen name
     */
    protected void captureScreen(String name) throws Exception {
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE.FILE);
        String dest = System.getProperty("user.dir") + "\\Screenshots\\" + name + ".png";
        FileUtils.copyFile(file, new File(dest));
    }

    /**
     * Capture screens in log
     *
     * @param screenShotName screenshotName
     * @return destinationPath
     * @throws IOException exception
     */
    private String capture(String screenShotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = System.getProperty("user.dir") + "\\Reports\\Screens\\" + screenShotName + ".png";
        File destination = new File(dest);
        FileUtils.copyFile(source, destination);
        return dest;
    }

    /**
     * Fluent wait
     *
     * @param element element
     * @param timeout timeoutInMilli
     */
    private void fluentWait(MobileElement element, int timeout) {
        try {
            Wait wait = new FluentWait(driver)
                    .withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(5))
                    .ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions.elementToBeClickable(element));

        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Click on element
     *
     * @param element element
     */
    public void click(MobileElement element) {
        try {
            fluentWait(element, 10);
            element.click();
            logger.info("Clicked on element: " + element);
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    protected void android_ScrollClick(String scrollableListId, String selectionText) {
        ((AndroidDriver)driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)."
                + "resourceId(\"" + scrollableListId + "\"))"
                + ".setAsHorizontalList().scrollIntoView(new UiSelector().text(\"" + selectionText + "\"))").click();
    }

    /**
     * Click on element with timeout
     *
     * @param element element
     * @param timeOut timeOut
     */
    public void click(MobileElement element, int timeOut) {
        try {
            fluentWait(element, timeOut);
            element.click();
            logger.info("Clicked on element: " + element);
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * click on element
     *
     * @param xpath element
     */
    protected void clickByXpath(String xpath) {
        try {
            driver.findElementByXPath(xpath).click();
            logger.info("Clicked on element: " + xpath);
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }


    /**
     * Enter value in text field
     *
     * @param element element
     * @param value   value
     */
    protected void enter(MobileElement element, String value) {
        try {
            fluentWait(element, 10);
            element.click();
            element.setValue(value);
            logger.info("Entered value: " + value + "on element: " + element);
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Write value in text field
     *
     * @param element element
     * @param value   value
     */
    public void write(MobileElement element, String value) {
        try {
            fluentWait(element, 10);
            element.setValue(value);
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Element is displaying
     *
     * @param element element
     * @return boolean
     */
    public boolean isDisplayed(MobileElement element) throws Exception {
        if (element.isDisplayed()) {
            logInfo(element + ": element is Displayed");
            return true;
        } else {
            logFail("Element is not displayed");
        }
        return false;
    }

    /**
     * Element is enabled
     *
     * @param element element
     * @return boolean
     */
    protected boolean isEnabled(MobileElement element) {
        if (element.isEnabled()) {
            logInfo(element + ": element is Enabled");
            return true;
        }
        return false;
    }

    /**
     * Is element visible
     *
     * @param xpath xpath String
     * @return boolean
     */
    protected boolean isElementVisible(String xpath) {
        if (driver.findElementsByXPath(xpath).size() != 0) {
            logInfo(xpath + ": element is Visible");
            return true;
        }
        return false;
    }

    /**
     * is present
     *
     * @param elements elements
     * @return boolean
     */
    protected boolean isPresent(List<MobileElement> elements) {
        if (elements.size() != 0) {
            logInfo(elements + ": element is Present");
            return true;
        }
        return false;
    }

    /**
     * Get page source
     *
     * @return pageSource
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Get mobile element
     *
     * @param mobileElement mobileElement
     * @param type          typeOf element
     * @return element
     * @throws Exception exception
     */
    public MobileElement getMobileElement(String mobileElement, String type) throws Exception {
        MobileElement element = null;
        switch (type) {
            case "xpath":
                element = (MobileElement) driver.findElementByXPath(mobileElement);
                break;
            case "id":
                element = (MobileElement) driver.findElementById(mobileElement);
                break;
            case "name":
                element = (MobileElement) driver.findElementByName(mobileElement);
                break;
            case "access_id":
                element = (MobileElement) driver.findElementByAccessibilityId(mobileElement);
                break;
        }
        if (element == null) {
            logger.error("Mobile element not found");
            throw new Exception(mobileElement + "not found");
        }
        return element;
    }

    /**
     * Verify text content
     *
     * @param actual   actual
     * @param expected expected
     */
    public void verifyTextContent(String actual, String expected) {
        Assert.assertEquals(actual, expected);
    }

    /**
     * Get Text content
     *
     * @param containText contain text
     * @return text
     */
    public String getTextContent(String containText) {
        return driver.findElementByXPath("//*[contains(text(),'" + containText + "')]").getText();
    }

    /**
     * Is Text present
     *
     * @param containsText contains text
     * @return boolean
     */
    public boolean isTextPresent(String containsText) throws Exception {
        if (driver.getPageSource().contains(containsText)) {
            return true;
        } else {
            logFail("Text is not present");
        }
        return false;
    }

    /**
     * Press Back
     */
    public void pressBack() {
        ((AndroidDriver)driver).pressKey(new KeyEvent(AndroidKey.BACK));
        logInfo("Press Back");
    }

    /**
     * Swipe Down
     */
    public void swipeDown() {
        driver.executeScript("mobile:scroll", ImmutableMap.of("direction", "down"));
        logInfo("Swipe Down");
    }

    /**
     * Swipe Up
     */
    public void swipeUP() {
        driver.executeScript("mobile:scroll", ImmutableMap.of("direction", "up"));
        logInfo("Swipe Up");
    }

    /**
     * Accept Alert
     */
    public void acceptAlert() {
        driver.executeScript("mobile:acceptAlert");
        logInfo("Accept Alert");
    }

    /**
     * Dismiss Alert
     */
    public void dismissAlert() {
        driver.executeScript("mobile:dismissAlert");
        logInfo("Dismiss Alert");
    }

    /**
     * Wait untill progressbar
     *
     * @param timeout timeoutInMilli
     */
    protected void waitUntilProgress(int timeout) {
        try {
            if (driver.findElementsById("au.com.wgtech.uat:id/progressBar1").size() != 0) {
                Wait wait = new FluentWait(driver)
                        .withTimeout(Duration.ofSeconds(timeout))
                        .pollingEvery(Duration.ofMillis(5))
                        .ignoring(NoSuchElementException.class);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("au.com.wgtech.uat:id/progressBar1")));
            }
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Wait untill progressbar home
     *
     * @param timeout timeoutInMilli
     */
    protected void waitUntilProgressHome(int timeout) {
        try {
            if (driver.findElementsById("au.com.wgtech.uat:id/progressbar_home").size() != 0) {
                Wait wait = new FluentWait(driver)
                        .withTimeout(Duration.ofSeconds(timeout))
                        .pollingEvery(Duration.ofMillis(5))
                        .ignoring(NoSuchElementException.class);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("au.com.wgtech.uat:id/progressbar_home")));
            }
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Wait untill progressbar home
     *
     * @param timeout timeoutInMilli
     */
    protected void waitUntilVisionAppear(int timeout) {
        try {
            if (driver.findElementsById("au.com.wgtech.uat:id/progressbar_home").size() != 0) {
                Wait wait = new FluentWait(driver)
                        .withTimeout(Duration.ofSeconds(timeout))
                        .pollingEvery(Duration.ofMillis(5))
                        .ignoring(NoSuchElementException.class);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("au.com.wgtech.uat:id/race_details_progress")));
            }
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Get text from the element
     *
     * @param element element
     * @return string
     */
    protected String getText(MobileElement element) {
        try {
            String value;
            fluentWait(element, 10);
            value = element.getText();
            return value;
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
        return null;
    }

    /**
     * Get text from the element
     *
     * @param element element
     * @return string
     */
    protected String getTextByXpath(String element) {
        try {
            String value;
            value = driver.findElementByXPath(element).getText();
            return value;
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
        return null;
    }

    /**
     * Get attribute text from the element
     *
     * @param element element
     * @return string
     */
    protected String getAttribute(MobileElement element) {
        try {
            String value;
            fluentWait(element, 10);
            value = element.getAttribute("text");
            return value;
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
        return null;
    }


    /**
     * Longpress key
     *
     * @param element element
     */
    public void longPress(MobileElement element) {
        try {
            fluentWait(element, 10);
            TouchAction myaction = new TouchAction(driver);
            myaction.longPress(ElementOption.element(element)).perform();
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Hide keyboard
     */
    protected void hideKeyboard() {
        driver.hideKeyboard();
    }

    /**
     * Scroll to specific location
     *
     * @param element element
     * @param value   location
     */
    public void scrollToLocation(MobileElement element, int value) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            HashMap<String, Double> scrollElement = new HashMap<String, Double>();
            scrollElement.put("startX", 0.50);
            scrollElement.put("startY", 0.95);
            scrollElement.put("endX", 0.50);
            scrollElement.put("endY", 0.01);
            scrollElement.put("duration", 3.0);
            js.executeScript("mobile: swipe", scrollElement);
        } catch (ElementNotVisibleException e) {
            logger.error("Element not visible", e);
        }
    }

    /**
     * Click on back button
     */
    public void clickBackButton() {
        driver.navigate().back(); //Closes keyboard
    }

    /**
     * Threaded sleep
     *
     * @param time time
     */
    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Log screen capture
     *
     * @param screenName screenName
     * @throws Exception exception
     */
    public void logScreenCapture(String screenName) throws Exception {
        String screenShotPath = capture(screenName);
        ExtentTestManager.getTest().log(LogStatus.PASS,
                screenName + " Verified");
        ExtentTestManager.getTest().log(LogStatus.INFO,
                screenName + ExtentTestManager.getTest().addScreenCapture(screenShotPath));
    }

    /**
     * Log allure
     *
     * @param message log message
     */
    @Step("{0}")
    private void log(String message) {
        logger.info(message);
    }

    /**
     * Log Pass
     *
     * @param details logDetails
     */
    protected void logPass(String details) throws Exception {
        ExtentTestManager.getTest().log(LogStatus.PASS, details);
        log(details);
    }

    /**
     * Tap click
     *
     * @param x x axis
     * @param y y axis
     */
    protected void tapClick(int x, int y) {
        TouchAction action = new TouchAction(driver);
        action.tap(PointOption.point(x, y));
        action.perform();
    }

    /**
     * Get coordinate by id
     *
     * @param byId Byid
     * @return point
     */
    public Point getCoordinates(String byId) {
        MobileElement element = (MobileElement) driver.findElementById(byId);
        Point location = element.getLocation();
        System.out.println(location);
        return location;
    }

    /**
     * Log fail
     *
     * @param details logDetails
     */
    protected void logFail(String details) throws Exception {
        String screenShotPath = capture(details);
        ExtentTestManager.getTest().log(LogStatus.FAIL, details);
        ExtentTestManager.getTest().log(LogStatus.INFO,
                details + ExtentTestManager.getTest().addScreenCapture(screenShotPath));
        log(details);
        throw new Exception(details);
    }

    /**
     * Log info
     *
     * @param details logDetails
     */
    protected void logInfo(String details) {
        ExtentTestManager.getTest().log(LogStatus.INFO, details);
        log(details);
    }

    /**
     * Wait for page to get loaded
     *
     * @param id locatorId
     */
    private void waitForPageToLoad(WebElement id) {
        WebDriverWait wait = new WebDriverWait(driver, 35);
        wait.until(ExpectedConditions.elementToBeClickable(id));
    }

    /**
     * Wait for element to disappear
     *
     * @param id locatorId
     */
    public void waitForElementToDisAppear(String id) {
        WebDriverWait wait = new WebDriverWait(driver, 25);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
    }

    /**
     * Wait for element to be displayed
     *
     * @param arg element
     * @return webElement
     */
    public WebElement waitForElement(MobileElement arg) {
        waitForPageToLoad(arg);
        WebElement el = arg;
        return el;
    }

    public void outputIfMatchPassOrFail(String expectedValue, String actualValue) {
        String result;
        if (expectedValue.trim().contains(actualValue.trim())) {
            result = "(PASS)";
        } else {
            result = "(FAIL)";
        }
        logger.info("Verifying Expected Value Matches Actual Value:");
        logger.info("\t* Expected Value: " + expectedValue);
        logger.info("\t* Actual Value: " + actualValue);
        logger.info("===> " + result);
    }

    /**
     * Check List is sorted
     *
     * @param ListToSort lists
     * @return boolean
     */
    public boolean checkListIsSorted(List<String> ListToSort) {

        if (ListToSort.size() > 0) {
            try {
                if (Ordering.natural().isOrdered(ListToSort)) {
                    logPass("Check sorting ,List is sorted");
                    return true;
                } else {
                    logFail("Check Sorting,List is not sorted");
                    return false;
                }
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            logInfo("There are no elements in the list");
        }
        return false;
    }

    protected String generateRandomFirstName() {
        String name = "testauto" + faker.name().firstName();
        logger.info("FirstName: " + name);
        return name;
    }

    protected String generateRandomLastName() {
        String name = faker.name().lastName();
        logger.info("LastName: " + name);
        return name;
    }

    protected String generateRandomUserName() {
        String name = RandomStringUtils.randomAlphabetic(6);
        logger.info("Username: " + name);
        return name;
    }

    /**
     * Generate random strings
     *
     * @return string
     */
    public String generateRandomString() {
        String name = RandomStringUtils.randomAlphabetic(5);
        logger.info(name);
        return name;
    }

    /**
     * Generate random email
     *
     * @return string
     */
    protected String generateRandomEmail() {
        String email = "testauto" + faker.internet().emailAddress();
        logger.info("EmailAddress: " + email);
        return email;
    }

    /**
     * Generate random mobile no
     *
     * @return string
     */
    protected String generateRandomMobileNo() {
        String mobNo = "0" + RandomStringUtils.randomNumeric(9);
        logger.info("MobileNo: " + mobNo);
        return mobNo;
    }

    /**
     * Touch Actions
     *
     * @param a1   axis 1
     * @param b1   axis 2
     * @param a2   axis 3
     * @param b2   axis 4
     * @param time time
     */
    private void touchActions(int a1, int b1, int a2, int b2, int time) {
        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(a1, b1)).
                waitAction(WaitOptions.waitOptions(Duration.ofMillis(time))).
                moveTo(PointOption.point(a2, b2)).release();
        touchAction.perform();
    }

    /**
     * Swipe with axix
     *
     * @param X    x axis
     * @param Y    y axis
     * @param X1   x1 axis
     * @param Y1   y1 axis
     * @param time timeInMilli
     */
    protected void swipeAxis(int X, int Y, int X1, int Y1, int count, int time) {
        for (int i = 0; i < count; i++) {
            touchActions(X, Y, X1, Y1, time);
        }
    }

    /**
     * tap to element for 250sec
     *
     * @param androidElement element
     */
    public void tapByElement(MobileElement androidElement) {
        new TouchAction(driver)
                .tap(TapOptions.tapOptions().withElement(ElementOption.element(androidElement)))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(250))).perform();
    }

    /**
     * Tap by coordinates
     *
     * @param x x
     * @param y y
     */
    public void tapByCoordinates(int x, int y) {
        new TouchAction(driver)
                .tap(PointOption.point(x, y))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(250))).perform();
    }

    /**
     * Press by element
     *
     * @param element element
     * @param seconds time
     */
    public void pressByElement(MobileElement element, long seconds) {
        new TouchAction(driver)
                .press(ElementOption.element(element))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds)))
                .release()
                .perform();
    }

    /**
     * Press by co-ordinates
     *
     * @param x       x
     * @param y       y
     * @param seconds time
     */
    public void pressByCoordinates(int x, int y, long seconds) {
        new TouchAction(driver)
                .press(PointOption.point(x, y))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds)))
                .release()
                .perform();
    }

    /**
     * Horizontal swipe by percentage
     *
     * @param startPercentage  start
     * @param endPercentage    end
     * @param anchorPercentage anchor
     */
    public void horizontalSwipeByPercentage(double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = driver.manage().window().getSize();
        int anchor = (int) (size.height * anchorPercentage);
        int startPoint = (int) (size.width * startPercentage);
        int endPoint = (int) (size.width * endPercentage);
        new TouchAction(driver)
                .press(PointOption.point(startPoint, anchor))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(endPoint, anchor))
                .release().perform();
    }

    /**
     * Veritical swipe by percentage
     *
     * @param startPercentage  start
     * @param endPercentage    end
     * @param anchorPercentage anchor
     */
    public void verticalSwipeByPercentages(double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = driver.manage().window().getSize();
        int anchor = (int) (size.width * anchorPercentage);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * endPercentage);

        new TouchAction(driver)
                .press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(anchor, endPoint))
                .release().perform();
    }

    /**
     * Swipe by elements
     *
     * @param startElement start
     * @param endElement   end
     */
    public void swipeByElements(MobileElement startElement, MobileElement endElement) {
        int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
        int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);

        int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
        int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);

        new TouchAction(driver)
                .press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(endX, endY))
                .release().perform();
    }

    /**
     * Multi touch by element
     *
     * @param androidElement element
     */
    public void multiTouchByElement(MobileElement androidElement) {
        TouchAction press = new TouchAction(driver)
                .press(ElementOption.element(androidElement))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .release();

        new MultiTouchAction(driver)
                .add(press)
                .perform();
    }

    /**
     * Swipe touch (UP,DOWN,LEFT,RIGHT)
     *
     * @param direction direction
     * @param count     count
     */
    protected void swipe(String direction, int count, int time) {
        String dire = direction;
        try {
            if (dire.equalsIgnoreCase("LEFT")) {
                for (int i = 0; i < count; i++) {
                    Dimension size = driver.manage()
                            .window().getSize();
                    int startx = (int) (size.width * 0.8);
                    int endx = (int) (size.width * 0.20);
                    int starty = size.height / 2;
                    touchActions(startx, starty, endx, starty, time);
                    logger.info("Swipe Left");
                }
            } else if (dire.equalsIgnoreCase("RIGHT")) {
                for (int j = 0; j < count; j++) {
                    Dimension size = driver.manage()
                            .window().getSize();
                    int endx = (int) (size.width * 0.8);
                    int startx = (int) (size.width * 0.20);
                    int starty = size.height / 2;
                    touchActions(startx, starty, endx, starty, time);
                    logger.info("Swipe Right");
                }
            } else if (dire.equalsIgnoreCase("UP")) {
                for (int j = 0; j < count; j++) {
                    Dimension size = driver.manage().window().getSize();
                    int starty = (int) (size.height * 0.80);
                    int endy = (int) (size.height * 0.20);
                    int startx = size.width / 2;
                    touchActions(startx, starty, startx, endy, time);
                    logger.info("Swipe Up");
                }
            } else if (dire.equalsIgnoreCase("DOWN")) {
                for (int j = 0; j < count; j++) {
                    Dimension size = driver.manage().window().getSize();
                    int starty = (int) (size.height * 0.80);
                    int endy = (int) (size.height * 0.20);
                    int startx = size.width / 2;
                    touchActions(startx, endy, startx, starty, time);
                    logger.info("Swipe Down");
                }
            }
        } catch (Exception e) {
            logger.error("Not able to perform swipe operation", e);
        }
    }

    public JSONObject getUserData(int threadID) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "/" +
                    "credentials.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray msg = (JSONArray) jsonObject.get("credentials");
            JSONObject a = (JSONObject) msg.get(threadID);
            logger.info(msg.get(threadID));
            return a;

        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * Get data from csv
     *
     * @param t_testcaseName testcaseName
     * @param t_fieldName    filedName
     * @param t_instance     instance
     * @return fieldValue
     */
    protected String getData(String t_testcaseName, String t_fieldName, int t_instance) {
        try {
            int flag = 0;
            CsvReader csvreaderobj = new CsvReader("input/DataSheet.csv");
            csvreaderobj.readHeaders();
            while (csvreaderobj.readRecord()) {
                String p_testcaseName = csvreaderobj.get("TestcaseName").trim();
                String p_testcaseInstance = csvreaderobj.get("TestcaseInstance").trim();
                if ((t_testcaseName.equalsIgnoreCase(p_testcaseName)) && (t_instance == Integer.parseInt(p_testcaseInstance))) {
                    for (int i = 0; i < csvreaderobj.getColumnCount() / 2 + 1; i++) {
                        String p_field = csvreaderobj.get("Field" + i).trim();
                        String p_objectProperty = csvreaderobj.get("Value" + i).trim();
                        dicttoread.put(p_field, p_objectProperty);
                    }
                    flag = 0;
                    break;
                } else {
                    flag = 1;
                }
            }
            if (flag == 1) {
                logger.info("Not data present for testname" + t_testcaseName);
            }
        } catch (FileNotFoundException ef) {
            logger.error(ef);
        } catch (IOException e) {
            logger.error(e);
        }
        return (String) dicttoread.get(t_fieldName);
    }

    /**
     * GetData from SQlite
     *
     * @param testCaseName test name
     * @param filedName    filed name
     * @return data
     */
    protected String getData(String testCaseName, String filedName) {
        String filed = filedName;
        String testCase = testCaseName;
        String url = "jdbc:sqlite:input/testdata";
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        String value = null;
        try {
            conn = java.sql.DriverManager.getConnection(url);
            if (conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select " + filed + " from testdata where TestcaseName = '" + testCase + "'");
                while (rs.next()) {
                    logger.info(rs.getString(filed));
                    value = rs.getString(filed);
                }
            }
            conn.close();
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return value;
    }

    /**
     * Write data to csv
     *
     * @param t_testcasename testcaseName
     * @param t_field        filedName
     * @param t_value        value
     * @param t_instance     instance
     */
    protected void writeData(String t_testcasename, String t_field, String t_value, int t_instance) {
        try {
            int flag = 0;
            int i;
            int P_valuenotduplicated = 0;
            String FileContent = null;
            CsvWriter csvOutput = new CsvWriter(new FileWriter("input\\Datasheet1.csv", false), ',');
            CsvReader csvobj = new CsvReader("input\\Datasheet.csv");
            csvobj.readHeaders();
            String FileContentPerRow = csvobj.getRawRecord();
            csvOutput.writeRecord(FileContentPerRow.split(","));
            while (csvobj.readRecord()) {
                FileContentPerRow = csvobj.getRawRecord();
                String p_testcaseName = csvobj.get("TestcaseName").trim();
                String p_testcaseInstance = csvobj.get("TestcaseInstance").trim();
                if (t_testcasename.equalsIgnoreCase(p_testcaseName) && (t_instance == Integer.parseInt(p_testcaseInstance))) {
                    flag = 1;
                    for (i = 1; i < csvobj.getColumnCount() / 2 + 1; i++) {
                        String p_filed = csvobj.get("Field" + i).trim();
                        if (p_filed.equalsIgnoreCase(t_field)) {
                            String p_field1 = csvobj.get("Value" + i).trim();
                            dicttoread.put(t_field, t_value);
                            logger.info("value for the field: " + t_field + " is updated to: " + t_value + " Successfully");
                            String stp = csvOutput.replace(FileContentPerRow, t_field + "," + p_field1, t_field + "," + t_value);
                            logger.info(stp);
                            FileContentPerRow = stp;
                            P_valuenotduplicated = 1;
                        }
                    }
                    if (P_valuenotduplicated == 0) {
                        String p_field1 = csvobj.get("Value" + (i - 1)).trim();
                        dicttoread.put(t_field, t_value);
                        String stp1 = csvOutput.replace(FileContentPerRow, p_field1, p_field1 + "," + t_field + "," + t_value);
                        logger.info(stp1);
                        FileContentPerRow = stp1;
                        logger.info("New Field: " + t_field + " is added successfully with value: " + t_value);
                    }
                    flag = 1;
                }
                csvOutput.writeRecord(FileContentPerRow.split(","));
            }
            csvOutput.flush();
            csvOutput.close();
            csvobj.close();
            RenameCsvFile("input\\Datasheet1.csv", "input\\Datasheet.csv");
            if (flag == 0) {
                logger.info("No data present for the testname");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * WriteData to Sqlite
     *
     * @param testCaseName testName
     * @param fieldName    fieldName
     * @param updatedValue updated Value
     */
    protected void writeData(String testCaseName, String fieldName, String updatedValue) {
        String filed = fieldName;
        String value = updatedValue;
        String testCase = testCaseName;
        String url = "jdbc:sqlite:input/testdata";
        String selectQuery = "select " + filed + " from testdata where TestcaseName='" + testCase + "'";
        String query = "update testdata set " + filed + "='" + value + "' where TestcaseName='" + testCase + "'";
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            conn = java.sql.DriverManager.getConnection(url);
            if (conn != null) {
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
                rs = stmt.executeQuery(selectQuery);
                while (rs.next()) {
                    System.out.println(rs.getString(filed));
                    logger.info("New Field: " + filed + " is added successfully with value: " + rs.getString(filed));
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Rename csv file
     *
     * @param source sourceFile
     * @param dest   destinationFile
     */
    private boolean RenameCsvFile(String source, String dest) {
        boolean b = false;
        try {
            boolean b1 = false;
            File file1 = new File(dest);
            System.gc();
            file1.setWritable(true);
            Thread.sleep(500);
            if (file1.exists()) {
                b1 = file1.delete();
            }
            Thread.sleep(500);
            logger.info(b1);
            File file = new File(source);
            final String st = dest;
            b = file.renameTo(new File(st));
            logger.info(b);
        } catch (Exception e) {
            logger.error(e);
        }
        return b;
    }

    /**
     * Capture image
     *
     * @param p_testcaseName testcaseName
     */
    public void captureImage(String p_testcaseName) {
        try {
            counter = counter + 1;
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(("ScreensDoc\\" + p_testcaseName + "\\" + datetimeabc + "\\" + counter + ".png")));
        } catch (Exception e) {
            logger.error("Capture screenShot failed", e);
        }
    }

    /**
     * Create image doc
     *
     * @param p_testcaseName1 testcaseName
     */
    protected void CreateImageDoc(String p_testcaseName1) {
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph p = doc.createParagraph();
            XWPFRun r = p.createRun();
            for (int i = 1; i <= counter; i++) {
                String path = "ScreensDoc\\" + p_testcaseName1 + "\\" + datetimeabc + "\\" + i + ".png";
                try (FileInputStream pic = new FileInputStream(path)) {
                    r.addBreak();
                    r.addCarriageReturn();
                    r.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, "ScreensDoc\\" + p_testcaseName1 + "\\" +
                            datetimeabc + "\\" + i + ".png", Units.toEMU(300), Units.toEMU(400));
                    FileOutputStream out = new FileOutputStream("ScreensDoc\\" + p_testcaseName1 + "\\" + datetimeabc + "\\" + p_testcaseName1 + ".docx");
                    doc.write(out);
                    pic.close();
                    out.close();
                } catch (IOException io) {
                    logger.error(io);
                }
            }
            for (int i = 1; i <= counter; i++) {
                File src1 = new File("ScreensDoc\\" + p_testcaseName1 + "\\" + datetimeabc + "\\" + i + ".png");
                deleteDir(src1);
            }
            counter = 0;
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Delete dir
     *
     * @param file fileName
     */
    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete();
            }
        }
        file.delete();
    }

    protected void SystemDateFormat() {
        String abc1 = null;
        try {
            DateFormat date = new SimpleDateFormat("yyyy.MM.dd_hh.mm");
            Date date1 = new Date();
            abc1 = date.format(date1);
            datetimeabc = "Run_" + abc1;
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * SQL server windows authentication
     */
    public void windowsAuthentication() {
        String path = System.getProperty("java.library.path");
        path = "input/sqljdbc_auth.dll" + ";" + path;
        System.setProperty("java.library.path", path);
        try {
            final Field sysPathFiled = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathFiled.setAccessible(true);
            sysPathFiled.set(null, null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Rotate screen
     *
     * @param rotation rotation
     */
    protected void rotateScreen(String rotation) {
        if (rotation.toLowerCase().equals("landscape")) {
            driver.rotate(ScreenOrientation.LANDSCAPE);
            logInfo("Screen rotated in landscape");
        } else {
            driver.rotate(ScreenOrientation.PORTRAIT);
            logInfo("Screen rotated in portrait");
        }
    }

    /**
     * Check element exists or not
     *
     * @param id id
     * @return return data
     */
    public boolean isExistsById(String id) {
        try {
            if (driver.findElementsById(id).size() != 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * Check element exists or not
     *
     * @param xpath xpath
     * @return return data
     */
    public boolean isExistsByXpath(String xpath) {
        try {
            if (driver.findElementsByXPath(xpath).size() != 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    protected void catchBlock(Exception e) {
        counter = 0;
        logger.error("Error Description", e);
        Assert.fail("TestCase Failed", e);
    }
}
