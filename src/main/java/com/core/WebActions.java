package com.core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebActions extends DriverManager {
    private static WebDriverWait wait;
    private static JavascriptExecutor jsExec;

    //Get the driver
    public void setDriver() {
        wait = new WebDriverWait(webDriver, 10);
        jsExec = (JavascriptExecutor) webDriver;
    }

    //Wait for JQuery Load
    public void waitForJQueryLoad() {
        //Wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) webDriver)
                .executeScript("return jQuery.active") == 0);

        //Get JQuery is Ready
        boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");

        //Wait JQuery until it is Ready!
        if (!jqueryReady) {
            System.out.println("JQuery is NOT Ready!");
            //Wait for jQuery to load
            wait.until(jQueryLoad);
        } else {
            System.out.println("JQuery is Ready!");
        }
    }


    //Wait for Angular Load
    public void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;

        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
                .executeScript(angularReadyScript).toString());

        //Get Angular is Ready
        boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if (!angularReady) {
            System.out.println("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            wait.until(angularLoad);
        } else {
            System.out.println("ANGULAR is Ready!");
        }
    }

    //Wait Until JS Ready
    public void waitUntilJSReady() {
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;

        //Wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady = (Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            System.out.println("JS in NOT Ready!");
            //Wait for Javascript to load
            wait.until(jsLoad);
        } else {
            System.out.println("JS is Ready!");
        }
    }

    //Wait Until JQuery and JS Ready
    public void waitUntilJQueryReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;

        //First check that JQuery is defined on the page. If it is, then wait AJAX
        Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
        if (jQueryDefined == true) {
            //Pre Wait for stability (Optional)
            sleep(20);

            //Wait JQuery Load
            waitForJQueryLoad();

            //Wait JS Load
            waitUntilJSReady();

            //Post Wait for stability (Optional)
            sleep(20);
        } else {
            System.out.println("jQuery is not defined on this site!");
        }
    }

    //Wait Until Angular and JS Ready
    public void waitUntilAngularReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;

        //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
        Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
        if (!angularUnDefined) {
            Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
            if (!angularInjectorUnDefined) {
                //Pre Wait for stability (Optional)
                sleep(20);

                //Wait Angular Load
                waitForAngularLoad();

                //Wait JS Load
                waitUntilJSReady();

                //Post Wait for stability (Optional)
                sleep(20);
            } else {
                System.out.println("Angular injector is not defined on this site!");
            }
        } else {
            System.out.println("Angular is not defined on this site!");
        }
    }

    //Wait Until JQuery Angular and JS is ready
    public void waitJQueryAngular() {
        waitUntilJQueryReady();
        waitUntilAngularReady();
    }

    public static void sleep(Integer seconds) {
        long secondsLong = (long) seconds;
        try {
            Thread.sleep(secondsLong);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Find element with FluentWait
    private WebElement findElement(final By locator, final int timeoutSeconds) {
        //FluentWait Decleration
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(timeoutSeconds, TimeUnit.SECONDS) //Set timeout
                .pollingEvery(100, TimeUnit.MILLISECONDS) //Set query/check/control interval
                .withMessage("Timeout occured!") //Set timeout message
                .ignoring(NoSuchElementException.class); //Ignore NoSuchElementException

        //Wait until timeout period and when an element is found, then return it.
        return wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return driver.findElement(locator);
            }
        });

        //This is lambda expression of below code block. It is only for JAVA 8
        //return wait.until((WebDriver webDriver) -> driver.findElement(locator));
    }

    public void clickWhenReady(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

}
