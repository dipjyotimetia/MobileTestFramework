package com.core;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.github.javafaker.Faker;
import com.google.common.collect.Ordering;
import com.relevantcodes.extentreports.LogStatus;
import com.reporting.ExtentReports.ExtentTestManager;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.*;

public class UserActions extends DriverManager {

    private static String datetimeabc = null;
    private static int Counter = 0;
    private static String abc1 = null;
    private Dictionary dicttoread = new Hashtable();
    private static String _dbusername;
    private static String _dbpassword;
    private static String _dburl;

    private static final Faker faker = new Faker();
    private Logger logger = LogManager.getLogger(UserActions.class);

    /**
     * Capture screenshot
     *
     * @param name Screen name
     * @throws Exception
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
     * @throws IOException Exception
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

    /**
     * Enter value in text field
     *
     * @param element element
     * @param value   value
     */
    public void enter(MobileElement element, String value) {
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
    public boolean isDisplayed(MobileElement element) {
        try {
            if (element.isDisplayed()) {
                return true;
            } else {
                logInfo("Element is Not displayed");
                return false;
            }
        } catch (Exception e) {
            logger.error("Element not visible", e);
        }
        return false;
    }

    public Dimension getDimension(MobileElement element){
        return element.getSize();
    }

    public Point getLocation(MobileElement element) {
        return element.getLocation();
    }

    public String getDescription(MobileElement element) {
        String elementText = "";
        try {
            elementText = element.getText();
            if (!elementText.isEmpty()) {
                return elementText;
            } else {
                String elementTag = element.getTagName();
                if (elementTag != null) {
                    return elementTag + " at " + element.getCoordinates();
                } else {
                    return "OldElement at " + element.getCoordinates();
                }
            }
        } catch (Exception e) {
            logger.warn("Could not get the element description");
            logger.warn(e.getMessage());
        }

        return elementText;
    }

    /**
     * Element is enabled
     *
     * @param element element
     * @return boolean
     */
    public boolean isEnabled(MobileElement element) {
        try {
            if (element.isEnabled()) {
                return true;
            } else {
                logFail("Element Not enabled");
                throw new Exception("Element is not displayed");
            }
        } catch (Exception e) {
            logger.error("Element not visible", e);
        }
        return false;
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
     * Log screen capture
     *
     * @param screenName screenName
     * @throws Exception Exception
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
        captureScreen(details);
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
        WebDriverWait wait = new WebDriverWait(driver, 15);
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
        String name = faker.name().firstName();
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
        String email = faker.internet().emailAddress();
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
            TouchAction touchAction = new TouchAction(driver);
            touchAction.press(PointOption.point(X, Y)).
                    waitAction(WaitOptions.waitOptions(Duration.ofMillis(time))).
                    moveTo(PointOption.point(X1, Y1)).release();
            touchAction.perform();
        }
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
                    TouchAction touchAction = new TouchAction(driver);
                    touchAction.press(PointOption.point(startx, starty)).
                            waitAction(WaitOptions.waitOptions(Duration.ofMillis(time))).
                            moveTo(PointOption.point(endx, starty)).release();
                    touchAction.perform();
                    logger.info("Swipe Left");
                }
            } else if (dire.equalsIgnoreCase("RIGHT")) {
                for (int j = 0; j < count; j++) {
                    Dimension size = driver.manage()
                            .window().getSize();
                    int endx = (int) (size.width * 0.8);
                    int startx = (int) (size.width * 0.20);
                    int starty = size.height / 2;
                    TouchAction touchAction = new TouchAction(driver);
                    touchAction.press(PointOption.point(startx, starty)).
                            waitAction(WaitOptions.waitOptions(Duration.ofMillis(time))).
                            moveTo(PointOption.point(endx, starty)).release();
                    touchAction.perform();
                    logger.info("Swipe Right");
                }
            } else if (dire.equalsIgnoreCase("UP")) {
                for (int j = 0; j < count; j++) {
                    Dimension size = driver.manage().window().getSize();
                    int starty = (int) (size.height * 0.80);
                    int endy = (int) (size.height * 0.20);
                    int startx = size.width / 2;
                    TouchAction touchAction = new TouchAction(driver);
                    touchAction.press(PointOption.point(startx, starty)).
                            waitAction(WaitOptions.waitOptions(Duration.ofMillis(time))).
                            moveTo(PointOption.point(startx, endy)).release();
                    touchAction.perform();
                    logger.info("Swipe Up");
                }
            } else if (dire.equalsIgnoreCase("DOWN")) {
                for (int j = 0; j < count; j++) {
                    Dimension size = driver.manage().window().getSize();
                    int starty = (int) (size.height * 0.80);
                    int endy = (int) (size.height * 0.20);
                    int startx = size.width / 2;
                    TouchAction touchAction = new TouchAction(driver);
                    touchAction.press(PointOption.point(startx, endy)).
                            waitAction(WaitOptions.waitOptions(Duration.ofMillis(time))).
                            moveTo(PointOption.point(startx, starty)).release();
                    touchAction.perform();
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
     * @throws Exception Exception
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
     * Write data to csv
     *
     * @param t_testcasename testcaseName
     * @param t_field        filedName
     * @param t_value        value
     * @param t_instance     instance
     * @throws IOException IOException
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
     * Rename csv file
     *
     * @param source sourceFile
     * @param dest   destinationFile
     * @return
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
     * @throws IOException Exception
     */
    public void captureImage(String p_testcaseName) throws IOException {
        try {
            Counter = Counter + 1;
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(("ScreensDoc\\" + p_testcaseName + "\\" + datetimeabc + "\\" + Counter + ".png")));
        } catch (Exception e) {
            logger.error("Capture screenShot failed", e);
        }
    }

    /**
     * Create image doc
     *
     * @param p_testcaseName1 testcaseName
     * @throws IOException            IoException
     * @throws InvalidFormatException invalidFormatException
     */
    protected void CreateImageDoc(String p_testcaseName1) {
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph p = doc.createParagraph();
            XWPFRun r = p.createRun();
            for (int i = 1; i <= Counter; i++) {
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
            for (int i = 1; i <= Counter; i++) {
                File src1 = new File("ScreensDoc\\" + p_testcaseName1 + "\\" + datetimeabc + "\\" + i + ".png");
                deleteDir(src1);
            }
            Counter = 0;
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
    public void WindowsAuthentication() {
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
     * Execute SQL query
     *
     * @param query sqlQuery
     * @return result string
     * @throws SQLException SQLException
     */
    public String ExecuteQuery(String query) throws SQLException {
        String resultValue = "";
        String columnName = "";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            logger.error("Class not found", e);
        }
        try (Connection connection = java.sql.DriverManager.getConnection(_dburl, _dbusername, _dbpassword)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            try {
                                if (rs.getString(i).toString() == null && i != columnCount) {
                                }
                            } catch (NullPointerException e) {
                                resultValue = "NULL";
                                logger.info("column name:" + columnName + "|" + "Column value:" + resultValue);
                                continue;
                            }
                            columnName = rsmd.getColumnName(i);
                            resultValue = rs.getString(i).toString();
                            logger.info("column name:" + columnName + "|" + "Column value:" + resultValue);
                        }
                    }
                }
                connection.close();
            } catch (SQLException sq) {
                logger.error(sq);
            }
        } catch (SQLException sq) {
            logger.error(sq);
        }
        return resultValue;
    }

    protected void catchBlock(Exception e) {
        Counter = 0;
        logger.error("Error Description", e);
        Assert.fail("TestCase Failed", e);
    }

    protected String readData(String data) {
        Properties prop = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("credential.properties");
            prop.load(inputStream);
            return prop.getProperty(data);
        } catch (IOException ex) {
            logger.error(ex);
        }
        return null;
    }
}
