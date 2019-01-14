package com.core;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

public class DriverManager extends AppiumController {

    public AppiumDriver driver;
    public WebDriver webDriver;

    public DriverManager() {
        this.driver = super.getDriver();
        this.webDriver = super.getWebDriver();
    }

}
