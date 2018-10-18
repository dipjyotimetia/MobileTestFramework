package com.core;

import io.appium.java_client.AppiumDriver;

public class DriverManager extends AppiumController {

    public AppiumDriver driver;

    public DriverManager() {
        this.driver = super.getDriver();
    }

}
